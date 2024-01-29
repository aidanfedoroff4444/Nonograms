import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Main {

    private static final int PUZZLEWIDTH = 5;
    private static final int PUZZLEHEIGHT = 5;
    private static final int MARGIN = 20;
    private static final int SCREENHEIGHTOFFSET = 30;
    private static Graphics2D g2;
    public static void main(String... args) {
        // Create a JFrame instance
        JFrame f = new JFrame();

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

        Nonogram myNonogram = new Nonogram(screenWidth, screenHeight - SCREENHEIGHTOFFSET, PUZZLEWIDTH, PUZZLEHEIGHT, MARGIN);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g2 = (Graphics2D) g;
                myNonogram.paint(g2);
                // myGrid.printDebug();
            }
        };

        // Add the JPanel to the JFrame
        f.add(panel);

        // Add a listener for the user's mouse
        f.getContentPane().addMouseListener(new ClickListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                myNonogram.mousePressed(e);
                // Repaint the JFrame
                f.repaint();
            }
        });
        f.setVisible(true);
    }
}
