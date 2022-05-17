package com.nathan.miniproject.repository;

import com.nathan.miniproject.domain.dao.OrderBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderBooksRepository extends JpaRepository<OrderBooks, Long> {
    @Query(value = "select o from OrderBooks o where o.stock.code = ?1 and o.price <= ?2 and o.isBuy = true " +
            "order by o.price asc, o.createdAt asc")
    public List<OrderBooks> findBuyBid(String stockCode, Double price);
    @Query(value = "select o from OrderBooks o where o.stock.code = ?1 and o.price >= ?2 and o.isBuy = false " +
            "order by o.price desc, o.createdAt asc")
    public List<OrderBooks> findSellBid(String stockCode, Double price);
}
