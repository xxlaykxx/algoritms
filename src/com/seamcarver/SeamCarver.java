package com.seamcarver;

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int width, height;
    // we need this to get RED, GREEN, BLUE VALUES FOR calculating energy
    private Color[][] pixelColor;
    // energy holder
    private PixelDiff[][] pixelDiffs;

    public SeamCarver(Picture picture) {
        this.width = picture.width();
        this.height = picture.height();
        pixelColor = new Color[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                //pre-setup color for pixel
                pixelColor[row][column] = picture.get(column, row);
            }
        }
    }
    // current picture
    public Picture picture() {
        Picture picToReturn = new Picture(width, height);
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                picToReturn.set(col, row, pixelColor[row][col]);
        return picToReturn;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at row x and column y
    private double calculatePixelDifEnergy(int x1, int y1, int x2, int y2) {
        Color pixelColor1 = pixelColor[x1][y1];
        Color pixelColor2 = pixelColor[x2][y2];
        double R = pixelColor1.getRed() - pixelColor2.getRed();
        double G = pixelColor1.getGreen() - pixelColor2.getGreen();
        double B = pixelColor1.getBlue() - pixelColor2.getBlue();
        return R * R + G * G + B * B;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        SeamEnergyWithBackPointer[][] seamEnergies = new SeamEnergyWithBackPointer[width][height];
        calculatePixelDiffsHorizontal();

        for (int row = 0; row < height; row++) {
            seamEnergies[0][row] = new SeamEnergyWithBackPointer();
            seamEnergies[0][row].setEnergy(pixelDiffs[0][row].getMid());
            seamEnergies[0][row].setPrevColumn(-1);
            seamEnergies[0][row].setPrevRow(-1);
        }
        for (int column = 1; column < width; column++) {
            for (int row = 0; row < height; row++) {
                double currentMin = Double.POSITIVE_INFINITY;
                seamEnergies[column][row] = new SeamEnergyWithBackPointer(Double.POSITIVE_INFINITY, -1, -1);
                if (row > 0) {
                    double energyLeft = seamEnergies[column - 1][row - 1].energy + pixelDiffs[column][row].getLeft();
                    if (currentMin > energyLeft) {
                        currentMin = energyLeft;
                        seamEnergies[column][row].setEnergy(energyLeft);
                        seamEnergies[column][row].setPrevColumn(column - 1);
                        seamEnergies[column][row].setPrevRow(row - 1);
                    }
                }
                double energyMid = seamEnergies[column - 1][row].energy + pixelDiffs[column][row].getMid();
                if (currentMin > energyMid) {
                    currentMin = energyMid;
                    seamEnergies[column][row].setEnergy(energyMid);
                    seamEnergies[column][row].setPrevColumn(column - 1);
                    seamEnergies[column][row].setPrevRow(row);
                }
                if (row < height - 1) {
                    double energyRght = seamEnergies[column - 1][row + 1].energy + pixelDiffs[column][row].getRight();
                    if (currentMin > energyRght) {
                        seamEnergies[column][row].setEnergy(energyRght);
                        seamEnergies[column][row].setPrevColumn(column - 1);
                        seamEnergies[column][row].setPrevRow(row + 1);
                    }
                }
            }
        }

        int[] seam = new int[width];
        double lowestEnergy = Double.POSITIVE_INFINITY;
        for (int row = 0; row < height; row++) {
            if (seamEnergies[width - 1][row].getEnergy() < lowestEnergy) {
                lowestEnergy = seamEnergies[width - 1][row].getEnergy();
//                seam[width - 2] = row;
                seam[width - 1] = row;
            }
        }
        for (int column = width - 2; column >= 0; column--) {
            if (column == 0) {
                seam[column] = seam[column + 1];
            } else {
                seam[column] = seamEnergies[column][seam[column + 1]].getPrevRow();
            }
        }
        return seam;
    }

    private PixelDiff calculatePixelDifLeftRightMid(int row, int column) {
        int columnL = (column > 0) ? (column - 1) : column;
        int columnR = (column < width - 1) ? (column + 1) : column;
        double currentDiff = calculatePixelDifEnergy(row, columnL, row, columnR);
        double r = row > 0 ? currentDiff + calculatePixelDifEnergy(row - 1, column, row, columnR) : 0;
        double l = row > 0 ? currentDiff + calculatePixelDifEnergy(row - 1, column, row, columnL) : 0;
        double m = currentDiff;
        return new PixelDiff(l, r, m);
    }

    private PixelDiff calculatePixelDifTopBottomMid(int row, int column) {
        int rowT = row > 0 ? row - 1 : row;
        int rowB = row < height - 1 ? row + 1 : row;
        double currentDiff = calculatePixelDifEnergy(rowT, column, rowB, column);
        double r = column > 0 ? currentDiff + calculatePixelDifEnergy(row, column - 1, rowB, column) : 0;
        double l = column > 0 ? currentDiff + calculatePixelDifEnergy(row, column - 1, rowT, column) : 0;
        double m = currentDiff;
        return new PixelDiff(l, r, m);
    }

    private void calculatePixelDiffsVertical() {
        pixelDiffs = new PixelDiff[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                pixelDiffs[row][column] = calculatePixelDifLeftRightMid(row, column);
            }
        }
    }

    private void calculatePixelDiffsHorizontal() {
        pixelDiffs = new PixelDiff[width][height];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                pixelDiffs[column][row] = calculatePixelDifTopBottomMid(row, column);
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        SeamEnergyWithBackPointer[][] seamEnergies = new SeamEnergyWithBackPointer[height][width];
        calculatePixelDiffsVertical();
        //calculate energy for top
        for (int column = 0; column < width; column++) {
            seamEnergies[0][column] = new SeamEnergyWithBackPointer();
            seamEnergies[0][column].setEnergy(pixelDiffs[0][column].getMid());
            seamEnergies[0][column].setPrevColumn(-1);
            seamEnergies[0][column].setPrevRow(-1);
        }
        for (int row = 1; row < height; row++) {
            for (int column = 0; column < width; column++) {
                double currentMin = Double.POSITIVE_INFINITY;
                seamEnergies[row][column] = new SeamEnergyWithBackPointer(Double.POSITIVE_INFINITY, -1, -1);
                if (column > 0) {
                    double energyLeft = seamEnergies[row - 1][column - 1].energy + pixelDiffs[row][column].getLeft();
                    if (currentMin > energyLeft) {
                        currentMin = energyLeft;
                        seamEnergies[row][column].setEnergy(energyLeft);
                        seamEnergies[row][column].setPrevColumn(column - 1);
                        seamEnergies[row][column].setPrevRow(row - 1);
                    }
                }
                double energyMid = seamEnergies[row - 1][column].energy + pixelDiffs[row][column].getMid();
                if (currentMin > energyMid) {
                    currentMin = energyMid;
                    seamEnergies[row][column].setEnergy(energyMid);
                    seamEnergies[row][column].setPrevColumn(column);
                    seamEnergies[row][column].setPrevRow(row - 1);
                }
                if (column < width - 1) {
                    double energyRght = seamEnergies[row - 1][column + 1].energy + pixelDiffs[row][column].getRight();
                    if (currentMin > energyRght) {
                        seamEnergies[row][column].setEnergy(energyRght);
                        seamEnergies[row][column].setPrevColumn(column + 1);
                        seamEnergies[row][column].setPrevRow(row - 1);
                    }
                }
            }
        }
        int[] seam = new int[height];
        double lowestEnergy = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width; col++) {
            if (seamEnergies[height - 1][col].getEnergy() < lowestEnergy) {
                lowestEnergy = seamEnergies[height - 1][col].getEnergy();
//                seam[height - 2] = col;
                seam[height - 1] = col;
            }
        }
        for (int row = height - 2; row >= 0; row--) {
            if (row == 0) {
                seam[row] = seam[row + 1];
            } else {
                seam[row] = seamEnergies[row][seam[row + 1]].getPrevColumn();
            }
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        for (int col = 0; col < width - 1; col++)
            for (int row = seam[col]; row < height - 1; row++) {
                pixelColor[row][col] = pixelColor[row + 1][col];
            }
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        for (int row = 0; row < height; row++) {
            int cutLoc = seam[row];
            System.arraycopy(pixelColor[row], cutLoc + 1, pixelColor[row], cutLoc, width - 1 - cutLoc);
        }
        width--;
    }
}
