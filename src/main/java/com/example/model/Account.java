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

	public void deposit(long amount) {
		balance.addAndGet(amount);
	}
	
	/* 使用Atomic
	 * 先讀取目前balance
	 * 計算好新的balance
	 * 使用compareAndSet : 檢查當前 balance 是否與讀取到的值相同
	 * true : 則更新為新的balance ， false : 則balance被修改過，重新讀取計算
	 * */
	public boolean withdraw(long amount) {
		while(true) {
			long curr = balance.get();
			
			if(curr < amount) {
				return false;
			}
			
			long newBalance = curr - amount;
			
			if(balance.compareAndSet(curr, newBalance)) {
				return true;
			}
		}
	}

	public int getId() {
		return id;
	}

	public long getBalance() {
		return balance.get();
	}

	public ReentrantLock getLock() {
		return lock;
	}
}
