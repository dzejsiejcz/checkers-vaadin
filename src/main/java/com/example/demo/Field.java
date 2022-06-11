package com.example.demo;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.StreamResource;


public class Field extends Image {

    StreamResource resourceBlue = new StreamResource("48px-BlueSquare.png", () -> getClass().getResourceAsStream("/icons/48px-BlueSquare.png"));
    StreamResource resourceWhite = new StreamResource("48px-WhiteSquare.png", () -> getClass().getResourceAsStream("/icons/48px-WhiteSquare.png"));

    public Field(String color) {
        if (color.equals("#0000FF")) {
            setSrc(resourceBlue);
        } else {
            setSrc(resourceWhite);
        }
    }
}
