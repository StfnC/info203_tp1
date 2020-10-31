package ca.qc.bdeb.info203.tp1.observer;

/**
 * Interface qui définit le contrat de l'Observateur dans le Patron Observer
 */
public interface Observateur {
    /**
     * Action que prend l'Observateur lorsqu'un objet Observable change d'état
     *
     * @param origine Objet Observable dont l'état a changé
     */
    void mettreAJour(Observable origine);
}
