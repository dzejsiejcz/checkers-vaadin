package com.checkers.web.logic;

import com.checkers.web.utils.PawnType;

import static com.checkers.web.utils.Constants.toMove;
import static com.checkers.web.utils.PawnType.RED;
import static com.checkers.web.utils.PawnType.WHITE;
import static com.checkers.web.views.GameBoardView.*;

public class Turn {

    private PawnType type = WHITE;

    public Turn() {
    }

    public PawnType getType() {
        return type;
    }

    public String getUserToMove() {
        if (type == WHITE) {
            return userTypeWhite.getName();
        }
        return userTypeRed.getName();
    }

    public String switchTurn() {
        if (type == WHITE) {
            type = RED;
            moveByComputer();
            return userTypeRed.getName() + toMove;
        }
        type = WHITE;
        return userTypeWhite.getName() + toMove;
    }

    public void cleanRightToMove() {
        this.type = WHITE;
    }

    public boolean isComputerMove() {
        return !type.equals(WHITE);
    }

    @Override
    public String toString() {
        return "RightToMove{" +
                "type=" + type +
                '}';
    }


}
