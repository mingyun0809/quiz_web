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
    public ArticleQuestionEntity getQuestionByItemIndex(int itemIndex) {
        return quizMapper.findQuestionByItemIndex(itemIndex);
    }

    public List<ArticleQuestionEntity> getQuestionsByQuizId(int quizId) {
        return quizMapper.findQuestionsByQuizId(quizId);
    }

    public ArticleQuestionEntity getNextQuestion(int quizId, int currentItemIndex){
        return quizMapper.findNextQuestion(quizId, currentItemIndex);
    }

    public boolean checkAnswer(int itemIndex, String userAnswer){
        if (userAnswer==null){
            return false;
        }
        ArticleQuestionEntity question=quizMapper.findQuestionByItemIndex(itemIndex);
        if (question!=null&&question.getAnswer()!=null){
            return userAnswer.trim().equalsIgnoreCase(question.getAnswer().trim());

        }
        return false;
    }




}
