package com.example.account.service.impl;

import java.util.Optional;

import javax.annotation.PostConstruct;

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


@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AccountRepository accountRepository;
	@PostConstruct
	private void createTest() throws CustomException {
		Account acc = new Account();
		acc.setFirstName("Test");
		acc.setLastName("Admin");
		acc.setPassword("12345678@Ab");
		acc.setUsername("ad");
		acc.setRole("admin");
//		create(acc);
	}
	
	@Override
	public Account create(Account acc) throws CustomException {		
		AccountValidator.validateAccount(acc);		
		String password = acc.getPassword();
		PasswordValidator.validatePassword(password);
		acc.setPassword(passwordEncoder.encode(password));
		try {
			Account newAccount = accountRepository.save(acc);
			newAccount.setPassword(null);
			return newAccount;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public Account update(long id, Account acc) throws CustomException {
		AccountValidator.validateAccount(acc);		
		String password = acc.getPassword();
		PasswordValidator.validatePassword(password);
		try {
			Optional<Account> optAccount = accountRepository.findById(id);
			if(optAccount.isEmpty()) {
				throw new CustomException(APIStatus.NOT_FOUND, "Not found account by id: " + id);
			}
			Account accountInDB = optAccount.get();
			accountInDB = updateNewInfo(accountInDB, acc);
			Account updatedAccount = accountRepository.save(accountInDB);
			updatedAccount.setPassword(null);
			return updatedAccount;
		} catch (Exception e) {
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
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public Account find(String username) throws UsernameNotFoundException  {
		Account account = accountRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException ("Username not found"));
		return account;
	}
	
	private Account updateNewInfo(Account oldAccount, Account newAccount) {
		oldAccount.setPassword(passwordEncoder.encode(newAccount.getPassword()));
		oldAccount.setFirstName(newAccount.getFirstName());
		oldAccount.setLastName(newAccount.getLastName());
		return oldAccount;
	}

}
