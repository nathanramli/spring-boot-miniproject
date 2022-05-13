package com.nathan.miniproject.repository;

import com.nathan.miniproject.domain.dao.UserStocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStocksRepository extends JpaRepository<UserStocks, UserStocks.UserStockId> {
    public List<UserStocks> findAllByUserId(Long id);
}
