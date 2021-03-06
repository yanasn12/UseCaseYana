package jdbc;
//this is

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class mysqlConnection {

//Explanation: draw method pulling the table with all fields
//InPut: Receives the name of the table we want to pull from
//OutOut: Get a linked list with all the details of the table.	Each entry contains the key line in addition to all the information that it 
	public static ArrayList<String> pull (String DB)
	{
		ArrayList<String> data= new ArrayList<String>(); // a Temporary location where we keep the table with all the details
		Statement stmt; // Enable to create a statment
		String line=""; // a temperary place where we keep every line of info
		try 
			{
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	        } catch (Exception ex) {/* handle the error*/}
		try 
			{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM "+ DB+";");
			// ResultSetMetaData a class that get the info from the Sql and use it, in this case to tell as how many Values we need to get every time
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
	 		while(rs.next())
	 		  { 
		 			for(int i=1; i <= columnsNumber; i++)
		 				line=line+ " " + rs.getString(i);
				data.add(line);
				line="";
	 		  } 
			rs.close();
			return data;
			} catch (SQLException e) {e.printStackTrace(); return null;}
	}
	
	
//Explanation: a method that checks whether if the value is already in the table (check the key of the table), 
//          this test is carried out before the PUSH to make sure that there will be duplication of information,
//      	and before UPDATE, to make sure that there is a value we can update
//InPut: Receives the name of the table we want to check from and data that contain the key value we check  
//OutOut: return TRUE   if the key info is not in the DB
//        return FALSE if the key info is already in the DB
public static boolean  checkme (String DB,ArrayList<String> data)
{
	Statement stmt; // Enable to create a statment
	boolean result=true; // a startup value that is initialized "true"
	String insertTableSQL = "SELECT * FROM "+ DB + " WHERE PhysicianName="+data.get(0);// the structure of the query
	try 
	{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Exception ex) {/* handle the error*/}

	try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(insertTableSQL);
		//if there isnt any line in the sql table with the key value the IF condition will not be activate 
		if(rs.next())
		{
			result=false;
		}
			
		return result;							
	} catch (SQLException e) {System.out.println("error: checkme- DB "+DB+" PhysicianName "+ data.get(0));return false;}
	 		
}
//Explanation: The method updates the value in the table by using it key value
//InPut: Receives the name of the table we want to update from and data that contain the key value and the value we want to change  
//OutOut: the method return true if it succeeded to update , else false;
public static boolean update (String DB,ArrayList<String> data)
	{
	 String updateString= "UPDATE "+DB.toString()+" SET Specialization=? WHERE PhysicianName=?;";// the structure of the query
	 try {
		   Class.forName("com.mysql.jdbc.Driver").newInstance();
         } catch (Exception ex) {/* handle the error*/}
	 try {	
			 Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
			 PreparedStatement insertTableSQL = con.prepareStatement(updateString);
			 insertTableSQL.setString(1, data.get(1).toString());
			 insertTableSQL.setString(2, data.get(0).toString());
			 insertTableSQL.executeUpdate();
			 return true;
	 	} catch (SQLException e) {
				System.out.println("unable to update");
				return false;}	 		
	}

//Explanation: The method enter to the table the record we want to add our SQL table
//InPut: Receives the name of the table we want to add info and the data we want to enter
//OutOut: return TRUE   if the info was add.
//		  return FALSE if the info was not add.
public static boolean push (String DB,ArrayList<String> data)
	{
		String insertTableSQL = "INSERT INTO "+DB + "(PhysicianName,Specialization) VALUES"+ "(?,?)";// the structure of the query
		try 
		{
           Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}

		try {	
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
			PreparedStatement update = con.prepareStatement(insertTableSQL);
			update.setString(2, data.get(1));
			update.setString(1, data.get(0));
			update.executeUpdate();
			return true;
		} catch (SQLException e) 
		{
				System.out.println("unable to push -PUSH");
				return false;
		}	 		
	}

//Explanation: The method enter to the table the record we want to add our SQL table
//InPut: Receives the name of the table we want to delete the info and the data (key data) we want to delete 
//OutOut: return TRUE   if the info was delete.
//return FALSE if the info was not delete.
public static boolean  delete(String DB,String key)
{
	String insertTableSQL = "DELETE FROM "+ DB.toString() + " WHERE PhysicianName=?;";// the structure of the query
	try 
	{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Exception ex) {/* handle the error*/}

	try {	
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
	      
		PreparedStatement st = con.prepareStatement(insertTableSQL);
	     st.setString(1, key.toString());	 
	      st.executeUpdate();
	        return true;
		}
	catch (SQLException e) 
		{
		System.out.println("unable to Delete -delete :DB - "+ DB+ " key-"+key);
		return false;
		}
 		
}

//Explanation: draw method pulling the info from the table with all fields of the Specific key
//InPut: Receives the name of the table we want to pull info from and Specific key
//OutOut: return the a ArrayList with one tab (the Specific key)
 public static ArrayList<String> pullByKey(String DB, String SearchKey)
 {
		String pullingString="SELECT * FROM "+DB.toString() + " WHERE PhysicianName='"+SearchKey+"'";// the structure of the query
		ArrayList<String> data= new ArrayList<String>();  // a Temporary location where we keep the table with all the details
		String line=""; // a temperary place where we keep every line of info
		try 
		{
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	    } catch (Exception ex) {/* handle the error*/}
		try {	
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sql1","root","xhxnt");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(pullingString);
			// ResultSetMetaData a class that get the info from the Sql and use it, in this case to tell as how many Values we need to get every time
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
	 		while(rs.next())
	 		{
	 			for(int i=1; i <= columnsNumber; i++)
	 				line=line+ " " + rs.getString(i);
			data.add(line);
			line="";
	 		} 
			rs.close();
			return data;
		} catch (SQLException e){
					e.printStackTrace();
					System.out.println("error: pullByKey DB "+DB+" SearchKey "+ SearchKey);
					return null;} 
}
}