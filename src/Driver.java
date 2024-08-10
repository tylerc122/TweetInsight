package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; //place with imports
import java.util.Collections; //place with imports

//Acquired from StackOverflow

public class Driver {
        public static void main(String[] args) {
                String filePath = "Covid-19 Twitter Dataset (Apr-Jun 2020).csv";
                ArrayList<Sentence> sentences = new ArrayList<>();
                try {
                        // Setting up being able to read the lines from the csv file we were given.
                        BufferedReader reader = new BufferedReader(new FileReader(filePath));

                        String line;
                        // Now we read each line from the csv file and convert it into a Sentence object
                        // using convertLine
                        while ((line = reader.readLine()) != null) {

                                Sentence sentence = Sentence.convertLine(line);
                                sentences.add(sentence);

                        }

                        reader.close();
                } catch (IOException error) {
                        error.printStackTrace();
                }

                // Defining the time range we want to stay between for our keep() method.
                String temporalRange = "March 20 2020-January 15 2021";

                // Filter the sentences to only include the sentences within the date range.
                ArrayList<Sentence> filteredSentences = new ArrayList<>();
                for (int i = 0; i < sentences.size(); i++) {
                        Sentence sentence = sentences.get(i);
                        if (sentence != null && sentence.keep(temporalRange)) {
                                filteredSentences.add(sentence);
                        }
                }

                // Print the top words from said filtered sentences.
                printTopWords(filteredSentences);

                // Then, for each sentence, we want to display its sentiment score.
                for (int i = 0; i < filteredSentences.size(); i++) {
                        Sentence sentence = filteredSentences.get(i);
                        if (sentence != null) {
                                System.out.println(
                                                sentence.getText() + " - Sentiment Score: " + sentence.getSentiment());
                        }
                }
        }

        /// printTopWords calcualutes the frequency of each word in the sentences and
        /// prints the top words found.
        /// @param sentences the sentences we want to get the top words from.
        /// @return the word with the amount of times it was found in the sentences.
        public static HashMap<String, Integer> printTopWords(ArrayList<Sentence> sentences) {
                HashMap<String, Integer> wordTotal = new HashMap<>();

                for (int i = 0; i < sentences.size(); i++) {
                        Sentence sentence = sentences.get(i);
                        if (sentence != null) {
                                ArrayList<String> words = sentence.splitSentence();

                                for (int j = 0; j < words.size(); j++) {
                                        String currentWord = words.get(j);

                                        if (!currentWord.isEmpty()) {
                                                if (!wordTotal.containsKey(currentWord)) {
                                                        wordTotal.put(currentWord, 1);
                                                } else {
                                                        wordTotal.put(currentWord, wordTotal.get(currentWord) + 1);
                                                }
                                        }
                                }
                        }
                }
                Map.Entry<String, Integer> maxEntry = null;
                {
                        for (Map.Entry<String, Integer> entry : wordTotal.entrySet())
                                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                                        maxEntry = entry;
                        // Preparing to sort word output.
                        int maxValueLen = maxEntry.getValue().toString().length();
                        ArrayList<String> results = new ArrayList<String>();
                        for (Map.Entry<String, Integer> set : wordTotal.entrySet()) {
                                String value = set.getValue().toString();
                                while (value.length() < maxValueLen)
                                        value = " " + value;
                                results.add(value + " of " + set.getKey());
                        }
                        // Sort and print the results.
                        Collections.sort(results);
                        Collections.reverse(results);
                        for (int i = 0; i < results.size() && i < 100; i++)
                                System.out.println(results.get(i));

                }
                return wordTotal;
        }

}
