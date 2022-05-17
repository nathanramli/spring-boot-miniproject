package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.OrderBooksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.OrderBooksService;
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
@RequestMapping("/api/v1/order-books")
public class OrderBooksController {
    @Autowired
    private OrderBooksService orderBooksService;

    @Autowired
    private UsersService usersService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody OrderBooksRequest orderBooksRequest, Principal principal) {
        try {
            // the order should be coming from the requester itself
            Users user = (Users) usersService.loadUserByUsername(principal.getName());
            orderBooksRequest.setUser(UsersRequest.builder().id(user.getId()).build());
            OrderBooksRequest orderBooks = orderBooksService.create(orderBooksRequest);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.CREATED, orderBooks);
        } catch (RuntimeException e) {
            log.error("Referenced id is not found");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }  catch (Exception e) {
            log.error("Failed to create order book");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            orderBooksService.deleteOrder(id);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, null);
        } catch (RuntimeException e) {
            log.error("Order book not found");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findAllBids(@PathVariable String code, @RequestParam(defaultValue = "buy") String pos) {
        try {
            List<OrderBooksRequest> orderBooksRequestList = orderBooksService.findAllBids(code, !pos.equalsIgnoreCase("sell") ? Double.MAX_VALUE : 0, !pos.equalsIgnoreCase("sell"));
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, orderBooksRequestList);
        } catch (Exception e) {
            log.error("Failed to find all order books");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
