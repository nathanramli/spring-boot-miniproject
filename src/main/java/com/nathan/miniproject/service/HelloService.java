package com.nathan.miniproject.service;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Hello;
import com.nathan.miniproject.domain.dto.HelloRequest;
import com.nathan.miniproject.repository.HelloRepository;
import com.nathan.miniproject.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelloService {
    @Autowired
    private HelloRepository helloRepository;

    public ResponseEntity<Object> saveHello(HelloRequest helloRequest) {
        Hello hello = Hello.builder().hello(helloRequest.getHello()).build();
        helloRepository.save(hello);
        return Response.build(ResponseMessage.SUCCESS, HttpStatus.CREATED,
                HelloRequest
                        .builder()
                        .id(hello.getId())
                        .hello(hello.getHello())
                        .build());
    }

    public ResponseEntity<Object> getHello(Long id) {
        Optional<Hello> optionalHello = helloRepository.findById(id);
        if (optionalHello.isEmpty())
            return Response.build(ResponseMessage.NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        Hello hello = optionalHello.get();
        return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK,
                HelloRequest
                        .builder()
                        .id(hello.getId())
                        .hello(hello.getHello())
                        .build());
    }
}