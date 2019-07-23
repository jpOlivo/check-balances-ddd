package com.nu.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nu.command.CreateUserAccountCommand;
import com.nu.command.CreditMoneyCommand;
import com.nu.command.DebitMoneyCommand;
import com.nu.dto.OperationType;
import com.nu.dto.TransactionDTO;
import com.nu.dto.UserAccountDTO;
import com.nu.exception.TransactionException;

@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {
	@Autowired
	private CommandGateway commandGateway;

	@Override
	public CompletableFuture<String> createAccount(UserAccountDTO accountCreateDTO) {
		return commandGateway
				.send(new CreateUserAccountCommand(accountCreateDTO.getUsername(), accountCreateDTO.getBalance()));
	}

	@Override
	public CompletableFuture<String> performTransactionOnAccount(String username, TransactionDTO transactionDTO) {
		
		String ucOperationType = transactionDTO.getOperationType().toUpperCase();
		if (!Arrays.asList(OperationType.values()).stream().map(ot -> ot.toString()).collect(Collectors.toList())
				.contains(ucOperationType)) {
			throw new TransactionException(
					"Operation type not allowed. Allowed values: " + OperationType.IN + ", " + OperationType.OUT);
		}

		OperationType operationType = OperationType.valueOf(ucOperationType);
		switch (operationType) {
		case IN:
			return commandGateway.send(new CreditMoneyCommand(username, transactionDTO.getAmount()));

		case OUT:
			return commandGateway.send(new DebitMoneyCommand(username, transactionDTO.getAmount()));

		/*
		 * default: throw new TransactionException(
		 * "Operation type not allowed. Allowed values: " + OperationType.IN + ", " +
		 * OperationType.OUT);
		 */
		}
		return null;
	}

}
