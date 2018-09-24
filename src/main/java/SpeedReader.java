import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

import javafx.geometry.VerticalDirection;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedReader extends JPanel implements ActionListener {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);
    private final JProgressBar pbar;
    private final int fontSize = 30;

    private String[] words;
    private final Timer timer;
    private BookReader bookReader;
    private int currWord;
    private JLabel timeLabel;
    private boolean paused;
    private int delay = 1000;
    private int pause = 1000;
    private int currChapter = 0;
    private JFrame frame;
    private Container contentPane;

    public void rewind() {
        currWord = currWord - 2;
        if(currWord < 0) {
            currWord = 0;
        }
        timeLabel.setText(words[currWord]);
        pbar.setValue(currWord);
    }

    private String chooseBook() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a book to read");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("epub files", "epub");
        jfc.addChoosableFileFilter(fileNameExtensionFilter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getPath();
        }
        return null;
    }

    public void forward() {
        currWord++;
        if(currWord > words.length - 1) {
            currWord = words.length - 1;
        }
        timeLabel.setText(words[currWord]);
        pbar.setValue(currWord);
    }

    public void restart() {
        currWord = 0;
        timeLabel.setText(words[currWord]);
        pbar.setValue(0);
    }

    public void finish() {
        currWord = words.length - 1;
        timeLabel.setText(words[currWord]);
        pbar.setValue(currWord);
    }

    public void openBook(String bookFileName) {
        bookFileName = (bookFileName == null) ? chooseBook() : bookFileName;
        bookReader = new BookReader(bookFileName);
        currChapter = 0;
        String text = bookReader.getChapter(currChapter);
        this.words = text.split("\\s+");
        this.currWord = 0;
    }

    public SpeedReader(String bookFileName, int speed) {
        this.delay = 60000 / speed;
        openBook(bookFileName);
        createWindow(bookReader.getTitle());
        timeLabel = new JLabel(this.words[this.currWord]);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, fontSize));
        add(timeLabel);
        timer = new Timer(delay, this);
        timer.setInitialDelay(pause);
        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(words.length);
        pbar.setLocation(0, (int) (1.2 * fontSize));
        pbar.setSize(frame.getWidth(), 5);
        contentPane.add(pbar, BOTTOM_ALIGNMENT);
        frame.setVisible(true);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        Line2D lin = new Line2D.Float(this.getWidth() >> 1, 0, this.getWidth() >> 1, 10);
        Line2D lin2 = new Line2D.Float(this.getWidth() >> 1, timeLabel.getY() + timeLabel.getHeight() + 10, this.getWidth() >> 1, timeLabel.getY() + timeLabel.getHeight());
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

    private void createWindow(String bookTitle) {
        frame = new JFrame("SpeedReader: " + bookTitle + " - chapter " + currChapter);
        frame.setSize(fontSize * 10, (int) (fontSize * 4));
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyControls(this));
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(this, TOP_ALIGNMENT);
    }

    public void nextChapter(int i) {
        currChapter = currChapter + i;
        String text = bookReader.getChapter(currChapter);
        this.words = text.split("\\s+");
        pbar.setMaximum(words.length);
        this.restart();
        logger.info("Current chapter: " + currChapter);
        frame.setTitle("SpeedReader: " + bookReader.getTitle() + " - chapter " + currChapter);
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
        book.setRequired(false);
        options.addOption(book);

        Option speed = new Option("s", "speed", true, "words per minute");
        speed.setRequired(false);
        options.addOption(speed);

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

            new SpeedReader(
                    cmd.getOptionValue("book", null),
                    Integer.parseInt(cmd.getOptionValue("speed", "60"))
            );
    }

    public void adaptSpeed(int i) {
        timer.setDelay(timer.getDelay() + i);
        logger.info("New speed: " + 60000 / timer.getDelay() + " words per minute");
    }
}
