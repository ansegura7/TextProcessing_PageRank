# Text Processing: PageRank

## Author Info
- Created By: Andres Segura Tinoco
- Created On: June 27, 2018

## Program Description
The program has 7 classes. Each of them is described below:

File class: **MainClass.java**
- Description: Main class of the program. Receive the directory path where the files (abstracts and gold-standard) are located. It should only be executed and the answers will be obtained by console and in the output files.
- Main methods:
    - **main**: Main function of main class.
    - **runHomeWork2**: Private function that run/execute the main task of Homework 2.
    - **showResultByConsole**: Shows the results by console in formatted mode.
    - **saveResults**: Save all the results to output files.

File class: **PageRank.java**
- Description: Class that contains the PageRank functions to create the Graphs list, calculate the PageRank score and calculate the MRR value.
- Main methods:
    - **createGraphList**: Function that creates a Graph List from the abstract of the documents.
    - **getPageRankList**: Function that run the PageRank algorithm on each word graph corresponding to each document in the collection. If weighted parameter is true then the algorithm uses weights of edges.
    - **runPageRankToGraph**: Run the PageRank algorithm to specific Word Graph. If weighted parameter is true then the algorithm uses weights of edges.
    - **createPageRankValues**: Function that create the PageRank values from a graph.
    - **createNgrams**: Function that forms n-grams of length up to 3 (unigrams, bigrams and trigrams) from words adjacent in text. 
    - **getRdScores**: Calculates the Rank(d) list for the entire collection for top-k ranked n-grams or phrases, where k ranges from 1 to 10.
    - **getStemmedGoldNgrams**: Return the list of stemmed gold N-grams.
    - **calculateMRR**: Calculates the Mean Reciprocal Rank (MRR) for an entire collection.
    - **getValidToken**: Return the word only if it is a nouns or adjective.
    - **getValidPartsOfTheSpeech**: Returns the valid part of speech tags for this HW2. It is a private function.

File class: **TfIdf.java**
- Description: Class that contains the TF-IDF functions to calculate the score by file/document.
- Main methods:
    - **getTfIdf**: Create the TF-IDF score by document <Word, Frequency>.
    - **getCollection**: Function that returns a collection of documents from a file list.
    - **createInverseIndex**: Function that create a inverted index from a collection of documents.
    - **createSparseTFIDF**: Function that create the sparse TF-IDF representation of collection of documents.
    - **getWordDoctFrequency**: Calculates and returns the frequency of a word in the documents of a collection.
    - **getMaxFreqByDoct**: Returns the maximum frequency of any word by document.
    - **cleanText**: Clean raw text. Remove punctuation. Data Quality function.

File class: **WordFreq.java**
- Description: Generic Word-Frequency class. Instance parameters: word, wordIx, freq.

File class: **Reader.java**
- Description: Class that contains the functions for reading and creating text files.
- Main methods:
    - **readFile**: Read a file line by line and return an array of strings.
    - **readFiles**: Read the document collection and returns a hash table.
    - **getFileList**: Returns the file list (of file name) from a directory.
    - **readFileContent**: Read and return the file content.
    - **saveArrayList**: Create a text file from a Array List of Strings.

File class: **Utilities.java**
- Description: Class that contains generic or utility (static) functions for any program.
- Main methods:
    - **folderExists**: Returns True if the directory path exists. Else return False.
    - **fileExists**: Returns True if the file path exists. Else return False.
    - **createFolder**: Create a directory/folder.
    - **splitTextByTokens**: Split a raw text by tokens. Tokenize on whitespace.
    - **sortHashtableByDblValue**: Sort a hash table by double values.
    - **sortHashtableByIntValue**: Sort a hash table by integer values.
    - **sortArrayList**: Sort an array list in alphabetical order with case-insensitive.
    - **getSortedKeys**: Sort the keys of hash table alphabetical.
    - **getSortedKeysFromHT**: Sorting words alphabetically from double hash table.
    - **getTopNtokens**: Get the Top N values by score decrescent order.
    - **getStopWordList**: Return a list of stop words.
    - **filterHashTableByKeys**: Filter the elements of a hash table against a list of keys.
    - **arrayContainsWord**: Generic function that returns True if a word is contained within an array list.
    - **showHashTable**: Shows the (key, value) pairs of a hash table structure.
    - **showHashTableNested**: Shows the (key, value) pairs of a nested hash table structure.
    - **log**: Calculates the Log function of any base.

File class: **Porter.java**
- Description: Compile it, import the Porter class into you program and create an instance. Then use the stripAffixes method of this method which takes a String as input and returns the stem of this String again as a String.
- Main methods:
    - **stripAffixes**: makes the steeming of token.
