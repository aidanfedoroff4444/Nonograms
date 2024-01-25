import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    private static final int PUZZLEWIDTH = 5;
    private static final int PUZZLEHEIGHT = 5;
    private static final int MARGIN = 20;
    private static final int SCREENHEIGHTOFFSET = 30;
    public static void main(String... args) {
        // Create a JFrame instance
        JFrame f = new JFrame();
        // Add a listener for the user's mouse
        f.getContentPane().addMouseListener(new ClickListener());

        // Get the local screen size
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the size of the window using percentages
        int screenWidth = (int)(size.getWidth() * 0.6);
        int screenHeight = (int)(size.getHeight() * 0.8);
        System.out.println("Current Screen resolution : " + screenWidth + " by " + screenHeight);

        // Set the bound and size of the window
        f.setBounds(0, 0, screenWidth, screenHeight);
        // Center the window on the screen
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Grid myGrid = new Grid(screenWidth, screenHeight - SCREENHEIGHTOFFSET, PUZZLEWIDTH, PUZZLEHEIGHT, MARGIN);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                myGrid.paint(g2);
            }
        };
        f.add(panel);
        f.setVisible(true);
    }
}
