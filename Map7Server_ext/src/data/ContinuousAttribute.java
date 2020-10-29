package data;


/**
 * Classe che modella un attributo continuo.
 * Estende la classe Atribute
 *
 */
//NOTE: Attributi numerici (es. 1,3,5...)
@SuppressWarnings("serial")
public class ContinuousAttribute extends Attribute {

	/**
	 * Invoca il costruttore della super-classe
	 *
	 * @param name  Nome dell'attributo
	 * @param index Indice dell'attributo
	 */
	public ContinuousAttribute(String name, int index) {
		super(name, index);
	}


}
