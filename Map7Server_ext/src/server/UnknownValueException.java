package server;

/** Gestisce il caso di acquisizione di valore mancante o fuori range di un attributo di un nuovo esempio da classificare */
@SuppressWarnings("serial")
public class UnknownValueException extends Exception {

	public UnknownValueException() {
	}

	public UnknownValueException(String arg0) {
		super(arg0);
	}

}
