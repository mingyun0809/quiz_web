package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleQuestionMapper {
    int insertQuestion(@Param("questions") ArticleQuestionEntity questions);

    ArticleQuestionEntity selectQuestionsByListIndex(@Param(value = "listIndex") int listIndex,
                                                     @Param(value = "itemIndex")  int itemIndex);

    int updateQuestion(ArticleQuestionEntity question);

    int deleteQuestionByListIndexAndItemIndex(@Param(value = "listIndex") int listIndex,
                                              @Param(value = "itemIndex") int itemIndex);
}
