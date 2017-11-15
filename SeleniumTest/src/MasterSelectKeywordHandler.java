package com.csc.flame.selenium.keywords;

import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;
import com.csc.flame.selenium.HtmlObject;
import com.csc.flame.selenium.KeywordResult;
import com.csc.fsg.flame.model.common.ComponentModel;



import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import com.csc.flame.selenium.HtmlObject;
import com.csc.flame.selenium.KeywordResult;
import com.csc.fsg.flame.model.common.ComponentModel;


/**
Keyword Id = 'Sample'
Please see FLAME Java Docs at
http://flame-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/

@Service("MasterSelect")
public class MasterSelectKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		//read data from testData map using Component model params
		//String data = testData.get(componentModel.getParam1())
		
		//Set result property of KeywordResult object
		//result.setResult(true);
		
		//Set message property of KewyWordResult object
		//result.setMessage("Sample keyword executed successfully");
		Actions action = new Actions(driver);
		String msg, valSelected;
		String datasheetVar1 = testData.get(componentModel.getParam1()).toString();
		msg = "";
		try
		{
		if (datasheetVar1 == null || datasheetVar1.equals(""))
			{
			result.setResult(true);
			result.setMessage("Select Skipped");
            }
		else{
			//do all things	
			WebElement dropdown1= htmlObject.getWebElement();
			Select sel1 = new Select(dropdown1);
			result.setResult(true);
			try{
				sel1.selectByValue(datasheetVar1);
				msg = "Selected using complete value.";
			}catch(Exception e){
				msg = "Couldnt select by complete value." + e.getMessage() + " CAUSE: " + e.getCause();
				try{
					sel1.selectByVisibleText(datasheetVar1);
					msg =msg + "Selected using partial value";
				}catch(Exception e2){
					msg =msg + "Couldnt select by complete text, visible text. " + e2.getMessage() + " CAUSE: " + e.getCause();
					try{
					//tab to close dropdown if its in open state
					action.sendKeys(Keys.TAB);
					//click then set value in dropdown then press tab
					dropdown1.click();
					dropdown1.sendKeys(datasheetVar1);
					action.sendKeys(Keys.TAB);
					msg =msg + "Tried by clicking and typing. ";
					}catch(Exception e3){
						msg=msg +"Couldnt select by click and set. " + e3.getMessage() + " CAUSE: " + e.getCause();
					}
				}
			}
		

		// for actual value selected
		WebElement option = sel1.getFirstSelectedOption();
		String selectedval = option.getText();
		if (selectedval.equals(datasheetVar1) || selectedval.contains(datasheetVar1) )
			msg =msg + "Select verified.";
		else
			{
			//to select by index
			try{
				sel1.selectByIndex(1);
			msg = msg + "Automatically selected first element in list using index. ";
			}catch(Exception e4){
				msg = msg + "Couldnt select using index. " + e4.getMessage();
				result.setResult(false);
			}
			
			}
		option = sel1.getFirstSelectedOption();
		valSelected= "Value selected: " + option.getText();
		result.setMessage(msg + valSelected);
		
		}
		
		}
		catch(Exception e9)
		{
			result.setResult(false);
			result.setMessage("Exception occured. " + e9.getMessage() + e9.getStackTrace()  );
		}
		return result;
	}

}
