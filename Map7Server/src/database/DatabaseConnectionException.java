package database;

/** Gestisce le eccezioni scatenate quando la connessione al database fallisce */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {

	@SuppressWarnings("javadoc")
	public DatabaseConnectionException() {}

	@SuppressWarnings("javadoc")
	public DatabaseConnectionException(String message) { super(message); }

}
