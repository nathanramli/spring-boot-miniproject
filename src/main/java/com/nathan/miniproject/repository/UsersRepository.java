package com.nathan.miniproject.repository;

import com.nathan.miniproject.domain.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users getDistinctTopByUsername(String username);
}
