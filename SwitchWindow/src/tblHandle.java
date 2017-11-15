import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

public class tblHandle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		
		driver.get("https://ec2-52-77-233-16.ap-southeast-1.compute.amazonaws.com/sit/ft/IntegralAsiaWeb");
		Actions action = new Actions(driver);
		long waittime = (long) 60.00;	
		long start = System.nanoTime(); 
		int i = 0;
		while(waittime > (long)(System.nanoTime()-start)/1000000000.0)
		{
			//waiting
			i++;
		}
		System.out.println("wait finsished");
		String msg = new String();
		
		try{
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		msg = "entered. ";
		String toclick = "s2339screensfl.slt_R";
		String fromdata = "O0000224";
		//String fromheader = testData.get(componentModel.getParam3());
		//String scrollcount = testData.get(componentModel1.getParam3());
		
		
		System.out.println("trying table");
		WebElement table1 = driver.findElement(By.xpath("//table[@class='s2339Table sSky sSky-Fixed']/tbody"));
		int col = 1, flag =0;
		int frow = 0;
		//searching
		
		List<WebElement> rows = table1.findElements(By.tagName("tr"));
		List<WebElement> cells;
		String cell1 = new String();
		
		msg = "Started from the bottom. ";
		for(i=4;i<=rows.size();i=i+2)
		{	msg = msg + "in for : " + i;
			
		try{
			cells = rows.get(i-1).findElements(By.tagName("td"));
			cell1 = cells.get(0).getText().trim();
			
		}catch(Exception e3)
		{
			msg = msg + "exception while scanning row. " + e3.getMessage() + e3.getStackTrace();
			
		}
			msg = msg + " cell val = " + cell1;

			if (cell1.equalsIgnoreCase(fromdata)||cell1==fromdata)
			{
				frow = i/2;
				msg = msg + "Row found: " + i;
				flag =1;
				break;
			}
			else
				flag = 0;
		}
		
		if(flag == 0)
		{	
			msg = msg + " No match found for the data. ";
			
		}
		else
		{
			toclick = toclick + frow;
			msg = msg + " Final element to click >" + toclick; 
			driver.findElement(By.xpath("//table[@class='s2339Table sSky sSky-Fixed']/tbody/tr[3]/td[2]")).click();
			System.out.println("clicked ref pt");
			
			while(frow>=4)
			{	System.out.println("scrolling for frow =" + frow);
				action.sendKeys(Keys.DOWN);
                action.perform();
                action.perform();
				frow--;
			}
			
			driver.findElement(By.id(toclick)).click();
			msg = msg + " || " + toclick + " clicked.";
		}
		}catch(Exception e)
		{
			msg = msg + " EXCEPTION OCCURED. " +  e.getMessage() + e.getStackTrace();
			System.out.println(msg);
		}
		System.out.println(msg);
	}

}
