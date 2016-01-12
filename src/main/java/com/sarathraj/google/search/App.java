package com.sarathraj.google.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App 
{
	
	static String fullString="";
    List<Item> items;
    int frequency = 0;
	public SearchResult getDataFromGoogleLoop(String searchItem,int index) throws Exception
	{	
		SearchResult  resultData=null;
	    String outputmain="";
	    String output=null;
	    ObjectMapper mapper =null;
		mapper = new ObjectMapper();
		//URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDQKOT4hhBVrU4_S6RwLed9fFhtkb2gyg8&cx=013036536707430787589:_pqjad5hr1a&q=flowers&alt=json");
		URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDQKOT4hhBVrU4_S6RwLed9fFhtkb2gyg8&cx=017576662512468239146:omuauf_lfve&q="+searchItem.trim()+"&alt=json&start="+String.valueOf(index)+"&num=10&fields=kind,items(title,displayLink,link,htmlTitle,snippet,htmlSnippet)");		
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        
        while ((output = br.readLine()) != null) {
         	outputmain=outputmain+output.trim();         	
        }
        System.out.println("############");
        System.out.println(outputmain);
        fullString=fullString+outputmain;
        frequency = frequency + outputmain.split(searchItem,-1).length-1;
        System.out.println("############ frequency" + frequency);
        resultData = mapper.readValue(outputmain, SearchResult.class); 
        br.close();
        conn.disconnect();		
        return resultData;
	}
	
    public void getDataFromGoogle(String searchItem, int size) throws Exception
    {
    	frequency=0;
    	items =new ArrayList<Item>();
    	for (int i=1,j=10;i<=size && j<=size;i=i+10,j=j+10)
    	{
    	System.out.println("loop starts here:" + i);		
    	SearchResult result_Old = getDataFromGoogleLoop(searchItem,i);
    	//System.out.println(result_Old.getItems().);
    	if(result_Old.getItems()!=null)
    	items.addAll(result_Old.getItems());		
    	}
    	
    	
    	System.out.println("final frequency "+frequency);
    	UniqueUrls();
    }
    
    
    public void UniqueUrls()
    {   
    	List<String> mainStringUrl = new ArrayList<String>();
    	Set<String> list = new HashSet<String>();
    	for (Item item : items) {
    		mainStringUrl.add(item.getDisplayLink());
    		list.add(item.getDisplayLink().trim());
    		saveItemsToFile(item.getDisplayLink().trim());
		}
    	saveItemsToFile("Frequency : "+frequency);
    	
    	System.out.println("Non Unique :  " + mainStringUrl.size());
    	System.out.println("Unique     : " + list.size());
    }
    
    
    public void getEmails(String keyword) 
    {
    	//String keyword = "@acelrtech.com";
    	String regex = "\\b"+keyword+"\\b";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(fullString);
        while(m.find()) {
            String s = m.group(); 
            s = s.substring(1); 
            saveItemsToFile(s);
        }
       
    }
    
    public void saveItemsToFile(String content)
    {
    	File file = new File("results.txt");
		try (FileOutputStream fop = new FileOutputStream(file,true)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.write("\n".getBytes());
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Searching starts here" );
        App searching = new App();
        searching.getDataFromGoogle("Radieux pvt ltd",30);
        searching.getDataFromGoogle("Scala",30);
        
        //getting emails from google
        searching.getDataFromGoogle("radieuxindia.com+%2B+email",50);
        searching.getEmails("@radieuxindia.com");
        
        
        
        
    }
}
