import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Colres {

	public static List<String> colreschdr = new ArrayList<>() ;	
	
	public static void main(String businessdate) {
		// TODO Auto-generated method stub
		FileWriter fw= null;
		
		File file = new File("D:\\S\\IEDriverServer_Win32_3.3.0 (1)\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		WebDriver driver = new InternetExplorerDriver();
		try{
			fw = new FileWriter(new File("D://heyjude.txt"));
			driver.get("http://192.168.2.11:11015/ZurichIntegralGroup/FirstServlet");
			
			driver.findElement(By.id("userid")).sendKeys("underwr1");
			driver.findElement(By.id("password")).sendKeys("underwr1");
			driver.findElement(By.id("loginId")).click();
			
			System.out.print("logged in!!!");
			//driver.switchTo().frame("mainForm");
			driver.switchTo().defaultContent();
		}catch(Exception e)
		{
			System.out.println("exceptions doing web operations");
		}
		
		
		//requisites
		//
		colresdata("D:\\ZurichDBTest\\MARK", businessdate);
		
		
		batchCOLRES(fw,driver);
		colresverify();
		
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
	
	public static void colresverify()
	 { //called multiple times orhandling it here
		 ResultSet rs = null;
		 String statcode = null, msg = null, premout = null;
		 try{
		 Connection conn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.12:1522:UATDB", "vm1dta", "vm1dta12#$");
		
		 Statement stmt = conn1.createStatement();
		 //reader loop returns polnum starts here
		 
		 for (String polnum : colreschdr)
		 {
		 try {
			 //polnum = colreschdr[chdrctr];
			 rs = stmt.executeQuery("select * from GCHD where CHDRNUM ='" + polnum + "'");
			 while(rs.next())
			 {statcode =getDBval(rs, "STATCODE");
				 if(statcode.equals("IF"))
				 {	msg = "Statcode verified as 'IF' for chdrnum " + polnum;
					 System.out.println(msg);
				 }else
				 {
					 msg = "Statcode expected: 'IF'. Actual value = "+ statcode + " for chdrnum " + polnum;
					 System.out.println(msg);

				 }
			 }
			 
			 
			 rs.close();
			 //2nd verification
			 System.out.println("2nd verification");
			rs = stmt.executeQuery("select * from GBIHPF where chdrnum ='"+ polnum +"' and ZBKTRFDT = (select TFRDATE from zcripf where chdrnum = '" + polnum + "')");
			while(rs.next())
			{
				premout = getDBval(rs,"PREMOUT");
				System.out.println("inside 2nd verification.");
				if(premout.equals("N"))
				{
					msg = "PREMOUT verified as 'N' for chdrnum " + polnum;
					System.out.println(msg);
					 
				}else
				{
					 msg = "PREMOUT expected: 'N'. Actual value = "+ premout + " for chdrnum " + polnum;
					 System.out.println(msg);
				}
			}
					 
		//reader loop ends here			 
		 System.out.println("at the end of the veifiacation block.");
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

	public static void colresdata(String folder, String businessdate)
	{
		String iq= null, query = "select a.chdrnum,b.zbktrfdt as trandate,b.zbktrfdt as tfrdate from gchipf a join gbihpf b on a.chdrnum = b.chdrnum and b.premout = 'Y' where a.zcolmcls = 'F' ";
		
		query ="select a.chdrnum,b.zbktrfdt as trandate,b.zbktrfdt as tfrdate from gchipf a join gbihpf b on a.chdrnum = b.chdrnum and a.chdrnum ='62259953'";
		
		ResultSet rs = null;
		Statement stmt=null,  stat = null;
		Connection conn1 = null;
		
		String tchdrnum = null;
		String ttrandate = null;
		String ttfrdate = null;
		
		
		
		try {
			conn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.12:1522:UATDB", "vm1dta", "vm1dta12#$");
			stat = conn1.createStatement();
			stmt =conn1.createStatement();
			 rs = stat.executeQuery(query);
			 
			 while(rs.next())
			 {
				 tchdrnum = getDBval(rs, "CHDRNUM");
				// ttrandate = getDBval(rs, "TRANDATE");
				// ttrandate = businessdate;
				 ttrandate = businessdate.substring(0, 4) + businessdate.substring(5,7) + businessdate.substring(8,10);
				 //System.out.println("the business date as per the DB: " + ttrandate);
				 
				 ttfrdate = getDBval(rs, "TFRDATE");
				 
				 //iq = "INSERT INTO ZCRIPF VALUES ("+"''," + tchdrnum + "," + ttrandate + "," +ttfrdate + ")";
				 
				 iq ="insert into ZCRIPF(TRANDATE, TFRDATE, CHDRNUM, DSHCDE, USRPRF, JOBNM, DATIME) values ('"+ ttrandate +"', '" + ttfrdate + "', '" + tchdrnum + "', '00', 'UNDERWR1', 'G1MTRREIF', '14-JUN-17 06.08.27.365000000 PM')"; 
				  
				 int f = stmt.executeUpdate(iq);
				 
				 if (f==1)
				 	System.out.println("inserted for " + tchdrnum);
				 else
					 System.out.println("issue in updation for" + tchdrnum);
				 colreschdr.add(tchdrnum.toString());
				
				 
				 
				 //write to a file too for result comparision.
				 
			 }
			 conn1.close();
		} catch (Exception e) {
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
	
	
	
}
