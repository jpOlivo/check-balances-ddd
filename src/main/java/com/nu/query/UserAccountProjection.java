package com.nu.query;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nu.dto.UserAccountDTO;
import com.nu.event.MoneyCreditedEvent;
import com.nu.event.MoneyDebitedEvent;
import com.nu.event.UserAccountCreatedEvent;

@Service
public class UserAccountProjection {
	private final List<UserAccountDTO> userAccounts = new CopyOnWriteArrayList<>();

	@Autowired
	private QueryUpdateEmitter queryUpdateEmitter;

	@EventHandler
	public void on(UserAccountCreatedEvent evt) {
		UserAccountDTO userAccountDTO = new UserAccountDTO(evt.getId(), evt.getInitialAmount());
		userAccounts.add(userAccountDTO);
	}

	@EventHandler
	public void on(MoneyDebitedEvent evt) {
		/*
		 * userAccountSummaries.stream().filter(cs ->
		 * evt.getId().equals(cs.getUsername())).findFirst() .ifPresent(userAccount -> {
		 * UserAccountDTO updatedUserAccountDTO =
		 * userAccount.subtractAmount(evt.getDebitAmount());
		 * userAccountSummaries.remove(userAccount);
		 * userAccountSummaries.add(updatedUserAccountDTO); });
		 * 
		 * queryUpdateEmitter.emit(String.class, userAccount ->
		 * userAccount.equals(evt.getId()), new UserAccountDTO(evt.getId(),
		 * evt.getDebitAmount()));
		 */
		Optional<UserAccountDTO> userAccountOptional = userAccounts.stream()
				.filter(cs -> evt.getId().equals(cs.getUsername())).findFirst();

		if (userAccountOptional.isPresent()) {
			UserAccountDTO userAccountDTO = userAccountOptional.get();
			UserAccountDTO updatedUserAccountDTO = userAccountDTO.subtractAmount(evt.getDebitAmount());
			userAccounts.remove(userAccountDTO);
			userAccounts.add(updatedUserAccountDTO);

			queryUpdateEmitter.emit(String.class, userAccount -> userAccount.equals(evt.getId()),
					updatedUserAccountDTO);
		}
	}

	@EventHandler
	public void on(MoneyCreditedEvent evt) {
		/*
		 * userAccountSummaries.stream().filter(cs ->
		 * evt.getId().equals(cs.getUsername())).findFirst() .ifPresent(userAccount -> {
		 * UserAccountDTO updatedUserAccountDTO =
		 * userAccount.addAmount(evt.getCreditAmount());
		 * userAccountSummaries.remove(userAccount);
		 * userAccountSummaries.add(updatedUserAccountDTO); });
		 * 
		 * queryUpdateEmitter.emit(String.class, userAccount ->
		 * userAccount.equals(evt.getId()), new UserAccountDTO(evt.getId(),
		 * evt.getCreditAmount()));
		 */
		Optional<UserAccountDTO> userAccountOptional = userAccounts.stream()
				.filter(cs -> evt.getId().equals(cs.getUsername())).findFirst();

		if (userAccountOptional.isPresent()) {
			UserAccountDTO userAccountDTO = userAccountOptional.get();
			UserAccountDTO updatedUserAccountDTO = userAccountDTO.addAmount(evt.getCreditAmount());
			userAccounts.remove(userAccountDTO);
			userAccounts.add(updatedUserAccountDTO);

			queryUpdateEmitter.emit(String.class, userAccount -> userAccount.equals(evt.getId()),
					updatedUserAccountDTO);
		}

	}

	@QueryHandler
	public UserAccountDTO fetch(String query) {
		return userAccounts.stream().filter(userAccount -> userAccount.getUsername().equals(query)).findFirst()
				.orElse(null);
	}

}
