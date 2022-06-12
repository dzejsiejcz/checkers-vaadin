package com.checkers.web.logic;

import com.checkers.web.utils.PawnType;

import static com.checkers.web.utils.Constants.toMove;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;
import static com.checkers.web.views.GameBoardView.userRed;
import static com.checkers.web.views.GameBoardView.userWhite;

public class Turn {

    private PawnType type = WHITE;

    public Turn() {
    }

    public PawnType getType() {
        return type;
    }

    public String getUserToMove() {
        if (type == WHITE) {
            return userWhite.getName();
        }
        return userRed.getName();
    }

    public String switchTurn() {
        if (type == WHITE) {
            type = RED;
            System.out.println("Red now");
            return userRed.getName() + toMove;
        }
        type = WHITE;
        System.out.println("White now");
        return userWhite.getName() + toMove;
    }

    public void cleanRightToMove() {
        this.type = WHITE;
    }

    @Override
    public String toString() {
        return "RightToMove{" +
                "type=" + type +
                '}';
    }


}
