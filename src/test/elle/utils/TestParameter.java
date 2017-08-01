package elle.utils;

public class TestParameter {
	private String url;
	private String expectedTitle;
	private String expectedMetaOgTitle;

	public TestParameter(String url, String expectedTitle, String expectedMetaOgTitle) {
		super();
		this.url = url;
		this.expectedTitle = expectedTitle;
		this.expectedMetaOgTitle = expectedMetaOgTitle;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getExpectedTitle() {
		return expectedTitle;
	}
	public void setExpectedTitle(String expectedTitle) {
		this.expectedTitle = expectedTitle;
	}
	public String getExpectedMetaOgTitle() {
		return expectedMetaOgTitle;
	}
	public void setExpectedMetaOgTitle(String expectedMetaOgTitle) {
		this.expectedMetaOgTitle = expectedMetaOgTitle;
	}

}
