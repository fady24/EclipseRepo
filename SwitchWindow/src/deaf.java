package com.csc.ignasia.selenium.keywords;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import com.csc.ignasia.selenium.HtmlObject;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.ignasia.selenium.keywords.KeywordHandler;
import com.csc.fsg.ignasia.model.common.ComponentModel;
import com.csc.fsg.ignasia.model.common.ObjectRepositoryModel;
import org.openqa.selenium.interactions.Actions;


/**
Keyword Id = 'Sample'
Please see IGNASIA Java Docs at
http://ignasia-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/

@Service("DynamicRowActions")
public class DynamicRowActionsKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		

		
		
		String msg = new String();
		String ftoclick = new String();
		int i = 0;
		try{
		
	
				
		
		//process params
		
		String fromheader = (String)testData.get(componentModel.getParam1());
		String fromdata = (String)testData.get(componentModel.getParam2());
		String toclick = (String)testData.get(componentModel.getParam3());
		String tosel = (String)testData.get(componentModel.getParam3());
		String action1 = (String)testData.get(componentModel.getParam4());
		String value1 = (String)testData.get(componentModel.getParam5());

		String xpathtbl = new String();
		ObjectRepositoryModel orModel = htmlObject.getObjectModel();

		if(orModel.getHtmlXpath() == null || orModel.getHtmlXpath().isEmpty())
		{
			result.setMessage("Please enter XPath for table without tbody/thead tag in the OR");
			result.setResult(false);
			return result;
		}
		else
		{
			xpathtbl = orModel.getHtmlXpath();
		}
		
		msg= "Trying table headers and body.";
		//WebElement table1 = driver.findElement(By.xpath(xpathtbl));
		WebElement tablehead = driver.findElement(By.xpath(xpathtbl+"/thead"));
		WebElement tablebody = driver.findElement(By.xpath(xpathtbl+"/tbody"));
		
		int col = 0, flag =0;
		int frow = 0;
		//searching for header
		
		List<WebElement> rows = tablehead.findElements(By.tagName("tr"));
		List<WebElement> cells;
		String cell1 = new String();
		int j = 0;
		msg = msg +"Rows found: " + rows.size();
		for(i=0;i<rows.size();i=i+1)
		{	msg = msg + " in for : " + i;
			try{
			cells = rows.get(i).findElements(By.tagName("th"));
			for(j=0;j<cells.size();j++)
			{	
				cell1 = cells.get(j).getText().trim();
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
				{	msg = msg + " Match found for header at column: " + (j+1);
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
		msg = msg + "Total rows in body : " + rows.size();
		i=0;
		for(i=0;i<rows.size();i=i+1)
		{	
			try{
			cells = rows.get(i).findElements(By.tagName("td"));
			cell1 = cells.get(col).getText().trim();
			
		}catch(Exception e3)
		{
			msg = msg + "exception while scanning data row " + e3.getMessage() + e3.getStackTrace();
			
		}
			if (cell1.equalsIgnoreCase(fromdata)||cell1==fromdata)
			{
				msg = msg + " Row found to click at: " + i;
				flag =1;
				break;
			}
			else
				flag = 0;
		}
		
		//String making
		String istr = i + "";
		ftoclick = toclick.replace("||", istr);
		
		

		if(action1.equalsIgnoreCase("click")||action1 == "click")
		{	msg = msg + "ID to click on : " + ftoclick;
			WebElement etoclick = driver.findElement(By.id(ftoclick));
			etoclick.click();
			msg = msg + " CLICKED.";
		}
		else if(action1.equalsIgnoreCase("select")||action1=="select")
		{	msg = msg + "ID to select value on : " + ftoclick;
			Select dropdown = new Select(driver.findElement(By.id(ftoclick)));
			dropdown.selectByVisibleText(value1);
			msg = msg + " VALUE SELECTED.";
		}
		else if(action1.equalsIgnoreCase("set")||action1=="set")
		{	msg = msg + "ID to set value on : " + ftoclick;
			WebElement etoset = driver.findElement(By.id(ftoclick));
			etoset.sendKeys(value1);
			msg = msg + " VALUE SET.";
		}
		result.setMessage(msg);
		result.setResult(true);
				
		}catch(Exception e)
		{
			msg = msg + " EXCEPTION OCCURED. " +  e.getMessage() + e.getStackTrace();
			result.setMessage(msg);
			result.setResult(false);
			return result;
		}
		

		result.setMessage(msg);
		return result;
	}


}