package com.example.service.interfaces;

import com.example.enums.TransferStatusEnum;
import com.example.model.Account;

public interface BankServiceInterface {
	TransferStatusEnum transfer(Account from, Account to, long amount);
	
	boolean withdraw(Account acc, long amount);
	
	void deposit(Account acc, long amount);
}
