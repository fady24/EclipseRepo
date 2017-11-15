

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;


public class upload {
	
	static int cpass =0;
	static int ctr =0;
	static int cfail = 0;
	

    private static final String FILE_NAME = "D:\\ZurichDBTest\\upload.xlsx";
    
    public static void batchjob(FileWriter fw)
    {
    	File file = new File("D:\\S\\IEDriverServer_Win32_3.3.0 (1)\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		try{
		int flag =0;
		String scheNo = new String();
		
		
		//WebDriver driver = new ChromeDriver();
		
		driver.get("http://cust1:11015/ZurichIntegralGroup/");
		Actions action = new Actions(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("userid")));
		driver.findElement(By.id("userid")).sendKeys("underwr1");
		driver.findElement(By.id("password")).sendKeys("underwr1");
		driver.findElement(By.id("loginId")).click();
		
		//JOptionPane.showMessageDialog(null, "Logged in. ");
		
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
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
		driver.findElement(By.name("membsel")).sendKeys("G1MBRDATAI");
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
		
		driver.findElement(By.name("scheduleName")).sendKeys("G1MBRDATAI");
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
				keyreport(fw, ++ctr, "G1MBRDATAI","SUBMIT BATCH", "Pass" , msg , df.format(date), df.format(date));
				cpass++;
				flag=1;
				f=0;
				break;	
			}else if(txt.equalsIgnoreCase("aborted") || txt == "Aborted")
			{
				msg = "Batch aborted";
				keyreport(fw, ++ctr, "G1MBRDATAI","SUBMIT BATCH", "Fail" , msg , df.format(date), df.format(date));
				cfail++;
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
    
    public static void insert(Statement stmt, FileWriter fw,Workbook workbook)
    {	//  ResultSet rs = null;
    	 DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
         Date date = new Date();
    	 try {

    		 String msg = "";
             Sheet datatypeSheet = workbook.getSheetAt(0);
             Iterator<Row> iterator = datatypeSheet.iterator();
             String que = null, key="INSERT VALUES";
             int row =0;
             int qresult = 0;
             
             qresult = stmt.executeUpdate("delete from zmuipf");
             System.out.println(qresult);
             
             qresult =0;
             while (iterator.hasNext()) {
             	if (row == 0)
             	{	row++;
             		iterator.next();
             		continue;
             	}
             	Row currentRow = iterator.next();
                 Iterator<Cell> cellIterator = currentRow.iterator();
                 que = currentRow.getCell(1).toString();
                 
                 System.out.println("QUERY"+ que);
                 try{
                 qresult = stmt.executeUpdate(que);
                 System.out.println(qresult);
                 if (qresult > 0)
                 {	msg = "STATEMENT EXECUTED SUCCESFULLY.";
                 	keyreport(fw, ++ctr, "ZMUIPF","INSERT_VALUES", "Pass" , msg , df.format(date), df.format(date));
                 	cpass++;
                 }else
                 {	msg = "STATEMENT COULDNT BE EXECUTED";
                	keyreport(fw, ++ctr, "ZMIEPF","INSERT_VALUES", "Fail" , msg , df.format(date), df.format(date));
                	cfail++;
                 }
                 }catch(Exception e1)
                 {
                	msg = "STATEMENT "+ que + "COULDNT BE EXECUTED BECAUSE OF EXCEPTION"+ e1.getMessage() + e1.getStackTrace();
                 	keyreport(fw, ++ctr, "ZMIEPF","INSERT_VALUES", "Fail" , msg , df.format(date), df.format(date));
                 	cfail++;
                 }
                 //check status
                 System.out.println("commit");
                 //stmt.execute("commmit;");
                 
             	}
         } catch (Exception e) {
     	 	JOptionPane.showMessageDialog(null,"Couldnt connect to the database for inserting values. " + e.getMessage() + e.getStackTrace());
    	 	
         }
    }

    public static void validate(Statement stmt,FileWriter fw,Workbook workbook)
    {	  ResultSet rs = null;
    	 try {

          
             Sheet datatypeSheet = workbook.getSheetAt(1);
             Iterator<Row> iterator = datatypeSheet.iterator();
             DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
             Date date = new Date();
             String que = null, err = null, dberr = null;
             int row =0;
             String chdrnum = null, msg = null;
           int flag =0;
             while (iterator.hasNext()) {
             	if (row == 0)
             	{	row++;
             		iterator.next();
             		continue;
             	}
             	Row currentRow = iterator.next();
                 Iterator<Cell> cellIterator = currentRow.iterator();
                 chdrnum = currentRow.getCell(0).toString().trim();
                 que = "SELECT * FROM ZMIEPF WHERE CHDRNUM = '"+chdrnum + "'";
                 rs = stmt.executeQuery(que);
                 err= currentRow.getCell(1).toString().trim();
                 flag =0;
                 while(rs.next())
                 {	dberr =getDBval(rs,"ERRORDSC");
                 if(dberr.equals(err))
                 {	flag =1;
                 	break;
                 }else
                 {	flag =0;
                 }
                 }
                 
                 
                 if(flag ==1)
                 {	msg = "Error verified on ZMIEPF for CHDRNUM: '" + chdrnum + "' Error: '"+ err + "'";
                	keyreport(fw, ++ctr, "ZMIEPF","VALIDATE_ERROR", "Pass" , msg , df.format(date), df.format(date));
                	cpass++;
                	System.out.println("verified " + msg );
                 }else
                 {	msg = "Error mismatch for CHDRNUM: " + chdrnum + "Expected : '"+ err + "' Actual: '" + dberr + "'";
                	 keyreport(fw, ++ctr, "ZMIEPF","VALIDATE_ERROR", "Fail" , msg , df.format(date), df.format(date));
                	 cfail++;
                	 System.out.println("failed "+msg );
                 }
                 System.out.println(que);
                 System.out.println(err);
               
             	}
         } catch (Exception e) {
    	 	JOptionPane.showMessageDialog(null,"Couldnt connect to the database for error msg verification. " + e.getMessage() + e.getStackTrace());
    	 	
         }
    }
    public static void main(String[] args) {
    	   FileInputStream excelFile;
    	   Connection conn1= null;
		try {
			excelFile = new FileInputStream(new File(FILE_NAME));
		
           Workbook workbook = new XSSFWorkbook(excelFile);
       
    	
           try{
       		conn1 = DriverManager.getConnection(  
       				"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
       		conn1.setAutoCommit(false);
       		Statement stmt=conn1.createStatement();
       		/*ResultSet rs;
       		String currentpol = null;
       		
       		rs = stmt.executeQuery("SELECT * FROM STAGEDBUSR.TOTPAMPOLDATA WHERE CHDRNUM= '62239999'");
       		*/
        	   DateFormat df2 = new SimpleDateFormat("yyyymmdd  HH_mm_ss");
       		Date date = new Date();
       		String folder = "D:\\ZurichDBTest\\POLICY_UPLOAD_"+ df2.format(date).toString();
       		File masterfolder = new File(folder);
       		boolean succesful = masterfolder.mkdir();
       		String folder2 = folder +"\\POLICYUPLOAD";
       		File TCfolder = new File(folder2);
       		succesful = TCfolder.mkdir(); 
       		File f = new File(TCfolder + "\\UPLOAD.XML");
			FileWriter fw= new FileWriter(f);
       		File tc = new File(TCfolder+"\\POLICYUPLOAD.XML");
       		FileWriter ftc = new FileWriter(tc);
       		ftc.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
       		ftc.write("<?xml-stylesheet type=\"text/xsl\" href=\"D:\\Report_XSL_Templates\\TestCase.xsl\"?>");
       		ftc.write("<TestCase>");
       		ftc.write("<Action>");
       		ftc.write("<Action1>POLICYUPLOAD</Action1>");
       			
       		
       		
       	
       		
       		String start = df2.format(date).toString();
       		
       			//currentpol = rs.getString("CHDRNUM");
       			ftc.write("<Act_ID>" + "UPLOAD" + "</Act_ID>");
       			ftc.write("<Act_Name>" + "UPLOAD" + "</Act_Name>");
       			ftc.write("<Act_Result>Fail</Act_Result>");
       			ftc.write("<Act_PassedKeyWords>1</Act_PassedKeyWords>");
       			ftc.write("<Act_WarningKeyWords>0</Act_WarningKeyWords>");
       			ftc.write("<Act_FailKeyWords>1</Act_FailKeyWords>");
       			ftc.write("<Act_StartTime>"+ df2.format(date).toString() +"</Act_StartTime>");
	    			fw.write("<?xml version="+"\"1.0\""+" encoding="+"\"UTF-8\""+"?>");
	    			//fw.write(System.getProperty("line.separator"));
	    			fw.write("<?xml-stylesheet type="+"\"text/xsl\""+" href=\""+"D:\\Report_XSL_Templates\\Action.xsl\""+"?>");
	    			//fw.write(System.getProperty("line.separator"));
	    			fw.write("<Action>");
	    			String ST = df2.format(date).toString();
	    			
	    			
	    			insert(stmt,fw,workbook);
	    			conn1.commit();
	    			batchjob(fw);
	    			
	    			validate(stmt,fw,workbook);
	    			
	    			fw.write("<SuiteID>MasterReport</SuiteID>");
					fw.write("<SuiteName>MEMBERUPLOAD</SuiteName>");
					fw.write("<TestCaseID>MEMBERUPLOAD</TestCaseID>");
					fw.write("<TestCaseName>MEMBERUPLOAD</TestCaseName>");
					fw.write("<Name>MEMBERUPLOAD</Name>");
					fw.write("<Act_Status>Fail</Act_Status>");
					fw.write("<Total_Keywords>"+(cpass+cfail) +"</Total_Keywords>");
					fw.write("<Keyword_Pass>"+cpass+"</Keyword_Pass>");
					fw.write("<Keyword_Failed>"+ cfail +"</Keyword_Failed>");
					fw.write("<dataRef>TOTPAMPOLDATA</dataRef>");
					fw.write("<StartTime>"+ST+"</StartTime>");
					fw.write("<EndTime>"+df2.format(date).toString()+"</EndTime>");
					fw.write("<Execution_span>34.86328 (Seconds)</Execution_span>");
					fw.write("</Action>");
					fw.close();
       			ftc.write("<Act_EndTime>"+ df2.format(date).toString() +"</Act_EndTime>");
       			ftc.write("</Action>");
       		
       		
       		String end = df2.format(date).toString();
       		
       		ftc.write("<SuiteID>MasterReport</SuiteID>");
       		ftc.write("<SuiteName>"+"POLICYUPLOAD"+"</SuiteName>");
       		ftc.write("<Name>"+"POLICYUPLOAD"+"</Name>");
       		ftc.write("<ID>POLICYUPLOAD</ID>");
       		ftc.write("<TC_Status>Fail</TC_Status>");
       		ftc.write("<TotalActions>"+ "1" +"</TotalActions>");
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
       		fm.write("<TC_ID>POLICYUPLOAD</TC_ID>");
       		fm.write("<TC_Result>Fail</TC_Result>");
       		fm.write("<TC_PassedActions>0</TC_PassedActions>");
       		fm.write("<TC_FailActions>1</TC_FailActions>");
       		fm.write("<TC_StartTime>"+ start +"</TC_StartTime>");
       		fm.write("<TC_EndTime>"+ end +"</TC_EndTime>");
       		fm.write("</TC>");
       		
       		fm.write("<Name>POLICYUPLOAD</Name>");
       		fm.write("<ID>MasterReport</ID>");
       		fm.write("<Suite_Status>Fail</Suite_Status>");
       		fm.write("<TC_Pass>0</TC_Pass>");
       		fm.write("<TC_Fail>1</TC_Fail>");
       		
       		fm.write("<ExecutionDate>" + start + "</ExecutionDate>");
       		fm.write("<StartTime>" + start + "</StartTime>");
       		fm.write("<EndTime>"+ end +"</EndTime>");
       		fm.write("<Execution_span>" + "032.02" + "(Seconds)</Execution_span>");
       		
       		fm.write("<Browser_name>"+"Internet Explorer"+" </Browser_name>");
       		fm.write("</Suite>");
       		fm.close();
       		
       		//write test case 
       	
       		//all static
       		
       		//vpoldata("53018283");
       		conn1.close();
       		
       		
       		}catch(Exception e)
       		{
       			JOptionPane.showMessageDialog(null, "Exception in main execution block.Network issue might exist."+ e.getMessage()+ e.getStackTrace());
       		}
           
           
  
    	//batchjob();
    	System.out.println("validation");
    	Desktop.getDesktop().open(new File("D:\\ZurichDBTest"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

    }
}