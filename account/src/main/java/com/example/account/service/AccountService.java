package com.example.account.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.account.model.Account;
import com.example.common.dto.CustomException;

public interface AccountService {
	Account create(Account acc) throws CustomException;
	Account update(long id, Account acc) throws CustomException;
	Account find(String username) throws UsernameNotFoundException ;
	void delete(long id) throws CustomException;	
}
