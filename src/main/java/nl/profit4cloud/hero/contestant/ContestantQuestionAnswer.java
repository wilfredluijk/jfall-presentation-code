package nl.profit4cloud.hero.contestant;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Data
public class ContestantQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer questionId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> answer;

    private Instant answeredAt;

    private int score;

    private boolean correct;

}
