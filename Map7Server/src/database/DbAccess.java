 package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * Classe che modella l'accesso al database
 *
 */
public class DbAccess {

	private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; // (Per utilizzare questo Driver scaricare e
																			// aggiungere al classpath il connettore
																			// mysql connector)
	private final String DBMS = "jdbc:mysql";
	private String SERVER = "localhost"; // contiene l’identificativo del server su cui risiede la base di dati (per
											// esempio localhost)
	private String DATABASE = "MapDB"; // contiene il nome della base di dati
	private final int PORT = 3306; // La porta su cui il DBMS MySQL accetta le connessioni
	private String USER_ID = "MapUser"; // contiene il nome dell’utente per l’accesso alla base di dati
	private String PASSWORD = "map"; // contiene la password di autenticazione per l’utente identificato da USER_ID
	private Connection conn; // gestisce una connessione

	/**
	 * Costruttore di classe
	 */
	public DbAccess() {
		// Empty constructor
	}

	/**
	 * Inizializza la connessione con il database
	 * @throws DatabaseConnectionException Eccezione scatenata nel caso in cui non è possibile stabilire la connessione con il database
	 */
	public void initConnection() throws DatabaseConnectionException {
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("[!] Driver not found: " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(InstantiationException e){
			System.out.println("[!] Error during the instantiation : " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(IllegalAccessException e){
			System.out.println("[!] Cannot access the driver : " + e.getMessage());
			throw new DatabaseConnectionException();
		}
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
					+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

		try {
			conn = DriverManager.getConnection(connectionString);
		} catch(SQLException e) {
			System.out.println("[!] SQLException: " + e.getMessage());
			System.out.println("[!] SQLState: " + e.getSQLState());
			System.out.println("[!] VendorError: " + e.getErrorCode());
			throw new DatabaseConnectionException();
		}
	}

	/**
	 * Restituisce l'oggetto di tipo Connection
	 * @return oggetto di tipo Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Chiude la connessione al database
	 * @throws SQLException Eccezione scatenata nel caso in cui non sia possibile comunicare con il database, o nel caso in cui la query di chiusura fallisca.
	 */
	public void closeConnection() throws SQLException{
		if(!conn.isClosed())
			conn.close();
	}

}
