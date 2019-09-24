import java.net.*;
import java.io.*;

public class TimeClient {
	private Socket socket = null; 
	private DataInputStream input = null;
   
	
	public static void main(String args[]) throws IOException { 
		TimeClient client = new TimeClient("time.nist.gov");
		System.out.println(client.getInfo());
	}
	public TimeClient(String hostName) throws IOException {
		socket = new Socket(hostName, 13);
		input = new DataInputStream(socket.getInputStream());
		input = new DataInputStream(socket.getInputStream());
		input = new DataInputStream(socket.getInputStream());
	}
	public int getInfo() throws IOException {
		//byte[] info = input.readAllBytes();
		int info = input.read();
		return info;
	}
}
