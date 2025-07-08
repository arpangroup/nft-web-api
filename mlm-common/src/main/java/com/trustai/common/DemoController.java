package com.trustai.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/common")
    public String hello() {
        return "hellow from common....";
    }
}
