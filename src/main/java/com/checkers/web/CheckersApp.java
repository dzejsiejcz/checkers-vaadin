package com.checkers.web;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

//@Push
@SpringBootApplication
public class CheckersApp {

	public static void main(String[] args) {
		SpringApplication.run(CheckersApp.class, args);
	}

}
