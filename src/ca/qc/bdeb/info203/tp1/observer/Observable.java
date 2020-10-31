package ca.qc.bdeb.info203.tp1.observer;

/**
 * Interface qui définit le contrat du Sujet dans le Patron Observer
 */
public interface Observable {
    /**
     * Définit l'objet Observateur qui va être informé des changement d'états du Sujet
     *
     * @param observateur Objet Observateur qui doit connaitre les changements d'états du Sujet
     */
    void definirObservateur(Observateur observateur);

    /**
     * Informe l'Observateur d'un chnagement d'état
     */
    void informerObservateur();
}
