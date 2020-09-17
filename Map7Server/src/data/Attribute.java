package data;

import java.io.Serializable;

/**
 * Classe che modella un attributo discreto o continuo.
 *
 */
@SuppressWarnings("serial")
public abstract class Attribute implements Serializable {

	private String name; // Nome simbolico dell'attributo
	private int index; // Identificativo numerico dell'attributo

	/**
	 * E' il costruttore di classe. Inizializza i valori dei membri name, index
	 *
	 * @param name - Nome dell'attributo
	 * @param index - Indice all'interno del training set
	 */
	public Attribute(String name, int index) {

		this.index = index;
		this.name = name;

	}

	/**
	 * Restituisce il valore nel membro name;
	 *
	 * @return name
	 */
	String getName() {
		return name;
	}

	/**
	 * Restituisce il valore nel membro index;
	 *
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

}
