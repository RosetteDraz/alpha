
package cluster;

import java.util.HashMap;
import java.util.List;

public class TfIdf implements Parameterize {
	private int allWordCount; // Holds the total count of the words within all the documents.
	// Holds a mapping between each word and its location within the total words vector.
	private HashMap<String, Integer> allWords;
	private List<Document> documents; // Holds all the documents.

	// Ctor.
	public TfIdf(int allWordCount, HashMap<String, Integer> allWords, List<Document> documents){
		this.allWordCount=allWordCount;
		this.allWords=allWords;
		this.documents=documents;
		
	}

	public double[] calcVector(Document doc) {
		double[] tfidf_array = new double[allWordCount];
		for(String word: doc.getDoc_words())
		{
			double tfidf =tfIdf(doc.getDoc_words(), documents, word);
			tfidf_array[allWords.get(word)] = tfidf;
			
			//System.out.println("TF-IDF ("+ word +(")=") + tfidf);
		}
		return tfidf_array;
	
	}

	private double tf(List<String> doc, String term) {
		double result = 0;
		for (String word : doc) {
			if (term.equalsIgnoreCase(word))
				result++;
		}	

		return result / doc.size();
	}

	private double idf(List<Document> docs, String term) {
		double n = 0;

		for (Document doc : docs) {

			for (String word : doc.getDoc_words()) {
				
				if (term.equalsIgnoreCase(word)) {

					n++;
             
					break;

				}

			}
			
		}

		return Math.log(docs.size() / n);

	}
	
	private double tfIdf(List<String> doc, List<Document> docs, String term) {
		return tf(doc, term) * idf(docs, term);

	}

}
