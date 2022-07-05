package com.checkers.web.model;

import com.checkers.web.utils.PawnType;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.web.utils.Constants.COLOR_PAWN_RED;
import static com.checkers.web.utils.Constants.COLOR_PAWN_WHITE;


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

        if (type.equals(PawnType.RED)) {
            setClassName(COLOR_PAWN_RED);
        } else {
            setClassName(COLOR_PAWN_WHITE);
        }

        setText(String.valueOf(number));
        this.addClickListener(event -> System.out.println("COL: "+ col + " ROW: " + row));
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
