package come.example.stressTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

import com.example.model.Account;
import com.example.service.BankService;
import com.example.service.interfaces.BankServiceInterface;
import com.example.task.TransferTask;

public class TransferStressTest {
	
	@Test
	void TransferStressTest() throws InterruptedException {
		int numAccounts = 10;
		int balance = 1000;
		int numTransfers = 1000;
		
		List<Account> accounts = new ArrayList<>();
		for(int i = 0 ; i < numAccounts ; i++) {
			accounts.add(new Account(i, new AtomicLong(balance)));
		}
		
		BankServiceInterface bankService = new BankService();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Random random = new Random();
		
		for(int i = 0 ; i < numTransfers ; i++) {
			Account from = accounts.get(random.nextInt(numAccounts));
			Account to = accounts.get(random.nextInt(numAccounts));
			long amount = random.nextInt(100) + 1;
			
			if(from != to) {
				executor.submit(new TransferTask(bankService, from, to, amount));
			}
		}
		
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
		
		long finalTotal = 0;
		
		for(Account account : accounts) {
			finalTotal += account.getBalance();
		}
		
		long total = accounts.size() * balance;
		
		assertEquals(finalTotal, total, "total不相同， 正確：" + total + "，實際：" + finalTotal);
	}
}
