package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.UsersService;
import com.nathan.miniproject.util.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/v1/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            UsersRequest user = usersService.find(id);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, user);
        } catch (RuntimeException e) {
            log.error("User not found");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<UsersRequest> users = usersService.find();
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, users);
        } catch (Exception e) {
            log.error("Failed to find all users");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
