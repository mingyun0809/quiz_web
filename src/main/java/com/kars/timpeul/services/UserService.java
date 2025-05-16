package com.kars.timpeul.services;

import com.kars.timpeul.entities.UserEntity;
import com.kars.timpeul.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 카카오 ID 토큰으로 사용자 로그인
     * @param idToken 카카오 ID 토큰
     * @return 로그인된 사용자 정보   */
    public UserEntity loginWithKakao(String idToken) {
        UserEntity user = userMapper.selectUserByIdToken(idToken);

        if(user == null) {
            user = new UserEntity();
            user.setIdToken(idToken);
            user.setAdmin(false);
            user.setDeleted(false);
            user.setSuspended(false);

            userMapper.insertUser(user);

            user = userMapper.selectUserByIdToken(idToken);
        }

        return user;
    }

    /**
     * ID 토큰으로 사용자 조회
     * @param idToken 카카오 ID 토큰
     * @return 사용자 정보 또는 null
     */
    public UserEntity getUserByIdToken(String idToken) {
        return userMapper.selectUserByIdToken(idToken);
    }
}
























