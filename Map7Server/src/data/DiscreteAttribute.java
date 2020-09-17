package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe che modella un attributo discreto
 */
//NOTE: Attributi nominali (es. A, B, C...)
@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable {

	// Rappresenta l'insieme di valori discreti che l'attributo può assumere
	private Set<String> values = new TreeSet<String>(); // order by asc

	/**
	 * Invoca il costruttore della super-classe e avvalora l'array values[] con i
	 * valori discreti in input.
	 *
	 * @param name   Nome simbolico dell'attributo
	 * @param index  Indice dell'attributo
	 * @param values Valori discreti
	 */
	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 *
	 * @return Cardinalita' dell'array values (numero di valori discreti)
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}

	/**
	 * Metodo che prende in input un indice i di un solo valore discreto
	 *
	 * @param i - indice della stringa che si vuole prendere dalla collezione
	 * @return i-esimo valore discreto dell'array values[]
	 */
	String getValue(int i) {

		Iterator<String> iter = iterator();

		int j = 0;
		while (j < i) {
			// Non è stato inserito il controllo per verificare se c'è un elemento
			// successivo poichè non crediamo sia possibile
			// modificare la signature del metodo.
			iter.next();
			j++;
		}
		return iter.next();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}
