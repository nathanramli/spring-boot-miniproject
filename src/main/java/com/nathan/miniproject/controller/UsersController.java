package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.UsersService;
import com.nathan.miniproject.util.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/users")
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

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Principal principal) {
        try {
            Users user = (Users) usersService.loadUserByUsername(principal.getName());
            UsersRequest usersRequest = usersService.find(user.getId(), true);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, usersRequest);
        } catch (Exception e) {
            log.error("Failed to find all users");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/balance")
    public ResponseEntity<?> fundBalance(Principal principal, @RequestBody UsersRequest usersRequest) {
        try {
            Users user = (Users) usersService.loadUserByUsername(principal.getName());
            if (!user.isAdmin())
                throw new RuntimeException("Unauthorized to fund balance.");

            UsersRequest userResponse = usersService.fundBalance(usersRequest);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, userResponse);
        } catch (RuntimeException e) {
            log.error("Unauthorized to fund balance.");
            return Response.build(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, null);
        } catch (Exception e) {
            log.error("Failed to find all users");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
