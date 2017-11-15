import java.sql.*;

import javax.swing.JOptionPane;

import java.lang.*;


public class Dbtest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i=0;
		
	try {
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:INTEGDB1","vm1dta","vm1dta12#$");
			
			Statement stmt=con.createStatement();
			
			ResultSet rs=stmt.executeQuery("SELECT * FROM ZMUIPF"); 
			
			JOptionPane.showMessageDialog(null, "success!!!");
			while(rs.next())  
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
				
				con.close();  
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
