package com.seamcarver;

public class SeamEnergyWithBackPointer {
    double energy;
    int prevRow;
    int prevColumn;

    public SeamEnergyWithBackPointer() {
    }

    public SeamEnergyWithBackPointer(double energy, int prevRow, int prevColumn) {
        this.energy = energy;
        this.prevRow = prevRow;
        this.prevColumn = prevColumn;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public int getPrevRow() {
        return prevRow;
    }

    public void setPrevRow(int prevRow) {
        this.prevRow = prevRow;
    }

    public int getPrevColumn() {
        return prevColumn;
    }

    public void setPrevColumn(int prevColumn) {
        this.prevColumn = prevColumn;
    }
}
