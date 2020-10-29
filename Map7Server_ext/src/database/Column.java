package database;


/**
 *
 * Classe che modella una tabella del database
 *
 */
public class Column {

	private String name;
	private String type;

	/**
	 * Istanzia un nuovo oggetto di classe Column
	 *
	 * @param name Nome della colonna
	 * @param type Tipo della colonna
	 */
	Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Restituisce il nome della colonna della tabella del database
	 * @return Nome della colonna della tabella del database
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 * Restituisce un valore booleano che indica se la colonna è di tipo numerico o meno
	 * @return Vero se il tipo della colonna è NUMERICO, falso altrimenti
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
	 * Restituisce un valore booleano che indica se la colonna è di tipo stringa o meno
	 * @return Vero se il tipo della colonna è STRINGA, falso altrimenti
	 */
	public boolean isString() {
		return type.equals("string");
	}

	@Override
	public String toString() {
		return name + ":" + type;
	}
}
