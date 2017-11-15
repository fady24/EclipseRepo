package com.csc.ignasia.selenium.keywords;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.csc.ignasia.selenium.HtmlObject;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.fsg.ignasia.dao.common.SeleniumExecutorDAO;
import com.csc.fsg.ignasia.model.common.ComponentModel;

/**
Keyword Id = 'Sample'
Please see FLAME Java Docs at
http://flame-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/

@Service("ZurichDbTest")
public class ZurichDbTestKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		String msg;
		ResultSet rs;
		//read data from testData map using Component model params
		try{
			String testchk = testData.get(componentModel.getParam1()).toString();
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
			Statement stmt=con.createStatement();
			
			rs=stmt.executeQuery("Select * from ZMUIPF");
			while(rs.next())  
				msg = rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3);  
			
			//JOptionPane.showMessageDialog(null, "hey");
			
			//System.out.print(msg);
			con.close();  

			
		}catch(Exception e)
		{
			msg = "Exception caught." +  e.getMessage().toString() +  e.getStackTrace().toString();
			result.setResult(false);
		}
		//Set message property of KewyWordResult object
		result.setMessage(msg);
		result.setResult(true);
		return result;
	}

}