package com.nathan.miniproject.controller;

import com.nathan.miniproject.domain.dto.HelloRequest;
import com.nathan.miniproject.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hello")
public class HelloController {
    @Autowired
    private HelloService helloService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getHello(@PathVariable Long id) {
        return helloService.getHello(id);
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> createHello(@RequestBody HelloRequest helloRequest) {
        return helloService.saveHello(helloRequest);
    }
}
