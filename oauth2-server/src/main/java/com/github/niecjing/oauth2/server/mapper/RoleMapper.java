package com.github.niecjing.oauth2.server.mapper;

import com.github.niecjing.oauth2.server.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jing Zhi Bao
 */
@Mapper
@Repository
public interface RoleMapper {

    @Select( "SELECT A.id,A.name FROM role A LEFT JOIN user_role B ON A.id=B.role_id WHERE B.user_id=${userId}" )
    List<Role> getRolesByUserId(@Param("userId") Long userId);

}
