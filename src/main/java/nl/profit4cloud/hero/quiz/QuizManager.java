package nl.profit4cloud.hero.quiz;

import nl.profit4cloud.hero.contestant.Contestant;
import nl.profit4cloud.hero.contestant.ContestantQuestionAnswer;
import nl.profit4cloud.hero.contestant.ContestantRepository;
import nl.profit4cloud.hero.contestant.ContestantStartMessage;
import nl.profit4cloud.hero.question.QuestionAnswer;
import nl.profit4cloud.hero.question.QuestionDto;
import nl.profit4cloud.hero.question.QuestionManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

@Component
public class QuizManager {

    private final ContestantRepository contestantRepository;
    private final QuestionManager questionManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    public QuizManager(ContestantRepository contestantRepository, QuestionManager questionManager, ApplicationEventPublisher applicationEventPublisher) {
        this.contestantRepository = contestantRepository;
        this.questionManager = questionManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public void updateContestantName(String uid, String name) {
        contestantRepository.findByUid(uid).ifPresent(contestant -> {
            contestant.setName(name);
            contestant.setConnected(true);
            contestantRepository.save(contestant);
        });
    }


    public void addPlayer(String sessionId, String uid) {

        if (!contestantRepository.existsByUid(uid)) {
            Contestant contestant = new Contestant();
            contestant.setUid(uid);
            contestant.setSessionId(sessionId);
            contestant.setConnected(true);
        } else {
            contestantRepository.findByUid(uid).ifPresent(contestant -> {
                if (!contestant.isBlocked()) {
                    contestant.setSessionId(sessionId);
                    contestant.setConnected(true);
                    contestantRepository.save(contestant);
                }
            });
        }
    }

    public void disconnectPlayer(String sessionId) {
        contestantRepository.findBySessionId(sessionId).ifPresent(contestant -> {
            contestant.setConnected(false);
        });
    }

    public void blockPlayer(Long id) {
        contestantRepository.findById(id).ifPresent(contestant -> {
            contestant.setBlocked(true);
            contestantRepository.save(contestant);
        });
    }

    public ContestantStartMessage startGameFor(String uid) {
        AtomicReference<QuizState> state = new AtomicReference<>(QuizState.STARTED);
        var questionForQuiz = questionManager.getQuestionForQuiz();
        var answeredQuestions = new ArrayList<Integer>();

        contestantRepository.findByUid(uid).ifPresent(contestant -> {


            if (contestant.isBlocked()) {
                state.set(QuizState.BLOCKED);
            } else {


                List<ContestantQuestionAnswer> answersForContestant = questionManager.getAnswersForContestant(contestant.getId());
                contestant.setAnsweredQuestions(answersForContestant);

                if (contestant.getStartedAt() != null) {

                    if (contestant.finishedAt != null) {
                        state.set(QuizState.UNDEFINED);
                        emitQuizSummary(contestant, contestant);
                    } else {
                        answersForContestant.stream()
                                .map(ContestantQuestionAnswer::getQuestionId)
                                .forEach(answeredQuestions::add);
                    }
                } else {
                    contestant.setStartedAt(Instant.now());
                }

                var questionIds = questionForQuiz.stream().map(QuestionDto::id).collect(toList());
                contestant.setQuestionIds(questionIds);

                contestantRepository.save(contestant);
            }
        });
        return new ContestantStartMessage(uid, state.get(), questionForQuiz, answeredQuestions);
    }


    public List<Contestant> getContestantsUpdate() {
        return contestantRepository.findAll().stream()
                .filter(contestant -> !contestant.isBlocked())
                .toList();
    }

    public void handleAnswer(String sessionId, QuestionAnswer answer) {
        contestantRepository.findBySessionIdAndUid(sessionId, answer.uid()).ifPresent(contestant -> {
            if (contestant.isBlocked()) {
                return;
            }

                var previousAnswerInstant = contestant.getAnsweredQuestions().stream()
                        .map(ContestantQuestionAnswer::getAnsweredAt)
                        .max(Instant::compareTo)
                        .orElseGet(contestant::getStartedAt);

                var contestantQuestionAnswer = questionManager.saveContestantQuestionAnswer(contestant, answer, previousAnswerInstant);
                var answeredQuestions = contestant.getAnsweredQuestions();
                answeredQuestions.add(contestantQuestionAnswer);

                var size = answeredQuestions.size();
                if (size == contestant.getQuestionIds().size()) {
                    contestant.setFinishedAt(Instant.now());
                    contestant.setTotalTimeSpendInMillis(contestant.getFinishedAt().toEpochMilli() - contestant.getStartedAt().toEpochMilli());
                    contestantRepository.save(contestant);

                    emitQuizSummary(contestant, contestant);
                }
                contestantRepository.save(contestant);

        });
    }

    private void emitQuizSummary(Contestant contestant, Contestant contestantQuizState) {
        var totalScore = contestantQuizState.getAnsweredQuestions().stream()
                .map(ContestantQuestionAnswer::getScore)
                .reduce(0, Integer::sum);
        var totalCorrect = contestantQuizState.getAnsweredQuestions()
                .stream()
                .filter(ContestantQuestionAnswer::isCorrect)
                .count();
        var quizSummary = new QuizSummary(contestantQuizState.getTotalTimeSpendInMillis(), totalScore, totalCorrect, contestant.getId());
        applicationEventPublisher.publishEvent(new QuizEndMessage(contestant.getUid(), QuizState.FINISHED, quizSummary));
    }
}
