package microservices.book.socialgamification.service;

import microservices.book.socialgamification.domain.GameStats;

public interface GameService {
    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

    GameStats retrieveStatsForUser(Long userId);
}
