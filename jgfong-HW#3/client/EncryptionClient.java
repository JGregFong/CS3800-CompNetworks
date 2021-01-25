package client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
 
import org.json.JSONObject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
//Jonathan Fong
//CS3800 HW3
 
public class EncryptionClient extends Application{
	Button encryptButton = new Button("Encrypt");
	Button decryptButton = new Button("Decrypt");
	TextField textToEncrypt = new TextField();
	TextField dataToDecrypt = new TextField();
	
	Label encryptText = new Label("Text To Encrypt ->");
	Label decryptText = new Label("<- Data to Decrypt");
	
	String key = "Merry Christmas, Professor!";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		HBox hbox1 = new HBox(10, encryptButton, decryptButton);
		HBox hbox2 = new HBox(10, textToEncrypt, dataToDecrypt);
		HBox hbox3 = new HBox(10, encryptText, decryptText);
		
		VBox vbox = new VBox(10, hbox3, hbox2, hbox1);
		
		primaryStage.setTitle("Text Encryptor");
		Scene myScene = new Scene (vbox, 500,300);
		
		vbox.setAlignment(Pos.CENTER);
		hbox1.setAlignment(Pos.CENTER);
		hbox2.setAlignment(Pos.CENTER);
		hbox3.setAlignment(Pos.CENTER);
		
		
		
		encryptButton.setOnAction(event ->{
			
			String message = textToEncrypt.getText();
			
			String result = transmit(true, message);
			
			dataToDecrypt.setText(result);
			
		});
		
		decryptButton.setOnAction(event-> {
			
			String message = dataToDecrypt.getText();
			
			String result = transmit(false, message);
			
			textToEncrypt.setText(result);
			
		});
		
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	public String transmit(boolean encrypt, String message) {
			String check;
			
			if(encrypt) {
				check = "E";
			}
			else {
				check = "D";
			}
			
			String encryptedMessage = HEncryptor.encrypt(message, key);
		String string = "{ \"EncryptionStatus\" : \"" + check+ "\", \"message\": \"" + encryptedMessage + "\" }";
		try {
 
			// Step1: Let's 1st read file from fileSystem
			// Change CrunchifyJSON.txt path here

			
 
			JSONObject jsonObject = new JSONObject(string);
			System.out.println(jsonObject);
 
			// Step2: Now pass JSON File Data to REST Service
			try {
				URL url = new URL("http://localhost:8080/ATextEncryptor/api/encryptDecrypt");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(jsonObject.toString());
				out.close();
 
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String received = null;
				
				String toDecode = "";
				while ((received = in.readLine()) != null) {
					System.out.println("Message Received: " + received);
					toDecode = received;
				}
				
					String result = "";
					System.out.println();	
					int messageIndex = 35;
					System.out.println("Message check:"+toDecode.charAt(messageIndex));
				if(toDecode.charAt(21) == 'H') {
					while(toDecode.charAt(messageIndex)!= '\"') {
						result+=toDecode.charAt(messageIndex);
						messageIndex++;
					}
					
				}
				else if(toDecode.charAt(21) =='A') {
					while(toDecode.charAt(messageIndex)!= '\"') {
						result+=toDecode.charAt(messageIndex);
						messageIndex++;
					}
				}
				result = HEncryptor.decrypt(result, key);
				System.out.println(result);
				
				string = result;
				System.out.println("\nMessage returned.");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				System.out.println(e);
			}
 

		} catch (Exception e) {
			e.printStackTrace();
		}


		return string;
	}


}