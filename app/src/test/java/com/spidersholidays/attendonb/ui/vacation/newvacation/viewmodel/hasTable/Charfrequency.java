package com.spidersholidays.attendonb.ui.vacation.newvacation.viewmodel.hasTable;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class Charfrequency {
    static final int SIZE = 1000;


    @Test
    public static void main(String args[]) {
        String sent = "aaLl9990077777777779AAAooo lk ko            ";
        String sent2 = "Faculty of Computers and Data Science";
        String sent3 = "ABC";
        runProgram(sent2);

    }


    // function to print the character and its
    // frequency in order of its occurrence

    @Test
    public static void runProgram(String str) {

        int[] mHashTable = implodeToHashTable(str);
        viewEachCharOccurrence(mHashTable); // just simple viewer


        /// create A tempCollection for
        HashMap<Character, Integer> charCollection = new HashMap<>();
        for (char i = 0; i < mHashTable.length - 1; i++) {
            if (mHashTable[i] != 0)
                charCollection.put(i, mHashTable[i]);
        }


        try {
            /// Please not we are using HasMap for searching and matching data
            /*
            As
               we can use our previous variable "mHashTable"
               but in this case we have to define A model object ex--> CustomNode Class
               and deal with it as ArrayList[CustomNode] ,
               but we have use HashMap in the seek  of simplicity
            * */

            int mostFrequentChar = gerMostFrequentChar(charCollection);

            /// you may have many different chars with the same frequent appearance
            HashMap<Character, Integer> matchedOccurrenceCollection = getMatchedOccurrenceCollection(charCollection.get((char) mostFrequentChar), charCollection);


            if (matchedOccurrenceCollection.size() > 1) {
                // pick char with the lowest ASC2
                validateCharSameValue(matchedOccurrenceCollection);
            } else {
                // if we don't have a duplicate with the same occurrence
                System.out.println("Most Frequent Char is -----> " + (char) mostFrequentChar + " with Apperance number : " + charCollection.get((char) mostFrequentChar));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * takes a string and convert it to an Array
     * in order to act as A HashTable
     * index       ---> Acts as "Char value"
     * index value ---> Acts as Char number of occurrence ......
     *
     * Please consider that we are storing our characters (A~z,1,..) with it's char value
     * instead of using  "hashing function" as an indicator for char index in the (HashTable or HashMap)
     * this will provide us with an accurate value without any collision ,
     * as our assignment only operate A single char so there is no need to set A hashing function and add more complexity
     */
    private static int[] implodeToHashTable(String str) {
        int[] mHashTable = new int[SIZE];

        for (int i = 0; i < str.length(); i++) {
            int vall = str.charAt(i);
            if (mHashTable[vall] == 0) {
                mHashTable[vall] = 1;

            } else {
                mHashTable[vall]++;
            }
        }
        return mHashTable;
    }

    /**
     * view each char Occurrence number Through Log
     */
    public static void viewEachCharOccurrence(int[] mHashTable) {
        for (int i = 0; i < mHashTable.length; i++) {
            if (mHashTable[i] != 0) {
                String val = String.valueOf(mHashTable[i]);
                System.out.print((char) i + "------>" + mHashTable[i] + "\n");
            }
        }
    }


    private static char gerMostFrequentChar(HashMap<Character, Integer> charCollection) throws Exception {
        int counter = 0;
        for (Map.Entry<Character, Integer> selectedEntry : charCollection.entrySet()) {
            for (Map.Entry<Character, Integer> loopEntry : charCollection.entrySet()) {
                counter += 1;
                if (selectedEntry.getValue() >= loopEntry.getValue()) {
                    if (counter == charCollection.size()) {
                        return selectedEntry.getKey();
                    }
                }
            }
            counter = 0;
        }
        throw new Exception("Couldn't detect Most occurrence char");
    }


    /**
     * return HashMap of different char with the same number of appearance
     * */
    private static HashMap<Character, Integer> getMatchedOccurrenceCollection(int mostFrequentOccurrence, HashMap<Character, Integer> charCollection) {
        int counter = 0;
        HashMap<Character, Integer> matchedCharCollection = new HashMap<>();

        for (Map.Entry<Character, Integer> loopEntry : charCollection.entrySet()) {
            if (mostFrequentOccurrence == (loopEntry.getValue())) {
                matchedCharCollection.put(loopEntry.getKey(), loopEntry.getValue());
            }
        }

        return matchedCharCollection;
    }





    private static char validateCharSameValue(HashMap<Character, Integer> matchedOccurrenceCollection) throws Exception {
        int counter = 0;
        for (Map.Entry<Character, Integer> selectedEntry : matchedOccurrenceCollection.entrySet()) {
            for (Map.Entry<Character, Integer> loopEntry : matchedOccurrenceCollection.entrySet()) {
                counter++;
                if ((int) selectedEntry.getKey() < (int) loopEntry.getKey()) {
                    if (counter == matchedOccurrenceCollection.size()-1) {
                        System.out.println("Most Frequent Char is -----> " + (char) selectedEntry.getKey() + " with Apperance number : " + selectedEntry.getValue());
                        return selectedEntry.getKey();
                    }

                }
            }
            counter = 0;
        }
        throw new Exception("Couldn't validate lowest ASC2 char");
    }

}




