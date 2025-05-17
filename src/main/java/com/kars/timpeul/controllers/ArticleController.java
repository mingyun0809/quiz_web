package com.kars.timpeul.controllers;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.UserEntity;
import com.kars.timpeul.results.ArticleResult;
import com.kars.timpeul.services.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping(value = "/write", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @RequestParam("Title") String title,
            @RequestParam("info") String info,
            @RequestParam(value = "question") List<String> questions,
            @RequestParam(value = "answer") List<String> answers) {

        ArticleResult result = articleService.write(title, info, questions, answers);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());

        return response.toString();
    }


    // 모든 퀴즈 조회 O
    @RequestMapping(value = "/playList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArticleListEntity[] getPlayLists() { // MemoEntity[] 배열로
        return this.articleService.getAll();
    }

    // 내가 만든 퀴즈 조회


}
