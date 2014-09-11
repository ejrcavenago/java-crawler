package crawler;

/**
 * The Main.class just accept as a parameter the start url, instantiate the WebCrawler class and call the Crawl function 
 * @author mcavenago
 *
 */
public class Main {

	public static void main(String[] args) {
		if (args != null && args.length > 0 && args[0] != null && args[0] instanceof String) {
			System.out.println("Starting url is " + args[0]);
			WebCrawler webCrawler = new WebCrawler();
			webCrawler.Crawl(args[0]);
		} else {
			System.out.println("The starting URL is missing");
		}
	}

}
