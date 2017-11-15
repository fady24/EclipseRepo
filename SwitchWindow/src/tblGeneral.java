import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import oracle.*;
import oracle.jdbc.driver.OracleDriver;

public class tblGeneral {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ResultSet rs;
		String msg = new String();
		msg = "";
		
		try {
		
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:INTEGDB1","vm1dta","vm1dta12#$");
			
			Statement stmt=con.createStatement();
			
			rs=stmt.executeQuery("Insert into ZMUIPF(DATATYPE,CHDRNUM,REFNUM,CLTIND,WORKKEY,CPNSCDE01,SALECHNL,DATESTART,DATEEND,HPROPDTE,CPNCLOSDT,NOTIFDT,DOCRCVDT,INTENT,LSURNAME01,LGIVNAME01,LSURNAME02,LGIVNAME02,CLTDOB01,CLTSEX,CLTPCODE,LCLTADDR01,LCLTADDR02,CLTADDR01,CLTADDR02) values('1','49900111','2017012812345','0','abcd','CMP022','01','20200202','20270128','20170128','20170328','20170128','20170128','1','Bablani','Akshit','Gupta','Ankit','19800101','M','1020073','Bank street','Lane number 42','Tokyo street','Lane number 11')"); 
			rs=stmt.executeQuery("Select * from ZMUIPF");
			while(rs.next())  
				msg = msg + "\n" + rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3);  
				
				con.close();  
				
			System.out.print(msg);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		File file = new File("D:\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		
		driver.get("http://cscpanapp001:9104/wma/");
		long waittime = (long) 60.00;	
		long start = System.nanoTime(); 
		int i = 0;
		while(waittime > (long)(System.nanoTime()-start)/1000000000.0)
		{
			//waiting
			i++;
		}
		System.out.println("wait finished");
		String msg = new String();
		String ftoclick = new String();
		String ftosel = new String();
		String dropval = new String();
		dropval= "CASH";
		String oper = "click";
		try{
		
	
				
		String toclick = "mainform:ContractHistoryIndexReturnedHistoryData:||:contractHistoryIndexContractHistoryIndexReturnedHistoryDataReverseInd";
		String tosel = "mainform:ContractHistoryIndexReturnedHistoryData:||:contractHistoryIndexContractHistoryIndexReturnedHistoryDataDisbInd";
		
		
		//process string to replace
		
		
		String fromdata = "NA - Regular Remittance";
		String fromheader = "Trx Code";
		String xpathtbl = "//table[@id='mainform:ContractHistoryIndexReturnedHistoryData']";
		//String scrollcount = testData.get(componentModel1.getParam3());
		
		for (String windowHandle : driver.getWindowHandles()) {
		    // Switches to pop-up window
		    driver.switchTo().window(windowHandle);
		}
		msg = "trying to get table";
		System.out.println("trying table");
		WebElement table1 = driver.findElement(By.xpath(xpathtbl));
		WebElement tablehead = driver.findElement(By.xpath(xpathtbl+"/thead"));
		WebElement tablebody = driver.findElement(By.xpath(xpathtbl+"/tbody"));
		
		int col = 0, flag =0;
		int frow = 0;
		//searching for header
		
		List<WebElement> rows = tablehead.findElements(By.tagName("tr"));
		List<WebElement> cells;
		String cell1 = new String();
		int j = 0;
		System.out.println("Rows found: " + rows.size());
		for(i=0;i<rows.size();i=i+1)
		{	msg = msg + "in for : " + i;
			System.out.println("In loop for row"+ i);
			try{
			cells = rows.get(i).findElements(By.tagName("th"));
			System.out.println(" Found " + cells.size() + "th elements in header");
			for(j=0;j<cells.size();j++)
			{	
				cell1 = cells.get(j).getText().trim();
				System.out.println("Value at cell " + j + "=" + cell1);
				if (cell1 == fromheader || cell1.equalsIgnoreCase(fromheader))
				{
					col=j;
					flag =1;
					break;
				}
				else
					flag =0;
			}
			
			}catch(Exception e2)
			{
				msg = msg + "exception while scanning header " + e2.getMessage() + e2.getStackTrace();
			
			}
		
			if(flag==1)
				{	msg = msg + "Match found for header at j: " + (j+1);
					System.out.println(msg);
					break;
				}
				else
			{
				if(i==rows.size())
				{
					msg = "No match found for header";
				}
			}

		}
		
		//finding the value in row
		
		rows = tablebody.findElements(By.tagName("tr"));
		System.out.println("Total rows in body: " + rows.size());
		i=0;
		for(i=0;i<rows.size();i=i+1)
		{	System.out.println("Looking in table body for: " + i);
			
			try{
			cells = rows.get(i).findElements(By.tagName("td"));
			cell1 = cells.get(col).getText().trim();
			
		}catch(Exception e3)
		{
			msg = msg + "exception while scanning data row " + e3.getMessage() + e3.getStackTrace();
			
		}
			
			msg = msg + " cell val = " + cell1;
			System.out.println(" cell val = " + cell1);
			if (cell1.equalsIgnoreCase(fromdata)||cell1==fromdata)
			{
				msg = msg + "Row found to click: " + i;
				flag =1;
				break;
			}
			else
				flag = 0;
		}
		
		System.out.println("Final element to click :with index" + i);
		
		//String making
		String istr = i + "";
		ftoclick = toclick.replace("||", istr);
		ftosel = tosel.replace("||", istr);
		System.out.println("Clicking element with ID: "+ ftoclick);
		
		if(oper.equalsIgnoreCase("click")||oper == "click")
		{	WebElement etoclick = driver.findElement(By.id(ftoclick));
			etoclick.click();
			etoclick.click();
			
		}
		else if(oper.equalsIgnoreCase("select"))
		{
			Select dropdown = new Select(driver.findElement(By.id(ftoclick)));
			dropdown.selectByIndex(3);
		}
		System.out.println("clicked");
				
		}catch(Exception e)
		{
			msg = msg + " EXCEPTION OCCURED. " +  e.getMessage() + e.getStackTrace();
			System.out.println(msg);
		}
		*/
		
	}

}
