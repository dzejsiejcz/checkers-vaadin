package com.checkers.web.views;

import com.checkers.web.domain.QuestionQuizDto;
import com.checkers.web.logic.QuizResult;
import com.checkers.web.quiz.QuizClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.checkers.web.views.GameBoardView.turn;

public class QuizComponent extends VerticalLayout {

    private QuizClient quizClient;

    private H3 quizTitle = new H3("Answer this question to win an additional move");
    private String question;
    private List<Button> buttons = new ArrayList<>();
    private QuestionQuizDto questionSet;
    private int correctAnswerPosition;

    public QuizComponent(QuizClient quizClient) {
        this.quizClient = quizClient;
    }

    public QuizComponent refreshQuestion(QuizResult quizResult) {
        removeAll();
        questionSet = quizClient.getOneQuestion().get(0);
        add(quizTitle);
        add(questionSet.getQuestion());
        Random random = new Random();
        correctAnswerPosition = random.nextInt(4);
        System.out.println(correctAnswerPosition);
        int j = 0;
        VerticalLayout answers = new VerticalLayout();
        for (int i = 0; i<4; i++) {
            Button answer = new Button();
            if (i != correctAnswerPosition) {
                answer.setText(questionSet.getIncorrectAnswers().get(j));
                answer.addClickListener(event -> {
                    Notification notification = Notification.show("Incorrect answer.");
                    quizResult.setAnswered(true);
                    quizResult.setCorrectAnswered(false);
                    removeAll();
                });
                answers.add(answer);
                j++;
            } else {
                answer.setText(questionSet.getCorrectAnswer());
                answer.addClickListener(event -> {
                    quizResult.setAnswered(true);
                    quizResult.setCorrectAnswered(true);
                    turn.switchTurn();
                    Notification notification = Notification.show("Correct answer. You have an additional move");
                    removeAll();
                });
                answers.add(answer);
            }
        }
        add(answers);
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getCorrectAnswerPosition() {
        return correctAnswerPosition;
    }

    public void setCorrectAnswerPosition(int correctAnswerPosition) {
        this.correctAnswerPosition = correctAnswerPosition;
    }
}
