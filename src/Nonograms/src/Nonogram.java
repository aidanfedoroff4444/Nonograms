package Nonograms.src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

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
        if(boxCol >= maxRowClues && boxCol < puzzleWidth + maxRowClues && boxRow >= maxColClues && boxRow < puzzleHeight + maxColClues)
            // Set the box under the mouse to the newSymbol
            puzzle[boxRow][boxCol].symbol = newSymbol;
    }

    public boolean checkSolution() {
        // Check the rows of the puzzle
        // System.out.println("The rows are valid: " + validateCompleteRows(maxColClues, maxRowClues, puzzle, rowClues));

        // Flip the puzzle
        // Create a new Box[][] array where the rows of the new array are the columns of puzzle
        Box[][] flippedPuzzle = new Box[puzzle[0].length][puzzle.length];
        for(int j = 0; j < puzzle[0].length; j++) { // For each column in puzzle
            Box[] newRow = new Box[puzzle.length];
            for(int i = 0; i < puzzle.length; i++) {
                newRow[i] = puzzle[i][j];
            }
            flippedPuzzle[j] = newRow;
        }

        return validateCompleteRows(maxColClues, maxRowClues, puzzle, rowClues) && validateCompleteRows(maxRowClues, maxColClues, flippedPuzzle, columnClues);
/*
        int[][] symbols = new int[flippedPuzzle.length][flippedPuzzle[0].length];
        for(int i = 0; i < flippedPuzzle.length; i++)
            for(int j = 0 ; j < flippedPuzzle[0].length; j++)
                symbols[i][j] = flippedPuzzle[i][j].symbol;
        // System.out.println("symbols: " + Arrays.deepToString(symbols));

        // System.out.println("The rows are valid: " + validateCompleteRows(maxRowClues, maxColClues, flippedPuzzle, columnClues));
 */
    }

    public boolean validateCompleteRows(int maxColClues, int maxRowClues, Box[][] puzzle, int[][] rowClues) {
        // System.out.println("checking solution:");
        // Checks each box that is not a clue in the puzzle. if there is one zero than return;
        for(int i = maxColClues; i < puzzle.length; i++) { // Check each row has the same groups in the same order as the clues
            // Get the groups that are in the row of puzzle[i]
            // A group has a length and a symbol
            int currSymbol, prevSymbol = -1, groupLength = 0, groupSymbol = -1;
            NonogramRowGroups nonogramRowGroups = new NonogramRowGroups();
            for(int j = maxRowClues; j < puzzle[i].length; j++) {
                // System.out.println("puzzle.length: " + puzzle.length + " - puzzle[i].length: " + puzzle[i].length + "\ni: " + i + " - j: " + j);
                if (puzzle[i][j].symbol == 0) {
                    System.out.println("Checked puzzle but it was incomplete. (i,j): (" + i + "," + j + ")");
                    return false;
                }
                currSymbol = puzzle[i][j].symbol;

                // If there is no prevSymbol
                if(prevSymbol == -1) {
                    groupLength = 1;
                    groupSymbol = currSymbol;
                } else if(currSymbol == prevSymbol) { // If there currSymbol is different from the prevSymbol. print test
                    // System.out.println("Found two" + currSymbol + " symbols in a row");
                    groupLength++;
                } else {
                    // System.out.println("Found a " + prevSymbol + " symbol next to a " + currSymbol + " symbol");
                    // add groupLength and groupSymbol to the NonogramRowGroups
                    nonogramRowGroups.groupSymbols.add(groupSymbol);
                    nonogramRowGroups.groupLengths.add(groupLength);
                    groupSymbol = currSymbol;
                    groupLength = 1;
                }
                prevSymbol = currSymbol;
            }
            nonogramRowGroups.groupSymbols.add(groupSymbol);
            nonogramRowGroups.groupLengths.add(groupLength);
            // All groupLength and groupSymbol pairs have been added for the row
            // Check that they are valid respective to the relative clues for the row
            int[] clues = rowClues[i - maxColClues];
            // System.out.println("i: " + i + ". clues: " + Arrays.toString(clues));
            // System.out.println("groupsLengths: " + nonogramRowGroups.groupLengths + "\ngroupSymbols:  " + nonogramRowGroups.groupSymbols);

            // Convert nonogramRowGroups to a single int[x] array of the groups of black squares, where x is The number of non-zero clues in clues
            int numOfClues = 0;
            for(int clue : clues) if(clue != 0) numOfClues++;
            ArrayList<Integer> groups = new ArrayList<>();
            for(int j = 0; j < nonogramRowGroups.groupLengths.size(); j++) // Find each group with a symbol of 1
                // If the symbol is a 1. add it to groups
                if(nonogramRowGroups.groupSymbols.get(j) == 1) {
                    groups.add(nonogramRowGroups.groupLengths.get(j));
                }
            // System.out.println("groups: " + groups);

            // get the non-zero clues in a single array
            int[] nonZeroClues = new int[numOfClues];
            int index = 0;
            for(int j = 0; j < clues.length; j++) {
                if (clues[j] != 0) {
                    nonZeroClues[index] = clues[j];
                    index++;
                }
            }
            // Convert groups to an int[]
            int[] intGroups = new int[groups.size()];
            for(int j = 0; j < groups.size(); j++) intGroups[j] = groups.get(j);

            // System.out.println("nonZeroClues: " + Arrays.toString(nonZeroClues));
            // System.out.println("The row is valid: " + Arrays.equals(nonZeroClues, intGroups));
            if(!Arrays.equals(nonZeroClues, intGroups)) return false;
        }
        return true;
    }

    public boolean attemptSolution() throws InterruptedException {
        System.out.println("Original State:");
        printPuzzleState();

        // Create a 2d int array of the current state of the puzzle's Boxes
        int[][] puzzleValues = new int[puzzleHeight][puzzleWidth];
        for(int i = 0; i < puzzleHeight; i++) {
            // Define a row
            int[] row = new int[puzzleWidth];
            for(int j = 0; j < puzzleWidth; j++)
                row[j] = puzzle[i + maxColClues][j + maxRowClues].symbol;
        }
        // Start solving strategies:

        //
        /**
         * Check for rows that have completed groups of 1's:
         * 
         */


        return true;
    }

    public void printPuzzleState() {
        // Print the column clues above the puzzle for the console
        // System.out.println(Arrays.deepToString(columnClues));
        for(int i = 0; i < maxRowClues + 5; i++) System.out.print(" ");
        System.out.print("[");
        for(int[] row : columnClues) {
            StringBuilder out = new StringBuilder("");
            for(int e : row) {
                out.append(e);
            }
            if(row != columnClues[columnClues.length - 1]) out.append(", ");
            else out.append("]\n");
            System.out.print(out);
        }

        // Create a 2d int array of the current state of the puzzle's Boxes
        int[][] puzzleValues = new int[puzzleHeight][puzzleWidth];
        for(int i = 0; i < puzzleHeight; i++) {
            // Print the row of the rowClues for the row of the puzzle
            System.out.print(Arrays.toString(rowClues[i]) + " ");
            // Define a row
            int[] row = new int[puzzleWidth];
            for(int j = 0; j < puzzleWidth; j++) {
                row[j] = puzzle[i + maxColClues][j + maxRowClues].symbol;
            }
            // Print the row
            System.out.println(Arrays.toString(row));
        }
    }
}