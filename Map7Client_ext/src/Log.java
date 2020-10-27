import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/** Classe che rappresenta l'entità Log, ovvero un registro che contiene tutti gli errori */
public class Log {

    private static ArrayList<String> log = new ArrayList<>(); // Arraylist di stringhe contenente tutte le stringhe di errore
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss"); // Classe utility per ottenere il timestamp in human-readable form

    private Log(){}

    
    /** 
     * Metodo che permette l'inserimento di un messaggio di log
     * @param messaggio Stringa contenente l'errore
     */
    static void inserisciMessaggio(String messaggio) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	log.add("(" + sdf.format(timestamp) + ") " + messaggio);
    }
    
    /**
     * Metodo che restituisce tutti i messaggi registrati, separati da un invio
     * @return Stringa concatenata di tutti i messaggi di errore
     */
    static String getAllMessages() {
        Iterator<String> it = log.iterator();
        String logList = "";
        while(it.hasNext()) 
            logList += it.next() + "\n";

        return logList;
    }
}
