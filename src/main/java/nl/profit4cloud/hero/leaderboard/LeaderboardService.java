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

    private final ContestantRepository contestantRepository;

    public LeaderboardService(ContestantRepository leaderboardEntryRepository) {
        this.contestantRepository = leaderboardEntryRepository;
    }

    public List<LeaderboardEntry> entries() {
        return contestantRepository.findAll().stream()
                .map(this::mapToLeaderboardEntry)
                .sorted((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()))
                .toList();
    }

private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardService.class);
    public LeaderboardEntry mapToLeaderboardEntry(Contestant contestantQuizState) {
        LOGGER.info("add to leaderboard {}", contestantQuizState);
        var leaderboardEntry = new LeaderboardEntry();
        leaderboardEntry.setContestantId(contestantQuizState.getId());
        leaderboardEntry.setName(contestantQuizState.getName());
        leaderboardEntry.setScore(contestantQuizState.getAnsweredQuestions().stream()
                .map(ContestantQuestionAnswer::getScore)
                .reduce(0, Integer::sum));
        leaderboardEntry.setCorrectQuestions(contestantQuizState.getAnsweredQuestions()
                .stream()
                .filter(ContestantQuestionAnswer::isCorrect)
                .count());
        leaderboardEntry.setTotalQuestions(contestantQuizState.getAnsweredQuestions().size());
        LOGGER.info("add to leaderboard {}", leaderboardEntry);
        return leaderboardEntry;
    }

}
