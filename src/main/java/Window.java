import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    static final int fontSize = 40;
    static final int width = fontSize * 15;
    private static final int height = (int) (fontSize * 2.5);

    public Window(String title) throws HeadlessException {
        super(title);
        this.setSize(width, height);
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
    }
}
