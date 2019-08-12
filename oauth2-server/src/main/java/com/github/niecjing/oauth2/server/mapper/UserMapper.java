package com.github.niecjing.oauth2.server.mapper;

import com.github.niecjing.oauth2.server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Jing Zhi Bao
 */
@Mapper
@Repository
public interface UserMapper {

    @Select("select id , username , password from user where username = #{username}")
    User loadUserByUsername(@Param("username") String username);

}
