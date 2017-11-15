import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.junit.internal.runners.model.EachTestNotifier;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BatchJobs {
	public static String[] colreschdr = null;
	public static int chdrctr = 0;
	public static void verifybatch(WebDriver driver, String scheNo, String batch)
	{	try{
		driver.switchTo().defaultContent();
		Thread.sleep(500);
		driver.switchTo().frame("mainForm");
		
		driver.findElement(By.name("scheduleName")).clear();
		driver.findElement(By.name("scheduleNumber")).clear();
		
		driver.findElement(By.name("scheduleName")).sendKeys(batch);
		driver.findElement(By.name("scheduleNumber")).sendKeys(scheNo);
		driver.findElement(By.id("continuebutton")).click();
		int flag =0;
		Thread.sleep(100);
		
		//check status.
		//tr[@id='tablerow1']/td[3]/
		//table[@id='s0080Table']/tbody/tr/td[3].
		//div[@class='sData']/table/tbody/tr/td[3]
		
		while(flag == 0)
		{	
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.switchTo().frame("mainForm");
		//JOptionPane.showMessageDialog(null, "Check ");
		String txt =  driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim();
		System.out.print("for schedule " + scheNo + txt);
			if (txt.equalsIgnoreCase("completed") || txt == "Completed")
			{	//JOptionPane.showMessageDialog(null, "Completed status found.");
				System.out.println(driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText());
				flag=1;
				break;	
			}
			else 
			{	
				driver.findElement(By.id("refreshbutton")).click();
				//JOptionPane.showMessageDialog(null,(driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim()));
			}
		}
		
		driver.switchTo().defaultContent();
		Thread.sleep(300);
		driver.switchTo().frame("mainForm");
		driver.findElement(By.id("exitbutton")).click();
		
	}catch(Exception e)
	{
		System.out.println("Couldnt verify batch completion" + e.getMessage() + e.getStackTrace());
	}
	}
	public static void keyreport(FileWriter fw,int id, String colname, String field, String status, String desc, String stime, String etime  )
	{	try{
		fw.write(System.getProperty("line.separator"));
		
		fw.write("<Keyword>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Key_ID>" + id + "</Key_ID>");
		//fw.write(System.getProperty("line.separator"));
		
		
		fw.write("<Key_Name>" + field + "</Key_Name>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<ObjectName>" + colname + "</ObjectName>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Key_Result>" + status +"</Key_Result>" );
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Key_Message>" + desc + "</Key_Message>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<On_Error>MOVE TO NEXT TESTCASE</On_Error>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Snapshot> No Snapshot </Snapshot>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Key_StartTime>" + stime + "</Key_StartTime>");
		//fw.write(System.getProperty("line.separator"));
		
		fw.write("<Key_EndTime>" + etime + "</Key_EndTime>");
		//fw.write(System.getProperty("line.separator"));
	
		fw.write("</Keyword>");
		fw.write(System.getProperty("line.separator"));
		}catch(Exception e)
	{
			JOptionPane.showMessageDialog(null,"Exception while writing report." + e.getMessage() + e.getStackTrace());
	}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String bd = JOptionPane.showInputDialog("Enter the business date");
		String colres = JOptionPane.showInputDialog("Execute Colres?, Input Y/N");
		
		
		Connection conn1= null;
		File file = new File("D:\\S\\IEDriverServer_Win32_3.3.0 (1)\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		bdchange(driver, bd);
		batches(driver);
		
		//fn for pol data extraction
		int polstat = 0;
		int bpolicies = 0;
		
		
		try{
		conn1 = DriverManager.getConnection(  
				"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
		Statement stmt=conn1.createStatement();
		ResultSet rs;
		String currentpol = null;
		
		rs = stmt.executeQuery("SELECT * FROM STAGEDBUSR.TOTPAMPOLDATA WHERE CHDRNUM ='53018223'");
		DateFormat df2 = new SimpleDateFormat("yyyymmdd  HH_mm_ss");
		Date date = new Date();
		String folder = "D:\\ZurichDBTest\\POLICY_DATA_EXTRACTION_"+ df2.format(date).toString();
		File masterfolder = new File(folder);
		boolean succesful = masterfolder.mkdir();
		String folder2 = folder +"\\POLICYDATAEXTRACTION";
		File TCfolder = new File(folder2);
		succesful = TCfolder.mkdir(); 
		
		File tc = new File(TCfolder+"\\POLICYDATAEXTRACTION.XML");
		FileWriter ftc = new FileWriter(tc);
		ftc.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		ftc.write("<?xml-stylesheet type=\"text/xsl\" href=\"D:\\Report_XSL_Templates\\TestCase.xsl\"?>");
		ftc.write("<TestCase>");
		ftc.write("<Action>");
		ftc.write("<Action1>POLICYDATAEXTRACTION</Action1>");
		
		
		
	
		
		String start = df2.format(date).toString();
		while(rs.next())
		{	bpolicies++;
		
			currentpol = rs.getString("CHDRNUM");
			ftc.write("<Act_ID>" + currentpol + "</Act_ID>");
			ftc.write("<Act_Name>" + currentpol + "</Act_Name>");
			ftc.write("<Act_Result>Fail</Act_Result>");
			ftc.write("<Act_PassedKeyWords>1</Act_PassedKeyWords>");
			ftc.write("<Act_WarningKeyWords>0</Act_WarningKeyWords>");
			ftc.write("<Act_FailKeyWords>1</Act_FailKeyWords>");
			ftc.write("<Act_StartTime>"+ df2.format(date).toString() +"</Act_StartTime>");
			//vpoldata(folder2,currentpol);
			ftc.write("<Act_EndTime>"+ df2.format(date).toString() +"</Act_EndTime>");
			ftc.write("</Action>");
		}
		
		String end = df2.format(date).toString();
		
		ftc.write("<SuiteID>MasterReport</SuiteID>");
		ftc.write("<SuiteName>"+"POLICYDATAEXTRACTION"+"</SuiteName>");
		ftc.write("<Name>"+"POLICYDATAEXTRACTION"+"</Name>");
		ftc.write("<ID>POLICYDATAEXTRACTION</ID>");
		ftc.write("<TC_Status>Fail</TC_Status>");
		ftc.write("<TotalActions>"+ bpolicies +"</TotalActions>");
		ftc.write("<Actions_Pass>0</Actions_Pass>");
		ftc.write("<Actions_Failed>1</Actions_Failed>");
		ftc.write("<StartTime>"+start+"</StartTime>");
		ftc.write("<EndTime>"+end+"</EndTime>");
		ftc.write("<Execution_span>3128.8679 (Seconds)</Execution_span>");
		ftc.write("</TestCase>");
		ftc.close();
		
		
		//write masterxml
		File masterrepo = new File(folder +"\\"+"MasterReport.xml");
		FileWriter fm = new FileWriter(masterrepo);
		fm.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		fm.write("<?xml-stylesheet type=\"text/xsl\" href=\"D:\\Report_XSL_Templates\\Suite.xsl\"?>");
		fm.write("<Suite>");
		fm.write("<TC>");
		fm.write("<TC_ID>POLICYDATAEXTRACTION</TC_ID>");
		fm.write("<TC_Result>Fail</TC_Result>");
		fm.write("<TC_PassedActions>0</TC_PassedActions>");
		fm.write("<TC_FailActions>1</TC_FailActions>");
		fm.write("<TC_StartTime>"+ start +"</TC_StartTime>");
		fm.write("<TC_EndTime>"+ end +"</TC_EndTime>");
		fm.write("</TC>");
		
		fm.write("<Name>POLICYDATAEXTRACTION</Name>");
		fm.write("<ID>MasterReport</ID>");
		fm.write("<Suite_Status>Fail</Suite_Status>");
		fm.write("<TC_Pass>0</TC_Pass>");
		fm.write("<TC_Fail>1</TC_Fail>");
		
		fm.write("<ExecutionDate>" + start + "</ExecutionDate>");
		fm.write("<StartTime>" + start + "</StartTime>");
		fm.write("<EndTime>"+ end +"</EndTime>");
		fm.write("<Execution_span>" + "13112.02" + "(Seconds)</Execution_span>");
		
		fm.write("<Browser_name>"+"Internet Explorer"+" </Browser_name>");
		fm.write("</Suite>");
		fm.close();
		
		//write test case 
	
		//all static
		
		//vpoldata("53018283");
		conn1.close();
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Exception while reading values from staging TOTPAMPOL file."+ e.getMessage()+ e.getStackTrace());
		}
		
		
		
		/////billing/////////////////////////////////////////////////////////////////////////////////////////////////
		try{
		conn1 = DriverManager.getConnection(  
				"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
		Statement stmt=conn1.createStatement();
		ResultSet rs;
		String currentpol = null;
		
		rs = stmt.executeQuery("SELECT * FROM STAGEDBUSR.TITPAMBILDAT WHERE CHDRNUM = '53018225'");
		DateFormat df2 = new SimpleDateFormat("yyyymmdd  HH_mm_ss");
		Date date = new Date();
		String folder = "D:\\ZurichDBTest\\POLICY_BILLING_"+ df2.format(date).toString();
		File masterfolder = new File(folder);
		boolean succesful = masterfolder.mkdir();
		String folder2 = folder +"\\POLICYBILLING";
		File TCfolder = new File(folder2);
		succesful = TCfolder.mkdir(); 
		
		File tc = new File(TCfolder+"\\POLICYBILLING.XML");
		FileWriter ftc = new FileWriter(tc);
		ftc.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		ftc.write("<?xml-stylesheet type=\"text/xsl\" href=\"D:\\Report_XSL_Templates\\TestCase.xsl\"?>");
		ftc.write("<TestCase>");
		ftc.write("<Action>");
		ftc.write("<Action1>POLICYBILLING</Action1>");
		

	
		
		String start = df2.format(date).toString();
		while(rs.next())
		{	bpolicies++;
		
			currentpol = rs.getString("CHDRNUM");
			ftc.write("<Act_ID>" + currentpol + "</Act_ID>");
			ftc.write("<Act_Name>" + currentpol + "</Act_Name>");
			ftc.write("<Act_Result>Fail</Act_Result>");
			ftc.write("<Act_PassedKeyWords>1</Act_PassedKeyWords>");
			ftc.write("<Act_WarningKeyWords>0</Act_WarningKeyWords>");
			ftc.write("<Act_FailKeyWords>1</Act_FailKeyWords>");
			ftc.write("<Act_StartTime>"+ df2.format(date).toString() +"</Act_StartTime>");
			//billing(folder2,currentpol);
			ftc.write("<Act_EndTime>"+ df2.format(date).toString() +"</Act_EndTime>");
			ftc.write("</Action>");
		}
		
		String end = df2.format(date).toString();
		
		ftc.write("<SuiteID>MasterReport</SuiteID>");
		ftc.write("<SuiteName>"+"POLICYBILLING"+"</SuiteName>");
		ftc.write("<Name>"+"POLICYBILLING"+"</Name>");
		ftc.write("<ID>POLICYBILLING</ID>");
		ftc.write("<TC_Status>Fail</TC_Status>");
		ftc.write("<TotalActions>"+ bpolicies +"</TotalActions>");
		ftc.write("<Actions_Pass>0</Actions_Pass>");
		ftc.write("<Actions_Failed>1</Actions_Failed>");
		ftc.write("<StartTime>"+start+"</StartTime>");
		ftc.write("<EndTime>"+end+"</EndTime>");
		ftc.write("<Execution_span>3128.8679 (Seconds)</Execution_span>");
		ftc.write("</TestCase>");
		ftc.close();
		
		
		//write masterxml
		File masterrepo = new File(folder +"\\"+"MasterReport.xml");
		FileWriter fm = new FileWriter(masterrepo);
		fm.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		fm.write("<?xml-stylesheet type=\"text/xsl\" href=\"D:\\Report_XSL_Templates\\Suite.xsl\"?>");
		fm.write("<Suite>");
		fm.write("<TC>");
		fm.write("<TC_ID>POLICYBILLING</TC_ID>");
		fm.write("<TC_Result>Fail</TC_Result>");
		fm.write("<TC_PassedActions>0</TC_PassedActions>");
		fm.write("<TC_FailActions>1</TC_FailActions>");
		fm.write("<TC_StartTime>"+ start +"</TC_StartTime>");
		fm.write("<TC_EndTime>"+ end +"</TC_EndTime>");
		fm.write("</TC>");
		
		fm.write("<Name>POLICYBILLING</Name>");
		fm.write("<ID>MasterReport</ID>");
		fm.write("<Suite_Status>Fail</Suite_Status>");
		fm.write("<TC_Pass>0</TC_Pass>");
		fm.write("<TC_Fail>1</TC_Fail>");
		
		fm.write("<ExecutionDate>" + start + "</ExecutionDate>");
		fm.write("<StartTime>" + start + "</StartTime>");
		fm.write("<EndTime>"+ end +"</EndTime>");
		fm.write("<Execution_span>" + "13112.02" + "(Seconds)</Execution_span>");
		
		fm.write("<Browser_name>"+"Internet Explorer"+" </Browser_name>");
		fm.write("</Suite>");
		fm.close();
		
		//write test case 
	
		//all static
		//colres actions
		//colres select insert
		//batch colres
		
		//verify colres data
		//69696
		//vpoldata("53018283");
		conn1.close();
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Exception while reading values from staging file TITPAMBILDAT."+ e.getMessage()+ e.getStackTrace());
		}
		
		
		try {
			Desktop.getDesktop().open(new File("D:\\ZurichDBTest"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("billing exception" + e.getMessage() + e.getStackTrace());
			e.printStackTrace();
		}
		//fn for billing data
		//billing();
		if(colres.equalsIgnoreCase("N"))
		{
			Colres.main(bd);
		}
		//start reading recordss from the db
	
		//include these with same conn..   69696
		//call 
		
	}
	
	public static String getDBval(ResultSet rs, String ob)
	{ String ret = null;
		try{
			if(rs.getString(ob)==null || rs.getString(ob).length() == 0) 
			{
				ret= "";
			}else
			{
				ret= rs.getString(ob).trim();
			}
		}catch(Exception e22)
		{
			System.out.println("Exception caught in fetching value from resultset for col name " + ob + e22.getMessage() + e22.getStackTrace());
		}
		return ret;
	}
	public static WebDriver getponline(String polnum)
	{
		System.setProperty("webdriver.ie.driver", "D:\\S\\IEDriverServer_Win32_3.3.0 (1)\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		Logger lg = Logger.getLogger(BatchJobs.class);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try{
		
			driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/FirstServlet");
			
			driver.findElement(By.id("userid")).sendKeys("underwr1");
			driver.findElement(By.id("password")).sendKeys("underwr1");
			driver.findElement(By.id("loginId")).click();
			lg.info("logged in");
			System.out.print("logged in!!!");
			//driver.switchTo().frame("mainForm");
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			driver.switchTo().frame("frameMenu");
			
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Enquiry Menu')]")).click();
			driver.switchTo().defaultContent();
			Thread.sleep(500);
			driver.switchTo().frame("mainForm");
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Member/Ind Policy Enquir')]")).click();
			
			System.out.print("submenu clicked");
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			driver.findElement(By.name("cownsel")).clear();
			driver.findElement(By.name("cownsel")).sendKeys(polnum);
			driver.findElement(By.id("continuebutton")).click();
			System.out.print("enquiry data submitted. ");
			
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			
		}catch(Exception edriver)
		{
			System.out.println("Exception while online enquiry of the policy.");
		}
		return driver;	
	}
	public static void billing(String folder, String polnum)
	{	
		//String polnum = "53018283";
		
		WebDriver driver = getponline(polnum);
		
		//driver.findElement(By.id("")).getText();
		boolean cc = true, bank= false;
		int mismatches = 0;
		DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyymmdd  HH_mm_ss");
		Date date = new Date();
		FileWriter fw = null;
		ResultSet rs;
		Connection con= null;
		String ST = df2.format(date).toString();
		int ctr = 0;
		try{
			File f = new File(folder + "\\"+polnum +".XML");
			fw= new FileWriter(f);
			//fw.write("started. " + "\n");
			con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
			Statement stmt=con.createStatement();
		
			String msg,temp,BTDATE = null, PRBILTDT = null, PRBILFDT = null, PPREM = null, DPREM = null, ZCRDTYPE = null;
			String ZBRANCHCD= null, BANKKEY = null, BANKACCKEY = null, BANKACCKEY_O = null, STATCODE =null, STATCODE_Src = null;
			String billtodate = null, FACTHOUS =null , SUMINSU = null;
			String APREM = null, ENDORSER = null, PTYPE = null;
		
			//for 37 used as bank branch code
			fw.write("<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>");
			//fw.write(System.getProperty("line.separator"));
			fw.write("<?xml-stylesheet type="+"\"text/xsl\""+" href=\""+"D:\\Report_XSL_Templates\\Action.xsl\""+"?>");
			//fw.write(System.getProperty("line.separator"));
			fw.write("<Action>");
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			driver.findElement(By.linkText("Unique per Endorser")).click();
			ZBRANCHCD= driver.findElement(By.id("zbranchcd_div")).getText();
			BANKACCKEY_O= driver.findElement(By.id("bnkacckey01_div")).getText();
			driver.findElement(By.linkText("Policy Details")).click();
			ENDORSER = driver.findElement(By.id("zendcode_div")).getText();
			
			driver.close();
			int pass =0, fail =0;
			System.out.println("the values" + BANKACCKEY_O+ ZBRANCHCD + ENDORSER);
			rs=stmt.executeQuery("select * from zendrpf  where zendcde ='" + ENDORSER+ "'");
			System.out.println("<><>endorsercode value ");
			try{
			while(rs.next())
				PTYPE = getDBval(rs,"ZCOLM");
			
						rs=stmt.executeQuery("Select * from GBIH WHERE CHDRNUM ='" + polnum+"'");
						while(rs.next())
						{
							//FOR 28
							PRBILFDT = getDBval(rs,"PRBILFDT");
							
							//for 29
							PRBILTDT = getDBval(rs,"PRBILTDT");
							
						}
						
						rs=stmt.executeQuery("Select * from GCHD WHERE CHDRNUM ='" + polnum +"'");
						while(rs.next())
						{
							
							//FOR 24
							BTDATE = getDBval(rs,"BTDATE");
							
							
							//for 42
							STATCODE_Src = getDBval(rs,"STATCODE");
							
						}
						
			
			
			if (PTYPE.equals("CC"))
			{//only for cc policies
				//6	- Credit Card
				rs=stmt.executeQuery("Select * from GBIH WHERE CHDRNUM ='" + polnum+"'");
				while(rs.next())
				{	temp = getDBval(rs,"PREMOUT");
					if (temp.equals("N"))
					{	msg = ("PREMOUT IN GBIH N");
					keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	
						msg = ("PREMOUT IN GBIH VALIDATION FAILED. VALUE : " + temp + ".EXPECTED : N");
						keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					temp = getDBval(rs,"ZBKTRFDT");
					if(temp.equals(""))
					{
						msg = "ZBKTRFDT VERIFICATION ON GBIH: PASS : EMPTY";
						keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}else
					{
						msg = "ZBKTRFDT VERIFICATION ON GBIH: FAIL : EXPECTED EMPTY. ACTUAL : " + temp ;
						keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}
					
					//temp = getDBval(rs,"ZBKTRFDT");
					//msg = (temp.equals("N"))?("ZBKTRFDT : PASS : N"):("ZBKTRFDT FAILED : "+ temp);
					//fw.write(msg + System.getProperty("line.separator"));
					
				}
				
				//8  + 6
				rs=stmt.executeQuery("Select * from GCHD WHERE CHDRNUM ='" + polnum + "'");
				while(rs.next())
				{	//6
					temp = getDBval(rs,"PTDATE");
					billtodate = getDBval(rs,"BTDATE");
					if (temp.equals(billtodate))
					{	msg = ("PTDATE = BTDAYE ON GCHD : N");
						keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					
					}else
					{	msg = ("PTDATE <> BTDATE ON GCHD : PTDATE: "+ temp + "BTDATE: " + billtodate);
						keyreport(fw, ++ctr, "PBJL_01A_06","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					
					//8
					temp = getDBval(rs,"STATCODE");
					if (temp.equals("IF"))
					{	msg = ("STATCODE ON GCHD : IF");
						keyreport(fw, ++ctr, "PBJL_01A_08","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}else
					{	msg = ("STATCODE ON GCHD : "+ temp);
						keyreport(fw, ++ctr, "PBJL_01A_08","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}
					//fw.write(msg + System.getProperty("line.separator"));	
					
				}
				
				
				//9
				rs=stmt.executeQuery("Select statcode from GCHD WHERE CHDRNUM = (SELECT GRUPNUM FROM STAGEDBUSR.TITPAMBILDAT where CHDRNUM ='" + polnum + "')");
				while(rs.next())
				{	temp = getDBval(rs,"STATCODE");
					
					if (temp.equals("IF"))
					{	msg = "STATCODE ON GCHD FOR MASTER: IF";
					keyreport(fw, ++ctr, "PBJL_01A_09","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	msg = "STATCODE ON GCHD FOR MASTER: "+ temp;
					keyreport(fw, ++ctr, "PBJL_01A_09","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					//fw.write(msg + System.getProperty("line.separator"));
					
				}
				
				
				
			}else
			{//only for bank policies
				
				//7 1
				rs=stmt.executeQuery("Select * from GBIH WHERE CHDRNUM ='" + polnum+ "'");
				while(rs.next())
				{
					temp = getDBval(rs,"PREMOUT");
					if (!temp.equals("N"))
					{	msg = ("PREMOUT ON GBIH : N");
					
					}
					else
					{	msg = ("PREMOUT ON GBIH : "+ temp);
					
					}
					//fw.write(msg + System.getProperty("line.separator"));
					
					//temp = getDBval(rs,"ZBKTRFD");
					//one thing
					
					//FOR 28
					PRBILFDT = getDBval(rs,"PRBILFDT");
					
					//for 29
					PRBILTDT = getDBval(rs,"PRBILTDT");
					
				}
			
				
				
			}
			
		
			//24
			rs=stmt.executeQuery("Select * from STAGEDBUSR.TITPAMBILDAT WHERE CHDRNUM ='" + polnum+"'");
			while(rs.next())
			{	
				//24
				temp =  getDBval(rs,"BTDATE");
				if (temp.equals(BTDATE))
				{	msg = ("BTDATE ON STAGING AND GCHD "+ BTDATE);
					keyreport(fw, ++ctr, "PBJL_01A_24","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
				
				}else
				{	msg = ("BTDATE FAIL GCHD:" + BTDATE  +" STAGING:"+ temp);
					keyreport(fw, ++ctr, "PBJL_01A_24","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
				//fw.write(msg + System.getProperty("line.separator"));
			
				
				//28
				temp = getDBval(rs,"INSTFROM");
				if (temp.equals(PRBILFDT))
				{	msg = ("PRBILTDT ON GBIH = INSTFROM IN STAGING "+ PRBILFDT);
				keyreport(fw, ++ctr, "PBJL_01A_28","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}
				else
				{	msg = ("PRBILTDT ON GBIH :" + PRBILFDT+ "," +" INSTFROM IN STAGING" +  temp);
				keyreport(fw, ++ctr, "PBJL_01A_28","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
				fail++;
				}
				//fw.write(msg + System.getProperty("line.separator"));
				
				//29
				temp = getDBval(rs,"INSTTO");
				if (temp.equals(PRBILTDT))
				{	msg = ("PRBILGDT ON GBIH = INSTFROM ON STAGING."+ PRBILTDT);
				keyreport(fw, ++ctr, "PBJL_01A_29","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}
				else
				{	msg = ("PRBILFDT ON GBIH" + PRBILTDT + "INSTFROM ON STAGING" + temp);
				keyreport(fw, ++ctr, "PBJL_01A_29","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
				fail++;
				}
				//fw.write(msg + System.getProperty("line.separator"));
				
				

				
				//for 35
				ZCRDTYPE = getDBval(rs,"CARDTYP");
				
				//for 37.2
				BANKKEY = getDBval(rs,"BANKKEY");
				
				//FOR 38
				BANKACCKEY = getDBval(rs,"BANKACCKEY");
				
				//for 42
				STATCODE = getDBval(rs,"STATCODE");
				 
				//for 21
				FACTHOUS =getDBval(rs,"FACTHOUS");
				
				//for 30
				SUMINSU = getDBval(rs,"TOTSI");
				
				//31
				APREM = getDBval(rs,"ANNL01");
				//33
				DPREM = getDBval(rs,"EXTR");
				//32
				PPREM = getDBval(rs,"INST01");
				
			}
			
			//21
			rs=stmt.executeQuery("select * FROM ZENDRPF WHERE ZENDCDE = (SELECT ENDSERCD FROM STAGEDBUSR.TITPAMBILDAT where CHDRNUM ='"+ polnum+"')");
			while(rs.next())
			{
				temp =  getDBval(rs,"ZENDFH");
				if (temp.equals(FACTHOUS))
				{	msg = ("ZFACTHOUS ON ENDRPF = FACTHOUS ON STAGING FILE: "+ FACTHOUS);
					keyreport(fw, ++ctr, "PBJL_01A_21","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
				}
				else
				{	msg = ("FACTHOUSE COMARISION FAIL STAGING: "+ FACTHOUS + " ZENDRPF: " + temp);
					keyreport(fw, ++ctr, "PBJL_01A_21","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
				//fw.write(msg + System.getProperty("line.separator"));
			}
			
			//30 
			rs = stmt.executeQuery("SELECT sum(SUMINSU) FROM GXHIPF WHERE CHDRNUM ='" + polnum + "'");
			while(rs.next())
			{
				temp = getDBval(rs,"SUM(SUMINSU)");
				if (temp.equals(SUMINSU))
				{	msg = ("SUM INSURED ON GXHIPF AND STAGING : " + temp);
				keyreport(fw, ++ctr, "PBJL_01A_30","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}
				else
				{
					msg = ("SUM INSURED MISMATCH. GXHIPF = " + temp + "STAGING :'" + SUMINSU+"'");
					keyreport(fw, ++ctr, "PBJL_01A_30","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
					//fw.write(msg  + System.getProperty("line.separator"));
			}
			
			
			
			//31
			rs = stmt.executeQuery("select sum(APREM) from GAPHPF where CHDRNUM = '" + polnum+"'");
			while(rs.next())
			{
				temp = getDBval(rs,"SUM(APREM)").trim();
				if (temp.equals(APREM))
				{	msg = ("APREM ON GAPHPF AND STAGING: " + temp);
				keyreport(fw, ++ctr, "PBJL_01A_31","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}else
				{
					msg = ("APREM MISMATCH. GAPHPF = " + temp + "STAGING :" + APREM);
					keyreport(fw, ++ctr, "PBJL_01A_31","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
					//fw.write(msg  + System.getProperty("line.separator"));
			}
			
			
			
			//32
			//get the PPRem sum of the bill no to compare witH the other value
			rs = stmt.executeQuery("SELECT SUM(PPREM) FROM GPMD WHERE CHDRNUM ='"+ polnum+"'");
			while(rs.next())
			{
				temp = getDBval(rs,"SUM(PPREM)");
				if (temp.equals(PPREM))
				{	msg = ("PPREM MATCHED ON GPMD AND STAGING : " + temp);
				keyreport(fw, ++ctr, "PBJL_01A_32","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}else
				{
					msg = ("PPREM MISMATCH. GPMD = " + temp + "STAGING :" + PPREM);
					keyreport(fw, ++ctr, "PBJL_01A_32","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
					//fw.write(msg  + System.getProperty("line.separator"));
			}
			
			//33
			//get t
			rs = stmt.executeQuery("SELECT SUM(DPREM) FROM GXHIPF WHERE CHDRNUM ='" + polnum+"'");
			while(rs.next())
			{
				temp = getDBval(rs,"SUM(DPREM)");
				if (temp.equals(DPREM))
				{	msg = ("DPREM MATCHED ON GXHI AND STAGING " + temp);
					keyreport(fw, ++ctr, "PBJL_01A_33","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
				}else
				{
					msg = ("DPREM MISMATCH. GXHI = " + temp + "STAGING :" + DPREM);
					keyreport(fw, ++ctr, "PBJL_01A_33","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
					//fw.write(msg  + System.getProperty("line.separator"));
			}
			
			
			//35
			/*
			rs = stmt.executeQuery("select ZCRDTYPE from ZENCTPF where CHDRNUM ='"+ polnum+"'");
			while(rs.next())
			{
				temp = getDBval(rs,"ZCRDTYPE");
				if (temp.equals(ZCRDTYPE))
				{	msg = ("ZCRDTYPE MATCHED ON GXHI AND STAGING : " + temp);
				keyreport(fw, ++ctr, "PBJL_01A_35","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
				}else
				{
					msg = ("ZCRDTYPE MISMATCH. GXHI = " + temp + "STAGING :" + ZCRDTYPE);
					keyreport(fw, ++ctr, "PBJL_01A_35","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
				}
					//fw.write(msg  + System.getProperty("line.separator"));
			}*/
			
			
			
			//37
			if (BANKKEY.equals(ZBRANCHCD))
			{	msg = ("BANKKEY MATCHED ONLINE AND ON STAGING " + BANKKEY);
				keyreport(fw, ++ctr, "PBJL_01A_37","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
			}else
			{
				msg = ("BANKKEY MISMATCH. ONLINE= " + ZBRANCHCD + "STAGING :" + BANKKEY);
				keyreport(fw, ++ctr, "PBJL_01A_37","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
				fail++;
			}
				//fw.write(msg  + System.getProperty("line.separator"));
			

			
			//38
			if (BANKACCKEY.equals(BANKACCKEY_O))
			{	msg = ("BANKACCKEY MATCHED ONLINE AND STAGING: " + BANKACCKEY);
			keyreport(fw, ++ctr, "PBJL_01A_38","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
			pass++;
			}else
			{
				msg = ("BANKACCKEY MISMATCH. ONLINE= " + BANKACCKEY_O + "STAGING :" + BANKACCKEY);
				keyreport(fw, ++ctr, "PBJL_01A_38","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
				fail++;
			}
				//fw.write(msg  + System.getProperty("line.separator"));
				
			
				
			//42
			if (STATCODE.equals(STATCODE_Src))
			{	msg = ("STATCODE MATCHED ON GCHD AND STAGING: " + STATCODE);
				keyreport(fw, ++ctr, "PBJL_01A_42","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
				pass++;
			}else
			{
				msg = ("STATCODE MISMATCH. GCHD= " + STATCODE_Src + "STAGING :" + STATCODE);
				keyreport(fw, ++ctr, "PBJL_01A_42","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
				fail++;
			}
			}catch(Exception err)
			{	
				System.out.println("Exception in innermost comparisions."  + err.getMessage() + err.getStackTrace());
			}
				fw.write(System.getProperty("line.separator"));	
		
			
				//58  !!! TITPAMBILDA VALIDATION FOR GETTING EMPTY AFTER BATCH RUN
				
				//RUN BATCH JOB G1ZPJBLTR
				
				//CHECK CHDRNUM USED ABOVE DOESNT EXIST NOW IN THE TABLE.
				//rs = stmt.executeQuery("");				
				fw.write("<SuiteID>MasterReport</SuiteID>");
				fw.write("<SuiteName>POLICYBILLING</SuiteName>");
				fw.write("<TestCaseID>POLICYBILLING</TestCaseID>");
				fw.write("<TestCaseName>POLICYBILLING</TestCaseName>");
				fw.write("<Name>POLICYBILLING</Name>");
				fw.write("<Act_Status>Fail</Act_Status>");
				fw.write("<Total_Keywords>"+(pass+fail) +"</Total_Keywords>");
				fw.write("<Keyword_Pass>"+pass+"</Keyword_Pass>");
				fw.write("<Keyword_Failed>"+ fail +"</Keyword_Failed>");
				fw.write("<dataRef>TOTPAMPOLDATA</dataRef>");
				fw.write("<StartTime>"+ST+"</StartTime>");
				fw.write("<EndTime>"+df2.format(date).toString()+"</EndTime>");
				fw.write("<Execution_span>34.86328 (Seconds)</Execution_span>");
				fw.write("</Action>");
			
				
				
			
			fw.close();
			con.close();
		}catch(Exception eout)
		{
			System.out.println("Exception in billing outer try catch. "+ eout.getMessage() + eout.getStackTrace());
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Couldnt close file writing operation.");
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("DB connection couldnt be closed.");
			}
		}
		
	}
	
	public static void bdchange(WebDriver driver, String businessdate){	
		
		Logger lg = Logger.getLogger(BatchJobs.class);
		
		
		
		try{
		driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/FirstServlet");
		
		driver.findElement(By.id("userid")).sendKeys("underwr1");
		driver.findElement(By.id("password")).sendKeys("underwr1");
		driver.findElement(By.id("loginId")).click();
		lg.info("logged in");
		System.out.print("logged in!!!");
		//driver.switchTo().frame("mainForm");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		
		driver.findElement(By.xpath("//descendant::div[contains(text(),'System Administration')]")).click();
		driver.switchTo().defaultContent();
		Thread.sleep(400);
		driver.switchTo().frame("mainForm");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Business Date Setup')]")).click();
		lg.info("in the submenu screen");
		System.out.print("to submenu succesfully!!!");
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.id("ccdateDisp")).clear();
		driver.findElement(By.id("ccdateDisp")).sendKeys(businessdate);
		driver.findElement(By.linkText("Confirm")).click();
		lg.info("submitted change req");
		Alert al = driver.switchTo().alert();
		al.accept();
		System.out.print("alert accepted!!!");
		Thread.sleep(800);
		//driver.switchTo().frame("frameMenu");
		System.out.print("Completed succesfully!!!");
		//driver.close();
		
		//driver.getPageSource().contains("Business date had been sucessfully changed!");
		}catch(Exception e)
		{
			System.out.println("Issue in changing business date" + e.getMessage() + e.getStackTrace());
			lg.info("info msg");
			lg.error(e.getMessage() + e.getStackTrace());
			
		}
		
	}
	public static void batches(WebDriver driver)
	{
		/*
		System.setProperty("webdriver.ie.driver", "D:\\S\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		Logger lg = Logger.getLogger(BatchJobs.class);
		*/
		Logger lg = Logger.getLogger(BatchJobs.class);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//String businessdate = "2017/06/05";
		try{
		/*driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/FirstServlet");
		
		driver.findElement(By.id("userid")).sendKeys("underwr1");
		driver.findElement(By.id("password")).sendKeys("underwr1");
		driver.findElement(By.id("loginId")).click();
		lg.info("logged in");
		System.out.print("logged in!!!");
		//driver.switchTo().frame("mainForm");
		*/
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
		driver.findElement(By.partialLinkText("Home")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Schedule Submission')]")).click();
		lg.info("in the submenu screen");
		System.out.print("to submenu succesfully!!!");
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.name("membsel")).clear();
		driver.findElement(By.name("membsel")).sendKeys("G1ZINSTBILL");
		driver.findElement(By.id("continuebutton")).click();
		System.out.print("G1ZINSTBILL submitted");
		String scheNo3 = driver.findElement(By.xpath("//div[@class='message']/div")).getText();
		//scheNo = "Schedule G1MBRDATAI/00001004 Submitted";
		scheNo3 = scheNo3.substring(20, 29);
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.name("membsel")).clear();
		driver.findElement(By.name("membsel")).sendKeys("G1ZPOLDATA");
		driver.findElement(By.id("continuebutton")).click();
		System.out.println("G1ZPOLDATA submitted");
		driver.switchTo().defaultContent();
		Thread.sleep(300);
		driver.switchTo().frame("frameMenu");
	
		Thread.sleep(250);
		//JOptionPane.showMessageDialog(null, "The jar execution has completed.");
		//Batch job submitted
		
		String scheNo1 = driver.findElement(By.xpath("//div[@class='message']/div")).getText();
		//scheNo = "Schedule G1MBRDATAI/00001004 Submitted";
		scheNo1 = scheNo1.substring(20, 29);
		
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.name("membsel")).clear();
		driver.findElement(By.name("membsel")).sendKeys("G1ZPJBLTR");
		driver.findElement(By.id("continuebutton")).click();
		System.out.println("G1ZPJBLTR submitted");
	
		driver.switchTo().defaultContent();
		Thread.sleep(300);
		driver.switchTo().frame("frameMenu");
		String scheNo2 = driver.findElement(By.xpath("//div[@class='message']/div")).getText();
		//scheNo = "Schedule G1MBRDATAI/00001004 Submitted";
		scheNo2 = scheNo2.substring(20, 29);
		
		
		
		/*
	
		*/
		//verification
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameMenu");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();

		Thread.sleep(500);
		
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainForm");
		driver.findElement(By.xpath("//descendant::div[contains(text(),'W/W Submitted Schedules')]")).click();

		Thread.sleep(500);
		
		verifybatch(driver,scheNo2,"G1ZPJBLTR");
		System.out.println("verification called 2nd time");
		verifybatch(driver,scheNo1,"G1ZPOLDATA");
		System.out.println("checking for instabill");
		verifybatch(driver,scheNo3,"G!ZINSTBILL");
		
		driver.close();
		}catch(Exception e)
		{
			System.out.print("exception caught "+ e.getMessage() + e.getStackTrace());
		}
	}

	public static void vpoldata(String folderc,String polno)
	{
		
		String polstatval = "AP";
		String tempdate ="00/00/00";
		
		DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyymmdd  HH_mm_ss");
		Date date = new Date();
		/*folderc = "D:\\ZurichDBTest\\POLICY_DATA_EXTRACTION_"+ df2.format(date).toString();
		
		File dir = new File(folder);
		
		boolean successful = dir.mkdir();*/
		//JOptionPane.showMessageDialog(null, "");
		String ST = df2.format(date).toString();
		System.setProperty("webdriver.ie.driver", "D:\\S\\IEDriverServer_Win32_3.3.0 (1)\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		Logger lg = Logger.getLogger(BatchJobs.class);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try{
		
			driver.get("http://cust1:11015/ZurichIntegralGroup/");
			
			driver.findElement(By.id("userid")).sendKeys("underwr1");
			driver.findElement(By.id("password")).sendKeys("underwr1");
			driver.findElement(By.id("loginId")).click();
			lg.info("logged in");
			System.out.print("logged in!!!");
			//driver.switchTo().frame("mainForm");
			driver.switchTo().defaultContent();
			driver.switchTo().frame("frameMenu");
			
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Enquiry Menu')]")).click();
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Member/Ind Policy Enquir')]")).click();
			
			System.out.print("submenu clicked");
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			driver.findElement(By.name("cownsel")).clear();
			driver.findElement(By.name("cownsel")).sendKeys(polno);
			try{
			Select stat = (Select) (driver.findElement(By.id("polcystats")));
			stat.selectByValue(polstatval);
			}catch(Exception e)
			{
				System.out.println("Issue in sel");
			}
			driver.findElement(By.id("continuebutton")).click();
			System.out.print("enquiry data submitted. ");
			
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
		
			//MBRPOLOWNER TAB
			driver.findElement(By.linkText("MbrPolOwner")).click();
			String PHSURNAME = driver.findElement(By.id("lsurname01_div")).getText();
			String PHGIVNAME = driver.findElement(By.id("lgivname01_div")).getText();
			//String  = driver.findElement(By.id("")).getText();
			String PHBSURNAMEJ = driver.findElement(By.id("zkanasnm01_div")).getText();
			String PHBGIVNAMEJ = driver.findElement(By.id("zkanagnm01_div")).getText();
			String PHCLTSEX = driver.findElement(By.id("cltsex_div")).getText();
			String PHCLTDOBX = driver.findElement(By.id("clntdobDisp_div")).getText();
			String PHCLTHPHONE   = driver.findElement(By.id("cltphone01_div")).getText();
			String PHCLTOPHONE = driver.findElement(By.id("cltphone02_div")).getText();
			String PHBADD1R = driver.findElement(By.id("zkanaddr01_div")).getText();
			String PHBADD2R = driver.findElement(By.id("cltaddr01_div")).getText();
			String PHBADD3R = driver.findElement(By.id("zkanaddr02_div")).getText();
			String PHBADD4R = driver.findElement(By.id("cltaddr02_div")).getText();
			String PHBADD5R = driver.findElement(By.id("zkanaddr03_div")).getText();
			String PHBADD6R = driver.findElement(By.id("cltaddr03_div")).getText();
			String PHBADD7R = driver.findElement(By.id("zkanaddr04_div")).getText();
			String PHBADD8R = driver.findElement(By.id("cltaddr04_div")).getText();
			String PHBPOSTCODE = driver.findElement(By.id("cltpcode_div")).getText();
			
			//INSURED TAB
			driver.findElement(By.linkText("Insured")).click();
			String ISSURNAME = driver.findElement(By.id("zkanasnm_div")).getText();
			String ISGIVNAME = driver.findElement(By.id("zkangnm_div")).getText();
			
			//template
			//String  = driver.findElement(By.id("")).getText();
			String ISBSURNAMEJ = driver.findElement(By.id("lsurname02_div")).getText();
			String ISBGIVNAMEJ = driver.findElement(By.id("lgivname02_div")).getText();
			String ISCLTSEX = driver.findElement(By.id("cltsex01_div")).getText();
			String ISCLTDOBX = driver.findElement(By.id("cltdobDisp_div")).getText();
			String ISCLTHPHONE   = driver.findElement(By.id("cltphone03_div")).getText();
			String ISCLTOPHONE = driver.findElement(By.id("cltphone04_div")).getText();
			
			String ISBADD1R = driver.findElement(By.id("zkanaddr05_div")).getText();
			String ISBADD2R = driver.findElement(By.id("cltaddr05_div")).getText();
			String ISBADD3R = driver.findElement(By.id("zkanaddr06_div")).getText();
			String ISBADD4R = driver.findElement(By.id("cltaddr06_div")).getText();
			
			String ISBADD5R = driver.findElement(By.id("zkanaddr07_div")).getText();
			String ISBADD6R = driver.findElement(By.id("cltaddr07_div")).getText();
			String ISBADD7R = driver.findElement(By.id("zkanaddr08_div")).getText();
			String ISBADD8R = driver.findElement(By.id("cltaddr08_div")).getText();
			String ISBPOSTCODE = driver.findElement(By.id("cltpcode01_div")).getText();
			
			
			//POLICY DETAILS TAB
			driver.findElement(By.linkText("Policy Details")).click();
			String CCDATE = driver.findElement(By.id("polzcstrdteDisp_div")).getText();
			String CRDATE = driver.findElement(By.id("zcenddteDisp_div")).getText();
			String ENDSERCD = driver.findElement(By.id("zendcode_div")).getText();
			String CAMPAIGN = driver.findElement(By.id("zcpncde_div")).getText();
			String FACTHOUS  = driver.findElement(By.id("facthous_div")).getText();
			
			
			String POLDUR = driver.findElement(By.id("zpolperd_div")).getText();
			String AGNTSEL00 = driver.findElement(By.id("agntnum01_div")).getText();
			String AGNTSEL01 = driver.findElement(By.id("agntnum02_div")).getText();
			String AGNTSEL02 = driver.findElement(By.id("agntnum03_div")).getText();
			String AGNTSEL03 = driver.findElement(By.id("agntnum04_div")).getText();
			String AGNTSEL04 = driver.findElement(By.id("agntnum05_div")).getText();
			
			
			//Unique per endorser TAB
			driver.findElement(By.linkText("Unique per Endorser")).click();
			String PHBANKACCKEY  = driver.findElement(By.id("bnkacckey01_div")).getText();
			String PHBANKACCDSC = driver.findElement(By.id("bankaccdsc01_div")).getText();
			String PHBBKBRCD = driver.findElement(By.id("zbranchcd_div")).getText();
			String PHBBKACTYP = driver.findElement(By.xpath("//div[.='Banck Acc Type']/following-sibling::div[1]")).getText();
			String PHCARDNMB = driver.findElement(By.id("crdtcard_div")).getText();
			String PHCARDEXPM = driver.findElement(By.id("mthto_div")).getText();
			String PHCARDEXPY = driver.findElement(By.id("yearto_div")).getText();
			String AUTHRINUM = driver.findElement(By.id("preautno_div")).getText();

			
			//coverage tab
			driver.findElement(By.linkText("Coverage")).click();
			System.out.println("coverage tab clicked");
			
			
			
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			WebElement element = null;
			//PRODUCT
			String[] CV = {"","","","","","","","","","","","","",};
			String[] SI = {"","","","","","","","","","","","","",};
			String[] PM = {"","","","","","","","","","","","","",};
			int i=1, j=1, k=1;
			try{
			//String[] CV= new String[13];   
			CV[0]="sq9hoscreensfl.prodtyp";
			
			System.out.println("try begins");
			try{
					while(i<10)	
						{
						CV[0]="sq9hoscreensfl.prodtyp" + i;
						System.out.println(CV[0]);
						//CV[i]=driver.findElement(By.id(CV[0])).getAttribute("innerText");
						element = driver.findElement(By.id(CV[0]));
						jse.executeScript("arguments[0].scrollIntoView(true);", element);
						CV[i]=driver.findElement(By.id(CV[0])).getText(); 
						System.out.println("val at " + i + CV[i]);
						i++;
						}
			}
			catch(NoSuchElementException e1)
			{	
				System.out.println("Total values "+ (i-1) );
			}
			--i;
			
			
			element = driver.findElement(By.xpath("//tr/th[contains(text(),'Premium')]"));
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500); 
			
			//SUMINSURED
			//String[] SI= new String[13];   
			
			
			System.out.println("sum insured");
			try{
					while(j<10)	
						{
						SI[0]="sq9hoscreensfl.zuminsu" + j;
						System.out.println(SI[0]);
						element = driver.findElement(By.id(SI[0]));
						jse.executeScript("arguments[0].scrollIntoView(true);", element);
						SI[j]=driver.findElement(By.id(SI[0])).getText(); 
						System.out.println("val at " + j + SI[j]);
						j++;
						}
			}
			catch(NoSuchElementException e2)
			{	
				System.out.println("Total values "+ (j-1) );
			}
			--j;
			
			
			//PREMIUM
			//String[] PM= new String[13];   
			
			
			System.out.println("premium");
			try{
					while(i<10)	
						{
						PM[0]="sq9hoscreensfl.dprem" + k;
						System.out.println(PM[0]);
						element = driver.findElement(By.id(PM[0]));
						jse.executeScript("arguments[0].scrollIntoView(true);", element);
						PM[k]=driver.findElement(By.id(PM[0])).getText(); 
						System.out.println("val at " + k + PM[k]);
						k++;
						}
			}
			catch(NoSuchElementException e)
			{	
				System.out.println("Total values "+ (k-1) );
			}
			--k;
			}catch(Exception e5)
			{
				System.out.println("Exception caught in processing list values" + e5.getMessage() + e5.getStackTrace());
			}
			
			driver.close();
			////////////////////////////////////////////////////////
			//comparing values online to those in DB
			FileWriter fw = null;
			
			Connection con = null;
			try{
				System.out.println("Staring the file writing operations");
				
				File f = new File(folderc + "\\"+ polno+".xml");
				
				//f = new File(f + "//"+"//POLDATA_EXTRACTION.xml");
				fw= new FileWriter(f);
				//fw.write("started. " + "\n");
				con=DriverManager.getConnection(  
						"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
				Statement stmt=con.createStatement();
				ResultSet rs;
				String msg;
				int pass = 0;
				int fail =0;
				int ctr = 0;
				//DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
				//Date date = new Date();
				rs=stmt.executeQuery("Select * from STAGEDBUSR.TOTPAMPOLDATA WHERE POLNUM ='" + polno + "'");
				System.out.println("resultset trav");
				fw.write("<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>");
				//fw.write(System.getProperty("line.separator"));
				fw.write("<?xml-stylesheet type="+"\"text/xsl\""+" href=\""+"D:\\Report_XSL_Templates\\Action.xsl\""+"?>");
				//fw.write(System.getProperty("line.separator"));
				fw.write("<Action>");
				//fw.write(System.getProperty("line.separator"));
				while(rs.next())
				{	System.out.println("resultset 01");
					if (PHSURNAME.trim().equals(getDBval(rs,"PHSURNAME").toString()))
					{	msg = ("PHSURNAME MATCHED : "+ PHSURNAME);
						keyreport(fw, ++ctr,"PHSURNAME", "FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}
					else
					{	msg = ("PHSURNAME MISMATCH DB: "+getDBval(rs,"PHSURNAME") + " ONLINE : "+ PHSURNAME );
						keyreport(fw, ++ctr,"PHSURNAME", "FETCH_VALUE", "Fail", msg, df.format(date), df.format(date));
						fail++;
						//fw.write(msg + System.getProperty("line.separator"));
					}
					if (PHGIVNAME.trim().equals(getDBval(rs,"PHGIVNAME")))
					{	msg = ("PHGIVNAME MATCHED : "+ PHGIVNAME);
						keyreport(fw, ++ctr, "PHGIVNAME","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHGIVNAME MISMATCH DB: "+getDBval(rs,"PHGIVNAME") + " ONLINE : "+ PHGIVNAME );
						keyreport(fw, ++ctr,"PHGIVNAME","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						pass++;}
					//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBSURNAMEJ.trim().equals(getDBval(rs,"PHBSURNAMEJ")))
					{	msg = ("PHBSURNAMEJ : "+ PHBSURNAMEJ);
						keyreport(fw, ++ctr, "PHBSURNAMEJ","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}
					else
					{	msg = ("PHBSURNAMEJ MISMATCH DB: "+getDBval(rs,"PHBSURNAMEJ") + " ONLINE : "+ PHBSURNAMEJ );
						keyreport(fw, ++ctr,"PHBSURNAMEJ","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}
					//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBGIVNAMEJ.trim().equals(getDBval(rs,"PHBGIVNAMEJ")))
					{	msg = ("PHBGIVNAMEJ : "+ PHBGIVNAMEJ);
						keyreport(fw, ++ctr, "PHBGIVNAMEJ","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHBGIVNAMEJ MISMATCH DB: "+getDBval(rs,"PHBGIVNAMEJ") + " ONLINE : "+ PHBGIVNAMEJ );
						keyreport(fw, ++ctr,"PHBGIVNAMEJ","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHCLTSEX.trim().equals(getDBval(rs,"PHCLTSEX")))
					{	msg = ("PHCLTSEX : "+ PHCLTSEX);
						keyreport(fw, ++ctr, "PHCLTSEX","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHCLTSEX MISMATCH DB: "+getDBval(rs,"PHCLTSEX") + " ONLINE : "+ PHCLTSEX );
						keyreport(fw, ++ctr,"PHCLTSEX","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}//fw.write(msg + System.getProperty("line.separator"));
					
					PHCLTDOBX = PHCLTDOBX.trim();
					tempdate = PHCLTDOBX.substring(0, 4) + PHCLTDOBX.substring(5,7) + PHCLTDOBX.substring(8,10);
					if (tempdate.trim().equals(getDBval(rs,"PHCLTDOBX")))
					{
						msg = ("PHCLTDOBX : "+ PHCLTDOBX);
						keyreport(fw, ++ctr, "PHCLTDOBX","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHCLTDOBX MISMATCH DB: "+getDBval(rs,"PHCLTDOBX") + " ONLINE : "+ PHCLTDOBX );
						keyreport(fw, ++ctr,"PHCLTDOBX","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}
					//fw.write(msg + System.getProperty("line.separator"));
					
					
					if (PHCLTHPHONE.trim().equals(getDBval(rs,"PHCLTHPHONE")))
					{	msg = ("PHCLTHPHONE : "+ PHCLTHPHONE);
						keyreport(fw, ++ctr, "PHCLTHPHONE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHCLTHPHONE MISMATCH DB: "+getDBval(rs,"PHCLTHPHONE") + " ONLINE : "+ PHCLTHPHONE );
						keyreport(fw, ++ctr,"PHCLTHPHONE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHCLTOPHONE.trim().equals(getDBval(rs,"PHCLTOPHONE")))
					{	msg = ("PHCLTOPHONE : "+ PHCLTOPHONE);
						keyreport(fw, ++ctr, "PHCLTOPHONE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHCLTOPHONE MISMATCH DB: "+getDBval(rs,"PHCLTOPHONE") + " ONLINE : "+ PHCLTOPHONE );
						keyreport(fw, ++ctr, "PHCLTOPHONE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}
					//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBADD1R.trim().equals(getDBval(rs,"PHBADD1R")))
					{	msg = ("PHBADD1R : "+ PHBADD1R);
						keyreport(fw, ++ctr, "PHBADD1R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;}else
					{	msg = ("PHBADD1R MISMATCH DB: "+getDBval(rs,"PHBADD1R") + " ONLINE : "+ PHBADD1R );
						keyreport(fw, ++ctr, "PHBADD1R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;}
					//fw.write(msg + System.getProperty("line.separator"));
					
					//equals
					
					if (PHBADD2R.trim().equals(getDBval(rs,"PHBADD2R")))
					{	msg = ("PHBADD2R : "+ PHBADD2R);
					keyreport(fw, ++ctr, "PHBADD2R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBADD2R MISMATCH DB: "+getDBval(rs,"PHBADD2R") + " ONLINE : "+ PHBADD2R );
					keyreport(fw, ++ctr, "PHBADD2R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHBADD3R.trim().equals(getDBval(rs,"PHBADD3R")))
					{	msg = ("PHBADD3R : "+ PHBADD3R);
					keyreport(fw, ++ctr, "PHBADD3R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBADD3R MISMATCH DB: "+getDBval(rs,"PHBADD3R") + " ONLINE : "+ PHBADD3R );
					keyreport(fw, ++ctr, "PHBADD3R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBADD4R.trim().equals(getDBval(rs,"PHBADD4R")))
					{	msg = ("PHBADD4R : "+ PHBADD4R);
					keyreport(fw, ++ctr, "PHBADD4R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					
					}else
					{	msg = ("PHBADD4R MISMATCH DB: "+getDBval(rs,"PHBADD4R") + " ONLINE : "+ PHBADD4R );
						keyreport(fw, ++ctr, "PHBADD4R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBADD5R.trim().equals(getDBval(rs,"PHBADD5R")))
					{	msg = ("PHBADD5R : "+ PHBADD5R);
					keyreport(fw, ++ctr, "PHBADD5R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBADD5R MISMATCH DB: "+getDBval(rs,"PHBADD5R") + " ONLINE : "+ PHBADD5R );
					keyreport(fw, ++ctr, "PHBADD5R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
							//System.out.println(getDBval(rs,"PHBADD6R"));
					//issue afetr this 
			
					//msg = (StringUtils.equals(PHBADD6R,(getDBval(rs,"PHBADD6R").trim())))?("PHBADD6R : "+ PHBADD6R):("PHBADD6R MISMATCH DB: "+getDBval(rs,"PHBADD6R") + " ONLINE : "+ PHBADD6R );
					//fw.write(msg + System.getProperty("line.separator"));
					
					///
					if (PHBADD6R.trim().equals(getDBval(rs,"PHBADD6R")))
					{	msg = ("PHBADD6R : "+ PHBADD6R);
						keyreport(fw, ++ctr, "PHBADD6R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}else
					{	msg = ("PHBADD6R MISMATCH DB: "+getDBval(rs,"PHBADD6R") + " ONLINE : "+ PHBADD6R );
					keyreport(fw, ++ctr, "PHBADD6R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}	//System.out.println("Issue2");
					//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBADD7R.trim().equals(getDBval(rs,"PHBADD7R")))
					{	msg = ("PHBADD7R : "+ PHBADD7R);
						keyreport(fw, ++ctr, "PHBADD7R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}else
					{	msg = ("PHBADD7R MISMATCH DB: "+getDBval(rs,"PHBADD7R") + " ONLINE : "+ PHBADD7R );
						keyreport(fw, ++ctr, "PHBADD7R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBADD8R.trim().equals(getDBval(rs,"PHBADD8R")))
					{	msg = ("PHBADD8R : "+ PHBADD8R);
					keyreport(fw, ++ctr, "PHBADD8R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBADD8R MISMATCH DB: "+getDBval(rs,"PHBADD8R") + " ONLINE : "+ PHBADD8R );
						keyreport(fw, ++ctr, "PHBADD8R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
						
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBPOSTCODE.trim().equals(getDBval(rs,"PHBPOSTCODE")))
					{	msg = ("PHBPOSTCODE : "+ PHBPOSTCODE);
					keyreport(fw, ++ctr, "PHBPOSTCODE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBPOSTCODE MISMATCH DB: "+getDBval(rs,"PHBPOSTCODE") + " ONLINE : "+ PHBPOSTCODE );
					keyreport(fw, ++ctr, "PHBPOSTCODE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
			
					
					//INSURED TAB FIELDS
						
					if (ISSURNAME.trim().equals(getDBval(rs,"ISSURNAME")))
					{	msg = ("ISSURNAME : "+ ISSURNAME);
					keyreport(fw, ++ctr, "ISSURNAME","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
						
					}else
					{	msg = ("ISSURNAME MISMATCH DB: "+getDBval(rs,"ISSURNAME") + " ONLINE : "+ ISSURNAME );
					keyreport(fw, ++ctr, "ISSURNAME","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISGIVNAME.trim().equals(getDBval(rs,"ISGIVNAME")))
					{	msg = ("ISGIVNAME : "+ ISGIVNAME);
					keyreport(fw, ++ctr, "ISGIVNAME","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISGIVNAME MISMATCH DB: "+getDBval(rs,"ISGIVNAME") + " ONLINE : "+ ISGIVNAME );
					keyreport(fw, ++ctr, "ISGIVNAME","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
						
					}
					
					if (ISBSURNAMEJ.trim().equals(getDBval(rs,"ISBSURNAMEJ")))
					{	msg = ("ISBSURNAMEJ : "+ ISBSURNAMEJ);
					keyreport(fw, ++ctr, "ISBSURNAMEJ","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					
					}else
					{	msg = ("ISBSURNAMEJ MISMATCH DB: "+getDBval(rs,"ISBSURNAMEJ") + " ONLINE : "+ ISBSURNAMEJ );
					keyreport(fw, ++ctr, "ISBSURNAMEJ","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}
					
					if (ISBGIVNAMEJ.trim().equals(getDBval(rs,"ISBGIVNAMEJ")))
					{	msg = ("ISBGIVNAMEJ : "+ ISBGIVNAMEJ);
					keyreport(fw, ++ctr, "ISBGIVNAMEJ","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					
					}else
					{	msg = ("ISBGIVNAMEJ MISMATCH DB: "+getDBval(rs,"ISBGIVNAMEJ") + " ONLINE : "+ ISBGIVNAMEJ );
					keyreport(fw, ++ctr, "ISBGIVNAMEJ","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISCLTSEX.trim().equals(getDBval(rs,"ISCLTSEX")))
					{	msg = ("ISCLTSEX : "+ ISCLTSEX);
					keyreport(fw, ++ctr, "ISCLTSEX","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISCLTSEX MISMATCH DB: "+getDBval(rs,"ISCLTSEX") + " ONLINE : "+ ISCLTSEX );
					keyreport(fw, ++ctr, "ISCLTSEX","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					
					ISCLTDOBX = ISCLTDOBX.trim();
					tempdate = ISCLTDOBX.substring(0, 4) + ISCLTDOBX.substring(5,7) + ISCLTDOBX.substring(8,10);
					
					if (tempdate.trim().equals(getDBval(rs,"ISCLTDOBX")))
					{	msg = ("ISCLTDOBX : "+ ISCLTDOBX);
					keyreport(fw, ++ctr, "ISCLTDOBX","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					
					}else
					{	msg = ("ISCLTDOBX MISMATCH DB: "+getDBval(rs,"ISCLTDOBX") + " ONLINE : "+ ISCLTDOBX );
					keyreport(fw, ++ctr, "ISCLTDOBX","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					
					
					if (ISCLTHPHONE.trim().equals(getDBval(rs,"ISCLTHPHONE")))
					{	msg = ("ISCLTHPHONE : "+ ISCLTHPHONE);
					keyreport(fw, ++ctr, "ISCLTHPHONE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISCLTHPHONE MISMATCH DB: "+getDBval(rs,"ISCLTHPHONE") + " ONLINE : "+ ISCLTHPHONE );
					keyreport(fw, ++ctr, "ISCLTHPHONE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					/*if (ISCLTOPHONE.trim().equals(getDBval(rs,"ISCLTOPHONE")))
					{	msg = ("ISCLTOPHONE : "+ ISCLTOPHONE);
					
					}
					else
					{	msg = ("ISCLTOPHONE MISMATCH DB: "+getDBval(rs,"ISCLTOPHONE") + " ONLINE : "+ ISCLTOPHONE );
						keyreport(fw, ++ctr, "ISCLTOPHONE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					*/
					if (ISBADD1R.trim().equals(getDBval(rs,"ISBADD1R")))
					{	msg = ("ISBADD1R : "+ ISBADD1R);
					keyreport(fw, ++ctr, "ISBADD1R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD1R MISMATCH DB: "+getDBval(rs,"ISBADD1R") + " ONLINE : "+ ISBADD1R );
					keyreport(fw, ++ctr, "ISBADD1R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD2R.trim().equals(getDBval(rs,"ISBADD2R")))
					{	msg = ("ISBADD2R : "+ ISBADD2R);
					keyreport(fw, ++ctr, "ISBADD2R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD2R MISMATCH DB: "+getDBval(rs,"ISBADD2R") + " ONLINE : "+ ISBADD2R );
					keyreport(fw, ++ctr, "ISBADD2R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD3R.trim().equals(getDBval(rs,"ISBADD3R")))
					{	msg = ("ISBADD3R : "+ ISBADD3R);
					keyreport(fw, ++ctr, "ISBADD3R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD3R MISMATCH DB: "+getDBval(rs,"ISBADD3R") + " ONLINE : "+ ISBADD3R );
					keyreport(fw, ++ctr, "ISBADD3R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD4R.trim().equals(getDBval(rs,"ISBADD4R")))
					{	msg = ("ISBADD4R : "+ ISBADD4R);
					keyreport(fw, ++ctr, "ISBADD4R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD4R MISMATCH DB: "+getDBval(rs,"ISBADD4R") + " ONLINE : "+ ISBADD4R );
					keyreport(fw, ++ctr, "ISBADD4R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD5R.trim().equals(getDBval(rs,"ISBADD5R")))
					{	msg = ("ISBADD5R : "+ ISBADD5R);
					keyreport(fw, ++ctr, "ISBADD5R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD5R MISMATCH DB: "+getDBval(rs,"ISBADD5R") + " ONLINE : "+ ISBADD5R );
					keyreport(fw, ++ctr, "ISBADD5R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					
					}//fw.write(msg + System.getProperty("line.separator"));
					
					
					if (ISBADD6R.trim().equals(getDBval(rs,"ISBADD6R")))
					{	msg = ("ISBADD6R : "+ ISBADD6R);
					keyreport(fw, ++ctr, "ISBADD6R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD6R MISMATCH DB: "+getDBval(rs,"ISBADD6R") + " ONLINE : "+ ISBADD6R );
					keyreport(fw, ++ctr, "ISBADD6R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD7R.trim().equals(getDBval(rs,"ISBADD7R")))
					{	msg = ("ISBADD7R : "+ ISBADD7R);
					keyreport(fw, ++ctr, "ISBADD7R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD7R MISMATCH DB: "+getDBval(rs,"ISBADD7R") + " ONLINE : "+ ISBADD7R );
					keyreport(fw, ++ctr, "ISBADD7R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ISBADD8R.trim().equals(getDBval(rs,"ISBADD8R")))
					{	msg = ("ISBADD8R : "+ ISBADD8R);
					keyreport(fw, ++ctr, "ISBADD8R","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBADD8R MISMATCH DB: "+getDBval(rs,"ISBADD8R") + " ONLINE : "+ ISBADD8R );
					keyreport(fw, ++ctr, "ISBADD8R","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//f//w.write(msg + System.getProperty("line.separator"));
					
					if (ISBPOSTCODE.trim().equals(getDBval(rs,"ISBPOSTCODE")))
					{	msg = ("ISBPOSTCODE : "+ ISBPOSTCODE);
					keyreport(fw, ++ctr, "ISBPOSTCODE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("ISBPOSTCODE MISMATCH DB: "+getDBval(rs,"ISBPOSTCODE") + " ONLINE : "+ ISBPOSTCODE );
					keyreport(fw, ++ctr, "ISBPOSTCODE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					//fw.write(msg + System.getProperty("line.separator"));
					//System.out.println("dates");
					//POLICY DETAILS TAB
					CCDATE = CCDATE.trim();
					tempdate = CCDATE.substring(0, 4) + CCDATE.substring(5,7) + CCDATE.substring(8,10);
					
					//System.out.println(tempdate);
					
					if (tempdate.trim().equals(getDBval(rs,"CCDATE")))
					{
						msg = ("CCDATE : "+ CCDATE);
						keyreport(fw, ++ctr, "CCDATE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
					}
					else
					{	msg = ("CCDATE MISMATCH DB: "+getDBval(rs,"CCDATE") + " ONLINE : "+ tempdate );
					keyreport(fw, ++ctr, "CCDATE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					CRDATE = CRDATE.trim();
					tempdate = CRDATE.substring(0, 4) + CRDATE.substring(5,7) + CRDATE.substring(8,10);
					
					//System.out.println(tempdate);
					
					if (tempdate.trim().equals(getDBval(rs,"CRDATE")))
					{	msg = ("CRDATE : "+ CRDATE);
					keyreport(fw, ++ctr, "CRDATE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("CRDATE MISMATCH DB: "+getDBval(rs,"CRDATE") + " ONLINE : "+ tempdate );
					keyreport(fw, ++ctr, "CRDATE","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (ENDSERCD.trim().equals(getDBval(rs,"ENDSERCD")))
					{	msg = ("ENDSERCD : "+ ENDSERCD);
					keyreport(fw, ++ctr, "ENDSERCD","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	msg = ("ENDSERCD MISMATCH DB: "+getDBval(rs,"ENDSERCD") + " ONLINE : "+ ENDSERCD );
					keyreport(fw, ++ctr, "ENDSERCD","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (CAMPAIGN.trim().equals(getDBval(rs,"CAMPAIGN")))
					{	msg = ("CAMPAIGN : "+ CAMPAIGN);
					keyreport(fw, ++ctr, "CAMPAIGN","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("CAMPAIGN MISMATCH DB: "+getDBval(rs,"CAMPAIGN") + " ONLINE : "+ CAMPAIGN );
					keyreport(fw, ++ctr, "CAMPAIGN","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (FACTHOUS.trim().equals(getDBval(rs,"FACTHOUS")))
					{	msg = ("FACTHOUS : "+ FACTHOUS);
					keyreport(fw, ++ctr, "FACTHOUS","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("FACTHOUS MISMATCH DB: "+getDBval(rs,"FACTHOUS") + " ONLINE : "+ FACTHOUS );
					keyreport(fw, ++ctr, "FACTHOUS","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (POLDUR.trim().equals(getDBval(rs,"POLDUR")))
					{	msg = ("POLDUR : "+ POLDUR);
					keyreport(fw, ++ctr, "POLDUR","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("POLDUR MISMATCH DB: "+getDBval(rs,"POLDUR") + " ONLINE : "+ POLDUR );
					keyreport(fw, ++ctr, "POLDUR","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (AGNTSEL00.trim().equals(getDBval(rs,"AGNTSEL00")))
					{	msg = ("AGNTSEL00 : "+ AGNTSEL00);
					keyreport(fw, ++ctr, "CRDATE","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("AGNTSEL00 MISMATCH DB: "+getDBval(rs,"AGNTSEL00") + " ONLINE : "+ AGNTSEL00 );
					keyreport(fw, ++ctr, "AGNTSEL00","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (AGNTSEL01.trim().equals(getDBval(rs,"AGNTSEL01")))
					{	msg = ("AGNTSEL01 : "+ AGNTSEL01);
					keyreport(fw, ++ctr, "AGNTSEL01","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	msg = ("AGNTSEL01 MISMATCH DB: "+getDBval(rs,"AGNTSEL01") + " ONLINE : "+ AGNTSEL01 );
					keyreport(fw, ++ctr, "AGNTSEL01","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (AGNTSEL02.trim().equals(getDBval(rs,"AGNTSEL02")))
					{	msg = ("AGNTSEL02 : "+ AGNTSEL02);
					keyreport(fw, ++ctr, "AGNTSEL02","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	msg = ("AGNTSEL02 MISMATCH DB: "+getDBval(rs,"AGNTSEL02") + " ONLINE : "+ AGNTSEL02 );
					keyreport(fw, ++ctr, "AGNTSEL02","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (AGNTSEL03.trim().equals(getDBval(rs,"AGNTSEL03")))
					{	msg = ("AGNTSEL03 : "+ AGNTSEL03);
					keyreport(fw, ++ctr, "AGNTSEL04","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("AGNTSEL04 MISMATCH DB: "+getDBval(rs,"AGNTSEL03") + " ONLINE : "+ AGNTSEL03 );
					keyreport(fw, ++ctr, "AGNTSEL04","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (AGNTSEL04.trim().equals(getDBval(rs,"AGNTSEL04")))
					{	msg = ("AGNTSEL04 : "+ AGNTSEL04);
					keyreport(fw, ++ctr, "AGNTSEL04","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("AGNTSEL04 MISMATCH DB: "+getDBval(rs,"AGNTSEL04") + " ONLINE : "+ AGNTSEL04 );
					keyreport(fw, ++ctr, "AGNTSEL04","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					
					//unique per endorser
					
					if (PHBANKACCKEY.trim().equals(getDBval(rs,"PHBANKACCKEY")))
					{	msg = ("PHBANKACCKEY : "+ PHBANKACCKEY);
					keyreport(fw, ++ctr, "PHBANKACCKEY","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBANKACCKEY MISMATCH DB: "+getDBval(rs,"PHBANKACCKEY") + " ONLINE : "+ PHBANKACCKEY );
					keyreport(fw, ++ctr, "PHBANKACCKEY","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
					if (PHBANKACCDSC.trim().equals(getDBval(rs,"PHBANKACCDSC")))
					{	msg = ("PHBANKACCDSC : "+ PHBANKACCDSC);
					keyreport(fw, ++ctr, "PHBANKACCDSC","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBANKACCDSC MISMATCH DB: "+getDBval(rs,"PHBANKACCDSC") + " ONLINE : "+ PHBANKACCDSC );
					keyreport(fw, ++ctr, "PHBANKACCDSC","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHBBKBRCD.trim().equals(getDBval(rs,"PHBBKBRCD")))
					{	msg = ("PHBBKBRCD : "+ PHBBKBRCD);
					keyreport(fw, ++ctr, "PHBBKBRCD","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBBKBRCD MISMATCH DB: "+getDBval(rs,"PHBBKBRCD") + " ONLINE : "+ PHBBKBRCD );
					keyreport(fw, ++ctr, "PHBBKBRCD","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHBBKACTYP.trim().equals(getDBval(rs,"PHBBKACTYP")))
					{	msg = ("PHBBKACTYP : "+ PHBBKACTYP);
					keyreport(fw, ++ctr, "PHBBKACTYP","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHBBKACTYP MISMATCH DB: "+getDBval(rs,"PHBBKACTYP") + " ONLINE : "+ PHBBKACTYP );
					keyreport(fw, ++ctr, "PHBBKACTYP","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHCARDNMB.trim().equals(getDBval(rs,"PHCARDNMB")))
					{	msg = ("PHCARDNMB : "+ PHCARDNMB);
					keyreport(fw, ++ctr, "PHCARDNMB","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHCARDNMB MISMATCH DB: "+getDBval(rs,"PHCARDNMB") + " ONLINE : "+ PHCARDNMB );
					keyreport(fw, ++ctr, "PHCARDNMB","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHCARDEXPM.trim().equals(getDBval(rs,"PHCARDEXPM")))
					{	msg = ("PHCARDEXPM : "+ PHCARDEXPM);
					keyreport(fw, ++ctr, "PHCARDEXPM","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("PHCARDEXPM MISMATCH DB: "+getDBval(rs,"PHCARDEXPM") + " ONLINE : "+ PHCARDEXPM );
					keyreport(fw, ++ctr, "PHCARDEXPM","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}
					
					if (PHCARDEXPY.trim().equals(getDBval(rs,"PHCARDEXPY")))
					{	msg = ("PHCARDEXPY : "+ PHCARDEXPY);
					keyreport(fw, ++ctr, "PHCARDEXPY","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{
						msg = ("PHCARDEXPY MISMATCH DB: "+getDBval(rs,"PHCARDEXPY") + " ONLINE : "+ PHCARDEXPY );
						keyreport(fw, ++ctr, "PHCARDEXPY","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
					}
					
					if (AUTHRINUM.trim().equals(getDBval(rs,"AUTHRINUM")))
					{	msg = ("AUTHRINUM : "+ AUTHRINUM);
					keyreport(fw, ++ctr, "AUTHRINUM","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}
					else
					{	msg = ("AUTHRINUM MISMATCH DB: "+getDBval(rs,"AUTHRINUM") + " ONLINE : "+ AUTHRINUM );
					keyreport(fw, ++ctr, "AUTHRINUM","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}//fw.write(msg + System.getProperty("line.separator"));
					
				/*	if (AUTHRINUM.trim().equals(getDBval(rs,"AUTHRINUM")))
					{	msg = ("AUTHRINUM : "+ AUTHRINUM);
					keyreport(fw, ++ctr, "AUTHRINUM","FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
					pass++;
					}else
					{	msg = ("AUTHRINUM MISMATCH DB: "+getDBval(rs,"AUTHRINUM") + " ONLINE : "+ AUTHRINUM);
					keyreport(fw, ++ctr, "AUTHRINUM","FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
					fail++;
					}*/
					 
					
					
					System.out.println(CV[i].trim());
					System.out.println("Starting the loops for coverage fields. i"+ i + "_j "+ j + "_k" + k);
					//COVEARAGE TAB
					try{
					while (i>0)		
					{
						if (CV[i].trim().equals(getDBval(rs,"CV00"+i)))
						{	msg = ("CV00"+ i + ">"  + CV[i]);
						keyreport(fw, ++ctr, "CV00" + i,"FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
						}else
						{	msg = ("CV00"+ i + " value DB: " + getDBval(rs,"CV00"+i) + ". online value: " + CV[i]);
						keyreport(fw, ++ctr, "CV00" + i,"FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
						}
						System.out.println(i + ">" + msg);
						
						//fw.write(msg + System.getProperty("line.separator"));
						
						i--;
						//System.out.println("looping i"+ i);
					}
					
					while (j>0)		
					{
						if (SI[j].trim().equals(getDBval(rs,"SI00"+j)))
						{	msg = ("SI00"+ j + ">" + SI[j]);
						keyreport(fw, ++ctr, "SI00" + i,"FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
						}else
						{	msg = ("SI00"+ j +" value DB: " + getDBval(rs,"SI00"+j) + ". online value: " + SI[j] );
						keyreport(fw, ++ctr, "SI00" + i,"FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
						}
						System.out.println(j + ">" + msg);
						//fw.write(msg + System.getProperty("line.separator"));
						
						
						j--;
						//System.out.println("looping j" + j);
					}
					while (k>0)		
					{
						if (PM[k].trim().equals(getDBval(rs,"PM00"+k)))
						{	msg = ("PM00"+ k + ">" + PM[k]);
						keyreport(fw, ++ctr, "PM00" + i,"FETCH_VALUE", "Pass" , msg , df.format(date), df.format(date));
						pass++;
						}
						else
						{	msg = ("PM00"+ k +" value DB: " + getDBval(rs,"PM00"+k) + ". online value: " + PM[k] );
						keyreport(fw, ++ctr, "PM00" + i,"FETCH_VALUE", "Fail" , msg , df.format(date), df.format(date));
						fail++;
						}
						System.out.println(k + ">" + msg);
						//fw.write(msg + System.getProperty("line.separator"));
						
						
						k--;
						//System.out.println("looping k" + k);
					}
					}catch(Exception e)
					{
						System.out.println("Exception comparing values in the list:"+e.getMessage() + e.getStackTrace());
					}
					
					fw.write("<SuiteID>MasterReport</SuiteID>");
					fw.write("<SuiteName>POLICYDATAEXTRACTION</SuiteName>");
					fw.write("<TestCaseID>POLICYDATAEXTRACTION</TestCaseID>");
					fw.write("<TestCaseName>POLICYDATAEXTRACTION</TestCaseName>");
					fw.write("<Name>POLICYDATAEXTRACTION</Name>");
					fw.write("<Act_Status>Fail</Act_Status>");
					fw.write("<Total_Keywords>"+(pass+fail) +"</Total_Keywords>");
					fw.write("<Keyword_Pass>"+pass+"</Keyword_Pass>");
					fw.write("<Keyword_Failed>"+ fail +"</Keyword_Failed>");
					fw.write("<dataRef>TOTPAMPOLDATA</dataRef>");
					fw.write("<StartTime>"+ST+"</StartTime>");
					fw.write("<EndTime>"+df2.format(date).toString()+"</EndTime>");
					fw.write("<Execution_span>34.86328 (Seconds)</Execution_span>");
					fw.write("</Action>");
				
					
					fw.close();
					
					
				}	
				con.close();
				/*
				if(((PHSURNAME =="")||(PHSURNAME == null))&&(rs.getString("PHSURNAME")==null)||rs.getString("PHSURNAME")=="")
				{
					System.out.println("Values matched for this");
				}
				else if(PHSURNAME==rs.getString("PHSURNAME"))
				{
					System.out.println("Values matched for this");
				}
				System.out.println(rs.getString("PHSURNAME"));
				*/
				
				
			}catch(Exception e3)
			{
				System.out.println("Exception in compairsion with DB" + e3.getMessage() + e3.getStackTrace());
				fw.close();
				con.close();
			}
			
			System.out.println(" c o m p l e t e");
			}catch(Exception e)
			{
				System.out.print("exception caught in master catch"+ e.getMessage() + e.getStackTrace());
			}
		

	}
	
	public static void colresdata(String folder, String businessdate)
	{
		String iq= null, query = "select a.chdrnum,b.zbktrfdt as trandate,b.zbktrfdt as tfrdate from gchipf a join gbihpf b on a.chdrnum = b.chdrnum and b.premout = 'Y' where a.zcolmcls = 'F' ";
		
		ResultSet rs = null;
		Statement stat = null;
		Connection conn1 = null;
		
		String tchdrnum = null;
		String ttrandate = null;
		String ttfrdate = null;
		
		
		
		try {
			conn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.12:1522:UATDB", "vm1dta", "vm1dta12#$");
			stat = conn1.createStatement();
			
			 rs = stat.executeQuery(query);
			 
			 while(rs.next())
			 {
				 tchdrnum = getDBval(rs, "CHDRNUM");
				// ttrandate = getDBval(rs, "TRANDATE");
				// ttrandate = businessdate;
				 ttrandate = businessdate.substring(0, 4) + businessdate.substring(5,7) + businessdate.substring(8,10);
				 System.out.println("the business date as per the DB: " + ttrandate);
				 
				 ttfrdate = getDBval(rs, "TFRDATE");
				 
				 //iq = "INSERT INTO ZCRIPF VALUES ("+"''," + tchdrnum + "," + ttrandate + "," +ttfrdate + ")";
				 
				 iq ="insert into ZCRIPF(TRANDATE, TFRDATE, CHDRNUM, DSHCDE, USRPRF, JOBNM, DATIME) values ('"+ ttrandate +"', '" + ttfrdate + "', '" + tchdrnum + "', '00', 'UNDERWR1', 'G1MTRREIF', '14-JUN-17 06.08.27.365000000 PM')"; 
				  
				 System.out.println("inserted for " + tchdrnum);
				 
				 colreschdr[chdrctr] = tchdrnum;
				 chdrctr++;
				 //insert query executed for each record.
				 
				 //write to a file too for result comparision.
				 
			 }
			 conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("EXCEPTION!!!" + e.getMessage() );
			e.printStackTrace();
			
				try {
					conn1.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		
	}

	public static void batchCOLRES(FileWriter fw, WebDriver driver)
	    {
	    	
			try{
			int flag =0;
			String scheNo = new String();
			
			
			//WebDriver driver = new ChromeDriver();
			
			//JOptionPane.showMessageDialog(null, "Logged in. ");
			
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			
			Thread.sleep(1000);
			driver.switchTo().defaultContent();
			Thread.sleep(200);
			driver.switchTo().frame("frameMenu");
			Thread.sleep(100);
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();

			Thread.sleep(100);
			
			
			
			driver.switchTo().defaultContent();
			//driver.switchTo().frame("activeframe");
			Thread.sleep(1000);
			driver.switchTo().frame("mainForm");
			//driver.findElement(By.xpath("//div[3]/div[7]/img")).click();
			//img[contains(@src,'http://192.168.2.11:11012/GroupAsiaWeb1/screenFiles/onePixel.gif')])[35]
			//	//descendant::div[contains(text(),'Endorsement Quotations')]
			Thread.sleep(1000);
			//JOptionPane.showMessageDialog(null,  "Going to click now. ");
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Schedule Submission')]")).click();
			//JOptionPane.showMessageDialog(null,  "Going to click now. ");
			//driver.findElement(By.xpath("//img[contains(@src,'http://192.168.2.11:11012/GroupAsiaWeb1/screenFiles/onePixel.gif')])[35]")).click();
			
			//Thread.sleep(100);
			Thread.sleep(2000);
			//G1MBRDATAI
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			driver.switchTo().frame("mainForm");
			driver.findElement(By.name("membsel")).sendKeys("G1ZCOLRES");
			driver.findElement(By.id("continuebutton")).click();
			Thread.sleep(1000);
			
			
			driver.switchTo().defaultContent();
			Thread.sleep(3000);
			driver.switchTo().frame("frameMenu");
		
			Thread.sleep(250);
			//JOptionPane.showMessageDialog(null, "The jar execution has completed.");
			//Batch job submitted
			
			scheNo = driver.findElement(By.xpath("//div[@class='message']/div")).getText();
			//scheNo = "Schedule G1MBRDATAI/00001004 Submitted";
			scheNo = scheNo.substring(20, 29);
			Thread.sleep(250);
			driver.switchTo().defaultContent();
			Thread.sleep(3000);
			driver.switchTo().frame("frameMenu");
			driver.findElement(By.xpath("//descendant::div[contains(text(),'Batch Processing')]")).click();

			Thread.sleep(1000);
			
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainForm");
			driver.findElement(By.xpath("//descendant::div[contains(text(),'W/W Submitted Schedules')]")).click();

			Thread.sleep(1000);
			
			
			//checking status
			
			driver.switchTo().defaultContent();
			Thread.sleep(3000);
			driver.switchTo().frame("mainForm");
			driver.findElement(By.name("scheduleName")).clear();
			driver.findElement(By.name("scheduleName")).sendKeys("G1ZCOLRES");
			driver.findElement(By.name("scheduleNumber")).sendKeys(scheNo);
			driver.findElement(By.id("continuebutton")).click();
			
			Thread.sleep(100);
			
			//check status.
			//tr[@id='tablerow1']/td[3]/
			//table[@id='s0080Table']/tbody/tr/td[3].
			//div[@class='sData']/table/tbody/tr/td[3]
			String msg = "";
			int f =0;
			while(f == 0)
			{	
			driver.switchTo().defaultContent();
			Thread.sleep(1000);
			 DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			Date date = new Date();
			driver.switchTo().frame("mainForm");
			//JOptionPane.showMessageDialog(null, "Check ");
			String txt =  driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim();
			System.out.print(txt);
				if (txt.equalsIgnoreCase("completed") || txt == "Completed" )
				{	//JOptionPane.showMessageDialog(null, "Completed status found.");
					msg = "Batch completed succesfully";
					System.out.println(msg);
					//keyreport(fw, ++ctr, "G1MBRDATAI","SUBMIT BATCH", "Pass" , msg , df.format(date), df.format(date));
					//cpass++;
					flag=1;
					f=0;
					break;	
				}else if(txt.equalsIgnoreCase("aborted") || txt == "Aborted")
				{
					msg = "Batch aborted";
					System.out.println(msg);
					//keyreport(fw, ++ctr, "G1MBRDATAI","SUBMIT BATCH", "Fail" , msg , df.format(date), df.format(date));
					//cfail++;
					f=0;
					break;
				}
				else 
				{	
					driver.findElement(By.id("refreshbutton")).click();
					//JOptionPane.showMessageDialog(null,(driver.findElement(By.xpath("//div[@class='sData']/table/tbody/tr/td[3]")).getText().trim()));
				}
			}
			
			//table[@id='s0080Table']/tbody/tr/td[3]
			//JOptionPane.showMessageDialog(null, "Done");
			
			//logout
			Thread.sleep(400);

			System.out.println("outside check");
			//driver.switchTo().alert().accept();
			Thread.sleep(400);
			//driver.navigate().to("http://192.168.2.11:11015/ZurichIntegralGroup/logout");
			Thread.sleep(400);
			
			
			
			}catch(Exception e)
			{	
				JOptionPane.showMessageDialog(null, "Exception occured. Field couldnt be loaded.. "+ e.getMessage().toString() + e.getStackTrace().toString());
			}finally{
				driver.close();
			}
			
		}
	    
	public static void colresverify()
	 { //called multiple times orhandling it here
		 ResultSet rs = null;
		 String statcode = null, polnum = "heyjude", msg = null, premout = null;
		 try{
		 Connection conn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.12:1522:UATDB", "vm1dta", "vm1dta12#$");
		
		 Statement stmt = conn1.createStatement();
		 //reader loop returns polnum starts here
		 chdrctr--;
		 while(chdrctr>=0)
		 {
		 try {
			 polnum = colreschdr[chdrctr];
			 rs = stmt.executeQuery("select * from GCHIPF where CHDRNUM ='" + polnum + "'");
			 while(rs.next())
			 {statcode =getDBval(rs, "STATCODE");
				 if(statcode.equals("IF"))
				 {	msg = "Statcode verified as 'IF' for chdrnum " + polnum;
					 System.out.println(msg);
				 }else
				 {
					 msg = "Statcode expected: 'IF'. Actual value = "+ statcode + "for chdrnum " + polnum;
					 System.out.println(msg);

				 }
			 }
			 
			 
			 
			 //2nd verification
			 
			rs = stmt.executeQuery("select * from GBIHPF where chdrnum ='"+ polnum +" and ZBKTRFDT = (select TFRDATE from zcripf where chdrnum = '" + polnum + "')");
			while(rs.next())
			{
				premout = getDBval(rs,"PREMOUT");
				
				if(premout.equals("N"))
				{
					msg = "PREMOUT verified as 'N' for chdrnum " + polnum;
					System.out.println(msg);
					 
				}else
				{
					 msg = "PREMOUT expected: 'N'. Actual value = "+ premout + "for chdrnum " + polnum;
					 System.out.println(msg);
				}
			}
					 
		//reader loop ends here			 
		 
		 } catch (SQLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 } 
		 }catch(Exception e)
		 {
			 System.out.println("eException occured" + e.getMessage() + e.getStackTrace() );
		 }
		 
		 
	 }

}
