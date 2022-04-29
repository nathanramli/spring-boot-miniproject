package com.nathan.miniproject.repository;

import com.nathan.miniproject.domain.dao.Stocks;
import com.nathan.miniproject.domain.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, String> {
}
