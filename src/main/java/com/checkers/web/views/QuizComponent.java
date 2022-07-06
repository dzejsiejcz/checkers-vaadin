package com.checkers.web.views;

import com.checkers.web.domain.QuestionQuizDto;
import com.checkers.web.quiz.QuizClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Random;

import static com.checkers.web.views.GameBoardView.*;

public class QuizComponent extends VerticalLayout {

    private QuizClient quizClient;

    private H3 quizTitle = new H3("Answer this question to win an additional move");
    private QuestionQuizDto questionSet;
    private int correctAnswerPosition;

    public QuizComponent(QuizClient quizClient) {
        this.quizClient = quizClient;
    }

    public QuizComponent refreshQuestion() {
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
                    removeAll();
                    try {
                        moveByComputer();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                answers.add(answer);
                j++;
            } else {
                answer.setText(questionSet.getCorrectAnswer());
                answer.addClickListener(event -> {
                    Notification notification = Notification.show("Correct answer. You have an additional move");
                    removeAll();
                });
                answers.add(answer);
            }
        }
        add(answers);
        return this;
    }



}
