package selenium;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Banking {
	public FileWriter fileWriter = null;
	public String acctNo = null;
	public String fileName = null;
	public String connectID = null;
	public String password = null;
	public String title = null;
	public String tdURL = "https://easyweb.td.com/waw/idp/login.htm?execution=e1s1";
	public String appName = Banking.class.getName();
	public WebDriver driver;
	
	
	public void initByProperties(String propertiesFileName) throws Exception {
		InputStream input = new FileInputStream(propertiesFileName);
		Properties props = new Properties();
		props.load(input);
		input.close();

	
		acctNo = props.getProperty("acctNo");
		fileName = props.getProperty("fileName");
		connectID = props.getProperty("connectID");
		password = props.getProperty("password");
		title = props.getProperty("title");
		title = String.format(title, acctNo);

	}

	public void execute() throws Exception {
		fileWriter = new FileWriter(fileName);
		fileWriter.write(title);
		fileWriter.write("<I>generated on " + new Date() + " by " + appName + " written by Henry</I><BR/>");
		fileWriter.write("<I>Ran from the directory: " + System.getProperty("user.dir") + "</I>");
		
		for (int i = 1; i <=12; i++) {
		driver = new FirefoxDriver();
		gotoAccountDetails();
		saveMonth(i);
		driver.quit();
		}
	}
	
	private void gotoAccountDetails() throws Exception {
		driver.get(tdURL);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webDriver) {
				return webDriver.findElement(By.id("login:AccessCard")) != null;
			}
		});

		WebElement element = driver.findElement(By.id("login:AccessCard"));
		element.sendKeys(connectID);

		element = driver.findElement(By.id("login:Webpassword"));
		element.sendKeys(password);

		element = driver.findElement(By.id("login"));
		element.submit();



		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webDriver) {
				return webDriver.getCurrentUrl().endsWith("/webbanking");
			}
		});
		driver.switchTo().frame("tddetails");
		gotoChequingAccount();
	}

	private void gotoChequingAccount() throws Exception {
		driver.findElement(By.partialLinkText(acctNo)).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Select select = new Select(driver.findElement(By.name("period")));
		select.selectByValue("CTM");

	}


	private void saveLastYear() throws Exception {	
		
		for (int i = 1; i <= 12; i++) {
			saveMonth(i);
		}
	}
	
	
	private void saveMonth(int i) throws Exception {
			
			System.out.println("Saving month: " + i);
			Select select = new Select(driver.findElement(By.name("DateRangeMonth")));
			select.selectByValue(new String("" + i));


			//driver.findElement(By.xpath("//img[contains(@alt,'OK')]")).click();
			driver.findElement(By.xpath("//a[contains(@title,'OK')]")).click();
			
			
			 String pageSource = "";
			(new WebDriverWait(driver, 5)).until(new  ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver webDriver) {
					
					//return driver.getPageSource().length() > 100;
					//return (webDriver.findElement(ByClassName.className("footer")) != null);
					return (webDriver.findElement(By.tagName("footer")) != null);
					//return false;
				}
			});
			
			Thread.sleep(5000);
			//pageSource = ((JavascriptExecutor) driver).executeScript("return document.body.innerHTML", new Object[]{}).toString();
			try {
				driver.switchTo().frame("tddetails");
			} catch (Exception e) {}
			pageSource = driver.getPageSource();
//			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			//pageSource = driver.getPageSource();
			Document doc = Jsoup.parse(pageSource);	
			//saveFile(doc.select("table.element").get(1).toString());
			//saveFile(doc.select("table[class=td-table-border-row]").get(0).toString());
			saveFile(doc.getElementsByClass("td-table-border-row").toString());
			
	}


	private void saveFile(String str) throws Exception {
		System.out.println(str);
		fileWriter.write(str);
		fileWriter.flush();
	}
}
