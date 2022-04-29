package com.nathan.miniproject.service;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.StocksRepository;
import com.nathan.miniproject.repository.UsersRepository;
import com.nathan.miniproject.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StocksService {
    @Autowired
    private StocksRepository stocksRepository;

    public StocksRequest create(StocksRequest stocksRequest) throws RuntimeException {
        Optional<Stocks> exists = stocksRepository.findById(stocksRequest.getCode());
        if (exists.isPresent()) {
            throw new RuntimeException("Stocks with code " + stocksRequest.getCode() + " already exist!");
        }

        Stocks stock = Stocks
                .builder()
                .code(stocksRequest.getCode())
                .name(stocksRequest.getName())
                .build();
        stocksRepository.save(stock);
        return StocksRequest.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .build();
    }

    public StocksRequest update(String code, StocksRequest stocksRequest) throws RuntimeException {
        Optional<Stocks> exists = stocksRepository.findById(code);
        if (exists.isEmpty()) {
            throw new RuntimeException("Stocks with code " + stocksRequest.getCode() + " not exist!");
        }

        Stocks stock = exists.get();
        stock.setName(stocksRequest.getName());
        stocksRepository.save(stock);
        return StocksRequest.builder()
                .code(stock.getCode())
                .name(stock.getName())
                .build();
    }

    public StocksRequest find(String code) {
        Optional<Stocks> optionalStocks = stocksRepository.findById(code);
        if (optionalStocks.isEmpty())
            throw new RuntimeException("Stock not found");
        Stocks stock = optionalStocks.get();
        return StocksRequest
                .builder()
                .code(stock.getCode())
                .name(stock.getName())
                .build();
    }

    public List<StocksRequest> find() {
        List<Stocks> stocksList = stocksRepository.findAll();
        List<StocksRequest> stocksRequests = new ArrayList<>();
        for(Stocks stock : stocksList) {
            stocksRequests.add(StocksRequest
                    .builder()
                    .code(stock.getCode())
                    .name(stock.getName())
                    .build());
        }
        return stocksRequests;
    }
}
