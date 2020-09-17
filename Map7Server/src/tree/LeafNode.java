package tree;

import java.io.Serializable;

import data.Data;

/**
 * Classe che modella l'entità nodo fogliare
 */
@SuppressWarnings("serial")
class LeafNode extends Node implements Serializable{


	private Double predictedClassValue; 

	/**
	 * Istanzia un oggetto invocando il costruttore della superclasse
	 *
	 * Avvalora l'attributo predictedClassValue (come media dei valori
	 * dell’attributo di classe che ricadono nella partizione -- ossia la porzione
	 * di trainingSet compresa tra beginExampleIndex e endExampleIndex)
	 *
	 * @param trainingSet - oggetto di classe Data contenente il training set
	 *             completo
	 * @param beginExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 * @param endExampleIndex - indice che identifica il sotto-insieme di
	 *             training coperto dal nodo corrente
	 */
	LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);

		// Avvaloro attributo predictedClassValue come media dei valori
		// dell’attributo di classe che ricadono nella partizione da begin a end
		double sumPredictedClassValue = 0;
		for (int i = beginExampleIndex; i <= endExampleIndex; i++) {
			sumPredictedClassValue += trainingSet.getClassValue(i);
		}
		this.predictedClassValue = sumPredictedClassValue / (endExampleIndex + 1 - beginExampleIndex);

	}

	/**
	 * Restituisce il numero di split originanti dal nodo foglia, ovvero 0.
	 */
	@Override
	int getNumberOfChildren() {
		return 0;
	}

	/**
	 * Restituisce il valore dell'attributo predictedClassValue
	 *
	 * @return double predictedClassValue
	 */
	Double getPredictedClassValue() {
		return predictedClassValue;
	}

	/**
	 * Invoca il metodo della superclasse e assegna anche il valore di classe della
	 * foglia
	 */
	@Override
	public String toString() {
		return "LEAF : class=" + getPredictedClassValue() + " Nodo: " + super.toString();
	}

}
