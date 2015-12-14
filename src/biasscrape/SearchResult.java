/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biasscrape;

/**
 *
 * @author Motahhare Eslami <eslamim2@illinois.edu>
 */
public class SearchResult {

    String URL;
    String label;
    String short_desc;

    @Override
    public String toString() {
        return "[" + "\n"
                + "URL=" + URL + "\n"
                + "label=" + label + "\n"
                + "short_desc=" + short_desc + "\n"
                + "]";
    }

}
