package com.checkers.web.views;

import com.checkers.web.logic.Controller;
import com.checkers.web.logic.QuizResult;
import com.checkers.web.logic.StateOfGame;
import com.checkers.web.logic.Turn;
import com.checkers.web.model.Field;
import com.checkers.web.model.Pawn;
import com.checkers.web.model.UserType;
import com.checkers.web.quiz.QuizClient;
import com.checkers.web.utils.Constants;
import com.checkers.web.utils.MoveType;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.layout.FluentGridLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;

import javax.annotation.security.PermitAll;

import java.util.Random;

import static com.checkers.web.logic.Controller.movementSummary;
import static com.checkers.web.utils.Constants.*;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;


@Route(value = "game", layout = MainLayout.class)
@CssImport("./styles/styles.css")
@PermitAll
public class GameBoardView extends VerticalLayout {

    public static UserType userTypeRed = new UserType(Constants.reds, RED, false);
    public static UserType userTypeWhite = new UserType(Constants.whites, WHITE, false);
    public static Field[][] fields = new Field[WIDTH][HEIGHT];
    public static Pawn[][] pawns = new Pawn[WIDTH][HEIGHT];
    public static Turn turn = new Turn();
    public static StateOfGame game = new StateOfGame();
    public static QuizResult quizResult = new QuizResult();
    private final QuizClient quizClient;

    public GameBoardView(QuizClient quizClient) {
        this.quizClient = quizClient;

        FluentGridLayout layout = new FluentGridLayout()
                .withPadding(true)
                .withSpacing(true)
                .withOverflow(GridLayoutComponent.Overflow.VISIBLE);
        layout.setClassName("board");
        layout.setBoxSizing(BoxSizing.CONTENT_BOX);
        layout.withSpacing(false);
        add(layout);

        QuizComponent quizComponent = new QuizComponent(quizClient);
        buildBoardWithPawns(layout, quizComponent);
        Button restartButton = new Button("Restart game");
        restartButton.addClickListener(event -> {
            buildBoardWithPawns(layout, quizComponent);
            game = new StateOfGame();
            turn = new Turn();
            quizResult = new QuizResult();
            Notification restart = Notification.show("New Game");
        });
        add(restartButton, quizComponent);

    }

    private void buildBoardWithPawns(FluentGridLayout layout, QuizComponent quizComponent) {
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
                    pawns[col][row] = pawn;
                    field.add(pawn);
                }
                layout.withRowAndColumn(field, row+1, col+1);
            }
        }

        //creating moving of pawns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Field fieldNewParent = fields[col][row];
                if (fieldNewParent.getColor().equals(COLOR_FIELD_BLUE)){

                    DropTarget.create(fieldNewParent).addDropListener((DropEvent<Field> event) -> {
                        if (event.getDragSourceComponent().isPresent() && fieldNewParent.getComponentCount()==0) {
                            Pawn pawnDragged = (Pawn) event.getDragSourceComponent().get();

                            MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, fieldNewParent.getRow(),
                                    fieldNewParent.getCol());
                            if (moveType == MoveType.NORMAL) {

                                move(pawnDragged, fieldNewParent);
                                quizComponent.refreshQuestion(quizResult);

                                String resultOfMove = movementSummary(pawnDragged, false, true);
                                System.out.println("destination field: "+ fieldNewParent.getRow() +
                                        fieldNewParent.getCol());
                                System.out.println(resultOfMove);


                            } else if (moveType == MoveType.KILLING) {

                                int neighborCol = (fieldNewParent.getCol() + pawnDragged.getCol()) / 2;
                                int neighborRow = (fieldNewParent.getRow() + pawnDragged.getRow()) / 2;

                                Field beatingField = fields[neighborCol][neighborRow];
                                beatingField.removeAll();
                                move(pawnDragged, fieldNewParent);
                                //quizComponent.refreshQuestion(quizResult);

                                String resultOfMove = movementSummary(pawnDragged, true, true);
                                System.out.println(resultOfMove);
                            }
                        }
                    });
                }
            }
        }

    }

    public static void move(Pawn pawnDragged, Field fieldNewParent) {
        Field fieldOldParent = (Field) pawnDragged.getParent().get();
        fieldOldParent.remove(pawnDragged);
        pawns[fieldOldParent.getCol()][fieldOldParent.getRow()] = null;
        fieldNewParent.add(pawnDragged);
        pawnDragged.setCol(fieldNewParent.getCol());
        pawnDragged.setRow(fieldNewParent.getRow());
        pawns[fieldNewParent.getCol()][fieldNewParent.getRow()] = pawnDragged;
    }

    public static String moveByComputer() {
        Random random = new Random();
        int trials = 0;

        while (true) {
            int randomCol = random.nextInt(8);
            int randomRow = random.nextInt(8);

            Pawn pawnDragged = GameBoardView.pawns[randomCol][randomRow];

            if (pawnDragged != null && pawnDragged.getType().equals(RED)) {
                System.out.println("checking for: " + pawnDragged.getNumber());
                while (true) {
                    int randColAfterMove = random.nextInt(5) - 2 + randomCol;
                    int randRowAfterMove = random.nextInt(5) - 2 + randomRow;
                    if (randColAfterMove >= 0 && randColAfterMove < 8) {
                        if (randRowAfterMove >= 0 && randRowAfterMove < 8) {
                            MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, randRowAfterMove, randColAfterMove);
                            Field fieldNewParent = fields[randColAfterMove][randRowAfterMove];
                            if (fieldNewParent.getComponentCount() == 0 && moveType == MoveType.NORMAL) {

                                move(pawnDragged, fieldNewParent);

                                String resultOfMove = movementSummary(pawnDragged, false, false);

                                System.out.println("Computer moves to destination field: " + fieldNewParent.getRow() +
                                        fieldNewParent.getCol());
                                System.out.println(resultOfMove);
                                return "The computer moved normally";

                            } else if (fieldNewParent.getComponentCount() == 0 && moveType == MoveType.KILLING) {
                                int neighborCol = (fieldNewParent.getCol() + pawnDragged.getCol()) / 2;
                                int neighborRow = (fieldNewParent.getRow() + pawnDragged.getRow()) / 2;
                                Field beatingField = fields[neighborCol][neighborRow];
                                beatingField.removeAll();
                                move(pawnDragged, fieldNewParent);

                                String resultOfMove = movementSummary(pawnDragged, true, false);
                                System.out.println(resultOfMove);
                                System.out.println("The computer killed your pawn");
                                break;
                            } else {
                                trials++;
                                if (trials == 8) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}



