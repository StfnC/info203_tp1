package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ConteneurGrille extends JPanel {
    private static final int ESPACE_HORIZONTAL = 5;
    private static final int ESPACE_VERTICAL = 5;
    private int tailleGrille;
    private ArrayList<CaseSudoku> listeCasesSudokuGrille = new ArrayList<>();

    // TODO: Javadoc everything

    public ConteneurGrille(int tailleGrille) {
        super(new GridLayout(tailleGrille, tailleGrille, ESPACE_HORIZONTAL, ESPACE_VERTICAL));
        this.tailleGrille = tailleGrille;
        Random r = new Random();
        // Rajoute un peu de couleur, plus amusant
        this.setBackground(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
    }

    public void populerGrille(int[][] matriceValeurs, Observateur observateur) {
        viderConteneur();
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

    public void ajouterCaseDansGrille(CaseSudoku caseSudoku) {
        listeCasesSudokuGrille.add(caseSudoku);
        this.add(caseSudoku);
    }

    public void viderConteneur() {
        // FIXME: Crashes
        for (CaseSudoku caseSudoku : listeCasesSudokuGrille) {
            this.remove(caseSudoku);
            listeCasesSudokuGrille.remove(caseSudoku);
        }
    }
}
