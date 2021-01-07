package u4.webserver;

/* JavaWebServerAdvanced.java */

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * Ein ganz einfacher Webserver auf TCP und einem
 * beliebigen Port (default Port 80). Der Server ist in der Lage,
 * Seitenanforderungen lokal zu dem Verzeichnis,
 * aus dem er gestartet wurde, zu bearbeiten. Wurde
 * der Server z.B. im Verzeichnis c:\tmp gestartet, so
 * würde eine Seitenanforderung
 * http://localhost:80/test/index.html die Datei
 * c:\tmp\test\index.html laden. CGIs, SSIs, Servlets
 * oder ähnliches wird nicht unterstützt.
 * <p>
 * Die Dateitypen .htm, .html, .gif, .jpg und .jpeg werden
 * erkannt und mit korrekten MIME-Headern übertragen, alle
 * anderen Dateien werden als "application/octet-stream"
 * übertragen. Jeder Request wird durch einen eigenen
 * Client-Thread bearbeitet, nach Übertragung der Antwort
 * schliesst der Server den Socket. Antworten werden mit
 * HTTP/1.0-Header gesendet.
 */
public class JavaWebServerAdvanced
{
	public static void main(String[] args) {

		int port = 80;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		} 
		
		try (ServerSocket httpd = new ServerSocket(port)) {

			System.out.println("Listening to port " + port);
			int calls = 0;

			while (true) {
				Socket socket = httpd.accept();
				(new BrowserClientThread(++calls, socket)).start();
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
	}
}

/**
 * Die Thread-Klasse für die Client-Verbindung.
 */
class BrowserClientThread extends Thread {
	static final String[][] mimetypes = {
			{"html", "text/html"},
			{"htm",  "text/html"},
			{"txt",  "text/plain"},
			{"gif",  "image/gif"},
			{"jpg",  "image/jpeg"},
			{"jpeg", "image/jpeg"},
			{"jnlp", "application/x-java-jnlp-file"},
			{"css",  "text/css"},
			{"svg",  "image/svg+xml"},
			{"js",   "text/javascript"},
			{"ico",  "image/x-icon"}

	};

	private Socket       socket;
	private int          id;
	private String       cmd;
	private String       url;
	@SuppressWarnings("unused")
	private String       httpversion;
	private String basicFilePath = "src/u4/webserver/files2/";
	private String defaultFile = "index.html";

	/**
	 * Erzeugt einen neuen Client-Thread mit der angegebenen
	 * id und dem angegebenen Socket.
	 */
	public BrowserClientThread(int id, Socket socket) {
		this.id     = id;
		this.socket = socket;
	}

	/**
	 * Hauptschleife für den Thread.
	 */
	public void run()
	{
		try {
			System.out.println(id + ": Incoming call...");
			InputStream  in = socket.getInputStream();
			readRequest(in);
			createResponse(new PrintStream(socket.getOutputStream()));
			socket.close();
			System.out.println(id + ": Closed.");
		} catch (IOException e) {
			System.out.println(id + ": " + e.toString());
			System.out.println(id + ": Aborted.");
		}
	}

	/**
	 * Liest den nächsten HTTP-Request vom Browser ein.
	 */
	private void readRequest(InputStream  in) throws IOException {
		//Request-Zeilen lesen
		Vector<StringBuffer> request = new Vector<StringBuffer>(20);
		StringBuffer sb = new StringBuffer(100);
		int c;
		while ((c = in.read()) != -1) {
			if (c == '\r') {
				//ignore
			} else if (c == '\n') { //line terminator
				if (sb.length() <= 0) {
					break;
				} else {
					request.addElement(sb);
					sb = new StringBuffer(100);
				}
			} else {
				sb.append((char)c);
			}
		}
		//Request-Zeilen auf der Konsole ausgeben
		Enumeration<StringBuffer> e = request.elements();
		while (e.hasMoreElements()) {
			sb = e.nextElement();
			System.out.println("< " + sb.toString());
		}
		//Kommando, URL und HTTP-Version extrahieren
		String s = request.elementAt(0).toString();
		cmd = "";
		url = "";
		httpversion = "";
		int pos = s.indexOf(' ');
		if (pos != -1) {
			cmd = s.substring(0, pos).toUpperCase();
			s = s.substring(pos + 1);
			//URL
			pos = s.indexOf(' ');
			if (pos != -1) {
				url = s.substring(0, pos);
				s = s.substring(pos + 1);
				//HTTP-Version
				pos = s.indexOf('\r');
				if (pos != -1) {
					httpversion = s.substring(0, pos);
				} else {
					httpversion = s;
				}
			} else {
				url = s;
			}
		}
	}

	/**
	 * Request bearbeiten und Antwort erzeugen.
	 */
	private void createResponse(PrintStream out) {
		if (cmd.equals("GET") || cmd.equals("HEAD")) {
			if (!url.startsWith("/")) {
				httpError(out, 400, "Bad Request");
			} else {
				//MIME-Typ aus Dateierweiterung bestimmen
				String mimestring = "application/octet-stream";
				for (int i = 0; i < mimetypes.length; ++i) {
					if (url.endsWith(mimetypes[i][0])) {
						mimestring = mimetypes[i][1];
						break;
					}
				}
				//URL in lokalen Dateinamen konvertieren
				String fsep = System.getProperty("file.separator", "/");
				StringBuffer sb = new StringBuffer(url.length());
				for (int i = 1; i < url.length(); ++i) {
					char c = url.charAt(i);
					if (c == '/') {
						sb.append(fsep);
					} else {
						sb.append(c);
					}
				}

				// set default index.html if necessary
				String fileName = basicFilePath + defaultFile;
				if ( sb.toString().length() > 0 ) {
					fileName = basicFilePath + sb.toString();
				} else {
					mimestring= "text/html";
				}

				try {
					FileInputStream is = new FileInputStream(fileName);
					//HTTP-Header senden
					out.print("HTTP/1.0 200 OK\r\n");
					System.out.println("> HTTP/1.0 200 OK");
					out.print("Server: JavaWebServerAdvanced 0.5\r\n");
					System.out.println(
							"> Server: JavaWebServerAdvanced 0.5"
							);
					out.print("Content-type: " + mimestring + "\r\n\r\n");
					System.out.println("> Content-type: " + mimestring);
					if (cmd.equals("GET")) {
						//Dateiinhalt senden
						byte[] buf = new byte[256];
						int len;
						while ((len = is.read(buf)) != -1) {
							out.write(buf, 0, len);
						}
					}
					is.close();
				} catch (FileNotFoundException e) {
					httpError(out, 404, "Error Reading File");
				} catch (IOException e) {
					httpError(out, 404, "Not Found");
				} catch (Exception e) {
					httpError(out, 404, "Unknown exception");
				}
			}
		} else {
			httpError(out, 501, "Not implemented");
		}
	}

	/**
	 * Eine Fehlerseite an den Browser senden.
	 */
	private void httpError(PrintStream out, int code, String description)
	{
		System.err.println("> ***" + code + ": " + description + "***");
		out.print("HTTP/1.0 " + code + " " + description + "\r\n");
		out.print("Content-type: text/html\r\n\r\n");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>JavaWebServerAdvanced-Error</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>HTTP/1.0 " + code + "</h1>");
		out.println("<h3>" + description + "</h3>");
		out.println("</body>");
		out.println("</html>");
	}
}