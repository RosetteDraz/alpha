package koko;

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class wordCount {
	public static void main(String[] args) throws IOException 
	{ 
		JButton open = new JButton();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("C:/dialog"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) 
		{
			//
		}
		    
		System.out.println("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());

		File file= fileChooser.getSelectedFile();
		FileReader fileReader=new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();
		String line;
		while ((line = br.readLine())!= null)
		{
			stringBuffer.append(" ");
			stringBuffer.append(line);
			
		}
		fileReader.close(); 
	
		//clean the text
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
	
		String everything=stringBuffer.toString();
		String [] stwords=everything.split("\\s+");
		// Arrays.asList(stwords);
		
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();

		for(int c =0; c<stwords.length;c++)
		{
			System.out.println(stwords[c]);
			if(!hm.containsKey(stwords[c])) {
				hm.put(stwords[c],1);
			}
			else {
				
				hm.put(stwords[c], hm.get(stwords[c]) + 1);
			}
		}
		
	}
}

	

