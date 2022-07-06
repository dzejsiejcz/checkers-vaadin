package com.checkers.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class QuestionQuizDto {

    private String correctAnswer;
    private List<String> incorrectAnswers = new ArrayList<>();
    private String question;


    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public String getQuestion() {
        return question;
    }

}
