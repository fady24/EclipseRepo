import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHandler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String stmDate = "28/11/2016"; //val;//String stmDate = (String)testData.get(componentModel.getSeleniumExecutor().getSystemDateforKeyword());             
          String StrParam1 = "+" ; //testData.get(componentModel.getParam1()).toString();//"-";
          String StrParam2 = "0" ; //testData.get(componentModel.getParam2()).toString();//"3";
          String StrParam3 = "0" ; //testData.get(componentModel.getParam3()).toString();//"1";
          String StrParam4 = "0"; //testData.get(componentModel.getParam4()).toString();//"1";
          String StrParam5 = "MMDDYYYY" ; //testData.get(componentModel.getParam5()).toString();
              
          int DD = 0;
          int MM = 0;
          int YYYY = 0;
          if (StrParam1.equals("+")) {
              DD = Integer.parseInt(StrParam2);
              MM = Integer.parseInt(StrParam3);
              YYYY = Integer.parseInt(StrParam4);
          } else {
              DD = Integer.parseInt("-" + StrParam2);
              MM = Integer.parseInt("-" + StrParam3);
              YYYY = Integer.parseInt("-" + StrParam4);
          }
          
          String[] arrDate = stmDate.split("/");

          int iDay = Integer.parseInt(arrDate[0]);
          int iMonth = Integer.parseInt(arrDate[1]);
          int iYear = Integer.parseInt(arrDate[2]);
          
          System.out.println(iDay);
          System.out.println(iMonth);
          System.out.println(iYear);
          
          SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
          SimpleDateFormat sdf2 = new SimpleDateFormat("MM/DD/YYYY");
              
          Calendar c1 = Calendar.getInstance();
          c1.set(iYear,iMonth,iDay);
          
          String  value = c1.getTime().toString();
          System.out.println(value);
          
          
          c1.add(Calendar.DAY_OF_MONTH,DD);
          c1.add(Calendar.MONTH,MM);
          c1.add(Calendar.YEAR,YYYY);
          
          value = sdf.format(c1.getTime());
          
          
          if (StrParam5.equals("MMDDYYYY")){
              //value = sdf2.format(c1.getTime());
          }           
        System.out.println(value);
        //333/11/2016
	}

}
