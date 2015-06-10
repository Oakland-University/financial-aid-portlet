package org.apereo.finaid.mvc.portlet;

import java.util.Locale;

import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;

import org.jasig.portal.search.SearchConstants;
import org.jasig.portal.search.SearchRequest;
import org.jasig.portal.search.SearchResult;
import org.jasig.portal.search.SearchResults;
import org.jasig.portal.search.PortletUrl;
import org.jasig.portal.search.PortletUrlType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;

import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSource;

/**
 * @author Jen Bourey, jbourey@unicon.net
 * @version $Revision$
 */
@Controller
@RequestMapping("VIEW")
public class SearchContentController implements MessageSourceAware {

    protected MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @EventMapping(SearchConstants.SEARCH_REQUEST_QNAME_STRING)
        public void searchContent(EventRequest request, EventResponse response) {
            final Event event = request.getEvent();
            final SearchRequest searchQuery = (SearchRequest)event.getValue();

            //Fetch data configured in messages.properties
            final String searchKeywords = messageSource.getMessage("search.keywords",null,Locale.getDefault());
            final String portletTitle = messageSource.getMessage("portlet.title",null,Locale.getDefault());
            final String portletSummary = messageSource.getMessage("portlet.summary",null,Locale.getDefault());

            //Content string that will be included in the search
            final String[] keywords = searchKeywords.split(" ");
            final String[] searchTerms = searchQuery.getSearchTerms().split(" ");

            for (final String term : searchTerms) {
                for (final String keyword : keywords) {
                    if (getDistance(keyword, term) < getLengthLimit(keyword)){

                        //matched, create results object and copy over the query id
                        final SearchResults searchResults = new SearchResults();
                        searchResults.setQueryId(searchQuery.getQueryId());
                        searchResults.setWindowId(request.getWindowID());

                        //Build the result object for the match
                        final SearchResult searchResult = new SearchResult();

                        searchResult.setTitle(portletTitle);
                        searchResult.setSummary(portletSummary);

                        //URL options to make portlet display correctly
                        PortletUrl url = new PortletUrl();
                        url.setType(PortletUrlType.RENDER);
                        url.setPortletMode("VIEW");
                        url.setWindowState("maximized");
                        searchResult.setPortletUrl(url);

                        //Add the result to the results and send the event
                        searchResults.getSearchResult().add(searchResult);
                        response.setEvent(SearchConstants.SEARCH_RESULTS_QNAME, searchResults);

                        //Stop processing
                        return;
                    }
                }
            }
        }

    //Determines the Levenshtein distance between a searched word and a keyword
    public static int getDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    //Determines how many characters can be different depending on word length
    public static int getLengthLimit(String s1) {

        int length = s1.length();

        if (length > 5)
            return 3;
        else if (length > 3)
            return 2;
        else
            return 1;
    }
}
