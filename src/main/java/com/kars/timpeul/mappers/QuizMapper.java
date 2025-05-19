package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import com.kars.timpeul.entities.ArticleListEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuizMapper {

    ArticleListEntity findQListById(@Param(value = "quizId") int quizId);
    ArticleQuestionEntity findFirstQuestionByQuizId(@Param(value = "quizId") int quizId);
    List<ArticleQuestionEntity> findQuestionsByQuizId(@Param(value = "quizId") int quizId);
    ArticleQuestionEntity findQuestionByItemIndex(@Param("quizId") int quizId, @Param("itemIndex") int itemIndex);
    ArticleQuestionEntity findNextQuestion(@Param("quizId") int quizId, @Param("currentItemIndex") int currentItemIndex);
}
