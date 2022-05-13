package com.nathan.miniproject.repository;

import com.nathan.miniproject.domain.dao.UserStocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStocksRepository extends JpaRepository<UserStocks, UserStocks.UserStockId> {
}
