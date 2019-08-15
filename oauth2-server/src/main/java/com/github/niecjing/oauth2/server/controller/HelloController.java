package com.github.niecjing.oauth2.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jing Zhi Bao
 */
@RestController
public class HelloController {

    @GetMapping("/hi")
    @PreAuthorize("hasAuthority('admin')")
    public String hi(String name) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        System.out.println(authentication);
        return "hi , " + name;
    }
}
