package com.example.demo;

import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.layout.FluentGridLayout;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;


@Route("game")
@AnonymousAllowed
public class GameBoard extends VerticalLayout {

    public static final String COLOR_BLUE = "#0000FF";
    public static final String COLOR_RED = "#8B0000";
    public static final String COLOR_WHITE = "#F8F8FF";


    public GameBoard() {

        FluentGridLayout layout = new FluentGridLayout()
                .withPadding(true)
                .withSpacing(true)
                .withOverflow(GridLayoutComponent.Overflow.VISIBLE);


        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                String color;
                if ((col + row) % 2 != 0) {
                    color = COLOR_BLUE;
                } else {
                    color = COLOR_WHITE;
                }
                Field field = new Field(color);
                layout.withRowAndColumn(field, col, row);
            }
        }

        layout.setBoxSizing(BoxSizing.CONTENT_BOX);
        layout.withSpacing(false);
        add(layout);

        int pawnNumber = 0;
        for (int col = 1; col < 9; col++) {
            for (int row = 1; row < 9; row++) {
                Pawn pawn;
                if (row < 4 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, COLOR_RED, pawnNumber);
                    pawnNumber++;
                    layout.withRowAndColumn(pawn, row, col);
                    layout.setColumnAlign(pawn, GridLayoutComponent.ColumnAlign.CENTER);
                    layout.setRowAlign(pawn, GridLayoutComponent.RowAlign.CENTER);

                }

                if (row > 5 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, COLOR_WHITE, pawnNumber);
                    pawnNumber++;
                    layout.withRowAndColumn(pawn, row, col);
                    layout.setColumnAlign(pawn, GridLayoutComponent.ColumnAlign.CENTER);
                    layout.setRowAlign(pawn, GridLayoutComponent.RowAlign.CENTER);
                }
            }
        }
    }
}



