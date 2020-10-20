import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Classe che rappresenta l'entità Socket.
 *  Contiene i metodi necessari per la scrittura, lettura, convalidazione di varie proprietà
 */
class CustomSocket {

	private static Socket socket;
	private static ObjectOutputStream out = null;
	private static ObjectInputStream in = null;


	/**
	 * Metodo che inizializza la socket 
	 * 
	 * @param ip Indirizzo IP dell'host con la quale si vuole creare la comunicazione
	 * @param port Porta dell'host sulla quale è avviato il servizio
	 * 
	 * 
	 * @throws IOException Eccezione lanciata quando si verifica un problema con l'output stream
	 */
	static void initSocket(String ip, Integer port) throws IOException {

		if (socket != null && !socket.isClosed())
			return; // Se la socket è già settata

		InetAddress addr;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			UtilityMethods.printError("Error Dialog", "Generic error",
					"Error. The IP address of the host could not be determined. Detail error: " + e.toString());
			return;
		}

		socket = new Socket(addr, port.intValue());

		// Inizializza gli stream di input e output relativi alla socket
		setOutputStream(socket.getOutputStream());
		setInputStream(socket.getInputStream());
	}
	/**
	 * Metodo che restituisce la generica istanza della socket
	 *
	 * @return socket Istanza della classe CustomSocket
	 */
	static Socket getIstance() {
		return socket;
	}
	/**
	 * Metodo che restituisce il flusso in output della socket
	 *
	 * @return ObjectOutputStream Restituisce la socket o null  
	 */
	static ObjectOutputStream getOutputStream() {
		return (socket != null) ? out : null;
	}
	/**
	 * Metodo che setta il flusso di Output della socket
	 *
	 * @param out flusso della socket 
	 */
	private static void setOutputStream(OutputStream out) {
		try {
			CustomSocket.out = new ObjectOutputStream(out);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Error with the output flow",
					"There has been some errors with the output flow. Detail error: " + e);
		}
	}
	/**
	 * Metodo che restituisce il flusso in input della socket
	 *
	 * @return in Valore della InputStream
	 */
	static ObjectInputStream getInputStream() {
		return (socket != null) ? in : null;
	}
	/**
	 * Metodo che setta il flusso di Input della socket
	 * 
	 * @param in Flusso di input della socket
	 */
	private static void setInputStream(InputStream in) {
		try {
			CustomSocket.in = new ObjectInputStream(in);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Error with the input flow",
					"There has been some errors with the input flow. Detail error: " + e);
		}
	}
	/**
	 * Metodo che verifica la corretta validità di una dato indirizzo ip
	 * es. (127.0.0.7)
	 * @param ip Indirizzo IP dell'host con la quale si vuole creare la
	 *           comunicazione
	 * @return Valore booleano che vale true se l'indirizzo ip rispetta il matches, false altrimenti
	 *
	 */
	private static boolean validateIp(String ip) {
		return ip.matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	}
	/**
	 * Metodo che verifica la corretta validità di una data porta
	 * es. (8107)
	 * @param port Porta dell'host sulla quale è avviato il servizio
	 * @return Valore booleano che vale true se la porta rispetta il matches, false altrimenti
	 * 
	 */
	private static boolean validatePort(Integer port) {
		return port.toString()
				.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
	}
	/**
	 * Metodo che chiude il flusso della socket se è stata già  inizializzata
	 * 
	 * @param socket Canale di comunicazione con il server
	 */
	static void closeSocketIfOpened(Socket socket) {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
				in.close();
				out.close();
			} catch (IOException e) {
				UtilityMethods.printError("Error Dialog", "Socket error",
						"Socket has not been closed correctly. Detail error: " + e);
			}
		}
	}
	/**
	 * Metodo che verifica la validità dell'indirizzo ip e della porta 
	 * 
	 * @param ip Indirizzo IP dell'host con la quale si vuole creare la
	 *                  comunicazione
	 * @param port      Porta dell'host sulla quale è avviato il servizio
	 * 
	 * @return Valore booleano che vale true se l'indirizzo ip e la porta sono validati,falso altrimenti
	 
	 */
	
	static boolean validateSettings(String ip, Integer port) {

		if (!ip.isEmpty() && !port.toString().isEmpty()) {
			if (!validateIp(ip)) {
				UtilityMethods.printError("Error Dialog", "There's some error with the IP...",
						"The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");
				return false;
			}
			if (!validatePort(new Integer(port))) {
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

	/**
	 * Metodo che tenta di stabilire una connessione con i parametri passati in
	 * input, per capire se è possibile comunicare con l'host o meno
	 * 
	 * @param ipAddress Indirizzo IP dell'host con la quale si vuole creare la
	 *                  comunicazione
	 * @param port      Porta dell'host sulla quale è avviato il servizio
	 * 
	 * @return Valore booleano che indica la possibilità di effettuare la
	 *         comunicazione
	 */
	static boolean tryConnection(String ipAddress, Integer port) {
		boolean result = true;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			UtilityMethods.printError("Error Dialog", "Generic error",
					"Cannot determine the IP Address of the host. Detail error: " + e);
			result = false;
		}
		Socket testConnection = null;
		try {
			testConnection = new Socket(addr, port);
			
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Make sure that the server is on and the port is correct!");
			result = false;
		} finally {
			closeSocketIfOpened(testConnection);
		}
		return result;
	}
	/**
	 * Metodo che permette di riavviare la socket
	 */
	static void restartSocket() {
		CustomSocket.closeSocketIfOpened(socket);
		ArrayList<String> settings = ConnectionController.readSettingsFromFile();
		String ip = settings.get(ConnectionController.IP_POSITION_IN_SETTINGS);
		Integer port = new Integer(settings.get(ConnectionController.PORT_POSITION_IN_SETTINGS));
		try {
			CustomSocket.initSocket(ip, port);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Make sure that the server is on and the port is correct!");
		}
	}

}
