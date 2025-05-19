package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleQuestionMapper {
    int insertQuestion(@Param("question") ArticleQuestionEntity question);

    // 특정 리스트 인덱스에 속한 문제들 모두 조회
    ArticleQuestionEntity selectQuestionsByListIndex(@Param(value = "listIndex") int listIndex,
                                                     @Param(value = "itemIndex")  int itemIndex);

    int updateQuestion(ArticleQuestionEntity question);

    int deleteQuestionByListIndexAndItemIndex(@Param(value = "listIndex") int listIndex,
                                              @Param(value = "itemIndex") int itemIndex);

}
