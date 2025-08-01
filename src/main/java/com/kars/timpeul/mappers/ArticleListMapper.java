package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.ArticleQuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ArticleListMapper {
    int insertList(@Param("lists") ArticleListEntity lists);


    // 작성 시 리스트에 저장
    ArticleListEntity selectListByIndex(@Param(value = "index") int index);



    // 수정

    // 삭제
    int update(@Param(value = "index") int index);



    // 모든 퀴즈 조회
    ArticleListEntity[] selectAll();

    // 내가 만든 퀴즈 조회
    ArticleListEntity[] selectListsByToken(@Param(value = "token") String token);
}
