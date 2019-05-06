# Text Processing: PageRank

## Author Info
- Created By: Andres Segura Tinoco
- Created On: June 27, 2018

## Abstract
The goal of this project is to use PageRank to derive a ranking of words in a document based on their PageRank scores. The PageRank score of a word serves as an indicator of the importance of the word in the text. Also compare the MRR of the above PageRank algorithm with the MRR of a ranking of words based on their TF-IDF ranking scheme.

The project name is: **HW2_PageRank** and it was created using the Eclipse IDE. The program solves all the questions in a single execution (run).

For point 1, a directed graph was used (using a hash table and adjacency lists as data structures).
For point 2 and 2', a function was created that returns the PageRank scores per word by document.
For point 3, a parameterized function was created that generates both unigrams and n-grams.
For point 4, a function was created that calculates the MRR from a list of proposed n-grams and the phrases available in the gold-standard files.
For point 5, a function was created that calculates the TF-IDF scores for each word of each abstract document and then calculates the MRR score to the proposed n-grams.

## Data
For this project, it was used the WWW collection consisting of titles and abstracts of research articles published in the WWW conference.

## Algorithms
- Simple PageRank: run PageRank on each word graph corresponding to each document in the collection as
follows:
    - Initialization: s = [s(V1), . . . , s(Vn)] = [1/n, . . . , 1/n], where n = |V|.
    - Score nodes in a graph using their PageRank obtained by iteratively computing the equation.
    - Where α is a damping factor (α = 0.85) and p =  [1/n, . . . , 1/n].
![Unigrams and Direct (Full) comparison](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/page-rank-single.PNG)

- Weighted PageRank: assume the weight Wij of an edge (Vi, Vj) is calculated as the number of times the corresponding words Wi and Wj are adjacent in text. Run PageRank on each word graph corresponding to each document in the collection using:
![Unigrams and Direct (Full) comparison](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/page-rank-weighted.PNG)

- Mean reciprocal rank (MRR):
![Unigrams and Direct (Full) comparison](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/mrr-formula.PNG)
Rd is the rank at which the first correct prediction was found for d ∈ D.

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

## Technologies and Techniques
- Java (JDK 1.7)
- Eclipse IDE
- Page Rank
- TF-IDF
- Tokenization
- Stemming
- Lemmatization
- Remove stop-words

## Program Execution Rules
A .JAR file of the program was created. The JAR name is: KD_HW2_v1.7.jar and you must send the path of the "data/www/" directory as a parameter, the n-grams size and the comparison type as parameters.

Therefore, to run the program you should go into the HW2 directory on your machine and then execute the following command:
~~~
    cd HW2
    java -jar KD_HW2_v1.7.jar data/ [UNIGRAMS|NGRAMS] [DIRECT|BY_TOKENS]
~~~
Execution examples:
~~~
    cd HW2
    java -jar KD_HW2_v1.7.jar data/ UNIGRAMS DIRECT
    java -jar KD_HW2_v1.7.jar data/ UNIGRAMS BY_TOKENS
    java -jar KD_HW2_v1.7.jar data/ NGRAMS DIRECT			(DEFAULT CONFIGURATION)
    java -jar KD_HW2_v1.7.jar data/ NGRAMS BY_TOKENS
~~~
The .JAR program must be run with Java 7 or higher.

## Program Output
1. Show the MRR Scores: Using only Unigrams and Direct (Full) comparison

|  | k=1  | k=2  |  k=3 | k=4 | k=5 | k=6 | k=7 | k=8 | k=9 | k=10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Simple MRR | 0.056 | 0.080 | 0.090 | 0.098 | 0.104 | 0.106 | 0.109 | 0.111 | 0.113 | 0.115 |
| Weighted MRR | 0.053 | 0.079 | 0.088 | 0.096 | 0.101 | 0.103 | 0.106 | 0.108 | 0.110 | 0.112 |
| TF-IDF MRR | 0.167 | 0.209 | 0.228 | 0.239 | 0.243 | 0.248 | 0.250 | 0.252 | 0.253 | 0.254 |

![Unigrams and Direct (Full) comparison](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/option1.PNG)

2. Show the MRR Scores: Using only Unigrams and comparison by Token

|  | k=1  | k=2  |  k=3 | k=4 | k=5 | k=6 | k=7 | k=8 | k=9 | k=10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Simple MRR | 0.394 | 0.503 | 0.537 | 0.556 | 0.566 | 0.570 | 0.574 | 0.576 | 0.577 | 0.579 |
| Weighted MRR | 0.388 | 0.500 | 0.534 | 0.554 | 0.564 | 0.569 | 0.572 | 0.574 | 0.575 | 0.576 |
| TF-IDF MRR | 0.576 | 0.674 | 0.706 | 0.714 | 0.718 | 0.721 | 0.722 | 0.723 | 0.723 | 0.723 |

![Unigrams and comparison by Token](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/option2.PNG)

3. Show the MRR Scores: Using N-grams and Direct (Full) comparison

|  | k=1  | k=2  |  k=3 | k=4 | k=5 | k=6 | k=7 | k=8 | k=9 | k=10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Simple MRR | 0.050 | 0.069 | 0.086 | 0.100 | 0.112 | 0.120 | 0.125 | 0.129 | 0.134 | 0.136 |
| Weighted MRR | 0.053 | 0.076 | 0.094 | 0.107 | 0.120 | 0.128 | 0.132 | 0.137 | 0.141 | 0.143 |
| TF-IDF MRR | 0.077 | 0.118 | 0.149 | 0.166 | 0.179 | 0.189 | 0.195 | 0.199 | 0.203 | 0.206 |

![N-grams and Direct (Full) comparison](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/option3.PNG)

4. Show the MRR Scores: Using N-grams and comparison by Token

|  | k=1  | k=2  |  k=3 | k=4 | k=5 | k=6 | k=7 | k=8 | k=9 | k=10 |
|---|---|---|---|---|---|---|---|---|---|---|
| Simple MRR | 0.717 | 0.777 | 0.791 | 0.798 | 0.803 | 0.805 | 0.807 | 0.807 | 0.808 | 0.808 |
| Weighted MRR | 0.720 | 0.779 | 0.794 | 0.801 | 0.806 | 0.808 | 0.809 | 0.809 | 0.810 | 0.810 |
| TF-IDF MRR | 0.764 | 0.804 | 0.819 | 0.827 | 0.831 | 0.832 | 0.833 | 0.834 | 0.834 | 0.835 |

![N-grams and comparison by Token](https://raw.githubusercontent.com/ansegura7/TextProcessing_PageRank/master/images/option4.PNG)

## Conclusions
1. The results between the simple and the weighted PageRank are practically the same. This may be due to the fact that the graphs weights are mostly low.
2. The results of TF-IDF were always better than those obtained by PageRanks.
3. The way in which the n-grams are created (unigrams, bigrams or trigrams) and the way in which they compare with the exits of the gold-standard files (directly or by tokens) have a marked influence on the results of the MRR.

---
