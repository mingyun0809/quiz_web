package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    int insertList(@Param("lists") ArticleListEntity lists);
    int insertQuestion(@Param("questions") ArticleQuestionEntity questions);

    // 추가
    ArticleListEntity selectListByIndex(@Param(value = "index") int index);
    ArticleQuestionEntity selectQuestionByIndex(@Param(value = "index") int index);


    // 조회
    ArticleListEntity[] selectAll();


    // 수정

    // 삭제
}
