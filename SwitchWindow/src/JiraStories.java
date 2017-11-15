

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JiraStories {

	
public void launchjira()
{   
try
{
	// log into Jira
	System.setProperty("webdriver.ie.driver", "D:\\selenium\\IEDriverServer.exe");
	WebDriver driver = new InternetExplorerDriver();
	driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
 	driver.get("https://jira.csc.com/");
	WebElement login_path = driver.findElement(By.linkText("Login with username and password"));
	login_path.click();
	WebElement JirauserName = driver.findElement(By.name("os_username"));
	JirauserName.sendKeys("schauhan46");
	WebElement JirapassWord = driver.findElement(By.name("os_password"));
	JirapassWord.sendKeys("schauhan46");
	WebElement JiraLogin = driver.findElement(By.name("login"));
	JiraLogin.click();	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Setting up Excel Read
	
	FileInputStream fis = new FileInputStream("D:\\Panorama\\jiraStory.xlsx");
	XSSFWorkbook xWb = new XSSFWorkbook(fis);
	FileOutputStream outFile = new FileOutputStream("D:\\Panorama\\jiraStory.xlsx");
	XSSFSheet xWs1 = xWb.getSheet("Sheet1");
	DataFormatter formatter = new DataFormatter();
	Row jirasheetheader = xWs1.getRow(0);
	String Jirastoryname = null;
	int  noOfColumns;
	String Jiralinktext = null;
	String JiraLinkTextInfo = null;
	 WebDriverWait wait = new WebDriverWait(driver, 60);
	
	
		for(noOfColumns = 0; noOfColumns <= xWs1.getRow(0).getLastCellNum(); noOfColumns++ )
		{
			Jirastoryname = jirasheetheader.getCell(noOfColumns).getStringCellValue();
			if(Jirastoryname.equalsIgnoreCase("JiraStory"))
			{
			break;
			}
		}
							System.out.println(Jirastoryname);
							System.out.println(xWs1.getPhysicalNumberOfRows());
							int NumberOfRows = xWs1.getPhysicalNumberOfRows()-1;
							System.out.println(NumberOfRows);
							for (int rowIndex_smoke = 1; rowIndex_smoke <= NumberOfRows ; rowIndex_smoke++)
							{ 
								Row row=xWs1.getRow(rowIndex_smoke);
								System.out.println(row.getRowNum());
								String JiraStoryNumber = formatter.formatCellValue(row.getCell(noOfColumns));
                                wait.until(ExpectedConditions.elementToBeClickable(By.id("quickSearchInput")));
								WebElement JiraSearchBox = driver.findElement(By.id("quickSearchInput"));
								JiraSearchBox.sendKeys(JiraStoryNumber.trim());
								JiraSearchBox.sendKeys(Keys.ENTER);
								System.out.println(JiraStoryNumber);								
                                wait.until(ExpectedConditions.stalenessOf(JiraSearchBox));
                                
								
								Jiralinktext = "";
								JiraLinkTextInfo = "";
								
									if(driver.findElements(By.xpath("//a[@class='link-title']")).size() >=1)
									{
											Thread.sleep(12000);
											List<WebElement> JiraLinksToExcel = driver.findElements(By.xpath("//a[@class='link-title']"));
											for(WebElement Jiraeachexcellink :JiraLinksToExcel )
											{
											System.out.println("jira" +Jiralinktext);
											Jiralinktext = Jiralinktext.concat("||" +Jiraeachexcellink.getAttribute("href"));
											JiraLinkTextInfo = JiraLinkTextInfo.concat("||" +Jiraeachexcellink.getText());
											
											}
											Cell cell1 = row.createCell(noOfColumns+1);
										
											cell1.setCellValue(Jiralinktext);
											Cell cell2 = row.createCell(noOfColumns+2);
											
											cell2.setCellValue(JiraLinkTextInfo);
											
									}
									else
									{
										Cell cell1 = row.createCell(noOfColumns+1);
										cell1.setCellValue("No External files linked");
										System.out.println("No such Excel Links Exists for the Story");
									}
									
							}
			
										xWb.write(outFile);
										outFile.close();
										xWb.close();
										fis.close();
										driver.close();
		
}

catch(Exception e)
{
	System.out.println("Error while reading excel" +e);
}

}

public static void main(String[] args)
{
System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");	
JiraStories objJira = new JiraStories();
objJira.launchjira();
}

}
