package com.checkers.web.logic;

import com.checkers.web.domain.Winner;
import com.checkers.web.model.UserType;
import com.checkers.web.service.ScoreService;
import com.checkers.web.utils.Constants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

    private final ScoreService scoreService;

    public StateOfGame(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    public String whoWon() {


        int whitePawns = userTypeWhite.getNumbOfPawns();
        int redPawns = userTypeRed.getNumbOfPawns();

        if (whitePawns == 0) {
            isGame = false;
            winner = userTypeRed.getName() + won;
            saveToScore(userTypeRed, redPawns);

            return winner;
        }
        if (redPawns == 0) {
            isGame = false;
            winner = userTypeWhite.getName() + won;
            saveToScore(userTypeWhite, whitePawns);

            return winner;
        }

        List<Integer> reds = getListOfRowsOfOneTypeOfPawns(RED);
        List<Integer> whites = getListOfRowsOfOneTypeOfPawns(WHITE);
        Collections.sort(reds);
        Collections.sort(whites);
        minRowOfReds = (reds.isEmpty()) ? 7 : reds.get(0);
        maxRowOfWhites = (whites.isEmpty()) ? 0 : whites.get(whites.size() - 1);

        if (maxRowOfWhites < minRowOfReds - 1) {
            isGame = false;
            if (whitePawns > redPawns) {
                winner = userTypeWhite.getName() + won;
                saveToScore(userTypeWhite, whitePawns - redPawns);
            } else if (whitePawns < redPawns) {
                winner = userTypeRed.getName() + won;
                saveToScore(userTypeRed, redPawns - whitePawns);
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


    private void saveToScore(UserType userType, int advantage) {
        String username;
        Winner winner;
        if (userType.getName().equals(Constants.whites)) {
            winner = Winner.USER;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        } else {
            username = "Computer";
            winner = Winner.COMPUTER;
        }
        scoreService.addScore(username, advantage, winner);


    }
}
