package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleQuestionMapper {
    int insertQuestion(@Param("questions") ArticleQuestionEntity questions);

    // 작성 시 문제들 저장
    ArticleQuestionEntity selectQuestionByIndex(@Param(value = "index") int index);
}
