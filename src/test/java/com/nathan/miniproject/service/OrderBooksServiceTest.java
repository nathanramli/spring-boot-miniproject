package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.dao.OrderBooks;
import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dao.UserStocks;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.OrderBooksRequest;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.*;
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
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderBooksService.class)
class OrderBooksServiceTest {
    @MockBean
    private OrderBooksRepository orderBooksRepository;

    @MockBean
    private StocksRepository stocksRepository;

    @MockBean
    private UserStocksRepository userStocksRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private TransactionsRepository transactionsRepository;

    @MockBean
    private UserStocksService userStocksService;

    @MockBean
    private UsersService usersService;

    @Autowired
    private OrderBooksService orderBooksService;

    @Test
    void createBuyBidExactAmountSuccess_Test() {
        when(orderBooksRepository.findSellBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(1000D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        OrderBooksRequest resp = orderBooksService
                .create(
                        OrderBooksRequest.builder()
                                .id(1L)
                                .price(1000D)
                                .isBuy(true)
                                .shareUnits(1L)
                                .user(UsersRequest.builder().id(1L).build())
                                .stock(StocksRequest.builder().code("BBCA").build())
                                .build()
                );

        assertEquals(resp.getStock().getCode(), "BBCA");
        assertEquals(resp.getUser().getId(), 1L);
        assertEquals(resp.getShareUnits(), 1L);
        assertEquals(resp.getPrice(), 1000D);
    }

    @Test
    void createSellBidExactAmountSuccess_Test() {
        when(orderBooksRepository.findBuyBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(1000D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        when(userStocksRepository.findById(any())).thenReturn(
                Optional.of(UserStocks.builder()
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        OrderBooksRequest resp = orderBooksService
                .create(
                        OrderBooksRequest.builder()
                                .id(1L)
                                .price(1000D)
                                .isBuy(false)
                                .shareUnits(1L)
                                .user(UsersRequest.builder().id(1L).build())
                                .stock(StocksRequest.builder().code("BBCA").build())
                                .build()
                );
        assertEquals(resp.getStock().getCode(), "BBCA");
        assertEquals(resp.getUser().getId(), 1L);
        assertEquals(resp.getShareUnits(), 1L);
        assertEquals(resp.getPrice(), 1000D);
    }

    @Test
    void createBuyBidPartialAmountSuccess_Test() {
        when(orderBooksRepository.findSellBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .price(1000D)
                        .shareUnits(1L)
                        .build(), OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .price(1000D)
                        .shareUnits(2L)
                        .build())
        );

        when(orderBooksRepository.getById(any())).thenReturn(
                OrderBooks.builder()
                        .id(1L)
                        .shareUnits(10L)
                        .build()
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(2000D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        when(userStocksRepository.findById(any())).thenReturn(
                Optional.of(UserStocks.builder()
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        OrderBooksRequest resp = orderBooksService
                .create(
                        OrderBooksRequest.builder()
                                .id(1L)
                                .price(1000D)
                                .isBuy(true)
                                .shareUnits(2L)
                                .user(UsersRequest.builder().id(1L).build())
                                .stock(StocksRequest.builder().code("BBCA").build())
                                .build()
                );

        assertEquals(resp.getStock().getCode(), "BBCA");
        assertEquals(resp.getUser().getId(), 1L);
        assertEquals(resp.getShareUnits(), 2L);
        assertEquals(resp.getPrice(), 1000D);
    }

    @Test
    void createSellBidPartialAmountSuccess_Test() {
        when(orderBooksRepository.findBuyBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(2000D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        when(userStocksRepository.findById(any())).thenReturn(
                Optional.of(UserStocks.builder()
                        .price(1000D)
                        .shareUnits(2L)
                        .build())
        );

        OrderBooksRequest resp = orderBooksService
                .create(
                        OrderBooksRequest.builder()
                                .id(1L)
                                .price(1000D)
                                .isBuy(false)
                                .shareUnits(2L)
                                .user(UsersRequest.builder().id(1L).build())
                                .stock(StocksRequest.builder().code("BBCA").build())
                                .build()
                );
        assertEquals(resp.getStock().getCode(), "BBCA");
        assertEquals(resp.getUser().getId(), 1L);
        assertEquals(resp.getShareUnits(), 2L);
        assertEquals(resp.getPrice(), 1000D);
    }

    @Test
    void createBidFailed_WhenInvalidId_Test() {
        when(usersRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        try {
            orderBooksService.create(OrderBooksRequest.builder()
                    .user(UsersRequest.builder().id(1L).build())
                    .stock(StocksRequest.builder().code("BBCA").build())
                    .shareUnits(1L)
                    .price(1000D)
                    .build());
            fail();
        } catch (RuntimeException e) {
            // success
        }
    }

    @Test
    void createBidFailed_WhenBalanceNotEnough_Test() {
        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(10D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        try {
            orderBooksService.create(OrderBooksRequest.builder()
                    .user(UsersRequest.builder().id(1L).build())
                    .stock(StocksRequest.builder().code("BBCA").build())
                    .shareUnits(1L)
                    .price(1000D)
                    .isBuy(true)
                    .build());
            fail();
        } catch (Exception e) {
            // success
        }
    }

    @Test
    void createBidFailed_WhenStockNotEnough_Test() {
        when(usersRepository.findById(any())).thenReturn(
                Optional.of(Users.builder()
                        .id(1L)
                        .balance(10D)
                        .build())
        );

        when(stocksRepository.findById(any())).thenReturn(
                Optional.of(Stocks.builder()
                        .code("BBCA")
                        .build())
        );

        when(userStocksRepository.findById(any())).thenReturn(
                Optional.of(UserStocks.builder()
                        .shareUnits(2L)
                        .build())
        );

        try {
            orderBooksService.create(OrderBooksRequest.builder()
                    .user(UsersRequest.builder().id(1L).build())
                    .stock(StocksRequest.builder().code("BBCA").build())
                    .shareUnits(10L)
                    .price(1000D)
                    .isBuy(false)
                    .build());
            fail();
        } catch (Exception e) {
            // success
        }
    }

    @Test
    void findAllBuyBidSuccess_Test() {
        when(orderBooksRepository.findBuyBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .build())
        );

        List<OrderBooksRequest> orderBooksRequestList = orderBooksService
                .findAllBids("BBNI", 1000D, true);

        assertEquals(orderBooksRequestList.get(0).getUser().getId(), 1L);
        assertEquals(orderBooksRequestList.get(0).getStock().getCode(), "BBNI");
    }

    @Test
    void findAllSellBidSuccess_Test() {
        when(orderBooksRepository.findSellBid(any(), any())).thenReturn(
                Arrays.asList(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .build())
        );

        List<OrderBooksRequest> orderBooksRequestList = orderBooksService
                .findAllBids("BBNI", 1000D, false);

        assertEquals(orderBooksRequestList.get(0).getUser().getId(), 1L);
        assertEquals(orderBooksRequestList.get(0).getStock().getCode(), "BBNI");
    }

    @Test
    void deleteBuyBidSuccess_Test() {
        when(orderBooksRepository.findById(any())).thenReturn(
                Optional.of(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .isBuy(true)
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        try {
            orderBooksService.deleteOrder(1L);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void deleteSellBidSuccess_Test() {
        when(orderBooksRepository.findById(any())).thenReturn(
                Optional.of(OrderBooks.builder()
                        .id(1L)
                        .user(Users.builder().id(1l).build())
                        .stock(Stocks.builder().code("BBNI").build())
                        .isBuy(false)
                        .price(1000D)
                        .shareUnits(1L)
                        .build())
        );

        when(userStocksRepository.getById(any())).thenReturn(
                UserStocks.builder()
                        .price(1000D)
                        .build()
        );

        try {
            orderBooksService.deleteOrder(1L);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void deleteBidFailed_Test() {
        when(orderBooksRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(userStocksRepository.getById(any())).thenReturn(
                UserStocks.builder()
                        .price(1000D)
                        .build()
        );

        try {
            orderBooksService.deleteOrder(1L);
            fail();
        } catch (Exception e) {
            // success
        }
    }
}