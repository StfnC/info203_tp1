package ca.qc.bdeb.info203.tp1.exceptions;

/**
 * Exception liée à la validation des grilles chargées
 */
public class GrilleInvalideException extends Exception {
    /**
     * Construit une exception sans message
     */
    public GrilleInvalideException() {
    }

    /**
     * Construit une exception avec un message
     * @param message Message à afficher
     */
    public GrilleInvalideException(String message) {
        super(message);
    }
}
