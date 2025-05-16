package com.kars.timpeul.controllers;




import com.kars.timpeul.entities.ArticleListEntity;
import com.kars.timpeul.entities.ArticleQuestionEntity;
import com.kars.timpeul.services.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/article")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {

        this.quizService = quizService;
    }

    @RequestMapping(value = "/quiz/{quizId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizPage(@PathVariable("quizId") int quizId, Model model) {

        ArticleListEntity quizInfo = quizService.getQuizListById(quizId);
        ArticleQuestionEntity firstQuestion = quizService.getFirstQuestionOfQuiz(quizId); // 첫 문제 정보

        if (quizInfo == null) {
            // 해당 ID의 퀴즈가 없을 경우, 에러 페이지로 리다이렉트하거나 메시지 처리
            return "error_page"; // 예시 에러 페이지
        }

        model.addAttribute("pageTitle", quizInfo.getTitle()); // HTML <title> 태그용
        model.addAttribute("quizInfo", quizInfo); // ArticleListEntity 전체를 전달
        model.addAttribute("firstQuestion", firstQuestion); // 첫 문제 정보 전달 (미리보기용)
        // model.addAttribute("quizActualId", quizId); // PathVariable로 이미 quizId를 받고 있음

        return "article/quiz"; // templates/article/quiz.html
    }

    @RequestMapping(value = "/quizContent/{quizId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizContentPage(@PathVariable("quizId") int quizId, Model model, HttpSession session){

        ArticleListEntity quizInfo=quizService.getQuizListById(quizId);
        ArticleQuestionEntity currentQuestion=quizService.getFirstQuestionOfQuiz(quizId);

        if (quizInfo==null){
            return "article/quiz";
        }

        if (currentQuestion==null){
            return "article/quiz";
        }

        String scoreAttributeName = "quiz_" + quizId + "_correctAnswers";
        session.setAttribute(scoreAttributeName, 0);

        model.addAttribute("pageTitle", quizInfo.getTitle() + " - 문제 풀이");
        model.addAttribute("quizInfo", quizInfo); // 현재 풀고 있는 퀴즈의 정보
        model.addAttribute("currentQuestion", currentQuestion); // 현재 문제 객체
        // 나중에 답안 제출 시 필요할 수 있는 ID들
        model.addAttribute("quizId", quizId);
        model.addAttribute("itemIndex", currentQuestion.getItemIndex());




        return "article/quizContent";
    }

    @RequestMapping(value = "/quiz/next")
    @ResponseBody
    public ResponseEntity<ArticleQuestionEntity> getNextQuizQuestion(
            @RequestParam("quizId") int quizId,
            @RequestParam("currentItemIndex") int currentItemIndex){

        ArticleQuestionEntity nextQuestion=quizService.getNextQuestion(quizId, currentItemIndex);
        if (nextQuestion!=null){
            return ResponseEntity.ok(nextQuestion);
        } else {
            return ResponseEntity.ok(null);
        }

    }

    @RequestMapping(value = "/quiz/checkAnswer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAnswer(
            @RequestParam("itemIndex") int itemIndex,
            @RequestParam("userAnswer") String userAnswer,
            @RequestParam("quizId") int quizId,
            HttpSession session) {

        boolean isCorrect = quizService.checkAnswer(itemIndex, userAnswer);
        String message = isCorrect ? "맞았습니다!" : "틀렸습니다!";

        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("message", message);

        if (isCorrect) {
            String scoreAttributeName = "quiz_" + quizId + "_correctAnswers";
            Integer currentScore = (Integer) session.getAttribute(scoreAttributeName);
            if (currentScore == null) {
                currentScore = 0;
            }
            currentScore++;
            session.setAttribute(scoreAttributeName, currentScore); // 세션 기록 확인
        }

        return ResponseEntity.ok(response); // HTTP 200 OK 와 함께 JSON 응답
    }





    @RequestMapping(value = "/quizResult/{quizId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizResultPage(@PathVariable("quizId") int quizId, Model model,
                                    HttpSession session){
        Integer correctAnswers = (Integer) session.getAttribute("quiz_" + quizId + "_correctAnswers");

        model.addAttribute("numberCorrectAnswer", correctAnswers);
        return "article/quizResult";
    }



}
