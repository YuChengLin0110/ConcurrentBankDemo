package com.example.service;

import java.util.concurrent.TimeUnit;

import com.example.enums.TransferStatus;
import com.example.model.Account;

public class BankService {
	private final int timeout = 1;
	private final TimeUnit timeUnit = TimeUnit.SECONDS;

	public TransferStatus transfer(Account from, Account to, long amount) {
		boolean fromLocked = false;
		boolean toLocked = false;

		try {
			if (lockAccounts(from, to)) {
				fromLocked = true;
				toLocked = true;
			}

			if (fromLocked && toLocked) {
				if (!from.withdraw(amount)) {
					return TransferStatus.INSUFFICIENT_BALANCE;
				}

				to.deposit(amount);
				return TransferStatus.SUCCESS;
			} else {
				return TransferStatus.LOCK_TIMEOUT;
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return TransferStatus.INTERRUPTED;
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
}
