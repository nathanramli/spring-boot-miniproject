package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.StocksService;
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
@RequestMapping("/api/v1/stocks")
public class StocksController {
    @Autowired
    private StocksService stocksService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody StocksRequest stocksRequest) {
        try {
            StocksRequest stock = stocksService.create(stocksRequest);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.CREATED, stock);
        } catch (RuntimeException e) {
            log.error("Stock already exist!");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable String code, @RequestBody StocksRequest stocksRequest) {
        try {
            StocksRequest stock = stocksService.update(code, stocksRequest);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, stock);
        } catch (RuntimeException e) {
            log.error("Stock not exist!");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findById(@PathVariable String code) {
        try {
            StocksRequest stock = stocksService.find(code);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, stock);
        } catch (RuntimeException e) {
            log.error("Stock not found");
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        try {
            List<StocksRequest> stocksList = stocksService.find();
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, stocksList);
        } catch (Exception e) {
            log.error("Failed to find all stocks");
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
