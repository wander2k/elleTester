package elle.elleTester;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import elle.utils.TestParameter;

@RunWith(Parameterized.class)
public class MetaTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	static Logger logger = LogManager.getLogger(MetaTest.class);

	@Parameters
	public static Collection<TestParameter> data() {
		return Arrays.asList(new TestParameter("/horoscope/pick", "", ""),
				new TestParameter("/horoscope/daily", "", "今日の星占い"),
				new TestParameter("/horoscope/daily/(star)/aries", "", "【ELLE】おひつじ座｜今日の星占い｜エル・オンライン"),
				new TestParameter("/horoscope/dailytest/17_0724", "", ""),
				new TestParameter("/horoscope/fs", "", ""),
				new TestParameter("/horoscope/happyfortune", "", ""),
				new TestParameter("/gourmet/pick", "", ""),
				new TestParameter("/gourmet/pick/saladcollection_17_0401", "", ""),
				new TestParameter("/gourmet/restaurant/restaurantdb", "", ""),
				new TestParameter("/gourmet/restaurant/magazine", "", ""),
				new TestParameter("/gourmet/restaurant/choice/rankingrestaurant_17_0705", "", ""),
				new TestParameter("/gourmet/cooking", "【ELLE gourmet】料理レシピ検索｜エル・オンライン", ""),
				new TestParameter("/gourmet/cooking/list", "", ""),
				new TestParameter("/gourmet/cooking/author", "", ""),
				new TestParameter("/gourmet/cooking/magazine", "", ""),
				new TestParameter("/gourmet/cooking/recipe/3283", "", ""),
				new TestParameter("/gourmet/foodiesclub/profile/latelier_del", "", ""),
				new TestParameter("/catwalk", "", ""),
				new TestParameter("/catwalk/fw201", "", ""));
	};

	@Parameter
	public /* NOT private */ TestParameter input;

	private void prepareChromeDriver() {
		System.setProperty("webdriver.chrome.driver", "exe\\chromedriver.exe");
		driver = new ChromeDriver();
	}

	private void prepareHTMLUnitDriver() {
		driver = new HtmlUnitDriver();
	}


	@Before
	public void setUp() throws Exception {
		// System.setProperty("webdriver.ie.driver",
		// "./exe/IEDriverServer.exe");
		//System.setProperty("webdriver.gecko.driver", "exe\\geckodriver.exe");
		// driver = new InternetExplorerDriver();
		//driver = new FirefoxDriver();
		//driver = new HtmlUnitDriver();
		//prepareChromeDriver();
		prepareHTMLUnitDriver();
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(414, 768));
		baseUrl = "http://adguest:samplecheck@m.dev.elle.co.jp";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testTitleAndMetaTags() throws Exception {
		String url = input.getUrl();
		driver.get(baseUrl + url);
		testTitle(input);
		testMetaTagByProperty(url, "og:title", input.getExpectedMetaOgTitle());
		testMetaTagByProperty(url, "og:description", null);
		outputMetaTagByName(url, "DESCRIPTION");
	}

	private void outputMetaTagByName(String url, String metaName) {
		String xPathBase = "//meta[@name='%s']";

		String xPathTitle = String.format(xPathBase, metaName);

		if (!isElementPresent(By.xpath(xPathTitle))) {
			// Assert.fail("メタタグが存在しない：" + metaName);
			logger.info("メタタグが存在しない：name=" + metaName);
		} else {
			WebElement ele = driver.findElement(By.xpath(xPathTitle));
			String content = ele.getAttribute("content");
			logger.info(url + ":meta[name=" + metaName + "]=" + content);
		}

	}

	private void testMetaTagByProperty(String url, String metaProperty, String expectedContent) {
		String xPathBase = "//meta[@property='%s']";

		String xPathTitle = String.format(xPathBase, metaProperty);

		if (!isElementPresent(By.xpath(xPathTitle))) {
			// Assert.fail("メタタグが存在しない：" + metaName);
			logger.info("メタタグが存在しない：property=" + metaProperty);
		} else {
			WebElement ele = driver.findElement(By.xpath(xPathTitle));
			String content = ele.getAttribute("content");
			logger.info(url + ":meta[property=" + metaProperty + "]=" + content);
			if (expectedContent != null && expectedContent.trim().length() > 0) {
				Assert.assertEquals("metaProperty:[" + metaProperty + "]の確認。",  expectedContent, content);
			}
		}

	}

	private void testTitle(TestParameter param) {
		String title = driver.getTitle();
		String expectedTitle = param.getExpectedTitle();
		logger.info(param.getUrl() + ":title=" + title);

		if (expectedTitle != null && expectedTitle.trim().length() > 0) {
			Assert.assertEquals("タイトル]の確認。",expectedTitle, title);
		}

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
