package com.example.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.account.model.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {

}
