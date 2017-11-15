package Negi;

import java.sql.*;
import java.util.*;


public class Oradata {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:INTEGDB1","vm1dta","vm1dta12#$");
			
			Statement stmt=con.createStatement();
			
			ResultSet rs=stmt.executeQuery("SELECT * FROM ZMUIPF"); 
			
			while(rs.next())  
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
				
				con.close();  
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
