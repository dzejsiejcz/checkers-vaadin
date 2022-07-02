package com.checkers.web.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class MessagesList extends Div {

    public MessagesList() {
        addClassName("message-list");
    }


    @Override
    public void add(Component ... components) {
        super.add(components);

        components[components.length-1]
                .getElement()
                .callJsFunction("scrollIntoView");
    }
}
