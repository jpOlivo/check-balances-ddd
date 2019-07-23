package com.nu.controller;

import javax.validation.constraints.NotBlank;

import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nu.dto.UserAccountDTO;
import com.nu.service.UserAccountQueryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;

@RestController
@Validated
public class UserAccountQueryController {
	@Autowired
	@Qualifier("queryGateway")
	private UserAccountQueryService userAccountQueryService;

	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = UserAccountDTO.class),
			@ApiResponse(code = 400, message = "Bad request") })
	@ApiOperation(value = "Return balance of an user account", response = UserAccountDTO.class)
	@GetMapping("/api/accounts/{name}/balance")
	public ResponseEntity<UserAccountDTO> getBalance(
			@ApiParam(required = true, name = "name", value = "Name of the user who owns the account") @PathVariable @NotBlank String name) {
		return ResponseEntity.ok().body(userAccountQueryService.getUserAccount(name));

	}

	@GetMapping(value = "/api/accounts/{name}/balance/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 400, message = "Bad request")})
	@ApiOperation(value = "Subscribes to updates on balance of an user account")
	public ResponseEntity<Flux<UserAccountDTO>> suscribeBalanceUserAccount(
			@ApiParam(required = true, name = "name", value = "Name of the user who owns the account") @PathVariable @NotBlank String name) {

		SubscriptionQueryResult<UserAccountDTO, UserAccountDTO> subscriptionQueryResult = userAccountQueryService
				.subscribeUserAccount(name);

		/*
		 * If you only want to send new messages to the client, you could simply do:
		 * return result.updates(); However, in our implementation we want to provide
		 * both existing messages and new ones, so we combine the initial result and the
		 * updates in a single flux.
		 */
		Flux<UserAccountDTO> initialResult = subscriptionQueryResult.initialResult().flatMapMany(Flux::just);
		return ResponseEntity.ok().body(Flux.concat(initialResult, subscriptionQueryResult.updates()));

	}

}
