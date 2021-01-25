package udpClientTest;

//Java program to illustrate Client side 
//Implementation using DatagramSocket 
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner; 
public class UDPClientTest {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(System.in);
		
		DatagramSocket inSocket = new DatagramSocket(343);
		DatagramSocket outSocket = new DatagramSocket();
		outSocket.setSoTimeout(3000);		
		InetAddress ip = InetAddress.getLocalHost();
		byte buf[] = null;
		byte[] receive = new byte[65535];

		while(true) {
			System.out.println("Enter input: ");
			String inp = sc.nextLine();
			buf = inp.getBytes();
			
			DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1234);
			outSocket.send(DpSend);	
			

			DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
	
			try {
				inSocket.receive(DpReceive);
			}
			catch(SocketTimeoutException e) {
				System.out.println("Timeout reached! " + e);
				continue;
			}
			String serverResponse = new String(receive);			

			
			System.out.println("Server Echo: " + serverResponse);
			
			if(inp.equals("bye")) {
				break;
			}
		}
		
		
	}

}
