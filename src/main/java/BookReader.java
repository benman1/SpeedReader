import nl.siegmann.epublib.Constants;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BookReader {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);

    Book readBook;
    String title;
    List<Resource> chapters;

    public BookReader(String bookFileName) {
        try {
            readBook = new EpubReader().readEpub(new FileInputStream(bookFileName), Constants.CHARACTER_ENCODING);
            title = readBook.getTitle();
            TableOfContents contents = readBook.getTableOfContents();
            chapters = new ArrayList<>();
            for(Resource resource: contents.getAllUniqueResources()){
                if(resource.getMediaType().equals(MediatypeService.XHTML)) {
                    chapters.add(resource);
                }
            }
            logger.info("Book title: " + title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chapter getChapter(int chapter) {
        try {
            if(chapter >= chapters.size()) chapter = chapters.size() - 1;
            Resource resource = chapters.get(chapter);
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
