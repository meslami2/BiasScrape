/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebiasscrape;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Motahhare Eslami <eslamim2@illinois.edu>
 */
public class ScrapeIt{// implements Job{

    /**
     * @param args the command line arguments
     */
    private static String createURL(String query) {
        query = query.replace(" ", "+");
        String url = "https://www.google.com/search?q=" + query;
        return url;
    }

    public List<SearchResult> extractSearchResults(Document doc) {
        List<SearchResult> results = new ArrayList<>();
        Elements blocks = doc.select("li.g");
        for (Element block : blocks) {
            String url = block.select(".r > a").attr("href");
            String label = block.select(".r > a").text();
            String short_desc = block.select(".st").text();

            SearchResult result = new SearchResult();
            result.URL = url;
            result.label = label;
            result.short_desc = short_desc;
            results.add(result);
        }

        return results;

    }

    public Document getPageDoc(String query) {
        String url = createURL(query);
        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent(
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(0).get();
            String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            String dataFile = "data/" + query + "_" + currentTime +".html";
            IOUtils.writeDataIntoFile(doc.toString(), dataFile);
            // System.out.println(doc.toString());
            return doc;

        } catch (IOException ex) {
            Logger.getLogger(ScrapeIt.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("errorrrrr");
        }
        return null;
    }

    public void scrape(String query) {
        //numberofpages is not currently used but if we decided to extend the results to two or more pages, will be used!
        Document doc = getPageDoc(query);
        List<SearchResult> results = extractSearchResults(doc);
        String currentTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        String dataFile = "data/" + query + "_info_" + currentTime;
        IOUtils.writeDataIntoFile(results.toString(), dataFile);
        //System.out.println("SearchResults = " + results);
    }
    
    public static void main(String[] args) throws InterruptedException {
        ScrapeIt sc = new ScrapeIt();
        sc.scrape(args[0]);
    }
    
    /*public void execute(JobExecutionContext context)
            throws JobExecutionException {

        String query = "democratic debate";
        scrape(query, 1);

    }*/

    /*public static void main(String[] args) throws InterruptedException {
         List<String> listofQueries = new ArrayList<>();
        
        listofQueries.add("democratic debate");
        listofQueries.add("dem debate");
        listofQueries.add("republican debate");
        listofQueries.add("rep debate");
        listofQueries.add("democratic debate");
        
        listofQueries.add("Bernie Sanders");
        listofQueries.add("Martin O'Malley");
        listofQueries.add("Hillary Clinton");
          
        listofQueries.add("Jeb Bush");
        listofQueries.add("Ben Carson");
        listofQueries.add("Chris Christie");
        listofQueries.add("Ted Cruz");
        listofQueries.add("Carly Fiorina");
        listofQueries.add("Jim Gilmore");
        listofQueries.add("Lindsey Graham");
        listofQueries.add("Mike Huckabee");
        listofQueries.add("John Kasich");
        listofQueries.add("George Pataki");
        listofQueries.add("Rand Paul");
        listofQueries.add("Marco Rubio");
        listofQueries.add("Rick Santorum");
        listofQueries.add("Donald Trump");
        
        ScrapeIt sc = new ScrapeIt();
        for (int i = 0; i < listofQueries.size(); i++) {
            sc.scrape(listofQueries.get(i), 2);
           // System.out.println("salam");
            Thread.sleep(300000);
        }
    }*/
}
