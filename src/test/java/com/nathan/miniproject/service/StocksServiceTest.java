package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.repository.StocksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StocksService.class)
class StocksServiceTest {
    @MockBean
    private StocksRepository stocksRepository;

    @Autowired
    private StocksService stocksService;

    @Test
    void findAllSuccess_Test() {
        when(stocksRepository.findAll()).thenReturn(
                Arrays.asList(
                        Stocks.builder().code("BBCA").name("Bank BCA").build(),
                        Stocks.builder().code("BBRI").name("Bank BRI").build()
                )
        );

        List<StocksRequest> stocksList = stocksService.find();
        assertEquals(2, stocksList.size());
        assertEquals("BBCA", stocksList.get(0).getCode());
        assertEquals("Bank BCA", stocksList.get(0).getName());
        assertEquals("BBRI", stocksList.get(1).getCode());
        assertEquals("Bank BRI", stocksList.get(1).getName());
    }

    @Test
    void findUserSuccess_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.of(Stocks.builder().code("BBCA").name("Bank BCA").build()));

        StocksRequest stock = stocksService.find("BBCA");
        assertEquals("BBCA", stock.getCode());
        assertEquals("Bank BCA", stock.getName());
    }

    @Test
    void findUserError_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.empty());

        try {
            StocksRequest stock = stocksService.find("BBCA");
            fail();
        } catch (RuntimeException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void createUserSuccess_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.empty());

        StocksRequest stock = stocksService.create(StocksRequest.builder().code("BBCA").name("Bank BCA").build());
        assertEquals("BBCA", stock.getCode());
        assertEquals("Bank BCA", stock.getName());
    }

    @Test
    void createUserError_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.of(Stocks.builder().code("BBCA").name("Bank BCA").build()));

        try {
            StocksRequest stock = stocksService.create(StocksRequest.builder().code("BBCA").name("Bank BCA").build());
            fail();
        } catch (RuntimeException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void updateUserSuccess_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.of(Stocks.builder().code("BBCA").name("Bank BCA").build()));
        StocksRequest stock = stocksService.update("BBCA", StocksRequest.builder().code("BBCA").name("Bank Name").build());

        assertEquals("BBCA", stock.getCode());
        assertEquals("Bank Name", stock.getName());
    }

    @Test
    void updateUserError_Test() {
        when(stocksRepository.findById(any())).thenReturn(Optional.empty());

        try {
            StocksRequest stock = stocksService.update("BBCA",
                    StocksRequest.builder().code("BBCA").name("Bank BCA").build());
            fail();
        } catch (RuntimeException e) {
        } catch (Exception e) {
            fail();
        }
    }

}