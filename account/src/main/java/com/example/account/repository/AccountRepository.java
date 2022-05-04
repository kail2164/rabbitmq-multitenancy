package com.example.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUsernameIgnoreCase(String username);
	
    @Query("SELECT a.id FROM tbl_account a ORDER BY a.id DESC")
	List<Long> getAllAccountId();
}
