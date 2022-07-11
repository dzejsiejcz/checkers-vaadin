package com.checkers.web.model;

import com.checkers.web.utils.PawnType;

import java.util.ArrayList;
import java.util.List;

public class UserType {

    private final String name;
    private int numbOfPawns;
    private final PawnType pawnType;
    private final List<Pawn> pawnList = new ArrayList<>();

    public UserType(String name, PawnType pawnType, int rows) {
        this.name = name;
        this.pawnType = pawnType;
        this.numbOfPawns = rows * 4;
    }

    public void subtractOnePawn() {
        numbOfPawns--;
    }

    public String getName() {
        return name;
    }

    public int getNumbOfPawns() {
        return numbOfPawns;
    }

    public List<Pawn> getPawnList() {
        return pawnList;
    }

    public void addPawn(Pawn pawn) {
        pawnList.add(pawn);
    }

    public void deletePawn(Pawn pawn) {
        pawnList.remove(pawn);
    }

    @Override
    public String toString() {
        return "UserType{" +
                "name='" + name + '\'' +
                ", numbOfPawns=" + numbOfPawns +
                ", pawnType=" + pawnType +
                '}';
    }
}
