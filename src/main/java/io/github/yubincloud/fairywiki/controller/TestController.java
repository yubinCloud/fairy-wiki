package io.github.yubincloud.fairywiki.controller;

import io.github.yubincloud.fairywiki.domain.Test;
import io.github.yubincloud.fairywiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/list")
    public List<Test> list() {
        return testService.list();
    }
}
