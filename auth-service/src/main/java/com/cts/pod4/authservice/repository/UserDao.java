package com.cts.pod4.authservice.repository;

import com.cts.pod4.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository <User, Integer> {
    User findByUsername(String username);
}
