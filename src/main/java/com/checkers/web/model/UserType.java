package com.checkers.web.model;

import com.checkers.web.utils.PawnType;

public class UserType {

    private final String name;
    private boolean isBeating;
    private int numbOfPawns = 12;

    private final PawnType pawnType;

    public UserType(String name, PawnType pawnType, boolean isBeating) {
        this.name = name;
        this.isBeating = isBeating;
        this.pawnType = pawnType;
    }

    public void subtractOnePawn() {
        numbOfPawns--;
    }

    public void cleanUp() {
        isBeating = false;
        numbOfPawns = 12;
    }

    public String getName() {
        return name;
    }

    public boolean isBeating() {
        return isBeating;
    }

    public void setBeating(boolean beating) {
        isBeating = beating;
    }

    public int getNumbOfPawns() {
        return numbOfPawns;
    }

    public void setNumbOfPawns(int numbOfPawns) {
        this.numbOfPawns = numbOfPawns;
    }

    public PawnType getPawnType() {
        return pawnType;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "name='" + name + '\'' +
                ", isBeating=" + isBeating +
                ", numbOfPawns=" + numbOfPawns +
                ", pawnType=" + pawnType +
                '}';
    }
}
