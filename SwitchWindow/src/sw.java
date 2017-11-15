import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class sw {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String whandles;
		try{
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		String expected_title = "Client Search";
		driver.get("http://10.159.40.59/csa/");
		
		
		
		//  WebDriverWait wait = new WebDriverWait(driver, 15);
	/*	
	driver.findElement(By.name("USERID")).sendKeys("FIREPOC");
	driver.findElement(By.name("PASSWORD")).sendKeys("FIREPOC");
	
	*/
		Set<String> handles = driver.getWindowHandles();
		String mainhandle = driver.getWindowHandle();
	System.out.println("window handle 1 : " + handles );
	
	//driver.findElement(By.id("btnLogOn")).click();
	
	//wait here
		
			
			System.out.println("window handle 1 : " + handles );
			System.out.print("Current window handle : "+ mainhandle + " Title: " + driver.getTitle());
			
	long start = System.nanoTime(); 
	//some try with nested loops 
	int winno = 2, flag = 0;
	Double t;
	
	while(35.0 > (long)(System.nanoTime()-start)/1000000000.0)
	{	t = (System.nanoTime()-start)/1000000000.0;
		System.out.println(t.toString());
		whandles = driver.getWindowHandles().toString();
		Set<String> subhandles = driver.getWindowHandles();
		String warray[] = whandles.split(",");
		System.out.println("All handles: "+ whandles);
		
		
		if (warray.length>=winno)
		{	System.out.println("All handles: "+ whandles);
			warray[winno-1] = warray[winno-1].substring(1, 37);
			System.out.println("Switching to: "+ warray[winno-1]);
			if((!mainhandle.equals(warray[winno-1])))
			{driver.switchTo().window(warray[winno-1]);
			flag = 1;
			break;		
			}
		}
	}
	
	if (flag==0)
	{	
		System.out.println("Waited for 35 secs. Window not found");
	}else
	{
		System.out.println("Switched " + driver.getTitle().toString());
	}
	
	
	System.out.println("Currently in : " + driver.getWindowHandle() +"title " +  driver.getTitle().toString());
	

	
	driver.switchTo().defaultContent();
	driver.switchTo().frame("mainContentFrame");
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	//Login
	//driver.wait(100);
	
	driver.findElement(By.id("logonForm:usernamefld")).sendKeys("jthomas46");
	driver.findElement(By.id("logonForm:passwordfld")).sendKeys("pano");
	driver.findElement(By.id("logonForm:submit"));
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	//driver.wait(100);
	
	driver.switchTo().defaultContent();
	driver.switchTo().frame("desktopMenu");
	//click on customer
	
	//driver.wait(100);
	
	driver.findElement(By.id("acceleratorMenuForm:CustomerCmdText")).click();
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	//click on search
	//driver.wait(100);
	
	driver.switchTo().defaultContent();
	driver.switchTo().frame("contextMenu");
	//click on customer
	
	//driver.wait(100);
	
	driver.findElement(By.id("customerServiceForm:searchText")).click();
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	
	//driver.wait(100);
	
	
	/*
	Thread.sleep(5000);
	//to get the mainhandle
	String mainhandle = driver.getWindowHandle();
	//to get all the handles
	handles = driver.getWindowHandles();
	
	System.out.print("window handle 2 : " + handles );
	
	for(String subhandle : handles ) {
	if(!subhandle.equals(mainhandle)) {
		driver.switchTo().window(subhandle);
	}
	}*/
	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}

	
}
