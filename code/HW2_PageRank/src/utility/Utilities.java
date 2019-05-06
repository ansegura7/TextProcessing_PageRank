package utility;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains generic or utility (static) functions for any program.
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import utility.Porter;

public class Utilities {
	
	// Class objects
	private static ArrayList<String> stopWords = getStopWordList();
	
	/*
	 * Returns True if the directory path exists. Else return False.
	 */
	public static boolean folderExists(String filepath) {
		File f = new File(filepath);
		
		if (f.isDirectory())
			return true;
		
		return false;
	}
	
	/*
	 * Returns True if the file path exists. Else return False.
	 */
	public static boolean fileExists(String filepath) {
		File f = new File(filepath);
		
		if (f.exists() && !f.isDirectory())
			return true;
		
		return false;
	}
	
	/*
	 * Create a directory/folder.
	 */
	public static boolean createFolder(String filepath) {
		File f = new File(filepath);
		f.mkdirs();
		
		return folderExists(filepath);
	}
	
	/*
	 * Split a raw text by tokens. Tokenize on whitespace.
	 */
	public static ArrayList<String> splitTextByTokens(String rawText, boolean advanced) {
		ArrayList<String> tokenList = new ArrayList<String>();
		
		// Temporal variables
		Porter stemmer = new Porter();
		StringTokenizer st = new StringTokenizer(rawText);
		String currToken = "";
		
		while (st.hasMoreElements()) {
			currToken = st.nextToken();
			
			// Remove some punctuation 
			if (currToken.compareTo(",_,") != 0) { // && currToken.compareTo("._.") != 0) {
			
				// Integration with the Porter stemmer and a stopword eliminator
				if (advanced) {
					
					// With double check of no stop-words
					if (!stopWords.contains(currToken)) {
						currToken = stemmer.stripAffixes(currToken);
						if (!stopWords.contains(currToken) && currToken != "")
							tokenList.add(currToken);
					}
				}
				else {
					// Just add the token
					if (currToken != "")
						tokenList.add(currToken);
				}
			}
		}
		
		return tokenList;
	}
	
	/*
	 * Sort a hash table by double values.
	 */
	public static List<Map.Entry<String, Double>> sortHashtableByDblValue(Hashtable<String, Double> wordCount) {
		List<Map.Entry<String, Double>> sortedList = new ArrayList<Map.Entry<String, Double>>(wordCount.entrySet());
		
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Double>>() {
        	public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
        		Double i1 = (double) e1.getValue();
        		Double i2 = (double) e2.getValue();
        		return i2.compareTo(i1);
            }
        });
        
        return sortedList;
	}
	
	/*
	 * Sort a hash table by integer values.
	 */
	public static List<Map.Entry<String, Integer>> sortHashtableByIntValue(Hashtable<String, Integer> wordCount) {
		List<Map.Entry<String, Integer>> sortedList = new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());
		
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
        	public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
        		Integer i1 = (int) e1.getValue();
        		Integer i2 = (int) e2.getValue();
        		return i2.compareTo(i1);
            }
        });
        
        return sortedList;
	}
	
	/*
	 * Sort an array list in alphabetical order with case-insensitive.
	 */
	public static void sortArrayList(ArrayList<String> list) {
		
		Collections.sort(list, new Comparator<String>() {
		    @Override
		    public int compare(String s1, String s2) {
		        return s1.compareToIgnoreCase(s2);
		    }
		});
	}
	
	/*
	 * Sort the keys of hash table alphabetical.
	 */
	public static ArrayList<String> getSortedKeys(Enumeration<String> e) {
		
		// Sorting words alphabetically
		ArrayList<String> sl = Collections.list(e);
		Collections.sort(sl);
		
		return sl;
	}
	
	/*
	 * Sorting words alphabetically from double hash table.
	 */
	public static ArrayList<String> getSortedKeysFromHT(Hashtable<String, Hashtable<String, Integer>> ht) {
		
		// Sorting words alphabetically
		ArrayList<String> sl = Collections.list(ht.keys());
		Collections.sort(sl);
		
		return sl;
	}
	
	/*
	 * Get the Top N values by score decrescent order.
	 */
	public static ArrayList<String> getTopNtokens(Hashtable<String, Double> wRank, int N, boolean show) {
		ArrayList<String> sWords = new ArrayList<String>();
		List<Map.Entry<String, Double>> sValues;
		String key;
		double value;
		int ix = 0;
		
		// Get sorted words by value
		sValues = Utilities.sortHashtableByDblValue(wRank);
		
		// Save the top N
		for(Map.Entry<String, Double> entry : sValues) {
			ix += 1;
			
			// Get only the top N words
			if (ix <= N) {
				key = entry.getKey();
				sWords.add(key);
				
				if (show) {
					value = entry.getValue();
					System.out.println(ix + ", word: " + key + ", value: " + value);
				}
			}
			else
				break;
		}
		
		return sWords;
	}
	
	/*
	 * Return a list of stop words.
	 */
	public static ArrayList<String> getStopWordList() {
		String filepath = "resources/stopwords.txt";
		Reader myReader = new Reader();
		ArrayList<String> stopWords = myReader.readFile(filepath);
		return stopWords;
	}
	
	/*
	 * Filter the elements of a hash table against a list of keys.
	 */
	public static Hashtable<String, String> filterHashTableByKeys(Hashtable<String, String> ht, ArrayList<String> sKeys) {
		Hashtable<String, String> newHT = new Hashtable<String, String>();
		Enumeration<String> e = ht.keys();
		String currKey;
		
		while(e.hasMoreElements()) {
			currKey = e.nextElement();
			if (sKeys.contains(currKey))
				newHT.put(currKey, ht.get(currKey));
		}
		
		return newHT;
	}
	
	/*
	 * Generic function that returns True if a word is contained within an array list.
	 */
	public static boolean arrayContainsWord(ArrayList<String> wordList, String tWord) {
		String[] tokens;
		String currToken;
		
		// Full exploration
		for(String word : wordList) {
			tokens = tWord.split(" ");
			for (int i=0; i < tokens.length; i++) {
				currToken = tokens[i];
				if (word.contains(currToken) || currToken.contains(word))
					return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Shows the (key, value) pairs of a hash table structure.
	 */
	public static void showHashTable(Hashtable<String, Integer> ht, String htTitle) {
		System.out.println(">> Show " + htTitle + " of size: "+ ht.size());
		Enumeration<String> e = ht.keys();
		String key;
		int value;
		int sumValues = 0;
		
		while(e.hasMoreElements()) {
			key = e.nextElement();
			value = ht.get(key);
			sumValues += value;
			System.out.println("   Key: " + key + ", Value: " + value);
		}
		System.out.println("   Sum of values: " + sumValues);
	}
	
	/*
	 * Shows the (key, value) pairs of a nested hash table structure.
	 */
	public static void showHashTableNested(Hashtable<String, Hashtable<String, Double>> ht, String htTitle) {
		System.out.println(">> Show " + htTitle + " of size: "+ ht.size());
		ArrayList<String> k1 = getSortedKeys(ht.keys());
		double value;
		
		for (String key1 : k1) {
			Hashtable<String, Double> ht2 = ht.get(key1);
			System.out.println(">> Object name: " + key1);
			
			ArrayList<String> k2 = getSortedKeys(ht2.keys());
			for (String key2 : k2) {
				value = ht2.get(key2);
				System.out.println("   Key: " + key2 + ", Value: " + value);
			}
		}
	}
	
	/*
	 * Calculates the Log function of any base.
	 */
	public static double log(double x, int base){
	    return (Math.log(x) / Math.log(base));
	}
	
}