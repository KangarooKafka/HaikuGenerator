/*
Date: 9/27/22
Author: Kevin Darke
Contact: kevindarke@gmail.com
Word objects are used as part of the HaikuWriter package. Each Word object represents one
individual, unique word. It contains a list of branch words, which are words that can
follow this word. Branch words are stored and saved as ints, which represent the location
of the branch word in the WordManager wordList.
 */

package HaikuWriter;

import java.util.ArrayList;
import java.util.Collections;

public class WordObj {
    private final String literalWord; //Literal word the Word object represents
    private final ArrayList<String> branchWords; //List of all words that can follow this word
    private final int syllables;
    public boolean hasNoBranches = true;
    private int currentBranchIndex = 0;

    //Constructor for Word
    protected WordObj(String literalWord){
        this.literalWord = literalWord;
        this.syllables = SyllableCounter.countSyllables(literalWord);
        branchWords = new ArrayList<>();
    }

    //Method to return literal word
    protected String getWord(){
        return literalWord;
    }

    //Method to return the next branch
    protected String getBranch(){
        // Return -1 if there are no branches
        if(hasNoBranches){
            return null;
        }

        // Reset branch index if it has gone too far
        if(currentBranchIndex >= branchWords.size()){
            currentBranchIndex = 0;
            shuffleBranches();
        }

        // Return the next branch and increase index
        return branchWords.get(currentBranchIndex++);
    }

    //Method to return the number of branches
    protected int getBranchSize(){
        return branchWords.size();
    }

    //Method to shuffle the branch positions
    protected void shuffleBranches(){
        Collections.shuffle(branchWords);
        currentBranchIndex = 0;
    }

    // Method to add a new branch
    protected void addBranch(String branch){
        if (!branchWords.contains(branch)){
            branchWords.add(branch);
            hasNoBranches = false;
        }
    }

    //Method to get syllables
    protected int getSyllables(){
        return syllables;
    }

    // Override of toString
    public String toString(){
        StringBuilder str = new StringBuilder(literalWord);
        str.append(", Syllables: ").append(syllables);
        str.append(", Branches: ").append(branchWords.size());
        for (String branch : branchWords){
            str.append(",");
            str.append(branch);
        }
        str.append(";");
        return str.toString();
    }
}
