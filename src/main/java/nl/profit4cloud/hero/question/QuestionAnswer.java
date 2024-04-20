package nl.profit4cloud.hero.question;

import java.util.List;

public record QuestionAnswer(String uid, Integer questionId, List<String> answer) {

}
