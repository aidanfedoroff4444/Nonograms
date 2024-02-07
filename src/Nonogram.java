import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * Handle everything to do with printing a nonogram
 */
public class Nonogram extends Rectangle {
    private int boxSize = 40;
    private int maxRowClues = 2, maxColClues = 1;
    private  int puzzleWidth = 5, puzzleHeight = 5;
    private Box[][] puzzle;
    private int[][] columnClues, rowClues;

    public Nonogram(int[][] columnClues, int[][] rowClues) {
        super(100, 100, 0, 0);
        width = boxSize * (maxRowClues + puzzleWidth);
        height = boxSize * (maxColClues + puzzleHeight);
        this.columnClues = columnClues;
        this.rowClues = rowClues;
    }
    public void paint(Graphics2D g) {
        g.setColor(Color.black);
        g.translate(x, y);

        /*
            Initialize a 2d array of Box objects to represent the puzzle and its clues
            Initialize all the clues' values and backgrounds
         */
        puzzle = new Box[puzzleHeight + maxColClues][puzzleWidth + maxRowClues];
        for(int i = 0; i < puzzle.length; i++) {
            // Runs for each row
            for(int j = 0; j < puzzle[0].length; j++) {
                // Runs for each column inside each row
                puzzle[i][j] = new Box(boxSize * j, boxSize * i, boxSize, boxSize, 0);

                // Set the border width of each box
                int[] squareBorderWidths = new int[] {1, 1, 1, 1};

                if(i == 0) squareBorderWidths[0]++; // For the top outline edge
                if(i == puzzle.length - 1) squareBorderWidths[2]++; // For the bottom outline edge
                if(j == 0) squareBorderWidths[3]++; // For the left outline edge
                if(j == puzzle[0].length - 1) squareBorderWidths[1]++; // For the right outline edge

                if(i == maxColClues - 1) squareBorderWidths[2]++; // For the bottom edges of the column clues
                if(j == maxRowClues - 1) squareBorderWidths[1]++; // For the right edges of the row clues

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

class Box extends Rectangle {
    private int[] borderWidths; // the 0th index is the top border, and it goes clockwise. So 1 is the right edge border
    private Color background; // The background of the square
    public int symbol = 0; // 0 for empty, 1 for filled, 2 for dot
    private int value;

    public Box(int x, int y, int width, int height, int symbol) {
        super(x, y, width, height);
        this.symbol = symbol;

        borderWidths = new int[] {1, 1, 1, 1};
        background = Color.white;
        value = 0;
    }

    public void paint(Graphics2D g) {
        Color tempColor = g.getColor();
        // Draw the background
        g.setColor(background);
        g.fillRect(x, y, width, height);

        g.setColor(Color.black);
        // Draw all 4 edges separately
        // The top edge
        g.setStroke(new BasicStroke(borderWidths[0]));
        g.drawLine(x, y, x + width, y);
        // The right edge
        g.setStroke(new BasicStroke(borderWidths[1]));
        g.drawLine(x + width, y, x + width, y + height);
        // The bottom edge
        g.setStroke(new BasicStroke(borderWidths[2]));
        g.drawLine(x, y + height, x + width, y + height);
        // The left edge
        g.setStroke(new BasicStroke(borderWidths[3]));
        g.drawLine(x, y, x, y + height);

        // Draw the clues
        if(value != 0) {
            // Prepare the font
            String fontName = "Arial";
            int fontStyle = Font.BOLD;
            int fontSize = 15;
            // Get a font render context
            FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
            // Get the largest font size where the text still fits inside the box horizontally and vertically
            double textWidth;
            double textHeight;
            do {
                textWidth = new Font(fontName, fontStyle, fontSize).getStringBounds(String.valueOf(value), frc).getWidth();
                textHeight = new Font(fontName, fontStyle, fontSize).getStringBounds(String.valueOf(value), frc).getHeight();
                fontSize++;
            } while(textWidth < width && textHeight < height);
            // Set the font
            g.setFont(new Font(fontName, Font.BOLD, fontSize));

            // Draw the value
            System.out.println((int)textHeight);
            g.drawString(String.valueOf(value), x + (int) textWidth / 2, y + height - (width / 8));
        }

        g.setColor(tempColor);
    }
    /*
    private int getMaximumFontSize(String name, int weight) {
        String text = "00";
        int fontSize = 12;
        Font font;
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int textWidth, textHeight;
        int cluePadding = 5;
        int fontSizeOffset = 8;

        do {
            font = new Font(name, weight, fontSize);
            textWidth = (int)(font.getStringBounds(text, frc).getWidth());
            textHeight = (int)(font.getStringBounds(text, frc).getHeight());
            fontSize++;
        } while( textWidth <= boxSize - (cluePadding * 2)  && textHeight <= boxSize - (cluePadding * 2) );

        fontSize -= fontSizeOffset;
        return fontSize;
    }
    */

    public int[] getBorderWidths() {
        return borderWidths;
    }
    public void setBorders(int[] newBorderWidths) {
        borderWidths = newBorderWidths;
    }
    public void setBackground(Color newBackground) {
        background = newBackground;
    }
    public void setValue(int newValue) {
        value = newValue;
    }
}
