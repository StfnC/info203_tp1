package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.observer.Observable;
import ca.qc.bdeb.info203.tp1.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Bouton adapté au jeu de Sudoku
 */
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

    /**
     * Construit une case qui peut être cliquée
     * Cette case informe un Observateur lorsqu'elle est cliquée
     *
     * @param observateur Objet qui doit être informé lorsqu'un bouton est cliqué
     */
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
                // Lorsqu'on clique sur le bouton, il faut incrémenter le nombre de clics et informer l'Observateur
                nbTotalClics++;
                informerObservateur();
            }
        });
    }

    /**
     * Construit une case ayant une valeur initiale.
     * Cette case ne peut pas être cliquée
     *
     * @param valeurCase Valeur initiale de la case
     */
    public CaseSudoku(int valeurCase) {
        super("" + valeurCase);
        this.numCase = nbTotalCases;
        nbTotalCases++;
        // La case ne doit pas être cliquable si elle à une valeur
        this.setEnabled(false);
    }

    /**
     * Remet le compteur de clics à 0
     */
    public static void reinitialiserNbTotalClics() {
        CaseSudoku.nbTotalClics = 0;
    }

    /**
     * Donne le numéro de la case
     *
     * @return Numéro de la case
     */
    public int getNumCase() {
        return numCase;
    }

    /**
     * Change la couleur du bouton en fonction de l'indice donné
     *
     * @param niveauCouleur Niveau de couleur à mettre
     */
    public void setIndiceCouleurCase(int niveauCouleur) {
        this.setBackground(indicesCouleurs.get(niveauCouleur));
        this.setForeground(COULEUR_TEXT_PAR_DEFAUT);
        if (niveauCouleur == 0) {
            // Ceci améliore le contrast du texte lorsque le bouton est noir
            this.setForeground(Color.WHITE);
        }
    }

    /**
     * Donne le nombre total de clics sur les cases
     *
     * @return Nombre total de clics
     */
    public static int getNbTotalClics() {
        return nbTotalClics;
    }

    /**
     * Remet le compteur de cases à zéro
     */
    public static void reinitialiserNbTotalCases() {
        CaseSudoku.nbTotalCases = 0;
    }

    /**
     * Définit l'instance du jeu qui va observer la case
     *
     * @param observateur Objet Observateur qui doit connaitre les changements d'états de la case
     */
    @Override
    public void definirObservateur(Observateur observateur) {
        jeu = observateur;
    }

    /**
     * Informe le jeu que la case a été cliquée
     */
    @Override
    public void informerObservateur() {
        jeu.mettreAJour(this);
    }
}
