package com.checkers.web.repository;

import com.checkers.web.domain.Score;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    List<Score> findAllByOrderByAdvantageDesc();

}
