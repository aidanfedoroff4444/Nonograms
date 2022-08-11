import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String... args) {
        JFrame frame = new JFrame();
        frame.getContentPane().addMouseListener(new ClickListener());

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(size.getWidth() * 1);
        int height = (int)(size.getHeight() * 0.8);
        System.out.println("Current Screen resolution : " + width + " by " + height);

        frame.setBounds(0, 0, width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(3);
        Grid myGrid = new Grid(width, height - 30, 10,10, 20);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                myGrid.paint(g2);
            }
        };
        frame.add(panel);
        frame.setVisible(true);
    }
}
