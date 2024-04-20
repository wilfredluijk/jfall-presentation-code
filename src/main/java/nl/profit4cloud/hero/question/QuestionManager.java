package nl.profit4cloud.hero.question;

import nl.profit4cloud.hero.contestant.Contestant;
import nl.profit4cloud.hero.contestant.ContestantQuestionAnswer;
import nl.profit4cloud.hero.contestant.ContestantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class QuestionManager {

    private final QuestionRepository questionRepository;
    private final ContestantRepository contestantQuestionAnswerRepository;

    private static final int MAX_SCORE = 1000;
    private static final int MAX_TIME = 25000;
    private static final int MIN_SCORE = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionManager.class);

    public QuestionManager(QuestionRepository questionRepository, ContestantRepository contestantQuestionAnswerRepository) {
        this.questionRepository = questionRepository;
        this.contestantQuestionAnswerRepository = contestantQuestionAnswerRepository;
    }

    public ContestantQuestionAnswer saveContestantQuestionAnswer(Contestant contestant, QuestionAnswer questionAnswer, Instant previousAnswerInstant) {
        return handleAnsweredQuestion(contestant, questionAnswer, previousAnswerInstant);
    }

    private ContestantQuestionAnswer handleAnsweredQuestion(Contestant contestant, QuestionAnswer questionAnswer, Instant previousAnswerInstant) {
        return questionRepository.findById(questionAnswer.questionId()).map(question -> {
            var correct = answerCorrect(question.getCorrectAnswer(), questionAnswer.answer());
            var entity = new ContestantQuestionAnswer();
            entity.setQuestionId(questionAnswer.questionId());
            entity.setAnsweredAt(Instant.now());

            var score = calculateScore(correct, previousAnswerInstant, entity.getAnsweredAt());
            entity.setScore(score);

            entity.setAnswer(questionAnswer.answer());
            entity.setCorrect(correct);
            return entity;
        }).orElseThrow();
    }

    private int calculateScore(boolean correct, Instant previousAnswerInstant, Instant answeredAt) {
        var startInMillis = previousAnswerInstant.toEpochMilli();
        var endInMillis = answeredAt.toEpochMilli();
        var time = endInMillis - startInMillis;
        if (correct) {
            if (time > MAX_TIME) {
                return MIN_SCORE;
            }
            var maxScorePercentage = (double) time / ((double) MAX_TIME) * 100;
            var score = (double) MAX_SCORE * (100 - maxScorePercentage) / 100;

            LOGGER.info("Score answer, time: {}, maxScorePercentage: {}, score: {}", time, maxScorePercentage, score);
            return (int) Math.max(score, MIN_SCORE);
        }
        return 0;
    }

    public List<ContestantQuestionAnswer> getAnswersForContestant(Integer contestantid) {
        return contestantQuestionAnswerRepository.findById(Long.valueOf(contestantid))
                .map(Contestant::getAnsweredQuestions)
                .orElse(new ArrayList<>());
    }

    public boolean answerCorrect(List<String> correctAnswer, List<String> givenAnswer) {
        if (correctAnswer.size() != givenAnswer.size()) {
            return false;
        }

        for (int i = 0; i < correctAnswer.size(); i++) {
            if (!correctAnswer.get(i).equals(givenAnswer.get(i))) {
                return false;
            }
        }
        return new HashSet<>(givenAnswer).containsAll(correctAnswer);
    }

    public List<QuestionDto> getQuestionForQuiz() {
        return questionRepository.findAll().stream()
                .map(this::toQuestionDto)
                .toList();
    }


    private QuestionDto toQuestionDto(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getTitle(),
                question.getQuestionType(),
                question.getQuestion(),
                new ArrayList<>(question.getAnswerOptions())
        );
    }
}
