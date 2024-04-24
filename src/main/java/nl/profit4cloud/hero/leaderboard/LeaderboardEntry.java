package nl.profit4cloud.hero.leaderboard;

import java.util.Objects;

public class LeaderboardEntry {
    private String name;
    private Integer contestantId;
    private Integer score;
    private Long correctQuestions;
    private Integer totalQuestions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContestantId() {
        return contestantId;
    }

    public void setContestantId(Integer contestantId) {
        this.contestantId = contestantId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(Long correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaderboardEntry that = (LeaderboardEntry) o;
        return Objects.equals(name, that.name) && Objects.equals(contestantId, that.contestantId) && Objects.equals(score, that.score) && Objects.equals(correctQuestions, that.correctQuestions) && Objects.equals(totalQuestions, that.totalQuestions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(contestantId);
        result = 31 * result + Objects.hashCode(score);
        result = 31 * result + Objects.hashCode(correctQuestions);
        result = 31 * result + Objects.hashCode(totalQuestions);
        return result;
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "name='" + name + '\'' +
                ", contestantId=" + contestantId +
                ", score=" + score +
                ", correctQuestions=" + correctQuestions +
                ", totalQuestions=" + totalQuestions +
                '}';
    }
}
