package nl.profit4cloud.hero.contestant;

import nl.profit4cloud.hero.question.QuestionDto;
import nl.profit4cloud.hero.quiz.QuizState;

import java.util.List;

public record ContestantStartMessage(String uid, QuizState state, List<QuestionDto> payload, List<Integer> answeredQuestions) {

}
