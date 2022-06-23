package com.checkers.web.views;

import com.checkers.web.logic.Controller;
import com.checkers.web.logic.StateOfGame;
import com.checkers.web.logic.Turn;
import com.checkers.web.model.Field;
import com.checkers.web.model.Pawn;
import com.checkers.web.model.UserType;
import com.checkers.web.utils.Constants;
import com.checkers.web.utils.MoveType;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.layout.FluentGridLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import static com.checkers.web.logic.Controller.doesMovementSummary;
import static com.checkers.web.utils.Constants.*;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;


@Route(value = "game", layout = MainLayout.class)
//@AnonymousAllowed
@PermitAll
@CssImport("./styles/styles.css")
public class GameBoardView extends VerticalLayout {


    public static UserType userTypeRed = new UserType(Constants.reds, RED, false);
    public static UserType userTypeWhite = new UserType(Constants.whites, WHITE, false);
    public static Field[][] fields = new Field[WIDTH][HEIGHT];
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

        //building board
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

                if (row == 0 ) {
                    Label colDescription = new Label(""+col);
                    Label rowDescription = new Label(""+ col);
                    colDescription.getStyle().set("text-align", "center");
                    rowDescription.getStyle().set("margin", "auto");
                    rowDescription.getStyle().set("padding", "5px");
                    layout.withRowAndColumn(colDescription, 9, col+1);
                    layout.withRowAndColumn(rowDescription, col+1, 9);
                }

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
                        System.out.println("source field "+rowDragged+colDragged);
                    });

                    field.add(pawn);
                }
                layout.withRowAndColumn(field, row+1, col+1);
            }
        }

        //creating pawns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Field fieldNewParent = fields[col][row];
                if (fieldNewParent.getColor().equals(COLOR_FIELD_BLUE)){

                    DropTarget.create(fieldNewParent).addDropListener((DropEvent<Field> event) -> {
                        if (event.getDragSourceComponent().isPresent() && fieldNewParent.getComponentCount()==0) {
                            Pawn pawnDragged = (Pawn) event.getDragSourceComponent().get();

                            MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, fieldNewParent.getRow(), fieldNewParent.getCol());
                            if (moveType == MoveType.NORMAL) {

                                move(pawnDragged, fieldNewParent);
                                String resultOfMove = doesMovementSummary(pawnDragged, false);

                                System.out.println("destination field: "+ fieldNewParent.getRow() + fieldNewParent.getCol());
                                System.out.println(resultOfMove);

                            } else if (moveType == MoveType.KILLING) {

                                int neighborCol = (fieldNewParent.getCol() + pawnDragged.getCol()) / 2;
                                int neighborRow = (fieldNewParent.getRow() + pawnDragged.getRow()) / 2;

                                Field beatingField = fields[neighborCol][neighborRow];
                                beatingField.removeAll();
                                move(pawnDragged, fieldNewParent);

                                String resultOfMove = doesMovementSummary(pawnDragged, true);
                                System.out.println(resultOfMove);
                            }
                        }
                    });
                }
            }
        }
    }

    private void move(Pawn pawnDragged, Field fieldNewParent) {
        Field fieldOldParent = (Field) pawnDragged.getParent().get();
        fieldOldParent.remove(pawnDragged);
        fieldNewParent.add(pawnDragged);
        pawnDragged.setCol(fieldNewParent.getCol());
        pawnDragged.setRow(fieldNewParent.getRow());
    }
}



