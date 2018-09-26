# SpeedReader

So far reads epub files and presents the text in a given chapter with a given speed. Keyboard commands are as follows:
* pause - space bar
* rewind - left arrow
* forward - right arrow
* faster - period (other keys like plus don't seem to work)
* slower - minus or comma
* next chapter - n
* previous chapter - p
* open a different book - b

Recognises sentence breaks in order to slow down for a moment.

Compile in maven. Give optional command line arguments for book and initial speed.

## TODO
* Make sure window is broad enough to accommodate all words.
* Font-size to be configurable.
* Skip table of content resources that are not XML/HTML.
* Read books in formats other than epub.

Pull requests appreciated.
