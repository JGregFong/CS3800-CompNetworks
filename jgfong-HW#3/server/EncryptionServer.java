package server;

//Jonathan Fong
//CS3800 Homework 3
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/")
public class EncryptionServer {
	String key = "Merry Christmas, Professor!";
	
	@POST
	@Path("/encryptDecrypt")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		String received = crunchifyBuilder.toString();
		
		
		System.out.println("Data Received: " + received);

		String message = "";
		int messageIndex = 35;
		while(received.charAt(messageIndex)!= '\"') {
			message+=received.charAt(messageIndex);
			messageIndex++;
		}
		String decrypted = HEncryptor.decrypt(message, key);
		System.out.println(message + " to " + decrypted);
		char check = 'A';
		System.out.println(received.charAt(21));
		if(received.charAt(21) == 'E') {
			message = HEncryptor.asciitoHEX(decrypted);
			message = HEncryptor.encrypt(message, key);
			check = 'H';
		}
		else if(received.charAt(21) =='D') {
			message = HEncryptor.hexToASCII(decrypted);
			message = HEncryptor.encrypt(message, key);
			check = 'A';
			
		}
		String string = "{\"EncryptionStatus\":\"" + check+ "\",\"message\":\"" + message + "\" }";
		System.out.println(string);
		// return HTTP response 200 in case of success
		return Response.status(200).entity(string).build();
	}
 
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "CrunchifyRESTService Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
 
}