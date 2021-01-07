package u3.basicwebserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JavaWebServer
{

	private static final int fNumberOfThreads = 100;
	private static final Executor fThreadPool = Executors.newFixedThreadPool(fNumberOfThreads);

	public static void main(String[] args) throws IOException {
		try (ServerSocket socket = new ServerSocket(80)) {
            System.out.println("open browser and enter url: " +" http://localhost");
			while (true) {
				final Socket connection = socket.accept();
				Runnable task = new Runnable() 	{
					@Override
					public void run() {
						HandleRequest(connection);
					}
				};
				fThreadPool.execute(task);
			}
		} 
	}
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	private static void HandleRequest(Socket s) {
		BufferedReader in;
		PrintWriter out;
		String request;

		try  {
			String webServerAddress = s.getInetAddress().toString();
			System.out.println("New Connection:" + webServerAddress);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			request = in.readLine();
			System.out.println("--- Client request: " + request);

			out = new PrintWriter(s.getOutputStream(), true);
			out.println("HTTP/1.0 200");
			out.println("Content-type: text/html");
			out.println("Server-name: myJavaServer");
			String response = "<html>\n"
					+ "<head>\n"
					+ "<title>My Java Web Server</title></head>\n"
					+ "<h1>Welcome to my Java Web Server!</h1>\n"
					+ "<p>Server Time: " +  getCurrentTimeStamp() + "</p>\n"
					+ "</html>\n";
			out.println("Content-length: " + response.length());
			out.println("");
			out.println(response);
			out.flush();
			out.close();
			s.close();
		}
		catch (IOException e) {
			System.out.println("Failed respond to client request: " + e.getMessage());
		}
		finally {
			if (s != null) 	{
				try {
					s.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

}