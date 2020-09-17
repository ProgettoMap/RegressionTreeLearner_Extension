package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** Classe che inizializza la connessione del server con ogni client */
public class MultiServer {

	private int PORT = 8080;
	private ServerSocket srvSck;

	/**
	 * Contatore delle connessioni su server
	 */
	static int connectionCount = 0;

	/**
	 * @param port Porta sulla quale inizializzare la connessione del server
	 * @throws IOException Viene lanciata un'eccezione di tipo IOException se la porta è già stata utilizzata
	 */
	public MultiServer(int port) throws IOException {
		if (port != PORT) { // La assegno solo se differente dalla default
			this.PORT = port;
		}
		// Costruttore di classe. Inizializza la porta ed invoca run()
		run();
	}

	/*
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di
	 * richiesta di connessioni da parte del client.
	 *
	 * Ad ogni nuova richiesta connessione si istanzia ServerOneClient.
	 */
	private void run() throws IOException {

		srvSck = new ServerSocket(PORT);

		try {
			while (true) {
				if (connectionCount == 0) {
					System.out.println("Waiting for a new connection...");
				}
				Socket socket = srvSck.accept();
				// Client connesso
				System.out.println(
						"\n New client has started the connection. Clients actually connected: " + ++connectionCount + "\n");

				try {
					new ServerOneClient(socket);
				} catch (IOException e) {
					// Se prima dell'esecuzione del run viene scatenata un'eccezione, verrà gestita
					// dal seguente blocco
					socket.close();
					System.out.println(
							"A client has terminated the connection. Clients actually connected: " + --connectionCount  + "\n\n");
				}
			}
		} finally {
			srvSck.close();
		}
	}

}
