package org.fromdatotoVisualization.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTML.Tag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tartarus.snowball.StemmForBigData;


public class ScrapData {
	
	static List<String> titulares = new ArrayList<String> ();

	 public static void main(String[] args) throws IOException {
	       
		 	// configuration connection proxy...
	    	//System.setProperty("http.proxyHost", "yyy.yyy.yyy.yyy");
	    	//System.setProperty("http.proxyPort", "8080");
	    	//System.setProperty("http.proxyUser", "xxx");
	    	//System.setProperty("http.proxyUser", "xxx");																														System.setProperty("http.proxyPassword", "xxxxxxxxx");
	    	
	    	// noticia de la palabra...
	        String url = "http://....";
	        print("Fetching %s...", url);
	        
	        // Scraping WWW you can only use for this newspaper web.
	        Document newspaperHTML = Jsoup.connect(url).get();
	        System.out.println("TEXTO PARA ANALIZAR");
	        
	        Elements contents = newspaperHTML.select("div[id=contenido]");
	        Elements articles = contents.get(0).select("article");
	        
	        for (Element article : articles) {
				Elements headers = article.select("header");
				
				for (Element header : headers) {
					Elements allTagsInsideHeaders = header.getAllElements();
					
					if (allTagsInsideHeaders.size() == 3 && allTagsInsideHeaders.get(1).tag().getName().equals(
							Tag.H1.toString())) 
					{						
						Elements texts = header.select("a");
						
						// log 
						for (Element text : texts) {
							
							System.out.println(text.text());							
							titulares.add(text.text());
						}							
					}
				}
				
			}
	        
	        CountWords cw = new CountWords();
	        
	        Map<String,String> mapa = new LinkedHashMap<String,String>();
	        Map<String,List<String>> mapaFrase = new LinkedHashMap<String,List<String>>();
	        
	        // Limpieza de caracteres raros.
	        for (String header : titulares) {
				String[] titularSentence = header.split(" ");							
				int c = 0;
				for (String simpleSentence : titularSentence) {										
					deleteSymbols (simpleSentence, titularSentence, c);																								
					c++;					
				}
		
				// Example...Algoritmo de stematización.
				for (String simpleSentence : titularSentence) { 
					StemmForBigData stemm = new StemmForBigData();
				
					if (!stemm.preExecutedStoppingWord(simpleSentence)) {
						System.out.println(" Preprocesado de la raíz : [" + 
								stemm.executeStemming(simpleSentence) + "]");
						
						
						//adding word map
						mapa.put(stemm.executeStemming(simpleSentence) , simpleSentence);
						
						
						//adding header map
						List<String> temporalHeaders = null;
						 if ((temporalHeaders = mapaFrase.get(stemm.executeStemming(simpleSentence)))== null) {						 
							 temporalHeaders = new ArrayList<String>();
						 	 temporalHeaders.add(header);
						 }
						 else {
							 temporalHeaders.add(header);
						 
						 }
						mapaFrase.put(stemm.executeStemming(simpleSentence), temporalHeaders);
						
						cw.count(stemm.executeStemming(simpleSentence));
						
					}
														
					//
					System.out.println("original: " + simpleSentence);
				}
				
				
			}
	        
	        Map<String,Integer> example = cw.returnTenWordsOnHeap();
			List<Integer> values = new ArrayList<Integer>(example.values());
			List<String> key = new ArrayList<String>(example.keySet());
			System.out.println("===========================================================");
			System.out.println("The ten terms more used in news are:");
			for (int j=key.size()-1; j>key.size()-10 ; j--)
			{
				
				System.out.print("[Word:" + key.get(j) + " ===> " + mapa.get(key.get(j)));
				System.out.print("[Header:" +  mapaFrase.get(key.get(j)));
				System.out.println(" n= "+values.get(j) + "]");
				
				
			}
			System.out.println("===========================================================");
	 }
	 
	 /**
	  * 
	  * @param targetWordOfSentence
	  * @param pos
	  * @param sentence
	  * @param wordPositionInSentence
	  */
	 private static void deleteSymbols (String targetWordOfSentence, String[] sentence, int wordPositionInSentence) 
	 {
		 char[] caracteres = targetWordOfSentence.toCharArray();
			for (int pos=0; pos<caracteres.length; pos++) {
				
			 if (caracteres[pos] == '.' || caracteres[pos] == ',' ||
				 caracteres[pos] == '!' || caracteres[pos] == '¡' ||
				 caracteres[pos] == ':' || caracteres[pos] == '¿' ||
				 caracteres[pos] == '?' || caracteres[pos] == ';' || caracteres[pos] == '\'') {
				 
		 		if (pos > 0 && pos < targetWordOfSentence.length()-1) {
		 			
					String pre = targetWordOfSentence.substring(0, pos);
					String post = targetWordOfSentence.substring(pos+1, targetWordOfSentence.length());
					sentence[wordPositionInSentence] = pre.concat(post);
					targetWordOfSentence = sentence[wordPositionInSentence];
					 caracteres = targetWordOfSentence.toCharArray();
					
				} else if (pos == 0) {
					
					sentence[wordPositionInSentence] = targetWordOfSentence.substring(1, targetWordOfSentence.length());
					targetWordOfSentence = sentence[wordPositionInSentence];
					caracteres = targetWordOfSentence.toCharArray();
					
				} else if (pos == targetWordOfSentence.length()-1) {
					
					sentence[wordPositionInSentence] = targetWordOfSentence.substring(0, targetWordOfSentence.length()-2);
					targetWordOfSentence = sentence[wordPositionInSentence];
					 caracteres = targetWordOfSentence.toCharArray();
				}
			 }
		}
	 }
	 
	 /** */	 
	 private static void print(String msg, Object... args) {
	      System.out.println(String.format(msg, args));
	 }
}
