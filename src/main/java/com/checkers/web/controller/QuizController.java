package com.checkers.web.controller;

import com.checkers.web.domain.QuestionQuizDto;
import com.checkers.web.quiz.QuizClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@CrossOrigin("*")
public class QuizController {

    private final QuizClient quizClient;

    public QuizController(QuizClient quizClient) {
        this.quizClient = quizClient;
    }

    @GetMapping("20questions")
    public ResponseEntity<List<QuestionQuizDto>> getQuestions() {
        List<QuestionQuizDto> questions = quizClient.get20Questions();

        questions.forEach(questionQuizDto -> System.out.println(questionQuizDto.getQuestion() +
                " correct answer: " + questionQuizDto.getCorrectAnswer() +
                " incorrect " + questionQuizDto.getIncorrectAnswers()));
        return ResponseEntity.ok(questions);
    }

    @GetMapping("1question")
    public ResponseEntity<List<QuestionQuizDto>> getQuestion() {
        List<QuestionQuizDto> questions = quizClient.getOneQuestion();

        questions.forEach(questionQuizDto -> System.out.println(questionQuizDto.getQuestion() +
                " correct answer: " + questionQuizDto.getCorrectAnswer() +
                " incorrect " + questionQuizDto.getIncorrectAnswers()));
        return ResponseEntity.ok(questions);
    }
}
