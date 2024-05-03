package Nonograms.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 *
 */
public class GameWindow extends JFrame {
    private final GraphicsDevice device;
    private final EditableNonogram editableNonogram = new EditableNonogram();
    private Nonogram nonogram;
    private boolean editing = true;
    private int dragSymbol;
    private final boolean TESTING = true;
    private static final boolean openInFullscreen = true;

    public GameWindow(GraphicsDevice device) {
        super("Nonogram Editor", device.getDefaultConfiguration());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(initMenu());

        if(TESTING) {
            int[][] columnClues = new int[][]{
                    {1},
                    {3},
                    {2},
                    {5},
                    {1}
            };
            int[][] rowClues = new int[][]{
                    {0, 2},
                    {0, 1},
                    {0, 3},
                    {0, 3},
                    {2, 1}
            };
            nonogram = new Nonogram(columnClues, rowClues);
            editing = false;
        }

        /*
            Draw everything on the GameWindow under the menu
         */
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                if (TESTING) {
                    nonogram.paint(g2);
                } else {
                    if (editing) editableNonogram.paint(g2);
                    else nonogram.paint(g2);
                }
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
            }
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
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

        JMenuItem create = new JMenuItem("Create Puzzle");
        create.setMnemonic(KeyEvent.VK_S);
        create.setAccelerator(KeyStroke.getKeyStroke('c'));
        create.addActionListener(e -> {
            editing = true;
            nonogram = new Nonogram(editableNonogram.getColumnClues(), editableNonogram.getRowClues());
            System.out.println("rowClues: " + Arrays.deepToString(editableNonogram.getRowClues()) + "\ncolumnClues: " + Arrays.deepToString(editableNonogram.getColumnClues()));
            repaint();
        });
        fileMenu.add(create);
        fileMenu.addSeparator();

        JMenuItem start = new JMenuItem("Start Puzzle");
        start.setMnemonic(KeyEvent.VK_S);
        start.setAccelerator(KeyStroke.getKeyStroke('s'));
        start.addActionListener(e -> {
            if(editing) {
                editing = false;
                nonogram = new Nonogram(editableNonogram.getColumnClues(), editableNonogram.getRowClues());
                System.out.println("rowClues: " + Arrays.deepToString(editableNonogram.getRowClues()) + "\ncolumnClues: " + Arrays.deepToString(editableNonogram.getColumnClues()));
                repaint();
            }
        });
        fileMenu.add(start);
        fileMenu.addSeparator();

        JMenuItem check = new JMenuItem("Check Full Solution");
        check.setMnemonic(KeyEvent.VK_C);
        check.setAccelerator(KeyStroke.getKeyStroke('c'));
        check.addActionListener(e -> { if(!editing) {
            System.out.println("The completed puzzle is valid: " + nonogram.checkSolution());
        } });
        fileMenu.add(check);
        fileMenu.addSeparator();

        JMenuItem attemptSolution = new JMenuItem("Attempt Full Solution");
        attemptSolution.setMnemonic(KeyEvent.VK_A);
        attemptSolution.setAccelerator(KeyStroke.getKeyStroke('a'));
        attemptSolution.addActionListener(e -> { if(!editing) {
            System.out.println("Attempting Full Solution...");
            boolean attemptSuccessful;
            try {
                attemptSuccessful = nonogram.attemptSolution();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("attemptSuccessful: " + attemptSuccessful);
        } });
        fileMenu.add(attemptSolution);
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_ESCAPE);
        exitItem.setMnemonic(KeyEvent.VK_E);
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


        if(openInFullscreen) gameWindow.begin();
        else {
            gameWindow.setSize(500, 500);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setLocationRelativeTo(null);
            gameWindow.setVisible(true);
        }
    }
}
