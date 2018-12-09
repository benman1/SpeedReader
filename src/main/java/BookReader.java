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
import org.apache.commons.io.FilenameUtils;


public class BookReader {
    private static Logger logger = LoggerFactory.getLogger(SpeedReader.class);

    private String title;
    private List<Resource> chapters;

    public BookReader(String bookFileName) {
        try {
            if(bookFileName.endsWith("epub")) {
                Book readBook = new EpubReader().readEpub(
                        new FileInputStream(bookFileName),
                        Constants.CHARACTER_ENCODING
                );
                title = readBook.getTitle();
                List<Resource> contents = readBook.getContents();
                chapters = new ArrayList<>();
                for (Resource resource : contents) {
                    if (resource.getMediaType() != null && resource.getMediaType().equals(MediatypeService.XHTML)) {
                        chapters.add(resource);
                    }
                }
                logger.info("Book title: " + title);
                logger.info("Discovered " + chapters.size() + " chapters");
            }
            else {  // txt or html
                this.title = FilenameUtils.removeExtension(
                        FilenameUtils.getBaseName(bookFileName)
                );
                this.chapters = new ArrayList<>();
                Resource resource = new Resource(
                        new FileInputStream(bookFileName),
                        Constants.CHARACTER_ENCODING
                );
                resource.setTitle("Full");
                this.chapters.add(resource);
                logger.info("Book title: " + this.title);
                logger.info("Discovered " + this.chapters.size() + " chapters");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chapter getChapter(int chapterNum) {
        try {
            if(chapterNum >= chapters.size()) {
                chapterNum = chapters.size() - 1;
                logger.info("Reached the end of the book!");
            }
            else if(chapterNum < 0) {
                logger.info("Can't go back beyond first chapter!");
                chapterNum = 0;
            }
            Resource resource = chapters.get(chapterNum);
            String text = IOUtils.toString(resource.getReader());
            logger.info("Chapter title: " + resource.getTitle());
            String title = resource.getTitle()!= null ? resource.getTitle(): "chapter " + chapterNum;
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
