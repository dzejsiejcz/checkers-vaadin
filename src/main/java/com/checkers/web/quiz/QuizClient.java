package com.checkers.web.quiz;

import com.checkers.web.domain.QuestionQuizDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class QuizClient {

    private final RestTemplate restTemplate;

    public QuizClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<QuestionQuizDto> get20Questions() {
        QuestionQuizDto[] questionsResponse = restTemplate.getForObject(
                "https://the-trivia-api.com/api/questions?categories=food" +
                        "_and_drink,music,geography,science,general_knowledge,society" +
                        "_and_culture&limit=20&region=PL&difficulty=easy",
                QuestionQuizDto[].class
        );
        return Optional.ofNullable(questionsResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public List<QuestionQuizDto> getOneQuestion() {
        QuestionQuizDto[] questionResponse = restTemplate.getForObject(
                "https://the-trivia-api.com/api/questions?categories=" +
                        "food_and_drink,music,geography,science,general_knowledge," +
                        "society_and_culture&limit=1&region=PL&difficulty=easy",
                QuestionQuizDto[].class
        );
        return Optional.ofNullable(questionResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}
