import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EnquireUpload {

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
	public static void main(String[] args) throws InterruptedException, HeadlessException, AWTException, IOException {
		// TODO Auto-generated method stub

		
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		

        // Test the autocomplete response - Explicit Wait
       
		try{
		Calendar now = Calendar.getInstance();
		String timestamp = formatter.format(now.getTime()).toString() + "Enquiry_Screenshots";
		new File("D:\\repo\\" + timestamp).mkdir();
		
		String chdrnum = args[0];
		
		
		File file2 = new File("D:\\S\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file2.getAbsolutePath());
		

		//WebDriver driver = new ChromeDriver();
		
		driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/");
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebDriverWait wait2 = new WebDriverWait(driver, 10);
		
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(By.id("userid")));
		driver.findElement(By.id("userid")).sendKeys("pmahajan7");
		driver.findElement(By.id("password")).sendKeys("pmahajan7");
		driver.findElement(By.id("loginId")).click();
		
		
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.switchTo().frame("frameMenu");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Enquiry Menu')]")).click();

		Thread.sleep(500);
		
		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.switchTo().frame("mainForm");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Member/Ind Policy Enquir')]")).click();
		Thread.sleep(1000);
		
		
		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.switchTo().frame("mainForm");
		Thread.sleep(350);
		
		driver.findElement(By.id("cownsel")).sendKeys(chdrnum);
		Select dropdown1 = new Select(driver.findElement(By.id("polcystats")));
		dropdown1.selectByValue("AP");
		
		driver.findElement(By.id("continuebutton")).click();
		Thread.sleep(500);

		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.switchTo().frame("mainForm");
		Thread.sleep(500);
		driver.findElement(By.partialLinkText("MbrPolOwner")).click();
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\1_MBRPolOwner.png"));
		
		driver.findElement(By.partialLinkText("Policy Details")).click();
		Thread.sleep(500);
		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\2_PolicyDetails.png"));
		Thread.sleep(500);
		
		driver.findElement(By.partialLinkText("Insured")).click();
		Thread.sleep(500);
		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\3_Insured.png"));
		Thread.sleep(500);
		
		driver.findElement(By.partialLinkText("Coverage")).click();
		Thread.sleep(500);
		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\4_Coverage.png"));
		Thread.sleep(500);
		
		driver.findElement(By.partialLinkText("Unique per Endorser")).click();
		Thread.sleep(500);
		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\5_UniqueperEndorser.png"));
		Thread.sleep(500);
		
		
		driver.findElement(By.partialLinkText("Corr Address")).click();
		Thread.sleep(500);
		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("D:\\repo\\" + timestamp + "\\6_CorrAddress.png"));
		Thread.sleep(500);
		
		driver.navigate().to("http://192.168.2.11:11015/ZurichIntegralGroup/logout");
		Thread.sleep(500);
		
		
		
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"Exception occured. Field couldnt be loaded. " + e.getMessage() + e.getStackTrace());	
		}finally{
			driver.quit();
		}
	}

	
	/* public void screenshot() throws Exception
	    {
	        Calendar now = Calendar.getInstance();
	        Robot robot = new Robot();
	        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	        ImageIO.write(screenShot, "JPG", new File("d:\\"+formatter.format(now.getTime())+".jpg"));
	        System.out.println(formatter.format(now.getTime()));
	    }*/
	
}
