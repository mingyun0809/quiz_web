package com.kars.timpeul.controllers;

import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.UserEntity;
import com.kars.timpeul.results.ArticleResult;
import com.kars.timpeul.services.ArticleService;
import jakarta.servlet.http.HttpSession;
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


    // 수정하기
    @PatchMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModify(
            @RequestParam("Title") String title,
            @RequestParam("info") String info,
            @RequestParam(value = "question") List<String> questions,
            @RequestParam(value = "answer") List<String> answers,
            @RequestParam(value = "listIndex") int listIndex,
            @RequestParam(value = "itemIndex") int itemIndex,
            HttpSession session) {
        String idToken = (String) session.getAttribute("idToken");

        ArticleResult result = articleService.modify(idToken, listIndex, itemIndex, title, info, questions, answers);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());

        return response.toString();
    }

    // 삭제
    @DeleteMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteModify(HttpSession session,
                               @RequestParam(value = "index", required = false, defaultValue = "0") int index) {
        String idTokenStr = (String) session.getAttribute("idToken");
        if (idTokenStr == null) {
            JSONObject response = new JSONObject();
            response.put("result", "failure");
            return response.toString();
        }

        UserEntity user = new UserEntity();
        user.setIdToken(idTokenStr);
        user.setAdmin(true);

        ArticleResult result = this.articleService.deleteByIndex(user, index);
        JSONObject response = new JSONObject();
        response.put("result", result.toString().toLowerCase());
        return response.toString();
    }


    // 작성
    @PostMapping(value = "/write", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(
            @RequestParam("Title") String title,
            @RequestParam("info") String info,
            @RequestParam(value = "question") List<String> questions,
            @RequestParam(value = "answer") List<String> answers,
            HttpSession session) {

        String idToken = (String) session.getAttribute("idToken");
        if (idToken == null) {
            // 로그인 안 된 상태 처리 (예: 실패 리턴)
            JSONObject response = new JSONObject();
            response.put("result", "failure");
            response.put("message", "로그인이 필요합니다.");
            return response.toString();
        }

        ArticleResult result = articleService.write(idToken, title, info, questions, answers);
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
    @RequestMapping(value = "/madeList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ArticleListEntity[] getMyLists(HttpSession session) {
        String idToken = (String) session.getAttribute("idToken");
        if (idToken == null) {
            return new ArticleListEntity[0]; // 로그인 안 된 경우 빈 배열
        }
        return articleService.getListsByToken(idToken);
    }


}
