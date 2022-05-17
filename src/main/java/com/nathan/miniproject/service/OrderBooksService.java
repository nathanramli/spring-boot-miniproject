package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.dao.*;
import com.nathan.miniproject.domain.dto.OrderBooksRequest;
import com.nathan.miniproject.domain.dto.StocksRequest;
import com.nathan.miniproject.domain.dto.UserStocksRequest;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderBooksService {
    @Autowired
    private OrderBooksRepository orderBooksRepository;

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private UserStocksRepository userStocksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserStocksService userStocksService;

    @Autowired
    private UsersService usersService;

    public OrderBooksRequest create(OrderBooksRequest orderBooksRequest) throws RuntimeException {
        Optional<Users> usersOptional = usersRepository.findById(orderBooksRequest.getUser().getId());
        Optional<Stocks> stocksOptional = stocksRepository.findById(orderBooksRequest.getStock().getCode());
        if (stocksOptional.isEmpty() || usersOptional.isEmpty())
            throw new RuntimeException("Referenced id not found.");

        if (orderBooksRequest.getIsBuy()) {
            if (usersOptional.get().getBalance() < orderBooksRequest.getShareUnits() * orderBooksRequest.getPrice()) {
                throw new RuntimeException("Balance is not enough.");
            }
            else {
                // deduct user balance
                Users user = usersOptional.get();
                user.setBalance(user.getBalance() - orderBooksRequest.getShareUnits() * orderBooksRequest.getPrice());
                usersRepository.save(user);
            }
        } else {
            Optional<UserStocks> userStocks = userStocksRepository.findById(new UserStocks.UserStockId(orderBooksRequest.getUser().getId(), orderBooksRequest.getStock().getCode()));
            if (userStocks.isEmpty() || userStocks.get().getShareUnits() < orderBooksRequest.getShareUnits())
                throw new RuntimeException("Invalid amount of stock.");
            else {
                // deduct user stock
                UserStocks userStock = userStocks.get();
                userStock.setShareUnits(userStock.getShareUnits() - orderBooksRequest.getShareUnits());
                userStocksRepository.save(userStock);
            }
        }

        List<OrderBooksRequest> orderBooksRequestList = this.findAllBids(orderBooksRequest.getStock().getCode(), orderBooksRequest.getPrice(), !orderBooksRequest.getIsBuy());

        Long shareUnits = orderBooksRequest.getShareUnits();
        for(OrderBooksRequest orderBook : orderBooksRequestList) {
            Double price = (orderBooksRequest.getPrice() + orderBook.getPrice()) / 2;
            if (orderBook.getShareUnits() >= shareUnits) {
                // requester
                transactionsRepository.save(Transactions.builder()
                        .user(Users.builder().id(orderBooksRequest.getUser().getId()).build())
                        .stock(Stocks.builder().code(orderBooksRequest.getStock().getCode()).build())
                        .isBuy(orderBooksRequest.getIsBuy())
                        .price(price)
                        .shareUnits(shareUnits)
                        .build());

                // matcher
                if (orderBook.getShareUnits() == shareUnits) {
                    orderBooksRepository.deleteById(orderBook.getId());
                } else {
                    OrderBooks orderBookMatcher = orderBooksRepository.getById(orderBook.getId());
                    orderBookMatcher.setShareUnits(orderBookMatcher.getShareUnits() - shareUnits);
                    orderBooksRepository.save(orderBookMatcher);
                }
                transactionsRepository.save(Transactions.builder()
                        .user(Users.builder().id(orderBook.getUser().getId()).build())
                        .stock(Stocks.builder().code(orderBook.getStock().getCode()).build())
                        .isBuy(orderBook.getIsBuy())
                        .price(price)
                        .shareUnits(shareUnits)
                        .build());

                if (orderBooksRequest.getIsBuy()) {
                    userStocksService.fundStock(UserStocksRequest.builder()
                            .userId(orderBooksRequest.getUser().getId())
                            .stockCode(orderBooksRequest.getStock().getCode())
                            .shareUnits(shareUnits)
                            .price(price)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBooksRequest.getUser().getId())
                            .balance(orderBooksRequest.getPrice() * shareUnits - price * shareUnits)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBook.getUser().getId())
                            .balance(price * shareUnits)
                            .build());
                } else {
                    userStocksService.fundStock(UserStocksRequest.builder()
                            .userId(orderBook.getUser().getId())
                            .stockCode(orderBook.getStock().getCode())
                            .shareUnits(shareUnits)
                            .price(price)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBooksRequest.getUser().getId())
                            .balance(price * shareUnits)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBook.getUser().getId())
                            .balance(orderBook.getPrice() * shareUnits - price * shareUnits)
                            .build());
                }

                shareUnits = 0L;
                break;
            } else {
                shareUnits -= orderBook.getShareUnits();

                // requester
                transactionsRepository.save(Transactions.builder()
                        .user(Users.builder().id(orderBooksRequest.getUser().getId()).build())
                        .stock(Stocks.builder().code(orderBooksRequest.getStock().getCode()).build())
                        .isBuy(orderBooksRequest.getIsBuy())
                        .price(price)
                        .shareUnits(orderBook.getShareUnits())
                        .build());

                // matcher
                orderBooksRepository.deleteById(orderBook.getId());
                transactionsRepository.save(Transactions.builder()
                        .user(Users.builder().id(orderBook.getUser().getId()).build())
                        .stock(Stocks.builder().code(orderBook.getStock().getCode()).build())
                        .isBuy(orderBook.getIsBuy())
                        .price(price)
                        .shareUnits(orderBook.getShareUnits())
                        .build());

                if (orderBooksRequest.getIsBuy()) {
                    userStocksService.fundStock(UserStocksRequest.builder()
                            .userId(orderBooksRequest.getUser().getId())
                            .stockCode(orderBooksRequest.getStock().getCode())
                            .shareUnits(orderBook.getShareUnits())
                            .price(price)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBooksRequest.getUser().getId())
                            .balance(orderBooksRequest.getPrice() * orderBook.getShareUnits() - price * orderBook.getShareUnits())
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBook.getUser().getId())
                            .balance(price * orderBook.getShareUnits())
                            .build());
                } else {
                    userStocksService.fundStock(UserStocksRequest.builder()
                            .userId(orderBook.getUser().getId())
                            .stockCode(orderBook.getStock().getCode())
                            .shareUnits(orderBook.getShareUnits())
                            .price(price)
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBooksRequest.getUser().getId())
                            .balance(price * orderBook.getShareUnits())
                            .build());

                    usersService.fundBalance(UsersRequest.builder()
                            .id(orderBook.getUser().getId())
                            .balance(orderBook.getPrice() * orderBook.getShareUnits() - price * orderBook.getShareUnits())
                            .build());
                }

            }
        }

        OrderBooks newOrderBook = OrderBooks.builder()
                .user(Users.builder().id(orderBooksRequest.getUser().getId()).build())
                .stock(Stocks.builder().code(orderBooksRequest.getStock().getCode()).build())
                .shareUnits(shareUnits)
                .price(orderBooksRequest.getPrice())
                .isBuy(orderBooksRequest.getIsBuy())
                .build();
        if (shareUnits > 0) {
            orderBooksRepository.save(newOrderBook);
        }

        return OrderBooksRequest.builder()
                .id(newOrderBook.getId())
                .price(orderBooksRequest.getPrice())
                .stock(StocksRequest.builder()
                        .code(stocksOptional.get().getCode())
                        .name(stocksOptional.get().getName())
                        .build())
                .user(UsersRequest.builder()
                        .id(usersOptional.get().getId())
                        .name(usersOptional.get().getName())
                        .build())
                .isBuy(orderBooksRequest.getIsBuy())
                .shareUnits(orderBooksRequest.getShareUnits())
                .build();
    }

    public void deleteOrder(Long id) {
        Optional<OrderBooks> optionalOrderBooks = orderBooksRepository.findById(id);
        if (optionalOrderBooks.isEmpty())
            throw new RuntimeException("Order not found");
        OrderBooks orderBook = optionalOrderBooks.get();
        Long userId = orderBook.getUser().getId();
        String stockCode = orderBook.getStock().getCode();
        if(orderBook.getIsBuy()) {
            usersService.fundBalance(UsersRequest.builder()
                    .id(userId)
                    .balance(orderBook.getShareUnits() * orderBook.getPrice())
                    .build());
        } else {
            UserStocks userStocks = userStocksRepository.getById(new UserStocks.UserStockId(userId, stockCode));
            userStocksService.fundStock(UserStocksRequest.builder()
                    .userId(userId)
                    .stockCode(stockCode)
                    .shareUnits(orderBook.getShareUnits())
                    .price(userStocks.getPrice())
                    .build());
        }
        orderBooksRepository.deleteById(id);
    }

    public List<OrderBooksRequest> findAllBids(String stockCode, Double price, Boolean isBuy) {
        final List<OrderBooks> orderBooks;
        if (isBuy)
            orderBooks = orderBooksRepository.findBuyBid(stockCode, price);
        else
            orderBooks = orderBooksRepository.findSellBid(stockCode, price);
        List<OrderBooksRequest> orderBooksRequestList = new ArrayList<>();
        for(OrderBooks orderBook : orderBooks) {
            Stocks stock = orderBook.getStock();
            Users user = orderBook.getUser();

            orderBooksRequestList.add(
                    OrderBooksRequest.builder()
                            .id(orderBook.getId())
                            .isBuy(orderBook.getIsBuy())
                            .stock(StocksRequest.builder()
                                    .name(stock.getName())
                                    .code(stock.getCode())
                                    .build())
                            .user(UsersRequest.builder()
                                    .id(user.getId())
                                    .name(user.getName())
                                    .build())
                            .price(orderBook.getPrice())
                            .shareUnits(orderBook.getShareUnits())
                            .build()
            );
        }
        return orderBooksRequestList;
    }
}
