package com.nu.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nu.dto.OperationType;
import com.nu.dto.TransactionDTO;
import com.nu.dto.UserAccountDTO;
import com.nu.event.MoneyCreditedEvent;
import com.nu.exception.TransactionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountCommandServiceTest {

	@Autowired
	private UserAccountCommandService userAccountCommandService;

	@Autowired
	private EventStore eventStore;

	@Test
	public void createUserAccount() {

		UserAccountDTO userAccountDTO = new UserAccountDTO("juan", BigDecimal.valueOf(0));
		CompletableFuture<String> result = userAccountCommandService.createAccount(userAccountDTO);
		try {
			assertEquals("juan", result.get());
		} catch (InterruptedException | ExecutionException e) {
			fail("The creation of user account has failed");
		}

	}

	@Test
	public void performTransactionOnAccount() {

		UserAccountDTO userAccountDTO = new UserAccountDTO("juan", BigDecimal.valueOf(0));
		userAccountCommandService.createAccount(userAccountDTO);

		TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.valueOf(100), OperationType.IN.toString());

		CompletableFuture<String> result = userAccountCommandService.performTransactionOnAccount("juan",
				transactionDTO);

		assertEquals(true, !result.isCompletedExceptionally());

		MoneyCreditedEvent event = (MoneyCreditedEvent) eventStore.readEvents("juan").asStream()
				.map(e -> e.getPayload()).filter(p -> p instanceof MoneyCreditedEvent).findFirst().get();

		assertEquals("juan", event.getId());
		assertEquals(BigDecimal.valueOf(100), event.getCreditAmount());

	}
	
	@Test(expected = TransactionException.class)
	public void performInvalidTransactionOnAccount() {

		UserAccountDTO userAccountDTO = new UserAccountDTO("juan", BigDecimal.valueOf(0));
		userAccountCommandService.createAccount(userAccountDTO);

		TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.valueOf(100), "input");

		userAccountCommandService.performTransactionOnAccount("juan",
				transactionDTO);

	}

}
