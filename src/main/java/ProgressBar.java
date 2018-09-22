import javax.swing.*;

public class ProgressBar extends JPanel {

    JProgressBar pbar;

    private static final int MY_MINIMUM = 0;

    private static final int MY_MAXIMUM = 100;

    public ProgressBar() {
        // initialize Progress Bar
        pbar = new JProgressBar();
        pbar.setMinimum(MY_MINIMUM);
        pbar.setMaximum(MY_MAXIMUM);
        // add to JPanel
        add(pbar);
    }

    public void updateBar(int newValue) {
        pbar.setValue(newValue);
    }

}
