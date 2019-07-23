package com.nu.service;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nu.dto.UserAccountDTO;
import com.nu.exception.UserAccountException;

@Service
@Qualifier("queryGateway")
public class UserAccountQueryServiceImpl2 implements UserAccountQueryService {
	@Autowired
	private QueryGateway queryGateway;

	@Override
	public UserAccountDTO getUserAccount(String username) {
		UserAccountDTO userAccountDTO = queryGateway.query(username, ResponseTypes.instanceOf(UserAccountDTO.class)).join();
		if(userAccountDTO == null) {
			throw new UserAccountException("The user account with name " + username + " was not found");
		}
		return userAccountDTO;

	}

	@Override
	public SubscriptionQueryResult<UserAccountDTO, UserAccountDTO> subscribeUserAccount(String username) {
		return queryGateway.subscriptionQuery(username, ResponseTypes.instanceOf(UserAccountDTO.class),
				ResponseTypes.instanceOf(UserAccountDTO.class));
	}

}
