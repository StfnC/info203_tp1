package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.observer.Observable;
import ca.qc.bdeb.info203.tp1.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CaseSudoku extends JButton implements Observable {
    private static final Color COULEUR_TEXT_PAR_DEFAUT = new Color(51, 51, 51);
    private static final Color VERT_PALE = new Color(170, 255, 136);
    private static final Color VERT_MOYEN = new Color(133, 255, 51);
    private static final Color VERT_FONCE = new Color(0, 238, 51);
    private static int nbTotalClics = 0;
    private static int nbTotalCases = 0;
    // Chaque niveau d'indice est associé à une couleur
    private Map<Integer, Color> indicesCouleurs = new HashMap<>();
    private int numCase;
    private Color coueurParDefaut = Color.WHITE;
    private Observateur jeu;

    public CaseSudoku(Observateur observateur) {
        indicesCouleurs.put(0, Color.DARK_GRAY);
        indicesCouleurs.put(1, VERT_PALE);
        indicesCouleurs.put(2, VERT_MOYEN);
        indicesCouleurs.put(3, VERT_FONCE);

        this.definirObservateur(observateur);
        this.numCase = nbTotalCases;
        nbTotalCases++;
        this.setBackground(coueurParDefaut);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nbTotalClics++;
                informerObservateur();
            }
        });
    }

    public CaseSudoku(int valeurCase) {
        super("" + valeurCase);
        this.numCase = nbTotalCases;
        nbTotalCases++;
        // La case ne doit pas être cliquable si elle à une valeur
        this.setEnabled(false);
    }

    public static void setNbTotalClics(int nbTotalClics) {
        CaseSudoku.nbTotalClics = nbTotalClics;
    }

    public int getNumCase() {
        return numCase;
    }

    public void setIndiceCouleurCase(int niveauCouleur) {
        this.setBackground(indicesCouleurs.get(niveauCouleur));
        this.setForeground(COULEUR_TEXT_PAR_DEFAUT);
        if (niveauCouleur == 0) {
            // Ceci améliore le contrast du texte lorsque le bouton est noir
            this.setForeground(Color.WHITE);
        }
    }

    public static int getNbTotalClics() {
        return nbTotalClics;
    }

    public static void setNbTotalCases(int nbTotalCases) {
        CaseSudoku.nbTotalCases = nbTotalCases;
    }

    @Override
    public void definirObservateur(Observateur observateur) {
        jeu = observateur;
    }

    @Override
    public void informerObservateur() {
        jeu.mettreAJour(this);
    }
}
