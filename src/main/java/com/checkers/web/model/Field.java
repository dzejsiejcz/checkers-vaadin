package com.checkers.web.model;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;


@Setter
public class Field extends VerticalLayout {

    private int col;
    private int row;
    private String color;

    public Field(int col, int row, String color) {
        this.col = col;
        this.row = row;
        this.color = color;
        setClassName(color);

        setWidth("100");
        setHeight("100");
        //add(new Label("col: " + col + " row: "+ row));
    }

    public String getColor() {
        return color;
    }
}
