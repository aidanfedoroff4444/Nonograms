package Nonograms.src;

import Nonograms.src.EditableNonogram;
import Nonograms.src.Nonogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 *
 */
public class GameWindow extends JFrame {
    private final GraphicsDevice device;
    private EditableNonogram editableNonogram = new EditableNonogram();;
    private Nonogram nonogram;
    private boolean editing = true;

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
                if(editing) editableNonogram.paint(g2);
                else nonogram.paint(g2);
            }
        };
        add(panel);

        JFrame frame = this;

        getContentPane().addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(editing) editableNonogram.mousePressed(e, frame);
                else nonogram.mousePressed(e);
                frame.repaint();
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
        });

        this.device = device;
    }

    private JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar(); // The underlying bar to hold the menus
        JMenu fileMenu = new JMenu("File"); // The first menu item "File"
        fileMenu.setMnemonic(KeyEvent.VK_F); // Set the keyboard key associated with choosing this option
        menuBar.add(fileMenu); // Add the menu to the menu bar

        JMenuItem start = new JMenuItem("Start:");
        start.setMnemonic(KeyEvent.VK_S);
        start.setAccelerator(KeyStroke.getKeyStroke('s'));
        start.addActionListener(e -> {
            if(editing) {
                editing = false;
                nonogram = new Nonogram(editableNonogram.getColumnClues(), editableNonogram.getRowClues());
                // System.out.println("columnClues: " + Arrays.deepToString(editableNonogram.getColumnClues()));
                // System.out.println("rowCLues: " + Arrays.deepToString(editableNonogram.getRowClues()));
                repaint();
            }
        });
        fileMenu.add(start);
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_ESCAPE);
        exitItem.setAccelerator(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE));
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
