package com.nu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nu.dto.UserAccountDTO;
import com.nu.event.MoneyCreditedEvent;
import com.nu.event.MoneyDebitedEvent;
import com.nu.event.UserAccountCreatedEvent;

@Service
@Qualifier("eventStore")
public class UserAccountQueryServiceImpl implements UserAccountQueryService {
	@Autowired
	private EventStore eventStore;

	@Override
	public UserAccountDTO getUserAccount(String username) {
		List<Object> events = eventStore.readEvents(username).asStream().map(s -> s.getPayload())
				.collect(Collectors.toList());

		// TODO: improve implementation
		BigDecimal balance = null;
		for (Object object : events) {
			if (object instanceof UserAccountCreatedEvent) {
				balance = ((UserAccountCreatedEvent) object).getInitialAmount();
			}

			if (object instanceof MoneyCreditedEvent) {
				balance = balance.add(((MoneyCreditedEvent) object).getCreditAmount());
			}

			if (object instanceof MoneyDebitedEvent) {
				balance = balance.subtract(((MoneyDebitedEvent) object).getDebitAmount());
			}
		}

		return new UserAccountDTO(username, balance);
	}

	@Override
	public SubscriptionQueryResult<UserAccountDTO, UserAccountDTO> subscribeUserAccount(String username) {
		throw new UnsupportedOperationException("Not implemented yet");
	}


}
