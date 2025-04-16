package com.example.enums;

public enum TransferStatus {
	SUCCESS, // 成功
	INTERRUPTED, // 執行緒被中斷
	LOCK_TIMEOUT, // 取得鎖定超時
	INSUFFICIENT_BALANCE,// 餘額不足
	OTHER;// 其他
}
