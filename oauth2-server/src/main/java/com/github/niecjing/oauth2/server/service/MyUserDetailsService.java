package com.github.niecjing.oauth2.server.service;

import com.github.niecjing.oauth2.server.entity.Role;
import com.github.niecjing.oauth2.server.entity.User;
import com.github.niecjing.oauth2.server.mapper.RoleMapper;
import com.github.niecjing.oauth2.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jing Zhi Bao
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查数据库
        User user = userMapper.loadUserByUsername(userName);
        if (null != user) {
            List<Role> roles = roleMapper.getRolesByUserId(user.getId());
            user.setAuthorities(roles);
        }

        return user;
    }
}
