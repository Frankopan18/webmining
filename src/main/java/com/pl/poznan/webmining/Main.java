package com.pl.poznan.webmining;

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
		String searchIn = rd.readLine();
		int npages = Integer.parseInt(rd.readLine());
		
		String searchOut = "";
		
		for (String val: searchIn.split(" ")) {
			searchOut += val + "+";
	    }
		searchOut = searchOut.substring(0, searchOut.length()-1);

		
		int p = 1;
		for(int i = 0; i<npages; i++) {
			String url = "https://scholar.google.com/scholar?start=" + i + "0&q=" + searchOut;
			System.out.println(url);
			System.out.println();
			Document d = Jsoup.connect(url).get();
			if(d != null) {
				Elements elements = d.select("div#gs_res_ccl_mid");

				
				for(Element element : elements.select("div.gs_ri")) {
					
					String title = element.select("h3.gs_rt a").text(); 
					System.out.println("title " + p + ": " + title);
					
					
					String extra = element.select("div.gs_a").text();
					System.out.println("extra " + p + ": " + extra);
					
					String description = element.select("div.gs_rs").text();
					System.out.println("description " + p + ": " + description);
					
					String citations = element.selectFirst("div.gs_fl a:eq(2)").text();
					System.out.println("citations " + p + ": " + citations);
					
					System.out.println();
					p++;
				}

			}
			System.out.println();

		}
		

		
		

	}

}
