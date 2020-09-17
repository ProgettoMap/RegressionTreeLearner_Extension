package map7Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;
/**
 * 
 * Main del client
 *
 */
public class MainClient {

	public static void main(String[] args) {

		// Validazione parametri in input
		if(args.length == 2) {
			if(!args[0].matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) { // Formato ip non valido
				System.err.println("[!] Error [!] The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");
				return;
			}
			if(!args[1].matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) { // Formato porta non valido
				System.err.println("[!] Error [!] The port that you've entered isn't correct. Please, start again the program and insert a valid port (value between 1 and 65535).");
				return;
			}
		} else { // Numero parametri insufficiente
			System.err.println("[!] Error [!] You haven't entered the necessary parameters for starting correctly the Regression Tree Learner.\nMind that you have to launch the program in the following mode: RegressionTreeClient.exe <ip address> <port>");
			return;
		}

		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			return;
		}
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			System.out.println("Regression Tree Learner\n\n");
			System.out.println("Trying to connect to the server " + addr + "...");
			socket = new Socket(addr, new Integer(args[1]).intValue());
			System.out.println(socket);

			// stream con richieste del client
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("[!] Error [!] Cannot initialize the connection with the server. Detail error: " + e.toString());
			return;
		}

		String answer = "";

		int decision = 0;
		do {
			System.out.println(" - Learn Regression Tree from data [1]");
			System.out.println(" - Load Regression Tree from archive [2]");
			decision = Keyboard.readInt();
		} while (!(decision == 1) && !(decision == 2));


		System.out.println("Table name:");
		String tableName = Keyboard.readString();
		try {
			if (decision == 1) { // Learn regression tree
				System.out.println("Starting data acquisition phase!");

				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					System.err.println(answer); // C'è stato qualche errore
					return;
				}
				System.out.println("Starting learning phase!");
				out.writeObject(1);

			} else { // Load tree from archive
				out.writeObject(2);
				out.writeObject(tableName);
			}

			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				System.out.println(answer); // Reading rules
			}

			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				System.out.println(answer); // Reading tree
			}

			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.err.println(answer);
				return;
			}

			char risp = 'y';
			do {
				out.writeObject(3);
				System.out.println("Starting prediction phase!");
				answer = in.readObject().toString(); // Reading tree.predictClass()

				while (answer.equals("QUERY")) {
					// Formulating query, reading answer
					answer = in.readObject().toString(); // Read trees
					System.out.println(answer);
					int path = Keyboard.readInt();
					out.writeObject(path);
					answer = in.readObject().toString();
				}

				if (answer.equals("OK")) { // Reading prediction
					answer = in.readObject().toString();
					System.out.println("Predicted class:" + answer);

				} else // Printing error message
					System.err.println(answer);

				System.out.println("Would you repeat ? (y/n)");
				risp = Keyboard.readChar();

			} while (Character.toUpperCase(risp) == 'Y');

			// Aggiunta stampa per far capire al server che l'esecuzione del client vuole terminare
			out.writeObject(0);
			System.out.println("Thank you for having used this Regression Tree Learner! See you soon");

		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.toString());
		} finally {
			try {
				socket.close();
			} catch (IOException e1) {
				System.err.println("[!] Error [!] Socket has not been closed correctly.");
			}
		}
	}

}
