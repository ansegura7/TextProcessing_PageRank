package main;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Main class of the program. Receive the directory path where the files (abstracts and gold-standard) are located.
 *           It should only be executed and the answers will be obtained by console and in the output files.
*/

import utility.Utilities;
import utility.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainClass {

	// Class objects
	private static Reader mReader = new Reader();
	private static PageRank mPRank = new PageRank();
	private static TfIdf tfidf = new TfIdf();
	
	// Main function of main class
	public static void main(String[] args) {
		
		// Document paths
		String abstPath = "";
		String goldPath = "";
		String outPath = "";
		String nGramsType = PageRank.NG_NGRAMS;
		String compType = PageRank.CMP_DIRECT;
		
		// Get program parameters
		if (args.length == 0 || !Utilities.folderExists(args[0])) {
			abstPath = "data/abstracts/";
			goldPath = "data/gold/";
			outPath = "data/output";
		}
		else {
			// Get path file
			abstPath = args[0] + "/abstracts/";
			goldPath = args[0] + "/gold/";
			outPath = args[0] + "/output";
			
			// Get N-grams type function
			if (args.length > 1) {
				nGramsType = args[1].toUpperCase();
				
				// Get Type of comparison
				if (args.length > 2) {
					compType = args[2].toUpperCase();
				}
			}
		}
		
		// Execute the home work 2
		System.out.println(">> Begin process");
		long startTime = System.nanoTime(); 
		runHomeWork2(abstPath, goldPath, outPath, nGramsType, compType);
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("\n>> End process - Elapsed Time: " + (elapsedTime / 1000000) + " ms");
	}
	
	/*
	 * Private function that run/execute the main task of Homework 2.
	 */
	private static void runHomeWork2(String abstPath, String goldPath, String outPath, String nGramsType, String compType) {
		
		// Main data structures
		Hashtable<String, String> fileList;
		Hashtable<String, String> goldList;
		Hashtable<String, Hashtable<String, Integer>> graphList;
		Hashtable<String, Hashtable<String, Double>> pageRankSimple;
		Hashtable<String, Hashtable<String, Double>> pageRankWeighted;
		Hashtable<String, Hashtable<String, Double>> tiIdfList;
		
		/*
		 * 0. Write author info
		 */
		System.out.println("0. Author Info:");
		System.out.println("   Name: Andres Segura Tinoco");
		System.out.println("   Student Code: 201711582");
		System.out.println("   HW 2: PageRank");
		System.out.println("   Approch: Directed Graph, creation of N-grams " + nGramsType.toLowerCase() + " and PageRank: " + compType.toLowerCase());
		
		/*
		 *  1. Write a program that loads each document into a word graph (either directed or undirected).
		 */
		System.out.println("\n1. Write a program that loads each document into a word graph (either directed or undirected):");
		goldList = mReader.readFiles(goldPath);
		fileList = mReader.readFiles(abstPath);
		fileList = Utilities.filterHashTableByKeys(fileList, Utilities.getSortedKeys(goldList.keys()));
		System.out.println(">> " + fileList.size() + " abstract documents were loaded correctly from: " + abstPath);
		System.out.println(">> " + goldList.size() + " gold documents were loaded correctly from: " + abstPath);
		
		// Creating a word graph list for all documents
		graphList = mPRank.createGraphList(fileList);
		System.out.println(">> " + graphList.size() + " Word Graphs of abstracts were created.");
		
		/*
		 * 2. Run PageRank on each word graph corresponding to each document in the collection.
		 */
		System.out.println("\n2. Run simple PageRank on each word graph corresponding to each document in the collection:");
		pageRankSimple = mPRank.getPageRankList(graphList, false);
		System.out.println(">> " + pageRankSimple.size() + " page rank list were created.");
		
		// Show PageRank data
		// Utilities.showHashTableNested(pageRankSimple, "PageRank data");
		
		/*
		 * 2'. Run PageRank on each word graph corresponding to each document in the collection.
		 *     Assume the weight wij of an edge (vi; vj) is calculated as the number of times the corresponding words wi and wj are adjacent in text.
		 */
		System.out.println("\n2'. Run weighted PageRank on each word graph corresponding to each document in the collection:");
		pageRankWeighted = mPRank.getPageRankList(graphList, true);
		System.out.println(">> " + pageRankWeighted.size() + " page rank list were created.");
		
		// Show PageRank data
		// Utilities.showHashTableNested(pageRankWeighted, "PageRank data");
		
		/*
		 * 3. Form n-grams of length up to 3 (unigrams, bigrams and trigrams) from words adjacent in text and 
		 *    score n-grams or phrases using the sum of scores of individual words that comprise the phrase.
		 */
		System.out.println("\n3. Form n-grams of length up to 3 (unigrams, bigrams and trigrams) from words adjacent in text:");
		System.out.println(">> Creating the N-grams from simple PageRank scores:");
		Hashtable<String, ArrayList<String>> nGramsSimple = mPRank.createNgrams(fileList, pageRankSimple, nGramsType);
		System.out.println(">> Creating the N-grams from weighted PageRank scores:");
		Hashtable<String, ArrayList<String>> nGramsWeighted = mPRank.createNgrams(fileList, pageRankWeighted, nGramsType);
	
		/*
		 * 4. Calculate the MRR for the entire collection for top-k ranked n-grams or phrases, where k ranges from 1 to 10
		 */
		int k = 10;
		System.out.println("\n4. Calculate the MRR for the entire collection for top-k ranked n-grams or phrases, where k ranges from 1 to " + k + ":");
		
		System.out.println(">> Comparing the N-grams against the gold-standard files from simple PageRank:");
		Hashtable<String, ArrayList<Integer>> mrrListSimple = mPRank.getRdScores(nGramsSimple, goldList, k, compType);
		ArrayList<Double> mrrSimple = mPRank.calculateMRR(mrrListSimple);
		
		System.out.println(">> Comparing the N-grams against the gold-standard files from weighted PageRank:");
		Hashtable<String, ArrayList<Integer>> mrrListWeighted = mPRank.getRdScores(nGramsWeighted, goldList, k, compType);
		ArrayList<Double> mrrWeighted = mPRank.calculateMRR(mrrListWeighted);
		
		/*
		 * 5. Compare the MRR of the above PageRank algorithm with the MRR of a ranking of words based on their TF-IDF ranking scheme.
		 *    Calculate the TF component from each document and the IDF component from the entire collection.
		 */
		System.out.println("\n5. Compare the MRR of the above PageRank algorithm with the MRR of a ranking of words based on their TF-IDF ranking scheme:");
		System.out.println(">> Creating TF-IDF scores:");
		tiIdfList = tfidf.getTfIdf(fileList);
		
		System.out.println(">> Creating the N-grams from TF-IDF scores:");
		Hashtable<String, ArrayList<String>> nGramsTFIDF = mPRank.createNgrams(fileList, tiIdfList, nGramsType);
		
		System.out.println(">> Comparing the N-grams against the gold-standard files from TF-IDF scores:");
		Hashtable<String, ArrayList<Integer>> mrrListTFIDF = mPRank.getRdScores(nGramsTFIDF, goldList, k, compType);
		ArrayList<Double> mrrTFIDF = mPRank.calculateMRR(mrrListTFIDF);
		
		/*
		 * 6. Show results by console
		 */
		System.out.println("\n6. Show the MRR Scores");
		showResultByConsole(k, mrrSimple, mrrWeighted, mrrTFIDF, nGramsType, compType);
		
		/*
		 * 7. Save the results.
		 */
		System.out.println("\n7. Save the results for " + graphList.size() + " files:");
		saveResults(outPath + "_smp/", graphList, pageRankSimple, nGramsSimple, mrrListSimple, mrrSimple);
		saveResults(outPath + "_wei/", graphList, pageRankWeighted, nGramsWeighted, mrrListWeighted, mrrWeighted);
		
	}
	
	/*
	 * Shows the results by console in formatted mode.
	 */
	private static void showResultByConsole(int k, ArrayList<Double> mrrSimple, ArrayList<Double> mrrWeighted, ArrayList<Double> mrrTFIDF, String nGramsType, String compType) {
		DecimalFormat df = new DecimalFormat("0.000");
		
		System.out.println("   Parameters >> N-grams Type: " + nGramsType.toLowerCase() + ", Type of comparison between N-grams: " + compType.toLowerCase());
		for (int i=0; i < k; i++) {
			if(i == 0)
				System.out.print("\t");
			System.out.print("\tk=" + (i + 1));
		}
		System.out.println();
		for (int i=0; i < k; i++) {
			if (i == 0)
				System.out.print("-Simple MRR:");
			System.out.print("\t" + df.format(mrrSimple.get(i)));
		}
		System.out.println();
		for (int i=0; i < k; i++) {
			if (i == 0)
				System.out.print("-Weighted MRR:");
			System.out.print("\t" + df.format(mrrWeighted.get(i)));
		}
		System.out.println();
		for (int i=0; i < k; i++) {
			if (i == 0)
				System.out.print("-TF-IDF MRR:");
			System.out.print("\t" + df.format(mrrTFIDF.get(i)));
			
		}
		System.out.println();
	}
	
	/*
	 * Save all the results to output files.
	 */
	private static void saveResults(String outPath, Hashtable<String, Hashtable<String, Integer>> graphList, Hashtable<String, Hashtable<String, Double>> pageRank, 
			Hashtable<String, ArrayList<String>> nGramsList, Hashtable<String, ArrayList<Integer>> mrrScores, ArrayList<Double> mrrGlobal) {
		boolean fResult = true;
		
		// If the folder doesn't exists then will be created
		if (!Utilities.folderExists(outPath))
			fResult = Utilities.createFolder(outPath);
		
		// If the output folder exists...
		if (fResult) {
			System.out.println("   The output folder is: " + outPath);
			
			// File variables
			String filepath;
			ArrayList<String> fileLines;
			Hashtable<String, Integer> graph;
			Hashtable<String, Double> pRank;
			ArrayList<String> nGrams;
			
			// Get file name list
			ArrayList<String> files = Utilities.getSortedKeys(graphList.keys());
			ArrayList<String> keys;
			String currKey;
			
			// Create the output files
			for (String fName : files) {
				fileLines = new ArrayList<String>();
				
				// Get file values
				filepath = outPath + fName; 
				graph = graphList.get(fName);
				pRank = pageRank.get(fName);
				nGrams = nGramsList.get(fName);
				
				// Save file info
				fileLines.add("File name: " + fName);
				//fileLines.add(" Results of weighted PageRank.");
				
				// Save graph info
				keys = Utilities.getSortedKeys(graph.keys());
				fileLines.add("\nGraph edges count: " + keys.size());
				for (int i=0; i < keys.size(); i++) {
					currKey = keys.get(i);
					fileLines.add(" " + currKey.replace(PageRank.JOIN_OPER, " - ") + ": " + graph.get(currKey));
				}
					
				// Save PageRank info
				keys = Utilities.getSortedKeys(pRank.keys());
				fileLines.add("\nPageRank scores count: " + keys.size());
				for (int i=0; i < keys.size(); i++) {
					currKey = keys.get(i);
					fileLines.add(" " + currKey + ": " + pRank.get(currKey));
				}
				
				// Save N-grams info
				fileLines.add("\nN-grams list: " + nGrams.size());
				for (int i=0; i < nGrams.size(); i++) {
					fileLines.add(" " + nGrams.get(i));
				}
				
				// Save the current Rd score
				ArrayList<Integer> currRd = mrrScores.get(fName);
				fileLines.add("\nRd for k = [1:10]: " + currRd);

				// Save the global MRR score
				fileLines.add("\nMRR for k = [1:10]: " + mrrGlobal);
				
				// Save/Create the output file
				mReader.saveArrayList(fileLines, filepath);
			}
			System.out.println("   The output files were created correctly.");
		}
		else {
			System.out.println("   The output files could not be created, because the folder does not exist: " + outPath);
		}
		
	}
	
}