# SpeedReader

Reads epub files and presents the text in a given chapter at a given speed (words per minute) at a focal point.

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

## Features
Recognises sentence breaks in order to slow down for a moment. 

## Installation and running
Compile in maven. Give optional command line arguments for book and initial speed.

## TODO
* Help window with all keyboard shortcuts.
* Font-size to be configurable.
* Read books in formats other than epub.
* Simplify class inter-dependencies (or just make them static)

Pull requests appreciated.
