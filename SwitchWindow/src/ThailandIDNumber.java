package com.csc.ignasia.selenium.keywords;

import java.util.Map;
import java.lang.*;


import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import com.csc.ignasia.selenium.HtmlObject;
import org.openqa.selenium.WebElement;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.ignasia.selenium.keywords.KeywordHandler;
import com.csc.fsg.ignasia.model.common.ComponentModel;


/**
Keyword Id = 'Sample'
Please see IGNASIA Java Docs at
http://ignasia-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/

@Service("SetThailandID")
public class SetThailandIDKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		String idnum = new String();
		int sum= 0, N, C;
		String res;
		char[] digits1;
		WebElement we;
		
		try{
		idnum = testData.get(componentModel.getParam1()).toString();
		
		digits1 = idnum.toCharArray();
		
	
		if (digits1.length != 12)
		{
			result.setResult(false);
			result.setMessage("Wrong Input. The input value must be of length 12.");
			
		}
		else
		{
			sum = Character.getNumericValue(digits1[0]) * 13; 
			sum = sum + Character.getNumericValue(digits1[1]) * 12;
			sum = sum + Character.getNumericValue(digits1[2]) * 11;
			sum = sum + Character.getNumericValue(digits1[3]) * 10;
			sum = sum + Character.getNumericValue(digits1[4]) * 9;
			sum = sum + Character.getNumericValue(digits1[5]) * 8;
			sum = sum + Character.getNumericValue(digits1[6]) * 7;
			sum = sum + Character.getNumericValue(digits1[7]) * 6;
			sum = sum + Character.getNumericValue(digits1[8]) * 5;
			sum = sum + Character.getNumericValue(digits1[9]) * 4;
			sum = sum + Character.getNumericValue(digits1[10]) * 3;
			sum = sum + Character.getNumericValue(digits1[11]) * 2;
			
			
			C = sum/11;

			res =""+(11-(sum-(11*C)));
			we =htmlObject.getWebElement();
			
			we.sendKeys(res);
			
			result.setResult(true);
			result.setMessage("Value :"+ res + " was set successfully to the field.");
			
		}
		
		
		}catch(Exception e)
		{
		result.setResult(false);
		result.setMessage("Exception occured." + e.getMessage() + e.getStackTrace());
		}
		
		
		return result;
	}

}