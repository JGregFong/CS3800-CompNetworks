package chatClient;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
//Jonathan Fong
public class ChatClient {
	
	final static int ServerPort = 1234;

	public static void main(String []args) throws UnknownHostException, IOException{
		Scanner keyboard = new Scanner(System.in);
		
		InetAddress ip = InetAddress.getByName("localhost");
		
		Socket s = new Socket(ip, ServerPort);
		System.out.println("Client starting!");
		
		 DataInputStream dis = new DataInputStream(s.getInputStream()); 
	        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	        // sendMessage thread 
	        Thread sendMessage = new Thread(new Runnable()  
	        { 
	        	boolean checker = true;
	            @Override
	            public void run() { 
	            	
	            	
	                while (checker) { 
	  
	                    // read the message to deliver. 
	                    String msg = keyboard.nextLine(); 
	                    
	                    try { 
	                        // write on the output stream 
	                    	if(msg== "Quit" || msg == "QUIT") {
	                    		dos.writeUTF("Quit");
	                    		stop();
	                    		break;
	                    	}
	                    	else {
	                    		dos.writeUTF(msg);	                    		
	                    	}
 
	                        
	                    } catch (IOException e) { 
	                        e.printStackTrace();   
	                        System.exit(0);
	                    } 

	                    
	                } 
	            }
	            
	            public void stop() {
	            	checker = false;
	            	System.exit(0);
	            }
	            
	        }); 
		
	
	        // readMessage thread 
	        Thread readMessage = new Thread(new Runnable()  
	        { 
	        	boolean checker = true;
	            @Override
	            public void run() { 
	  
	            	
	                while (checker) { 
	                    try { 
	                        // read the message sent to this client 
	                        String msg = dis.readUTF(); 
	                        System.out.println(msg); 
	                        if(msg == "Quitting") {
	                        	stop();
	                        	break;
	                        }
	                    } catch (IOException e) { 
	                    	System.out.println("You have logged out.");
	                        //e.printStackTrace(); 
	                        System.exit(0);
	                    } 
	                } 
	            }
	            
	            public void stop() {
	            	
	            	checker = false;
	            	System.exit(0);
	            }
	            
	            
	        }); 
	  
	        sendMessage.start(); 
	        readMessage.start(); 
	        
	}
	
}
