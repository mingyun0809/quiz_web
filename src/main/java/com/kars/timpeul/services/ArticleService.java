package com.kars.timpeul.services;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.ArticleQuestionEntity;
import com.kars.timpeul.mappers.ArticleMapper;
import com.kars.timpeul.results.ArticleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }


    //작성
    public ArticleResult write(ArticleListEntity articleList, List<String> questions, List<String> answers) {

        articleList.setAdmin(false);
        articleList.setCreatedAt(LocalDateTime.now());
        articleList.setModifiedAt(null);
        articleList.setDeleted(false);
        articleList.setToken("0010001"); // getToken()으로 바꾸기

        int dbList = this.articleMapper.insertList(articleList);
        if (dbList == 0) {
            return ArticleResult.FAILURE;
        }

        int listIndex = articleList.getIndex();
        for (int i = 0; i < questions.size(); i++) {
            ArticleQuestionEntity question = new ArticleQuestionEntity();
            question.setListIndex(listIndex);
            question.setItemIndex(questions.size());
            question.setQuestion(question.getQuestion());
            question.setAnswer(question.getAnswer());

            int dbQuestion = this.articleMapper.insertQuestion(question);
            if (dbQuestion == 0) {
                return ArticleResult.FAILURE;
            }


        }
        return  ArticleResult.SUCCESS;
    }



    // 리스트 조회 O
    public ArticleListEntity[] getAll() {
        return this.articleMapper.selectAll();
    }


    // 수정

    // 삭제
    public ArticleResult deleteByIndex(int index) {
        if (index < 0) {
            return ArticleResult.FAILURE;
        }
        ArticleListEntity dbList = this.articleMapper.selectListByIndex(index);
        if (dbList == null || dbList.isDeleted()) {
            return ArticleResult.FAILURE;
        }
        dbList.setDeleted(true);
        return this.articleMapper.update(dbList) > 0 ? ArticleResult.SUCCESS : ArticleResult.FAILURE;

    }


}