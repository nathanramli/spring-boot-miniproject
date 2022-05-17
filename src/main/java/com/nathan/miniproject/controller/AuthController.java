package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.AuthService;
import com.nathan.miniproject.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersRequest request) {
        try {
            authService.register(request);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, null);
        } catch (RuntimeException e) {
            log.error("Username already registered.");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersRequest request) {
        try {
            return authService.authenticatedAndGenerateToken(request);
        } catch (BadCredentialsException e) {
            return Response.build(ResponseMessage.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}