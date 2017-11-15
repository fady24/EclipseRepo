import java.io.File;
import java.sql.Driver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

public class ClickSp {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
			
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		
		driver.get("http://cscpanapp001:9103/pw/jsp/ProductWizard.jsp");
		
	driver.findElement(By.name("username")).sendKeys("admin");
	driver.findElement(By.name("password")).sendKeys("admin");
	driver.findElement(By.id("pwbutton")).click();

	
	//int x = document.applet.length.toInt;
	
	JavascriptExecutor js = (JavascriptExecutor)driver;
	
	String st = (String) js.executeScript("return document.URL");
	System.out.print(st);
	}

}
