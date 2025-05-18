package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleQuestionMapper {
    int insertQuestion(@Param("questions") ArticleQuestionEntity questions);

    // 작성 시 문제들 저장
    ArticleQuestionEntity selectQuestionByIndex(@Param(value = "index") int index);
}
