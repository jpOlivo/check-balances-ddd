package com.nu.service;

import org.axonframework.queryhandling.SubscriptionQueryResult;

import com.nu.dto.UserAccountDTO;

public interface UserAccountQueryService {
	UserAccountDTO getUserAccount(String username);

	SubscriptionQueryResult<UserAccountDTO, UserAccountDTO>  subscribeUserAccount(String username);
}
