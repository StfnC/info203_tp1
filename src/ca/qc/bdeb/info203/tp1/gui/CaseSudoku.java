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
    private static int nbTotalClics = 0;
    private static int nbTotalCases = 0;
    private Map<Integer, Color> indicesCouleurs = new HashMap<>();
    private int numCase;
    private Color coueurParDefaut = Color.WHITE;
    private Observateur jeu;

    public CaseSudoku(Observateur observateur) {
        indicesCouleurs.put(0, Color.DARK_GRAY);
        indicesCouleurs.put(1, Color.RED);
        indicesCouleurs.put(2, Color.YELLOW);
        indicesCouleurs.put(3, Color.GREEN);

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
    }

    public static int getNbTotalClics() {
        return nbTotalClics;
    }

    public static void setNbTotalCases(int nbTotalCases) {
        CaseSudoku.nbTotalCases = nbTotalCases;
    }

    @Override
    public String toString() {
        return "CaseSudoku{" +
                "numCase=" + numCase +
                '}';
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
