package com.kars.timpeul.mappers;

import com.kars.timpeul.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserEntity selectUserByIdToken(String idToken);

    int insertUser(UserEntity user);

    int updateUser(UserEntity user);
}
