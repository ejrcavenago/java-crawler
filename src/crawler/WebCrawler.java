package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crawler.model.Link;

/**
 * The WebCrawler class fetches the links in the start link page and add them to
 * a Map<String, Link>, with the Link url as a key. If an url isn't already in
 * the map it is added, otherwise the next url will be analyzed.
 * 
 * @author mcavenago
 * 
 */
public class WebCrawler {
	// How many links will be stored?
	private static final int LINKS_LIMIT = 1000;
	// Regex to find the 'a' tag
	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	// Regex to find the content of the 'href' attribute
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	// Map with the different founded links
	private Map<String, Link> linksMap;
	// Buffer of links still to be parsed
	private List<Link> links;

	private Pattern patternTag, patternLink;
	private Matcher matcherTag, matcherLink;

	public WebCrawler() {
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}

	public void Crawl(String start) {
		linksMap = new HashMap<String, Link>();
		links = new ArrayList<Link>();
		// Put the start url into the map and into the array
		Link l = new Link(start, null);
		linksMap.put(l.getUrl(), l);
		links.add(l);
		// Cycle and crawl link untill the limit is reached or there aren't
		// further links to analyze
		do {
			// Check always the first element of the array
			crawlLink(links.get(0));
			// Every Link in the Array must be also in the Map
			linksMap.get(links.get(0).getUrl()).setParsed(true);
			// Remove the first element of the array, already checked
			links.remove(0);
		} while (linksMap.size() < LINKS_LIMIT && links.size() > 0);
		System.out.println("End of crawling. " + linksMap.size() + " links found");
	}

	private void crawlLink(Link link) {
		try {
			System.out.println("Checking the url " + link.getUrl());
			// Fetch the url content
			URL url = new URL(link.getUrl());
			URLConnection uc = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			for (String line; (line = br.readLine()) != null;) {
				// System.out.println("Line: " + line);
				matcherTag = patternTag.matcher(line);
				while (matcherTag.find()) {
					String href = matcherTag.group(1); // href
					matcherLink = patternLink.matcher(href);
					while (matcherLink.find()) {
						String actuallink = matcherLink.group(1).replace("\"", ""); // link
						// Here there should be other controls in order to manage relative paths, change of behavior and so on 
						if (actuallink.startsWith("/")) {
							// If its a relative path add the context
							actuallink = url.getProtocol() + "://" + url.getHost() + actuallink;
						}
						if (actuallink.startsWith("?")) {
							// If its a parameter path add all the url
							actuallink = url.toString() + actuallink;
						}
						// If the link isn't stored yet
						if (!linksMap.containsKey(actuallink)) {
							System.out.println("Link found " + actuallink);
							Link newLink = new Link(actuallink, link.getUrl());
							linksMap.put(actuallink, newLink);
							links.add(newLink);
							// Stop parsing if the limit is already reached
							if (linksMap.size() == LINKS_LIMIT) {
								return;
							}
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("The url " + link.getUrl() + " has thrown a MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("The url " + link.getUrl() + " has thrown a IOException");
			e.printStackTrace();
		} finally {
			System.out.println("End the check of url " + link.getUrl());
		}
	}

}
