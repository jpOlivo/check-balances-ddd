package com.nu.service;

import java.util.concurrent.CompletableFuture;

import com.nu.dto.TransactionDTO;
import com.nu.dto.UserAccountDTO;

public interface UserAccountCommandService {

	CompletableFuture<String> createAccount(UserAccountDTO accountCreateDTO);

	CompletableFuture<String> performTransactionOnAccount(String username, TransactionDTO transactionDTO);
}
