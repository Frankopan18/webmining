package com.pl.poznan.webmining;

import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the desired query : ");
        String search_query = rd.readLine(); //the query
        System.out.println("Enter the number of pages wanted to be parsed : ");
        int nr_pages = Integer.parseInt(rd.readLine()); //total number of pages
        rd.close();

        String search_query_url = "";
        Map<String, String> authorLinks = new LinkedHashMap<>();
        for (String val : search_query.split(" "))
            search_query_url += val + "+";
        search_query_url = search_query_url.substring(0, search_query_url.length() - 1);

        int position_nr = 1; //paper position in the page
        for (int page_nr = 0; page_nr < nr_pages; page_nr++) { // page_nr is the current page
            String url = "https://scholar.google.com/scholar?start=" + page_nr + "0&q=" + search_query_url;
            //System.out.println(url);
            //System.out.println();

            Document doc = Jsoup.connect(url).get();
            if (doc != null) {
                Elements page = doc.select("div#gs_res_ccl_mid");

                //go through every paper on a single page
                Elements links = doc.select("div.gs_a  > a");
                // getting authors from the page and link to their homepage
                links.forEach(link -> authorLinks.put(link.text(), link.attr("abs:href")));
                for (Element paper : page.select("div.gs_ri")) {

                    //Paper details variables
                    List<String> authorsList = new ArrayList<>();
                    String publication_venue = "";
                    int publication_year = 0;
                    String publication_publisher = ""; // the last information in the second line
                    int citationsNr = 0;
                    String paperTitle = paper.select("h3.gs_rt a").text(); //title of the paper


                    String details = paper.select("div.gs_a").text(); //second line
                    int index = 0;
                    String authors = "";
                    for (String detail : details.split(" - ")) {


                    	// authors are here,always check for links to get additional information
						// if more than one author has hyperlinks then maybe only 1 is enough,but we will see..
						// authors can be list of strings,all additional information can be used to update cited papers..
                        if (index == 0) {
                            authors = detail; //authors
                            if (detail.endsWith("…")) //means that there is more authors and that we should correct it if it's possible to go author webpage
                                authors = detail.substring(0, detail.length() - 1);
                            for (String author : authors.split(", ")) {
                                authorsList.add(author); //adding a single author to the list
                            }
							for(String singleAuthor : authorsList){
								// could be an error,maybe it's just empty string("")
								if (authorLinks.get(singleAuthor) != null){
									// use jsoup to connect to his personal webpage
									// fix publication venue if possible by extracting additional metadata
									// get SVG graph statistics for number of citations
									// update all information for an author and list of authors for a citing paper
								}
							}
                        } else if (index == 1) {
                            String[] publication_details = detail.split(", ");

                            if (publication_details.length == 1) {
                                publication_venue = "none";
                                publication_year = Integer.parseInt(publication_details[0]);
                            } else {
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
                        } else
                            publication_publisher = detail; //publisher
                        index++;
                    }

                    String citationsString = paper.selectFirst("div.gs_fl a:eq(2)").text();
                    String[] citations = citationsString.split(" ");
                    citationsNr = Integer.parseInt(citations[2]);

                    //creates a paper in the system
                    CitedPaper citedPaper = new CitedPaper(page_nr, position_nr, paperTitle, publication_venue,
                            publication_year, citationsNr, authorsList);

                    // TODO
                    // Save CitedPaper to MongoDB
                  //  saveToDb(citedPaper);
                    // Try to retrieve it
                    // Try to Save exactly same one to the database to see if it will update it


//					System.out.println("title: " + citedPaper.getPaperTitle() + "\n" +
//									"publication venue: " + citedPaper.getPublicationVenu() + "\n" +
//									"publication year: " + citedPaper.getDateOfPublication() + "\n" +
//									"number of citations: " + citedPaper.getNumOfCitations());
//					System.out.print("authors: ");
//					List<String> writers = citedPaper.getNamesOfAuthors();
//					for (Iterator<String> iterator = writers.iterator(); iterator.hasNext();) {
//						String element = (String) iterator.next();
//						System.out.print(element + " ");
//					}
//					System.out.println("\n");

                    position_nr++;
                }
            }
            System.out.println();
            // new page now,so new authors...
            authorLinks.clear();
        }
    }

    private static void saveToDb(CitedPaper citedPaper) throws UnknownHostException {
        MongoClient mongo = new MongoClient();
        DB db = mongo.getDB("yourdb");
        DBCollection articles = db.getCollection("articles");
        Gson gson = new Gson();
        BasicDBObject obj = (BasicDBObject) JSON.parse(gson.toJson(citedPaper));
        articles.insert(obj);
    }

    public static void findCitedPaper(BasicDBObject query,DBCollection collection){

        DBCursor cursor = collection.find(query);

        try {
            while(cursor.hasNext()) {
                DBObject dbobj = cursor.next();
                //Converting BasicDBObject to a custom Class(Employee)
                CitedPaper paper = (new Gson()).fromJson(dbobj.toString(), CitedPaper.class);
                System.out.println(paper.toString());
            }
        } finally {
            cursor.close();
        }

    }

}
