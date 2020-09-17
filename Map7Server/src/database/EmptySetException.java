package database;

/** Gestisce l'eccezione di un result set vuoto */
@SuppressWarnings("serial")
public class EmptySetException extends Exception {

	@SuppressWarnings("javadoc")
	public EmptySetException() { }

	@SuppressWarnings("javadoc")
	public EmptySetException(String arg0) {	super(arg0); }

}
