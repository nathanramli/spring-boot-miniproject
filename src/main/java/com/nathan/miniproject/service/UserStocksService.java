package com.nathan.miniproject.service;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dao.UserStocks;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.UserStocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.StocksRepository;
import com.nathan.miniproject.repository.UserStocksRepository;
import com.nathan.miniproject.repository.UsersRepository;
import com.nathan.miniproject.util.Response;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserStocksService {
    @Autowired
    private UserStocksRepository userStocksRepository;

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private UsersRepository usersRepository;

    public List<UserStocksRequest> getUserStocks(Long userId) {
        List<UserStocks> userStocksList = userStocksRepository.findAllByUserId(userId);
        List<UserStocksRequest> userStocksRequestList = new ArrayList<>();
        for(UserStocks userStock : userStocksList) {
            userStocksRequestList.add(
                    UserStocksRequest.builder()
                            .userId(userStock.getUser().getId())
                            .stockCode(userStock.getStock().getCode())
                            .shareUnits(userStock.getShareUnits())
                            .price(userStock.getPrice())
                            .build());
        }
        return userStocksRequestList;
    }

    public UserStocksRequest fundStock(UserStocksRequest userStocksRequest) {
        UserStocks.UserStockId id = new UserStocks.UserStockId(userStocksRequest.getUserId(), userStocksRequest.getStockCode());
        Optional<UserStocks> userStocksOptional = userStocksRepository.findById(id);
        UserStocks userStocks = null;
        if (userStocksOptional.isEmpty()) {
            Optional<Users> usersOptional = usersRepository.findById(id.getUser());
            Optional<Stocks> stocksOptional = stocksRepository.findById(id.getStock());
            if (stocksOptional.isEmpty() || usersOptional.isEmpty())
                throw new RuntimeException("Referenced id not found.");

            userStocks = userStocksRepository.save(
                    UserStocks
                            .builder()
                            .user(usersOptional.get())
                            .stock(stocksOptional.get())
                            .price(userStocksRequest.getPrice())
                            .shareUnits(userStocksRequest.getShareUnits())
                            .build()
            );
        } else {
            userStocks = userStocksOptional.get();
            Double average = (userStocks.getPrice() * Double.longBitsToDouble(userStocks.getShareUnits())
                            + userStocksRequest.getPrice() * Double.longBitsToDouble(userStocksRequest.getShareUnits()))
                    / Double.longBitsToDouble(userStocks.getShareUnits() + userStocksRequest.getShareUnits());
            userStocks.setPrice(average);
            userStocks.setShareUnits(userStocks.getShareUnits() + userStocksRequest.getShareUnits());
            userStocksRepository.save(userStocks);
        }
        return UserStocksRequest
                .builder()
                .userId(id.getUser())
                .stockCode(id.getStock())
                .price(userStocks.getPrice())
                .shareUnits(userStocks.getShareUnits())
                .build();
    }

}
