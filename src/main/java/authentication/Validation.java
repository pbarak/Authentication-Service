package authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

public class Validation {
	   static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";   
    static final String DB_URL = Database.getURL();

    //  Database credentials
    static final String USER = Database.getUsername();
    static final String PASS = Database.getPassword();
    static Connection conn=null;
	static PreparedStatement stmt=null;
	public static  Map validateUser(String username, String password) {		
		Map userData = getUser(username);
		if (userData == null) {
			return null;
		}
		System.out.println("Checking password");
		
		String pwdhash = userData.get("pwdhash").toString();
		System.out.println(password + ""+pwdhash);
	
		if(BCrypt.checkpw(password, pwdhash)) {
			System.out.println("Passwords match");
			userData.remove("pwdhash");
			return userData;
		}
		System.out.println("Passwords don't match");
		return null;
	}
	
	
	public static Map getUser(String username) {
		Map<String,String> userData = new HashMap<String,String>();
		try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);

		  	String queryString = "SELECT USERNAME, NAME, NICKNAME, PASSWORD FROM USERS WHERE USERNAME = ?";		
		      stmt = conn.prepareStatement(queryString);
		      stmt.setString(1, username);
		      ResultSet rs =stmt.executeQuery();
		      while(rs.next()){
		    	  System.out.println("Mapping to userdata");
		      	userData.put("username",rs.getString("USERNAME"));
				userData.put("name",rs.getString("NAME"));
				userData.put("nick",rs.getString("NICKNAME"));
				userData.put("pwdhash",rs.getString("PASSWORD"));
		       }
		 

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();	   
		    
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		      
		   }//end try
		if(userData.isEmpty()) {
			System.out.println("Userdata is empty");
			return null;
		}
		System.out.println("returning userdata");
		return userData;
	}
}