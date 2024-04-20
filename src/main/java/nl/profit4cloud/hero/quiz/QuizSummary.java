package nl.profit4cloud.hero.quiz;

public record QuizSummary(Long timeInMillis, int score, long correctQuestions, int contestantId) {
}
