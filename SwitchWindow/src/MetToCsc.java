import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MetToCsc {

       public static void main(String[] args) {
              // TODO Auto-generated method stub
              //launch login
              String[] result = new String[32];
              String buffStr = new String();
              WebElement buffEle;
              
              
              //data to be taken as input
              String policyNo = "T11SMKT1001";
              String trans = "PA - Death Claim";
              String tranDate = "02/13/2016";
              
              
              File file = new File("D:\\Selenium\\IEDriverServer.exe");
      		  System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
              WebDriver wd = new InternetExplorerDriver();
              Actions action = new Actions(wd);
              wd.get("http://10.159.40.114:9081/wma/login.jsf");
              wd.findElement(By.id("mainform:userId")).sendKeys("mkatragadda2");
              wd.findElement(By.id("mainform:userPassword")).sendKeys("pano");
              Select env = new Select(wd.findElement(By.id("mainform:environment")));
              env.selectByVisibleText("System Integrated Test Environment");
              wd.findElement(By.id("mainform:loginAction")).click();
              
              WebDriverWait wait = new WebDriverWait(wd, 30);
              WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("mainform:searchButton")));
              
              //searching for policy 
              
              wd.findElement(By.id("mainform:clientRequestMasterId")).sendKeys(policyNo);
              wd.findElement(By.id("mainform:searchButton")).click();
              element = wait.until(ExpectedConditions.elementToBeClickable(By.id("mainform:cmdContractPrint")));
              
              //fetching values from COMMON POLICY AREA
              
              
              buffEle = wd.findElement(By.xpath("//td[span = 'Status:']"));
              buffStr = buffEle.getText().split(": ")[1];
              System.out.println("STATUS: "+buffStr);
              //pol status
              
              buffEle = null;
              buffStr = null;
              
              
              buffEle = wd.findElement(By.xpath("//td[span = 'Policy:']"));
              buffStr = buffEle.getText().split(": ")[1];
              System.out.println("POLICY: "+buffStr);
              //policy no

              buffEle = null;
              buffStr = null;
              
              buffEle = wd.findElement(By.xpath("//td[span = 'Company:']"));
              buffStr = buffEle.getText().split(": ")[1];
              System.out.println("COMPANY: "+buffStr);
              //company    

              buffEle = null;
              buffStr = null;
              
              buffEle = wd.findElement(By.xpath("//td[label = 'Statutory Company Code:']/following-sibling::td[1]"));
              buffStr = buffEle.getText();
              System.out.println("STAT COMPANY CODE: "+buffStr);

              buffEle = null;
              buffStr = null;
              //policy stat company
              //Statutory Company Code:
              
              //navigation to ADMINISTRATION
              wd.findElement(By.linkText("Administration")).click();
              element = wait.until(ExpectedConditions.elementToBeClickable(By.id("mainform:contractProfileAdminContractProfileAdminTrxSegmentCurrentServicingAgent")));

              buffEle = wd.findElement(By.id("mainform:contractProfileAdminContractProfileAdminTrxSegmentCurrentServicingAgent"));
              buffStr = buffEle.getAttribute("value").toString();
              System.out.println(buffStr);
              //Servicing agent code || policy producer

              buffEle = null;
              buffStr = null;
              
              //naviagation HISTOTY
              wd.findElement(By.linkText("History")).click();
              //ADD WAIT
              
              buffEle = wd.findElement(By.xpath("//td[span = 'GS - Term Rider']/following-sibling::td[1]"));
              buffStr = buffEle.getText();
              System.out.println("This is the date: " + buffStr);
              

              buffEle = null;
              buffStr = null;
              
              if(buffStr != tranDate)
              {
                     System.out.println("Date not found");
                     //take action to skip other ops for this row.  BREAK
              }
              System.out.println("transaction date:" + tranDate); //
              System.out.println("Transaction code:" + trans); //J
              //set tran date val
              
              //navigation after double click
              action.moveToElement(wd.findElement(By.xpath("//td[span = 'GS - Term Rider']"))).doubleClick().build().perform();
              
              //
              element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[label = 'BasePlanCode:']")));

              buffEle = wd.findElement(By.xpath("//td[label = 'BasePlanCode:']/following-sibling::td[1]"));
              buffStr = buffEle.getText();
              System.out.println("Policy Plan : " + buffStr);
                     
              buffEle = wd.findElement(By.xpath("//td[label = 'DateProcessed:']/following-sibling::td[1]"));
              buffStr = buffEle.getText();
              String days = (String) buffStr.subSequence(4, 7);
              String years = (String) buffStr.subSequence(0, 4);
              buffStr = "=DATE("+ years + ",1," + days + ")";
              System.out.println("Date processed : " + buffStr);
              

              buffEle = null;
              buffStr = null;
              String pathVar = new String();
              int i=0;
              pathVar = "j_id149_" + i;
              if(trans.contains("PA"))
              {
                     //find rows in table
                     while(wd.findElement(By.xpath("//td[@id='"+ pathVar + "']/tr[3]))")).isDisplayed()==true)
                     {
                           buffEle = wd.findElement(By.xpath("//td[@id='"+ pathVar + "']/tr[3]))"));
                           buffStr = buffEle.getText();
                           //enter a row
                           System.out.println("Gross value " + i + " " + buffStr);
                           buffEle = null;
                           buffStr = null;
                           
                           
                     }
                           
              }
              
              //DateProcessed:
              
              
              
              
       
       }
}

