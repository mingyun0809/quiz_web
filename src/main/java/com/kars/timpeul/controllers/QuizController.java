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
import java.util.List;
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

        model.addAttribute("pageTitle", quizInfo.getTitle());
        model.addAttribute("quizInfo", quizInfo);
        model.addAttribute("firstQuestion", firstQuestion);

        return "article/quiz";
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
            @RequestParam("quizId") int quizId, // quizId는 이미 받고 있음
            HttpSession session) {

        // quizService.checkAnswer 호출 시 quizId 전달
        boolean isCorrect = quizService.checkAnswer(quizId, itemIndex, userAnswer);
        String message = isCorrect ? "정답!" : "오답!";

        // quizService.getQuestionByItemIndex 호출 시 quizId 전달
        ArticleQuestionEntity question = quizService.getQuestionByItemIndex(quizId, itemIndex);
        String correctAnswer = question != null ? question.getAnswer() : "";

        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("message", message);
        response.put("correctAnswer", correctAnswer);

        if (isCorrect) {
            String scoreAttributeName = "quiz_" + quizId + "_correctAnswers";
            Integer currentScore = (Integer) session.getAttribute(scoreAttributeName);
            if (currentScore == null) {
                currentScore = 0;
            }
            currentScore++;
            session.setAttribute(scoreAttributeName, currentScore);
        }
        return ResponseEntity.ok(response);
    }





    @RequestMapping(value = "/quizResult/{quizId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizResultPage(@PathVariable("quizId") int quizId, Model model,
                                    HttpSession session){

        Integer correctAnswers = (Integer) session.getAttribute("quiz_" + quizId + "_correctAnswers");
        if (correctAnswers == null) {
            correctAnswers = 0; // 세션 만료 또는 비정상 접근 시 기본값
        }

        // QuizService에 총 문제 수를 가져오는 메소드가 있다고 가정합니다.
        // 이 메소드는 QuizMapper를 통해 DB에서 문제 수를 COUNT 해야 합니다.
        // 예: int totalQuestions = quizService.getTotalQuestionsForQuiz(quizId);
        // 아래는 임시로 List를 가져와 size를 사용하는 방식이지만, COUNT(*) 쿼리가 더 효율적입니다.
        List<ArticleQuestionEntity> questions = quizService.getQuestionsByQuizId(quizId);
        int totalQuestions = questions != null ? questions.size() : 0;




        String customResultMessage;
        double percentage = 0;

        if (totalQuestions > 0) {
            percentage = ((double) correctAnswers / totalQuestions) * 100;
        }

        if (totalQuestions == 0 && correctAnswers == 0) {
            customResultMessage = "문제가 아직 없어요";
        } else if (percentage >= 91) {
            customResultMessage = "완벽해요! 퀴즈 마스터시군요!";
        } else if (percentage >= 61) {
            customResultMessage = "훌륭해요! 거의 다 맞혔네요!";
        } else if (percentage >= 31) {
            customResultMessage = "조금만 더 분발해보세요!";
        } else {
            customResultMessage = "아쉬워요! 다음엔 더 잘할 수 있을거예요!";
        }

        model.addAttribute("numberCorrectAnswer", correctAnswers);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("percentage", percentage); // 소수점 포함된 값 전달 (JS에서 반올림 등 처리)
        model.addAttribute("customResultMessage", customResultMessage);
        model.addAttribute("currentQuizId", quizId); // "다시하기" 링크용

        return "article/quizResult";
    }
}
