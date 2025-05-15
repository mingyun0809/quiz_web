package com.kars.timpeul.services;

import com.kars.timpeul.entities.QuizContentEntity;
import com.kars.timpeul.entities.QuizEntity;
import com.kars.timpeul.mappers.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final QuizMapper quizMapper;

    @Autowired
    public QuizService(QuizMapper quizMapper) {
        this.quizMapper = quizMapper;
    }

    public QuizEntity getQuizListById(int quizId) {
        return quizMapper.findQListById(quizId);
    }

    public QuizContentEntity getFirstQuestionOfQuiz(int quizId) {
        return quizMapper.findFirstQuestionByQuizId(quizId);
    }

    // quizContent.html을 위한 메소드
    public QuizContentEntity getQuestionByItemIndex(int itemIndex) {
        return quizMapper.findQuestionByItemIndex(itemIndex);
    }

    public boolean checkAnswer(int itemIndex, String userAnswer){
        if (userAnswer==null){
            return false;
        }
        QuizContentEntity question=quizMapper.findQuestionByItemIndex(itemIndex);
        if (question!=null&&question.getAnswers()!=null){
            return userAnswer.trim().equalsIgnoreCase(question.getAnswers().trim());

        }
        return false;
    }




}
