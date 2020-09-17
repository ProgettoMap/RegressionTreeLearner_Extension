package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * Classe che modella la tabella di un database
 *
 */
public class TableData {

	private DbAccess db;

	/**
	 * @param db Gestore database
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione per
	 * estrarre le tuple distinte da tale tabella. Per ogni tupla del resultset, si
	 * crea un oggetto istanza della classe Example, il cui riferimento va incluso
	 * nella lista da restituire. In particolare, per la tupla corrente nel
	 * resultset, si estraggono i valori dei singoli campi (usando getFloat() o
	 * getString()), e li si aggiungono all’oggetto istanza della classe Example che
	 * si sta costruendo.
	 *
	 * @param table Nome della tabella nel database
	 * @return Lista di transazioni memorizzate nella tabella
	 * @throws SQLException      Eccezione generica che viene lanciata quando non è
	 *                           possibile comunicare con il database o non è
	 *                           possibile creare uno statement per l'interazione
	 *                           con esso, o quando la query non viene eseguita
	 *                           correttamente
	 * @throws EmptySetException Eccezione lanciata quando la tabella è vuota, o le
	 *                           query ritornano 0 righe come risultato
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db, table);

		String query = "SELECT ";

		int countAttributes = tSchema.getNumberOfAttributes();
		for (int i = 0; i < countAttributes; i++) {
			Column c = tSchema.getColumn(i);
			if (i > 0)
				query += ",";
			query += c.getColumnName();
		}
		if (countAttributes == 0)
			throw new SQLException("[!] Error [!] The table '" + table + "' has " + countAttributes
					+ " column. This number of column is not enough for the tree learning.");
		query += (" FROM " + table);

		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty = true;
		while (rs.next()) { // Per ogni riga
			empty = false;
			Example currentTuple = new Example();
			for (int i = 0; i < countAttributes; i++)
				if (tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i + 1));
				else
					currentTuple.add(rs.getString(i + 1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if (empty)
			throw new EmptySetException("[!] Error [!] The table '" + table
					+ "' has zero rows, therefore is empty. Before the execution of the learning of the tree, insert some row first!");

		return transSet;
	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti
	 * ordinati di column e popolare un insieme da restituire
	 *
	 * @param table  Nome della tabella dalla quale si vogliono ottenere i valori
	 *               distinti
	 * @param column Nome della colonna dalla quale si vogliono ottenere i valori
	 *               distinti
	 * @return Insieme di valori distinti ordinati in modalità ascendente che
	 *         l’attributo identificato da nome column assume nella tabella
	 *         identificata dal nome table
	 * @throws SQLException Viene scatenata un'eccezione quando la query non viene
	 *                      eseguita correttamente, la tabella non è presente, la
	 *                      query presenta un errore di sintassi.
	 */
	// E' stato modificato il tipo del valore di ritorno da Set<Object> a
	// Set<String>
	// perchè il metodo viene chiamato attualmente solo nell'individuazione di tutti
	// i valori distinti degli attributi di tipo discreto.
	// In alternativa si sarebbe dovuto "convertire" il set di object nel tipo
	// desiderato, ma essendo l'utilizzo univoco (allo stato attuale del progetto),
	// si è ritenuto più utile modificare il valore di ritorno.
	public Set<String> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<String> distinctValues = new TreeSet<String>();
		ResultSet r = db.getConnection().createStatement().executeQuery("SELECT DISTINCT " + column.getColumnName()
				+ " FROM " + table + " ORDER BY " + column.getColumnName() + " ASC");
		while (r.next()) {
			distinctValues.add(r.getString(1)); // Con l'utilizzo di un TreeSet, l'ordinamento avviene
												// automaticamente nell'operazione dell'inserimento
		}
		return distinctValues;
	}

}
