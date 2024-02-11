package Nonograms.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class EditableNonogram extends Rectangle {
    public final int BOXSIZE = 40;
    public int maxRowClues, maxColClues;
    private final int puzzleWidth = 10, puzzleHeight = 10;
    private Box[][] puzzle;
    private int[][] rowClues, columnClues;

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
    }

    public void mousePressed(MouseEvent e, JFrame frame) {
        int mX = e.getX();
        int mY = e.getY();
        if(!(mX >= x && mY >= y && mX <= x + width && mY <= y + height)) return; // Quits if the mouse click was outside the puzzle

        int boxRow = (mY - y) / BOXSIZE;
        int boxCol = (mX - x) / BOXSIZE;
        if((boxRow < maxColClues && boxCol >= maxRowClues) || (boxRow >= maxColClues && boxCol < maxRowClues)) { // if a column clue was clicked
            // Display dialog and receive the clues in a string of int values with a space as a delimiter.
            String clues = (String) JOptionPane.showInputDialog(
                    frame,
                    "Enter the clues for this column, separate each clue with a space.",
                    "Column Clues", JOptionPane.PLAIN_MESSAGE,
                    null, null, "");
            if (clues == null) return;

            // Assign the new clues to their respective place in columnClues
            String[] cluesArr = clues.split(" ");
            int[] intArr = new int[cluesArr.length];
            for (int i = 0; i < cluesArr.length; i++) intArr[i] = Integer.parseInt(cluesArr[i]);

            if(boxRow < maxColClues && boxCol >= maxRowClues) addColumnClueRow(boxCol - maxRowClues, intArr);
            else if(boxRow >= maxColClues && boxCol < maxRowClues) addRowClueRow(boxRow - maxColClues, intArr); // if a row clue was clicked
        }
    }

    /**
     * @param colIndex int, The index in columnClues that the clueArr @param belongs
     * @param clueArr int[], The array of int values that make up the new clues to be inserted into columnClues
     *                2 cases:
     *                 - clueArr.length <= maxColClues:
     *                    - add in x 0's to the beginning of the columnClues row, where x is maxColClues - clueArr.length
     *                    - Complete the columnClues row with the rest of the values copied over from clueArr. columnClues[colIndex].length should equal maxColClues
     *                 - clueArr.length >  maxColClues:
     *                    - copy over everything in puzzle into a new Box[][] array with x additional rows, where x is clueArr.length - maxColClues
     *                       - all the rows should be the same except for the additional x 0's placed before all the values in the row
     *                    - copy the clueArr into the columnClues[colIndex] array from index 0.
     */
    public void addColumnClueRow(int colIndex, int[] clueArr) {
        if(clueArr.length <= maxColClues) {
            // add in x 0's to the beginning of the columnClues row, where x is maxColClues - clueArr.length
            for(int i = 0; i < maxColClues - clueArr.length; i++) columnClues[colIndex][i] = 0;
            // Complete the columnClues row with the rest of the values copied over from clueArr. columnClues[colIndex].length should equal maxColClues
            System.arraycopy(clueArr, 0, columnClues[colIndex], maxColClues - clueArr.length, clueArr.length);
        } else {
            for(int i = 0; i < clueArr.length - maxColClues; i++) { // Runs for each 0 to be added to each row in columnClues
                for(int j = 0; j < columnClues.length; j++) {
                    int[] newRow = new int[columnClues[j].length + 1];
                    newRow[0] = 0;
                    System.arraycopy(columnClues[j], 0, newRow, 1, columnClues[j].length);
                    columnClues[j] = newRow;
                }
            }
            maxColClues += clueArr.length - maxColClues;
            height = BOXSIZE * (maxColClues + puzzleHeight);
            puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];

            System.arraycopy(clueArr, 0, columnClues[colIndex], clueArr.length - maxColClues, clueArr.length);
        }
    }
    public void addRowClueRow(int rowIndex, int[] clueArr) {
        if(clueArr.length <= maxRowClues) {
            // add in x 0's to the beginning of the columnClues row, where x is maxColClues - clueArr.length
            for(int i = 0; i < maxRowClues - clueArr.length; i++) rowClues[rowIndex][i] = 0;
            // Complete the columnClues row with the rest of the values copied over from clueArr. columnClues[colIndex].length should equal maxColClues
            System.arraycopy(clueArr, 0, rowClues[rowIndex], maxRowClues - clueArr.length, clueArr.length);
        } else {
            for(int i = 0; i < clueArr.length - maxRowClues; i++) { // Runs for each 0 to be added to each row in columnClues
                for(int j = 0; j < rowClues.length; j++) {
                    int[] newRow = new int[rowClues[j].length + 1];
                    newRow[0] = 0;
                    System.arraycopy(rowClues[j], 0, newRow, 1, rowClues[j].length);
                    rowClues[j] = newRow;
                }
            }
            maxRowClues += clueArr.length - maxRowClues;
            width = BOXSIZE * (maxRowClues + puzzleWidth);
            puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];

            System.arraycopy(clueArr, 0, rowClues[rowIndex], clueArr.length - maxRowClues, clueArr.length);
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.black);

        g.translate(x, y);

        for(int i = 0; i < height / BOXSIZE; i++) { // For each row
            for(int j = 0; j < width / BOXSIZE; j++) { // For each column in each row
                puzzle[i][j] = new Box(BOXSIZE * j, BOXSIZE * i, BOXSIZE, BOXSIZE, 0, false);

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

    public int[][] getColumnClues() {
        return columnClues;
    }

    public int[][] getRowClues() {
        return rowClues;
    }
}
