/*
Date: 11/3/22
Author: Kevin Darke
Contact: kevindarke@gmail.com
The WordManager is used as part of the HaikuWriter package. It creates and stores a HashMap of Word objects,
then uses the list to create a Haiku based on the text in the file given.
*/

package HaikuWriter;

import java.util.*;

public class HaikuGenerator {
    protected Map<String, WordObj> wordMap;
    ArrayList<String> keysArray;
    ArrayList<String> badEndings = new ArrayList<>(List.of("and", "i", "as","a", "my",
            "the", "it", "on", "just", "they", "your", "an", "by", "to", "with", "or",
            "these", "like", "you", "for", "but", "we", "at", "in", "of", "were", "was",
            "from", "she", "are", "he", "whose", "our", "has", "no", "that", "than", "is",
            "their"));
    ArrayList<String> badBeginnings = new ArrayList<>(List.of("and", "didn't", "as", "out", ""));

    //Constructor to initiate HashMap
    protected HaikuGenerator(){
        wordMap = new HashMap<>();
    }

    // Method to import a raw text file
    protected void importFile(String fileName){
        try {
            // Load file into scanner
            Scanner sc = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream(fileName)));

            // Go through each line in file
            while (sc.hasNextLine()){
                // Tokenize each word in the String
                String line = sc.nextLine();
                line = line.toLowerCase(Locale.ROOT);
                StringTokenizer wordTokens = new StringTokenizer(line," \n");

                // Get or create a Word object for the first word in the line
                WordObj leadingWordObj = getWordObject(wordTokens.nextToken());

                // Go through each word token in line
                while (wordTokens.hasMoreElements()){

                    // Get or create a Word object for the next word in the line
                    String trailingWord = wordTokens.nextToken();
                    WordObj trailingWordObj = getWordObject(trailingWord);

                    // Set trailing word as branch of leading word
                    leadingWordObj.addBranch(trailingWord);

                    //Set trailing branch as new leading branch
                    leadingWordObj = trailingWordObj;
                }
            }
        } catch (NullPointerException e){
            e.getMessage();// Exception for invalid file
        }
        // Load the key array
        keysArray = new ArrayList<>((wordMap.keySet()));
    }

    // Method to return a word if it already exists, or create and return a new one if not
    protected WordObj getWordObject(String str){
        if(wordMap.containsKey(str)){
            return wordMap.get(str);
        } else {
            WordObj newWordObj = new WordObj(str);
            wordMap.put(str, newWordObj);
            return newWordObj;
        }
    }

    // Method to returns a finished haiku
    protected String writeHaiku(){
        StringBuilder haiku = new StringBuilder();
        Random ran = new Random();// Random object to be passed around

        // Iterates through the lines of a haiku, passing 7 syllables for the middle line and 5 for the others
        for (int i = 1; i <= 3; i++){
            if(i == 2){
                haiku.append(writeLine(7, ran));
            } else {
                haiku.append(writeLine(5, ran));
            }
        }

        // Return finished haiku, turn last eclipses into period
        return haiku.substring(0,haiku.length()-3);
    }

    // Method to write a single Haiku line using the recursive method
    private String writeLine(int syllables, Random ran){
        Stack<String> wordStack = new Stack<>();// Stack for final word choices to be placed on
        StringBuilder line = new StringBuilder();
        boolean deadEnd = false;// Flags if the chosen word path has no possible branches to reach syllable target
        WordObj firstWord;// First word object

        /*
        Gets a starter word then tries recursively to build a line from it
        If recursiveWrite comes back as false, it could not complete a line with that starter word and tries a new one
        If it comes back true, a successful line was added to the wordStack
         */
        while (!deadEnd) {
            firstWord = getStarterWord(ran);
            deadEnd = recursiveWrite(firstWord, syllables, wordStack);
        }

        // Empty the wordStack into a String
        while (!wordStack.isEmpty()){
            line.append(" ").append(wordStack.pop());
        }
        line = new StringBuilder(line.toString().trim());

        // Format line and return it
        return line.substring(0,1).toUpperCase(Locale.ROOT) + line.substring(1) + "...\n";
    }

    // Recursive method for building Haiku lines by going down word branches
    private boolean recursiveWrite(WordObj currentWordObj, int syllablesLeft, Stack<String> wordStack){
        // Calculates new syllables and gets current literal word
        int syllablesToTarget = syllablesLeft - currentWordObj.getSyllables();
        String currentWordStr = currentWordObj.getWord();

        // If syllable target is hit
        if (syllablesToTarget == 0){

            // Return false if ending word is in the flagged list of bad line endings
            if(badEndings.contains(currentWordStr) || currentWordStr.contains("'")){
                return false;
            }

            // If not, push properly capitalized word onto wordStack and return true
            wordStack.push(capitalize(currentWordStr));
            return true;
        }

        // If there are still more syllables needed
        else if (syllablesToTarget > 0){
            WordObj nextWordObj;
            boolean hasReachedTarget;

            // For each of the current word's branches
            for (int i = 0; i < currentWordObj.getBranchSize(); i++){
                nextWordObj = wordMap.get(currentWordObj.getBranch());

                // If there is a valid branch
                if (nextWordObj != null) {
                    // Try another recursive call on the next branch
                    hasReachedTarget = recursiveWrite(nextWordObj, syllablesToTarget, wordStack);

                    //If target has been reached, push the current word to the stack and return true
                    if (hasReachedTarget){
                        wordStack.push(capitalize(currentWordStr));
                        return true;
                    }
                }
            }
            // If there are no valid branches, or they have all been checked, return false and back out
            return false;
        }
        // If the syllable target is negative, we've gone too far and need to return false and back out
        else{
            return false;
        }
    }

    // Method to return a valid, random word to start a line with
    private WordObj getStarterWord(Random ran){
        WordObj wordObj= wordMap.get(keysArray.get(ran.nextInt(keysArray.size())));

        // Get random words from the hashMap and check if they are good starting words and have branches
        while(badBeginnings.contains(wordObj.getWord()) || wordObj.hasNoBranches){
            wordObj = wordMap.get(keysArray.get(ran.nextInt(keysArray.size())));
        }
        return wordObj;
    }

    // Method to manually replace words that should have a capitalized first letter
    private String capitalize(String str){
        //There is also the option to censor out swear words by removing commented out lines below
        return switch (str) {
            case "i" -> "I";
            case "i'll" -> "I'll";
            case "i'm" -> "I'm";
            case "i've" -> "I've";
            case "i'd" -> "I'd";
            //case "cock" -> "chicken";
            //case "bitch" -> "bich";
            //case "shit" -> "poo";
            //case "fuck" -> "frick";
            default -> str;
        };
    }

    // Method to clear HashMap of all words
    protected void clearWords(){
        wordMap = new HashMap<>();
    }
}
