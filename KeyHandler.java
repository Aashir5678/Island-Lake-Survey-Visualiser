import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyListener{
    boolean space_presssed = false;
    boolean m_pressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            space_presssed = true;
        }

        else {
            space_presssed = false;
        }

        if (key == KeyEvent.VK_M) {
            m_pressed = true;
        }

        else {
            m_pressed = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            space_presssed = false;
        }

        else if (key == KeyEvent.VK_M) {
            m_pressed = false;
        }
    }
}
