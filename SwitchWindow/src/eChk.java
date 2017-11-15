package com.csc.ignasia.selenium.keywords;

import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import com.csc.ignasia.selenium.HtmlObject;
import com.csc.ignasia.selenium.KeywordResult;
import com.csc.ignasia.selenium.keywords.KeywordHandler;
import com.csc.fsg.ignasia.model.common.ComponentModel;



/**
Keyword Id = 'Sample'
Please see IGNASIA Java Docs at
http://ignasia-javadocs.s3-website-us-east-1.amazonaws.com
to know more about the referenced classes
**/


@Service("Insert")
public class InsertKeywordHandler implements KeywordHandler {

	public KeywordResult process(WebDriver driver, HtmlObject htmlObject, ComponentModel componentModel, Map testData) {
		KeywordResult result = new KeywordResult();
		
		ResultSet rs;
		String msg = new String();
		msg = "";
		
		try {
		
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:INTEGDB1","vm1dta","vm1dta12#$");
			
			Statement stmt=con.createStatement();
			
			rs=stmt.executeQuery("Insert into ZMUIPF(DATATYPE,CHDRNUM,REFNUM,CLTIND,WORKKEY,CPNSCDE01,SALECHNL,DATESTART,DATEEND,HPROPDTE,CPNCLOSDT,NOTIFDT,DOCRCVDT,INTENT,LSURNAME01,LGIVNAME01,LSURNAME02,LGIVNAME02,CLTDOB01,CLTSEX,CLTPCODE,LCLTADDR01,LCLTADDR02,CLTADDR01,CLTADDR02) values('1','49900112','2017012812345','0','abcd','CMP022','01','20200202','20270128','20170128','20170328','20170128','20170128','1','Bablani','Akshit','Gupta','Ankit','19800101','M','1020073','Bank street','Lane number 42','Tokyo street','Lane number 11')"); 
			rs=stmt.executeQuery("Select * from ZMUIPF");
			while(rs.next())  
				msg = msg + "\n" + rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3);  
				
				con.close();  
				
				result.setResult(true);
				result.setMessage(msg);
				return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
				result.setMessage("Exception occured: " + e.getStackTrace());
				result.setResult(false);
				return result;
		}
		

	}

}