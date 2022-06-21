package com.checkers.web;

import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Push
public class CheckersApp {

	public static void main(String[] args) {
		SpringApplication.run(CheckersApp.class, args);
	}

}
