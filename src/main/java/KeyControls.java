import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyControls implements KeyListener {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);
    private int forwardedBefore = 0;
    private int rewindedBefore = 0;

    private final SpeedReader timerTime;

    public KeyControls(SpeedReader timerTime) {
        this.timerTime = timerTime;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            for(int i=0; i<(1 + rewindedBefore / 5); i++) timerTime.getChapter().rewind();
            rewindedBefore += 1;
        } else {
            rewindedBefore = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            for(int i=0; i<(1 + forwardedBefore / 5); i++) timerTime.getChapter().forward();
            forwardedBefore += 1;
        } else {
            forwardedBefore = 0;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_BEGIN) {
            timerTime.getChapter().restart();
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_END) {
            timerTime.getChapter().finish();
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
            timerTime.adaptSpeed(-10);
        }

        if (
                key == KeyEvent.VK_MINUS
                    || key == KeyEvent.VK_BRACELEFT
                    || key == KeyEvent.VK_LESS
                    || key == KeyEvent.VK_LEFT_PARENTHESIS
                    || key == KeyEvent.VK_COMMA
        ) {
            timerTime.adaptSpeed(+10);
        }

        if (key == KeyEvent.VK_P) {
            timerTime.nextChapter(-1);
        }

        if (key == KeyEvent.VK_N) {
            timerTime.nextChapter(1);
        }

        if (key == KeyEvent.VK_B) {
            timerTime.openBook(null);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public int getRewindedBefore() {
        return rewindedBefore;
    }

    public int getForwardedBefore() {
        return forwardedBefore;
    }
}
