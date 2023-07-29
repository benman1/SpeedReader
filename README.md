# SpeedReader

Reads epub files and presents the text in a given chapter at a given speed (words per minute) at a focal point.

![](resources/screenshot.png)

## Features
* Reads epub, txt and html formats
* displays words at variable frequencies
* functionality to pause, restart, skip, rewind and forward.
* recognises sentence breaks (in English texts) in order to slow down at each end of a sentence

## Installation and running
Compile in maven. Give optional command line arguments for book and initial speed.

You need to have OpenJDK and Maven installed.

You can compile and execute the app with maven like this:
```bash
mvn exec:java
```

This should bring up a file selector, where you can choose an EPUB file to load. Depending on the book structure, you might have to skip the cover and table of contents to get to the main content.

## Commands
Keyboard commands are as follows:
* pause - space bar
* rewind - left arrow
* forward - right arrow
* faster - period (other keys like plus don't seem to work)
* slower - minus or comma
* next chapter - n
* previous chapter - p
* open a different book - b
* speed-read the clipboard - c
* quit - q

## TODO
* Help window with all keyboard shortcuts.
* Font-size to be configurable (configuration file?).
* Read books in formats (e.g. tika or other ebook library?).
* Show time left or estimated time of completion for book/chapter?
* On new chapter, even if not playing, update to show the first word.
* Adjust display period by word length.

Pull requests appreciated.
