package com.checkers.web.security;

import com.checkers.web.service.CustomUserDetailsService;
import com.checkers.web.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration
        extends VaadinWebSecurityConfigurerAdapter {

    //@Resource(name = "authService")
    @Autowired
    private UserDetailsService userDetailsService;

//    public SecurityConfiguration(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Delegating the responsibility of general configurations
        // of http security to the super class. It is configuring
        // the followings: Vaadin's CSRF protection by ignoring
        // framework's internal requests, default request cache,
        // ignoring public views annotated with @AnonymousAllowed,
        // restricting access to other views/endpoints, and enabling
        // ViewAccessChecker authorization.
        // You can add any possible extra configurations of your own
        // here (the following is just an example):

        // http.rememberMe().alwaysRemember(false);


//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/game", "/")
//                .hasAuthority("USER")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/accessDenied")
                .and().authorizeRequests()
                .antMatchers("/VAADIN/**", "/PUSH/**", "/UIDL/**", "/error/**", "/accessDenied/**", "/vaadinServlet/**")
                .permitAll()
                        .and()
                                .sessionManagement().sessionFixation().newSession();
                //.antMatchers("/", "/game").fullyAuthenticated();


        super.configure(http);

        // This is important to register your login view to the
        // view access checker mechanism:
        setLoginView(http, LoginView.class);
    }

    /**
     * Allows access to static resources, bypassing Spring security.=
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Configure your static resources with public access here:
        web.ignoring().antMatchers(
                "/icons/**"
        );



        // Delegating the ignoring configuration for Vaadin's
        // related static resources to the super class:
        super.configure(web);
    }

    /**
     * Demo UserDetailService which only provide two hardcoded
     * in memory users and their roles.
     * NOTE: This should not be used in real world applications.
     */
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService(String username) {
//
//        return  new CustomUserDetailsService(username);
//    }
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

}