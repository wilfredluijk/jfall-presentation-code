package nl.profit4cloud.hero.contestant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Entity
public class Contestant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 4000)
    private String uid;

    private String name;
    private String sessionId;

    private boolean blocked;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<Integer> questionIds;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ContestantQuestionAnswer> answeredQuestions;

    public Instant startedAt;

    public boolean connected;

    public Instant finishedAt;
    public Long totalTimeSpendInMillis;
}
