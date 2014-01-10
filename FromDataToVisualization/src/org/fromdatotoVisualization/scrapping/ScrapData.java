package org.fromdatotoVisualization.scrapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	    	System.setProperty("http.proxyHost", "yyy.yyy.yyy.yyy");
	    	System.setProperty("http.proxyPort", "8080");
	    	System.setProperty("http.proxyUser", "xxx");
	    	System.setProperty("http.proxyUser", "xxx");																														System.setProperty("http.proxyPassword", "xxxxxxxxx");
	    	
	    	// noticia de la palabra...
	        String url = "http://www...";
	        print("Fetching %s...", url);
	        
	        // Scraping WWW you can only use for this newspaper web.
	        Document elmundoNewspaperHTML = Jsoup.connect(url).get();
	        System.out.println("TEXTO PARA ANALIZAR");
	        
	        Elements contents = elmundoNewspaperHTML.select("div[id=contenido]");
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
				
					if (!stemm.preExecutedStoppingWord(simpleSentence))
						System.out.println(" Preprocesado de la raíz : [" + 
								stemm.executeStemming(simpleSentence) + "]");
					
					//
					System.out.println("original: " + simpleSentence);
				}
			}
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
				 
		 		if (pos > 0 && pos < targetWordOfSentence.length()) {
		 			
					String pre = targetWordOfSentence.substring(0, pos);
					String post = targetWordOfSentence.substring(pos+1, targetWordOfSentence.length());
					sentence[wordPositionInSentence] = pre.concat(post);
					
				} else if (pos == 0) {
					
					sentence[wordPositionInSentence] = targetWordOfSentence.substring(1, targetWordOfSentence.length());
					
				} else if (pos == targetWordOfSentence.length()-1) {
					
					sentence[wordPositionInSentence] = targetWordOfSentence.substring(0, targetWordOfSentence.length()-1);
				}
			 }
		}
	 }
	 
	 /** */	 
	 private static void print(String msg, Object... args) {
	      System.out.println(String.format(msg, args));
	 }
}
