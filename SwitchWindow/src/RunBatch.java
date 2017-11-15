import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RunBatch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
		int flag =0;
		String scheNo = new String();
		
		
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		
		File file2 = new File("D:\\S\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file2.getAbsolutePath());
		
		WebDriver driver = new InternetExplorerDriver();
		//WebDriver driver = new ChromeDriver();
		
		driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/");
		Actions action = new Actions(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("userid")));
		driver.findElement(By.id("userid")).sendKeys("pmahajan7");
		driver.findElement(By.id("password")).sendKeys("pmahajan7");
		driver.findElement(By.id("loginId")).click();
		
		//JOptionPane.showMessageDialog(null, "Logged in. ");
		
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		Thread.sleep(1000);
		Thread.sleep(600);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		Thread.sleep(100);
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();

		Thread.sleep(100);
		
		
		
		driver.switchTo().defaultContent();
		//driver.switchTo().frame("activeframe");
		driver.switchTo().frame("mainForm");
		//driver.findElement(By.xpath("//div[3]/div[7]/img")).click();
		//img[contains(@src,'http://192.168.2.11:11012/GroupAsiaWeb1/screenFiles/onePixel.gif')])[35]
		//	//descendant::div[contains(text(),'Endorsement Quotations')]
		Thread.sleep(100);
		//JOptionPane.showMessageDialog(null,  "Going to click now. ");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Schedule Submission')]")).click();
		//JOptionPane.showMessageDialog(null,  "Going to click now. ");
		//driver.findElement(By.xpath("//img[contains(@src,'http://192.168.2.11:11012/GroupAsiaWeb1/screenFiles/onePixel.gif')])[35]")).click();
		
		//Thread.sleep(100);

		//G1MBRDATAI
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.name("membsel")).sendKeys("G1MBRDATAI");
		driver.findElement(By.id("continuebutton")).click();
		Thread.sleep(250);
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
	
		Thread.sleep(250);
		//JOptionPane.showMessageDialog(null, "The jar execution has completed.");
		//Batch job submitted
		
		scheNo = driver.findElement(By.xpath("//div[@class='message']/div")).getText();
		//scheNo = "Schedule G1MBRDATAI/00001004 Submitted";
		scheNo = scheNo.substring(20, 29);
		Thread.sleep(250);
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();

		Thread.sleep(100);
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'W/W Submitted Schedules')]")).click();

		Thread.sleep(100);
		
		
		//checking status
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		
		driver.findElement(By.name("scheduleName")).sendKeys("G1MBRDATAI");
		driver.findElement(By.name("scheduleNumber")).sendKeys(scheNo);
		driver.findElement(By.id("continuebutton")).click();
		
		Thread.sleep(100);
		
		//check status.
		//tr[@id='tablerow1']/td[3]/
		//table[@id='s0080Table']/tbody/tr/td[3].
		//div[@class='sData']/table/tbody/tr/td[3]
		
		while(flag == 0)
		{	
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		//JOptionPane.showMessageDialog(null, "Check ");
		String txt =  driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim();
		System.out.print(txt);
			if (txt.equalsIgnoreCase("completed") || txt == "Completed")
			{	//JOptionPane.showMessageDialog(null, "Completed status found.");
				System.out.println(driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText());
				flag=1;
				break;	
			}
			else 
			{	
				driver.findElement(By.id("refreshbutton")).click();
				//JOptionPane.showMessageDialog(null,(driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim()));
			}
		}
		
		//table[@id='s0080Table']/tbody/tr/td[3]
		//JOptionPane.showMessageDialog(null, "Done");
		
		//logout
		Thread.sleep(1000);

		driver.navigate().to("http://192.168.2.11:11015/ZurichIntegralGroup/logout");
	
		
		
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "EXCEPTION OCCURED. THE APPLICATION COMPONENT WASNT LOADED. "+ e.getMessage().toString() + e.getStackTrace().toString());
		}finally
		{
			driver.quit();
		}
		
	}

}
