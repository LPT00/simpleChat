// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;


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
public class EchoServer extends AbstractServer 
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
  public EchoServer(int port) 
  {
    super(port);
  }

  //Instance methods ************************************************
  
  
  /*
   * this is part c of question 1, where we are printing a message that the 
   * client has connected
   */
  
  @Override
  public synchronized void clientConnected(ConnectionToClient client) {
	  

	  String mesg = "A new client is attempting to connect to the server.\n" + 
	  		"Message received: #login " + client.getId() + " from null.\n"
	  		+ client.getId() + " has logged on.\n";
	  
	  this.sendToAllClients("SERVER MSG>" + mesg);
	  
	  System.out.print(mesg);
  }
  
  /*
   * this is also part c of question 1, where we are printing a message that the 
   * client has disconnected
   */
  
  @Override
  public synchronized void clientDisconnected(ConnectionToClient client) {
	  
	  String mesg = "You have been disconnected!!";
	  
	  this.sendToAllClients("SERVER MSG>" + mesg);
	  
	  System.out.print(mesg);
	  
  }
  
  /*
   * this is the last part of part c of question 1, where we are printing a message that the 
   * client has disconnected also, since there is an exception
   */
  
  @Override 
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  
	  String mesg = "You have been disconnected!!";
	  
	  this.sendToAllClients("SERVER MSG>" + mesg);
	  
	  System.out.print(mesg);
	  
  }
  
  
  
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  /*
	   * Here is going to be implementing part i) of 3c) where the #login command is 
	   * recognized from the server when .sendtoserver method is used by the client to 
	   * send a message to this server. In order to do this, we will handle the message 
	   * in a way similar to before where we check if the object starts with a # 
	   */
	  //here we need to convert the object into a string in order to read it and detect the #
	  String mesg = msg.toString();
	  if (mesg.startsWith("#")) {
		  
		  String first = "";
		  String last = "";
		  
		  for (int i = 1; i < mesg.length() ; i++) {
			  if (mesg.charAt(i) == ' ' ) {
				  for (int j = 1; j < i; j++) {
					  first = first + mesg.charAt(j);
				  }
				  for (int k = i + 1; k < mesg.length(); k++) {
					  last = last + mesg.charAt(k);
				  }
				  break;
			  }
		  }
		  if (first.equalsIgnoreCase("login") && last != null){

			  if (client.getInfo("logID") == null) {
				  client.setInfo("logID", last);
			  }
			  else {
				  try {
					  System.out.println("Hello, your loginID has already been made");
					  client.close();
				  }
				  catch (IOException exc) {
					  exc.printStackTrace();
				  }
			  }
		  }
	  }
	  
	  else {
		  if (client.getInfo("logID") == null) {
			  try {
				  System.out.println("Hello, you need to make a loginID before sending messages");
				  client.close();
			  }
			  catch (IOException exc) {
				  exc.printStackTrace();
			  }
		  }
		  else {
			  System.out.println("Message received: " + msg + " from " + client.getId());
			  this.sendToAllClients(msg);
			  
		  }
	  }
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
  
  /*
   * This method is for exercise 2 part c which is similar to exercise 2 part a)
   * #quit will make the server quit gracefully like before, but we don't have to terminate
   * before exiting the program
   * #stop will make the server stop listening to the new clients 
   * #close will do the same thing as #stop, but also will disconnect the other clients
   * #setport will call the setport method as said before 
   * #start will make the server listen for new clients, but the server must not be active
   * #getport just displays the port number that is currenty in the system
   * 
   */
  public void handleMessageFromServerUI(String msg) {
	  
	  if (msg.startsWith("#")) {
		  
		  String[] messages = msg.split(" ");
		  String cmd = messages[0];
		  
		  switch(cmd) {
		  
		  case "#quit":
			  try {
				  this.close();
			  }
			  catch (IOException exc) {
				  System.exit(5);
			  }
			  System.exit(0);
			  break;
			  
		  case "#stop":	
			  this.stopListening();
			  break;
			  
		  case "#close":
			  try {
				  this.close(); 
			  }
			  
			  //disconnect all clients
			  catch (IOException exc) {
	               exc.printStackTrace();
			  }
			  break;

		  case "#setport":
			  if (this.getNumberOfClients() < 1) {
				  
				  int newport = Integer.parseInt(messages[1]);
				  
				  super.setPort(newport);
				  System.out.println("Port is now: " + newport);
			  }
			  else {
				  System.out.println("There are still clients and the server is not closed");
			  }
			  break;
			  
		  case "#start":
			  if (!this.isListening()) {
				  
				  try {
					  this.listen();
				  }
				  catch (IOException exc) {
		               exc.printStackTrace();
				  }
			  }
			  else {
				  System.out.println("Hello there, the server has already started");
			  }
			  break;
			  
		  case "#getport":
			  
			  System.out.println("Port: " + this.getPort());
			  break;
			  
          default:
        	  
        	  System.out.println(cmd + "is an invalid input");
			  
		  }
	  }  
	   
		
  }
	  
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int prt = 0; //Port to listen on

    try
    {
      prt = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      prt = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(prt);
    
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
