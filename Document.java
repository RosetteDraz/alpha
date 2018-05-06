package cluster;

import java.util.Arrays;
import java.util.List;

public class Document {
private String name;
private double[] vector;
private List<String> doc_words;

public Document( String name)
{
	this.name=name;
}

public double[] getVector() {
	return vector;
}

public void setVector(double[] vector) {
	this.vector = vector;
}


public String getName() {
	return name;
}

public List<String> getDoc_words() {
	return doc_words;
}

public void setDoc_words(List<String> doc_words) {
	this.doc_words = doc_words;
}

public String toString()
{
	return this.name;
}

public void printVector() {
	System.out.println(Arrays.toString(this.vector));
}
}
