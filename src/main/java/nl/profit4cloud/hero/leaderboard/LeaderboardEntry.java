package nl.profit4cloud.hero.leaderboard;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer contestantId;
    private Integer score;
    private Long correctQuestions;
    private Integer totalQuestions;
}
