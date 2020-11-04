// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;   
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  
  //added logID here so that we can take the logID as the first parameter in main 
  
  public ClientConsole(String logID, String host, int port) 
  {
    
     try {
		client= new ChatClient(logID, host, port, this);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
   
   
    
    // Create scanner object to read from console
    //we don't need this anymore now as we have the userID 
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	/*
	 *This is exercise 3a, and here, I added in the logID parameter which will 
	 *be the first argument added in the console
	 *
	 */
	String logID;
	
    String host;
    
    int prt;
    
    //here, we are initializig the logID to be the first argument given in the console
    try {
    	logID = args[0];
    }
    catch (ArrayIndexOutOfBoundsException exc) {
    	System.out.println("ERROR - No login ID specified.  Connection aborted.");
    	System.exit(1);
    	return;
    }


    try {
        host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e) {
    	host = "localhost";
    }
    
    
    /*
     * This is part b of question 1. Here, we are accepting the 
     * input of a port and if no input, use the default port of 5555
     */
    try {
    	prt = Integer.parseInt(args[2]);
    } 
    catch (Exception exc) {
    	prt = DEFAULT_PORT;
    }
    
    /*here we create a new clientconsole object that creates a chatclient instance 
     * which contains the same parameters
     */
    
    //here we added the logID so that we can create a new object with logID as a parameter
    //the constructor had to be changed to accomodate this change
    ClientConsole chat = new ClientConsole(logID, host, prt);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
