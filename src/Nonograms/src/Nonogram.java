package Nonograms.src;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
/**
 * Handle everything to do with printing a nonogram
 */
public class Nonogram extends Rectangle {
    private final int BOXSIZE = 40;
    private final int maxRowClues, maxColClues;
    private final int puzzleWidth, puzzleHeight;
    private Box[][] puzzle;
    private final int[][] columnClues, rowClues;

    public Nonogram(int[][] columnClues, int[][] rowClues) {
        super(100, 100, 0, 0);
        maxRowClues = rowClues[0].length;
        maxColClues = columnClues[0].length;
        puzzleWidth = columnClues.length;
        puzzleHeight = rowClues.length;
        width = BOXSIZE * (maxRowClues + puzzleWidth);
        height = BOXSIZE * (maxColClues + puzzleHeight);
        this.columnClues = columnClues;
        this.rowClues = rowClues;

        puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.black);
        g.translate(x, y);

        /*
         *  Initialize a 2d array of Box objects to represent the puzzle and its clues
         *  Initialize all the clues' values and backgrounds
         */
        for(int i = 0; i < puzzle.length; i++) { // Runs for each row
            for(int j = 0; j < puzzle[0].length; j++) { // Runs for each column inside each row
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
                if(i < maxColClues && j >= maxRowClues) puzzle[i][j].setValue(columnClues[j - maxRowClues][i]);
                // Set the value of the boxes for the rowClues
                if(j < maxRowClues && i >= maxColClues) puzzle[i][j].setValue(rowClues[i - maxColClues][j]);
            }
        }

        for(Box[] boxRow : puzzle)
            for(Box box : boxRow)
                box.paint(g); // Draw each square
    }
}