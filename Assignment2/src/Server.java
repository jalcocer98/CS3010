import java.net.*; 
import java.io.*; 
import java.text.*;
import java.time.*;
import java.util.*;

public class Server {
	//initialize socket and input stream 
	private Socket socket = null; 
	private ServerSocket server = null; 
	private DataInputStream in = null;
	private DataOutputStream out = null;

	
	public static void main(String args[]) throws IOException
	{ 
		Server server = new Server(5000); 
		
		while(true) {
			Socket connectionSocket = server.acceptCon();
		}
	}
	public Server(int portNumber) throws IOException {
		server = new ServerSocket(portNumber);
		
	}
	public Socket acceptCon() throws IOException {
		return server.accept();
	}
}
