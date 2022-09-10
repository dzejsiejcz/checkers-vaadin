package com.checkers.web.quiz;

import com.checkers.web.domain.QuestionQuizDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class QuizClient {

    @Value("${spring.application.api.quiz.20quest}")
    private String URL_FOR_20_QUESTS;

    @Value("${spring.application.api.quiz.1quest}")
    private String URL_FOR_1_QUEST;

    private final RestTemplate restTemplate;

    public QuizClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<QuestionQuizDto> get20Questions() {
        QuestionQuizDto[] questionsResponse = restTemplate.getForObject(
                URL_FOR_20_QUESTS,
                QuestionQuizDto[].class
        );
        return Optional.ofNullable(questionsResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public List<QuestionQuizDto> getOneQuestion() {
        QuestionQuizDto[] questionResponse = restTemplate.getForObject(
                URL_FOR_1_QUEST,
                QuestionQuizDto[].class
        );
        return Optional.ofNullable(questionResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}
