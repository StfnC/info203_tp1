package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Grille qui contient les boutons du jeu
 */
public class ConteneurGrille extends JPanel {
    private static final int ESPACE_HORIZONTAL = 5;
    private static final int ESPACE_VERTICAL = 5;

    /**
     * Construit une grille d'une taille spécifique
     *
     * @param tailleGrille Taille de la grille à initialiser
     */
    public ConteneurGrille(int tailleGrille) {
        super(new GridLayout(tailleGrille, tailleGrille, ESPACE_HORIZONTAL, ESPACE_VERTICAL));
        Random r = new Random();
        // Rajoute un peu de couleur, plus amusant
        this.setBackground(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
    }

    /**
     * Initialiser les boutons de la grille à partir d'une matrice
     *
     * @param matriceValeurs Matrice contenant les valeurs des boutons à initialiser
     * @param observateur    Observateur qui sera informé des changements d'état des boutons
     */
    public void populerGrille(int[][] matriceValeurs, Observateur observateur) {
        for (int[] ligne : matriceValeurs) {
            for (int valeur : ligne) {
                // La case est vide
                if (valeur <= 0) {
                    ajouterCaseDansGrille(new CaseSudoku(observateur));
                } else { // La case a une valeur
                    ajouterCaseDansGrille(new CaseSudoku(valeur));
                }
            }
        }
    }

    /**
     * Méthode qui ajoute un bouton à la grille
     *
     * @param caseSudoku Bouton de Sudoku à ajouter à la grille
     */
    public void ajouterCaseDansGrille(CaseSudoku caseSudoku) {
        this.add(caseSudoku);
        caseSudoku.invalidate();
        caseSudoku.repaint();
    }
}
