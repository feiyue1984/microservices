package microservices.book.socialgamification.service;

import microservices.book.socialgamification.domain.LeaderBoardRow;
import microservices.book.socialgamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTest {
    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void getCurrentLeaderBoardTest() {
        Long                 userId = 1L;
        LeaderBoardRow       leaderRow1 = new LeaderBoardRow(userId, 300L);
        List<LeaderBoardRow> expectedLeaderBoard = Collections.singletonList(leaderRow1);
        given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);
        List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();
        assertThat(leaderBoard).isEqualTo(expectedLeaderBoard);
    }
}