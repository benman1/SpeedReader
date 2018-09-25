import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public static final int fontSize = 40;
    public static final int width = fontSize * 10;

    public Window(String title) throws HeadlessException {
        super(title);
        this.setSize(width, (int) (fontSize * 4));
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
    }
}
