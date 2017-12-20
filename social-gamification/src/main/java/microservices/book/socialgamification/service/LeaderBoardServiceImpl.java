package microservices.book.socialgamification.service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.socialgamification.domain.LeaderBoardRow;
import microservices.book.socialgamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class LeaderBoardServiceImpl implements LeaderBoardService{
    private ScoreCardRepository scoreCardRepository;

    LeaderBoardServiceImpl(final ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        return scoreCardRepository.findFirst10();
    }
}
