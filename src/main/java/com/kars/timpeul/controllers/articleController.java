package com.kars.timpeul.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/article")
public class articleController {

    // 테스트 주석입니다.

    @RequestMapping(value = "/playList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getPlayList() {return "article/playList";}

    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getWrite() {return "article/write";}

    @RequestMapping(value = "/modify", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getModify() {return "article/modify";}

    @RequestMapping(value = "/made", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getMade() {return "article/made";}
}
