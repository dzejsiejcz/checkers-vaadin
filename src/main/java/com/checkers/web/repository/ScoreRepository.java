package com.checkers.web.repository;

import com.checkers.web.domain.Score;
import com.checkers.web.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    List<Score> findScoresByUserOrderByAdvantage(User user);

}
