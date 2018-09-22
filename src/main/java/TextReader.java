import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextReader extends JPanel implements ActionListener {
    private static Logger logger = LoggerFactory.getLogger(TextReader.class);

    private final String[] words;
    private final Timer timer;
    private int currWord;
    private JLabel timeLabel;
    private boolean paused;
    private int delay = 1000;
    private int pause = 1000;

    public void rewind() {
        currWord = currWord - 2;
        if(currWord < 0) {
            currWord = 0;
        }
        timeLabel.setText(words[currWord]);
    }

    public void forward() {
        currWord++;
        if(currWord > words.length - 1) {
            currWord = words.length - 1;
        }
        timeLabel.setText(words[currWord]);
    }

    public void restart() {
        currWord = 0;
        timeLabel.setText(words[currWord]);
    }

    public void finish() {
        currWord = words.length - 1;
        timeLabel.setText(words[currWord]);
    }

    public TextReader(String text) {
        this.words = text.split("\\s+");
        createWindow();
        timeLabel = new JLabel(this.words[this.currWord]);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, 30));
        add(timeLabel);
        timer = new Timer(delay, this);
        timer.setInitialDelay(pause);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(this.getWidth() >> 1, 0, this.getWidth() >> 1, 10);
        Line2D lin2 = new Line2D.Float(this.getWidth() >> 1, this.getHeight() - 10, this.getWidth() >> 1, this.getHeight());
        g2.draw(lin);
        g2.draw(lin2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isPaused()) {
            forward();
            timeLabel.setText(words[currWord]);
        }
    }


    private void createWindow() {
        JFrame frame = new JFrame("TextReader");
        frame.setSize(350, 75);
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(new KeyControls(this));
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option book = new Option("b", "book", true, "epub book file");
        book.setRequired(true);
        options.addOption(book);

        Option speed = new Option("s", "speed", true, "words per minute");
        speed.setRequired(false);
        options.addOption(speed);

        final Option chapter = new Option("c", "chapter", true, "chapter");
        chapter.setRequired(true);
        options.addOption(chapter);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        BookReader bookReader = new BookReader(cmd.getOptionValue("book"));
        new TextReader(bookReader.getChapter(Integer.parseInt(cmd.getOptionValue("chapter"))));
    }

    public void adaptSpeed(int i) {
        timer.setDelay(timer.getDelay() + i);
        logger.info("New speed: " + timer.getDelay());
    }
}
