package com.checkers.web.logic;

import java.util.Collections;
import java.util.List;

import static com.checkers.web.logic.Controller.getListOfRowsOfOneTypeOfPawns;
import static com.checkers.web.utils.Constants.draw;
import static com.checkers.web.utils.Constants.won;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;
import static com.checkers.web.views.GameBoardView.userTypeRed;
import static com.checkers.web.views.GameBoardView.userTypeWhite;

public class StateOfGame {

    private boolean isGame = true;
    int minRowOfReds = 7;
    int maxRowOfWhites = 0;
    String winner = null;

    public StateOfGame() {
    }

    public String whoWon() {


        int whitePawns = userTypeWhite.getNumbOfPawns();
        int redPawns = userTypeRed.getNumbOfPawns();

        if (whitePawns == 0) {
            isGame = false;
            winner = userTypeRed.getName() + won;
            return winner;
        }
        if (redPawns == 0) {
            isGame = false;
            winner = userTypeWhite.getName() + won;
            return winner;
        }

        List<Integer> reds = getListOfRowsOfOneTypeOfPawns(RED);
        List<Integer> whites = getListOfRowsOfOneTypeOfPawns(WHITE);
        Collections.sort(reds);
        Collections.sort(whites);
        minRowOfReds = reds.get(0);
        maxRowOfWhites = whites.get(whites.size() - 1);

        if (maxRowOfWhites < minRowOfReds - 1) {
            isGame = false;
            if (whitePawns > redPawns) {
                winner = userTypeWhite.getName() + won;
            } else if (whitePawns < redPawns) {
                winner = userTypeRed.getName() + won;
            } else {
                winner = draw;
            }
        }

        return winner;
    }

    public boolean isGame() {
        return isGame;
    }

    public void cleanStateOfGame() {
        isGame = true;
        winner = null;
    }

    @Override
    public String toString() {
        return "StateOfGame{" +
                "isGame=" + isGame +
                ", minRowOfReds=" + minRowOfReds +
                ", maxRowOfWhites=" + maxRowOfWhites +
                '}';
    }
}
