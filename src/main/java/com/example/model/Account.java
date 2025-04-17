package com.example.model;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private final int id;
	private AtomicLong balance;
	private final ReentrantLock lock = new ReentrantLock();

	public Account(int id, AtomicLong balance) {
		this.id = id;
		this.balance = balance;
	}

	
	public int getId() {
		return id;
	}

	public long getBalance() {
		return balance.get();
	}
	
	public boolean updateBalance(long oldBalance, long newBalance) {
		return balance.compareAndSet(oldBalance, newBalance);
	}

	public ReentrantLock getLock() {
		return lock;
	}
}
