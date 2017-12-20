package microservices.book.socialgamification.service;

import microservices.book.socialgamification.domain.Badge;
import microservices.book.socialgamification.domain.BadgeCard;
import microservices.book.socialgamification.domain.GameStats;
import microservices.book.socialgamification.domain.ScoreCard;
import microservices.book.socialgamification.repository.BadgeCardRepository;
import microservices.book.socialgamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class GameServiceImplTest {
    private GameServiceImpl gameService;

    @Mock
    private BadgeCardRepository badgeCardRepository;
    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(badgeCardRepository, scoreCardRepository);
    }

    @Test
    public void processFirstCorrectAttempt() {
        Long      userId     = 1L;
        Long      attemptId  = 8L;
        int       totalScore = 10;
        ScoreCard scoreCard  = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(1L)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(Collections.emptyList());
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);
        assertThat(iteration.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(iteration.getBadges()).containsOnly(Badge.FIRST_WON);
    }

    @Test
    public void processCorrectAttemptForScoreBadgeTest() {
        Long      userId        = 1L;
        Long      attemptId     = 29L;
        int       totalScore    = 100;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(createNScoreCards(10, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(firstWonBadge));
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
    }

    @Test
    public void processWrongAttemptTest() {
        Long      userId     = 1L;
        Long      attemptId  = 8L;
        int       totalScore = 10;
        ScoreCard scoreCard  = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(Collections.emptyList());
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, false);
        assertThat(iteration.getScore()).isEqualTo(0);
        assertThat(iteration.getBadges()).isEmpty();
    }

    @Test
    public void retrieveStatsForUserTest() {
        Long      userId     = 1L;
        int       totalScore = 1000;
        BadgeCard badgeCard  = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(badgeCard));
        GameStats stats = gameService.retrieveStatsForUser(userId);
        assertThat(stats.getScore()).isEqualTo(totalScore);
        assertThat(stats.getBadges()).containsOnly(Badge.SILVER_MULTIPLICATOR);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId) {
        return IntStream.range(0, n).mapToObj(i -> new ScoreCard(userId, (long) i)).collect(Collectors.toList());
    }
}