package stepDef;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SupportMethod{
	
	public String expectedDate;
	public String expectedMonth;
	public String expectedYear;
	public String postData;
	public String token;

	public void setBrowser(WebDriver driver, String browser) {
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
				chromeOptions.addArguments("--silent");
				driver = new ChromeDriver(chromeOptions);				
			}
		}else {
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
		}
	}
	public void setBrowserAndOpenUrl(WebDriver driver, String browser, String url) {
		if(System.getProperty("os.name").contains("Mac")) {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/Users/18055967/AutomationFrameworkBDD/WebUI/driver/macOS/geckodriver");
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
				driver = new FirefoxDriver();	
				driver.get(url);
			}else {
				System.setProperty("webdriver.chrome.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/macOS/chromedriver");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				driver = new ChromeDriver(chromeOptions);	
				driver.get(url);
			}
		}else {
			if(browser.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/windows/geckodriver.exe");
				driver = new FirefoxDriver();	
				driver.get(url);
			}else {
				System.setProperty("webdriver.chrome.driver", "/Users/18055966/Documents/SourceCode/GITLAB/AutomationFramework/WebUI/Driver/Windows/chromedriver.exe");
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--silent");
				driver = new ChromeDriver(chromeOptions);
				driver.get(url);
			}
		}
	}

	public static void waitFor(long timeout) {
		long multipliedTimedOut = timeout * 1000;
		try {
			Thread.sleep(multipliedTimedOut);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isElementPresent(WebElement element) {
		boolean condition = false;
		for (int i = 0; i < 150; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean isElementPresent(WebElement element, int timeout) {
		boolean condition = false;
		for (int i = 0; i < timeout; i++) {
			if (checkElement(element)) {
				condition = true;
				break;
			}
		}
		return condition;
	}

	public boolean checkElement(WebElement element) {
		if (!isNoElementAvailable() || element.isDisplayed()){
			try {
				if(isNoElementAvailable()){
					waitFor(2);
				} else if(!isNoElementAvailable() || element.isDisplayed()){
					waitFor(2);
					return true;
				}
			}
			catch (Exception e){
			}
		}
		return false;
	}

	public boolean isNoElementAvailable() {
		return NoSuchElementException.class.desiredAssertionStatus();
	}

	public boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				result = true;
			}
		} catch (Exception e) {}

		return result;
	}

	public static boolean isClickable(WebElement element, WebDriver driver) 
	{
		try{
			WebDriverWait wait = new WebDriverWait(driver, 6);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		}
		catch (Exception e){
			return false;

		}
	}

	public boolean isAlertPresent(WebDriver driver){
		try{
			driver.switchTo().alert();
			return true;
		}catch (NoAlertPresentException e){
			return false;
		}
	}

	public boolean isTextPresent(WebDriver driver, String text) {
		Boolean result = false;
		if (driver.getPageSource().contains(text)) {
			result = true;
			return result;
		}else {
			return result;
		}
	}

	public boolean isOptionValuePresent(WebElement element, String value) {
		Boolean found = false;
		Select select = new Select(element);
		List<WebElement> allOptions = select.getOptions();
		for(int i=0; i<allOptions.size(); i++) {
			if(allOptions.get(i).getAttribute("value").equalsIgnoreCase(value)) {
				found=true;
				break;
			}
		}
		return found;
	}

	public boolean isOptionLabelPresent(WebElement element, String value) {
		Boolean found = false;
		Select select = new Select(element);
		List<WebElement> allOptions = select.getOptions();
		for(int i=0; i<allOptions.size(); i++) {
			if(allOptions.get(i).getText().equalsIgnoreCase(value)) {
				found=true;
				break;
			}
		}
		return found;
	}

	public  void verifyLink(String urlLink) {
		try {
			URL link = new URL(urlLink);
			HttpURLConnection httpConn =(HttpURLConnection)link.openConnection();
			httpConn.setConnectTimeout(2000);
			httpConn.connect();
			if(httpConn.getResponseCode()== 200) {  
				System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			}
			if(httpConn.getResponseCode()== 404) {
				System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			}
			if(httpConn.getResponseCode()== 400) { 
				System.out.println(urlLink+" - "+httpConn.getResponseMessage()); 
			}

			if(httpConn.getResponseCode()== 500) {
				System.out.println(urlLink+" - "+httpConn.getResponseMessage()); 
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public static Boolean isVisibleInViewport(WebElement element) {
		WebDriver driver = ((RemoteWebElement)element).getWrappedDriver();

		return (Boolean)((JavascriptExecutor)driver).executeScript(
				"var elem = arguments[0],                 " +
						"  box = elem.getBoundingClientRect(),    " +
						"  cx = box.left + box.width / 2,         " +
						"  cy = box.top + box.height / 2,         " +
						"  e = document.elementFromPoint(cx, cy); " +
						"for (; e; e = e.parentElement) {         " +
						"  if (e === elem)                        " +
						"    return true;                         " +
						"}                                        " +
						"return false;                            "
						, element);
	}

	public boolean isElementChecked(WebElement element) {
		Boolean result = false;
		if (element.isSelected() == true) {
			result = true;
		}
		return result;
	}

	public String getTitleWindow(WebDriver driver) {
		String title = driver.getTitle();
		return title;
	}

	public int getIndexWindow(WebDriver driver) {
		int index = 0;	
		Set<String> handles = driver.getWindowHandles();
		List<String> windowStrings = new ArrayList<>(handles);
		for (int i = 0; i<windowStrings.size(); i++) {
			if(windowStrings.get(i) == driver.getWindowHandle()) {
				index = i;
				break;
			}
		}
		return index;
	}

	public int getWidth(WebDriver driver) {
		Dimension initial_size = driver.manage().window().getSize();
		int width = initial_size.getWidth();
		return width;
	}

	public int getTopPosotion(WebDriver driver) {
		WebElement htmlElement = driver.findElement(By.tagName("html"));
		Point viewPortLocation = ((Locatable) htmlElement).getCoordinates().onScreen();
		int y = viewPortLocation.getY();
		return y;
	}

	public int getLeftPosotion(WebDriver driver) {
		WebElement htmlElement = driver.findElement(By.tagName("html"));
		Point viewPortLocation = ((Locatable) htmlElement).getCoordinates().onScreen();
		int x = viewPortLocation.getX();
		return x;
	}

	public int getHeight(WebDriver driver) {
		Dimension initial_size = driver.manage().window().getSize();
		int height = initial_size.getHeight();
		return height;
	}

	public String getUrlFromCurrentWindow(WebDriver driver) {
		String url = driver.getCurrentUrl();
		return url;
	}

	public String getTextFromElement(WebElement element){
		String text = element.getText();
		return text;
	}
	public static String getvalueJson(String json, String valueJson) {
		JSONParser parser = new JSONParser();
		JSONObject jsonResponseObject = new JSONObject();
		Object obj = new Object();
		try {
			obj = parser.parse(json);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		jsonResponseObject = (JSONObject) obj;
		String value = jsonResponseObject.get(valueJson).toString();
		return value;
	}

	public void waitUntilExpectedCondition(WebDriver driver, ExpectedCondition<Boolean> expectation) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(expectation);
	}

	public WebElement waitForElement(WebDriver driver, String locator) {
		WebElement element;
		try {
			element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));	
		} catch (Exception e) {
			try {
				element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
			} catch (Exception e2) {
				try {
					element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.name(locator)));
				} catch (Exception e3) {
					try {
						element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text() = '"+locator+"']")));
					} catch (Exception e4) {
						element = (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
					}
				}
			}

		}	
		return element;
	}

	public boolean waitForElementNotHasAttribute(WebDriver driver, WebElement element, String attribute) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
		return result;
	}

	public boolean watiForElementAttributeValue(WebDriver driver, WebElement element, String attribute, String value) {
		Boolean result = (new WebDriverWait(driver, 30)).until(ExpectedConditions.attributeToBe(element, attribute, value));
		return result;
	}
	
	public void setValue(String date){
		String expected = date;
		String[] base = expected.split("\\s+");
		expectedDate = base[0];
		expectedMonth = base[1];
		expectedYear = base[2];
	}
		
	public String getDate(){
		return expectedDate;
	}

	public String getMonth(){
		return expectedMonth;
	}

	public String getYear(){
		return expectedYear;
	}
		
	public void selectYear(WebDriver driver){
		//click Current Month header section
		driver.findElement(By.xpath("/html/body/div[@class='datepicker datepicker-dropdown dropdown-menu datepicker-orient-left datepicker-orient-bottom']/div[1]/table/thead/tr[2]/th[2]")).click();

		//click Current Year
		driver.findElement(By.xpath("/html/body/div[@class='datepicker datepicker-dropdown dropdown-menu datepicker-orient-left datepicker-orient-bottom']/div[2]/table/thead/tr[2]/th[2]")).click();

		WebElement ele = driver.findElement(By.xpath("//span[text()='"+getYear()+"']"));
		while (ele.isDisplayed() == false) {
			//click panah
			driver.findElement(By.xpath("/html/body/div[@class='datepicker datepicker-dropdown dropdown-menu datepicker-orient-left datepicker-orient-bottom']/div[3]/table/thead/tr[2]/th[1]")).click();
		}
		//select year
		driver.findElement(By.xpath("//span[text()='"+getYear()+"']")).click();
	}
	public void selectMonth(WebDriver driver){
		driver.findElement(By.xpath("//span[text()='"+getMonth()+"']")).click();
	}

	public void selectDate(WebDriver driver){
		driver.findElement(By.xpath("//td[text()='"+getDate()+"']")).click();
	}
	
	public String getSessionID(WebDriver driver, String locator) {
		WebElement ele = driver.findElement(By.cssSelector(locator));
		String sessionID = ele.getAttribute("innerHTML").substring(44, 64);
		return sessionID;
	}
	
	public void setToken(String tkn) {
		postData = tkn;
	}
	public String getToken() {
		return postData;
	}	
	
	public void setTokenAuth(String tokenAuth) {
		token = tokenAuth;
	}
	
	public String getTokenAuth() {
		return token;
	}
	
}