package tree;

import java.io.Serializable;
import java.util.ArrayList;

import data.Attribute;
import data.Data;

/** Classe che modella il nodo di split relativo ad un attributo indipendente discreto */
@SuppressWarnings("serial")
class DiscreteNode extends SplitNode implements Serializable {

	/**
	 *
	 * Istanzia un oggetto invocando il costruttore della superclasse con il
	 * parametro attribute
	 *
	 * @param trainingSet       - oggetto di classe Data contenente il training set
	 *                          completo
	 * @param beginExampleIndex - Indice estremo del sotto-insieme di training
	 * @param endExampleIndex   - Indice estremo del sotto-insieme di training
	 * @param attribute         - Attributo indipendente sul quale si definisce lo
	 *                          split
	 */
	public DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}

	/**
	 * (Implementazione da class abstract): Istanzia oggetti SplitInfo (definita
	 * come inner class in Splitnode) con ciascuno dei valori discreti
	 * dell’attributo relativamente al sotto-insieme di training corrente (ossia la
	 * porzione di trainingSet compresa tra beginExampleIndex e endExampleIndex),
	 * quindi popola l'array mapSplit[] con tali oggetti
	 *
	 * @param trainingSet       - oggetto di classe Data contenente il training set
	 *                          completo
	 * @param beginExampleIndex - indice che identifica il sotto-insieme di training
	 *                          coperto dal nodo corrente
	 * @param endExampleIndex   - indice che identifica il sotto-insieme di training
	 *                          coperto dal nodo corrente
	 * @param attribute         - Attributo indipendente sul quale si definisce lo
	 *                          split
	 *
	 */
	@Override
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {

		// numberOfSplit non viene più utilizzato in quanto adesso utilizziamo una lista
		// di stringhe che non può avere dimensione fissa
		// int numberOfSplit = getNumberOfSplit(trainingSet, beginExampleIndex,
		// endExampleIndex, attribute);

		int begin = beginExampleIndex;
		Object splitValue = trainingSet.getExplanatoryValue(begin, attribute.getIndex());
		Object explanatoryValue = null;

		mapSplit = new ArrayList<SplitInfo>();

		int counterSplit = 0;

		for (int x = beginExampleIndex; x <= endExampleIndex; x++) {
			explanatoryValue = trainingSet.getExplanatoryValue(x, attribute.getIndex());
			if (!explanatoryValue.equals(splitValue)) {
				mapSplit.add(counterSplit, new SplitInfo(splitValue, begin, x - 1, counterSplit));
				splitValue = explanatoryValue;
				begin = x;
				counterSplit++;
			} else if (x == endExampleIndex)
				mapSplit.add(counterSplit, new SplitInfo(splitValue, begin, endExampleIndex, counterSplit));
		}

	}

	/**
	 * (Implementazione da class abstract): effettua il confronto del valore in
	 * input rispetto al valore contenuto nell’attributo splitValue di ciascuno
	 * degli oggetti SplitInfo collezionati in mapSplit[] e restituisce
	 * l'identificativo dello split (indice della posizione nell’array mapSplit) con
	 * cui il test è positivo
	 *
	 * @param value - valore discreto dell'attributo che si vuole testare rispetto a
	 *              tutti gli split
	 * @return int branchNumber - Identificativo dello split (indice della posizione
	 *         nell’array mapSplit)
	 *
	 */
	@Override
	int testCondition(Object value) {

		String valueStr = (String) value;
		int k = 0;
		for (SplitInfo splitInfo : mapSplit) {
			if (splitInfo.getSplitValue().equals(valueStr))
				return k;
			k += 1;
		}
		return -1;
	}

	/**
	 * Invoca il metodo della superclasse specializzandolo per discreti
	 */
	@Override
	public String toString() {
		return "DISCRETE " + super.toString();
	}

	/**
	 * Confronta i valori di splitVariance dei due nodi, restituendone l'esito. 0 :
	 * valori di gain uguali; -1 : valore di gain minore 1 : valore di gain maggiore
	 */
	@Override
	public int compareTo(SplitNode o) {
		return ((Double) this.getVariance()).compareTo(o.getVariance());
	}

}
