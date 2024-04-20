package nl.profit4cloud.hero.leaderboard;

import nl.profit4cloud.hero.contestant.Contestant;
import nl.profit4cloud.hero.contestant.ContestantQuestionAnswer;
import nl.profit4cloud.hero.contestant.ContestantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardService.class);

    private final ContestantRepository contestantRepository;

    public LeaderboardService(ContestantRepository leaderboardEntryRepository) {
        this.contestantRepository = leaderboardEntryRepository;
    }

    public List<LeaderboardEntry> entries() {
        return contestantRepository.findAll().stream()
                .map(this::mapToLeaderboardEntry)
                .sorted((o1, o2) -> Integer.compare(o2.score(), o1.score()))
                .toList();
    }

    public LeaderboardEntry mapToLeaderboardEntry(Contestant contestant) {
        var score = contestant.getAnsweredQuestions().stream()
                .map(ContestantQuestionAnswer::getScore)
                .reduce(0, Integer::sum);

        var correctQuestions = contestant.getAnsweredQuestions().stream()
                .filter(ContestantQuestionAnswer::isCorrect)
                .count();

        var totalQuestions = contestant.getAnsweredQuestions().size();

        return new LeaderboardEntry(contestant.getId(),
                contestant.getName(),
                contestant.getId(),
                score,
                correctQuestions,
                totalQuestions);
    }

}
