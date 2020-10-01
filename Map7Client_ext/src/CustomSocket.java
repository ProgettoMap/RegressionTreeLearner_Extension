import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomSocket {

    private static Socket socket;
	private static ObjectOutputStream out = null;
	private static ObjectInputStream in = null;

	private CustomSocket(){	}

	public static void initSocket(String ip, Integer port) {

		//TODO: rivedere, non è un singleton
		if(socket != null) return; // Se la socket è già settata 

		InetAddress addr;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			UtilityMethods.printError("Error Dialog", "Generic error", e.toString());
			return;
		}

		try {
			socket = new Socket(addr, port.intValue());

			// stream con richieste del client
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e.toString());
			try {
				closeSocketIfOpened();
			} catch (IOException e1) {
				UtilityMethods.printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
			}
			return;
		}

	}

	public static Socket getIstance() {
		return socket;
	}

	public static ObjectOutputStream getOutputStream() {
		if(socket != null) return out;
		else return null;
	}
	public static ObjectInputStream getInputStream() {
		if(socket != null) return in;
		else return null;
	}

	static void closeSocketIfOpened() throws IOException {
		if (socket != null && !socket.isClosed()){
			socket.close(); 
			in.close();
			out.close();
		}
		
    }
    
}
