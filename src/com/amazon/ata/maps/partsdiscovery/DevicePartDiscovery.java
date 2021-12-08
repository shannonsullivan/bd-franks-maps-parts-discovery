package com.amazon.ata.maps.partsdiscovery;

import com.amazonaws.services.dynamodbv2.xspec.M;

import java.util.*;

/**
 * Helps expose key words from new editions of part catalogs.
 */
public class DevicePartDiscovery {

    // --- Part A ---
    /**
     * Calculate how often each word appears in a Catalog.
     * @param catalog The catalog to calculate word frequencies for.
     * @return A Map of words that appear in the catalog to the number of times they appear.
     */
    public Map<String, Integer> calculateWordCounts(PartCatalog catalog) {
        // PARTICIPANTS: Implement calculateWordCounts()
        // Instantiate the objects to be returned (returnedMap)
        Map<String, Integer> returnedMap = new HashMap<>();

        // Go through all the words in the PartCatalog catalogWordList
        for (String aWord : catalog.getCatalogWords()) {
            // check to see if the word is already in the returnMap
            if (returnedMap.containsKey(aWord)) { //  if it's in the returnMap, increment its count by 1
                int currentCount = returnedMap.get(aWord); //  get the current count for the word from the returnedMap
                currentCount++; //  increment the current count by 1
                returnedMap.put(aWord, currentCount); // put the current count for the word back in the returnedMap
            } else {  //  if it's not in the returnedMap,
                returnedMap.put(aWord, 1); // add the current word to the returnedMap with a count of 1
            }
        }
        return returnedMap; // Return the object expected
    }

    // --- Part B ---
    /**
     * Removes a word from the provided word count map.
     * @param word the word to be removed
     * @param wordCounts the map to remove the word from
     */
    public void removeWord(String word, Map<String, Integer> wordCounts) {
        // PARTICIPANTS: Implement removeWord()
        wordCounts.remove(word); // Remove the word provided from the Map provided
        return; // Return is optional since the return type is void
    }

    // --- Part C ---
    /**
     * Find the word that appears most frequently based on the word counts from a catalog.
     * @param wordCounts an association between a word and the total number of times it appeared in a catalog
     * @return The word that appears most frequently in the catalog to the number of times they appear.
     */
    public String getMostFrequentWord(Map<String, Integer> wordCounts) {
        // PARTICIPANTS: Implement getMostFrequentWord()
        // The word count is the value in an entry in the Map
        // Instantiate the return object
        String mostFrequentWord = "";

        int maxCount = 0; // Variable to hold the highest count as we iterate through the Map

        // Iterate through the Map remembering the key with the highest word count
        // Map.Entry represents an entry in a Map .entrySet() - return all entries in a Map
        for (Map.Entry<String, Integer> anEntry : wordCounts.entrySet()) {
            if (anEntry.getValue() > maxCount) { // If the value in current entry is greater than max count so far
                mostFrequentWord = anEntry.getKey(); // Remember the key as the current most frequent word
                maxCount = anEntry.getValue(); // Value is the max count so far
            }
        }

//        Set<String> theKeys = wordCounts.keySet(); // Store all Map keys in set
//        for (String aKey : theKeys) { // Iterate through the Map keys one at a time
//            if (wordCounts.get(aKey) > maxCount) { // If the value in current entry is greater than max count so far
//                mostFrequentWord = aKey; // Remember the key as the current most frequent word
//                maxCount = wordCounts.get(aKey); // Value is the max count so far
//            }
//        }
        return mostFrequentWord; // Return the most frequent word
    }

    // --- Part D ---
    /**
     * Calculates the TF-IDF score for each word in a catalog. The TF-IDF score for a word
     * is equal to the count * idf score. You can assume there will be an idfScore for each word
     * in wordCounts.
     * @param wordCounts - associates a count for each word from a catalog
     * @param idfScores - associates an IDF score for each word in the catalog
     * @return a map associating each word with its TF-IDF score.
     */
    public Map<String, Double> getTfIdfScores(Map<String, Integer> wordCounts, Map<String, Double> idfScores) {
        // PARTICIPANTS: Implement getTfIdfScores()
        // Instantiate the return object
        Map<String,Double> tfScore = new TreeMap<>(); // Store entries in word order

        // Iterate through the wordCounts Map
        // Calculate the IF-IDE for each entry and store in new map
        for (Map.Entry<String, Integer> anEntry : wordCounts.entrySet()) {
            tfScore.put(anEntry.getKey(), anEntry.getValue() * idfScores.get(anEntry.getKey()));
        }
        return tfScore; // Return the map with the IF-IDE scores
    }

    // --- Extension 1 ---
    /**
     * Gets the 10 highest (TF-IDF) scored words for a catalog.
     *
     * @param tfIdfScores - associates a TF-IDF score for each word in a catalog
     * @return a list of the 10 highest scored words for a catalog.
     */
    public List<String> getBestScoredWords(Map<String, Double> tfIdfScores) {
        // PARTICIPANTS: Implement getBestScoredWords()
        // Iterate through tfScore , keeping 10 highest in list
        // Use comparator to get highest collections.sort in list

        // Instantiate object that takes all tfScores from a Map
        List<Map.Entry<String, Double>> topTenList = new ArrayList<>(tfIdfScores.entrySet());

        // TopTenList is sorted by comparing the values in descending order o2 to o1
        Collections.sort(topTenList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        List<String> returnedList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            returnedList.add(topTenList.get(i).getKey());
        }

        return returnedList;
    }

    // --- Extension 2 ---
    /**
     * Calculates the IDF score for each word in a set of catalogs. The IDF score for a word
     * is equal to the inverse of the total number of times appears in all catalogs.
     * Assume df is the sum of the counts of a single word across all catalogs, then idf = 1.0/df.
     *
     * @param catalogWordCounts - a list of maps that associate a count for each word for each catalog
     * @return a map associating each word with its IDF score.
     */
    public Map<String, Double> calculateIdfScores(List<Map<String,Integer>> catalogWordCounts) {
        // PARTICIPANTS: Implement getIdfScores()
        Map<String, Double> returnedMap = new TreeMap<>();

        for (Map<String, Integer> listEntry : catalogWordCounts) {
            for (String word : listEntry.keySet()) {
                if (returnedMap.containsKey(word)) {
                    returnedMap.put(word, listEntry.get(word) + returnedMap.get(word));
                } else {
                    returnedMap.put(word, Double.valueOf(listEntry.get(word)));
                }
            }
        }

        for (Map.Entry<String, Double> anEntry : returnedMap.entrySet()) {
            returnedMap.put(anEntry.getKey(), 1.0/anEntry.getValue());
        }
        return returnedMap;
    }

}
