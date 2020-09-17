import java.io.IOException;
import java.util.Scanner;

import server.MultiServer;
/**
 *
 * Main del server
 *
 */
class MainServer {

	/**
	 * Metodo principale del server, rappresenta il punto d'ingresso dell'applicativo
	 * @param args Parametri passati in input al programma
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Regression Tree Learner\n");
		try {
			boolean connectionError; // Flag usato per ripetere il do-while nel caso in cui la porta sia già in uso
			do {
				connectionError = false;
				boolean isNumber;
				int port = 0;
				do {
					try {
						System.out.println("Insert the port on which to create the connection: ");
						isNumber = true;
						port = Integer.parseInt(scanner.nextLine()); // E' stato deciso di leggere in input una stringa. Nel caso in cui l'utente ha inserito una stringa non convertibile in intero, viene scatenata un'eccezione
					} catch (NumberFormatException e) {
						System.err.println(
								"[!] Error [!] The string that you've entered is not a number.");
						isNumber = false;
					}
				} while ( ! isNumber );

				try {
					new MultiServer(port);
				} catch (IOException e) { // La porta è già in uso
					System.err.println(
							"[!] Error [!] Cannot initialize the connection on the port " + port + ". Maybe it's already used. Detail error: " + e);
					System.out.println("Do you want insert a new PORT? ( Y - retry to insert a new port | N - Close the server)");
					String risposta;
					do {
						System.out.println("Y/N");
						risposta = scanner.nextLine().toUpperCase();

						if (risposta.equals("Y")) {
							connectionError = true;
						}
					} while (!risposta.equals("Y")  && !risposta.equals("N"));
				}
			} while (connectionError); // Fin quando c'è un'errore di connessione (porta occupata...) ripeto il ciclo

		} finally {
			scanner.close();
			System.out.println("Closing the server... Goodbye!");
		}
	}
}
