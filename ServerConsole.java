

import common.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class ServerConsole implements ChatIF{
	
	
	final public static int DEFAULT_PORT = 5555;

	
	EchoServer server;   
	  
	
	
	//this is the constructor initializing the value for echoserver
    public ServerConsole(int port) {
    	server = new EchoServer(port);
    	try {
    		server.listen();
    	}
    	catch (IOException exc) {
            exc.printStackTrace();
        }
    }
	
    
    /* 
     * this is exersise 2b) part i) where stuff typed on the server's console by end-user is echoed
     * to the server's console and to clients
     * */
	public void accept() {
		
		try {
			
			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			
			
			String msg;
			while (( msg = consoleInput.readLine()) != null) {
		        server.handleMessageFromServerUI(msg);
		        System.out.println(msg);
		    }
		}
		catch (Exception exc) {
			exc.printStackTrace();
	    }
	}
	
	/*this is part ii) of exercise 2b) where we display the "SERVER MSG>" to the end-user
	 * 
	 */
	public void display(String msg) {
		if (msg.startsWith("#")) {
			return;
		}
		
		System.out.println("SERVER MSG>" + msg);
	}
	
	public static void main(String[] args) {
		
		int prt;
		
		try {
	    	prt = Integer.parseInt(args[0]);
	    } 
		
		catch (Exception e) {
	    	prt = DEFAULT_PORT;
	    }
		
		ServerConsole srv = new ServerConsole(prt);
		srv.accept();
	}

}
