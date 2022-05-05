package com.example.account.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.account.model.Account;
import com.example.account.repository.AccountRepository;
import com.example.account.service.AccountService;
import com.example.account.validator.AccountValidator;
import com.example.account.validator.PasswordValidator;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AccountRepository accountRepository;

	@Override
	public Account create(Account acc) throws CustomException {		
		AccountValidator.validateAccount(acc);		
		String password = acc.getPassword();
		PasswordValidator.validatePassword(password);
		acc.setPassword(passwordEncoder.encode(password));
		try {			
			return accountRepository.save(acc);
		} catch (Exception e) {
			log.error("Error in create: ", e);
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Account update(long id, Account acc, boolean nullableUpdate) throws CustomException {
		AccountValidator.validateAccount(acc);				
		try {
			Optional<Account> optAccount = accountRepository.findById(id);
			if(optAccount.isEmpty()) {
				throw new CustomException(APIStatus.NOT_FOUND, "Not found account by id: " + id);
			}
			Account accountInDB = optAccount.get();
			accountInDB = updateNewInfo(accountInDB, acc, nullableUpdate);
			Account updatedAccount = accountRepository.save(accountInDB);
			updatedAccount.setPassword(null);
			return updatedAccount;
		} catch (Exception e) {
			log.error("Error in update: ", e);
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void delete(long id) throws CustomException{
		try {
			Optional<Account> optAccount = accountRepository.findById(id);
			if(optAccount.isEmpty()) {
				throw new CustomException(APIStatus.NOT_FOUND, "Not found account by id: " + id);
			}
			Account accountInDB = optAccount.get();
			accountRepository.delete(accountInDB);
		} catch (Exception e) {
			log.error("Error in delete: ", e);
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public Account find(String username) throws UsernameNotFoundException  {
		Account account = accountRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException ("Username not found"));
		return account;
	}
	
	private Account updateNewInfo(Account oldAccount, Account newAccount, boolean nullableUpdate) throws CustomException{
		ObjectUtils.setFieldValue(newAccount, oldAccount, "firstName", nullableUpdate);
		ObjectUtils.setFieldValue(newAccount, oldAccount, "lastName", nullableUpdate);
		return oldAccount;
	}

}
