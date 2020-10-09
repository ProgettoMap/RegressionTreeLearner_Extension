import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Log {

    private static ArrayList<String> log = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

    private Log(){}

    public static void inserisciMessaggio(String messaggio) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	log.add(sdf.format(timestamp) + ") " + messaggio);
    }
    
    public static String getAllMessages() {
        Iterator<String> it = log.iterator();
        String logList = "";
        while(it.hasNext()) {
            logList += it.next() + "\n";
        }
        return logList;
    }
}
