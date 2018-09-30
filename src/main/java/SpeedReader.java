import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.File;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedReader extends JPanel implements ActionListener {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);
    private final Timer timer;
    private BookReader bookReader;
    private boolean paused;
    private int delay = 1000;
    private int pause = 1000;
    private int currChapter = 0;
    private JFrame frame;
    private Chapter chapter;

    public Timer getTimer() {
        return timer;
    }

    public int getDelay() {
        return delay;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        if(this.chapter!=null) this.chapter.finalize();
        currChapter = 0;
        this.chapter = chapter;
        this.chapter.register(this);
    }

    private String chooseBook() {
        JFileChooser jfc = new JFileChooser(new File(FileSystemView.getFileSystemView().getHomeDirectory(), "Downloads"));
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

    public void openBook(String bookFileName) {
        bookFileName = (bookFileName == null) ? chooseBook() : bookFileName;
        bookReader = new BookReader(bookFileName);
        setChapter(bookReader.getChapter(currChapter));
        frame.setTitle("SpeedReader: " + bookReader.getTitle() + " - chapter " + currChapter);
    }

    public SpeedReader(String bookFileName, int speed) {
        this.delay = 60000 / speed;
        frame = new Window("SpeedReader");
        frame.addKeyListener(new KeyControls(this));
        frame.getContentPane().add(this, TOP_ALIGNMENT);
        openBook(bookFileName);
        timer = new Timer(delay, this);
        timer.setInitialDelay(pause);
        frame.setVisible(true);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        Line2D lin = new Line2D.Float(
                this.getWidth() >> 1,
                chapter.getTimeLabel().getY() - 10,
                this.getWidth() >> 1,
                chapter.getTimeLabel().getY()

                );
        Line2D lin2 = new Line2D.Float(
                this.getWidth() >> 1,
                chapter.getTimeLabel().getHeight(),
                this.getWidth() >> 1,
                chapter.getTimeLabel().getHeight() + 10
        );
        g2.draw(lin);
        g2.draw(lin2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!isPaused()) {
            chapter.next(this);
        }
    }

    public void nextChapter(int i) {
        currChapter = currChapter + i;
        setChapter(bookReader.getChapter(currChapter));
        logger.info("Current chapter: " + currChapter);
        frame.setTitle(bookReader.getTitle() + " - " + chapter.getTitle());
        chapter.restart();
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
            logger.error(e.getMessage());
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
