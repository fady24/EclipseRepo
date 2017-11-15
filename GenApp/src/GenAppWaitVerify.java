package com.csc.flame.selenium.keywords;

import java.util.Map;
import java.util.Set;

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

@Service("SwitchWindowDynamicWait")
public class SwitchWindowDynamicWaitKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		//read data from testData map using Component model params
		int flag=0;
		String win = testData.get(componentModel.getParam1()).toString();
		int winno = Integer.parseInt(win);
		
		result.setResult(false);			
		 
		String whandles= null, msg= null;
		//Set result property of KeywordResult object
		//result.setResult(true);


		if (testData.get(componentModel.getParam1()) == null || testData.get(componentModel.getParam1()).equals(""))
        {   
            result.setResult(true);
            msg= "Switching skipped";
            result.setMessage(" Switching skipped");
            //return result;
        } else
        {
		try{

			long waittime = (long) 0.00;
		if (testData.get(componentModel.getParam2()) == null || testData.get(componentModel.getParam2()).equals(""))
				waittime = (long) 35.0;
		else
				waittime = Long.parseLong(testData.get(componentModel.getParam2()).toString());

			long start = System.nanoTime(); 
	//some try with nested loops 
	Double t = 0.00;
	
	while(waittime > (long)(System.nanoTime()-start)/1000000000.0)
	{	t = (System.nanoTime()-start)/1000000000.0;
		//System.out.println(t.toString());
		whandles = driver.getWindowHandles().toString();
		Set<String> subhandles = driver.getWindowHandles();
		String warray[] = whandles.split(",");
		//System.out.println("All handles: "+ whandles);
		
		
		if (warray.length>=winno)
		{	//System.out.println("All handles: "+ whandles);
			warray[winno-1] = warray[winno-1].substring(1, 37);
			//System.out.println("Switching to: "+ warray[winno-1]);
			driver.switchTo().window(warray[winno-1]);
			flag = 1;
			
			break;		
		}
	}
	
	if (flag==0)
	{	
		msg = "Waited for " + waittime + " secs. Window not found";
		result.setResult(false);
	}else
	{
		msg = "Waited for : " + t + "secs. Switched to :" + driver.getTitle();
		result.setResult(true);
	}
	
		}catch(Exception e){
			msg ="Exception caught: " + e.getMessage() + e.getStackTrace();
		}

	}
		result.setMessage(msg);
		return result;
	}

}