package utility;

/* 
 * Homework: 2
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains the functions for reading and creating text files.
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
	
	// Class constants
	private final static int MAX_FILES = 5000;
	
	/*
	 * Read a file line by line and return an array of strings.
	 */
	public ArrayList<String> readFile(String filename)  {
		ArrayList<String> textFile = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	textFile.add(line);
		    }
		}
	    catch (IOException ex) {
	    	textFile = new ArrayList<String>();
	        System.out.println(">> ERROR for " + filename + ": " + ex.getMessage());
	    }
		return textFile;
	}
	
	/*
	 * Read the document collection and returns a hash table.
	 */
	public Hashtable<String, String> readFiles(String filepath) {
		return readFiles(filepath, MAX_FILES);
	}
	
	// Overloaded Method of readFiles(String filepath)
	public Hashtable<String, String> readFiles(String filepath, int maxFiles) {
		Hashtable<String, String> files = new Hashtable<String, String>();
		
		// Temporal variables
		ArrayList<String> fileList = getFileList(filepath);
		
		if (fileList.size() > 0) {
			String filename = "";
			String fileFullname = "";
			String fileContent = "";
			
			// Reading files content
			for(int i = 0; i< fileList.size(); i++) {
				
				if (i < maxFiles) {
					filename = fileList.get(i);
					fileFullname = filepath + filename;
					fileContent = readFileContent(fileFullname);
					
					// Adding file
					files.put(filename, fileContent);
				}
				else {
					break;
				}
			}
		}
		
		return files;
	}
	
	/*
	 * Returns the file list (of file name) from a directory.
	 */
	public ArrayList<String> getFileList(String filepath) {	
		ArrayList<String> fileList = new ArrayList<String>();
		File folder = new File(filepath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        fileList.add(file.getName());
		    }
		}
		
		return fileList;
	}
	
	/*
	 * Read and return the file content.
	 */
	public String readFileContent(String filename) {
		String content = "";
		
	    try {
	        content = new String(Files.readAllBytes(Paths.get(filename)));
	    }
	    catch (IOException ex) {
	        content = "";
	        System.out.println(">> ERROR for " + filename + ": " + ex.getMessage());
	    }
	    
	    return content;
	}
	
	/*
	 * Create a text file from a Array List of Strings.
	 */
	public boolean saveArrayList(ArrayList<String> fileLines, String filename) {
		boolean result;
		Writer writer = null;
		
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		    
		    for(String line : fileLines)
		    	writer.write(line + "\n");
		    
		    result = true;
		}
		catch (IOException ex) {
			System.out.println(">> ERROR for " + filename + ": " + ex.getMessage());
			result = false;
		}
		finally {
		   try { writer.close(); } catch (Exception ex) {/*ignore*/}
		}
		
		return result;
	}
	
}