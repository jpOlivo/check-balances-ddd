package com.nu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(description = "Representation of a user account")
public class UserAccountDTO implements Serializable {
	@ApiModelProperty(value = "The username of account")
	@NotBlank
	private String username;

	@ApiModelProperty(value = "The balance of account")
	@NotNull
	@PositiveOrZero
	private BigDecimal balance;

	public UserAccountDTO() {
		super();
	}
	
	public UserAccountDTO(@NotBlank String username, @NotNull @PositiveOrZero BigDecimal balance) {
		super();
		this.username = username;
		this.balance = balance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public UserAccountDTO subtractAmount(BigDecimal toBeDeducted) {
		return new UserAccountDTO(username, balance.subtract(toBeDeducted));
	}

	public UserAccountDTO addAmount(BigDecimal toBeAdded) {
		return new UserAccountDTO(username, balance.add(toBeAdded));
	}

	@Override
	public String toString() {
		return "UserAccountDTO {" + "id: '" + username + '\'' + ", balance: " + balance + '}';
	}

}
