package Nonograms.src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Box extends Rectangle {
    private int[] borderWidths; // the 0th index is the top border, and it goes clockwise. So 1 is the right edge border
    private Color background; // The background of the square
    public int symbol = 0; // 0 for empty, 1 for filled, 2 for dot
    private int value;
    private final boolean EDITABLE;

    public Box(int x, int y, int width, int height, int symbol, boolean editable) {
        super(x, y, width, height);
        this.symbol = symbol;

        borderWidths = new int[] {1, 1, 1, 1};
        background = Color.white;
        value = 0;

        EDITABLE = editable;
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("A box was clicked!");
        symbol = (symbol + 1) % 3;
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

        // Draw the values
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
            g.drawString(String.valueOf(value), x + (int) textWidth / 2, y + height - (height / 8));
        }

        // Draw the symbol
        if(symbol == 1) {
            System.out.println("Tried to draw filled box");
            g.fillRect(x, y, width, height);
        } else if(symbol == 2) {
            System.out.println("Tried to draw dot");
            g.fillOval(x, y, width, height);
        }

        g.setColor(tempColor);
    }
    public void setBorders(int[] newBorderWidths) { borderWidths = newBorderWidths; }
    public void setBackground(Color newBackground) { background = newBackground; }
    public void setValue(int newValue) { value = newValue; }
}