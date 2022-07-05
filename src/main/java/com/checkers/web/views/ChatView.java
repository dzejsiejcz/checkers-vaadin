package com.checkers.web.views;

import com.checkers.web.utils.ChatMessage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import javax.annotation.security.PermitAll;

@Route(value = "chat", layout = MainLayout.class)
@CssImport("./styles/styles.css")
@PermitAll
//@Push(value= PushMode.MANUAL, transport = Transport.WEBSOCKET_XHR)
public class ChatView extends VerticalLayout {

    private UnicastProcessor<ChatMessage> publisher;
    private Flux<ChatMessage> messages;
    final SecurityContext securityContext = SecurityContextHolder.getContext();

    public ChatView(UnicastProcessor<ChatMessage> publisher, Flux<ChatMessage> messages) {
        this.publisher = publisher;
        this.messages = messages;
//        Authentication authentication = securityContext.getAuthentication();
//        if(authentication==null) {
//            authentication = vaadinSec.getAuthentication();
//        }
//        if(authentication==null) {
//            throw new RuntimeException("No authentication found!");
//        }


        H1 header = new H1("Chat");
        add(header);

        showChat();
    }

    private void showChat() {
        MessagesList messagesList = new MessagesList();
        expand(messagesList);
        add(messagesList, createInputLayout());

        messages.subscribe(message -> {
            getUI().ifPresent(ui->
                    ui.access(()->
                            messagesList.add(
                                    new Paragraph(message.getFrom() + ": " + message.getMessage())
                            )
                    ));
        });
    }

    private Component createInputLayout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        TextField messageField = new TextField();
        Button sendButton = new Button("Send");

        layout.add(messageField, sendButton);
        layout.expand(messageField);

        sendButton.addClickListener(clisk -> {
           publisher.onNext(new ChatMessage(username, messageField.getValue()));
           messageField.clear();
           messageField.focus();
        });
        messageField.focus();

        return layout;
    }
}
