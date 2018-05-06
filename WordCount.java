package cluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount implements Parameterize {
	private int allWordCount;
	private HashMap<String, Integer> allWords;

	public WordCount(int allWordCount, HashMap<String, Integer> allWords) {
		this.allWordCount=allWordCount;
		this.allWords=allWords;
	}

	public double[] calcVector(Document doc) {
		double[] wc_array = new double[allWordCount];
		HashMap<String, Integer> hm = wordC(doc.getDoc_words());
		
		for(Map.Entry<String, Integer> m : hm.entrySet()) {
			wc_array[allWords.get(m.getKey())] = m.getValue();
		}
		return wc_array;
	}

	private HashMap<String, Integer> wordC(List<String> doc) {
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for(int c =0; c<doc.size();c++)
		{
//			System.out.println(doc.get(c));
			if(!hm.containsKey(doc.get(c))) {
				hm.put(doc.get(c),1);
			}
			else {
				
				hm.put(doc.get(c), hm.get(doc.get(c)) + 1);
			}
		}
		return(hm);
	}
}
