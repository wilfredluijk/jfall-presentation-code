package nl.profit4cloud.hero.quiz;


public record QuizEndMessage(String uid, QuizState state, QuizSummary payload) {
}
