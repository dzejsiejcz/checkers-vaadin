package com.example.demo;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

public class Pawn extends Image {

    StreamResource resourceRed = new StreamResource("red-dot.png", () -> getClass().getResourceAsStream("/icons/red-dot.png"));
    StreamResource resourceWhite = new StreamResource("white-dot.png", () -> getClass().getResourceAsStream("/icons/white-dot.png"));

    private int col;
    private int row;
    private String color;
    private int number;


    public Pawn(int col, int row, String color, int number) {
        if (color.equals("#8B0000")) {
            setSrc(resourceRed);
        } else {
            setSrc(resourceWhite);
        }
        setText(String.valueOf(number));
        this.col = col;
        this.row = row;
        this.color = color;
        this.number = number;
    }
}
