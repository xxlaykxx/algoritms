package com.seamcarver;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Client {
    private static int removeColumns;
    private static int removeRows;
    private static SeamCarver seamCarver;
    private static boolean isParallel;

    public static void main(String[] args) {
        if (args.length != 4) {
            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            return;
        }

        Picture inputImg = new Picture(args[0]);
        removeColumns = Integer.parseInt(args[1]);
        removeRows = Integer.parseInt(args[2]);
        isParallel = Boolean.parseBoolean(args[3]);
        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        seamCarver = new SeamCarver(inputImg);

        Stopwatch sw = new Stopwatch();

        Mode mode = getMode();
        if(mode == null)
        {
            StdOut.println("Nothing to resize.");
            return;
        }

        switch (mode)
        {
            case V:
                removeOnlyVertical();
                break;
            case H:
                removeOnlyHorizontal();
                break;
            case HV:
                if(isParallel) {
                    removeHorizontalAndVertical();
                }
                else
                {
                    removeOnlyHorizontal();
                    removeOnlyVertical();
                }
                break;
        }
        Picture outputImg = seamCarver.picture();

        StdOut.printf("new image size is %d columns by %d rows\n", seamCarver.width(), seamCarver.height());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        inputImg.show();
        outputImg.show();
    }


    private static Mode getMode() {
        if (removeColumns > 0 && removeRows > 0) {
            return Mode.HV;
        } else if (removeColumns > 0) {
            return Mode.V;
        } else if (removeRows > 0) {
            return Mode.H;
        }
        return null;
    }

    private static void removeOnlyVertical() {
        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = seamCarver.findVerticalSeam();
            seamCarver.removeVerticalSeam(verticalSeam);
        }
    }

    private static void removeOnlyHorizontal() {
        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = seamCarver.findHorizontalSeam();
            seamCarver.removeHorizontalSeam(horizontalSeam);
        }
    }

    private static void removeHorizontalAndVertical() {
        int i = 0;
        while (i < Math.max(removeColumns, removeRows)) {
            if (i < removeColumns) {
                int[] verticalSeam = seamCarver.findVerticalSeam();
                seamCarver.removeVerticalSeam(verticalSeam);
            }
            if (i < removeRows) {
                int[] horizontalSeam = seamCarver.findHorizontalSeam();
                seamCarver.removeHorizontalSeam(horizontalSeam);
            }
            i++;
        }
    }
}
