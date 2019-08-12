package com.github.niecjing.oauth2.server.mapper;

import com.github.niecjing.oauth2.server.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jing Zhi Bao
 */
@Mapper
@Repository
public interface PermissionMapper {

    @Select( "SELECT A.NAME AS roleName,C.url FROM role AS A LEFT JOIN role_permission B ON A.id=B.role_id LEFT JOIN permission AS C ON B.permission_id=C.id" )
    List<RolePermission> getRolePermissions();
}
