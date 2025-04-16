package com.example.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.enums.TransferStatus;
import com.example.model.Account;

public class TransactionLog {
	private static final Logger logger = LoggerFactory.getLogger(TransactionLog.class);
	
	public static void logTransferResult(Account from, Account to, long amount, TransferStatus status) {
        switch (status) {
            case SUCCESS -> logger.info("轉帳成功：從帳戶 {} 轉出 {} 元至帳戶 {}", from.getId(), amount, to.getId());
            case INSUFFICIENT_BALANCE -> logger.warn("帳戶 {} 餘額不足，無法轉出 {} 元", from.getId(), amount);
            case LOCK_TIMEOUT -> logger.warn("轉帳失敗：帳戶 {} 或帳戶 {} 鎖定逾時", from.getId(), to.getId());
            case INTERRUPTED -> logger.error("轉帳中斷：從帳戶 {} 到帳戶 {}", from.getId(), to.getId());
            default -> logger.warn("其他錯誤：{}", status);
        }
    }
}
