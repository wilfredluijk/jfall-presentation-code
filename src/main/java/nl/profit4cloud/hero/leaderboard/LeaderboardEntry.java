package nl.profit4cloud.hero.leaderboard;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record LeaderboardEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id,
        String name,
        Integer contestantId,
        Integer score,
        Long correctQuestions,
        Integer totalQuestions) {
}

