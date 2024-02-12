package Nonograms.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 */
public class GameWindow extends JFrame {
    private final GraphicsDevice device;
    private final EditableNonogram editableNonogram = new EditableNonogram();;
    private Nonogram nonogram;
    private boolean editing = true;
    private JPanel panel;
    private boolean clicking;
    private int dragSymbol;

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
                int mX = e.getX();
                int mY = e.getY();
                if(editing) editableNonogram.mousePressed(e, frame);
                else {
                    nonogram.mousePressed(e);
                    // Set the drag symbol to the next symbol of whatever is immediately under the click
                    if(mX >= nonogram.x && mX <= nonogram.x + nonogram.width && mY >= nonogram.y && mY <= nonogram.y + nonogram.height) {
                        int boxRow = (mY - nonogram.y) / nonogram.BOXSIZE;
                        int boxCol = (mX - nonogram.x) / nonogram.BOXSIZE;
                        dragSymbol = nonogram.puzzle[boxRow][boxCol].symbol;
                    }
                }
                frame.repaint();
                clicking = true;
            }
            public void mouseReleased(MouseEvent e) {
                clicking = false;
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {
            }
        });

        getContentPane().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mX = e.getX();
                int mY = e.getY();
                if(!editing && mX >= nonogram.x && mX <= nonogram.x + nonogram.width && mY >= nonogram.y && mY <= nonogram.y + nonogram.height) {
                    nonogram.mouseMoved(e);
                    frame.repaint();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                // System.out.println("A new drag event");
                int mX = e.getX();
                int mY = e.getY();
                if(!editing && mX >= nonogram.x && mX <= nonogram.x + nonogram.width && mY >= nonogram.y && mY <= nonogram.y + nonogram.height) {
                    nonogram.mouseDragged(e, dragSymbol);
                    frame.repaint();
                }
            }
        });

        this.device = device;
    }

    private JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar(); // The underlying bar to hold the menus
        JMenu fileMenu = new JMenu("File"); // The first menu item "File"
        fileMenu.setMnemonic(KeyEvent.VK_F); // Set the keyboard key associated with choosing this option
        menuBar.add(fileMenu); // Add the menu to the menu bar

        JMenuItem start = new JMenuItem("Start Puzzle");
        start.setMnemonic(KeyEvent.VK_S);
        start.setAccelerator(KeyStroke.getKeyStroke('s'));
        start.addActionListener(e -> {
            if(editing) {
                editing = false;
                nonogram = new Nonogram(editableNonogram.getColumnClues(), editableNonogram.getRowClues());
                repaint();
            }
        });
        fileMenu.add(start);
        fileMenu.addSeparator();

        JMenuItem check = new JMenuItem("Check Full Solution");
        check.setMnemonic(KeyEvent.VK_C);
        check.setAccelerator(KeyStroke.getKeyStroke('c'));
        check.addActionListener(e -> { if(!editing) {
            nonogram.checkSolution();
        } });
        fileMenu.add(check);
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
