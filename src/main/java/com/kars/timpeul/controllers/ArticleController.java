package com.kars.timpeul.controllers;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.results.ArticleResult;
import com.kars.timpeul.services.ArticleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @RequestMapping(value = "/playList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPlayList() {
        return "article/playList";
    }
    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite() {
        return "article/write";
    }
    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify() {
        return "article/modify";
    }
    @RequestMapping(value = "/made", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getMade() {
        return "article/made";
    }

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    // 작성
    @RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(ArticleListEntity articleList,
                            @RequestParam(value = "question") List<String> questions,
                            @RequestParam(value = "answer") List<String> answers) {
        ArticleResult result = this.articleService.write(articleList, questions, answers);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        if (result == ArticleResult.SUCCESS) {
            response.put("index", articleList.getIndex());
        }
        return response.toString();
    }


    // 조회
    @RequestMapping(value = "/playList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArticleListEntity[] getPlayLists() { // MemoEntity[] 배열로
        return this.articleService.getAll();
    }

}
