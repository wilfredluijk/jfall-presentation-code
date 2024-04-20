package nl.profit4cloud.hero.contestant;

import nl.profit4cloud.hero.quiz.QuizState;

public record ContestantConnectedMessage(String uid, QuizState state, Object payload)  {
}
