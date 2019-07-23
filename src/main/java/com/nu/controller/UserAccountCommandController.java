package com.nu.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nu.dto.ResponseTransactionDTO;
import com.nu.dto.TransactionDTO;
import com.nu.dto.UserAccountDTO;
import com.nu.exception.OperationNotCompletedException;
import com.nu.service.UserAccountCommandService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Validated
public class UserAccountCommandController {

	@Autowired
	private UserAccountCommandService userAccountCommandService;

	@ApiOperation(value = "Register a new monetary transaction", response = ResponseTransactionDTO.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = ResponseTransactionDTO.class),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 409, message = "Conflict") })
	@PutMapping("/api/accounts/{name}/transactions")
	public ResponseEntity<ResponseTransactionDTO> registerTransaction(
			@ApiParam(required = true, name = "name", value = "Name of the user who owns the account") @PathVariable @NotBlank String name,
			@ApiParam(required = true, name = "transaction", value = "Money transaction") @Valid @RequestBody TransactionDTO transactionDTO) {

		try {
			userAccountCommandService.performTransactionOnAccount(name, transactionDTO).get();
		} catch (Exception e) {
			throw new OperationNotCompletedException(e.getMessage());
		}

		return ResponseEntity.ok().body(new ResponseTransactionDTO(UUID.randomUUID()));

	}

	@ApiOperation(value = "Register a new user account", response = String.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = String.class),
		@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 409, message = "Conflict") })
	@PostMapping("/api/accounts")
	public ResponseEntity<String> createUserAccount(@ApiParam(required = true, value = "Data user account") @Valid @RequestBody UserAccountDTO accountCreateDTO) {
		String accountIdentifier = null;
		try {
			CompletableFuture<String> result = userAccountCommandService.createAccount(accountCreateDTO);
			accountIdentifier = result.get();
		} catch (Exception e) {
			throw new OperationNotCompletedException(
					"Is not possible create user account with id " + accountCreateDTO.getUsername());
		}

		return ResponseEntity.ok().body("User account created with ID: " + accountIdentifier);
	}

}
