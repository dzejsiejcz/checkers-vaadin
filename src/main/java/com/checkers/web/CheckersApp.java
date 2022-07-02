package com.checkers.web;

import com.checkers.web.utils.ChatMessage;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.rmi.server.UnicastRemoteObject;

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
