package com.nu.client;

import org.springframework.web.reactive.function.client.WebClient;

import com.nu.dto.UserAccountDTO;

import reactor.core.publisher.Flux;

public class UserAccountWebClient {
	 private static WebClient client = WebClient.create("http://localhost:8080");
	 
	 public static void main(String [] args) {
		 Flux<UserAccountDTO> userAccountFlux = client.get().uri("/api/accounts/{name}/balance/subscribe", "juan").retrieve().bodyToFlux(UserAccountDTO.class);
		 userAccountFlux.subscribe(System.out::println);
		 System.out.println("test end");
		 
	 }
}
