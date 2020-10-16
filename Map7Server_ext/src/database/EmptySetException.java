package database;

/** Gestisce l'eccezione di un result set vuoto */
@SuppressWarnings("serial")
public class EmptySetException extends Exception {

	public EmptySetException() { }

	public EmptySetException(String arg0) {	super(arg0); }

}
