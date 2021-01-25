package chatServer;

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;
//Jonathan Fong
public class ChatServer {
	
	static Vector<ClientHandler> ar = new Vector<>();
	static int i = 0;
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		System.out.println("Server Starting!");

		ServerSocket ss = new ServerSocket(1234);
		Socket s;
		
		while(true) {
			s = ss.accept();
			
			System.out.println("New client request received :" + s);
			System.out.println("Client " + i +" has joined.");
			
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			
            System.out.println("Creating a new handler for this client...");
			
            ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); 
            
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
            
		}
	}

}

class ClientHandler implements Runnable{

    Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
    Date currentDate;
      
    // constructor 
    public ClientHandler(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String received;
		while (true) {
			
		       try
	            { 
	                // receive the string 
	                received = dis.readUTF(); 
	        		currentDate = new Date();
	                //System.out.println(received); 
	                  
	                
	                
	                // break the string into message and recipient part 
	                /*
	                StringTokenizer st = new StringTokenizer(received, "#"); 
	                String MsgToSend = st.nextToken(); 
	                String recipient = st.nextToken();
	                */
	                
	                
	                String message = this.name+ ": " + received +" ["+ currentDate.toString() + "]";
	                System.out.println(message);
	  
	                // search for the recipient in the connected devices list. 
	                // ar is the vector storing client of active users 
	                for (ClientHandler mc : ChatServer.ar)  
	                { 
	                	
	                    if(!mc.name.equals(this.name) && mc.isloggedin == true) {
	                    	if(received == "Quit" || received == "QUIT") {
	                    		mc.dos.writeUTF(this.name + " has logged off");
	                    	}
	                    	else {
	                    		mc.dos.writeUTF(message);           		
	                    	}

	                        break; 
	                	}
	                    
	                    
	                } 
	                if(received.equals("Quit") || received.equals("QUIT")){ 
	                    this.isloggedin=false; 
	                    this.dos.writeUTF("Quitting");
	                    System.out.println(this.name + " has logged off.");
	                    this.s.close(); 
	                    break; 
	                } 
	                
	            } catch (IOException e) { 
	                  
	                e.printStackTrace(); 
	            } 
	              
	        } 
	        try
	        { 
	            // closing resources 
	            this.dis.close(); 
	            this.dos.close(); 
	              
	        }catch(IOException e){ 
	            e.printStackTrace(); 
	        } 
			
		}
	
	
}
