package ca.qc.bdeb.info203.tp1.observer;

public interface Observable {
    void definirObservateur(Observateur observateur);
    void informerObservateur();
}
