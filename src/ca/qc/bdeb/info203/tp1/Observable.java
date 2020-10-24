package ca.qc.bdeb.info203.tp1;

public interface Observable {
    void definirObservateur(Observateur observateur);
    void informerObservateur();
}
