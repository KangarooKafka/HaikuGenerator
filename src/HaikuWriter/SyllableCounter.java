/*
Author: Kevin Darke
Date: 11/3/22
Contact: kevindarke@gmail.com
This class is designed to algorithmically determine how many syllables are in a given word, without using any outside
word lists or databases. The primary method, countSyllables, takes a single word and returns an int for the syllable
count. This is not perfect since rules for syllables do not apply evenly across all words. It is effective over 99% of
the time and primarily only fails with specific words or words containing specific character combinations (for
example, in words ending in 'ement', half the time the first 'e' is silent, half the time it is not).
It works essentially by adding syllables per vowel group, then adding or removing syllables if special circumstances
apply.
 */

package HaikuWriter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.StringTokenizer;

public class SyllableCounter {

    //Primary method that takes a word and returns syllable count
    protected static int countSyllables(String word){

        //If word is less than 2 characters, automatically return 1
        if (word.length() < 2){
            return 1;
        }

        int syllables = 0;
        word = word.toLowerCase(Locale.ROOT).trim();

        //Booleans to be checked later in edge cases
        boolean secondToLastIsVowel = isVowel(word.charAt(word.length()-2));
        boolean thirdToLastIsVowel = false;
        boolean fourthToLastIsVowel = false;

        //Check lengths before testing letters for booleans
        if (word.length() > 2){
            thirdToLastIsVowel = isVowel(word.charAt(word.length()-3));
            if (word.length() > 3){
                fourthToLastIsVowel = isVowel(word.charAt(word.length()-4));
            }
        }

        //Lists of specific endings that change the syllable count but require no further checks
        ArrayList<String> endingsToAdd = new ArrayList<>(
                List.of("ying","oing","n't","'ve","ier"));
        ArrayList<String> endingsToSubtract = new ArrayList<>(
                List.of("ement", "ewise", "ely", "e's", "gue"));

        //Sort out consonants and separate vowels into tokens of vowel groups
        StringTokenizer vowelTokens = new StringTokenizer(word,"qwrtpsdfghjklzxcvbnm ");

        //Get the syllable values of each vowel token
        while (vowelTokens.hasMoreTokens()){
            syllables += getClusterValue(vowelTokens.nextToken());
        }

        /*
        The below are checks for specific edge cases, which will add or remove a syllable before returning
         */

        //Minus one for silent e's hidden by suffixes that may have other suffixes after them
        if (word.contains("eness") || word.contains("eful") || word.contains("eless")){
            syllables--;
        }

        //Endings that add a syllable but require no further checks
        for (String ending : endingsToAdd){
            if(word.endsWith(ending)){
                return checkReturn(++syllables);
            }
        }

        //Endings that remove a syllable but require no further checks
        for (String ending : endingsToSubtract){
            if(word.endsWith(ending)){
                return checkReturn(--syllables);
            }
        }

        //Minus one for plural silent 'e's
        if(word.endsWith("es") && !thirdToLastIsVowel){
            //Exception if silent 'e' is proceeded with a consonant and 'l'
            if(word.endsWith("les") && !fourthToLastIsVowel){
                return checkReturn(syllables);
            }
            return checkReturn(--syllables);
        }

        //Minus one for singular silent 'e's
        else if(word.endsWith("e") && !secondToLastIsVowel){
            //Exception if silent 'e' is proceeded with a consonant and 'l'
            if(word.endsWith("le") && !thirdToLastIsVowel){
                return checkReturn(syllables);
            }
            return checkReturn(--syllables);
        }

        //Minus one for past tense silent 'e's (instances of 'ed' where the 'e' is silent)
        if(word.endsWith("ed") && !thirdToLastIsVowel){
            //Special exceptions where the e is not silent
            if(word.endsWith("kled") || word.endsWith("gled") || word.endsWith("bled")){
                return checkReturn(syllables);
            }
            return checkReturn(--syllables);
        }

        //Return syllables
        return checkReturn(syllables);
    }

    //Method checks if a letter is a vowel
    private static boolean isVowel(char c) {
        return "aeiouy".indexOf(c) != -1;
    }

    //Method returns how many syllables are in a cluster of vowels
    private static int getClusterValue(String cluster){

        //Specific, large vowel clusters that usually count as only one syllable
        ArrayList<String> threeVowelClusters = new ArrayList<>(List.of
                ("eau", "eye", "oye", "you", "yea", "uai", "ueue"));

        //If the cluster is over two and not in the exception list
        if (cluster.length() >= 3 && !threeVowelClusters.contains(cluster)){
            return 2;
        }
        //Return 1 in other cases
        return 1;
    }

    //Method to make sure that syllable count is at least 1
    private static int checkReturn(int value){
        return value > 0 ?  value : 1;
    }
}
