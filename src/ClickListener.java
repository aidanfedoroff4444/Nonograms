import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getX() + ", " + e.getY());
    }
}
