import nl.siegmann.epublib.Constants;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class BookReader {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);

    Book readBook;
    String title;

    public BookReader(String bookFileName) {
        try {
            readBook = new EpubReader().readEpub(new FileInputStream(bookFileName), Constants.CHARACTER_ENCODING);
            title = readBook.getTitle();
            logger.info("Book title: " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chapter getChapter(int chapter) {
        TableOfContents contents = readBook.getTableOfContents();
        try {
            Resource resource = contents.getAllUniqueResources().get(chapter);
            String text = IOUtils.toString(resource.getReader());
            logger.info("Chapter title: " + resource.getTitle());
            return new Chapter(text, resource.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

}
