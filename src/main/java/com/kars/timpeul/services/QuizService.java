package com.kars.timpeul.services;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.mappers.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizMapper quizMapper;

    @Autowired
    public QuizService(QuizMapper quizMapper) {
        this.quizMapper = quizMapper;
    }

    public ArticleListEntity getQuizListById(int quizId) {
        return quizMapper.findQListById(quizId);
    }

    public ArticleQuestionEntity getFirstQuestionOfQuiz(int quizId) {
        return quizMapper.findFirstQuestionByQuizId(quizId);
    }

    // quizContent.html을 위한 메소드
    public ArticleQuestionEntity getQuestionByItemIndex(int quizId, int itemIndex) {
        return quizMapper.findQuestionByItemIndex(quizId, itemIndex);
    }

    public List<ArticleQuestionEntity> getQuestionsByQuizId(int quizId) {
        return quizMapper.findQuestionsByQuizId(quizId);
    }

    public ArticleQuestionEntity getNextQuestion(int quizId, int currentItemIndex){
        return quizMapper.findNextQuestion(quizId, currentItemIndex);
    }

    public boolean checkAnswer(int quizId, int itemIndex, String userAnswer) { // quizId 파라미터 추가
        if (userAnswer == null) {
            return false;
        }
        ArticleQuestionEntity question = quizMapper.findQuestionByItemIndex(quizId, itemIndex); // quizId 전달
        if (question != null && question.getAnswer() != null) {
            return userAnswer.trim().equalsIgnoreCase(question.getAnswer().trim());
        }
        return false;
    }




}
