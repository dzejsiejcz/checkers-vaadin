package com.checkers.web.model;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        setWidth(62, Unit.PIXELS);
        setHeight(62, Unit.PIXELS);
    }

    public String getColor() {
        return color;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
