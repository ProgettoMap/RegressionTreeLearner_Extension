/**
 * Eccezione lanciata quando si verifica un problemo con il DB server
 *
 */
 class DatabaseConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore dell'eccezione DatabaseConnectionException
     */
    DatabaseConnectionException() {
        super();
    }
}
