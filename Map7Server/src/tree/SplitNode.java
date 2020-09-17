package tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.Data;

/** Classe che modella l'entità nodo non fogliare */
@SuppressWarnings("serial")
abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {

	// NOTE: uno splitNode ha più splitInfo

	private Attribute attribute; // oggetto Attribute che modella l'attributo indipendente sul quale lo split è
	// generato
	/**
	 * Array per memorizzare gli split candidati in una struttura dati di dimensione
	 */
	List<SplitInfo> mapSplit = new ArrayList<SplitInfo>();
	// pari ai possibili valori di test
	private double splitVariance; // attributo che contiene il valore di varianza a seguito del partizionamento
	// indotto dallo split corrente

	/**
	 * Invoca il costruttore della superclasse
	 *
	 * Ordina i valori dell'attributo di input per gli esempi nel range
	 * beginExampleIndex - endExampleIndex e sfrutta questo ordinamento per
	 * determinare i possibili split e popolare l'array mapSplit[].
	 *
	 * Computa lo SSE (splitVariance) per l'attributo usato nello split sulla base
	 * del partizionamento indotto dallo split (lo stesso è la somma degli SSE
	 * calcolati su ciascuno SplitInfo collezionati in mapSplit)
	 *
	 *
	 * @param trainingSet - oggetto di classe Data contenente il training set
	 *             completo
	 * @param beginExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @param endExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @param attribute - Attributo indipendente sul quale si definisce lo split
	 *
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {

		super(trainingSet, beginExampleIndex, endExampleIndex);
		trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute
		setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);

		// compute variance

		splitVariance = 0;
		for (int i = 0; i < mapSplit.size(); i++) {

			double localVariance = new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(),
					mapSplit.get(i).getEndIndex()).getVariance();
			splitVariance += (localVariance);

		}

		this.attribute = attribute;

	}

	/**
	 * Metodo abstract per generare le informazioni necessarie per ciascuno degli
	 * split candidati (in mapSplit[])
	 *
	 * @param trainingSet - oggetti di esempio
	 * @param beginExampleIndex - indici estremi del sotto-insieme di
	 *                  training
	 * @param endExampleIndex - indici estremi del sotto-insieme di
	 *                  training
	 * @param attribute - attributo indipendente sul quale si definisce lo
	 *                  split
	 */
	abstract void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute);

	/**
	 * (Implementazione da class abstract) effettua il confronto del valore in input
	 * rispetto al valore contenuto nell’attributo splitValue di ciascuno degli
	 * oggetti SplitInfo collezionati in mapSplit[].
	 *
	 * Restituisce l'identificativo dello split (indice della posizione nell’array
	 * mapSplit) con cui il test è positivo
	 *
	 * @param value - Valore discreto dell'attributo che si vuole testare
	 *               rispetto a tutti gli split
	 * @return int - Identificativo del ramo di split
	 */
	abstract int testCondition(Object value);

	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split
	 *
	 * @return Attribute - Oggetto per l'attributo usato per lo split
	 */
	Attribute getAttribute() {
		return attribute;
	}

	/**
	 * Restituisce l'information gain per lo split corrente
	 *
	 * @return double splitVariance - attributo che contiene il valore di varianza a
	 *         seguito del partizionamento
	 */

	@Override
	double getVariance() {
		return splitVariance;
	}

	/**
	 * Restituisce il numero dei rami originanti nel nodo corrente
	 *
	 * @return int - numeri di figli di un nodo di split
	 */
	@Override
	int getNumberOfChildren() {
		return mapSplit.size();
	}

	/**
	 * Restituisce le informazioni per il ramo in mapSplit[] indicizzato da child.
	 *
	 * @param child - numero di figlio di un ramo di Split
	 * @return SplitInfo - oggetto contenente le informazioni relative al nodo di
	 *         tipo SplitNode
	 */
	SplitInfo getSplitInfo(int child) {
		return mapSplit.get(child);
	}

	/**
	 * Concatena le informazioni di ciascun test (attributo, operatore e valore) in
	 * una String finale.
	 *
	 * Necessario per la predizione di nuovi esempi
	 *
	 * @return String - informazioni di ciascun test
	 */
	String formulateQuery() {

		String query = "";
		for (int i = 0; i < mapSplit.size(); i++)
			query += (i + ":" + attribute + mapSplit.get(i).getComparator() + mapSplit.get(i).getSplitValue()) + "\n";
		return query;
	}

	/**
	 * Concatena le informazioni di ciascun test (attributo, esempi coperti,
	 * varianza, varianza di Split) in una String finale.
	 *
	 * @return String - Informazioni concatenate (attributo, esempi coperti,
	 *         varianza, varianza di Split) di ciascun test
	 */
	@Override
	public String toString() {

		String v = "SPLIT : attribute=" + attribute + " Nodo: " + super.toString() + " Split Variance: " + getVariance()
				+ "\n";

		for (int i = 0; i < mapSplit.size(); i++) {
			v += "\t" + mapSplit.get(i) + "\n";
		}

		return v;
	}

	@Override
	public int compareTo(SplitNode o) {
		return ((Double) this.getVariance()).compareTo(o.getVariance());
	}

	/*
	 * Esempi di splitnode: X1, X2...
	 */
	/*
	 * NOTE: E' la descrizione della variabile indipendente (es. X1, X2)
	 *
	 * Attributo = variabile (dipendente / indipendente)
	 *
	 * Es. Descrizione: motor | Valori: A,B,C,D,E Descrizione: screw | Valori:
	 * A,B,C,D,E Descrizione: pgain | Valori: 3,4,5,6 Descrizione: vgain | Valori:
	 * 1,2,3,4,5
	 */
	/**
	 *
	 * Classe che aggrega tutte le informazioni riguardanti un nodo di split
	 *
	 */
	class SplitInfo implements Serializable {

		private Object splitValue; // valore di tipo Object (di un attributo indipendente) che definisce uno split
		// NOTE: valore descrittivo del nodo (Stringa,
		private int beginIndex; // indice che identifica il sotto-insieme di
		// training coperto dal nodo corrente
		private int endIndex; // indice che identifica il sotto-insieme di
		// training coperto dal nodo corrente
		private int numberChild; // numero di split (nodi figli) originanti dal nodo corrente
		private String comparator = "="; // operatore matematico che definisce il test nel nodo
		// corrente ("=" per valori discreti)

		/**
		 * Costruttore che avvalora gli attributi di classe per split a valori discreti
		 *
		 * @param splitValue - valore di tipo Object (di un attributo
		 *               indipendente) che definisce uno split
		 * @param beginIndex -indice che identifica il sotto-insieme di training
		 *               coperto dal nodo corrente
		 * @param endIndex - indice che identifica il sotto-insieme di training
		 *               coperto dal nodo corrente
		 * @param numberChild - numero di split (nodi figli) originanti dal nodo
		 *               corrente
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {

			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
		}

		/**
		 * Costruttore che avvalora gli attributi di classe per generici split (da usare
		 * per valori continui)
		 *
		 * @param splitValue - valore di tipo Object (di un attributo
		 *               indipendente) che definisce uno split
		 * @param beginIndex -indice che identifica il sotto-insieme di training
		 *               coperto dal nodo corrente
		 * @param endIndex - indice che identifica il sotto-insieme di training
		 *               coperto dal nodo corrente
		 * @param numberChild - numero di split (nodi figli) originanti dal
		 *               nodocorrente
		 * @param comparator - operatore matematico che definisce il test nel
		 *               nodo corrente ("=" per valori discreti)
		 */
		SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {

			this.splitValue = splitValue;
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			this.numberChild = numberChild;
			this.comparator = comparator;

		}

		/**
		 *
		 * @return int getBegin - indice che identifica l'inizio sotto-insieme di
		 *         training coperto dal nodo corrente
		 */
		int getBeginindex() {
			return beginIndex;
		}

		/**
		 * @return int endIndex - indice che identifica la fine del sotto-insieme di
		 *         training coperto dal nodo corrente
		 */
		int getEndIndex() {
			return endIndex;
		}

		/**
		 * @return Object - Restituisce il valore dello split
		 */
		Object getSplitValue() {
			return splitValue;
		}

		/**
		 * Concatena in un oggetto String i valori di beginExampleIndex,
		 * endExampleIndex, child, splitValue, comparator e restituisce la stringa
		 * finale.
		 *
		 * @return String - Stringa contenente i valori di beginExampleIndex,
		 *         endExampleIndex, child, splitValue, comparator concatenati.
		 */
		@Override
		public String toString() {
			return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:" + beginIndex + "-"
					+ endIndex + "]";
		}

		/**
		 * Restituisce il valore dell'operatore matematico che definisce il test
		 *
		 * @return String - valore dell'operatore matematico che definisce il test
		 */
		String getComparator() {
			return comparator;
		}

	}
}
