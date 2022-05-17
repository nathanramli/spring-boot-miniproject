package com.nathan.miniproject.controller;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.UserStocks;
import com.nathan.miniproject.domain.dto.UserStocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.service.UserStocksService;
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
@RequestMapping("/api/v1/user-stocks")
public class UserStocksController {
    @Autowired
    private UserStocksService userStocksService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserStocks(@PathVariable Long id) {
        try {
            List<UserStocksRequest> userStocks = userStocksService.getUserStocks(id);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, userStocks);
        } catch (RuntimeException e) {
            log.error("Bad request: ", e);
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            log.error("Something happened when get user stocks. ", e);
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> fundStock(@RequestBody UserStocksRequest userStocksRequest) {
        try {
            UserStocksRequest userStock = userStocksService.fundStock(userStocksRequest);
            return Response.build(ResponseMessage.SUCCESS, HttpStatus.OK, userStock);
        } catch (RuntimeException e) {
            log.error("Bad request: ", e);
            return Response.build(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            log.error("Something happened when funding a stock. ", e);
            return Response.build(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
