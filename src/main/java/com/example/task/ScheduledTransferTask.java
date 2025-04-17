package com.example.task;

import com.example.enums.TransferStatusEnum;
import com.example.log.TransactionLog;
import com.example.model.Account;
import com.example.service.interfaces.BankServiceInterface;

public class ScheduledTransferTask implements Runnable{
	private final BankServiceInterface bankService;
	private final Account from;
	private final Account to;
	private final long amount;
	
	public ScheduledTransferTask (BankServiceInterface bankService, Account from, Account to, long amount) {
		this.bankService = bankService;
        this.from = from;
        this.to = to;
        this.amount = amount;
	}
	
	@Override
	public void run() {
		TransferStatusEnum status = bankService.transfer(from, to, amount);
		TransactionLog.logTransferResult(from, to, amount, status);
	}
	
}
