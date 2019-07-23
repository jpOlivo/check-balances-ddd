package com.nu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.nu.constraint.Operation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(description = "Representation of a money transaction")
public class TransactionDTO implements Serializable {

	@ApiModelProperty(value = "The amount of transaction")
	@NotNull
	@Positive
	private BigDecimal amount;

	@ApiModelProperty(value = "The operation type", allowableValues = "IN, OUT")
	@NotNull()
	@Operation(value = { OperationType.IN, OperationType.OUT }, ignoreCase = true)
	private String operationType;

	public TransactionDTO(@NotNull @Positive BigDecimal amount, @NotNull String operationType) {
		super();
		this.amount = amount;
		this.operationType = operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return "TransactionDTO {" + "amount: '" + amount + '\'' + ", operationType: " + operationType + '}';
	}

}
