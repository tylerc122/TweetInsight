package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.Date;

public class Sentence {

	// Contains text of current tweet.
	private String text;
	// Contains author of tweet.
	private String author;
	// Contains timestamp of tweet creation
	private String timestamp;

	public Sentence(String text, String author, String timestamp) {

		this.text = text;
		this.author = author;
		this.timestamp = timestamp;

	}

	/// This method calculates the sentiment score of the tweet using the Standford
	/// NLP Libary.
	/// @return int the sentiment score of the tweet
	/// (Scale of 1-4, 1 for negative, 2 for netural, 3 for positive, 4 for very
	/// positive)
	public int getSentiment() {
		String tweet = this.text;
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(tweet);

		// Check if any tweets exist before we get the first one
		if (annotation.get(CoreAnnotations.SentencesAnnotation.class).isEmpty()) {
			System.out.println("No sentences found in given tweet: " + tweet);
			return -1;
		}
		CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
		Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
		return RNNCoreAnnotations.getPredictedClass(tree);
	}

	/// Method to check if a sentence's timestamp falls within a specific range
	/// @param temporalRange the range of dates we want to stay in between.
	/// @return boolean the truth value of whether or not the tweet falls within the
	/// expected date range.
	public boolean keep(String temporalRange) {
		try {
			String[] dates = temporalRange.split("-");
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

			Date startDate = formatter.parse(dates[0].trim());
			Date endDate = formatter.parse(dates[1].trim());
			Date tweetDate = formatter.parse(this.timestamp);
			return (tweetDate.equals(startDate) || tweetDate.after(startDate)) &&
					(tweetDate.equals(endDate) || tweetDate.before(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Overriding the default toString method for custom string representation.
	public String toString() {
		return "{author:" + author + ", sentence:\"" + text + "\", timestamp:\"" + timestamp + "\"}";
	}

	// Getters
	public String getText() {
		return text;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getAuthor() {
		return author;
	}

	// Setters
	public void setText(String text) {
		this.text = text;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/// Splits the sentence into words, however, we exclude certain words for
	/// simplicity and effeciency sake.
	/// Furthermore, they don't tell us anything about the sentiment.
	/// @return ArrayList<String> contains the words of the sentence that were not
	/// stop words
	public ArrayList<String> splitSentence() {

		ArrayList<String> words = new ArrayList<>();

		String[] pieces = text.split(" ");

		String[] stopwords = { "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any",
				"are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
				"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
				"don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
				"have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself",
				"him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
				"isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no",
				"nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours ourselves", "out",
				"over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some",
				"such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there",
				"there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to",
				"too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were",
				"weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's",
				"whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're",
				"you've", "your", "yours", "yourself", "yourselves" }; // from https://www.ranks.nl/stopwords
		for (int i = 0; i < pieces.length; i++) {
			String piece = pieces[i].toLowerCase();

			boolean isStopWord = false;
			for (int j = 0; j < stopwords.length; j++) {
				if (piece.equals(stopwords[j])) {
					isStopWord = true;
					break;
				}
			}
			if (!piece.isEmpty() && !isStopWord) {
				words.add(piece);
			}

		}
		return words;
	}

	/// convertLine parses a line in the csv file into a sentence object for further
	/// analaysis
	/// @param line the raw inputted line from the csv file.
	/// @return Sentence returns a sentence object constructed from our method.
	public static Sentence convertLine(String line) {

		ArrayList<String> pieces = new ArrayList<>();
		boolean quotes = false;
		String basket = "";

		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);

			if (currentChar == '\"') {
				quotes = !quotes;
			} else if (currentChar == ',' && !quotes) {
				pieces.add(basket.trim());
				basket = "";
			} else {
				basket += currentChar;
			}

		}
		pieces.add(basket.trim());
		// Error handling, each line is expected to have at least 8 pieces of
		// information seperated by commas
		// If a line has fewer than 8, it's considered malformed and we skip analyzing
		// that line.
		if (pieces.size() < 8) {
			System.out.println("Skipping malformed line: " + line);
			return null;
		}

		String dates = pieces.get(2);

		String[] format = dates.split(" ");
		String[] format1 = format.length > 0 ? format[0].split("/") : new String[] {};
		if (format1.length < 2) {
			System.out.println("Skipping malformed date: " + dates);
			return null;
		}

		String month = "";
		if (format1.length > 0) {
			switch (format1[0]) {
				case "1":
					month = "January";
					break;
				case "2":
					month = "February";
					break;
				case "3":
					month = "March";
					break;
				case "4":
					month = "April";
					break;
				case "5":
					month = "May";
					break;
				case "6":
					month = "June";
					break;
				case "7":
					month = "July";
					break;
				case "8":
					month = "August";
					break;
				case "9":
					month = "September";
					break;
				case "10":
					month = "October";
					break;
				case "11":
					month = "November";
					break;
				case "12":
					month = "December";
					break;
			}
		}
		// 2nd 4th and 7th indexes are date author tweet respectively.
		// Return new Sentence object using these pieces.
		if (pieces.size() >= 8 && format1.length > 0) {
			System.out.println(format1[1]);
			String date = month + " " + format1[1] + " 2020";
			String author = pieces.get(4);
			String text = pieces.get(7);
			return new Sentence(text, author, date);
		} else {
			return null;
		}

	}
}
