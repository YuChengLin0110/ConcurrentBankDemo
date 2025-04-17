package com.example.service;

import java.util.concurrent.TimeUnit;

import com.example.enums.TransferStatusEnum;
import com.example.model.Account;
import com.example.service.interfaces.BankServiceInterface;

public class BankService implements BankServiceInterface{
	private final int timeout = 1;
	private final TimeUnit timeUnit = TimeUnit.SECONDS;
	

	public void deposit(Account acc, long amount) {
		updateBalance(acc, amount);
	}


	public boolean withdraw(Account acc, long amount) {
		return updateBalance(acc, -amount);
	}

	public TransferStatusEnum transfer(Account from, Account to, long amount) {
		boolean fromLocked = false;
		boolean toLocked = false;

		try {
			if (lockAccounts(from, to)) {
				fromLocked = true;
				toLocked = true;
			}

			if (fromLocked && toLocked) {
				if (!withdraw(from, amount)) {
					return TransferStatusEnum.INSUFFICIENT_BALANCE;
				}

				deposit(to, amount);
				return TransferStatusEnum.SUCCESS;
			} else {
				return TransferStatusEnum.LOCK_TIMEOUT;
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return TransferStatusEnum.INTERRUPTED;
		} finally {
			unlockAccounts(from, to, fromLocked, toLocked);
		}
	}

	// 固定鎖定順序，避免死鎖
	private boolean lockAccounts(Account from, Account to) throws InterruptedException {
		boolean fromLocked = false;
		boolean toLocked = false;

		if (from.hashCode() < to.hashCode()) {
			fromLocked = from.getLock().tryLock(timeout, timeUnit);
			toLocked = to.getLock().tryLock(timeout, timeUnit);
		} else {
			toLocked = to.getLock().tryLock(timeout, timeUnit);
			fromLocked = from.getLock().tryLock(timeout, timeUnit);
		}

		return fromLocked && toLocked;
	}

	// 解鎖順序和鎖定順序相反
	private void unlockAccounts(Account from, Account to, boolean fromLocked, boolean toLocked) {
		if (from.hashCode() < to.hashCode()) {
			if (toLocked) {
				to.getLock().unlock();
			}

			if (fromLocked) {
				from.getLock().unlock();
			}
		} else {
			if (fromLocked) {
				from.getLock().unlock();
			}

			if (toLocked) {
				to.getLock().unlock();
			}
		}
	}
	
	/*
	 * 使用Atomic 先讀取目前balance 計算好新的balance 使用compareAndSet : 檢查當前 balance 是否與讀取到的值相同
	 * true : 則更新為新的balance ， false : 則balance被修改過，重新讀取計算
	 */
	private boolean updateBalance(Account acc, long amount) {
		while(true) {
			long curr = acc.getBalance();
			long newBalance = curr + amount;
			
			if(newBalance < 0) {
				return false;
			}
			
			if(acc.updateBalance(curr, newBalance)) {
				return true;
			}
		}
	}
}
