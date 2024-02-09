package Nonograms.src;

import Nonograms.src.EditableNonogram;
import Nonograms.src.Nonogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 */
public class GameWindow extends JFrame {
    private final GraphicsDevice device;
    private EditableNonogram editableNonogram;
    private Nonogram nonogram;

    private JPanel panel;

    private final int[][] columnClues = new int[][] {
            {0,0,2},
            {0,2,1},
            {0,0,6},
            {0,0,3},
            {0,0,4},
            {0,1,5},
            {1,2,1},
            {0,0,3},
            {0,0,2},
    };
    private final int[][] rowClues = new int[][] {
            {0,0,4},
            {0,5,1},
            {0,7,1},
            {1,1,5},
            {0,3,4},
            {0,1,1},
            {0,1,1},
            {0,1,1},
    };

    public GameWindow(GraphicsDevice device) {
        super("Nonogram Editor", device.getDefaultConfiguration());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(initMenu());

        /*
            Draw everything on the GameWindow under the menu
         */
        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                editableNonogram = new EditableNonogram();
                editableNonogram.paint(g2);

                // Nonogram nonogram = new Nonogram(columnClues, rowClues);
                // nonogram.paint(g2);
            }
        };
        add(panel);

        JFrame frame = this;

        getContentPane().addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("click: (" + e.getX() + "," + e.getY() + ")");
                editableNonogram.mousePressed(e, frame);
                frame.repaint();
            }
            public void mouseReleased(MouseEvent e) {

            }
            public void mouseEntered(MouseEvent e) {

            }
            public void mouseExited(MouseEvent e) {

            }
            public void mouseClicked(MouseEvent e) {

            }
        });

        this.device = device;
    }

    private JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar(); // The underlying bar to hold the menus
        JMenu fileMenu = new JMenu("File"); // The first menu item "File"
        fileMenu.setMnemonic(KeyEvent.VK_F); // Set the keyboard key associated with choosing this option
        menuBar.add(fileMenu); // Add the menu to the menu bar

        /*
        JMenu openMenu = new JMenu("Sub-menu:");
        openMenu.setMnemonic(KeyEvent.VK_O);

        JMenuItem nonogramItem = new JMenuItem("Sub-menu item");
        nonogramItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));
        nonogramItem.addActionListener(e -> {
            game = 0;
            panel.repaint();
        });
        openMenu.add(nonogramItem);

        JMenuItem engineItem = new JMenuItem("Sub-menu item");
        engineItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
        engineItem.addActionListener(e -> {
            game = 1;
            panel.repaint();
        });
        openMenu.add(engineItem);

        fileMenu.add(openMenu);
        fileMenu.addSeparator();
         */

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        return menuBar;
    }

    private void begin() {
        setUndecorated(true);
        device.setFullScreenWindow(this);
        validate();
    }

    public static void main(String... args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getScreenDevices()[0];

        GameWindow gameWindow = new GameWindow(device);
        gameWindow.begin();
    }

}
