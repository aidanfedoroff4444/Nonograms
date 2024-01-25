import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Scanner;

public class Grid {
    private int[][] columnClues;
    private int[][] rowClues;
    private int[][] puzzle; // 2's, 1's, and 0's. 2 is filled, 1 is not filled(the box has an x), 0 is empty

    private final Scanner sc;

    // Styling
    private final Color puzzleColor;
    private final int puzzleWidth, puzzleHeight;
    private int width, height;
    private final int screenWidth, screenHeight;
    private int availableWidth, availableHeight;
    private int boxSize;
    private int gridWidth, gridHeight;
    private int gridOriginX, gridOriginY;
    private final int margin;
    private int maxNumOfColumnClues, maxNumOfRowClues;
    private Font font;
    private final int FONTSIZEOFFSET;

    private final boolean useDefault;

    public Grid(int screenWidth, int screenHeight, int puzzleWidth, int puzzleHeight, int margin) {
        FONTSIZEOFFSET = 8;
        puzzleColor = new Color(15,15,15);

        this.puzzleWidth = puzzleWidth;
        this.puzzleHeight = puzzleHeight;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.margin = margin;

        // Ask for default grid
        // Test Variables
        useDefault = true;
        sc = new Scanner(System.in);

        initializeGrid();
        // printDebug();
        solvePuzzle();
    }

    private void initializeGrid() {
        initializeClues();
        // Get the dimensions for the grid
        availableWidth = screenWidth - (margin * 2);
        availableHeight = screenHeight - (margin * 2);

        boxSize = Math.min(availableHeight / height, availableWidth / width);

        gridWidth = boxSize * width;
        gridHeight = boxSize * height;

        gridOriginX = availableWidth/2 - gridWidth/2;
        gridOriginY = availableHeight/2 - gridHeight/2 + margin;

        String fontName = "Arial";
        int fontWeight = Font.PLAIN;
        int fontSize = getMaximumFontSize(fontName, fontWeight);
        font = new Font(fontName, fontWeight, fontSize);
    }

    private void initializeClues() {
        if(useDefault) {
            maxNumOfColumnClues = 2;
            maxNumOfRowClues = 1;
        } else {
            System.out.println("Maximum number of clues for the clues in the columns: ");
            maxNumOfColumnClues = sc.nextInt();
            System.out.println("Maximum number of clues for the clues in the rows: ");
            maxNumOfRowClues = sc.nextInt();
        }

        columnClues = new int[puzzleWidth][maxNumOfColumnClues];
        rowClues = new int[puzzleHeight][maxNumOfRowClues];

        width = puzzleWidth + maxNumOfRowClues;
        height = puzzleHeight + maxNumOfColumnClues;

        if(useDefault) {
            /* 10 x 10
            // P
            columnClues = new int[][]{
                    {1, 0},
                    {1,0},
                    {10,0},
                    {10,0},
                    {2,2},
                    {2,2},
                    {2,1},
                    {5,0},
                    {3,0},
                    {0,0}
            };
            rowClues = new int[][] {
                    {5,0},
                    {6,0},
                    {2,2},
                    {2,2},
                    {2,2},
                    {2,1},
                    {5,0},
                    {4,0},
                    {2,0},
                    {4,0}
            };
            // Random Testing
            columnClues = new int[][]{
                    {10,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {1,0},
                    {10,0}
            };
            rowClues = new int[][] {
                    {1,1},
                    {1,1},
                    {1,1},
                    {4,1},
                    {1,1},
                    {1,1},
                    {1,1},
                    {1,1},
                    {4,1},
                    {1,1}
            };
             */
            columnClues = new int[][]{
                    {3,0},
                    {1,1},
                    {1,3},
                    {2,0},
                    {2,0}
            };
            rowClues = new int[][]{
                    {3},
                    {1},
                    {3},
                    {3},
                    {3}
            };
        } else {
            for(int i = 0; i < puzzleWidth; i++) {
                System.out.println("How many clues are in Column #" + (i+1) + ":");
                int numOfClues = sc.nextInt();
                System.out.println("Enter clues for Column #" + (i+1) + ": ");
                for(int j = 0; j < numOfClues; j++) columnClues[i][j] = sc.nextInt();
            }
            for(int i = 0; i < puzzleHeight; i++) {
                System.out.println("How many clues are in Row #" + (i+1) + ":");
                int numOfClues = sc.nextInt();
                System.out.println("Enter clues for Row #" + (i+1) + ":");
                for(int j = 0; j < numOfClues; j++) rowClues[i][j] = sc.nextInt();
            }
        }
    }

    private void printDebug() {
        System.out.println("puzzleWidth: " + puzzleWidth);
        System.out.println("puzzleHeight: " + puzzleHeight);
        System.out.println("screenWidth: " + screenWidth);
        System.out.println("screenHeight: " + screenHeight);
        System.out.println("margin: " + margin);

        System.out.println("width: " + width);
        System.out.println("height: " + height);

        System.out.println("columnClues: ");
        print2DIntArray(columnClues);
        System.out.println("rowClues: ");
        print2DIntArray(rowClues);

        System.out.println("availableWidth: " + availableWidth);
        System.out.println("availableHeight: " + availableHeight);
        System.out.println("boxSize: " + boxSize);
        System.out.println("gridWidth: " + gridWidth);
        System.out.println("gridHeight: " + gridHeight);
        System.out.println("gridOriginX: " + gridOriginX);
        System.out.println("gridOriginY: " + gridOriginY);
    }

    private void print2DIntArray(int[][] arr2D) {
        System.out.print("[");
        for(int i = 0; i < arr2D.length; i++) {
            System.out.print("[");
            for(int j = 0; j < arr2D[i].length; j++) {
                System.out.print(arr2D[i][j]);
                if(j != arr2D[i].length - 1) System.out.print(", ");
            }
            System.out.print("]");
            if(i != arr2D.length - 1) System.out.print(", ");
            System.out.println();
        }
    }

    private int getMaximumFontSize(String name, int weight) {
        String text = "00";
        int fontSize = 12;
        Font font;
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int textWidth, textHeight;
        int cluePadding = 5;

        do {
            font = new Font(name, weight, fontSize);
            textWidth = (int)(font.getStringBounds(text, frc).getWidth());
            textHeight = (int)(font.getStringBounds(text, frc).getHeight());
            fontSize++;
        } while( textWidth <= boxSize - (cluePadding * 2)  && textHeight <= boxSize - (cluePadding * 2) );

        fontSize -= FONTSIZEOFFSET;
        return fontSize;
    }

    public void paint(@NotNull Graphics2D g) {
        drawPuzzle(g);

        /*
         * Draw the light grey background for the clues
         */
        g.setColor(Color.lightGray);
        g.fillRect(gridOriginX + (boxSize * maxNumOfRowClues),
                gridOriginY,
                gridWidth - (boxSize * maxNumOfRowClues),
                boxSize * maxNumOfColumnClues);
        g.fillRect(gridOriginX,
                gridOriginY + (boxSize * maxNumOfColumnClues),
                boxSize * maxNumOfRowClues,
                gridHeight - (boxSize * maxNumOfColumnClues));
        g.setColor(Color.black);

        /*
         * Draw outline of the grid
         */
        g.drawRect(gridOriginX, gridOriginY, gridWidth, gridHeight);

        /*
         * Draw the lines for the grid
         */
        // Vertical Lines:
        for(int i = 1; i <= width - 1; i++) {
            g.setStroke(new BasicStroke(2));
            int x1 = gridOriginX + boxSize * i;
            int y1 = gridOriginY;
            int x2 = gridOriginX + boxSize * i;
            int y2 = gridOriginY + gridHeight;

            // Skip the negative space in the top left
            if(i < maxNumOfRowClues) y1 = gridOriginY + boxSize * maxNumOfColumnClues;
            // The lines that are multiples of 5 are bolded
            if(i > maxNumOfRowClues && (i - maxNumOfRowClues) % 5 == 0) g.setStroke(new BasicStroke(3));
            g.drawLine(x1, y1, x2, y2);
        }
        // Horizontal Lines:
        for(int i = 1; i <= height - 1; i++) {
            g.setStroke(new BasicStroke(2));
            int x1 = gridOriginX;
            int y1 = gridOriginY + boxSize * i;
            int x2 = gridOriginX + gridWidth;
            int y2 = gridOriginY + boxSize * i;

            // Skip the negative space in the top left
            if(i < maxNumOfColumnClues) x1 = gridOriginX + maxNumOfRowClues * boxSize;
            // The lines that are multiples of 5 are bolded
            if(i > maxNumOfColumnClues && (i - maxNumOfColumnClues) % 5 == 0) g.setStroke(new BasicStroke(3));

            g.drawLine(x1, y1, x2, y2);
        }

        /*
         * Draw the clue numbers
         */
        g.setStroke(new BasicStroke(1));
        g.setFont(font);
        // Print the columnClues
        // System.out.println("columnClues: " + columnClues.length * columnClues[0].length);
        for(int i = 0; i < columnClues.length; i++) {
            for(int j = 0; j < nonZerosInArray(columnClues[i]); j++) {
                // int clueValue = columnClues[i][columnClues[i].length - j - 1];
                int clueValue = columnClues[i][j];
                if( clueValue == 0 ) continue;
                String clueText = String.valueOf(clueValue);
                int x1 = gridOriginX + boxSize * (maxNumOfRowClues + i) + boxSize/2;
                int y1;
                if( columnClues[i].length != nonZerosInArray(columnClues[i]) ) y1 = gridOriginY + boxSize * j + boxSize/2 - (columnClues[i].length - nonZerosInArray(columnClues[i]) * boxSize);
                else y1 = gridOriginY + boxSize * j + boxSize/2;

                drawStringFromCenter(clueText, x1, y1, g);
            }
        }
        // Print the rowClues
        // System.out.println("rowClues: " + rowClues.length * rowClues[0].length);
        for(int i = 0; i < rowClues.length; i++) {
            for(int j = 0; j < rowClues[i].length; j++) {
                int clueValue = rowClues[i][rowClues[i].length - j - 1];
                if( clueValue == 0 ) continue;
                String clueText = String.valueOf(clueValue);
                int x1 = gridOriginX + boxSize * j + boxSize/2;
                int y1 = gridOriginY + boxSize * (maxNumOfColumnClues + i) + boxSize/2;

                drawStringFromCenter(clueText, x1, y1, g);
            }
        }
    }

    public int nonZerosInArray(int[] arr) {
        int out = 0;
        for( int n : arr ) if ( n != 0 ) out++;
        return out;
    }

    private void drawStringFromCenter(@NotNull String str, int x1, int y1, Graphics2D g) {
        int digitXOffset = 2;
        if( str.length() > 1 ) x1 -= digitXOffset;
        int yOffset = 2;
        y1 += yOffset;
        Rectangle2D fmBounds = g.getFontMetrics().getStringBounds(str, g);
        // System.out.println("fm.getStringBounds(str, g): " + fmBounds);

        x1 = (int)(x1 - fmBounds.getWidth()/2);
        y1 = (int)(y1 + Math.abs(fmBounds.getY()) * 2/5 );

        g.drawString(str, x1, y1);
    }

    private void solvePuzzle() {
        puzzle = new int[puzzleHeight][puzzleWidth];

        /*
         * Input whatever fits in the full length of the row or column
         *
         * clue + space + clue = total
         * width/height - total = takeaway
         *
         * output the 2's:
         * skip takeaway + (clue - takeaway) + space + takeaway + (clue - takeaway)
         */
        // Rows
        for( int i = 0; i < puzzleHeight; i++ ) {
            int nonZeroClues = nonZerosInArray(rowClues[i]);
            if( nonZeroClues != 0 ) {
                int total = 0;
                for( int j = 0; j < rowClues[i].length; j++) total += rowClues[i][j];
                total += nonZeroClues - 1;
                int takeaway = puzzleWidth - total;
            }
            for( int j = 0; j < puzzleWidth; j++ ) {

            }
        }

        /*
         * Check for empty rows and columns
        // Rows
        for( int i = 0; i < puzzleHeight; i++ ) {
            if( rowClues[i][0] == 0 ) {
                for( int j = 0; j < puzzleWidth; j++ ) puzzle[i][j] = 1;
            }
        }
        // Columns
        for( int i = 0; i < puzzleWidth; i++ ) {
            if( columnClues[i][0] == 0 ) {
                for( int j = 0; j < puzzleHeight; j++ ) puzzle[j][i] = 1;
            }
        }
        /*
         * Check for full rows and columns
        // Rows
        for( int i = 0; i < puzzleHeight; i++ ) {
            if( rowClues[i][0] == puzzleWidth ) {
                for( int j = 0; j < puzzleWidth; j++ ) puzzle[i][j] = 2;
            }
        }
        // Columns
        for( int i = 0; i < puzzleWidth; i++ ) {
            if( columnClues[i][0] == puzzleHeight ) {
                for( int j = 0; j < puzzleHeight; j++ ) puzzle[j][i] = 2;
            }
        }
        */
        /*
         * extends groups away from the wall they are touching
         * Ex: 4 [1,0,0,0,0,0,0,0,0,0] =>
         *     4 [1,1,1,1,0,0,0,0,0,0]
         *
         * Ex: 3 [0,0,0,0,0,0,0,0,0,1] =>
         *     3 [0,0,0,0,0,0,0,1,1,1]
         *
         * Check if there is a filled box right next to the beginning wall(first box in row or column)
         * Check the first clue
         * Extend the group outward from the wall by (clue - 1)
         *
         * Check if there is a filled box next to the further side of the wall(last box in row or column)
         * Check from the back of the clues for the first clue that is not a 0
         * Extend the group outward from the wall  by (clue - 1)
         // Rows
        for( int i = 0; i < puzzleHeight; i++ ) {
            if( puzzle[i][0] == 2 ) {
                for(int j = 1; j < rowClues[i][0]; j++) {
                    puzzle[i][j] = 2;
                }
            }
            if( puzzle[i][puzzleWidth - 1] == 2 ) {
                for(int j = 1; j < rowClues[i][0]; j++ ) {
                    puzzle[i][puzzleWidth - j - 1] = 2;
                }
            }
        }
         */


        print2DIntArray(puzzle);
    }

    private void drawPuzzle(Graphics2D g) {
        g.setColor(puzzleColor);
        int puzzleOriginX = gridOriginX + maxNumOfRowClues * boxSize;
        int puzzleOriginY = gridOriginY + maxNumOfColumnClues * boxSize;
        int x1, y1;
        int circleSize = boxSize/5;

        for(int i = 0; i < puzzle.length; i++) { // Row
            y1 = puzzleOriginY + boxSize * i;
            for(int j = 0; j < puzzle[0].length; j++) { // Column
                x1 = puzzleOriginX + boxSize * j;
                if( puzzle[i][j] == 2 ) g.fillRect(x1, y1, boxSize, boxSize);
                if( puzzle[i][j] == 1 ) g.fillOval(x1 + boxSize/2 - circleSize/2, y1 + boxSize/2 - circleSize/2, circleSize, circleSize);
            }
        }
        g.setColor(Color.black);
    }
}
