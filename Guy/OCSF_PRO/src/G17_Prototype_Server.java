// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import ocsf.server.*;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class G17_Prototype_Server extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public G17_Prototype_Server(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
    
  {
	  String[] str_arr; 
	  str_arr =  ((String) msg).split(":");
	  String Op_Code = str_arr[0].toUpperCase();
	  String where="sql1.";//Selelct * From DataSet  
	  String msgToClinet="";
	  
	  //Pull
	  if (Op_Code.equals("PULL"))
	  {
		 
		  //pull return string
		  this.sendToAllClients("pulling...");

		  String[] temp;
		  temp = str_arr[1].split(",");
		  where=where+temp[0];
		  ArrayList<String> nameList = new ArrayList<String>();
		  nameList = jdbc.mysqlConnection.pull(where);
		 for(int i=0; i<nameList.size();i++)
			 this.sendToAllClients(nameList.get(i));
		 where="sql1.";
		 msgToClinet="Done";
	  }
	 else if(Op_Code.equals("UPDATE"))//Selelct * From DataSet Where
	  {
		 String[] temp;
		 ArrayList<String> Dataset=new ArrayList<String>();//Selelct * From DataSet
		 temp = str_arr[1].split(",");
		
		 for(int i=1; i<temp.length ;i++)
		 {
			 Dataset.add(temp[i]);
		 }	 
		 if(Dataset.size()==2)
		 {
		  	this.sendToAllClients("updateing...");
		  	where=where+temp[0];		  
		  	if(jdbc.mysqlConnection.checkme(where,Dataset)==true)
		  		msgToClinet="Error";
		  	else
		  	{
		  	jdbc.mysqlConnection.update(where,Dataset);
		  	  System.out.println("Message received: " + msg + " from " + client);
		  	  where="sql1.";
		  	  msgToClinet="Done";
		  	}
		 }
		
	  }
	 else if(Op_Code.equals("DELETE"))
	 {
		 String[] temp;
		 ArrayList<String> Dataset=new ArrayList<String>();//Selelct * From DataSet
		 temp = str_arr[1].split(",");
		 Dataset.add(temp[1]);
			this.sendToAllClients("delete...");
		  	where=where+temp[0];		  
		  	if(jdbc.mysqlConnection.checkme(where,Dataset)==true)
		  		msgToClinet="Error";
		  	else
		  	{
		  	  jdbc.mysqlConnection.delete(where,temp[1]);
		  	  System.out.println("Message received: " + msg + " from " + client);
		  	  where="sql1.";
		  	  msgToClinet="Done";
		  	}		 
	 }
	 else 
		 if(Op_Code.equals("PUSH"))//Selelct * From DataSet Where
		 {
		 String[] temp;
		 ArrayList<String> Dataset=new ArrayList<String>();//Selelct * From DataSet
		 temp = str_arr[1].split(",");
		
		 for(int i=1; i<temp.length ;i++)
		 {
			 Dataset.add(temp[i]);
		 }	 
		 if(Dataset.size()==2)
		 {
		  	this.sendToAllClients("pushing...");
		  	where=where+temp[0];		  
		  	if(jdbc.mysqlConnection.checkme(where,Dataset)==true)
		  		msgToClinet="Error";
		  	else
		  	{
		  	jdbc.mysqlConnection.push(where,Dataset);
		  	  System.out.println("Message received: " + msg + " from " + client);
		  	  where="sql1.";
		  	  msgToClinet="Done";
		  	}
		 }
		 }
		 else 	  
			 if (Op_Code.equals("PULLBYKEY"))
		 			{
			  String[] temp;
			  temp = str_arr[1].split(",");
			  where=where+temp[0];			 
			  this.sendToAllClients("pulling By Key "+temp[1]+"...");
			  ArrayList<String> nameList = new ArrayList<String>();
			  nameList =jdbc.mysqlConnection.pullByKey(where, temp[1]);
			 for(int i=0; i<nameList.size();i++)
				 this.sendToAllClients(nameList.get(i));
			 where="sql1.";
			 msgToClinet="Done";
		
		 			}		
	  this.sendToAllClients(msgToClinet);
	  //
  }
  
  

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    G17_Prototype_Server sv = new G17_Prototype_Server(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
