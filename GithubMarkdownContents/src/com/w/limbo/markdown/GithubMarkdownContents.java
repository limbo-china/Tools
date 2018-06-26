package com.w.limbo.markdown;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GithubMarkdownContents {
	
	private String filename = null;
	private String originalText = "";
	
	private FileInputStream input = null;
	private InputStreamReader reader = null;
	private BufferedReader buffer = null;
	
	private List<Title> titleList = new ArrayList<Title>();
	
	private static GithubMarkdownContents INSTANCE = null; 
	
	private GithubMarkdownContents(String filename) throws FileNotFoundException{
		this.filename = filename;	
		this.input = new FileInputStream(filename);
		this.reader = new InputStreamReader(input);
		this.buffer = new BufferedReader(reader);
	}
	
	public static GithubMarkdownContents getInstance(String filename) throws FileNotFoundException{
		if(INSTANCE == null)
			INSTANCE = new GithubMarkdownContents(filename);
		return INSTANCE;
	}
	
	private void addContents() throws IOException{
		fillTitleList();
		handleTitleList();
		writeContents();
		//to do refactor!
		
		//to do
	}
	
	private void fillTitleList() throws IOException{
		String line = null;
		while((line = buffer.readLine())!=null){
			if(isMainTitle(line))
				titleList.add(new Title(line, true));
			else if(isSubTitle(line))
				titleList.add(new Title(line, false));
			originalText += line + "\n";
		}
	}
	
	private boolean isMainTitle(String line){
		return line.startsWith("# ");
	}
	private boolean isSubTitle(String line){
		return line.startsWith("### ");
	}
	
	private void handleTitleList(){
		for(Title title : titleList)
			title.handleTitle();
	}
	
	private String generateContents(){
		String contents = "";
		for(Title title : titleList)
			contents += generateALine(title);
		contents += "\n";
		return contents;
	}
	private String generateALine(Title title){
		return (title.isMainTitle()?"- ":"\t- ")+"["+
				title.getOriginalTitle().replaceAll("[#]+[ ]+", "")+"]"+"("+
				title.getHandledTitle()+")"+"\n";
				
	}

	private void writeContents() throws IOException{
		
		FileWriter writer = new FileWriter(filename+"___contents");
		writer.write(generateContents());	
		writer.write(originalText);
		writer.close();
	}
	public static void main(String[] args) throws IOException{
		GithubMarkdownContents mc = GithubMarkdownContents.getInstance(args[0]);
		mc.addContents();
	}
}

class Title{
	private String originalTitle;
	private String handledTitle;
	private boolean isMainTitle;
	
	public Title(String title, boolean isMainTitle){
		this.originalTitle = title;
		this.isMainTitle = isMainTitle;
		this.handledTitle = "unhandled";
	}
	
	public String getOriginalTitle(){ return originalTitle; }
	public String getHandledTitle(){ return handledTitle; }
	public boolean isMainTitle(){ return isMainTitle; }
	
	public void handleTitle(){
		handledTitle = originalTitle.toLowerCase()
				.replaceAll("[#]+[ ]+", "#")
				.replaceAll("[^a-zA-z 0-9#]","")
				.replaceAll("[ ]+", "-");
	}
	
	@Override
	public String toString(){		
		return originalTitle+ ","+
				handledTitle+ ","+
				isMainTitle + "\n";
	}
	
}
