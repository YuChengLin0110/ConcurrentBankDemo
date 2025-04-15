package com.example.model;

import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private final int id;
	private long balance;
	private final ReentrantLock lock = new ReentrantLock();

	public Account(int id, Long balance) {
		this.id = id;
		this.balance = balance;
	}

	public void deposit(long amount) {
		lock.lock();
		try {
			balance += amount;
		} finally {
			lock.unlock();
		}
	}

	public boolean withdraw(long amount) {
		lock.lock();
		try {
			if (balance >= amount) {
				balance -= amount;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	public int getId() {
		return id;
	}

	public long getBalance() {
		lock.lock();
		try {
			return balance;
		} finally {
			lock.unlock();
		}
	}

	public ReentrantLock getLock() {
		return lock;
	}
}
