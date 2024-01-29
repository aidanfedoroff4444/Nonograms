import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class MyButton extends Component {
    /**
     * The coordinates of the top left of the rectangle (x, y)
     */
    private Point position;
    /**
     * The width and height of the rectangle
     */
    private Dimension dimensions;
    /**
     * The background color of the button.
     */
    private Color backgroundColor;
    /**
     * The text color of the button.
     */
    private Color textColor;
    /**
     * The text of the button
     */
    private String text;
    private final int FONTSIZE = 32;

    public MyButton() {
        this("Default MyButton", 0, 0, 100, 20);
    }
    public MyButton(String text, int x, int y, int width, int height) {
        this.text = text;
        position = new Point(x, y);
        dimensions = new Dimension(width, height);
    }

    public void paint(Graphics2D g) {
        Color tempColor = g.getColor();

        g.setColor(Color.lightGray);
        g.fillRect(0, 0, dimensions.width, dimensions.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, dimensions.width, dimensions.height);
        g.setFont(new Font("Arial", Font.PLAIN, FONTSIZE));
        int greyscale = 50;
        g.setColor(new Color(greyscale, greyscale, greyscale));
        drawStringFromCenter(text, dimensions.width / 2, dimensions.height / 2, g);

        g.setColor(tempColor);
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

    public int mousePressed(MouseEvent e) {
        // System.out.println("This click is inside the button: (" + e.getX() + ", " + e.getY() + ")");
        return 0;
    }

    public Point getPosition() { return position; }
    public void setPosition(Point newPos) { if(newPos != null) position = newPos; }

    public Dimension getDimensions() { return dimensions; }
    public void setPosition(Dimension newDimension) { if(newDimension != null) dimensions = newDimension; }

    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color newBackgroundColor) { if(newBackgroundColor != null) backgroundColor = newBackgroundColor; }

    public Color getTextColor() { return textColor; }
    public void setTextColor(Color newTextColor) { if(newTextColor != null) textColor = newTextColor; }

    public String getText() { return text; }
    public void setText(String newText) { if(newText != null) text = newText; }
}
