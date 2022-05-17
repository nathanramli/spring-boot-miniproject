package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dao.UserStocks;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.domain.dto.UserStocksRequest;
import com.nathan.miniproject.repository.StocksRepository;
import com.nathan.miniproject.repository.UserStocksRepository;
import com.nathan.miniproject.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserStocksService.class)
class UserStocksServiceTest {
    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private UserStocksRepository userStocksRepository;

    @MockBean
    private StocksRepository stocksRepository;

    @Autowired
    private UserStocksService userStocksService;

    @Test
    void getUserStocksSuccess_Test() {
        when(userStocksRepository.findAllByUserId(any())).thenReturn(
                Arrays.asList(
                        UserStocks.builder()
                                .user(Users.builder()
                                        .id(1L).build())
                                .stock(Stocks.builder()
                                        .code("BBCA")
                                        .build())
                                .shareUnits(1L)
                                .price(1000D)
                                .build(),
                        UserStocks.builder()
                                .user(Users.builder()
                                        .id(1L).build())
                                .stock(Stocks.builder()
                                        .code("BBNI")
                                        .build())
                                .shareUnits(2L)
                                .price(500D)
                                .build()
                )
        );

        List<UserStocksRequest> userStocksRequestList = userStocksService.getUserStocks(1L);
        assertEquals("BBCA", userStocksRequestList.get(0).getStockCode());
        assertEquals("BBNI", userStocksRequestList.get(1).getStockCode());
        assertEquals(1L, userStocksRequestList.get(0).getShareUnits());
        assertEquals(2L, userStocksRequestList.get(1).getShareUnits());
    }

    @Test
    void fundUserStockForTheFirstTimeSuccess_Test() {
        when(userStocksRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        when(userStocksRepository.save(any())).thenReturn(
                UserStocks.builder()
                        .stock(Stocks.builder().code("BBCA").build())
                        .user(Users.builder().id(1L).build())
                        .price(1000D)
                        .shareUnits(1L)
                        .build()
        );

        UserStocksRequest resp = userStocksService.fundStock(UserStocksRequest.builder()
                        .userId(1L)
                        .stockCode("BBCA")
                        .price(1000D)
                        .shareUnits(1L)
                .build());

        assertEquals(1L, resp.getUserId());
        assertEquals("BBCA", resp.getStockCode());
        assertEquals(1000D, resp.getPrice());
        assertEquals(1L, resp.getShareUnits());
    }

    @Test
    void fundUserStockSuccess_Test() {
        when(userStocksRepository.findById(any())).thenReturn(
                Optional.of(UserStocks.builder()
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        UserStocksRequest resp = userStocksService.fundStock(UserStocksRequest.builder()
                .userId(1L)
                .stockCode("BBCA")
                .price(1000D)
                .shareUnits(1L)
                .build());

        assertEquals(1L, resp.getUserId());
        assertEquals("BBCA", resp.getStockCode());
        assertEquals(1000D, resp.getPrice());
        assertEquals(2L, resp.getShareUnits());
    }

    @Test
    void fundUserStockFail_Test() {
        when(userStocksRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        try {
            UserStocksRequest resp = userStocksService.fundStock(UserStocksRequest.builder()
                    .userId(1L)
                    .stockCode("BBCA")
                    .price(1000D)
                    .shareUnits(1L)
                    .build());
            fail();
        } catch (RuntimeException e) {
            // success
        }
    }
}