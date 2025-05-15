package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.QuizContentEntity;
import com.kars.timpeul.entities.QuizEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuizMapper {

    QuizEntity findQListById(@Param(value = "quizId") int quizId);
    QuizContentEntity findFirstQuestionByQuizId(@Param(value = "quizId") int quizId);
    List<QuizContentEntity> findQuestionsByQuizId(@Param(value = "quizId") int quizId);
    QuizContentEntity findQuestionByItemIndex(@Param(value = "itemIndex") int itemIndex);
}
