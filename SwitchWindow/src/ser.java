package com.csc.ignasia.selenium.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csc.ignasia.selenium.HtmlObject;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.fsg.ignasia.dao.common.SeleniumExecutorDAO;
import com.csc.fsg.ignasia.model.common.ComponentModel;

@Service("VerifyValueTrim")
public class VerifyValueTrimKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		String value1 = new String();

		try {
		
		if (testData.get(componentModel.getParam1()).toString() == null || testData.get(componentModel.getParam1()).toString().equals("")) {
			result.setResult(true);
			result.setMessage("Verification skipped");
			return result;
		}
		else
		{	value1 = testData.get(componentModel.getParam1()).toString().trim();
		
			String valueActual = htmlObject.getWebElement().getText().trim();
			
			
			if (value1.equalsIgnoreCase(valueActual)) {
			   	result.setResult(true);		   	
				result.setMessage("Success. Actual value: "+valueActual+". Expected value: "+ value1);
			} else {
				result.setResult(false);
				result.setMessage("Failed. Actual value: "+valueActual+". Expected value: "+ value1);
			}
		}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("Exception occured. "+ e.getMessage() +" Reason: "+e.getStackTrace());
		}
		return result;
	}
		
}
