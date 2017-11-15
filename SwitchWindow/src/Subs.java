import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Subs {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
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
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		Thread.sleep(100);
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Enquiry Menu')]")).click();

		Thread.sleep(100);
		
		
		
		driver.switchTo().defaultContent();
		//driver.switchTo().frame("activeframe");
		driver.switchTo().frame("mainForm");
		//driver.findElement(By.xpath("//div[3]/div[7]/img")).click();
		//img[contains(@src,'http://192.168.2.11:11012/GroupAsiaWeb1/screenFiles/onePixel.gif')])[35]
		//	//descendant::div[contains(text(),'Endorsement Quotations')]
		Thread.sleep(100);
		//JOptionPane.showMessageDialog(null,  "Going to click now. ");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Member/Ind Policy Enquir')]")).click();
		
		//62138961
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		//actions for enquiry menu
		driver.findElement(By.id("cownsel")).sendKeys("62138961");
		driver.findElement(By.id("continuebutton")).click();

		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		
		String check = new String();
		try{
		driver.findElement(By.name("zdfcncy")).getAttribute("checked");
		check = " checked. ";
		}catch(Exception e)
		{
			System.out.println("Exception in checkbox" + e.getMessage() + e.getStackTrace());
			check = " unchecked. ";
		}finally{
			System.out.println("The checkbox is : " + check);
		}		
		
		
		}catch(Exception e){
				System.out.print("Exception occured. " + e.getMessage()+ e.getStackTrace());
		}
	}

}
