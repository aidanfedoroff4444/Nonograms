import java.awt.*;

public class EditableNonogram extends Rectangle {
    private final int BOXSIZE = 40;
    private int maxRowClues, maxColClues;
    private int puzzleWidth = 10, puzzleHeight = 10;
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

    public void paint(Graphics2D g) {
        g.setColor(Color.black);
        g.translate(x, y);

        for(int i = 0; i < puzzle.length; i++) { // For each row
            for(int j = 0; j < puzzle[0].length; j++) { // For each column in each row
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
