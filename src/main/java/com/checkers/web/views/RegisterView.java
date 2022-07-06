package com.checkers.web.views;

import com.checkers.web.service.CustomUserDetailsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegisterView extends Composite {


    private final CustomUserDetailsService userDetailsService;

    public RegisterView(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");

        return new VerticalLayout(
                new H2("Register"),
                username,
                password1,
                password2,
                new Button("Send", event -> register(
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue()
                )),
                new RouterLink("Log in", LoginView.class)
        );
    }

    private void register(String username, String password1, String password2) {
        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            userDetailsService.register(username, password1);
            Notification.show("Registration succeeded");
        }
    }
}
