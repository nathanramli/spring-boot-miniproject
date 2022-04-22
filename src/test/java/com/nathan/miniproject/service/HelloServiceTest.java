package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.common.ApiResponse;
import com.nathan.miniproject.domain.dao.Hello;
import com.nathan.miniproject.domain.dto.HelloRequest;
import com.nathan.miniproject.repository.HelloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HelloService.class)
class HelloServiceTest {

    @MockBean
    private HelloRepository helloRepository;

    @Autowired
    private HelloService helloService;

    @Test
    void saveHelloSuccess_Test() {
        when(helloRepository.save(any()))
                .thenAnswer(args -> {
                    Hello hello = args.getArgument(0);
                    hello.setId(1L);
                    return null;
                });

        ResponseEntity<Object> responseEntity = helloService.saveHello(
                HelloRequest.builder().hello("Hello world!").build());
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        HelloRequest helloRequest = (HelloRequest) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        assertEquals("Hello world!", helloRequest.getHello());
        assertEquals(1L, helloRequest.getId());
    }

    @Test
    void getHelloSuccess_Test() {
        when(helloRepository.findById(1L))
                .thenReturn(Optional.of(Hello
                        .builder()
                        .id(1L)
                        .hello("Hello world!")
                        .build()));

        ResponseEntity<Object> responseEntity = helloService.getHello(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        HelloRequest helloRequest = (HelloRequest) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("Hello world!", helloRequest.getHello());
        assertEquals(1L, helloRequest.getId());
    }

    @Test
    void getHelloError_Test() {
        ResponseEntity<Object> responseEntity = helloService.getHello(1L);
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        HelloRequest helloRequest = (HelloRequest) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }
}