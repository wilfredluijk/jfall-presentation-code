package nl.profit4cloud.hero.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionStub {

    String json = "["
            + "{"
            + "\"title\": \"Java Primitive Types\","
            + "\"questionType\": \"sorting\","
            + "\"question\": \"Sort the following Java primitive data types from smallest to largest storage (first to last):\","
            + "\"answerOptions\": [\"int\", \"long\", \"short\", \"byte\"],"
            + "\"correctAnswer\": [\"byte\", \"short\", \"int\", \"long\"]"
            + "},"
            + "{"
            + "\"title\": \"Correct Loop Syntax\","
            + "\"questionType\": \"single-choice\","
            + "\"question\": \"Which of the following is the correct syntax for a for-loop in Java?\","
            + "\"answerOptions\": [\"for int i=0; i<10; i++\", \"for (i=0; i<10; i++)\", \"for (int i=0; i<10; i++)\"],"
            + "\"correctAnswer\": [\"for (int i=0; i<10; i++)\"]"
            + "},"
            + "{"
            + "\"title\": \"Click the button 5 times!\","
            + "\"questionType\": \"click-count\","
            + "\"question\": \"Click the button exactly 5 times.\","
            + "\"answerOptions\": [\"5\"],"
            + "\"correctAnswer\": [\"5\"]"
            + "},"
            + "{"
            + "\"title\": \"Java Collection Types\","
            + "\"questionType\": \"sorting\","
            + "\"question\": \"Sort these Java collection types by typical use case from most specific (first) to most general (last):\","
            + "\"answerOptions\": [\"Set\", \"List\", \"Queue\", \"Collection\"],"
            + "\"correctAnswer\": [\"Queue\", \"Set\", \"List\", \"Collection\"]"
            + "},"
            + "{"
            + "\"title\": \"Commonly Used Java Version\","
            + "\"questionType\": \"single-choice\","
            + "\"question\": \"As of 2023, which Java version is most commonly used in enterprise environments?\","
            + "\"answerOptions\": [\"Java 8\", \"Java 11\", \"Java 17\"],"
            + "\"correctAnswer\": [\"Java 11\"]"
            + "},"
            + "{"
            + "\"title\": \"Click the button 3 times!\","
            + "\"questionType\": \"click-count\","
            + "\"question\": \"Click the button exactly 3 times to continue.\","
            + "\"answerOptions\": [\"3\"],"
            + "\"correctAnswer\": [\"3\"]"
            + "},"
            + "{"
            + "\"title\": \"Sorting Access Levels\","
            + "\"questionType\": \"sorting\","
            + "\"question\": \"Order these access levels from least accessible to most accessible:\","
            + "\"answerOptions\": [\"public\", \"private\", \"protected\"],"
            + "\"correctAnswer\": [\"private\", \"protected\", \"public\"]"
            + "},"
            + "{"
            + "\"title\": \"Default Constructor\","
            + "\"questionType\": \"single-choice\","
            + "\"question\": \"What does a default constructor in Java do?\","
            + "\"answerOptions\": [\"Initializes object properties with null or zero\", \"Is always private\", \"Cannot be overridden\"],"
            + "\"correctAnswer\": [\"Initializes object properties with null or zero\"]"
            + "},"
            + "{"
            + "\"title\": \"Click as fast as you can!\","
            + "\"questionType\": \"click-count\","
            + "\"question\": \"Challenge: Click the button as fast as you can, exactly 7 times.\","
            + "\"answerOptions\": [\"7\"],"
            + "\"correctAnswer\": [\"7\"]"
            + "},"
            + "{"
            + "\"title\": \"Exception Handling\","
            + "\"questionType\": \"single-choice\","
            + "\"question\": \"Which exception is thrown when trying to access an array with an index that is out of bounds?\","
            + "\"answerOptions\": [\"ArrayIndexOutOfBoundsException\", \"IndexOutOfBoundsException\", \"Exception\"],"
            + "\"correctAnswer\": [\"ArrayIndexOutOfBoundsException\"]"
            + "}"
            + "]";


    private final ObjectMapper objectMapper;
    private final QuestionRepository questionRepository;

    public QuestionStub(ObjectMapper objectMapper, QuestionRepository questionRepository) {
        this.objectMapper = objectMapper;
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void init() {
        try {
            TypeReference<List<Question>> typeReference = new TypeReference<>() {};
            List<Question> parsedQuestions = objectMapper.readValue(json, typeReference);
            questionRepository.saveAll(parsedQuestions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
