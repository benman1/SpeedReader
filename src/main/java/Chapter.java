import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.awt.Component.BOTTOM_ALIGNMENT;

public class Chapter {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);

    List<String> words = new ArrayList<>();
    List<Integer> sentenceBreaks = new ArrayList<>();
    String title;
    private int currWord;
    private JProgressBar pbar = initProgressBar();
    private JLabel timeLabel;
    private boolean justStopped = true;
    private SpeedReader speedReader;

    public void register(SpeedReader speedReader) {
        this.speedReader = speedReader;
        speedReader.getFrame().getContentPane().add(pbar, BOTTOM_ALIGNMENT);
        speedReader.add(timeLabel);
    }

    @Override
    public void finalize() {
        logger.info("Chapter instance is getting destroyed");
        speedReader.remove(timeLabel);
        speedReader.getFrame().getContentPane().remove(pbar);
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public JProgressBar getPbar() {
        return pbar;
    }

    private JProgressBar initProgressBar() {
        JProgressBar pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setLocation(0, (int) (1.2 * Window.fontSize));
        pbar.setSize(Window.width, 5);
        return pbar;
    }

    /**
     * @param text
     * @return
     *
     * Strips HTML and JSON from text
     */
    private String stripHTML(String text) {
        return text.replaceAll("<[^>]+>", " ").replaceAll("\\{[^\\}]+\\}", " ");
    }


    private String getCurrWord() {
        if(currWord < 0) currWord = 0;
        if(currWord < this.words.size()) {
            pbar.setValue(currWord);
            return this.words.get(currWord);
        }
        else return null;
    }

    public Chapter(String text, String title) {
        this.title = title;
        tokenizeText(text);
        this.currWord = 0;
        pbar.setMaximum(words.size()-1);
        pbar.setValue(0);
        timeLabel = new JLabel(getCurrWord());
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, Window.fontSize));
        timeLabel.setText(getCurrWord());
    }

    public void rewind() {
        currWord--;
        if(currWord < 0) {
            currWord = 0;
        }
        timeLabel.setText(getCurrWord());
        pbar.setValue(currWord);
    }

    public void next(SpeedReader speedReader) {
        if(sentenceBreaks.contains(currWord))
            if(!justStopped) {  // sentence breaks
                currWord--;
                this.justStopped = true;
            } else justStopped = false;
        forward();
    }

    public void forward() {
        currWord++;
        if(currWord >= words.size()) {
            currWord = words.size() - 1;
        }
        timeLabel.setText(getCurrWord());
    }

    public void restart() {
        currWord = 0;
        timeLabel.setText(getCurrWord());
    }

    public void finish() {
        currWord = words.size() - 1;
        timeLabel.setText(getCurrWord());
        pbar.setValue(currWord);
    }

    private void tokenizeText(String paragraph) {
        words.clear();
        sentenceBreaks.clear();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(stripHTML(paragraph));
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        int count = 0;
        for(CoreMap sentence:sentences){
            List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
            String originalString = edu.stanford.nlp.ling.SentenceUtils.listToOriginalTextString(labels);
            String[] tokens = originalString.split("\\s+");
            count += tokens.length;
            words.addAll(Arrays.asList(tokens));
            sentenceBreaks.add(count);
        }
    }
}

