package Nonograms.src;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Handle everything to do with printing a nonogram
 */
public class Nonogram extends Rectangle {
    public final int BOXSIZE = 40;
    private final int maxRowClues, maxColClues;
    private final int puzzleWidth, puzzleHeight;
    public Box[][] puzzle;
    private final int[][] columnClues, rowClues;
    public int focusedRow, focusedCol;

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

    public void mousePressed(MouseEvent e) {
        int mX = e.getX();
        int mY = e.getY();

        // If the click was inside the puzzle
        if(mX >= x && mX <= x + width && mY >= y && mY <= y + height) {
            // Get the boxRow and boxCol that was clicked
            int boxRow = (mY - y) / BOXSIZE;
            int boxCol = (mX - x) / BOXSIZE;
            // If the box that was clicked was inside the puzzle and not in the clue area
            if(boxRow >= maxColClues && boxCol >= maxRowClues)
                // Call the appropriate mousePressed(e) function for the correct box that was clicked
                puzzle[boxRow][boxCol].mousePressed(e);
        }
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
                if(puzzle[i][j] == null || puzzle[i][j].symbol == 0)
                    puzzle[i][j] = new Box(BOXSIZE * j, BOXSIZE * i, BOXSIZE, BOXSIZE, 0, true);

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

                // Set the background of the focusedRow and focusedCol clue col/row with a slightly lighter gray
                if(focusedRow >= maxColClues && i == focusedRow && j < maxRowClues) puzzle[i][j].setBackground(new Color(220, 220, 220));
                if(focusedCol >= maxRowClues && j == focusedCol && i < maxColClues) puzzle[i][j].setBackground(new Color(220, 220, 220));

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

    public void mouseMoved(MouseEvent e) {
        int mX = e.getX();
        int mY = e.getY();
        // Set the focusedRow and focusedCol relative to the column and row of the current mouse position
        focusedRow = (mY - y) / BOXSIZE;
        focusedCol = (mX - x) / BOXSIZE;
    }

    public void mouseDragged(MouseEvent e, int newSymbol) {
        int mX = e.getX();
        int mY = e.getY();
        int boxCol = (mX - x) / BOXSIZE;
        int boxRow = (mY - y) / BOXSIZE;
        if(boxRow >= maxRowClues && boxCol >= maxColClues)
            // Set the box under the mouse to the newSymbol
            puzzle[boxRow][boxCol].symbol = newSymbol;
    }

    public void checkSolution() {
        // Check each box that is not a clue in the puzzle. if there is one zero than return;
        for(int i = maxColClues; i < puzzle.length; i++)
            for(int j = maxRowClues; j < puzzle[0].length; j++)
                if(puzzle[i][j].symbol == 0) return;
        System.out.println("Checking Solution");
    }
}