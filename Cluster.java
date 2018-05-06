	package cluster;
	
	import java.io.BufferedReader;  
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.IOException;
	import java.util.Arrays;
	import java.util.HashMap;
	import java.util.LinkedList;
	import java.util.List;
	
	import javax.swing.JButton;
	import javax.swing.JFileChooser;
	
	public class Cluster {
		public enum DisMethodType {
		    MAX, MIN, AVG
		}
		
		public static void main(String[] args) throws IOException 
		{ 
			if (args.length != 2) {
				System.out.println("Provide wordCount|tfidf min|max|avg.");
				return;
			}
			String countType = args[0];
			if (!countType.equals("wordCount") && !countType.equals("tfidf")) {
				System.out.println(countType + " is not supported");
				System.out.println("Only wordCount or tfidf needs to be provided.");
				return;
			}
			String disMethodType = args[1];
			if (!disMethodType.equals("min") && !disMethodType.equals("max") && !disMethodType.equals("avg")) {
				System.out.println(disMethodType + " is not supported");
				System.out.println("Only min, max or avg needs to be provided.");
				return;
			}
			
			File[] files = getFiles();
			if (files.length == 0) {
				return;
			}

			List<Document> documents = new LinkedList<Document>();
			HashMap<String, Integer> allWords = new HashMap<String, Integer>();
			int allWordCount = createDocumentsFromFiles(files, documents, allWords);
			
			Parameterize parameterize;
			if (countType.equals("wordCount"))
			{
				System.out.println("Using wordCount with " + disMethodType);
				parameterize = new WordCount(allWordCount, allWords);
			}
			else {
				System.out.println("Using TFIDF with " + disMethodType);
				parameterize = new TfIdf(allWordCount, allWords, documents);
			}
			createDocumentsVector(documents, parameterize);
			
			for (Document doc: documents)
			{
				System.out.println(doc + ":");
				doc.printVector();
			}
			
			cluster(documents, DisMethodType.valueOf(disMethodType.toUpperCase()));
	//		System.out.println(wc_list);
			
	}
	
		private static void createDocumentsVector(List<Document> documents, Parameterize parameterize) {
			for(Document doc : documents)
			{
				doc.setVector(parameterize.calcVector(doc));				
			}
		}
	
		private static int createDocumentsFromFiles(File[] files, List<Document> documents,
				HashMap<String, Integer> allWords) throws FileNotFoundException, IOException {
			int wordCount = 0;
			for (File file : files) {
				FileReader fileReader=new FileReader(file);
				BufferedReader br = new BufferedReader(fileReader);
				String line;
				StringBuffer stringBuffer = new StringBuffer();
				while ((line = br.readLine())!= null)
				{
					stringBuffer.append(" ");
					stringBuffer.append(line);	
				}
				fileReader.close(); 
				txtClean(stringBuffer);
				String everything=stringBuffer.toString();
				everything=everything.toLowerCase();
				String [] stwords=everything.split("\\s+");
				
				
				for (int i=0;i<stwords.length;i++)
				{
					if(!allWords.containsKey(stwords[i])) {
						allWords.put(stwords[i], wordCount++);
					}
				}
				Document doc=new Document(file.getName());
				doc.setDoc_words(Arrays.asList(stwords));
				documents.add(doc);
			}
			return wordCount;
		}
	
		private static File[] getFiles() {
			JButton open = new JButton();
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setCurrentDirectory(new java.io.File("C:/dialog"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.showOpenDialog(open);
			System.out.println("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
			
			File[] files = fileChooser.getSelectedFiles();
			return files;
		}
		
		
		private static void txtClean(StringBuffer stringBuffer) {
			
			for(int i=0; i< stringBuffer.length();i++)
			{
				if(stringBuffer.charAt(i)!=' ' &&
						!((stringBuffer.charAt(i)>='A' && stringBuffer.charAt(i)<='Z')||
								(stringBuffer.charAt(i)>='a' && stringBuffer.charAt(i)<='z')))
				{
					stringBuffer.deleteCharAt(i);
					i--;
				} 
			}
			}
		
		
		public static double arr_dis(double[] array1, double[] array2)
		{
		 double sum=0;
		 for(int i=0; i<array1.length; i++)
		 {
			 sum += array1[i]-array2[i];
		 }
			return sum;
		}
		
		public static double group_dis(List<Document> group1, List<Document> group2, DisMethodType method)
		{
			double min = Double.MAX_VALUE;
			double max = -1;
			double sum = 0;
	    	for(Document doc1:group1)
			{
	    		for(Document doc2:group2)
	    		{
	    			double dis=arr_dis(doc1.getVector(),doc2.getVector());
	    			if (dis>max){
	    				max=dis;
	    			}
	    				
	    			if(dis<min){
	    				min=dis;
	    			}
	    			
	    			sum+=dis;
	    		}
			}
	    	if (method == DisMethodType.MIN) {
	    		return min;
	    	}
	    	if (method == DisMethodType.AVG) {
	    		return sum/group1.size();
	    	}
	    	if (method == DisMethodType.MAX) {
	    		return max;
	    	}
	    	return 0;
		}
		
		public static void cluster(List<Document> documents, DisMethodType method)
		{
			List<List<Document>> group_list = new LinkedList<>();
			for(Document doc : documents)
			{
				List<Document> group = new LinkedList<>();
				group.add(doc);
				group_list.add(group);
			}
			while(group_list.size()>1)
			{
				joinGroups(group_list, method);
			}
		}
		
		private static void joinGroups(List<List<Document>> group_list, DisMethodType method) {
			double min=Double.MAX_VALUE;
			int g1_index=-1;
			int g2_index=-1;
			for(int i = 0; i < group_list.size(); i++)
			{
				for(int j= i+1; j < group_list.size(); j++)
				{
					double dis=group_dis(group_list.get(i),group_list.get(j), method);
					
					if(dis<min)
					{
						min=dis;
						g1_index=i;
						g2_index=j;
					}
				}
			}
			if (g1_index == -1 || g2_index == -1) {
				return;
			}
			List<Document> g1 = group_list.get(g1_index);
			List<Document> g2 = group_list.get(g2_index);
			
			System.out.print("Joining ");
			System.out.print(g1);
			System.out.print(" with ");
			System.out.println(g2);

			g1.addAll(g2);
			group_list.remove(g2_index);
		
		}
	
	}
	
		
	
