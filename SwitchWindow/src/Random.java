import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Random {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		
		driver.get("http://10.159.40.59/csa/");
		
		//first switch
		driver = switchWindow(driver, 2, "Accelerator Desktop");
		
		//login cred
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainContentFrame");
		driver.findElement(By.id("logonForm:usernamefld")).sendKeys("pano");
		driver.findElement(By.id("logonForm:passwordfld")).sendKeys("pano");
		
		//mainContentFrame
		
		driver.findElement(By.id("logonForm:submit")).click();
		
		//2nd switch to select roles.
		System.out.println("The second phase.");
		driver = switchWindow(driver, 3, "Roles");
		
		driver.findElement(By.id("rolesForm:rowTable:1:typeValue")).click();
		
		driver.findElement(By.id("rolesForm:successOK")).click();
		
		
		//click ok and then switch back to the 3rd window for the other details
		driver = switchWindow(driver, 2, "Accelerator Desktop");
		
	}
	
	
	public static WebDriver switchWindow(WebDriver d2, int winno, String winTitle)
	{
		long waittime = (long) 20.0;
		long start = System.nanoTime(); 
		Double t;
		String whandles= null;
		int flag = 0;
		
		
		try{
		while(waittime > (long)(System.nanoTime()-start)/1000000000.0)
		{	t = (System.nanoTime()-start)/1000000000.0;
			//System.out.println(t.toString());
			whandles = d2.getWindowHandles().toString();
			String warray[] = whandles.split(",");
			//System.out.println("All handles: "+ whandles);
			
			if (warray.length>=winno)
			{	//System.out.println("All handles: "+ whandles);
				warray[winno-1] = warray[winno-1].substring(1, 37);
				//System.out.println("Switching to: "+ warray[winno-1]);
				d2.switchTo().window(warray[winno-1]);
				flag =2;
				if (winTitle==d2.getTitle().toString() || winTitle.equals(d2.getTitle().toString()) || winTitle == "notitle")
				{	flag = 1;	
					//System.out.println("Switched succesfully to the window " + d2.getTitle().toString());
					break;		
				}
				
			}
		}
		}catch(Exception e)
		{
			System.out.print("Exception occured. " + e.getMessage() + e.getStackTrace());
		}
		
		
		if (flag ==0)
			System.out.println("Window no. not available. ");
		else if(flag ==1)
			System.out.println("Switched succesfullly to : "+ d2.getTitle().toString() );
		else if(flag ==2)
			System.out.println("Window title doesnt match. ");
		
		
		return d2;
	
	}

}
