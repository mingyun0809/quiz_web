package com.kars.timpeul.controllers;

import com.kars.timpeul.dtos.KakaoLoginDto;
import com.kars.timpeul.entities.UserEntity;
import com.kars.timpeul.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/article")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getLogin() {
        return "article/login";
    }

    /**
     * 카카오 로그인 처리 API
     * @param kakaoLoginDto 카카오 로그인 정보
     * @param session HTTP 세션
     * @return 로그인 결과 및 리다이렉트 URL
     */
    @RequestMapping(value = "/kakao-login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestBody KakaoLoginDto kakaoLoginDto, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            if(kakaoLoginDto.getIdToken() == null || kakaoLoginDto.getIdToken().isEmpty()) {
                response.put("success", false);
                response.put("message", "ID 토큰이 제공되지 않았습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            UserEntity user = userService.loginWithKakao(kakaoLoginDto.getIdToken());

            if(user.isSuspended()) {
                response.put("success", false);
                response.put("message", "계정이 정지되었습니다.");
                return ResponseEntity.ok(response);
            }

            if(user.isDeleted()) {
                response.put("success", false);
                response.put("message", "탈퇴한 계정입니다.");
                return ResponseEntity.ok(response);
            }

            session.setAttribute("loggedInUser", user);
            session.setAttribute("idToken", user.getIdToken());

            response.put("success", true);
            response.put("redirectUrl", "/article/playList");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "로그인 처리 중 오류가 발생했습니다." + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}























