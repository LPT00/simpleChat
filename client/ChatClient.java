// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
 
  public ChatClient(String logID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.openConnection();
  
  
  /*this is for part b) of question 3, where we are telling the server that 
   * a client has been connected to the system and as seen below we are 
   * appending their loginID 
   */
    this.sendToServer("#login " + logID);
  
  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   * 
   * This is part 2a) where we are going to implement the different commands. 
   * the quit command will terminate gracefully.
   * the logoff command will cause the client to not quit, but disconnect from the server (closeConnection())
   * the sethost command will call setHost to the client if the client is not already connected to the server.
   * if we are already connected to the server, we will print out saying already connected
   * the setport command will call setport and has the same conditions as the sethost
   * the login command will login the client to the server, but if the client is already connected, there will be a warning.
   * the gethost will display the name of the host
   * the getport wil display the current number of the port.
   */
  public void handleMessageFromClientUI(String msg)
  {
	  if (msg.startsWith("#")) {
		   
		   String[] messages = msg.split(" ");
		   String cmd = messages[0];
		   
		   switch(cmd) {
		   		
			   case "#quit":
				   
				   quit();
				   break;
			   
			   case "#logoff":
				   
				   try {
		               closeConnection();
		           } 
				   catch (IOException exc) {
		               exc.printStackTrace();
		           }
				   break;
				   
			   case "#sethost":
				   
				   if (this.isConnected()) {
					   System.out.println("Hello, you are already connected");
				   }
				   else {
					   this.setHost(messages[1]);
				   }
				   break;
				   
			   case "#setport":
				   
				   if (this.isConnected()) {
					   System.out.println("Hello, you are already connected");
				   }
				   else {
					   this.setPort(Integer.parseInt(messages[1]));
				   }
				   break;   
				   
			   case "#login":
				   
				   if (this.isConnected()) {
					   System.out.println("Hello, you are already connected");
					   
				   }
				   else {
					   try {
			                this.openConnection();
			           } 
					   catch (IOException exc) {
			               exc.printStackTrace();
			           }
				   }
			   
				   break;
				   
			   case "#gethost":
				   
				   System.out.println("Host: "+ this.getHost());
				   break;
			 
			   case "#getport":
				   
				   System.out.println("Port: "+ this.getPort());
				   break;
			  
			   default:
				   
				   System.out.println(cmd + " is an invalid input");
				   break;
		
		   }
	   }
	   else {
		   try {
              sendToServer(msg);
          }
		   catch (IOException exc) {
              
              exc.printStackTrace();
              
              quit();
          }
	   }
  }
  
  /**
 	 * Hook method called after the connection has been closed. The default
 	 * implementation does nothing. The method may be overriden by subclasses to
 	 * perform special processing such as cleaning up and terminating, or
 	 * attempting to reconnect.
 	 * 
 	 * This is part 1a of the assignment, we are just printing out a message
 	 * saying that the server has shut down 
 	 */
   @Override
   public void connectionClosed() {
 	  System.out.println("server has shut down");
 	  
   }
   /**
 	 * Hook method called each time an exception is thrown by the client's
 	 * thread that is waiting for messages from the server. The method may be
 	 * overridden by subclasses.
 	 * 
 	 * @param exception
 	 *            the exception raised.
 	 */
   @Override
   public void connectionException(Exception exception) {
 	  System.out.println("WARNING - The server has stopped listening for connections\n" + "SERVER SHUTTING DOWN! DISCONNECTING\n"
      + "Abnormal termination of connection");
 	  quit();
 	  
   }
   
   
   public void handleMessageFromServer(String msg) {
	   clientUI.display(msg.toString());
   }
   
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
}
//End of ChatClient class
