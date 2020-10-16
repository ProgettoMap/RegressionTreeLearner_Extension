package database;

/** Gestisce l'eccezione di tipo vuoto */
@SuppressWarnings("serial")
class EmptyTypeException extends Exception {

	EmptyTypeException() { }

	EmptyTypeException(String arg0) { super(arg0); }

}
