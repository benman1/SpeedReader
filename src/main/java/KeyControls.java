import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyControls implements KeyListener {
    private static Logger logger = LoggerFactory.getLogger(TextReader.class);

    private final TextReader timerTime;

    public KeyControls(TextReader timerTime) {
        this.timerTime = timerTime;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            timerTime.rewind();
        }

        if (key == KeyEvent.VK_RIGHT) {
            timerTime.forward();
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_BEGIN) {
            timerTime.restart();
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_END) {
            timerTime.finish();
        }

        if (key == KeyEvent.VK_SPACE) {
            timerTime.setPaused(!timerTime.isPaused());
        }

        if (
                key == KeyEvent.VK_PLUS
                        || key == KeyEvent.VK_BRACERIGHT
                        || key == KeyEvent.VK_GREATER
                        || key == KeyEvent.VK_RIGHT_PARENTHESIS
                        || key == KeyEvent.VK_PERIOD
        ) {
            logger.info("Faster requested");
            timerTime.adaptSpeed(-10);
        }

        if (
                key == KeyEvent.VK_MINUS
                    || key == KeyEvent.VK_BRACELEFT
                    || key == KeyEvent.VK_LESS
                    || key == KeyEvent.VK_LEFT_PARENTHESIS
                    || key == KeyEvent.VK_COMMA
        ) {
            logger.info("Slower requested");
            timerTime.adaptSpeed(+10);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
