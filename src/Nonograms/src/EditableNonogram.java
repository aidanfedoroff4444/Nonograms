package Nonograms.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class EditableNonogram extends Rectangle {
    private final int BOXSIZE = 40;
    private int maxRowClues, maxColClues;
    private int puzzleWidth = 10, puzzleHeight = 10;
    private Box[][] puzzle;
    private int[][] rowClues, columnClues;
    private boolean test;

    public EditableNonogram() {
        super(100, 100, 0, 0);
        maxRowClues = 1;
        maxColClues = 1;
        width = BOXSIZE * (maxRowClues + puzzleWidth);
        height = BOXSIZE * (maxColClues + puzzleHeight);

        puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];

        rowClues = new int[puzzleHeight][1];
        columnClues = new int[puzzleWidth][1];
        for(int i = 0; i < columnClues.length; i++) columnClues[i] = new int[] {0};
        for(int i = 0; i < rowClues.length; i++) rowClues[i] = new int[] {0};

        test = false;
    }

    public void mousePressed(MouseEvent e, JFrame frame) {
        int mX = e.getX();
        int mY = e.getY();
        if(!(mX >= x && mY >= y && mX <= x + width && mY <= y + height)) return; // Quits if the mouse click was outside the puzzle
        // System.out.println("Box is in (row,col): (" + (mY - y) / BOXSIZE + "," + (mX - x) / BOXSIZE + ")");
        int boxRow = (mY - y) / BOXSIZE;
        int boxCol = (mX - x) / BOXSIZE;
        if(boxRow < maxColClues && boxCol >= maxRowClues) {
            // Display dialog and receive the clues in a string of int values with a space as a delimiter.
            String clues = (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter the clues for this column, separate each clue with a space.",
                    "Column Clues", JOptionPane.PLAIN_MESSAGE,
                    null, null, "");
            // Assign the new clues to their respective place in columnClues
            if (clues == null) return;
            String[] cluesArr = clues.split(" ");
            int[] intArr = new int[cluesArr.length];
            for (int i = 0; i < cluesArr.length; i++) intArr[i] = Integer.parseInt(cluesArr[i]);
            // System.out.println(Arrays.toString(intArr));
            addColumnClueRow(boxCol - 1, intArr);
        }
    }

    public void addColumnClueRow(int colIndex, int[] clueArr) {
        if(clueArr.length > columnClues[0].length) { // Need to enlarge the other rows of the columnClues 2D array by putting a 0 as the new first element of each row.
            for(int i = 0; i < columnClues.length; i++) { // Runs for each row in columnClues
                int[] newRow = new int[clueArr.length];
                newRow[0] = 0;
                // Fill the rest of the newRow with the rest of the values already in the row of columnClues
                System.arraycopy(columnClues[i], 0, newRow, 1, columnClues[i].length);
                columnClues[i] = newRow;
            }
            maxColClues = columnClues[0].length;
            height = BOXSIZE * (maxColClues + puzzleHeight);

            puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];
        }
        // Assign the new clueArr row to the respective row in columnClues given by colIndex
        columnClues[colIndex] = clueArr;
        System.out.println(Arrays.deepToString(columnClues));
        test = true;
    }

    public void paint(Graphics2D g) {
        System.out.println("columnClues: " + Arrays.deepToString(columnClues));
        g.setColor(Color.black);

        System.out.println("1: " + test);
        if(test) {
            System.out.println("Test");
        }

        g.translate(x, y);

        for(int i = 0; i < height / BOXSIZE; i++) { // For each row
            for(int j = 0; j < width / BOXSIZE; j++) { // For each column in each row
                puzzle[i][j] = new Box(BOXSIZE * j, BOXSIZE * i, BOXSIZE, BOXSIZE, 0);

                // Set the border width of each box
                int[] squareBorderWidths = new int[] {1, 1, 1, 1};

                if(i == 0) squareBorderWidths[0]++; // For the top outline edge
                if(i == puzzle.length - 1) squareBorderWidths[2]++; // For the bottom outline edge
                if(j == 0) squareBorderWidths[3]++; // For the left outline edge
                if(j == puzzle[0].length - 1) squareBorderWidths[1]++; // For the right outline edge

                if(i == maxColClues - 1) squareBorderWidths[2]++; // For the bottom edges of the column clues
                if(j == maxRowClues - 1) squareBorderWidths[1]++; // For the right edges of the row clues

                if(i >= maxColClues && i % 5 == maxColClues) squareBorderWidths[0]++; // For each fifth row
                if(j >= maxRowClues && j % 5 == maxRowClues) squareBorderWidths[3]++; // For each fifth collumn

                puzzle[i][j].setBorders(squareBorderWidths);

                // Set the background of the clues to light gray
                if(i < maxColClues || j < maxRowClues) puzzle[i][j].setBackground(Color.lightGray);

                // Set the value of the boxes for the columnClues
                if(i < maxColClues && j >= maxRowClues) {
                    if(columnClues[j - maxRowClues][i] == 1) System.out.println("Found a 1");
                    puzzle[i][j].setValue(columnClues[j - maxRowClues][i]);
                }
                // Set the value of the boxes for the rowClues
                if(j < maxRowClues && i >= maxColClues) puzzle[i][j].setValue(rowClues[i - maxColClues][j]);
            }
        }

        for(Box[] boxRow : puzzle)
            for(Box box : boxRow)
                box.paint(g); // Draw each square
    }
}
