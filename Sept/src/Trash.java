import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Trash {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ResultSet rs;
		String msg = new String();
		msg = "";
		
		try {
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@192.168.2.12:1522:UATDB","vm1dta","vm1dta12#$");
			Statement stmt=con.createStatement();
			
			rs=stmt.executeQuery("Select * from ZMUIPF");
			while(rs.next())  
				msg = rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3);  
			
			//JOptionPane.showMessageDialog(null, "hey");
			
			System.out.print(msg);
			con.close();  			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Issue occured while connecting to the database." + e.getStackTrace());
		}
			
	}

}
