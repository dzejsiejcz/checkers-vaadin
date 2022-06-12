package com.checkers.web.model;

import com.checkers.web.utils.PawnType;
import com.vaadin.flow.component.html.Div;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.web.utils.Constants.COLOR_PAWN_RED;

public class Pawn extends Div {

    private int col;
    private int row;
    private PawnType type;
    private int number;

    private List<Coordinates> fieldsAfterBeats = new ArrayList<>();

    public Pawn(int col, int row, PawnType type, int number) {
        this.col = col;
        this.row = row;
        this.type = type;
        this.number = number;
        setClassName("color");

        if (type.equals(PawnType.RED)) {
            getStyle().set("background-color", "red");
        } else {
            getStyle().set("background-color", "white");
        }
        getStyle().set("border-radius","50%");
        getStyle().set("width","60px");
        getStyle().set("height","60px");
        getStyle().set("font-size","x-small");
        getStyle().set("text-align", "center");
        getStyle().set("stroke-width", "3px");
        getStyle().set("stroke", "black");

        setText(String.valueOf(number));

        setId(String.valueOf(number));
        this.addClickListener(event -> {
            System.out.println("COL: "+ col + " ROW: " + row);
        });
    }

    public void setPossiblePositionAfterBeating(int col, int row) {
        fieldsAfterBeats.add(
                Coordinates.Builder.aCoordinates()
                        .colNumber(col)
                        .rowNumber(row)
                        .build());
    }

    public void clearFieldsAfterBeats() {
        fieldsAfterBeats = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Pawn{" +
                ", col=" + col +
                "row=" + row +
                ", type=" + type +
                ", numb=" + number +
                ", coordinates=" + fieldsAfterBeats.toString() +
                "}";
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

    public PawnType getType() {
        return type;
    }

    public void setType(PawnType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Coordinates> getFieldsAfterBeats() {
        return fieldsAfterBeats;
    }

    public void setFieldsAfterBeats(List<Coordinates> fieldsAfterBeats) {
        this.fieldsAfterBeats = fieldsAfterBeats;
    }
}
