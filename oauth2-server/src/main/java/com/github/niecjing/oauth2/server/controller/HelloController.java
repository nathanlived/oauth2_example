package com.github.niecjing.oauth2.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jing Zhi Bao
 */
@RestController
public class HelloController {

    @GetMapping("/hi")
    public String hi(String name){
        return "hi , " + name;
    }
}
