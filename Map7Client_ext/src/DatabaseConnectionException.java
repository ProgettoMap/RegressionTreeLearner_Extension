/**
 * Eccezione lanciata quando si verifica un problemo con il DB server
 *
 */
public class DatabaseConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore dell'eccezione DatabaseConnectionException
     */
    public DatabaseConnectionException() {
        super();
    }
}
