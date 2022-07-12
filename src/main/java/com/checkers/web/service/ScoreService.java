package com.checkers.web.service;

import com.checkers.web.domain.Score;
import com.checkers.web.domain.User;
import com.checkers.web.domain.Winner;
import com.checkers.web.repository.ScoreRepository;
import com.checkers.web.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ScoreService {

    public ScoreService(ScoreRepository scoreRepository, UserRepository userRepository) {
        this.scoreRepository = scoreRepository;
        this.userRepository = userRepository;
    }

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;

    public void addScore(String userName, int advantage, Winner winner) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            Score score = new Score(user, winner, advantage);
            user.getScoreList().add(score);
            scoreRepository.save(score);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("UserType not found");
        }
    }

    public List<Score> findAllOrderByAdvantage(){
        return scoreRepository.findAllByOrderByAdvantageDesc();
    }

}
