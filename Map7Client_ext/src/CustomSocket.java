import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomSocket {

    private static Socket socket;
	private static ObjectOutputStream out = null;
	private static ObjectInputStream in = null;

	private CustomSocket(){	}

	public static void initSocket(String ip, Integer port) throws IOException {

		if(socket != null && !socket.isClosed()) return; // Se la socket è già settata 

		InetAddress addr;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			UtilityMethods.printError("Error Dialog", "Generic error", e.toString());
			return;
		}

		socket = new Socket(addr, port.intValue());

		// stream con richieste del client
		setOutputStream(socket.getOutputStream());
		setInputStream(socket.getInputStream());
	}

	public static Socket getIstance() {
		return socket;
	}

	public static ObjectOutputStream getOutputStream() {
		if(socket != null) return out;
		else return null;
	}

	public static void setOutputStream(OutputStream out) {
		try {
			out = new ObjectOutputStream(out);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Error with the output flow", "There has been some errors with the output flow. Detail error: " + e);
		}
	}

	public static ObjectInputStream getInputStream() {
		if(socket != null) return in;
		else return null;
	}

	public static void setInputStream(InputStream in) {
		try {
			in = new ObjectInputStream(in);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Error with the input flow", "There has been some errors with the input flow. Detail error: " + e);
		}
	}
	private static boolean validateIp(String ip) {
		// Formato ip non valido
		return ip.matches(
			"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");		
	}

	private static boolean validatePort(Integer port) {
		return 
			port.toString()
			.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
	}

	static void closeSocketIfOpened() {
		if (socket != null && !socket.isClosed()){
			try {
				socket.close();
				in.close();
				out.close();
			} catch (IOException e) {
				UtilityMethods.printError("Error Dialog", "Socket error", "Socket has not been closed correctly. Detail error: " + e);
			}
		}
	}

	static void closeSocketIfOpened(Socket socket) {
		if (socket != null && !socket.isClosed()){
			try {
				socket.close();
				in.close();
				out.close();
			} catch (IOException e) {
				UtilityMethods.printError("Error Dialog", "Socket error", "Socket has not been closed correctly. Detail error: " + e);
			}
		}
	}

	static boolean validateSettings(String ip,Integer port) {

		if (!ip.isEmpty() && !port.toString().isEmpty()) {
			if(!validateIp(ip)){
				UtilityMethods.printError("Error Dialog", "There's some error with the IP...",
				"The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");	
				return false;
			}
			if(!validatePort(new Integer(port))){
				UtilityMethods.printError("Error Dialog", "There's some error with the port...",
				"The port that you've entered isn't correct. Please, start again the program and insert a valid port (value between 1 and 65535)");
				return false;
			}
		} else { // Numero parametri insufficiente
			UtilityMethods.printError("Error Dialog", "Settings doesn't match the right format...",
					"Please review your settings, there's some errors within it.");
			return false;
		}
		return true;
    }

	public static boolean tryConnection(String ipAddress,Integer port) {

		InetAddress addr;
		try {
			addr = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			UtilityMethods.printError("Error Dialog", "Generic error", "Cannot determine the IP Address of the host. Detail error: " + e);
			return false;
		}
		Socket testConnection = null;
		try {
			testConnection = new Socket(addr, port);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
			"Cannot initialize the connection with the server. Make sure that the server is on and the port is correct!");
			return false; 
		} finally {
			closeSocketIfOpened(testConnection);
		}
		return true;
	}
    
}
