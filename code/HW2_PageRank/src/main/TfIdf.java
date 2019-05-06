package main;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains the TF-IDF functions to calculate the score by file/document.
*/

import utility.Utilities;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TfIdf {

	// Class constants
	private static final Pattern PUNCT_SYMBOLS = Pattern.compile("\\p{Punct}");
	private static final Pattern DIGIT_SYMBOLS = Pattern.compile("\\p{Digit}");
	private static final boolean TO_LOWER_CASE = true;
	
	// Class objects
	private PageRank pRank = new PageRank();
	
	/*
	 * Create the TF-IDF score by document <Word, Frequency>.
	 */
	public Hashtable<String, Hashtable<String, Double>> getTfIdf(Hashtable<String, String> fileList) {
		Hashtable<String, Hashtable<String, Double>> tfIdfList = new Hashtable<String, Hashtable<String, Double>>();
		
		// Get the token of the collection (file by file)
		Hashtable<String, ArrayList<String>> collection = getCollection(fileList, true);
		
		// Create the inverse index TF-IDF
		Hashtable<String, Hashtable<String, Integer>> invIndex = createInverseIndex(collection);
		
		// Get word document frequency in the collection (for IDF)
		Hashtable<String, Integer> wordDoctFreq = getWordDoctFrequency(invIndex);
		
		// Encode each document using the sparse TF-IDF representation
		Hashtable<String, ArrayList<WordFreq>> sparseIndex = createSparseTFIDF(invIndex);
		
		// Get the maximum frequency of any word by document
		Hashtable<String, Integer> maxWordFreq = getMaxFreqByDoct(sparseIndex);
		
		// TF_IDF objects/collections
		ArrayList<String> files = Utilities.getSortedKeys(sparseIndex.keys());
		ArrayList<String> vocabulary = Utilities.getSortedKeys(wordDoctFreq.keys());
		ArrayList<WordFreq> doctFreq;
		Hashtable<String, Double> frequency;
		WordFreq wf;
		String word;
		
		// TI-IDF variables
		double tfIdf;
		double tf;
		double idf;
		double df;
		double n = (double) files.size();
		
		// File loop
		for (String fName : files) {
			doctFreq = sparseIndex.get(fName);
			frequency = new Hashtable<String, Double>();

			// Words loop
			for (int i=0; i < doctFreq.size(); i++) {
				
				// Calculations
				wf = doctFreq.get(i);
				word = vocabulary.get(wf.wordIx);
				tf = wf.freq / maxWordFreq.get(fName);
				df = wordDoctFreq.get(word);
				idf = Utilities.log((n / df), 2);
				tfIdf = tf * idf;
				
				// Save the TF-IDF by word
				frequency.put(word, tfIdf);
				// System.out.println(">> fName: " + fName + ", word: " + word + ", tfIdf: " + tfIdf + ", tf: " + tf + ", idf: " + idf + ", n: " + n + ", df: " + df);
			}
			
			// Save the frequency of words of current document
			tfIdfList.put(fName, frequency);
		}
		
		// Return td-idf scores
		return tfIdfList;
	}
	
	/*
	 * Function that returns a collection of documents from a file list.
	 */
	public Hashtable<String, ArrayList<String>> getCollection(Hashtable<String, String> fileList, boolean advanced) {
		Hashtable<String, ArrayList<String>> collection = new Hashtable<String, ArrayList<String>>();
		ArrayList<String> tokens;
		
		// Temporal variables
		ArrayList<String> files = Utilities.getSortedKeys(fileList.keys());
		String content;
		ArrayList<String> tokenList;
		
		// Creating the graph list
		for(String filename : files) {
			content = fileList.get(filename);
			content = content.toLowerCase();
			tokens = new ArrayList<String>();
			
			// Split content by tokens
			tokenList = Utilities.splitTextByTokens(content, false);
			
			// Creating the current graph
			for(String word : tokenList) {
				word = pRank.getValidToken(word, true);
				
				if (word != "")
					tokens.add(word);
			}
				
			// Saving the current graph
			collection.put(filename, tokens);
			// System.out.println(">> Filename: " + filename + ", tokens: " + tokens.size());
		}

		// Return the graph list
		return collection;
	}
	
	/*
	 * Function that create a inverted index from a collection of documents.
	 */
	public Hashtable<String, Hashtable<String, Integer>> createInverseIndex(Hashtable<String, ArrayList<String>> collection) {
		Hashtable<String, Hashtable<String, Integer>> invIndex = new Hashtable<String, Hashtable<String, Integer>>();
		
		// Temporal variables
		Enumeration<String> e = collection.keys();
		ArrayList<String> doct;
		Hashtable<String, Integer> occurrence;
		String filename = "";
		String currWord = "";
		
		// Create Inverse Index
		while(e.hasMoreElements()) {
			filename = e.nextElement();			
			doct = collection.get(filename);
			
			for(int ix = 0; ix < doct.size(); ix++) {
				currWord = doct.get(ix);
				occurrence = null;
				
				if (!invIndex.containsKey(currWord)) {
					occurrence = new Hashtable<String, Integer>();
					occurrence.put(filename, 1);
				}
				else {
					occurrence = invIndex.get(currWord);
					
					if (!occurrence.containsKey(filename)) {
						occurrence.put(filename, 1);
					}
					else {
						occurrence.put(filename, (1 + occurrence.get(filename)));
					}
				}
				
				// Save word
				if (occurrence != null) {
					invIndex.put(currWord, occurrence);
				}
				
			}
		}
		
		return invIndex;
	}
	
	/*
	 * Function that create the sparse TF-IDF representation of collection of documents. 
	 */
	public Hashtable<String, ArrayList<WordFreq>> createSparseTFIDF(Hashtable<String, Hashtable<String, Integer>> invIndex) {
		Hashtable<String, ArrayList<WordFreq>> sparseIndex = new Hashtable<String, ArrayList<WordFreq>>(); 
		
		// Get sorted word list
		List<String> wordList = Utilities.getSortedKeysFromHT(invIndex);
		
		// Temporal variables
		ArrayList<WordFreq> doct;
		Hashtable<String, Integer> occurrence;
		Enumeration<String> e;
		String wordName;
		String filename;
		int wordFreq;
		
		for (int wordIx=0; wordIx < wordList.size(); wordIx++) {
			wordName = wordList.get(wordIx);
			occurrence = invIndex.get(wordName);
			
			// Create document encoding
			e = occurrence.keys();
			while(e.hasMoreElements()) {
				filename = e.nextElement();
				wordFreq = occurrence.get(filename);
				
				if (!sparseIndex.containsKey(filename)) {
					doct = new ArrayList<WordFreq>();
				}
				else {
					doct = sparseIndex.get(filename);
				}

				// Save the word-frequency pairs 
				doct.add(new WordFreq(wordIx, wordFreq));
				
				// Update document
				sparseIndex.put(filename, doct);
			}
		}
		
		// Return the sparse TF-IDF representation
		return sparseIndex;
	}
	
	/*
	 * Calculates and returns the frequency of a word in the documents of a collection.
	 */
	public Hashtable<String, Integer> getWordDoctFrequency(Hashtable<String, Hashtable<String, Integer>> invIndex) {
		Hashtable<String, Integer> wordDoctFreq = new Hashtable<String, Integer>();
		
		// Temporal variables
		Enumeration<String> e = invIndex.keys();
		String currWord = "";
		Hashtable<String, Integer> occurrence;
		int wordFreq = 0;
		
		while(e.hasMoreElements()) {
			currWord = e.nextElement();			
			occurrence = invIndex.get(currWord);
			wordFreq = occurrence.size();
			wordDoctFreq.put(currWord, wordFreq);
		}
		
        // Return the words total frequency on collection
		return wordDoctFreq;
	}
	
	/*
	 * Returns the maximum frequency of any word by document.
	 */
	public Hashtable<String, Integer> getMaxFreqByDoct(Hashtable<String, ArrayList<WordFreq>> sparseIndex) {
		Hashtable<String, Integer> maxWordFreq = new Hashtable<String, Integer>();
		
		// Temporal variables
		Enumeration<String> e = sparseIndex.keys();
		ArrayList<WordFreq> wordsFreq;
		String filename = "";
		int maxFreq;
		
		// Finding the most frequent word by document
		while(e.hasMoreElements()) {
			filename = e.nextElement();			
			wordsFreq = sparseIndex.get(filename);
			
			maxFreq = 0;
			for (int i=0; i < wordsFreq.size(); i++) {
				if (wordsFreq.get(i).freq > maxFreq)
					maxFreq = (int) wordsFreq.get(i).freq;
			}
			
			// Save the frequency of the most frequent word
			maxWordFreq.put(filename, maxFreq);
		}
		
        // Return the words total frequency on collection
		return maxWordFreq;
	}
	
	/*
	 * Clean raw text. Remove punctuation. Data Quality function.
	 *  Step 1: Transform to lower case
	 *  Step 2: Remove punctuation symbols
	 *  Step 3: Remove digits
	 */
	public String cleanText(String rawText) {
		String newText = (TO_LOWER_CASE ? rawText.toLowerCase() : rawText);
		Matcher unwantedMatcher;
		
		// Remove punctuation symbols
		unwantedMatcher = PUNCT_SYMBOLS.matcher(newText);
		newText = unwantedMatcher.replaceAll("");

		// Remove numbers
		unwantedMatcher = DIGIT_SYMBOLS.matcher(newText);
		newText = unwantedMatcher.replaceAll("");
		
		// Return cleaned text
		return newText;
	}
	
}