package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ConteneurGrille extends JPanel {
    private static final int ESPACE_HORIZONTAL = 5;
    private static final int ESPACE_VERTICAL = 5;
    private int tailleGrille;
    private ArrayList<CaseSudoku> listeCasesSudokuGrille = new ArrayList<>();

    public ConteneurGrille(int tailleGrille) {
        super(new GridLayout(tailleGrille, tailleGrille, ESPACE_HORIZONTAL, ESPACE_VERTICAL));
        this.tailleGrille = tailleGrille;
        Random r = new Random();
        // Rajoute un peu de couleur, plus amusant
        this.setBackground(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
    }

    public void populerGrille(ArrayList<Character> grille) {
        for (Character character : grille) {
            if (character.equals(' ')) {
                ajouterCaseDansGrille(new CaseSudoku(tailleGrille));
            } else {
                // FIXME: This is bad :(, maybe use something else than chars
                ajouterCaseDansGrille(new CaseSudoku(tailleGrille, Integer.parseInt("" + character)));
            }
        }
    }

    public void ajouterCaseDansGrille(CaseSudoku caseSudoku) {
        listeCasesSudokuGrille.add(caseSudoku);
        this.add(caseSudoku);
    }
}
