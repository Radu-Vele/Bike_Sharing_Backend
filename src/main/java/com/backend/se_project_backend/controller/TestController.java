package com.backend.se_project_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping(path = "/testMe")
    public String respond() {
        return "Merge bai!";
    }
}
