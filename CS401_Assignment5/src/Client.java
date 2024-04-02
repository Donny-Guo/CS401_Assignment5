import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
	public void go() {
		
		// set up socket to connect to server
		try (Socket sock = new Socket("127.0.0.1", 5000)) {
			
			// set up reader + deserializer
			ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
			
			// set up writer + serializer
			ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
			
			// set up scanner
			Scanner scanner = new Scanner(System.in);
			String line;
			
			// pass a login message to server
			Message login = new Message(MessageType.LOGIN, Status.SENT, "Client Login Request");
			writer.writeObject(login);
			
			// read login message from server (expecting status success)
			Message loginReceipt = (Message) reader.readObject();
			
			// if login message status is success, continue
			if (loginReceipt.getType() == MessageType.LOGIN 
				&& loginReceipt.getStatus() == Status.SUCCESS) {
				
				// while loop:
				while(true) {
					// Prompt the user to enter message
					System.out.println("Enter lines of text OR enter logout to log out.");
					line = scanner.nextLine();
					
					// if message is not logout:
					if (!line.equals("logout")) {
						
						// send this text message 
						Message textMsg = new Message(MessageType.TEXT, Status.SENT, line);
						writer.writeObject(textMsg);
						
						// read the receipt from server
						Message textMsgReceipt = (Message) reader.readObject();
						
						// if text message receipt is received, continue
						if (textMsgReceipt.getType() == MessageType.TEXT 
							&& textMsgReceipt.getStatus() == Status.RECEIVED) {
							
							System.out.println("Server responds: " + textMsgReceipt.getText());
							
						} else { // else: print out status
							
							System.out.println("Server failed to receive message: " + line);
							System.out.println("Debug information as following: ");
							printMessage(textMsgReceipt);
							
						} // end if/else text message receipt is received
						
						
					} else { // message is logout
		
						// send logout message to server
						Message logout = new Message(MessageType.LOGOUT, Status.SENT, "Client Logout Request");
						writer.writeObject(logout);
						
						// wait for logout success receipt from server
						Message logoutReceipt = (Message) reader.readObject();
						
						// if success: break the while loop
						if (logoutReceipt.getType() == MessageType.LOGOUT 
							&& logoutReceipt.getStatus() == Status.SUCCESS) {
							
							break;
							
						} else { // else: print out status
							
							System.out.println("Server failed to log you out.");
							System.out.println("Debug information as following: ");
							printMessage(logoutReceipt);
							
						} // end if/else logout is successful
						
						
					} // end if/else message is not logout
					
					
				} // end while loop
				
				
			} // end if login message status is success
			
			
			// close reader, writer, scanner
			reader.close();
			writer.close();
			scanner.close();
				
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} // end try/catch with resource
		
		
	} // end method go
	
	public void printMessage(Message msg) {
		System.out.println("Message type: " + msg.getType());
		System.out.println("Message status: " + msg.getStatus());
		System.out.println("Message text: " + msg.getText());
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.go();
	}
	
}
