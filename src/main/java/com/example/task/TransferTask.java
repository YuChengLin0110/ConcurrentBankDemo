package com.example.task;

import com.example.enums.TransferStatus;
import com.example.log.TransactionLog;
import com.example.model.Account;
import com.example.service.BankService;

public class TransferTask implements Runnable {
	private final BankService bank;
	private final Account from;
	private final Account to;
	private final long amount;

	public TransferTask(BankService bank, Account from, Account to, long amount) {
		this.bank = bank;
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	@Override
	public void run() {
		TransferStatus status = bank.transfer(from, to, amount);

		TransactionLog.logTransferResult(from, to, amount, status);
	}
}
