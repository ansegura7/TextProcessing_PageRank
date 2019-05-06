package main;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains the PageRank functions to create the Graphs list, calculate the PageRank score and calculate the MRR value.
*/

import utility.Utilities;
import utility.Porter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class PageRank {
	
	// N-Gram public constants
	public final static String NG_UNIGRAMS = "UNIGRAMS";
	public final static String NG_NGRAMS = "NGRAMS";
	
	// Type of comparison constants
	public static final String CMP_DIRECT = "DIRECT";
	public static final String CMP_BY_TOKENS = "BY_TOKENS";
	
	// PageRank algorithm constants
	public static final int PR_MAX_ITER = 100;
	public static final double PR_ALFA = 0.85;
	public static final double PR_CONVERG_GAP = 0.00000001;
	public static final boolean PR_WITH_NORM = true;
	
	// Class constants
	public static final String JOIN_OPER = "|";
	public static final String WS_OPER = " ";
	public static final int TOP_WORDS = 10;
	public static final int N_GRAM_SIZE = 3;
	
	// Class objects
	private Porter stemmer = new Porter();
	private ArrayList<String> stopWords = Utilities.getStopWordList();
	private ArrayList<String> validPOTS = getValidPartsOfTheSpeech();
	
	/*
	 * Function that creates a Graph List from the abstract of the documents.
	 */
	public Hashtable<String, Hashtable<String, Integer>> createGraphList(Hashtable<String, String> fileList) {
		Hashtable<String, Hashtable<String, Integer>> graphList = new Hashtable<String, Hashtable<String, Integer>>();
		Hashtable<String, Integer> currGraph;
		
		// Temporal variables
		ArrayList<String> files = Utilities.getSortedKeys(fileList.keys());
		String content;
		ArrayList<String> tokenList;
		String prevToken;
		String currToken;
		String tokenKey;
		
		// Creating the graph list
		for(String filename : files) {
			content = fileList.get(filename);
			content = content.toLowerCase();
			currGraph = new Hashtable<String, Integer>();
			
			// Split content by tokens
			tokenList = Utilities.splitTextByTokens(content, false);
			prevToken = "";
			
			// Creating the current graph
			for(String word : tokenList) {
				word = getValidToken(word, true);
				
				if (word != "") {
					currToken = word;
					
					if (prevToken != "" && currToken != "") {
						tokenKey = prevToken + JOIN_OPER + currToken;
						
						// Updating the weight of edge
						if (currGraph.containsKey(tokenKey))
							currGraph.put(tokenKey, (currGraph.get(tokenKey) + 1));
						else
							currGraph.put(tokenKey, 1);
					}
					prevToken = currToken;
				}
				else {
					prevToken = "";
				}
				
			}
				
			// Saving the current graph
			graphList.put(filename, currGraph);
			// Utilities.showHashTable(currGraph, "graph " + filename);
		}

		// Return the graph list
		return graphList;
	}
	
	/*
	 * Function that run the PageRank algorithm on each word graph corresponding to each document in the collection.
	 * If weighted parameter is true then the algorithm uses weights of edges.
	 */
	public Hashtable<String, Hashtable<String, Double>> getPageRankList(Hashtable<String, Hashtable<String, Integer>> graphList, boolean weighted) {
		Hashtable<String, Hashtable<String, Double>> pageRankList = new Hashtable<String, Hashtable<String, Double>>();
		
		// Temporal variables
		Enumeration<String> e = graphList.keys();
		String filename;
		Hashtable<String, Integer> currGraph;
		
		// Creating the graph list
		while(e.hasMoreElements()) {
			filename = e.nextElement();
			currGraph = graphList.get(filename);
			System.out.println(">> Filename: " + filename + " and edge number: " + currGraph.size());
			
			// Run PageRank algorithm with 100 iterations as maximum
			Hashtable<String, Double> pageRank = runPageRankToGraph(currGraph, weighted);
			
			// Save PageRank data
			pageRankList.put(filename, pageRank);
		}

		// Return the page rank list for each graph/file
		return pageRankList;
	}
	
	/*
	 * Run the PageRank algorithm to specific Word Graph.
	 * If weighted parameter is true then the algorithm uses weights of edges.
	 */
	public Hashtable<String, Double> runPageRankToGraph(Hashtable<String, Integer> graph, boolean weighted) {
		Hashtable<String, Double> pageRank = new Hashtable<String, Double>();
		
		// PageRank variables
		ArrayList<String> vocabulary = new ArrayList<String>();
		Hashtable<String, ArrayList<Integer>> edges = new Hashtable<String, ArrayList<Integer>>();
		Hashtable<String, Integer> outWeights = new Hashtable<String, Integer>();
		int nWords = 0;
		
		// Temporal variables
		Enumeration<String> e = graph.keys();
		ArrayList<String[]> pairs = new ArrayList<String[]>();
		String currWord;
		
		// Creating the vocabulary list
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			String[] tokens = key.split(Pattern.quote(JOIN_OPER));
			
			// Save the word-word pairs
			pairs.add(tokens);
			
			// Creating the vocabulary list
			for(int j=0; j < 2; j++)
				if(!vocabulary.contains(tokens[j]))
					vocabulary.add(tokens[j]);
		}
		
		// Sorting vocabulary
		Utilities.sortArrayList(vocabulary);
		nWords = vocabulary.size();
		
		// Creating the edges list by words
		for(int i=0; i < nWords; i++) {
			currWord = vocabulary.get(i);
			
			// Internal variables
			String nodeName;
			Integer currWeight;
			ArrayList<Integer> tempEdges = new ArrayList<Integer>();
			
			// Summarize out weight by word
			for(int j=0; j < pairs.size(); j++) {
				
				if (currWord.compareTo(pairs.get(j)[0]) == 0) {
					
					if (weighted) {
						nodeName = currWord + JOIN_OPER + pairs.get(j)[1];
						
						// Calculating and saving the neighboring nodes
						if (outWeights.containsKey(currWord))
							currWeight = (int) (outWeights.get(currWord) + graph.get(nodeName));
						else
							currWeight = (int) (graph.get(nodeName));
					}
					else {
						if (outWeights.containsKey(currWord))
							currWeight = (int) (outWeights.get(currWord) + 1);
						else
							currWeight = 1;
					}
					outWeights.put(currWord, currWeight);
				}
				
				// Create adjacency list by word
				if (currWord.compareTo(pairs.get(j)[1]) == 0) {
					nodeName = pairs.get(j)[0];
					
					if (vocabulary.contains(nodeName))
						tempEdges.add(vocabulary.indexOf(nodeName));
				}
			}
			
			// Save edges list by words and initial weights
			edges.put(currWord, tempEdges);
		}
		
		// Create Page Rank values (using weight parameter)
		pageRank = createPageRankValues(graph, vocabulary, edges, outWeights, weighted);
		
		// Return the page rank of word graph
		return pageRank;
	}
	
	/*
	 * Function that create the PageRank values from a graph.
	 */
	public Hashtable<String, Double> createPageRankValues(Hashtable<String, Integer> graph, ArrayList<String> vocabulary, Hashtable<String, ArrayList<Integer>> edges, Hashtable<String, Integer> outWeights, boolean weighted) {
		Hashtable<String, Double> fWeighs = new Hashtable<String, Double>();
		int nWords = vocabulary.size();
		double[][] weighs = new double[PR_MAX_ITER][nWords];
		double currConver = Integer.MAX_VALUE;
		double valValue = 1;
		double beta = 0.0;
		String currWord = "";
		String neighWord = "";
		ArrayList<Integer> nodeEdges;
		String edgeKey;
		Integer outWeight;
		Integer edgeWeight;
		int ix;
		int ixNeigh;
		System.out.println("   Graph vocabulary: " + vocabulary.size() + ", with follow edges list: " + edges.size());
		
		// Initialization of weight matrix
		for (int j=0; j < nWords; j++)
			weighs[0][j] = (1.0/nWords);
		
		// Create PageRank scores by words
		for (ix=1; ix < PR_MAX_ITER; ix++) {
			
			// Validation of convergence
			if (currConver > PR_CONVERG_GAP) {
				
				for (int j=0; j < nWords; j++) {
					currWord = vocabulary.get(j);
					nodeEdges = edges.get(currWord);
					beta = 0.0;
					
					// Summarize weights of neighboring nodes
					for (int k=0; k < nodeEdges.size(); k++) {
						ixNeigh = nodeEdges.get(k);
						neighWord = vocabulary.get(ixNeigh);
						outWeight = outWeights.get(neighWord);
						
						if (weighted) {
							edgeKey = neighWord + JOIN_OPER + currWord;
							edgeWeight = graph.get(edgeKey);	
							beta += edgeWeight * weighs[ix-1][ixNeigh] / outWeight;
						}
						else {
							beta += weighs[ix-1][ixNeigh] / outWeight;
						}
					}
					
					// PageRank equation
					weighs[ix][j] = (1 - PR_ALFA) * weighs[0][j] + PR_ALFA * beta;
				}
				
				// Update the convergence gap and the validation value
				currConver = 0;
				valValue = 0;
				for (int j=0; j < nWords; j++) {
					currConver += Math.abs(weighs[ix-1][j] - weighs[ix][j]);
					valValue += weighs[ix][j];
				}
			}
			else
				break;
		}
		
		// Saving final weights after convergence
		System.out.println("   Convergence after " + ix + " iterations. Gap between iterations: " + currConver);
		for (int j=0; j < vocabulary.size(); j++) {
			if (PR_WITH_NORM)
				weighs[ix - 1][j] = (weighs[ix - 1][j] / valValue);
			fWeighs.put(vocabulary.get(j), weighs[ix - 1][j]);
		}
		
		// Return the vocabulary weight using PageRank algorithm
		return fWeighs;
	}
	
	/*
	 * Function that forms n-grams of length up to 3 (unigrams, bigrams and trigrams) from words adjacent in text.
	 */
	public Hashtable<String, ArrayList<String>> createNgrams(Hashtable<String, String> fileList, Hashtable<String, Hashtable<String, Double>> pageRank, String ngramsType) {
		Hashtable<String, ArrayList<String>> nGrams = new Hashtable<String, ArrayList<String>>();
		ArrayList<String> fileNames = Utilities.getSortedKeys(pageRank.keys());
		
		// Simple approach of N-grams creation
		if (ngramsType.compareTo(NG_UNIGRAMS) == 0) {
			
			Hashtable<String, Double> wRank;
			ArrayList<String> sWords;
			
			// Get the best unigrams from abstract files
			for (String fName : fileNames) {
				wRank = pageRank.get(fName);
				
				// Get top N unigrams
				sWords = Utilities.getTopNtokens(wRank, TOP_WORDS, false);
				
				// Save the top N words
				nGrams.put(fName, sWords);
				// System.out.println(">> File name: " + fName + ", words size: " + wRank.size() + ", unigrams size: " + sWords.size());
			}
		}
		// Complex approach of N-grams creation (with Bigrams or Trigrams)
		else if (ngramsType.compareTo(NG_NGRAMS) == 0) {
			
			Hashtable<String, Double> wRank;
			Hashtable<String, Double> pRank;
			ArrayList<String> sWords;
			String content;
			ArrayList<String> tokenList;
			String nGramText;
			double nGramScore;
			int nGramCount;
			
			// Get the best N-grams from abstract files
			for (String fName : fileNames) {
				
				// Split content by tokens
				content = fileList.get(fName).toLowerCase();
				tokenList = Utilities.splitTextByTokens(content, false);
				pRank = pageRank.get(fName);
				
				// Initialization of temporal variables
				wRank = new Hashtable<String, Double>();
				nGramText = "";
				nGramScore = 0.0;
				nGramCount = 0;
				
				// Creating the current graph
				for (String word : tokenList) {
					word = getValidToken(word, true);
					
					// If the word is valid and has PageRank score...
					if (word != "" && pRank.containsKey(word)) {
						
						if (nGramCount < N_GRAM_SIZE) {
							
							// Creating the N-gram
							nGramText += word + WS_OPER;
							nGramScore += pRank.get(word);
							nGramCount++;
						}
						else {
							// Save the N-gram
							nGramText = nGramText.trim();
							if (!wRank.containsKey(nGramText)) {
								wRank.put(nGramText, nGramScore);
							}
							else {
								double tempScore = (double) wRank.get(nGramText); 
								if (tempScore < nGramScore) {
									wRank.put(nGramText, nGramScore);
								}
							}
							
							// Get the new N-gram
							nGramText = word + WS_OPER;
							nGramScore = pageRank.get(fName).get(word);
							nGramCount = 1;
						}
						
					}
					else {
						// If exists a N-gram valid
						nGramText = nGramText.trim();
						if (nGramText != "") {
							
							// Then save the N-gram 
							if (!wRank.containsKey(nGramText)) {
								wRank.put(nGramText, nGramScore);
							}
							else {
								double tempScore = (double) wRank.get(nGramText); 
								if (tempScore < nGramScore) {
									wRank.put(nGramText, nGramScore);
								}
							}
						}
						
						// Clear values
						nGramText = "";
						nGramScore = 0.0;
						nGramCount = 0;
					}
				}
				
				// Get top N N-grams
				sWords = Utilities.getTopNtokens(wRank, TOP_WORDS, false);
				
				// Save the top N words
				nGrams.put(fName, sWords);
				// System.out.println(">> File name: " + fName + ", words size: " + wRank.size() + ", N-grams size: " + sWords.size());
			}
		}
		
		// Returns the N-grams
		return nGrams;
	}
	
	/*
	 * Calculates the Rank(d) list for the entire collection for top-k ranked n-grams or phrases, where k ranges from 1 to 10.
	 */
	public Hashtable<String, ArrayList<Integer>> getRdScores(Hashtable<String, ArrayList<String>> nGrams, Hashtable<String, String> goldList, int kMax, String compType) {
		Hashtable<String, ArrayList<Integer>> rdList = new Hashtable<String, ArrayList<Integer>>();
		ArrayList<Integer> rdLocal;
		
		// Initial validation
		if (nGrams.size() != goldList.size() || nGrams.size() == 0) {
			System.out.println(">> ERROR: The size of the objects can not be zero or different between them. N-grams size: " + nGrams.size() + ", Gold list size: "+ goldList.size());
			return rdList;
		}
		
		// Temporal variables
		ArrayList<String> fileNames = Utilities.getSortedKeys(nGrams.keys());
		ArrayList<String> sWords;
		ArrayList<String> tokenList;
		String goldContent;
		int rankD;
			
		// Loop through the files
		for (String fName : fileNames) {
			sWords = nGrams.get(fName);
			goldContent = goldList.get(fName);
			tokenList = getStemmedGoldNgrams(goldContent);
			rankD = 0;
			rdLocal = new ArrayList<Integer>();
			
			// Loop through k
			for(int k = 1; k <= kMax; k++) {
				
				// Find into the gold-standard files
				for (int i=0; i < k && i < sWords.size(); i++) {
					
					// Comparison type...
					if (compType.compareTo(CMP_BY_TOKENS) == 0) {
					
						// By-Tokens or Complex comparison
						if (Utilities.arrayContainsWord(tokenList, sWords.get(i))) {
							rankD = (i + 1);
							break;
						}
					}
					else if (compType.compareTo(CMP_DIRECT) == 0) {
						// Direct or Complete comparison						
						if (tokenList.contains(sWords.get(i))) {
							rankD = (i + 1);
							break;
						}
							
					}
				}
				
				// Save the local rank(d) when k=i
				rdLocal.add(rankD);
			}
			
			// Save the local MRR score for i = k 
			rdList.put(fName, rdLocal);
		}
		
		// Return MRR score
		return rdList;
	}
	
	/*
	 * Return the list of stemmed gold N-grams.
	 */
	private ArrayList<String> getStemmedGoldNgrams(String goldContent) {
		ArrayList<String> nGramList = new ArrayList<String>(); 
		
		// Temporal variables
		String nGram;
		String[] nGrams = goldContent.split("\\r?\\n");
		String currToken;
		String[] tokens;
		
		// Main loop
		for (int i=0; i < nGrams.length; i++) {
			nGram = "";
			tokens = nGrams[i].split(" ");
			
			for (int j=0; j < tokens.length; j++) {
				currToken = stemmer.stripAffixes(tokens[j]);
				if (currToken != "") {
					nGram += currToken + " "; 
				}
			}
			
			// Save the stemmed n-gram
			if (nGram != "")
				nGramList.add(nGram.trim());
		}	
		
		// Return the gold nGrams list
		return nGramList;
	}

	/*
	 * Calculates the Mean Reciprocal Rank (MRR) for an entire collection.
	 */
	public ArrayList<Double> calculateMRR(Hashtable<String, ArrayList<Integer>> rdList) {
		ArrayList<Double> mrrList = new ArrayList<Double>();
		ArrayList<String> fileNames = Utilities.getSortedKeys(rdList.keys());
		
		if (fileNames.size() == 0) {
			System.out.println(">> ERROR: The size of the Rd list is zero.");
			return mrrList;
		}
		
		// Get file keys
		int n = fileNames.size();
		int k = rdList.get(fileNames.get(0)).size();
		ArrayList<Integer> mrrLocal;
		double mrrScore = 0.0;
		double cumMRR;
		double currRd;
		
		// Loop through k
		for(int i = 0; i < k; i++) {
			cumMRR = 0.0;
			
			for (String fName : fileNames) {
				mrrLocal = rdList.get(fName);
				currRd = mrrLocal.get(i);
				
				if (currRd > 0)
					cumMRR += (1.0 / currRd);
				
				// Update MRR score
				mrrScore = cumMRR / n;
			}
		
			// Save the MRR score for k
			mrrList.add(mrrScore);
		}
		
		// Return the mrr list of scores
		return mrrList;
	}
	
	/*
	 * Return the word only if it is a nouns or adjective.
	 */
	public String getValidToken(String currToken, boolean advanced) {
		
		for (String currPOTS : validPOTS) {
			if (currToken.endsWith(currPOTS)) {
				currToken = currToken.replace(currPOTS, "");
				
				// With double check of no stop-words
				if (!stopWords.contains(currToken)) {
					
					if (advanced) {
						currToken = stemmer.stripAffixes(currToken);
						if (!stopWords.contains(currToken) && currToken != "")
							return currToken;
					}
					return currToken;
				}
				return "";
			}
		}
		return "";
	}
	
	/*
	 * Returns the valid part of speech tags for this HW2. It is a private function.
	 */
	private ArrayList<String> getValidPartsOfTheSpeech() {
		ArrayList<String> list = new ArrayList<String>();
		
		// 7. JJ: Adjective
		list.add("_jj");
		
		// 12. NN:  Noun, singular or mass
		list.add("_nn");
		
		// 13. NNS: Noun, plural
		list.add("_nns");
		
		// 14. NNP: Proper noun, singular
		list.add("_nnp");
		
		// 15. NNPS: Proper noun, plural
		list.add("_nnps");
		
		// Returns the valid list of tags
		return list;
	}
	
}