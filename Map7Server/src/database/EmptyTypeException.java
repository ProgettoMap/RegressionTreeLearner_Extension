package database;

/** Gestisce l'eccezione di tipo vuoto */
@SuppressWarnings("serial")
class EmptyTypeException extends Exception {

	@SuppressWarnings("javadoc")
	EmptyTypeException() { }

	@SuppressWarnings("javadoc")
	EmptyTypeException(String arg0) { super(arg0); }

}
