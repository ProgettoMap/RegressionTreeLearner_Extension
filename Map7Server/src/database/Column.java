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
	 * @param name Nome della colonna
	 * @param type Tipo della colonna
	 */
	public Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 *
	 * @return Nome della colonna della tabella del database
	 */
	public String getColumnName() {
		return name;
	}

	/**
	 *
	 * @return Vero se il tipo della colonna è NUMERICO, falso altrimenti
	 */
	public boolean isNumber() {
		return type.equals("number");
	}

	/**
	 *
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