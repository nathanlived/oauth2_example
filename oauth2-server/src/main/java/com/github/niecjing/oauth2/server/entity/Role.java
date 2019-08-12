package com.github.niecjing.oauth2.server.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jing Zhi Bao
 */
@Data
public class Role implements GrantedAuthority {

    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
