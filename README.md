# HaikuGenerator
Generates random haikus based on the works of Taylor Swift, Weird Al, and Edgar Allen Poe

What it does:
It generates random haikus based on the vocabulary and word usage of select works from Taylor Swift, Weird Al, Edgar Allen Poe, and Charles Dickens. 
The user can get haikus based on any single one of them alone, or combine them in any way they wish, to see how the different vocabularies and word 
usages mix.

How to use it:
There is a GUI class designed to run the application, allowing the user to check boxes for which artists they want haikus based on, then having the 
options to generate haikus or reset the boxes and try something else. However, the HaikuGenerator class can also be used on its own. If that is the case, 
class constructor needs to be called, along with importFile() with an argument of the text file to use as source material, then writeHaiku() to get a haiku.

If the user wants to supply their own source material, it should be formatted as follows: it should be a plain text file with no empty lines 
(although extra spaces are okay) and with all punctuation removed except apostrophes and dashes. It is suggested that at every sentence ending and comma, 
a new line is inserted.

Note:
In the capitalize() method in the HaikuGenerator class, four lines can be uncommented out if the user wishes to include swear words. By default, swear
words are replaced by other words.


Creator: Kevin Darke Git @ KangarooKafka Kevindarke@gmail.com
