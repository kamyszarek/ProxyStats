package com.arkaprox.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class StartController {

    @GetMapping("/")
    public String getHomepage() throws IOException {
        Resource resource = new ClassPathResource("static/index.html");
        byte[] bytes = Files.readAllBytes(Path.of(resource.getURI()));
        return new String(bytes);
    }

    @GetMapping("/is-server-alive")
    public String isServerAlive() {
        return "Server is alive";
    }

}
