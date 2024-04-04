import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	public void go() {
		
		// set up thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		
		// set up server socket
		try (ServerSocket serverSock = new ServerSocket(5000)) {
			
			// print out server info
			InetAddress localHost = InetAddress.getLocalHost();
			System.out.println("Server running at IPv4 Address " + localHost.getHostAddress()
								+ " at port " + serverSock.getLocalPort());
			System.out.println("ServerSocket awaiting connections...");
			
			// while true
			while(true) {
				// wait for incoming connection
				// hand it to a thread
				threadPool.execute(new threadJob(serverSock.accept()));
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} // end try/catch with resource
			
		threadPool.shutdown();
		
	} // end method go
	
	private class threadJob implements Runnable {
		private Socket sock;
		
		public void run() {
			
			// print client connection info
			System.out.println("A new client connected: " + sock);
			
			try {
				// set up writer
				ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
				
				// set up reader
				ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
				
				// wait for login message
				Message login = (Message) reader.readObject();
				
				// check login message
				if (login.getType() == MessageType.LOGIN 
					&& login.getStatus() == Status.SENT) {
					
					// print out login success
					System.out.println("A client successfully login: " + sock);
					
					// return back success login message
					login.setStatus(Status.SUCCESS);
					writer.writeObject(login);
					
					// while loop waiting for messages
					Message msg;
					while((msg = (Message) reader.readObject()) != null) {
						// ignore login message
						if (msg.getType() == MessageType.LOGIN) continue;
						
						// print message text
						System.out.println(msg.getText());
						// if message is text
						if (msg.getType() == MessageType.TEXT) {
					
							// change to received
							msg.setStatus(Status.RECEIVED);
							// all caps message
							String newText = msg.getText().toUpperCase();
							msg.setText(newText);
							// send it back to client
							writer.writeObject(msg);
							
						} else {// else: if message is logout
							// change status to success
							msg.setStatus(Status.SUCCESS);
							// send it back to client
							writer.writeObject(msg);
							// break the while loop
							break;
						}
					} // end while loop
					
					// close the reader and writer
					reader.close();
					writer.close();
					
					
				} else { // print message 
					System.out.println("Fail to login in.");
					System.out.println("Debug info as follows");
					printMessage(login);
				} // end if login message is valid
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			} finally { // finally close sock
				try { 
					sock.close();
					System.out.println("A client successfully logout and Close: " + sock);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} // end method run
		
		
		private void printMessage(Message msg) {
			System.out.println("Message type: " + msg.getType());
			System.out.println("Message status: " + msg.getStatus());
			System.out.println("Message text: " + msg.getText());
		}
		
		
		// constructor
		threadJob(Socket sock) {
			this.sock = sock;
		}
		
	} // end private class threadJob
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.go();
	}
}
