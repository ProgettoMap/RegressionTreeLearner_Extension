/**
 * 
 * Eccezione lanciata quando la tabella non viene trovata nel database
 *
 */
class TableNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Costruttore della classe TableNotFoundException
     */
   TableNotFoundException() {
        super();
    }
}
