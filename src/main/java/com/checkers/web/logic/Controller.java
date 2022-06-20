package com.checkers.web.logic;

import com.checkers.web.model.Coordinates;
import com.checkers.web.model.Field;
import com.checkers.web.model.Pawn;
import com.checkers.web.utils.MoveType;
import com.checkers.web.utils.PawnType;

import java.util.ArrayList;
import java.util.List;

import static com.checkers.web.utils.Constants.*;
import static com.checkers.web.utils.MoveType.*;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;
import static com.checkers.web.views.GameBoardView.*;
import static java.lang.Math.abs;

public class Controller {

    public static Controller INSTANCE = new Controller();

    private Controller() {
    }

    public MoveType checkMove(Pawn pawn, int newRow, int newCol) {
        int oldCol = pawn.getCol();
        int oldRow = pawn.getRow();

        /*
         * whose turn?
         */
        if (turn.getType() != pawn.getType()) {
            return FORBIDDEN;
        }

        /*
         *  check possibility of beating, if possible, user must beat
         */
        Coordinates checkingFieldAfterBeat = new Coordinates(newRow, newCol);
        if (checkBeatingForGivenPawn(pawn)) {
            for (Coordinates currentField : pawn.getFieldsAfterBeats()) {
                if (currentField.getColNumber() == checkingFieldAfterBeat.getColNumber() && currentField.getRowNumber() == checkingFieldAfterBeat.getRowNumber()) {
                    return KILLING;
                }
            }
            return FORBIDDEN;
        }

        /*
         *  check right direction of move, except beating
         */
        int deltaY = newRow - oldRow;
        if (pawn.getType() == WHITE && deltaY > 0 && !checkBeatingForGivenPawn(pawn)) {
            return FORBIDDEN;
        }
        if (pawn.getType() == RED && deltaY < 0 && !checkBeatingForGivenPawn(pawn)) {
            return FORBIDDEN;
        }

        int deltaX = newCol - oldCol;

        /*
         *   normal movement if no beating, beating is obligatory
         */
        if (abs(deltaX) == 1 && abs(deltaY) == 1 && !checkBeatingOnEntireBoard(pawn.getType()) && game.isGame()) {
            System.out.println("first checking");
            return NORMAL;
        }

        return FORBIDDEN;
    }

    public static boolean checkBeatingForGivenPawn(Pawn checkingPawn) {
        boolean possible = false;
        Field neighbor;
        Field positionAfterBeating;
        checkingPawn.clearFieldsAfterBeats();
        PawnType pawnType = checkingPawn.getType();
        int x = checkingPawn.getCol();
        int y = checkingPawn.getRow();

        /*
         * checking if in neighborhood of given pawn is a possibility to hit
         * local array 5x5: (a -the field after beating, o-an opponent, p-the given pawn)
         * iteration of each of the arms of X shown below
         *
         *    -2 -1 0 +1 +2
         * +2  a         a
         * +1     o   o
         *  0       p
         * -1     o   o
         * -2  a         a
         */

        for (int i = -1; i < 2; i = i + 2) {
            for (int j = -1; j < 2; j = j + 2) {
                int adjacentOpponentCol = x + i;
                int adjacentOpponentRow = y + j;
                int locationAfterBeatCol = adjacentOpponentCol + i;
                int locationAfterBeatRow = adjacentOpponentRow + j;
                /*
                 * checking if position after beating is out of board?
                 */
                if (locationAfterBeatCol >= 0 && locationAfterBeatRow >= 0 &&
                        locationAfterBeatRow < HEIGHT && locationAfterBeatCol < WIDTH) {
                    neighbor = fields[adjacentOpponentCol][adjacentOpponentRow];
                    positionAfterBeating = fields[locationAfterBeatCol][locationAfterBeatRow];
                    /*
                     * is there a Pawn to hit? is there a free field to move in after hit? is there an opponent pawn?
                     */
                    if (neighbor.getComponentCount() > 0 && positionAfterBeating.getComponentCount() == 0) {
                        Pawn neighborPawn = (Pawn) neighbor.getChildren().findFirst().get();
                        PawnType neighborType = neighborPawn.getType();
                        if (neighborType != pawnType) {
                            checkingPawn.setPossiblePositionAfterBeating(locationAfterBeatCol, locationAfterBeatRow);
                            possible = true;
                        }
                    }
                }
            }
        }
        return possible;
    }

    public static List<Integer> getListOfRowsOfOneTypeOfPawns(PawnType color) {
        List<Integer> rowsOfPawns = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Field checkingField = fields[x][y];
                if (checkingField.getComponentCount()>0) {
                    Pawn checkingPawn = (Pawn) checkingField.getChildren().findFirst().get();
                    if (checkingPawn.getType() == color) {
                        rowsOfPawns.add(checkingPawn.getRow());
                    }
                }
            }
        }
        return rowsOfPawns;
    }

    /*
     * check if given Type of Pawns has the possibility of hitting and set this
     * info into each Pawn and return true if any Pawn can hit
     */
    public static boolean checkBeatingOnEntireBoard(PawnType color) {
        List<Pawn> pawns = getListOfOneTypeOfPawns(color);
        boolean response = false;
        for (Pawn checkingPawn : pawns) {
            if (checkBeatingForGivenPawn(checkingPawn)) {
                response = true;
            }
        }
        return response;
    }

    public static List<Pawn> getListOfOneTypeOfPawns(PawnType color) {
        List<Pawn> pawns = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Field checkingField = fields[x][y];
                if (checkingField.getComponentCount() > 0) {
                    Pawn checkingPawn = (Pawn) checkingField.getChildren().findFirst().get();
                    if (checkingPawn.getType() == color) {
                        pawns.add(checkingPawn);
                    }
                }
            }
        }
        return pawns;
    }

    public static String doesMovementSummary(Pawn pawn, boolean isKilled) {
        PawnType type = pawn.getType();

        if (isKilled) {
            if (type == WHITE) {
                userRed.subtractOnePawn();
            } else {
                userWhite.subtractOnePawn();
            }
            if (checkBeatingForGivenPawn(pawn)) {
                return additional;
            }
        }

        String endResponse = game.whoWon();

        if (endResponse != null) {
            return endResponse;
        }

        return turn.switchTurn();
    }

}
