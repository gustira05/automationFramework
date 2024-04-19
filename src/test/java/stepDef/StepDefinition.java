package stepDef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.testng.AssertJUnit;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.vimalselvam.graphql.GraphqlTemplate;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StepDefinition extends SupportMethod{
	protected static WebDriver driver;
	String addURL;
	HttpHeaders headers;
	HttpEntity<String> entity;
	private String responseBody;
	public String responseBodyPOST;
	public String responseStatusCode;
	RestTemplate restTemplate = new RestTemplate();
	JSONObject jsonResponseObject = null;
	JSONParser parser = new JSONParser();
	private static final OkHttpClient client = new OkHttpClient();
	String sessionID;

	//============= Keyword Open Website with browser ================//
	@Given(".*?open.*? \"(.*?)\" .* \"(.*?)\"$")
	public void openBrowserAndNavigateTo(String url, String browser) {
		if(System.getProperty("os.name").contains("Mac")) {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Driver/macOS/geckodriver");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				driver = new FirefoxDriver();
				driver.get(url);
			}else {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/Driver/macOS/chromedriver");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
//				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--ignore-certificate-errors");
				chromeOptions.addArguments("--ignore-ssl-errors=yes");
				chromeOptions.addArguments("--window-size=1325x744");
				driver = new ChromeDriver(chromeOptions);
				driver.get(url);
			}
		}else if (System.getProperty("os.name").contains("Win")){
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Driver/windows/geckodriver.exe");
				driver = new FirefoxDriver();
				driver.get(url);
			}else {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/Driver/Windows/chromedriver.exe");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				chromeOptions.addArguments("--ignore-certificate-errors");
				chromeOptions.addArguments("--ignore-ssl-errors=yes");
				chromeOptions.addArguments("--window-size=1325x744");
				chromeOptions.addArguments("--headless");
				driver = new ChromeDriver(chromeOptions);	
				driver.get(url);
			}
		}else {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				driver = new FirefoxDriver();
				driver.get(url);
			}else {
				System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--window-size=1325x744");
				driver = new ChromeDriver(chromeOptions);
				driver.get(url);
			}
		}		
	}
	//===================================================================//

	//========================= Keyword Clear Text =========================//
	/** Keyword for clear string in text area 
	 * @param selector text area in HTML placed in Features 		
	 **/
	@When(".*?clear text.*? \"(.*?)\".*? by ID$")
	public void clearTextById(String selector) {
		WebElement element = driver.findElement(By.id(selector));
		element.clear();
	}

	@When(".*?clear text.*? \"(.*?)\".*? by name$")
	public void clearTextByName(String selector) {
		WebElement element = driver.findElement(By.name(selector));
		element.clear();
	}

	@When(".*?clear text.*? \"(.*?)\".*? by text$")
	public void clearTextByText(String selector) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+selector+"']"));
		element.clear();
	}

	@When(".*?clear text.*? \"(.*?)\".*? by cssSelector$")
	public void clearTextByCssSelector(String selector) {
		WebElement element = driver.findElement(By.cssSelector(selector));
		element.clear();
	}

	@When(".*?clear text.*? \"(.*?)\".*? by xpath$")
	public void clearTextByXpath(String selector) {
		WebElement element = driver.findElement(By.xpath(selector));
		element.clear();
	}
	//========================================================================//

	//================= Keyword Double Click ============================//
	@When(".*?double click.*? \"(.*?)\".*? by ID$")
	public void doubleClickElementById(String selector) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id(selector));
		action.doubleClick(element).perform();;
	}

	@When(".*?double click.*? \"(.*?)\".*? by text$")
	public void doubleClickElementByText(String selector) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+selector+"']"));
		action.doubleClick(element).perform();;
	}

	@When(".*?double click.*? \"(.*?)\".*? by name$")
	public void doubleClickElementByName(String selector) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.name(selector));
		action.doubleClick(element).perform();;
	}

	@When(".*?double click.*? \"(.*?)\".*? by cssSelector$")
	public void doubleClickElementByCssSelector(String selector) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(selector));
		action.doubleClick(element).perform();;
	}

	@When(".*?double click.*? \"(.*?)\".*? by xpath$")
	public void doubleClickElementByXpath(String selector) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(selector));
		action.doubleClick(element).perform();;
	}
	//===================================================================//


	//========================== Keyword Forward ========================//
	@And(".*?press forward browser$")
	public void pressForwardBrowser() {
		driver.navigate().forward();
	}
	//===================================================================//	

	//========================== Keyword Click ==========================//
	@When(".*?click.*? \"(.*?)\".*? by ID$")
	public void clickElementById(String selector) {
		WebElement element = driver.findElement(By.id(selector));
		element.click();
	}

	@When(".*?click.*? \"(.*?)\".*? by name$")
	public void clickElementByName(String selector) {
		WebElement element = driver.findElement(By.name(selector));
		element.click();
	}

	@When(".*?click.*? \"(.*?)\".*? by css selector$")
	public void clickElementByCssSelector(String selector) {
		WebElement element = driver.findElement(By.cssSelector(selector));
		element.click();
	}
	@When(".*?click.*? \"(.*?)\".*? by text$")
	public void clickElementByText(String selector) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+selector+"']"));
		element.click();
	}
	@When(".*?click.*? \"(.*?)\".*? by xpath$")
	public void clickElementByXpath(String selector) {
		WebElement element = driver.findElement(By.xpath(selector));
		element.click();
	}
	//===================================================================//

	//========================= Keyword Delay =========================//
	@And(".*?set delay.*?(\\d+).*?seconds$")
	public void delay(long seconds) {
		waitFor(seconds);
	}
	@And(".*?wait for.*?(\\d+).*?seconds$")
	public void waitInSecond(long seconds) {
		waitFor(seconds);
	}
	//=================================================================//

	//========================= Keyword Alert =========================//
	@And(".*?dismiss alert.*?$")
	public void dismissAlert() {
		driver.switchTo().alert().dismiss();
	}
	@And(".*?accept alert.*?$")
	public void acceptAlert() {
		driver.switchTo().alert().accept();
	}
	//=================================================================//

	//========================= Keyword Press back browser =========================//
	@And(".*?press back browser$")
	public void pressBackBrowser() {
		driver.navigate().back();
	}
	//===============================================================================//

	//========================= Keyword Get Alert Text =========================//
	@And(".*? get alert text$")
	public void getAlertText() {
		String text = driver.switchTo().alert().getText();
		System.out.println("Alert text is "+text);
	}
	//==========================================================================//

	//========================= Keyword Get All Links On Current Page =========================//
	@And(".*? get all links on current page$")
	public void getAllLinksFromCurrentPage() {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println("Total links are "+links.size());
	}
	//=========================================================================================//

	//========================= Keyword Get Attribute =========================//
	@And(".*? get attribute \"(.*?)\" from \"(.*?)\" by css selector$")
	public void getAttributeByCssSelector(String attributeName, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		System.out.println("Value from attribute "+attributeName+" is "+element.getAttribute(attributeName));
	}
	@And(".*? get attribute \"(.*?)\" from \"(.*?)\" by ID$")
	public void getAttributeByID(String attributeName, String locator) {
		WebElement element = driver.findElement(By.id(locator));
		System.out.println("Value from attribute "+attributeName+" is "+element.getAttribute(attributeName));
	}
	@And(".*? get attribute \"(.*?)\" from \"(.*?)\" by name$")
	public void getAttributeByName(String attributeName, String locator) {
		WebElement element = driver.findElement(By.name(locator));
		System.out.println("Value from attribute "+attributeName+" is "+element.getAttribute(attributeName));
	}
	@And(".*? get attribute \"(.*?)\" from \"(.*?)\" by text$")
	public void getAttributeByText(String attributeName, String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		System.out.println("Value from attribute "+attributeName+" is "+element.getAttribute(attributeName));
	}
	@And(".*? get attribute \"(.*?)\" from \"(.*?)\" by xpath$")
	public void getAttributeByXpath(String attributeName, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		System.out.println("Value from attribute "+attributeName+" is "+element.getAttribute(attributeName));
	}
	//=========================================================================//

	//========================= Keyword Get CSS Value =========================//
	@And(".*? get css value \"(.*?)\" by css selector$")
	public void getCssValueByCssSelector(String locator, String propertyName) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		System.out.println("Element CSS Value from "+propertyName+" is "+element.getCssValue(propertyName));
	}
	@And(".*? get css value \"(.*?)\" by ID$")
	public void getCssValueByID(String locator, String propertyName) {
		WebElement element = driver.findElement(By.id(locator));
		System.out.println("Element CSS Value from "+propertyName+" is "+element.getCssValue(propertyName));
	}
	@And(".*? get css value \"(.*?)\" by name$")
	public void getCssValueByName(String locator, String propertyName) {
		WebElement element = driver.findElement(By.name(locator));
		System.out.println("Element CSS Value from "+propertyName+" is "+element.getCssValue(propertyName));
	}
	@And(".*? get css value \"(.*?)\" by text$")
	public void getCssValueByText(String locator, String propertyName) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		System.out.println("Element CSS Value from "+propertyName+" is "+element.getCssValue(propertyName));
	}
	@And(".*? get css value \"(.*?)\" by xpath$")
	public void getCssValueByXpath(String locator, String propertyName) {
		WebElement element = driver.findElement(By.xpath(locator));
		System.out.println("Element CSS Value from "+propertyName+" is "+element.getCssValue(propertyName));
	}
	//=========================================================================//

	//========================= Keyword Get Element Height =========================//
	@And(".*? get element height \"(.*?)\" by css selector$")
	public void getElementHeightByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element height is "+initial_size.getHeight());
	}
	@And(".*? get element height \"(.*?)\" by ID$")
	public void getElementHeightByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element height is "+initial_size.getHeight());
	}
	@And(".*? get element height \"(.*?)\" by name$")
	public void getElementHeightByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element height is "+initial_size.getHeight());
	}
	@And(".*? get element height \"(.*?)\" by text$")
	public void getElementHeightByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Dimension initial_size = element.getSize();
		System.out.println("Element height is "+initial_size.getHeight());
	}
	@And(".*? get element height \"(.*?)\" by xpath$")
	public void getElementHeightByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element height is "+initial_size.getHeight());
	}
	//==============================================================================//

	//========================= Keyword Get Element Left Position =========================//
	@And(".*? get element left position \"(.*?)\" by css selector$")
	public void getElementLeftPositionByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Point viewPortLocation = ((Locatable) element).getCoordinates().onScreen();
		System.out.println("Element left position is "+viewPortLocation.getX());
	}
	@And(".*? get element left position \"(.*?)\" by ID$")
	public void getElementLeftPositionByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		Point viewPortLocation = ((Locatable) element).getCoordinates().onScreen();
		System.out.println("Element left position is "+viewPortLocation.getX());
	}
	@And(".*? get element left position \"(.*?)\" by name$")
	public void getElementLeftPositionByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		Point viewPortLocation = ((Locatable) element).getCoordinates().onScreen();
		System.out.println("Element left position is "+viewPortLocation.getX());
	}
	@And(".*? get element left position \"(.*?)\" by text$")
	public void getElementLeftPositionByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Point viewPortLocation = ((Locatable) element).getCoordinates().onScreen();
		System.out.println("Element left position is "+viewPortLocation.getX());
	}
	@And(".*? get element left position \"(.*?)\" by xpath$")
	public void getElementLeftPositionByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		Point viewPortLocation = ((Locatable) element).getCoordinates().onScreen();
		System.out.println("Element left position is "+viewPortLocation.getX());
	}
	//=====================================================================================//

	//========================= Keyword Get Element Width =========================//
	@And(".*? get element width \"(.*?)\" by css selector$")
	public void getElementWidthByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element width is "+initial_size.getWidth());
	}
	@And(".*? get element width \"(.*?)\" by ID$")
	public void getElementWidthByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element width is "+initial_size.getWidth());
	}
	@And(".*? get element width \"(.*?)\" by name$")
	public void getElementWidthByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element width is "+initial_size.getWidth());
	}
	@And(".*? get element width \"(.*?)\" by text$")
	public void getElementWidthByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Dimension initial_size = element.getSize();
		System.out.println("Element width is "+initial_size.getWidth());
	}
	@And(".*? get element width \"(.*?)\" by xpath$")
	public void getElementWidthByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		Dimension initial_size = element.getSize();
		System.out.println("Element width is "+initial_size.getWidth());
	}
	//=============================================================================//

	//========================= Keyword Get Number Of Selected Option =========================//
	@And(".*?get number of selected option \"(.*?)\" by css selector$")
	public void getNumberOfSelectedOptionByCssSelector(String locator) {
		Select option = new Select(driver.findElement(By.cssSelector(locator)));
		List <WebElement> elementCount = option.getAllSelectedOptions();
		System.out.println("Total selected option is "+elementCount.size());
	}
	@And(".*?get number of selected option \"(.*?)\" by ID$")
	public void getNumberOfSelectedOptionByID(String locator) {
		Select option = new Select(driver.findElement(By.id(locator)));
		List <WebElement> elementCount = option.getAllSelectedOptions();
		System.out.println("Total selected option is "+elementCount.size());
	}
	@And(".*?get number of selected option \"(.*?)\" by name$")
	public void getNumberOfSelectedOptionByName(String locator) {
		Select option = new Select(driver.findElement(By.name(locator)));
		List <WebElement> elementCount = option.getAllSelectedOptions();
		System.out.println("Total selected option is "+elementCount.size());
	}
	@And(".*?get number of selected option \"(.*?)\" by text$")
	public void getNumberOfSelectedOptionByText(String locator) {
		Select option = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		List <WebElement> elementCount = option.getAllSelectedOptions();
		System.out.println("Total selected option is "+elementCount.size());
	}
	@And(".*?get number of selected option \"(.*?)\" by xpath$")
	public void getNumberOfSelectedOptionByXpath(String locator) {
		Select option = new Select(driver.findElement(By.xpath(locator)));
		List <WebElement> elementCount = option.getAllSelectedOptions();
		System.out.println("Total selected option is "+elementCount.size());
	}
	//=========================================================================================//

	//========================= Keyword Get Number Of Total Option =========================//
	@And(".*?get number of total option \"(.*?)\" by css selector$")
	public void getNumberOfTotalOptionByCssSelector(String locator) {
		Select option = new Select(driver.findElement(By.cssSelector(locator)));
		List <WebElement> elementCount = option.getOptions();
		System.out.println("Total option is "+elementCount.size());
	}
	@And(".*?get number of total option \"(.*?)\" by ID$")
	public void getNumberOfTotalOptionByID(String locator) {
		Select option = new Select(driver.findElement(By.id(locator)));
		List <WebElement> elementCount = option.getOptions();
		System.out.println("Total option is "+elementCount.size());
	}
	@And(".*?get number of total option \"(.*?)\" by name$")
	public void getNumberOfTotalOptionByName(String locator) {
		Select option = new Select(driver.findElement(By.name(locator)));
		List <WebElement> elementCount = option.getOptions();
		System.out.println("Total option is "+elementCount.size());
	}
	@And(".*?get number of total option \"(.*?)\" by text$")
	public void getNumberOfTotalOptionByText(String locator) {
		Select option = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		List <WebElement> elementCount = option.getOptions();
		System.out.println("Total option is "+elementCount.size());
	}
	@And(".*?get number of total option \"(.*?)\" by xpath$")
	public void getNumberOfTotalOptionByXpath(String locator) {
		Select option = new Select(driver.findElement(By.xpath(locator)));
		List <WebElement> elementCount = option.getOptions();
		System.out.println("Total option is "+elementCount.size());
	}
	//======================================================================================//

	//========================= Keyword Get Page Height =========================//
	@And(".*?get page height$")
	public void getPageHeight() {
		System.out.println("Page height is "+getHeight(driver));
	}
	//===========================================================================//

	//========================= Keyword Get Page Width =========================//
	@And(".*?get page width$")
	public void getPageWidth() {
		System.out.println("Page width is "+getWidth(driver));
	}
	//==========================================================================//

	//========================= Keyword Get Text =========================//
	@And(".*?get text \"(.*?)\" by css selector$")
	public void getTextByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		System.out.println("Element text is "+getTextFromElement(element));
	}
	@And(".*?get text \"(.*?)\" by ID$")
	public void getTextByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		System.out.println("Element text is "+getTextFromElement(element));
	}
	@And(".*?get text \"(.*?)\" by name$")
	public void getTextByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		System.out.println("Element text is "+getTextFromElement(element));
	}
	@And(".*?get text \"(.*?)\" by text$")
	public void getTextByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		System.out.println("Element text is "+getTextFromElement(element));
	}
	@And(".*?get text \"(.*?)\" by xpath$")
	public void getTextByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		System.out.println("Element text is "+getTextFromElement(element));
	}
	//====================================================================//

	//========================= Keyword Get Url =========================//
	@And(".*?get url$")
	public void getUrl() {
		System.out.println("Current URL is "+getUrlFromCurrentWindow(driver));
	}
	//===================================================================//

	//========================= Keyword Get Viewport Height =========================//
	@And(".*?get viewport height$")
	public void getViewportHeight() {
		System.out.println("Viewport height is "+getHeight(driver));
	}
	//===============================================================================//

	//========================= Keyword Get Viewport Left Position =========================//
	@And(".*?get viewport left position$")
	public void getViewportLeftPosition() {
		System.out.println("Viewport left position is "+getLeftPosotion(driver));
	}
	//======================================================================================//

	//========================= Keyword Get Viewport Top Position =========================//
	@And(".*?get viewport top position$")
	public void getViewportTopPosition() {
		System.out.println("Viewport top position is "+getTopPosotion(driver));
	}
	//=====================================================================================//

	//========================= Keyword Get Viewport Width =========================//
	@And(".*?get viewport width$")
	public void getViewportWidth() {
		System.out.println("Viewport width is "+getWidth(driver));
	}
	//==============================================================================//

	//========================= Keyword Get Window Index =========================//
	@And(".*?get window index$")
	public void getWindowIndex() {
		System.out.println("Window Index is "+getIndexWindow(driver));
	}
	//============================================================================//

	//========================= Keyword Get Window Title =========================//
	@And(".*?get window title$")
	public void getWindowTitle() {
		System.out.println("Window Title is "+getTitleWindow(driver));
	}
	//============================================================================//

	//========================= Keyword Maximized Window =========================//
	@And(".*?maximized window$")
	public void maximizedWindow() {
		driver.manage().window().maximize();
	}
	//============================================================================//

	//========================= Keyword Mouse Over =========================//
	@And(".*?mouse over \"(.*?)\" by css selector$")
	public void mouseOverByCssSelector(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(locator));
		action.moveToElement(element);
	}
	@And(".*?mouse over \"(.*?)\" by ID$")
	public void mouseOverByID(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id(locator));
		action.moveToElement(element);
	}
	@And(".*?mouse over \"(.*?)\" by name$")
	public void mouseOverByName(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.name(locator));
		action.moveToElement(element);
	}
	@And(".*?mouse over \"(.*?)\" by text$")
	public void mouseOverByText(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		action.moveToElement(element);
	}
	@And(".*?mouse over \"(.*?)\" by xpath$")
	public void mouseOverByXpath(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(locator));
		action.moveToElement(element);
	}
	//======================================================================//

	//========================= Keyword Mouse Over Offset =========================//
	@And(".*?mouse over offset (\\d+) and (\\d+)$")
	public void mouseOverOffsetByCssSelector(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).perform();
	}
	@And(".*?mouse over offset \"(.*?)\" with (\\d+) and (\\d+) by ID$")
	public void mouseOverOffsetByID(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).perform();
	}
	@And(".*?mouse over offset \"(.*?)\" with (\\d+) and (\\d+) by name$")
	public void mouseOverOffsetByName(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.name(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).perform();
	}
	@And(".*?mouse over offset \"(.*?)\" with (\\d+) and (\\d+) by text$")
	public void mouseOverOffsetByText(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		action.moveToElement(element);
		action.moveByOffset(x, y).perform();
	}
	@And(".*?mouse over offset \"(.*?)\" with (\\d+) and (\\d+) by xpath$")
	public void mouseOverOffsetByXpath(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).perform();
	}
	//=============================================================================//

	//========================= Keyword Navigate To Url =========================//
	@And(".*?navigate to url \"(.*?)\"$")
	public void navigateToUrl(String url) {
		driver.get(url);
	}
	//===========================================================================//

	//========================= Keyword Open Browser =========================//
	@And(".*? open browser \"(.*?)\"$")
	public void openBrowser(String browser) {
		if(System.getProperty("os.name").contains("Mac")) {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/Users/18055967/AutomationFrameworkBDD/WebUI/driver/macOS/geckodriver");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				driver = new FirefoxDriver();			
			}else {
				System.setProperty("webdriver.chrome.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/macOS/chromedriver");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--ignore-certificate-errors");
				chromeOptions.addArguments("--ignore-ssl-errors=yes");
				chromeOptions.addArguments("--window-size=1325x744");
				chromeOptions.addArguments("--silent");
				driver = new ChromeDriver(chromeOptions);				
			}
		}else if (System.getProperty("os.name").contains("Win")){
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/windows/geckodriver.exe");
				driver = new FirefoxDriver();				
			}else {
				System.setProperty("webdriver.chrome.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/Windows/chromedriver.exe");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				driver = new ChromeDriver(chromeOptions);				
			}
		}else {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				driver = new FirefoxDriver();
			}else {
				System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				chromeOptions.addArguments("--headless");
				chromeOptions.addArguments("--window-size=1325x744");
				driver = new ChromeDriver(chromeOptions);
			}
		}
	}
	//========================================================================//

	//========================= Keyword Refresh =========================//
	@And(".*?refresh.*?$")
	public void refreshBrowser() {
		driver.navigate().refresh();
	}
	//===================================================================//

	//========================= Keyword Right Click =========================//
	@And(".*?right click \"(.*?)\" by css selector$")
	public void rightClickByCssSelector(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(locator));
		action.contextClick(element);
	}
	@And(".*?right click \"(.*?)\" by ID$")
	public void rightClickByID(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id(locator));
		action.contextClick(element);
	}
	@And(".*?right click \"(.*?)\" by name$")
	public void rightClickByName(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.name(locator));
		action.contextClick(element);
	}
	@And(".*?right click \"(.*?)\" by text$")
	public void rightClickByText(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		action.contextClick(element);
	}
	@And(".*?right click \"(.*?)\" by xpath$")
	public void rightClickByXpath(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(locator));
		action.contextClick(element);
	}
	//=======================================================================//

	//========================= Keyword Right Click Offset =========================//
	@And(".*?right click offset \"(.*?)\" with (\\d+) and (\\d+) by css selector$")
	public void rightClickOffsetByCssSeletor(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).contextClick().perform();
	}
	@And(".*?right click offset \"(.*?)\" with (\\d+) and (\\d+) by ID$")
	public void rightClickOffsetByID(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).contextClick().perform();
	}
	@And(".*?right click offset \"(.*?)\" with (\\d+) and (\\d+) by name$")
	public void rightClickOffsetByName(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.name(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).contextClick().perform();
	}
	@And(".*?right click offset \"(.*?)\" with (\\d+) and (\\d+) by text$")
	public void rightClickOffsetByText(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		action.moveToElement(element);
		action.moveByOffset(x, y).contextClick().perform();
	}
	@And(".*?right click offset \"(.*?)\" with (\\d+) and (\\d+) by xpath$")
	public void rightClickOffsetByXpath(String locator, int x, int y) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(locator));
		action.moveToElement(element);
		action.moveByOffset(x, y).contextClick().perform();
	}
	//==============================================================================//

	//========================= Keyword Sroll To Element =========================//
	@And(".*?scroll to element \"(.*?)\" by css selector$")
	public void scrollToElementByCssSelector(String locator) {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.cssSelector(locator));
		action.moveToElement(element);
	}
	//============================================================================//

	//========================= Keyword Sroll To Position =========================//
	@And(".*?scroll to position (\\d+) and (\\d+)$")
	public void scrollToPosition(int x, int y) {
		Actions action = new Actions(driver);
		action.moveByOffset(x, y);
	}
	//=============================================================================//

	//========================= Keyword Select All Option =========================//
	@And(".*?select all option \"(.*?)\" by css selector$")
	public void selectAllOptionByCssSelector(String locator) {
		Select option = new Select(driver.findElement(By.cssSelector(locator)));
		List <WebElement> elementCount = option.getOptions();
		for(int i =0; i<elementCount.size() ; i++){
			option.deselectByIndex(i);
		}
	}
	@And(".*?select all option \"(.*?)\" by ID$")
	public void selectAllOptionByIDCssSelector(String locator) {
		Select option = new Select(driver.findElement(By.id(locator)));
		List <WebElement> elementCount = option.getOptions();
		for(int i =0; i<elementCount.size() ; i++){
			option.deselectByIndex(i);
		}
	}
	@And(".*?select all option \"(.*?)\" by name$")
	public void selectAllOptionByName(String locator) {
		Select option = new Select(driver.findElement(By.name(locator)));
		List <WebElement> elementCount = option.getOptions();
		for(int i =0; i<elementCount.size() ; i++){
			option.deselectByIndex(i);
		}
	}
	@And(".*?select all option \"(.*?)\" by text$")
	public void selectAllOptionByText(String locator) {
		Select option = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		List <WebElement> elementCount = option.getOptions();
		for(int i =0; i<elementCount.size() ; i++){
			option.deselectByIndex(i);
		}
	}
	@And(".*?select all option \"(.*?)\" by xpath$")
	public void selectAllOptionByXpath(String locator) {
		Select option = new Select(driver.findElement(By.xpath(locator)));
		List <WebElement> elementCount = option.getOptions();
		for(int i =0; i<elementCount.size() ; i++){
			option.deselectByIndex(i);
		}
	}
	//=============================================================================//

	//========================= Keyword Select Option By Index =========================//
	@And(".*?select option \"(.*?)\" by index \"(.*?)\" with css selector$")
	public void selectOptionByIndexWithCssSelector(String locator, String index) {
		int i = Integer.parseInt(index);
		Select dropdown = new Select(driver.findElement(By.cssSelector(locator)));
		dropdown.selectByIndex(i);
	}
	@And(".*?select option \"(.*?)\" by index \"(.*?)\" with ID$")
	public void selectOptionByIndexWithIDr(String locator, String index) {
		int i = Integer.parseInt(index);
		Select dropdown = new Select(driver.findElement(By.id(locator)));
		dropdown.selectByIndex(i);
	}
	@And(".*?select option \"(.*?)\" by index \"(.*?)\" with name$")
	public void selectOptionByIndexWithName(String locator, String index) {
		int i = Integer.parseInt(index);
		Select dropdown = new Select(driver.findElement(By.name(locator)));
		dropdown.selectByIndex(i);
	}
	@And(".*?select option \"(.*?)\" by index \"(.*?)\" with text$")
	public void selectOptionByIndexWithText(String locator, String index) {
		int i = Integer.parseInt(index);
		Select dropdown = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		dropdown.selectByIndex(i);
	}
	@And(".*?select option \"(.*?)\" by index \"(.*?)\" with xpath$")
	public void selectOptionByIndexWithXpath(String locator, String index) {
		int i = Integer.parseInt(index);
		Select dropdown = new Select(driver.findElement(By.xpath(locator)));
		dropdown.selectByIndex(i);
	}
	//==================================================================================//

	//========================= Keyword Select Option By Label =========================//
	@And(".*?select option \"(.*?)\" by label \"(.*?)\" with css selector$")
	public void selectOptionByLabelWithCssSelector(String locator, String label) {
		Select dropdown = new Select(driver.findElement(By.cssSelector(locator)));
		dropdown.selectByVisibleText(label);
	}
	@And(".*?select option \"(.*?)\" by label \"(.*?)\" with ID$")
	public void selectOptionByLabelWithID(String locator, String label) {
		Select dropdown = new Select(driver.findElement(By.id(locator)));
		dropdown.selectByVisibleText(label);
	}
	@And(".*?select option \"(.*?)\" by label \"(.*?)\" with name$")
	public void selectOptionByLabelWithName(String locator, String label) {
		Select dropdown = new Select(driver.findElement(By.name(locator)));
		dropdown.selectByVisibleText(label);
	}
	@And(".*?select option \"(.*?)\" by label \"(.*?)\" with text$")
	public void selectOptionByLabelWithText(String locator, String label) {
		Select dropdown = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		dropdown.selectByVisibleText(label);
	}
	@And(".*?select option \"(.*?)\" by label \"(.*?)\" with xpath$")
	public void selectOptionByLabelWithXpath(String locator, String label) {
		Select dropdown = new Select(driver.findElement(By.xpath(locator)));
		dropdown.selectByVisibleText(label);
	}
	//==================================================================================//

	//========================= Keyword Select Option By Value =========================//
	@And(".*?select option \"(.*?)\" by value \"(.*?)\" with css selector$")
	public void selectOptionByValueWithCssSelector(String locator, String value) {
		Select dropdown = new Select(driver.findElement(By.cssSelector(locator)));
		dropdown.selectByValue(value);
	}
	@And(".*?select option \"(.*?)\" by value \"(.*?)\" with ID$")
	public void selectOptionByValueWithID(String locator, String value) {
		Select dropdown = new Select(driver.findElement(By.id(locator)));
		dropdown.selectByValue(value);
	}
	@And(".*?select option \"(.*?)\" by value \"(.*?)\" with name$")
	public void selectOptionByValueWithName(String locator, String value) {
		Select dropdown = new Select(driver.findElement(By.name(locator)));
		dropdown.selectByValue(value);
	}
	@And(".*?select option \"(.*?)\" by value \"(.*?)\" with text$")
	public void selectOptionByValueWithText(String locator, String value) {
		Select dropdown = new Select(driver.findElement(By.xpath("//*[text() = '"+locator+"']")));
		dropdown.selectByValue(value);
	}
	@And(".*?select option \"(.*?)\" by value \"(.*?)\" with xpath$")
	public void selectOptionByValueWithXpath(String locator, String value) {
		Select dropdown = new Select(driver.findElement(By.xpath(locator)));
		dropdown.selectByValue(value);
	}
	//==================================================================================//

	//========================= Keyword Send Keys =========================//
	@And(".*?send keys \"(.*?)\" in \"(.*?)\" by css selector$")
	public void sendKeysByCssSelector(String text, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		element.sendKeys(text);
	}
	@And(".*?send keys \"(.*?)\" in \"(.*?)\" by ID$")
	public void sendKeysByID(String text, String locator) {
		WebElement element = driver.findElement(By.id(locator));
		element.sendKeys(text);
	}
	@And(".*?send keys \"(.*?)\" in \"(.*?)\" by name$")
	public void sendKeysByName(String text, String locator) {
		WebElement element = driver.findElement(By.name(locator));
		element.sendKeys(text);
	}
	@And(".*?send keys \"(.*?)\" in \"(.*?)\" by text$")
	public void sendKeysByText(String text, String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		element.sendKeys(text);
	}
	@And(".*?send keys \"(.*?)\" in \"(.*?)\" by xpath$")
	public void sendKeysByXpath(String text, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		element.sendKeys(text);
	}
	//=====================================================================//

	//========================= Keyword Set Alert Text =========================//
	@And(".*?set alert text \"(.*?)\"$")
	public void setAlertText(String text) throws AWTException {
		driver.switchTo().alert();
		Robot rb = new Robot();
		StringSelection username = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, null); 
		rb.keyPress(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
	}
	//==========================================================================//

	//========================= Keyword Set Encrypted Text =========================//
	@And(".*?set encrypted text \"(.*?)\" in \"(.*?)\" by css selector$")
	public void setEncryptedTextByCssSelector(String text, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		element.sendKeys(new String(encodedBytes));
	}
	@And(".*?set encrypted text \"(.*?)\" in \"(.*?)\" by ID$")
	public void setEncryptedTextByID(String text, String locator) {
		WebElement element = driver.findElement(By.id(locator));
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		element.sendKeys(new String(encodedBytes));
	}
	@And(".*?set encrypted text \"(.*?)\" in \"(.*?)\" by name$")
	public void setEncryptedTextByName(String text, String locator) {
		WebElement element = driver.findElement(By.name(locator));
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		element.sendKeys(new String(encodedBytes));
	}
	@And(".*?set encrypted text \"(.*?)\" in \"(.*?)\" by text$")
	public void setEncryptedTextByText(String text, String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		element.sendKeys(new String(encodedBytes));
	}
	@And(".*?set encrypted text \"(.*?)\" in \"(.*?)\" by xpath$")
	public void setEncryptedTextByXpath(String text, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		element.sendKeys(new String(encodedBytes));
	}
	//==============================================================================//

	//========================= Keyword Set Masked Text =========================//
	@And(".*? set masked text \"(.*?)\" in \"(.*?)\" by css selector$")
	public void setMaskedTextByCssSelector(String text, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		element.sendKeys(text);
	}
	@And(".*? set masked text \"(.*?)\" in \"(.*?)\" by ID$")
	public void setMaskedTextByID(String text, String locator) {
		WebElement element = driver.findElement(By.id(locator));
		element.sendKeys(text);
	}
	@And(".*? set masked text \"(.*?)\" in \"(.*?)\" by name$")
	public void setMaskedTextByName(String text, String locator) {
		WebElement element = driver.findElement(By.name(locator));
		element.sendKeys(text);
	}
	@And(".*? set masked text \"(.*?)\" in \"(.*?)\" by text$")
	public void setMaskedTextByText(String text, String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		element.sendKeys(text);
	}
	@And(".*? set masked text \"(.*?)\" in \"(.*?)\" by xpath$")
	public void setMaskedTextByXpath(String text, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		element.sendKeys(text);
	}
	//===========================================================================//

	//========================= Keyword SetText =========================//
	@And(".*?enter \"(.*?)\" in \"(.*?)\" by ID$")
	public void inputTextById(String text, String selector) {
		WebElement element = driver.findElement(By.id(selector));
		element.sendKeys(text);
	}
	@And(".*?enter \"(.*?)\" in \"(.*?)\" by name$")
	public void inputTextByName(String text, String selector) {
		WebElement element = driver.findElement(By.name(selector));
		element.sendKeys(text);
	}
	@And(".*?enter \"(.*?)\" in \"(.*?)\" by css selector$")
	public void inputTextByCssSelector(String text, String selector) {
		WebElement element = driver.findElement(By.cssSelector(selector));
		element.sendKeys(text);
	}
	@And(".*?enter \"(.*?)\" in \"(.*?)\" by text$")
	public void inputTextByText(String text, String selector) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+selector+"']"));
		element.sendKeys(text);
	}
	@And(".*?enter \"(.*?)\" in \"(.*?)\" by xpath$")
	public void inputTextByXpath(String text, String selector) {
		WebElement element = driver.findElement(By.xpath(selector));
		element.sendKeys(text);
	}
	//===================================================================//

	//========================= Keyword Set View Port Size =========================//
	@And(".*?set view port size \"(.*?)\" and \"(.*?)\"$")
	public void setViewPortSize(String width, String height) {
		Dimension d = new Dimension(Integer.parseInt(width) ,Integer.parseInt(height));
		driver.manage().window().setSize(d);
	}
	//==============================================================================//

	//========================= Keyword Submit =========================//
	@And(".*?submit \"(.*?)\" by css selector$")
	public void submitByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			element.click();
		}

	}
	@And(".*?submit \"(.*?)\" by ID$")
	public void submitByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			element.click();
		}
	}
	@And(".*?submit \"(.*?)\" by name$")
	public void submitByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			element.click();
		}
	}
	@And(".*?submit \"(.*?)\" by text$")
	public void submitByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			element.click();
		}
	}
	@And(".*?submit \"(.*?)\" by xpath$")
	public void submitByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			element.click();
		}
	}
	//==================================================================//

	//========================= Keyword Switch To Default Content =========================//
	@And(".*?switch to default content$")
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}
	//=====================================================================================//

	//========================= Keyword Switch To Frame =========================//
	@And(".*?switch to frame \"(.*?)\" by css selector$")
	public void switchToFrameByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		driver.switchTo().frame(element);
	}
	@And(".*?switch to frame \"(.*?)\" by ID$")
	public void switchToFrameByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		driver.switchTo().frame(element);
	}
	@And(".*?switch to frame \"(.*?)\" by Name$")
	public void switchToFrameByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		driver.switchTo().frame(element);
	}
	@And(".*?switch to frame \"(.*?)\" by xpath$")
	public void switchToFrameByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		driver.switchTo().frame(element);
	}
	@And(".*?switch to frame \"(.*?)\" by index$")
	public void switchToFrameByIndex(String index) {
		int i = Integer.parseInt(index);
		driver.switchTo().frame(i);
	}
	//===========================================================================//

	//========================= Keyword Switch To Window Index =========================//
	@And(".*?switch to window index \"(.*?)\"$")
	public void switchToWindowIndex(String index) {
		Set<String> handles = driver.getWindowHandles();
		List<String> windowStrings = new ArrayList<>(handles);
		String reqWindow = windowStrings.get(Integer.parseInt(index));
		driver.switchTo().window(reqWindow);
	}
	//==================================================================================//

	//========================= Keyword Switch To Window Title =========================//
	@And(".*?switch to window title \"(.*?)\"$")
	public void switchToWindowTitle(String title) {
		Set<String> handles = driver.getWindowHandles();
		for(String curWindow : handles){
			driver.switchTo().window(curWindow);
			if (driver.getTitle().equalsIgnoreCase(title)) {
				System.out.println("Success switch window");
				break;
			}
		}
	}
	//==================================================================================//

	//========================= Keyword Switch To Window Url =========================//
	@And(".*?switch to window url \"(.*?)\"$")
	public void switchToWindowUrl(String url) {
		Set<String> handles = driver.getWindowHandles();
		for(String curWindow : handles){
			driver.switchTo().window(curWindow);
			if (driver.getCurrentUrl().equalsIgnoreCase(url)) {
				System.out.println("Success switch window");
				break;
			}
		}
	}
	//================================================================================//

	//========================= Keyword Take Screenshot =========================//
	@And(".*?take screenshot \"(.*?)\"$")
	public void takeScreenshot(String pathFile) throws IOException {
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		FileUtils.copyFile(scrShot.getScreenshotAs(OutputType.FILE), new File(pathFile));
		byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        Hooks.myScenario.embed(screenshot, "image/png");
	}
	//===========================================================================//

	//========================= Keyword Type On Image =========================//
	@And(".*?type \"(.*?)\" on image \"(.*?)\"$")
	public void typeOnImage(String text, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		element.sendKeys(text);
	}
	//=========================================================================//

	//========================= Keyword Un-check =========================//
	@And(".*?un-check \"(.*?)\"$")
	public void unCheck(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		if (element.isSelected()) {
			element.click();
		}else {
			System.out.println("Element is not checked");
		}
	}
	//====================================================================//

	//========================= Keyword Upload File =========================//
	@And(".*?upload file \"(.*?)\" in element \"(.*?)\" by css selector$")
	public void uploadFile(String filePath, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		element.sendKeys(filePath);
	}
	//=======================================================================//

	//========================= Keyword Verify Alert Not Present =========================//
	@And(".*? verify alert not present$")
	public void verifyAlertNotPresent() {
		assertFalse("Alert is present !", isAlertPresent(driver));
	}
	//====================================================================================//

	//========================= Keyword Verify Alert Present =========================//
	@And(".*? verify alert present$")
	public void verifyAlertPresent() {
		assertTrue("Alert is not present !", isAlertPresent(driver));
	}
	//================================================================================//

	//========================= Keyword Verify All Links On Current Page Accessible =========================//
	@And(".*? verify all links on current page acessible$")
	public void verifyAllLinksOnCurrentPageAccessible() {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		for(int i=0; i<links.size(); i++) {
			WebElement element = links.get(i);
			String url=element.getAttribute("href");
			verifyLink(url);            
		}
	}
	//=======================================================================================================//

	//========================= Keyword Verify Element Attribute Value =========================//
	@And(".*? verify element \"(.*?)\" attribute \"(.*?)\" value \"(.*?)\" by css selector$")
	public void verifyElementAttributeValueByCssSelector(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element has not attribute "+attribute+" with value "+value, element.getAttribute(attribute).equalsIgnoreCase(value));
	}
	@And(".*? verify element \"(.*?)\" attribute \"(.*?)\" value \"(.*?)\" by ID$")
	public void verifyElementAttributeValueByID(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element has not attribute "+attribute+" with value "+value, element.getAttribute(attribute).equalsIgnoreCase(value));
	}
	@And(".*? verify element \"(.*?)\" attribute \"(.*?)\" value \"(.*?)\" by name$")
	public void verifyElementAttributeValueByName(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element has not attribute "+attribute+" with value "+value, element.getAttribute(attribute).equalsIgnoreCase(value));
	}
	@And(".*? verify element \"(.*?)\" attribute \"(.*?)\" value \"(.*?)\" by text$")
	public void verifyElementAttributeValueByText(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element has not attribute "+attribute+" with value "+value, element.getAttribute(attribute).equalsIgnoreCase(value));
	}
	@And(".*? verify element \"(.*?)\" attribute \"(.*?)\" value \"(.*?)\" by xpath$")
	public void verifyElementAttributeValueByXpath(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element has not attribute "+attribute+" with value "+value, element.getAttribute(attribute).equalsIgnoreCase(value));
	}
	//==========================================================================================//

	//========================= Keyword Verify Element Checked =========================//
	@And(".*? verify element \"(.*?)\" checked by css selector$")
	public void verifyElementCheckedByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" checked by ID$")
	public void verifyElementCheckedByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" checked by name$")
	public void verifyElementCheckedByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" checked by text$")
	public void verifyElementCheckedByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" checked by xpath$")
	public void verifyElementCheckedByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not checked !", isElementChecked(element));
	}
	//==================================================================================//

	//========================= Keyword Verify Element Clickable =========================//
	@And(".*? verify element \"(.*?)\" clickable by css selector$")
	public void verifyElementClickableByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" clickable by ID$")
	public void verifyElementClickableByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" clickable by name$")
	public void verifyElementClickableByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" clickable by text$")
	public void verifyElementClickableByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" clickable by xpath$")
	public void verifyElementClickableByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	//====================================================================================//

	//========================= Keyword Verify Element Has Attribute =========================//
	@And(".*? verify element \"(.*?)\" has attribute \"(.*?)\" by css selector$")
	public void verifyElementHasAttributeByCssSelector(String locator, String attribute) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element not has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" has attribute \"(.*?)\" by ID$")
	public void verifyElementHasAttributeByID(String locator, String attribute) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element not has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" has attribute \"(.*?)\" by name$")
	public void verifyElementHasAttributeByName(String locator, String attribute) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element not has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" has attribute \"(.*?)\" by text$")
	public void verifyElementHasAttributeByText(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element not has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" has attribute \"(.*?)\" by xpath$")
	public void verifyElementHasAttributeByXpath(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element not has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	//========================================================================================//

	//========================= Keyword Verify Element In Viewport =========================//
	@And(".*? verify element \"(.*?)\" in viewprot by css selector$")
	public void verifyElementInViewportByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element not in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" in viewprot by ID$")
	public void verifyElementInViewportByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element not in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" in viewprot by name$")
	public void verifyElementInViewportByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element not in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" in viewprot by text$")
	public void verifyElementInViewportByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element not in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" in viewprot by xpath$")
	public void verifyElementInViewportByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element not in viewport", isVisibleInViewport(element));
	}
	//======================================================================================//	

	//========================= Keyword Verify Element Not Checked =========================//
	@And(".*? verify element \"(.*?)\" not checked by css selector$")
	public void verifyElementNotCheckedByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element is checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" not checked by ID$")
	public void verifyElementNotCheckedByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element is checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" not checked by name$")
	public void verifyElementNotCheckedByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element is checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" not checked by text$")
	public void verifyElementNotCheckedByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element is checked !", isElementChecked(element));
	}
	@And(".*? verify element \"(.*?)\" not checked by xpath$")
	public void verifyElementNotCheckedByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element is checked !", isElementChecked(element));
	}
	//======================================================================================//

	//========================= Keyword Verify Element Not Clickable =========================//
	@And(".*? verify element \"(.*?)\" not clickable by css selector$")
	public void verifyElementNotClickableByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" not clickable by ID$")
	public void verifyElementNotClickableByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" not clickable by name$")
	public void verifyElementNotClickableByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" not clickable by text$")
	public void verifyElementNotClickableByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? verify element \"(.*?)\" not clickable by xpath$")
	public void verifyElementNotClickableByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	//========================================================================================//

	//========================= Keyword Verify Element Not Has Attribute =========================//
	@And(".*? verify element \"(.*?)\" not has attribute \"(.*?)\" by css selector$")
	public void verifyElementNotHasAttributeByCssSelector(String locator, String attribute) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" not has attribute \"(.*?)\" by ID$")
	public void verifyElementNotHasAttributeByID(String locator, String attribute) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" not has attribute \"(.*?)\" by name$")
	public void verifyElementNotHasAttributeByName(String locator, String attribute) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" not has attribute \"(.*?)\" by text$")
	public void verifyElementNotHasAttributeByText(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	@And(".*? verify element \"(.*?)\" not has attribute \"(.*?)\" by xpath$")
	public void verifyElementNotHasAttributeByXpath(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element has attribute "+attribute, isAttribtuePresent(element, attribute));
	}
	//============================================================================================//

	//========================= Keyword Verify Element Not In Viewport =========================//
	@And(".*? verify element \"(.*?)\" not in viewport by css selector$")
	public void verifyElementNotInViewportByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" not in viewport by ID$")
	public void verifyElementNotInViewportByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" not in viewport by name$")
	public void verifyElementNotInViewportByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" not in viewport by text$")
	public void verifyElementNotInViewportByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" not in viewport by xpath$")
	public void verifyElementNotInViewportByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element in viewport", isVisibleInViewport(element));
	}
	//==========================================================================================//

	//========================= Keyword Verify Element Not Present =========================//
	@And(".*? verify element \"(.*?)\" not present by css selector$")
	public void verifyElementNotPresentByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not present by ID$")
	public void verifyElementNotPresentByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not present by name$")
	public void verifyElementNotPresentByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not present by text$")
	public void verifyElementNotPresentByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not present by xpath$")
	public void verifyElementNotPresentByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element is present !", isElementPresent(element));
	}
	//======================================================================================//

	//========================= Keyword Verify Element Not Visible =========================//
	@And(".*? verify element \"(.*?)\" not visible by css selector$")
	public void verifyElementNotVisibleByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not visible by ID$")
	public void verifyElementNotVisibleByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not visible by name$")
	public void verifyElementNotVisibleByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not visible by text$")
	public void verifyElementNotVisibleByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" not visible by xpath$")
	public void verifyElementNotVisibleByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element is visible !", isElementPresent(element));
	}
	//======================================================================================//

	//========================= Keyword Verify Element Not Visible In Viewport =========================//
	@And(".*? verify element \"(.*?)\" not visible in viewport by css selector$")
	public void verifyElementNotVisibleInViewportByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element present but visible in viewport", isElementPresent(element) == true && isVisibleInViewport(element) == false);
	}
	@And(".*? verify element \"(.*?)\" not visible in viewport by ID$")
	public void verifyElementNotVisibleInViewportByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element present but visible in viewport", isElementPresent(element) == true && isVisibleInViewport(element) == false);
	}
	@And(".*? verify element \"(.*?)\" not visible in viewport by name$")
	public void verifyElementNotVisibleInViewportByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element present but visible in viewport", isElementPresent(element) == true && isVisibleInViewport(element) == false);
	}
	@And(".*? verify element \"(.*?)\" not visible in viewport by text$")
	public void verifyElementNotVisibleInViewportByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element present but visible in viewport", isElementPresent(element) == true && isVisibleInViewport(element) == false);
	}
	@And(".*? verify element \"(.*?)\" not visible in viewport by xpath$")
	public void verifyElementNotVisibleInViewportByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element present but visible in viewport", isElementPresent(element) == true && isVisibleInViewport(element) == false);
	}
	//==================================================================================================//

	//========================= Keyword Verify Element Present =========================//
	@And(".*? verify element \"(.*?)\" present by css selector$")
	public void verifyElementPresentByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not present in DOM !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" present by ID$")
	public void verifyElementPresentByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not present in DOM !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" present by name$")
	public void verifyElementPresentByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not present in DOM !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" present by text$")
	public void verifyElementPresentByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not present in DOM !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" present by xpath$")
	public void verifyElementPresentByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not present in DOM !", isElementPresent(element));
	}
	//==================================================================================//

	//========================= Keyword Verify Element Text =========================//
	@Then(".*?verify element by ID.*? \"(.*?)\" has text \"(.*?)\"$")
	public void verifyElementTextById(String selector,  String text) {
		WebElement element = driver.findElement(By.id(selector));
		assertEquals("Element text is not match with "+element.getText(), text, element.getText());
	}
	@Then(".*?verify element by name.*? \"(.*?)\" has text \"(.*?)\"$")
	public void verifyElementTextByName(String selector,  String text) {
		WebElement element = driver.findElement(By.name(selector));
		assertEquals("Element text is not match with "+element.getText(), text, element.getText());
	}
	@Then(".*?verify element by css selector.*? \"(.*?)\" has text \"(.*?)\"$")
	public void verifyElementTextByCssSelector(String selector,  String text) {
		WebElement element = driver.findElement(By.cssSelector(selector));
		assertEquals("Element text is not match with "+element.getText(), text, element.getText());
	}
	@Then(".*?verify element by text.*? \"(.*?)\" has text \"(.*?)\"$")
	public void verifyElementTextByText(String selector,  String text) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+selector+"']"));
		assertEquals("Element text is not match with "+element.getText(), text, element.getText());
	}
	@Then(".*?verify element by xpath.*? \"(.*?)\" has text \"(.*?)\"$")
	public void verifyElementTextByXpath(String selector,  String text) {
		WebElement element = driver.findElement(By.xpath(selector));
		assertEquals("Element text is not match with "+element.getText(), text, element.getText());
	}
	//===============================================================================//


	//========================= Keyword Verify Element Visible =========================//
	@And(".*? verify element \"(.*?)\" visible with css selector$")
	public void verifyElementVisibleByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" visible with ID$")
	public void verifyElementVisibleByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" visible with nama$")
	public void verifyElementVisibleByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" visible with text$")
	public void verifyElementVisibleByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*? verify element \"(.*?)\" visible with xpath$")
	public void verifyElementVisibleByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	//==================================================================================//

	//========================= Keyword Verify Element Visible In Viewport =========================//
	@And(".*? verify element \"(.*?)\" visible in viewport by css selector$")
	public void verifyElementVisibleInViewPortByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not visible in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" visible in viewport by ID$")
	public void verifyElementVisibleInViewPortByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not visible in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" visible in viewport by name$")
	public void verifyElementVisibleInViewPortByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not visible in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" visible in viewport by text$")
	public void verifyElementVisibleInViewPortByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not visible in viewport", isVisibleInViewport(element));
	}
	@And(".*? verify element \"(.*?)\" visible in viewport by xpath$")
	public void verifyElementVisibleInViewPortByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not visible in viewport", isVisibleInViewport(element));
	}
	//==============================================================================================//

	//========================= Keyword Verify Equal =========================//
	@And(".*? verify equal \"(.*?)\" with \"(.*?)\"$")
	public void verifyEqual(String actual, String expected) {
		assertEquals(actual+" is not equal with "+expected+". Maybe greater or less !", Integer.parseInt(expected), Integer.parseInt(actual));
	}
	//========================================================================//

	//========================= Keyword Verify Greater Than =========================//
	@And(".*? verify greater than \"(.*?)\" with \"(.*?)\"$")
	public void verifyGreaterThan(String actual, String expected) {
		assertTrue(actual+" is not greater than "+expected+". Maybe equal or less !", Integer.parseInt(actual) > Integer.parseInt(expected));
	}
	//===============================================================================//

	//========================= Keyword Verify Greater Than Or Equal =========================//
	@And(".*? verify greater than or equal \"(.*?)\" with \"(.*?)\"$")
	public void verifyGreaterThanOrEqual(String actual, String expected) {
		assertTrue(actual+" is not greater than or equal "+expected+". Maybe equal or less !", Integer.parseInt(actual) >= Integer.parseInt(expected));
	}
	//========================================================================================//

	//========================= Keyword Verify Image Present =========================//
	@And(".*? verify image \"(.*?)\" present by css selector$")
	public void verifyImagePresentByCssSelector(String locator) {
		WebElement image = driver.findElement(By.cssSelector(locator));
		assertTrue("Image is not present", image.isDisplayed());
	}
	@And(".*? verify image \"(.*?)\" present by ID$")
	public void verifyImagePresentByID(String locator) {
		WebElement image = driver.findElement(By.id(locator));
		assertTrue("Image is not present", image.isDisplayed());
	}
	@And(".*? verify image \"(.*?)\" present by name$")
	public void verifyImagePresentByName(String locator) {
		WebElement image = driver.findElement(By.name(locator));
		assertTrue("Image is not present", image.isDisplayed());
	}
	@And(".*? verify image \"(.*?)\" present by text$")
	public void verifyImagePresentByText(String locator) {
		WebElement image = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Image is not present", image.isDisplayed());
	}
	@And(".*? verify image \"(.*?)\" present by xpath$")
	public void verifyImagePresentByXpath(String locator) {
		WebElement image = driver.findElement(By.xpath(locator));
		assertTrue("Image is not present", image.isDisplayed());
	}
	//================================================================================//

	//========================= Keyword Verify Less Than =========================//
	@And(".*? verify less than \"(.*?)\" with \"(.*?)\"$")
	public void verifyLessThan(String actual, String expected) {
		assertTrue(actual+" is not less than "+expected+". Maybe equal or greater !", Integer.parseInt(actual) < Integer.parseInt(expected));
	}
	//============================================================================//

	//========================= Keyword Verify Less Than Or Equal =========================//
	@And(".*? verify less than or equal \"(.*?)\" with \"(.*?)\"$")
	public void verifyLessThanOrEqual(String actual, String expected) {
		assertTrue(actual+" is not less than or equal with "+expected+". Or maybe equal !", Integer.parseInt(actual) <= Integer.parseInt(expected));
	}
	//=====================================================================================//

	//========================= Keyword Verify Links Accessible =========================//
	@And(".*? verify links acessible")
	public void verifyLinksAccessible() {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		System.out.println("Total links are "+links.size());
		for(int i=0; i<links.size(); i++) {
			WebElement element = links.get(i);
			String url=element.getAttribute("href");
			verifyLink(url);            
		} 
	}
	//===================================================================================//

	//========================= Keyword Verify Match =========================//
	@And(".*? verify match \"(.*?)\" with \"(.*?)\"$")
	public void verifyMatch(String actual, String expected) {
		assertEquals(actual+" is not match with "+expected, expected, actual);
	}
	//========================================================================//

	//========================= Keyword Verify Not Equal =========================//
	@And(".*? verify not equal \"(.*?)\" with \"(.*?)\"$")
	public void verifyNotEqual(String actual, String expected) {
		try {
			assertEquals(actual+"is not equal with "+expected, expected, actual);
		} catch (Exception e) {
			try {
				assertEquals(actual+"is not equal with "+expected, Integer.parseInt(expected), Integer.parseInt(actual));
			} catch (Exception e2) {
				System.out.println(e.getMessage());
			}
		}
	}
	//============================================================================//

	//========================= Keyword Verify Not Match =========================//
	@And(".*? verify not match \"(.*?)\" with \"(.*?)\"$")
	public void verifyNotMatch(String actual, String expected) {
		assertNotEquals(expected+" match with "+actual, expected, actual);
	}
	//============================================================================//

	//========================= Keyword Verify Option Not Present By Label =========================//
	@And(".*? verify option \"(.*?)\" not present by label \"(.*?)\" with css selector$")
	public void verifyOptionNotPresentByLabelWithCssSelector(String locator, String label) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Label "+label+" is present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" not present by label \"(.*?)\" with ID$")
	public void verifyOptionNotPresentByLabelWithID(String locator, String label) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Label "+label+" is present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" not present by label \"(.*?)\" with name$")
	public void verifyOptionNotPresentByLabelWithName(String locator, String label) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Label "+label+" is present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" not present by label \"(.*?)\" with text$")
	public void verifyOptionNotPresentByLabelWithText(String locator, String label) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Label "+label+" is present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" not present by label \"(.*?)\" with xpath$")
	public void verifyOptionNotPresentByLabelWithXpath(String locator, String label) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Label "+label+" is present in option", isOptionLabelPresent(element, label));
	}
	//==============================================================================================//

	//========================= Keyword Verify Option Not Present By Value =========================//
	@And(".*? verify option \"(.*?)\" not present by value \"(.*?)\" with css selector$")
	public void verifyOptionNotPresentByValueWithCssSelector(String locator, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Value "+value+" is present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" not present by value \"(.*?)\" with ID$")
	public void verifyOptionNotPresentByValueWithID(String locator, String value) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Value "+value+" is present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" not present by value \"(.*?)\" with name$")
	public void verifyOptionNotPresentByValueWithName(String locator, String value) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Value "+value+" is present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" not present by value \"(.*?)\" with text$")
	public void verifyOptionNotPresentByValueWithText(String locator, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Value "+value+" is present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" not present by value \"(.*?)\" with xpath$")
	public void verifyOptionNotPresentByValueWithXpath(String locator, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Value "+value+" is present in option", isOptionValuePresent(element, value));
	}
	//==============================================================================================//

	//========================= Keyword Verify Option Not Selected By Index =========================//
	@And(".*? verify option \"(.*?)\" not selected by index \"(.*?)\" with css selector$")
	public void verifyOptionNotSelectedByIndexWithCssSelector(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertFalse("Option with index "+i+" selected", option.getFirstSelectedOption().getText().contains(list.get(i).getText()));
	}
	@And(".*? verify option \"(.*?)\" not selected by index \"(.*?)\" with ID$")
	public void verifyOptionNotSelectedByIndexWithID(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertFalse("Option with index "+i+" selected", option.getFirstSelectedOption().getText().contains(list.get(i).getText()));
	}
	@And(".*? verify option \"(.*?)\" not selected by index \"(.*?)\" with name$")
	public void verifyOptionNotSelectedByIndexWithName(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertFalse("Option with index "+i+" selected", option.getFirstSelectedOption().getText().contains(list.get(i).getText()));
	}
	@And(".*? verify option \"(.*?)\" not selected by index \"(.*?)\" with text$")
	public void verifyOptionNotSelectedByIndexWithText(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertFalse("Option with index "+i+" selected", option.getFirstSelectedOption().getText().contains(list.get(i).getText()));
	}
	@And(".*? verify option \"(.*?)\" not selected by index \"(.*?)\" with xpath$")
	public void verifyOptionNotSelectedByIndexWithXpath(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertFalse("Option with index "+i+" selected", option.getFirstSelectedOption().getText().contains(list.get(i).getText()));
	}

	//===============================================================================================//

	//========================= Keyword Verify Option Not Selected By Label =========================//
	@And(".*? verify option \"(.*?)\" not selected by label \"(.*?)\" with css selector$")
	public void verifyOptionNotSelectedByLabelWithCssSelector(String locator, String label) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		assertFalse("Option with label "+label+" selected", option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" not selected by label \"(.*?)\" with ID$")
	public void verifyOptionNotSelectedByLabelWithID(String locator, String label) {
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		assertFalse("Option with label "+label+" selected", option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" not selected by label \"(.*?)\" with name$")
	public void verifyOptionNotSelectedByLabelWithName(String locator, String label) {
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		assertFalse("Option with label "+label+" selected", option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" not selected by label \"(.*?)\" with text$")
	public void verifyOptionNotSelectedByLabelWithText(String locator, String label) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		assertFalse("Option with label "+label+" selected", option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" not selected by label \"(.*?)\" with xpath$")
	public void verifyOptionNotSelectedByLabelWithXpath(String locator, String label) {
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		assertFalse("Option with label "+label+" selected", option.getFirstSelectedOption().getText().contains(label));
	}
	//===============================================================================================//

	//========================= Keyword Verify Option Not Selected By Value =========================//
	@And(".*? verify option \"(.*?)\" not selected by value \"(.*?)\" with css selector$")
	public void verifyOptionNotSelectedByValueWithCssSelector(String locator, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		assertFalse("Option with value "+value+" selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" not selected by value \"(.*?)\" with ID$")
	public void verifyOptionNotSelectedByValueWithID(String locator, String value) {
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		assertFalse("Option with value "+value+" selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" not selected by value \"(.*?)\" with name$")
	public void verifyOptionNotSelectedByValueWithName(String locator, String value) {
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		assertFalse("Option with value "+value+" selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" not selected by value \"(.*?)\" with text$")
	public void verifyOptionNotSelectedByValueWithText(String locator, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		assertFalse("Option with value "+value+" selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" not selected by value \"(.*?)\" with xpath$")
	public void verifyOptionNotSelectedByValueWithXpath(String locator, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		assertFalse("Option with value "+value+" selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	//===============================================================================================//

	//========================= Keyword Verify Option Present By Label =========================//
	@And(".*? verify option \"(.*?)\" present by label \"(.*?)\" with css selector$")
	public void verifyOptionPresentByLabelWithCssSelector(String locator, String label) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Label "+label+" is not present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" present by label \"(.*?)\" with ID$")
	public void verifyOptionPresentByLabelWithID(String locator, String label) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Label "+label+" is not present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" present by label \"(.*?)\" with Name$")
	public void verifyOptionPresentByLabelWithName(String locator, String label) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Label "+label+" is not present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" present by label \"(.*?)\" with text$")
	public void verifyOptionPresentByLabelWithText(String locator, String label) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Label "+label+" is not present in option", isOptionLabelPresent(element, label));
	}
	@And(".*? verify option \"(.*?)\" present by label \"(.*?)\" with xpath$")
	public void verifyOptionPresentByLabelWithXpath(String locator, String label) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Label "+label+" is not present in option", isOptionLabelPresent(element, label));
	}
	//==========================================================================================//

	//========================= Keyword Verify Option Present By Value =========================//
	@And(".*? verify option \"(.*?)\" present by value \"(.*?)\" with css selector$")
	public void verifyOptionPresentByValueWithCssSelector(String locator, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Value "+value+" is not present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" present by value \"(.*?)\" with ID$")
	public void verifyOptionPresentByValueWithID(String locator, String value) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Value "+value+" is not present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" present by value \"(.*?)\" with Name$")
	public void verifyOptionPresentByValueWithName(String locator, String value) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Value "+value+" is not present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" present by value \"(.*?)\" with text$")
	public void verifyOptionPresentByValueWithText(String locator, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Value "+value+" is not present in option", isOptionValuePresent(element, value));
	}
	@And(".*? verify option \"(.*?)\" present by value \"(.*?)\" with xpath$")
	public void verifyOptionPresentByValueWithXpath(String locator, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Value "+value+" is not present in option", isOptionValuePresent(element, value));
	}
	//==========================================================================================//

	//========================= Keyword Verify Option Selected By Index =========================//
	@And(".*? verify option \"(.*?)\" selected by index \"(.*?)\" with css selector$")
	public void verifyOptionSelectedByIndexWithCssSelector(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertTrue("Option index "+i+" not selected", list.get(i).getText().equals(option.getFirstSelectedOption().getText()));
	}
	@And(".*? verify option \"(.*?)\" selected by index \"(.*?)\" with ID$")
	public void verifyOptionSelectedByIndexWithID(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertTrue("Option index "+i+" not selected", list.get(i).getText().equals(option.getFirstSelectedOption().getText()));
	}
	@And(".*? verify option \"(.*?)\" selected by index \"(.*?)\" with name$")
	public void verifyOptionSelectedByIndexWithName(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertTrue("Option index "+i+" not selected", list.get(i).getText().equals(option.getFirstSelectedOption().getText()));
	}
	@And(".*? verify option \"(.*?)\" selected by index \"(.*?)\" with text$")
	public void verifyOptionSelectedByIndexWithText(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertTrue("Option index "+i+" not selected", list.get(i).getText().equals(option.getFirstSelectedOption().getText()));
	}
	@And(".*? verify option \"(.*?)\" selected by index \"(.*?)\" with xpath$")
	public void verifyOptionSelectedByIndexWithXpath(String locator, String index) {
		int i = Integer.parseInt(index);
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		List<WebElement> list = option.getOptions();
		assertTrue("Option index "+i+" not selected", list.get(i).getText().equals(option.getFirstSelectedOption().getText()));
	}
	//===========================================================================================//

	//========================= Keyword Verify Option Selected By Label =========================//
	@And(".*? verify option \"(.*?)\" selected by label \"(.*?)\" with css selector$")
	public void verifyOptionSelectedByLabelWithCssSelector(String locator, String label) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		assertTrue("Option with label "+label+" not selected", option.getFirstSelectedOption().getAttribute("label").contains(label) || option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" selected by label \"(.*?)\" with id$")
	public void verifyOptionSelectedByLabelWithID(String locator, String label) {
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		assertTrue("Option with label "+label+" not selected", option.getFirstSelectedOption().getAttribute("label").contains(label) || option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" selected by label \"(.*?)\" with name$")
	public void verifyOptionSelectedByLabelWithName(String locator, String label) {
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		assertTrue("Option with label "+label+" not selected", option.getFirstSelectedOption().getAttribute("label").contains(label) || option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" selected by label \"(.*?)\" with text$")
	public void verifyOptionSelectedByLabelWithText(String locator, String label) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		assertTrue("Option with label "+label+" not selected", option.getFirstSelectedOption().getAttribute("label").contains(label) || option.getFirstSelectedOption().getText().contains(label));
	}
	@And(".*? verify option \"(.*?)\" selected by label \"(.*?)\" with xpath$")
	public void verifyOptionSelectedByLabelWithXpath(String locator, String label) {
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		assertTrue("Option with label "+label+" not selected", option.getFirstSelectedOption().getAttribute("label").contains(label) || option.getFirstSelectedOption().getText().contains(label));
	}
	//===========================================================================================//

	//========================= Keyword Verify Option Selected By Value =========================//
	@And(".*? verify option \"(.*?)\" selected by value \"(.*?)\" with css selector$")
	public void verifyOptionSelectedByValueWithCssSelector(String locator, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Select option = new Select(element);
		assertTrue("Option with value "+value+" not selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" selected by value \"(.*?)\" with id$")
	public void verifyOptionSelectedByValueWithID(String locator, String value) {
		WebElement element = driver.findElement(By.id(locator));
		Select option = new Select(element);
		assertTrue("Option with value "+value+" not selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" selected by value \"(.*?)\" with name$")
	public void verifyOptionSelectedByValueWithName(String locator, String value) {
		WebElement element = driver.findElement(By.name(locator));
		Select option = new Select(element);
		assertTrue("Option with value "+value+" not selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" selected by value \"(.*?)\" with text$")
	public void verifyOptionSelectedByValueWithText(String locator, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Select option = new Select(element);
		assertTrue("Option with value "+value+" not selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	@And(".*? verify option \"(.*?)\" selected by value \"(.*?)\" with xpath$")
	public void verifyOptionSelectedByValueWithXpath(String locator, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		Select option = new Select(element);
		assertTrue("Option with value "+value+" not selected", option.getFirstSelectedOption().getAttribute("value").contains(value));
	}
	//===========================================================================================//

	//========================= Keyword Verify Options Present =========================//
	@And(".*? verify option present by css selector$")
	public void verifyOptionsPresentByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Option is not present !", checkElement(element));
	}
	@And(".*? verify option present by ID$")
	public void verifyOptionsPresentByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Option is not present !", checkElement(element));
	}
	@And(".*? verify option present by name$")
	public void verifyOptionsPresentByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Option is not present !", checkElement(element));
	}
	@And(".*? verify option present by text$")
	public void verifyOptionsPresentByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Option is not present !", checkElement(element));
	}
	@And(".*? verify option present by xpath$")
	public void verifyOptionsPresentByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Option is not present !", checkElement(element));
	}
	//==================================================================================//

	//========================= Keyword Verify Text Not Present =========================//
	@And(".*? verify text \"(.*?)\" not present$")
	public void verifyTextNotPresent(String text) {
		assertFalse("Text is present !", isTextPresent(driver, text));
	}
	//===============================================================================//

	//========================= Keyword Verify Text Present =========================//
	@And(".*? verify text \"(.*?)\" present$")
	public void verifyTextPresent(String text) {
		assertTrue("Text is not present !", isTextPresent(driver, text));
	}
	//===============================================================================//

	//========================= Keyword Wait For Alert =========================//
	@And(".*? wait for alert$")
	public void waitForAlert() {
		int i=0;
		while(i++<5) {
			try {
				driver.switchTo().alert();
				break;
			}
			catch(NoAlertPresentException e) {
				waitFor(3);
				continue;
			}
		}
	}
	//==========================================================================//

	//========================= Keyword Wait For Angular Load =========================//
	@And(".*? wait for angular load$")
	public void waitForAngularLoad() {
		String javaScriptToLoadAngular =
				"var injector = window.angular.element('body').injector();" + 
						"var $http = injector.get('$http');" + 
						"return ($http.pendingRequests.length === 0)";

		ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
			}
		};
		waitUntilExpectedCondition(driver, pendingHttpCallsCondition);
	}
	//=================================================================================//

	//========================= Keyword Wait For Element Attribute Value =========================//
	@And(".*? wait for elememt \"(.*?)\" attribute \"(.*?)\" with value \"(.*?)\" by css selector$")
	public void waitForElementAttributeValueByCssSelector(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Boolean result = watiForElementAttributeValue(driver, element, attribute, value);
		assertTrue("Element does not has attribute "+attribute+" with value "+value, result);
	}
	@And(".*? wait for elememt \"(.*?)\" attribute \"(.*?)\" with value \"(.*?)\" by id$")
	public void waitForElementAttributeValueByID(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.id(locator));
		Boolean result = watiForElementAttributeValue(driver, element, attribute, value);
		assertTrue("Element does not has attribute "+attribute+" with value "+value, result);
	}	
	@And(".*? wait for elememt \"(.*?)\" attribute \"(.*?)\" with value \"(.*?)\" by name$")
	public void waitForElementAttributeValueByName(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.name(locator));
		Boolean result = watiForElementAttributeValue(driver, element, attribute, value);
		assertTrue("Element does not has attribute "+attribute+" with value "+value, result);
	}
	@And(".*? wait for elememt \"(.*?)\" attribute \"(.*?)\" with value \"(.*?)\" by text$")
	public void waitForElementAttributeValueByText(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Boolean result = watiForElementAttributeValue(driver, element, attribute, value);
		assertTrue("Element does not has attribute "+attribute+" with value "+value, result);
	}
	@And(".*? wait for elememt \"(.*?)\" attribute \"(.*?)\" with value \"(.*?)\" by xpath$")
	public void waitForElementAttributeValueByXpath(String locator, String attribute, String value) {
		WebElement element = driver.findElement(By.xpath(locator));
		Boolean result = watiForElementAttributeValue(driver, element, attribute, value);
		assertTrue("Element does not has attribute "+attribute+" with value "+value, result);	
	}
	//============================================================================================//

	//========================= Keyword Wait For Element Clickable =========================//
	@And(".*? wait for element \"(.*?)\" clickable by css selector$")
	public void waitForElementClickableByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" clickable by ID$")
	public void waitForElementClickableByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" clickable by name$")
	public void waitForElementClickableByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" clickable by text$")
	public void waitForElementClickableByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" clickable by xpath$")
	public void waitForElementClickableByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertTrue("Element is not clickable !", isClickable(element, driver));
	}
	//======================================================================================//

	//========================= Keyword Wait For Element Has Attribute =========================//
	@And(".*? wait for element \"(.*?)\" has attribute \"(.*?)\" by css selector$")
	public void waitForElementHasAttributeByCssSelector(String locator, String attribute) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertTrue("Element does not has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" has attribute \"(.*?)\" by ID$")
	public void waitForElementHasAttributeByID(String locator, String attribute) {
		WebElement element = driver.findElement(By.id(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertTrue("Element does not has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" has attribute \"(.*?)\" by name$")
	public void waitForElementHasAttributeByName(String locator, String attribute) {
		WebElement element = driver.findElement(By.name(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertTrue("Element does not has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" has attribute \"(.*?)\" by text$")
	public void waitForElementHasAttributeByText(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertTrue("Element does not has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" has attribute \"(.*?)\" by xpath$")
	public void waitForElementHasAttributeByXpath(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertTrue("Element does not has attribute "+ attribute, result);
	}
	//==========================================================================================//

	//========================= Keyword Wait For Element Not Clickable =========================//
	@And(".*? wait for element \"(.*?)\" not clickable by css selector$")
	public void waitForElementNotClickableByCssSelector(String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" not clickable by ID$")
	public void waitForElementNotClickableByID(String locator) {
		WebElement element = driver.findElement(By.id(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" not clickable by name$")
	public void waitForElementNotClickableByName(String locator) {
		WebElement element = driver.findElement(By.name(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" not clickable by text$")
	public void waitForElementNotClickableByText(String locator) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	@And(".*? wait for element \"(.*?)\" not clickable by xpath$")
	public void waitForElementNotClickableByXpath(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		assertFalse("Element is clickable !", isClickable(element, driver));
	}
	//==========================================================================================//

	//========================= Keyword Wait For Element Not Has Attribute =========================//
	@And(".*? wait for element \"(.*?)\" not has attribute \"(.*?)\" by css selector$")
	public void waitForElementNotHasAttributeByCssSelector(String locator, String attribute) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertFalse("Element does has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" not has attribute \"(.*?)\" by ID$")
	public void waitForElementNotHasAttributeByID(String locator, String attribute) {
		WebElement element = driver.findElement(By.id(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertFalse("Element does has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" not has attribute \"(.*?)\" by name$")
	public void waitForElementNotHasAttributeByName(String locator, String attribute) {
		WebElement element = driver.findElement(By.name(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertFalse("Element does has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" not has attribute \"(.*?)\" by text$")
	public void waitForElementNotHasAttributeByText(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertFalse("Element does has attribute "+ attribute, result);
	}
	@And(".*? wait for element \"(.*?)\" not has attribute \"(.*?)\" by xpath$")
	public void waitForElementNotHasAttributeByXpath(String locator, String attribute) {
		WebElement element = driver.findElement(By.xpath(locator));
		Boolean result = waitForElementNotHasAttribute(driver, element, attribute);
		assertFalse("Element does has attribute "+ attribute, result);
	}
	//==============================================================================================//

	//========================= Keyword Wait For Element Not Present =========================//
	@And(".*? wait for element \"(.*?)\" not present by css selector$")
	public void waitForElementNotPresentByCss(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" not present by id$")
	public void waitForElementNotPresentID(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" not present by name$")
	public void waitForElementNotPresentName(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" not present by text$")
	public void waitForElementNotPresentText(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" not present by xpath$")
	public void waitForElementNotPresentXpath(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is present !", isElementPresent(element));
	}
	//========================================================================================//

	//========================= Keyword Wait For Element Not Visible =========================//
	@And(".*?wait for element \"(.*?)\" not visible by css selector$")
	public void waitForElementNotVisibleByCss(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" not visible by ID$")
	public void waitForElementNotVisibleByID(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" not visible by name$")
	public void waitForElementNotVisibleByName(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" not visible by text$")
	public void waitForElementNotVisibleByText(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" not visible by xpath$")
	public void waitForElementNotVisibleByXpath(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertFalse("Element is visible !", isElementPresent(element));
	}
	//========================================================================================//

	//========================= Keyword Wait For Element Present =========================//
	@And(".*? wait for element \"(.*?)\" present by css selector$")
	public void waitForElementPresentByCss(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" present by id$")
	public void waitForElementPresentID(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" present by name$")
	public void waitForElementPresentName(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" present by text$")
	public void waitForElementPresentText(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not present !", isElementPresent(element));
	}
	@And(".*? wait for element \"(.*?)\" present by xpath$")
	public void waitForElementPresentXpath(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not present !", isElementPresent(element));
	}
	//====================================================================================//

	//========================= Keyword Wait For Element Visible =========================//
	@And(".*?wait for element \"(.*?)\" visible by css selector$")
	public void waitForElementVisibleByCss(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" visible by ID$")
	public void waitForElementVisibleByID(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" visible by name$")
	public void waitForElementVisibleByName(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" visible by text$")
	public void waitForElementVisibleByText(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	@And(".*?wait for element \"(.*?)\" visible by xpath$")
	public void waitForElementVisibleByXpath(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Element is not visible !", isElementPresent(element));
	}
	//====================================================================================//

	//========================= Keyword Wait For Image Present =========================//
	@And(".*?wait for image \"(.*?)\" present by css selector$")
	public void waitForImagePresentByCss(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Image is not present !", isElementPresent(element));
	}
	@And(".*?wait for image \"(.*?)\" present by ID$")
	public void waitForImagePresentByID(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Image is not present !", isElementPresent(element));
	}
	@And(".*?wait for image \"(.*?)\" present by name$")
	public void waitForImagePresentByName(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Image is not present !", isElementPresent(element));
	}
	@And(".*?wait for image \"(.*?)\" present by text$")
	public void waitForImagePresentByText(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Image is not present !", isElementPresent(element));
	}
	@And(".*?wait for image \"(.*?)\" present by xpath$")
	public void waitForImagePresentByXpath(String locator) {
		WebElement element = waitForElement(driver, locator);
		assertTrue("Image is not present !", isElementPresent(element));
	}
	//==================================================================================//

	//========================= Keyword Wait For JQuery Load =========================//
	@And(".*?wait for JQuery load$")
	public void waitForJqueryLoad() {
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;
		try {
			ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) driver)
					.executeScript("return jQuery.active") == 0);
			boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");
			if (!jqueryReady) {
				waitUntilExpectedCondition(driver, jQueryLoad);
			}
		} catch (WebDriverException ignored) {
		}
	}
	//===============================================================================//

	//========================= Keyword Wait For Page Load =========================//
	@And(".*?wait for page load$")
	public void waitForPageLoad() {
		ExpectedCondition<Boolean> expectation = new
				ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
			}
		};
		try {
			waitUntilExpectedCondition(driver, expectation);
		} catch (Throwable error) {
			AssertJUnit.fail("Timeout waiting for Page Load Request to complete.");
		}
	}
	//==============================================================================//
	
	//========================= Keyword Multiple Upload =========================//
	@And("I multiple upload in \"(.*?)\" by ID")
	public void multipleUploadFileByID(String locator, DataTable table ) {
		List<List<String>> rows = table.asLists(String.class);
		String finalPath = "";
		for (int i = 0; i <= rows.size()-1 ; i++) {
			finalPath = finalPath.concat(rows.get(i).toString()+"\n");
		}
		finalPath = finalPath.replaceAll("\\[|\\]", "");
		finalPath = finalPath.substring(0, (finalPath.length() - 1));	
		System.out.println(finalPath);
		WebElement element = driver.findElement(By.id(locator));
		element.sendKeys(finalPath);
	}
	@And("I multiple upload in \"(.*?)\" by css selector")
	public void multipleUploadFileByCssSelector(String locator, DataTable table ) {
		List<List<String>> rows = table.asLists(String.class);
		String finalPath = "";
		for (int i = 0; i <= rows.size()-1 ; i++) {
			finalPath = finalPath.concat(rows.get(i).toString()+"\n");
		}
		finalPath = finalPath.replaceAll("\\[|\\]", "");
		finalPath = finalPath.substring(0, (finalPath.length() - 1));	
		System.out.println(finalPath);
		WebElement element = driver.findElement(By.cssSelector(locator));
		element.sendKeys(finalPath);
	}
	@And("I multiple upload in \"(.*?)\" by name")
	public void multipleUploadFileByName(String locator, DataTable table ) {
		List<List<String>> rows = table.asLists(String.class);
		String finalPath = "";
		for (int i = 0; i <= rows.size()-1 ; i++) {
			finalPath = finalPath.concat(rows.get(i).toString()+"\n");
		}
		finalPath = finalPath.replaceAll("\\[|\\]", "");
		finalPath = finalPath.substring(0, (finalPath.length() - 1));	
		System.out.println(finalPath);
		WebElement element = driver.findElement(By.name(locator));
		element.sendKeys(finalPath);
	}
	@And("I multiple upload in \"(.*?)\" by text")
	public void multipleUploadFileByText(String locator, DataTable table ) {
		List<List<String>> rows = table.asLists(String.class);
		String finalPath = "";
		for (int i = 0; i <= rows.size()-1 ; i++) {
			finalPath = finalPath.concat(rows.get(i).toString()+"\n");
		}
		finalPath = finalPath.replaceAll("\\[|\\]", "");
		finalPath = finalPath.substring(0, (finalPath.length() - 1));	
		System.out.println(finalPath);
		WebElement element = driver.findElement(By.xpath("//*[text() = '"+locator+"']"));
		element.sendKeys(finalPath);
	}
	@And("I multiple upload in \"(.*?)\" by xpath")
	public void multipleUploadFileByXpath(String locator, DataTable table ) {
		List<List<String>> rows = table.asLists(String.class);
		String finalPath = "";
		for (int i = 0; i <= rows.size()-1 ; i++) {
			finalPath = finalPath.concat(rows.get(i).toString()+"\n");
		}
		finalPath = finalPath.replaceAll("\\[|\\]", "");
		finalPath = finalPath.substring(0, (finalPath.length() - 1));	
		System.out.println(finalPath);
		WebElement element = driver.findElement(By.xpath(locator));
		element.sendKeys(finalPath);
	}
	//===========================================================================//
	
	
	@And(".*?hover at element \"(.*?)\" by xpath$")
	public void hover(String locator) {
		WebElement element = waitForElement(driver, locator);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}
	
	
	
	@And(".*? get session ID \"(.*?)\" by css selector$")
	public void getSessionIDAfterUpload(String locator) {
		sessionID = getSessionID(driver, locator);
	}
	
	@And(".*? verify status \"(.*?)\" on table by session ID$")
	public void handleTable(String expectedStatus) {
		WebElement Table = driver.findElement(By.cssSelector("#myMenu > tasklist > div.row > div.panel.panel-default > div.panel-body > div > table > tbody"));
		List<WebElement> rows_table = Table.findElements(By.tagName("tr"));
		for (int row = 0; row < rows_table.size(); row++) {
			List<WebElement> Columns = rows_table.get(row).findElements(By.tagName("td"));
			for (int col = 0; col < Columns.size(); col++) {
				if (Columns.get(0).getText().contains("01OPTIMUS120071315073252300077")) {
					Columns.get(5).findElement(By.tagName("span")).getText().contains(expectedStatus);
		            break;
		        }
			}
		}
	}
	
	
	@And(".*? set date picker \"(.*?)\" with value \"(.*?)\"$")
	public void selectDatePicker(String locator, String value) {
		setValue(value);
		driver.findElement(By.id(locator)).click();
		selectYear(driver);
		selectMonth(driver);
		selectDate(driver);
	}

	//===========================WEB SERVICE KEYWORD=====================================//
		@Given(".*? set endpoint \"(.*?)\"$")
		public void get(String URL) {
			addURL = URL;
			System.out.println("Add URL :"+ URL);
		}

		//===========================WEB SERVICE KEYWORD - Request Header=====================================//
		@And("^I set request Headers$")
		public void setHeader() {
			headers = new HttpHeaders();
		}

		@And(".*?add request header \"(.*?)\" with value \"(.*?)\"$")
		public void addRequestHeader(String header, String value) {
			headers.add(header, value);
		}

		@Given(".*? get data from response \"(.*?)\"$")
		public void getData(String dataID) {
			setToken(JsonPath.read(responseBody, "$."+ dataID).toString());
		}

		@Given(".*? input text \"(.*?)\" from API Data$")
		public void inputDataAPI(String element) {
			WebElement ele = driver.findElement(By.id(element));
			ele.sendKeys(postData);
		}

		@And(".*? reset header$")
		public void resetHeader(){
			headers = null;
		}

		@And(".*?set token in header$")
		public void setTokenInHeader() {
			System.out.println("Token value is "+getToken());
			headers.add("Authorization", "Bearer "+getToken());
		}
		
		
		
		private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
	            new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers(){
	                    return null;
	                }
	                public void checkClientTrusted( X509Certificate[] certs, String authType ){}
	                public void checkServerTrusted( X509Certificate[] certs, String authType ){}
	            }
	        };

	    public  static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
	        // Install the all-trusting trust manager
	        final SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    }

	    public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
	        // Return it to the initial state (discovered by reflection, now hardcoded)
	        SSLContext.getInstance("SSL").init( null, null, null );
	    }
	    
		//===========================WEB SERVICE KEYWORD - Get HTTP Request=====================================//
		@When("^I send a GET HTTP request$")
		public void sendRequestGet() throws KeyManagementException, NoSuchAlgorithmException, InterruptedException {
			HttpEntity<String>entity = new HttpEntity<String>(headers);	
			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}

				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			});
			turnOffSslChecking();
			ResponseEntity<String> response = restTemplate.exchange(addURL, HttpMethod.GET, entity, String.class);
			responseBody = response.getBody().toString();
			responseStatusCode = response.getStatusCode().toString();
		}

		@When(".*? post login$")
		public void testCallStepDef() throws KeyManagementException, NoSuchAlgorithmException, InterruptedException {
			get("https://dsme-ms-auth-integral-dev.apps.ms.dev.corp.btpn.co.id/login");
			setHeader();
			addRequestHeader("Content-type","application/json");
			addRequestHeader("Cookie","7644089a1af6a6a4b4c692f8b0a3d0a9=2c4403407583b26838dbe7b75116e05c");
			addRequestHeader("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjaWYiOiIwMDE5QjQiLCJpYXQiOjE1OTg0MTQzOTgsImV4cCI6MTU5ODkzMjc5OCwiYXVkIjoiZHNtZS11c2VyIiwiaXNzIjoiZHNtZS1tcy1hdXRoIiwic3ViIjoiMDAxOUI0In0.Jb-A6KWLjOxImffI25rhGK7LEjUVazgzl_wLC59WE17i7pNIycktchnFNExt-F_X1LUiZ1L98W8PUPV4YBRKBx0RhfVnbc-iqICpNikUt0bmyJr7QQoO8G1lGHhh16ogInbc9rJeWhennsMGpF1VCPoKffFkbAOxOo5EFC9l2qwdMlla_nXs1RsdJE3BV6WpHKLp2f--NH6zeqm3N0pSdInoCmOQ9xb-TnOM4HB3gb5Rd7_Y3iSwf2m9CKvOlQcmkFDVJnnCu1zDD2KIpGjncCv5PnIEDgutfVU2AQFwqmEI5hwmQAeyvCzkdFt3Qsg2RayaXnyo2gtCcRK66Ew2V9zTut_rFc30FmZ1XTfE4S0QThFd-AZAIzp74eeDuc7Fcm_tCNKv2LQenBqUy5Br3johoXIEpCuTZf8MLLXbQrfhCHEWWHDJPb7sY9ZfSHiJHAnINVuvMipl39fRWvoo6kzgSpzy4QGh5vuvCcX5Aqc__o2k5i8IRwxYYyKOHVB6zA7VtQsorbGRJKkC9IgZtFZYFBtTB7vgQoWbAQ-PnmOfWDqnkfs8Fb0SD7ltRnOwCU9kUNiR5HTkLIG-QZ5MMW1iwSE4heIqvY4gjy2fb2ObmzrcZUEzRLOriwOUc-WbVkhnIP6ukctWd9NG6BuKYD-fIiewaSTWLF0l0wroEww");
			sendRequestPost("{\"username\": \"customer\",\"password\": \"Coba@123\"}");
			verifyStatusCode("200 OK");
			getData("token");
			resetHeader();
		}

		//===========================WEB SERVICE KEYWORD - POST HTTP Request=====================================//
		@When("^I send a POST HTTP request with body \'(.*?)\'$")
		public void sendRequestPost(String Body) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException  {
			String jsonBody = Body;
			HttpEntity<String>entity = new HttpEntity<String>(jsonBody, headers); 

			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			});
			turnOffSslChecking();
			ResponseEntity<String> response = restTemplate.postForEntity(addURL, entity, String.class);	
			responseBody = response.getBody().toString();
			responseStatusCode = response.getStatusCode().toString();
			System.out.println("Response Body is : "+responseBody);
			System.out.println("Response Status is : "+responseStatusCode);
		}

		@And(".*? verify status code is return \"(.*?)\"$")
		public void verifyStatusCode(String statusCode) {
			assertEquals(responseStatusCode, statusCode);
			System.out.println("HTTP Status Code is " + responseStatusCode);
		}

		//===========================WEB SERVICE KEYWORD - PUT HTTP Request=====================================//
		@And("^I send a PUT HTTP request with body \'(.*?)\'$")
		public void sendRequestPut(String Body) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException  {
			String jsonBody = Body;
			HttpEntity<String>entity = new HttpEntity<String>(jsonBody, headers);
			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			});
			turnOffSslChecking();
			ResponseEntity<String> response = restTemplate.exchange(addURL, HttpMethod.PUT, entity, String.class);
			responseBody = response.getBody().toString();
			System.out.println("Response Body is : "+responseBody);
		}

		//===========================WEB SERVICE KEYWORD - DELETE HTTP Request=====================================//
		@And("^ I send a DELETE HTTP request$")
		public void sendRequestPut() throws KeyManagementException, NoSuchAlgorithmException, InterruptedException  {
			HttpEntity<String>entity = new HttpEntity<String>(headers); 
			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			});
			turnOffSslChecking();
			ResponseEntity<String> response = restTemplate.exchange(addURL, HttpMethod.DELETE, entity, String.class);
			responseBody = response.getBody().toString();
			System.out.println("Response Body is : "+responseBody);
		}
	
		//===========================WEB SERVICE KEYWORD - VERIFY DATA CONTAINS=====================================//
		@And(".*? verify response contains root \"(.*?)\" with key \"(.*?)\" and value \"(.*?)\"$")
		public void verifyDataContains(String root, String key, String data) {
			//		Integer posts = JsonPath.read(responseBody, "$."+ root +".length()");
			//		System.out.println(posts);
			//		if(posts >= 1) {
			//			for(int i=0; i < posts; i++) {
			//	            String post_id = JsonPath.read(responseBody,"$."+ root +"[" + i + "]"+ key +" ");
			//	            if(post_id == data) {
			//	            	 assertEquals("EQUALS", post_id, data);
			//	            	System.out.println(post_id);
			//	            	break;
			//	            }
			//	        }
			//		}else {
			String postData = JsonPath.read(responseBody, "$."+ root +"."+ key +"");
			System.out.println("================="+postData);
			assertEquals("EQUALS", postData, data);
			//		}
		}

		@And(".*? verify response contains text \'(.*?)\'$")
		public void verifyDataContainsText(String text) {
			System.out.println("RESPONSE BODY =========> "+responseBody);
			assertTrue("Not Matched", responseBody.contains(text));
		}		
}