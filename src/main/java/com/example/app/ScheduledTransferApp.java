package com.example.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.example.model.Account;
import com.example.service.BankService;
import com.example.service.interfaces.BankServiceInterface;
import com.example.task.ScheduledTransferTask;

public class ScheduledTransferApp {

	public static void main(String[] args) {
		Account from = new Account(1, new AtomicLong(1000));
		Account to = new Account(2, new AtomicLong(500));
		BankServiceInterface bankService = new BankService();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable scheduledTask = new ScheduledTransferTask(bankService, from, to, 50);
		
		// 定時任務，每 1 秒轉帳一次
		scheduler.scheduleAtFixedRate(scheduledTask, 0, 1, TimeUnit.SECONDS);
		
		// 定時任務，10 秒後關閉排程
		scheduler.schedule(shutdownScheduledTask(scheduler, from, to), 10, TimeUnit.SECONDS);
	}
	
	private static Runnable shutdownScheduledTask (ScheduledExecutorService scheduler, Account from, Account to) {
		return () -> {
			System.out.println("結束定時任務");
			scheduler.shutdown();
			try {
				if(!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
					scheduler.shutdownNow();
				}
			}catch (InterruptedException e) {
				scheduler.shutdownNow();
			}
			
			System.out.println("帳戶：" + from.getId() + " 餘額：" + from.getBalance());
			System.out.println("帳戶：" + to.getId() + " 餘額：" + to.getBalance());
		};
	}
}
