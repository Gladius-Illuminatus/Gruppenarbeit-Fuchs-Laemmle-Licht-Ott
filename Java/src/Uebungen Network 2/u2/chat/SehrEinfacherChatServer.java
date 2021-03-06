package u2.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class SehrEinfacherChatServer {
	
	ArrayList<PrintWriter> clientAusgabeStroeme;
	
	
	public class ClientHandler implements Runnable {
		
		BufferedReader reader;
		
		Socket sock;
		
		
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
				
			} catch(Exception ex) {ex.printStackTrace();}
		} // Konstruktor schliessen
		
		public void run() {
			String nachricht;
			
			try {
				
				while ((nachricht = reader.readLine()) != null) {
					
					System.out.println("gelesen: " + nachricht);
					esAllenWeitersagen(nachricht);
					
				} // Ende der while-Schleife
			} catch(Exception ex) {ex.printStackTrace();}
		} // run schliessen
	} // innere Klasse schliessen
	
	
	public static void main (String[] args) {
		new SehrEinfacherChatServer().los();
	}
	
	public void los() {
		clientAusgabeStroeme = new ArrayList<PrintWriter>();
		
		try (ServerSocket serverSock = new ServerSocket(5000);){
			
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());         
				clientAusgabeStroeme.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				
				
				System.out.println("habe eine Verbindung");
			}
			// wenn wir hier angelangt sind, haben wir eine Verbindung
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		
	}
	
	public void esAllenWeitersagen(String nachricht) {
		Iterator<PrintWriter> it = clientAusgabeStroeme.iterator();
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(nachricht);
				writer.flush();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} // Ende der while-Schleife

//		Statt einen Iterator und eine while-Schleife zu benutzen,
//		koennten Sie in Java 5 auch wie folgt vorgehen (da wir aus der 
//		ArrayList clientAusgabeStr�me hier eine parametrisierte 
//		ArrayList<PrintWriter> clientAusgabeStr�me gemacht haben):
//		
//		for(PrintWriter writer:clientAusgabeStr�me) {
//			try {
//				writer.println(nachricht);
//				writer.flush();
//			} catch(Exception ex) {
//				ex.printStackTrace();
//			}		
//		} // Ende der for-Schleife
//				
		
	} // esAllenWeitersagen schliessen
	
	
}
