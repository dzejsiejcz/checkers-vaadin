package com.checkers.web.model;

public class Coordinates {

    private final int colNumber;
    private final int rowNumber;

    public Coordinates(int rowNumber, int colNumber) {
        this.colNumber = colNumber;
        this.rowNumber = rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public static final class Builder {
        private int colNumber;
        private int rowNumber;

        private Builder() {
        }

        public static Builder aCoordinates() {
            return new Builder();
        }

        public Builder colNumber(int colNumber) {
            this.colNumber = colNumber;
            return this;
        }

        public Builder rowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public Coordinates build() {
            return new Coordinates(rowNumber, colNumber);
        }
    }
}
