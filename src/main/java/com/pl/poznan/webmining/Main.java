package com.pl.poznan.webmining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		String search_query = rd.readLine(); //the query
		int nr_pages = Integer.parseInt(rd.readLine()); //total number of pages 
		
		String search_query_url = "";
		for (String val: search_query.split(" ")) 
			search_query_url += val + "+";
		search_query_url = search_query_url.substring(0, search_query_url.length()-1);

		int position_nr = 1; //paper position in the page
		for(int page_nr = 0; page_nr < nr_pages; page_nr++) { // page_nr is the current page
			String url = "https://scholar.google.com/scholar?start=" + page_nr + "0&q=" + search_query_url;
			System.out.println(url);
			System.out.println();
			
			Document doc = Jsoup.connect(url).get();
			if(doc != null) {
				Elements page = doc.select("div#gs_res_ccl_mid");

				//go through every paper on a single page
				for(Element paper : page.select("div.gs_ri")) {
					
					//Paper details variables 
					List<String> authorsList = new ArrayList<String>();
					String publication_venue = "";
					int publication_year = 0;
					String publication_publisher = ""; // the last information in the second line 
					int citationsNr = 0;
					String paperTitle = paper.select("h3.gs_rt a").text(); //title of the paper
					
					String details = paper.select("div.gs_a").text(); //second line 
					int index = 0;
					String authors = "";
					for (String detail: details.split(" - ")) {
						
						if(index == 0) { 
							authors = detail; //authors
							if(detail.endsWith("…")) //means that there is more authors 
								authors = detail.substring(0, detail.length()-1);
							for(String author: authors.split(", ")) {
								authorsList.add(author); //adding a single author to the list
							}
						}
							
						else if(index == 1) {							
							String[] publication_details = detail.split(", ");
							
							if( publication_details.length == 1 ) {
								publication_venue = "none";
								publication_year = Integer.parseInt(publication_details[0]);
							}
							else {
								publication_venue = publication_details[0];
								publication_year = Integer.parseInt(publication_details[1]);
							}
							
						/***	To remove the dots in the publication_venue, but if there is dots means that  
						 ***    there is more information about the publication_venue						   
						 
						 	if(publication_details[0].startsWith("…"))
								publication_venue = publication_venue.substring(2, publication_venue.length());
							
							if(publication_details[0].endsWith("…")) 
								publication_venue = publication_venue.substring(0, publication_venue.length()-2);	
						*/
						}
						
						else 
							publication_publisher = detail; //publisher
						index++;
				    }
					
					String citationsString = paper.selectFirst("div.gs_fl a:eq(2)").text();
					String[] citations = citationsString.split(" ");
					citationsNr = Integer.parseInt(citations[2]);
					
					//creates a paper in the system
					CitedPaper citedPaper = new CitedPaper(page_nr, position_nr, paperTitle, publication_venue, 
							publication_year, citationsNr, authorsList);

				/*** To test if it's getting the right data 
				 
					System.out.println("title: " + citedPaper.getPaperTitle() + "\n" + 
									"publication venue: " + citedPaper.getPublicationVenu() + "\n" + 
									"publication year: " + citedPaper.getDateOfPublication() + "\n" + 
									"number of citations: " + citedPaper.getNumOfCitations());
					System.out.print("authors: ");
					List<String> writers = citedPaper.getNamesOfAuthors();
					for (Iterator<String> iterator = writers.iterator(); iterator.hasNext();) {
						String element = (String) iterator.next();
						System.out.print(element + " ");
					}
					System.out.println("\n");
				*/
					position_nr++;
				}

			}
			System.out.println();
		}
	}

}
