package nl.profit4cloud.hero.question;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String questionType;

    @Column(length = 4000)
    private String question;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> answerOptions;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> correctAnswer;
}
