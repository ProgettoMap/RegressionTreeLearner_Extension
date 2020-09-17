package data;

import java.io.Serializable;

/**
 * Classe che modella un attributo continuo.
 * Estende la classe Atribute
 *
 */
//NOTE: Attributi numerici (es. 1,3,5...)
@SuppressWarnings("serial")
public class ContinuousAttribute extends Attribute implements Serializable {

	/**
	 * Invoca il costruttore della super-classe
	 *
	 * @param name  Nome dell'attributo
	 * @param index Indice dell'attributo
	 */
	public ContinuousAttribute(String name, int index) {
		super(name, index);
	}

	@Override
	public String toString() {
		return getName();
	}

}
