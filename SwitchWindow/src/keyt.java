package com.csc.ignasia.selenium.keywords;

import java.util.Map;
import java.lang.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import com.csc.ignasia.selenium.HtmlObject;
import org.openqa.selenium.WebElement;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.ignasia.selenium.keywords.KeywordHandler;
import com.csc.fsg.ignasia.model.common.ComponentModel;


/**
Keyword Id = 'Sample'
Please see FLAME Java Docs at
http://flame-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/

@Service("DynamicSwitchWindow")
public class DynamicSwitchWindowKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		String msg = new String();
		
		if (testData.get(componentModel.getParam1()) == null || testData.get(componentModel.getParam1()).equals(""))
        {   
            result.setResult(true);
            msg= "Switching skipped";
            result.setMessage(" Switching skipped");
            //return result;
        } else
        {
		try{
		String logURL = testData.get(componentModel.getParam1()).toString().trim();
		String logEntry = testData.get(componentModel.getParam2()).toString().trim();
		logEntry =  "admin-"+ logEntry + ".";
		int flag =0, i = 0;
	
		msg = "";
		WebElement element1;
		
		//try{
		
		logEntry = logEntry.trim();
		
		driver.get(logURL);
		Thread.sleep(300);
		
		while(flag == 0 && i <= 100)
		{	
			try
			{	
				driver.findElement(By.linkText(logEntry + i + ".log"));		
			}catch(NoSuchElementException e)
			{	
				i--;
				flag =1;	
				break;
			}
			i++;
		}
		if (flag == 0)
		{
			msg = "Couldnt find any entry in log for this date.";
			result.setMessage(msg);
			result.setResult(false);
			return result;
			
		}else
		{
			WebElement element = driver.findElement(By.linkText(logEntry + i + ".log"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			element.click();
			msg = "Succesfully scrolled to log element and clicked. ";
		}
	
		}catch(Exception e2)
		{
			msg = "Exception occured "+ e2.getMessage() + e2.getStackTrace();
			result.setMessage(msg);
			result.setResult(false);
			return result;
		}
	
		}
		result.setMessage(msg);
		result.setResult(true);
		return result;
	}

}