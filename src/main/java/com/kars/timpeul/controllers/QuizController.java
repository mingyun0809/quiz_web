package com.kars.timpeul.controllers;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/article")
public class QuizController {
    @RequestMapping(value = "/quiz", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizPage(){
        return "article/quiz";
    }

    @RequestMapping(value = "/quizContent", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizContentPage(){
        return "article/quizContent";
    }

    @RequestMapping(value = "/quizResult", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getQuizResultPage(){
        return "article/quizResult";
    }



}
