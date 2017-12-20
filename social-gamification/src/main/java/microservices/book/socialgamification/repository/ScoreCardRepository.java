package microservices.book.socialgamification.repository;

import microservices.book.socialgamification.domain.LeaderBoardRow;
import microservices.book.socialgamification.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {
    @Query("select sum(s.score) from microservices.book.gamification.domain.ScoreCard s where s.userId = :userId ")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    @Query("select NEW microservices.book.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) FROM " +
    "microservices.book.gamification.domain.ScoreCard s GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
