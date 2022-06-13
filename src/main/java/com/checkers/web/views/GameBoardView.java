package com.checkers.web.views;

import com.checkers.web.logic.Controller;
import com.checkers.web.logic.StateOfGame;
import com.checkers.web.logic.Turn;
import com.checkers.web.model.Field;
import com.checkers.web.model.Pawn;
import com.checkers.web.model.User;
import com.checkers.web.utils.Constants;
import com.checkers.web.utils.MoveType;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.layout.FluentGridLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import static com.checkers.web.logic.Controller.doesMovementSummary;
import static com.checkers.web.utils.Constants.*;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;


@Route("game")
@AnonymousAllowed
@CssImport("./styles/styles.css")
public class GameBoardView extends VerticalLayout {


    public static User userRed = new User(Constants.reds, RED, false);
    public static User userWhite = new User(Constants.whites, WHITE, false);
    public static Field fields[][] = new Field[WIDTH][HEIGHT];
    public static Turn turn = new Turn();
    public static StateOfGame game = new StateOfGame();

    public GameBoardView() {



        FluentGridLayout layout = new FluentGridLayout()
                .withPadding(true)
                .withSpacing(true)
                .withOverflow(GridLayoutComponent.Overflow.VISIBLE);
        layout.setClassName("board");
        layout.setBoxSizing(BoxSizing.CONTENT_BOX);
        layout.withSpacing(false);
        add(layout);

        int pawnNumber = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                String color;
                if ((col + row) % 2 != 0) {
                    color = COLOR_FIELD_BLUE;
                } else {
                    color = COLOR_FIELD_WHITE;
                }

                Field field = new Field(col, row, color);
                fields[col][row] = field;
                Pawn pawn = null;

                if (row < 3 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, RED, pawnNumber);
                }
                if (row > 4 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, WHITE, pawnNumber);
                }
                if (pawn != null) {
                    pawnNumber++;
                    final int colDragged = col;
                    final int rowDragged = row;
                    DragSource<Div> pawnDragSource = DragSource.create(pawn);
                    pawnDragSource.addDragStartListener((DragStartEvent<Div> event) -> {
                        event.setDragData(rowDragged+colDragged);
                        System.out.println("zrodlo field "+rowDragged+colDragged);
                    });

                    field.add(pawn);
                }
                layout.withRowAndColumn(field, row+1, col+1);
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int finalRow = row;
                int finalCol = col;
                Field fieldNewParent = fields[finalCol][finalRow];
                if (fieldNewParent.getColor().equals(COLOR_FIELD_BLUE)){
                    DropTarget.create(fieldNewParent).addDropListener((DropEvent<Field> event) -> {
                        if (event.getDragSourceComponent().isPresent() && fieldNewParent.getComponentCount()==0) {
                            Pawn pawnDragged = (Pawn) event.getDragSourceComponent().get();
                            Field fieldOldParent = (Field) pawnDragged.getParent().get();

                            MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, finalRow, finalCol);
                            if (moveType == MoveType.NORMAL) {
                                fieldOldParent.remove(pawnDragged);
                                fieldNewParent.add(pawnDragged);
                                pawnDragged.setCol(finalCol);
                                pawnDragged.setRow(finalRow);
                                String resultOfMove = doesMovementSummary(pawnDragged, false);
                                System.out.println("cel: "+ finalRow + finalCol);
                                System.out.println(resultOfMove);

                            } else if (moveType == MoveType.KILLING) {
                                fieldOldParent.remove(pawnDragged);
                                fieldNewParent.add(pawnDragged);
                                pawnDragged.setCol(finalCol);
                                pawnDragged.setRow(finalRow);
                                int neighborCol = (finalCol + pawnDragged.getCol()) / 2;
                                int neighborRow = (finalRow + pawnDragged.getCol()) / 2;
                                Field beatingField = fields[neighborCol][neighborRow];
                                beatingField.removeAll();
                                String resultOfMove = doesMovementSummary(pawnDragged, true);
                                System.out.println(resultOfMove);

                            }

                        }
                    });
                }
            }
        }
    }

}



