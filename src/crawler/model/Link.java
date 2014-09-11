package crawler.model;

/**
 * This is an utility class useful to store an url with its father. 
 * 
 * @author mcavenago
 *
 */
public class Link {
	private String url;
	private String fatherUrl;
	private boolean parsed = false;
	
	public Link(){
		
	}
	
	public Link(String url, String fatherUrl){
		this.url = url;
		this.fatherUrl = fatherUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFatherUrl() {
		return fatherUrl;
	}

	public void setFatherUrl(String fatherUrl) {
		this.fatherUrl = fatherUrl;
	}

	public boolean isParsed() {
		return parsed;
	}

	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}
}
