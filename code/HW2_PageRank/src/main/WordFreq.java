package main;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Generic Word-Frequency class. Instance parameters: word, wordIx, freq. 
*/

public class WordFreq {
	
	// Object variables
	public String word;
	public int wordIx;
	public double freq;
	
	// Constructor 1
	public WordFreq(String word, double freq) {
		this.word = word;
		this.wordIx = -1;
		this.freq = freq;
	}

	// Constructor 2
	public WordFreq(int wordIx, double freq) {
		this.word = "";
		this.wordIx = wordIx;
		this.freq = freq;
	}
	
	// Constructor 3: full parameters
	public WordFreq(String word, int wordIx, double freq) {
		this.word = word;
		this.wordIx = wordIx;
		this.freq = freq;
	}
	
}