package nl.profit4cloud.hero.question;

import java.util.List;

public record QuestionDto(
        Integer id,
        String title,
        String questionType,
        String question,
        List<String> answerOptions
) {
}
