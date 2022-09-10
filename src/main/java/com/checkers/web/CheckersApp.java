package com.checkers.web;

import com.checkers.web.utils.ChatMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;


@SpringBootApplication
public class CheckersApp {

	public static void main(String[] args) {
		SpringApplication.run(CheckersApp.class, args);
	}

	@Bean
	UnicastProcessor<ChatMessage> publisher() {
		return UnicastProcessor.create();
	}

	@Bean
	Flux<ChatMessage> messages(UnicastProcessor<ChatMessage> publisher) {
		return publisher.replay(30).autoConnect();
	}

}
