package com.kars.timpeul.services;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.ArticleQuestionEntity;
import com.kars.timpeul.entities.UserEntity;
import com.kars.timpeul.mappers.ArticleListMapper;
import com.kars.timpeul.mappers.ArticleQuestionMapper;
import com.kars.timpeul.results.ArticleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleListMapper listMapper;
    private final ArticleQuestionMapper questionMapper;

    @Autowired
    public ArticleService(ArticleListMapper listMapper, ArticleQuestionMapper questionMapper) {
        this.listMapper = listMapper;
        this.questionMapper = questionMapper;
    }

    // 수정하기
    public ArticleResult modify(String idToken, int listIndex, int itemIndex, String title, String info, List<String> questions, List<String> answers) {
        if (idToken == null || title == null || info == null || questions == null || answers == null || questions.size() != answers.size()) {
            return ArticleResult.FAILURE;
        }

        ArticleListEntity list = listMapper.selectListByIndex(listIndex);
        if (list == null || list.isDeleted()) {
            return ArticleResult.FAILURE;
        }

        list.setTitle(title);
        list.setInfo(info);
        list.setModifiedAt(LocalDateTime.now());

        for (int i = 0; i < questions.size(); i++) {
            ArticleQuestionEntity q = new ArticleQuestionEntity();
            q.setListIndex(listIndex);
            q.setItemIndex(i);
            q.setQuestion(questions.get(i));
            q.setAnswer(answers.get(i));
            questionMapper.insertQuestion(q);
        }

        return this.listMapper.updateList(list) > 0 ?  ArticleResult.SUCCESS : ArticleResult.FAILURE;
    }




    //작성
    public ArticleResult write(String idToken, String title, String info, List<String> questions, List<String> answers) {
        if (idToken == null || title == null || info == null || questions == null || answers == null || questions.size() != answers.size()) {
            return ArticleResult.FAILURE;
        }

        ArticleListEntity list = new ArticleListEntity();
        list.setTitle(title);
        list.setInfo(info);
        list.setToken(idToken);  // 토큰
        list.setAdmin(false);
        list.setDeleted(false);
        list.setCreatedAt(LocalDateTime.now());
        list.setModifiedAt(null);

        int listResult = listMapper.insertList(list);
        if (listResult == 0) return ArticleResult.FAILURE;

        for (int i = 0; i < questions.size(); i++) {
            ArticleQuestionEntity question = new ArticleQuestionEntity();
            question.setListIndex(list.getIndex());
            question.setItemIndex(i);
            question.setQuestion(questions.get(i));
            question.setAnswer(answers.get(i));
            questionMapper.insertQuestion(question);
        }

        return ArticleResult.SUCCESS;
    }

    // 삭제
    public ArticleResult deleteByIndex(UserEntity user, int index) {
        if (index < 1 || user == null || user.isDeleted() || !user.isAdmin()) {
            return ArticleResult.FAILURE;
        }

        ArticleListEntity list = listMapper.selectListByIndex(index);
        if (list == null || list.isDeleted()) {
            return ArticleResult.FAILURE;
        }

        list.setDeleted(true);
        return this.listMapper.update(index) > 0 ? ArticleResult.SUCCESS : ArticleResult.FAILURE;
    }

    // 리스트 조회 O
    public ArticleListEntity[] getAll() {
        return this.listMapper.selectAll();
    }

    // 내가 만든 퀴즈 조회
    public ArticleListEntity[] getListsByToken(String token) {
        return listMapper.selectListsByToken(token);
    }


}