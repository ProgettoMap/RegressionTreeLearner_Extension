package tree;

import java.io.Serializable;

import data.Data;

/** Classe astratta che modella il nodo di un albero */
@SuppressWarnings("serial")
abstract class Node implements Serializable {

	/**
	 * Contatore dei nodi generati nell'albero
	 */
	static int idNodeCount = 0;
	private int idNode; // identificativo numerico del nodo
	private int beginExampleIndex; // indice nell'array del training set del primo esempio coperto dal nodo
	// corrente
	private int endExampleIndex; // indice nell'array del training set dell'ultimo esempio coperto dal nodo
	// corrente. beginExampleIndex e endExampleIndex individuano //un sotto-insieme
	// di training
	private double variance; // valore dello SSE calcolato, rispetto all'attributo di classe, nel
	// sotto-insieme di training del nodo

	/**
	 * Avvalora gli attributi primitivi di classe, inclusa la varianza che viene
	 * calcolata rispetto all'attributo da predire nel sotto-insieme di training
	 * coperto dal nodo
	 *
	 * @param trainingSet - oggetto di classe Data contenente il training set
	 *             completo
	 * @param beginExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @param endExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 */
	Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {

		this.beginExampleIndex = beginExampleIndex;
		this.endExampleIndex = endExampleIndex;
		this.variance = computeVariance(trainingSet, beginExampleIndex, endExampleIndex);
		this.idNode = idNodeCount;
		idNodeCount++;

	}

	/**
	 * @param trainingSet - oggetto di classe Data contenente il training set
	 *             completo
	 * @param beginExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @param endExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @return Valore della somma degli scarti quadratici
	 */
	private double computeVariance(Data trainingSet, int beginExampleIndex, int endExampleIndex) {

		double[] classValues = new double[endExampleIndex - beginExampleIndex + 1];
		double sommatoria = 0;
		for (int i = 0; i < endExampleIndex - beginExampleIndex + 1; i++) {
			classValues[i] = trainingSet.getClassValue(i + beginExampleIndex);
			sommatoria += trainingSet.getClassValue(i + beginExampleIndex);
		}
		double avgValori = sommatoria / (endExampleIndex - beginExampleIndex + 1);

		double sse = 0;
		for (int i = 0; i < endExampleIndex - beginExampleIndex + 1; i++)
			sse += Math.pow(classValues[i] - avgValori, 2);

		return sse;

	}

	/**
	 * Restituisce il valore dell'attributo idNode
	 *
	 * @return int - identificativo numerico del nodo
	 */
	int getIdNode() {
		return idNode;
	}

	/**
	 * Restituisce il valore dell'attributo beginExampleIndex
	 *
	 * @return int - indice del primo esempio del sotto-insieme rispetto al training
	 *         set complessivo
	 */
	int getBeginExampleIndex() {
		return beginExampleIndex;
	}

	/**
	 * Restituisce il valore dell'attributo endExampleIndex
	 *
	 * @return int - indice dell'ultimo esempio del sotto-insieme rispetto al
	 *         training set complessivo
	 */
	int getEndExampleIndex() {
		return endExampleIndex;
	}

	/**
	 * Restituisce il valore dell'attributo variance
	 *
	 * @return double - valore dello SSE dell’attributo da predire rispetto al nodo
	 *         corrente
	 */
	double getVariance() {
		return variance;
	}

	/**
	 * Metodo astratto la cui implementazione riguarda i nodi di tipo test (split
	 * node) dai quali si possono generare figli, uno per ogni split prodotto.
	 *
	 * @return int - Numero di nodi figli
	 */
	abstract int getNumberOfChildren();

	/**
	 * Concatena in un oggetto String i valori di beginExampleIndex,
	 * endExampleIndex, variance e restituisce la stringa finale.
	 *
	 */
	@Override
	public String toString() {
		return ("[Examples: " + beginExampleIndex + "-" + endExampleIndex + "] variance:" + variance);
	}

}
