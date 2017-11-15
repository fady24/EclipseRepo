import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SearchAndScrollLog {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		
		WebDriver driver = new InternetExplorerDriver();
		
		String logURL = "http://192.168.2.11/integral/logs/";
		String logEntry = "admin-2017-07-26.";
		int flag =0, i = 0;
		String msg = new String();
		msg = "";
		WebElement element1;
		
		//try{
		
		logEntry = logEntry.trim();
		
		driver.get(logURL);
		Thread.sleep(500);
		
		while(flag == 0 && i <= 100)
		{	
			try
			{	System.out.println("trying: " + logEntry + i + ".log");
				driver.findElement(By.linkText(logEntry + i + ".log"));		
			}catch(NoSuchElementException e)
			{	System.out.println("Looked for: " + logEntry + i + ".log");
				i--;
				flag =1;	
				break;
			}
			i++;
		}
		if (flag == 0)
		{
			msg = msg + "Couldnt find any entry in log for this date.";
		}else
		{
			WebElement element = driver.findElement(By.linkText(logEntry + i + ".log"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			element.click();
		}
		
		
		System.out.println(msg);
		System.out.println("completed.");
		
	
		
		/*}catch(Exception e)
		{
			msg = "Exception occured. " + e.getMessage() + e.getStackTrace();
			System.out.println(msg + "Exception occured: " + e.getMessage() + e.getStackTrace() );
		}*/
	}
}
