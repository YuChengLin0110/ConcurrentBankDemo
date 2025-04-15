package com.example.task;

import com.example.enums.TransferStatus;
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

		switch (status) {
		case SUCCESS:
			System.out.println("帳戶:" + from.getId() + " 轉帳 " + amount + " 元 給帳戶:" + to.getId());
			break;
		case INSUFFICIENT_BALANCE:
			System.out.println("帳戶:" + from.getId() + " 餘額不足，轉帳失敗");
			break;
		case LOCK_TIMEOUT:
			System.out.println("轉帳超時：帳戶 " + from.getId() + " ➜ " + to.getId());
			break;
		case INTERRUPTED:
			System.out.println("執行緒中斷，轉帳失敗");
			break;
		}
	}
}
