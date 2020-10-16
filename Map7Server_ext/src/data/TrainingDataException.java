package data;


/**
 * Gestisce le eccezioni causate da acquisizione errata dei dati.
 *
 * Eccezioni gestite:
 * <ul>
 * 	<li> File inesistente </li>
 *  <li> Schema mancante </li>
 *  <li> Training set vuoto </li>
 	<li> Training set privo di variabile target numerica </li>
 * </ul>
 */
@SuppressWarnings("serial")
public class TrainingDataException extends Exception {

	public TrainingDataException() { }

	public TrainingDataException(String message) { super(message); }

}
