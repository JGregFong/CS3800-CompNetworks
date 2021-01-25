package serverUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.time.LocalDateTime;

public class ServerUDP {
	
	public static void main(String [] args) throws IOException {
		LocalDateTime currentTime = LocalDateTime.now();		
		DatagramSocket inSocket = new DatagramSocket(1234);
		DatagramSocket outSocket = new DatagramSocket();
		byte[] receive = new byte[65535];
		
		System.out.println("Server running.");
		
		
		while(true) {
			
			DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
			inSocket.receive(DpReceive);
			String echo = "[" + currentTime.now().toString() + "] " + data(receive);
			
			byte[] echoByte = echo.getBytes();
			
			InetAddress clientAddress = InetAddress.getLocalHost();
			int clientPort = DpReceive.getPort();
			
			DatagramPacket DpSend = new DatagramPacket(echoByte, echoByte.length, clientAddress, 343);
			outSocket.send(DpSend);	
			
			if(data(receive).toString().equals("bye")) {
				System.out.println("Client sent bye...... Exiting");
				break;
			}
			receive = new byte[65535];
		}
		
	}
	
    public static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
}

