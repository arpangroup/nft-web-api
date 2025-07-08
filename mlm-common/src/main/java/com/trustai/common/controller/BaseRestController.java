package com.trustai.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
public class BaseRestController {
    protected ResponseEntity <Object> buildResponse(Object body) {
        return ResponseEntity.ok().body(body);
    }

    public void getCurrentUser(){

    }

    public void validateRequest() {

    }

    public void logRequest() {

    }
}
