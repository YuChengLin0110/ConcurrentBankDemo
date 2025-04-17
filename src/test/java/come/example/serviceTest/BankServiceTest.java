package come.example.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

import com.example.enums.TransferStatusEnum;
import com.example.model.Account;
import com.example.service.BankService;
import com.example.service.interfaces.BankServiceInterface;

public class BankServiceTest {
	
	BankServiceInterface bankService = new BankService();
	
	@Test
	void depositTest() {
		Account acc = new Account(1, new AtomicLong(1000));
		bankService.deposit(acc, 500);
		assertEquals(1500, acc.getBalance());
	}
	
	@Test
	void withdrawTest() {
		Account acc = new Account(1, new AtomicLong(1000));
		boolean result = bankService.withdraw(acc, 500);
		assertTrue(result);
		assertEquals(500, acc.getBalance());
	}
	
	@Test
	void withdrawFailTest() {
		Account acc = new Account(1, new AtomicLong(100));
		boolean result = bankService.withdraw(acc, 500);
		assertFalse(result);
		assertEquals(100, acc.getBalance());
	}
	
	@Test
	void transferSuccessTest() {
		Account from = new Account(1, new AtomicLong(1000));
	    Account to = new Account(2, new AtomicLong(500));
	    
	    TransferStatusEnum status = bankService.transfer(from, to, 300);
	    
	    assertEquals(TransferStatusEnum.SUCCESS, status);
	    assertEquals(700, from.getBalance());
	    assertEquals(800, to.getBalance());
	}
	
	@Test
	void transferFailTest() {
		Account from = new Account(1, new AtomicLong(100));
	    Account to = new Account(2, new AtomicLong(500));
	    
	    TransferStatusEnum status = bankService.transfer(from, to, 300);
	    
	    assertEquals(TransferStatusEnum.INSUFFICIENT_BALANCE, status);
	    assertEquals(100, from.getBalance());
	    assertEquals(500, to.getBalance());
	}
	
}
