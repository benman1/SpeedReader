import nl.siegmann.epublib.Constants;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BookReader {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);

    Book readBook;
    String title;
    List<Resource> chapters;

    public BookReader(String bookFileName) {
        try {
            readBook = new EpubReader().readEpub(new FileInputStream(bookFileName), Constants.CHARACTER_ENCODING);
            title = readBook.getTitle();
            Resources contents = readBook.getResources();
            chapters = new ArrayList<>();
            for(Resource resource: contents.getAll()){
                if(resource.getMediaType().equals(MediatypeService.XHTML)) {
                    chapters.add(resource);
                }
            }
            logger.info("Book title: " + title);
            logger.info("Discovered " + chapters.size() + " chapters");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chapter getChapter(int chapter) {
        try {
            if(chapter >= chapters.size()) chapter = chapters.size() - 1;
            else if(chapter < 0) chapter = 0;
            Resource resource = chapters.get(chapter);
            String text = IOUtils.toString(resource.getReader());
            logger.info("Chapter title: " + resource.getTitle());
            String title = resource.getTitle()!= null ? resource.getTitle(): "chapter " + chapter;
            return new Chapter(text, title);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

}
