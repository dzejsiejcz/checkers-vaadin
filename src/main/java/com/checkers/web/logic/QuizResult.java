package com.checkers.web.logic;

public class QuizResult {

    private boolean isCorrectAnswered;
    private boolean isAnswered = true;

//    public static QuizResult INSTANCE_QUIZ = new QuizResult();

    public QuizResult() {
    }

    public boolean isCorrectAnswered() {
        return isCorrectAnswered;
    }

    public void setCorrectAnswered(boolean correctAnswered) {
        isCorrectAnswered = correctAnswered;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
