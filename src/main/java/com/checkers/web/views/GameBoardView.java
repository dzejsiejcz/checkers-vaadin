package com.checkers.web.views;

import com.checkers.web.logic.Controller;
import com.checkers.web.logic.StateOfGame;
import com.checkers.web.model.Field;
import com.checkers.web.model.Pawn;
import com.checkers.web.model.UserType;
import com.checkers.web.quiz.QuizClient;
import com.checkers.web.utils.Constants;
import com.checkers.web.utils.MoveType;
import com.github.appreciated.css.grid.GridLayoutComponent;
import com.github.appreciated.layout.FluentGridLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.checkers.web.logic.Controller.movementSummary;
import static com.checkers.web.utils.Constants.*;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;


@Route(value = "game", layout = MainLayout.class)
@CssImport("./styles/styles.css")
@PermitAll
public class GameBoardView extends VerticalLayout {

    public static UserType userTypeRed = new UserType(Constants.reds, RED, false, 3);
    public static UserType userTypeWhite = new UserType(Constants.whites, WHITE, false, 3);
    public static Field[][] fields = new Field[WIDTH][HEIGHT];
    public static Pawn[][] pawns = new Pawn[WIDTH][HEIGHT];
    public static StateOfGame game = new StateOfGame();
    private final QuizClient quizClient;
    private int rowsOfPawns = 3;

    private static List<int[]> possiblePawnMoves = buildArrayOfPossibleMoves();

    public GameBoardView(QuizClient quizClient) {
        this.quizClient = quizClient;

        HorizontalLayout mainLayout = new HorizontalLayout();

        FluentGridLayout boardLayout = new FluentGridLayout()
                .withPadding(true)
                .withSpacing(true)
                .withOverflow(GridLayoutComponent.Overflow.VISIBLE);
        boardLayout.setClassName("board");
        boardLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
        boardLayout.withSpacing(false);

        mainLayout.add(boardLayout, buildRightPanel());
        add(mainLayout);

        QuizComponent quizComponent = new QuizComponent(quizClient);
        buildBoardWithPawns(boardLayout, quizComponent, 3);
        Button makeNewTwelvePawnsBoard = new Button("Restart game");
        makeNewTwelvePawnsBoard.addClickListener(event -> {
            buildBoardWithPawns(boardLayout, quizComponent, 3);
            Notification restart = Notification.show("New Game with 12 pawns");
        });

        Button makeNewFourPawnsBoard = new Button("New Game with 4 pawns");
        makeNewFourPawnsBoard.addClickListener(event -> {
            buildBoardWithPawns(boardLayout, quizComponent, 1);
            userTypeRed = new UserType(Constants.reds, RED, false, 1);
            userTypeWhite = new UserType(Constants.whites, WHITE, false, 1);
            Notification restart = Notification.show("New Game with 4 pawns");

        });


        add(makeNewTwelvePawnsBoard, makeNewFourPawnsBoard, quizComponent);
    }

    private VerticalLayout buildRightPanel() {
        VerticalLayout infos = new VerticalLayout();





        return infos;
    }

    private void buildBoardWithPawns(FluentGridLayout layout, QuizComponent quizComponent, int rows) {
        game = new StateOfGame();

        int pawnNumber = 0;
        rowsOfPawns = rows;
        int startLineForReds = 3 - rows;
        int endLineForWhites = 4 + rows;

        //building board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                fields[col][row] = null;
                pawns[col][row] = null;

                String color;
                if ((col + row) % 2 != 0) {
                    color = COLOR_FIELD_BLUE;
                } else {
                    color = COLOR_FIELD_WHITE;
                }

                Field field = new Field(col, row, color);
                fields[col][row] = field;
                Pawn pawn = null;

                if (row == 0) {
                    Label colDescription = new Label("" + col);
                    Label rowDescription = new Label("" + col);
                    colDescription.getStyle().set("text-align", "center");
                    rowDescription.getStyle().set("margin", "auto");
                    rowDescription.getStyle().set("padding", "5px");
                    layout.withRowAndColumn(colDescription, 9, col + 1);
                    layout.withRowAndColumn(rowDescription, col + 1, 9);
                }

                if (row >= startLineForReds && row < 3 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, RED, pawnNumber);
                }
                if (row <= endLineForWhites && row > 4 && ((col + row) % 2) != 0) {
                    pawn = new Pawn(col, row, WHITE, pawnNumber);
                    final int colDragged = col;
                    final int rowDragged = row;
                    DragSource<Div> pawnDragSource = DragSource.create(pawn);
                    pawnDragSource.addDragStartListener((DragStartEvent<Div> event) -> {
                        event.setDragData(rowDragged + colDragged);
                        System.out.println("source field " + rowDragged + colDragged);
                    });
                }
                if (pawn != null) {
                    pawnNumber++;
                    pawns[col][row] = pawn;
                    field.add(pawn);
                }
                layout.withRowAndColumn(field, row + 1, col + 1);
            }
        }

        //creating moving of pawns
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Field fieldNewParent = fields[col][row];
                if (fieldNewParent.getColor().equals(COLOR_FIELD_BLUE)) {

                    DropTarget.create(fieldNewParent).addDropListener((DropEvent<Field> event) -> {
                        if (event.getDragSourceComponent().isPresent() && fieldNewParent.getComponentCount() == 0) {
                            Pawn pawnDragged = (Pawn) event.getDragSourceComponent().get();

                            MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, fieldNewParent.getRow(),
                                    fieldNewParent.getCol());
                            if (moveType == MoveType.NORMAL) {

                                move(pawnDragged, fieldNewParent);
                                quizComponent.refreshQuestion();

                                String resultOfMove = movementSummary(pawnDragged, false, true);
                                Notification.show(resultOfMove);
                                System.out.println("destination field: row: " + fieldNewParent.getRow() + " col: " +
                                        fieldNewParent.getCol());
                                System.out.println(resultOfMove);

                            } else if (moveType == MoveType.KILLING) {

                                int neighborCol = (fieldNewParent.getCol() + pawnDragged.getCol()) / 2;
                                int neighborRow = (fieldNewParent.getRow() + pawnDragged.getRow()) / 2;

                                Field beatingField = fields[neighborCol][neighborRow];
                                pawns[neighborCol][neighborRow] = null;
                                beatingField.removeAll();
                                move(pawnDragged, fieldNewParent);

                                String resultOfMove = movementSummary(pawnDragged, true, true);
                                Notification.show(resultOfMove);
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

    public static String moveByComputer() throws InterruptedException {
        Random random = new Random();
        int trials = 0;

        while (true) {
            int randomCol = random.nextInt(8);
            int randomRow = random.nextInt(8);

                Pawn pawnDragged = GameBoardView.pawns[randomCol][randomRow];

                if (pawnDragged != null && pawnDragged.getType().equals(RED)) {
                    System.out.println("checking for: " + pawnDragged.getNumber());
                    for (int[] move : possiblePawnMoves) {
                        trials++;
                        int randomDeltaCol = move[0];
                        int randomDeltaRow = move[1];
                        int randColAfterMove = randomDeltaCol + randomCol;
                        int randRowAfterMove = randomDeltaRow + randomRow;
                        if (randColAfterMove >= 0 && randColAfterMove < 8) {
                            if (randRowAfterMove >= 0 && randRowAfterMove < 8) {
                                MoveType moveType = Controller.INSTANCE.checkMove(pawnDragged, randRowAfterMove, randColAfterMove);
                                Field fieldNewParent = fields[randColAfterMove][randRowAfterMove];
                                if (fieldNewParent.getComponentCount() == 0 && moveType == MoveType.NORMAL) {
                                    Thread.sleep(1000);
                                    move(pawnDragged, fieldNewParent);

                                    String resultOfMove = movementSummary(pawnDragged, false, false);
                                    Notification.show(resultOfMove);
                                    System.out.println("Computer moves from source field: row: " + randomRow + " col: " + randomCol +
                                            " to destination field: row: " + fieldNewParent.getRow() + " col: " + fieldNewParent.getCol());
                                    System.out.println(resultOfMove);
                                    return "The computer moved normally";

                                } else if (fieldNewParent.getComponentCount() == 0 && moveType == MoveType.KILLING) {
                                    int neighborCol = (fieldNewParent.getCol() + pawnDragged.getCol()) / 2;
                                    int neighborRow = (fieldNewParent.getRow() + pawnDragged.getRow()) / 2;
                                    Field beatingField = fields[neighborCol][neighborRow];
                                    beatingField.removeAll();

                                    Thread.sleep(1000);
                                    move(pawnDragged, fieldNewParent);
                                    Thread.sleep(1000);

                                    String resultOfMove = movementSummary(pawnDragged, true, false);
                                    System.out.println(resultOfMove);
                                    Notification.show(resultOfMove);
                                    System.out.println("The computer killed your pawn");
                                    break;
                                }
                            }
                        }
                        if (trials == 8) {
                            break;
                        }
                    }
                }
            }
    }

    private static List<int[]> buildArrayOfPossibleMoves() {
        List<int[]>moves=new ArrayList<>();
        for (int i=-2; i<3; i++) {
            for (int j=-2; j< 3; j++) {
                if (i==j || i==(-j) ) {
                    int [] array = {i, j};
                    moves.add(array);
                }
            }
        }
        return moves;
    }

}



