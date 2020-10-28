package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Jeu implements Observateur {
    private final int TAILLE_GRILLE = 4;
    private List<String> lignesGrillesInitiale;
    private int[][] matriceJeu;

    public Jeu(File grille) {
        matriceJeu = new int[TAILLE_GRILLE][TAILLE_GRILLE];
        this.lireFichier(grille);
        this.initialiserMatriceJeu();
        this.verifierBlocs();
    }

    public int[][] getMatriceJeu() {
        return this.matriceJeu;
    }

    public void lireFichier(File fichier) {
        // TODO: Handle the exception better
        try {
            this.lignesGrillesInitiale = Files.readAllLines(fichier.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialiserMatriceJeu() {
        // FIXME: This doesn't account for multiple grids in one file
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            String ligne = this.lignesGrillesInitiale.get(i);
            char[] characteres = ligne.toCharArray();
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                int valeurNumerique = Character.getNumericValue(characteres[j]);

                // Character.getNumericValue() renvoie -1 si le character n'est pas un nombre
                matriceJeu[i][j] = Math.max(valeurNumerique, 0);
            }
        }
        afficherMatrice();
    }

    public void afficherMatrice() {
        System.out.println();
        for (int[] ligne : matriceJeu) {
            System.out.print("[");
            for (int valeur : ligne) {
                System.out.print(valeur + ", ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public boolean verifierGrille() {
        boolean grilleValide = true;

        grilleValide &= verifierLignesHorizontales();
        grilleValide &= verifierLignesVericales();
        grilleValide &= verifierBlocs();

        return grilleValide;
    }

    private boolean verifierLignesHorizontales() {
        boolean valide = true;
        for (int[] ligneHorizontale : matriceJeu) {
            valide &= verifierBonsChiffresPresents(ligneHorizontale);
        }
        return valide;
    }

    private boolean verifierLignesVericales() {
        boolean valide = true;
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            int[] ligneVerticale = new int[TAILLE_GRILLE];
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                ligneVerticale[j] = matriceJeu[j][i];
            }
            valide &= verifierBonsChiffresPresents(ligneVerticale);
        }
        return valide;
    }

    public boolean verifierBlocs() {
        boolean valide = true;
        int limiteBloc = ((int) Math.sqrt(TAILLE_GRILLE));

        // Algorithme adapté de (voir vers 10:20) https://www.youtube.com/watch?v=Pl7mMcBm2b8&ab_channel=NickWhite
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            int[] valeursBloc = new int[TAILLE_GRILLE];
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                int indexLigne = limiteBloc * (i / limiteBloc);
                int indexColonne = limiteBloc * (i % limiteBloc);
                valeursBloc[j] = matriceJeu[indexLigne + j / limiteBloc][indexColonne + j % limiteBloc];
            }
            valide &= verifierBonsChiffresPresents(valeursBloc);
        }
        return valide;
    }

    private boolean verifierBonsChiffresPresents(int[] chiffres) {
        boolean valide = true;
        for (int i = 1; i <= TAILLE_GRILLE; i++) {
            valide &= listeContient(chiffres, i);
        }
        return valide;
    }

    private boolean listeContient(int[] chiffres, int valeur) {
        boolean trouve = false;
        for (int chiffre : chiffres) {
            if (chiffre == valeur) {
                trouve = true;
            }
        }
        return trouve;
    }

    @Override
    public void mettreAJour(Observable origine) {
        if (origine instanceof CaseSudoku) {
            // TODO: Abstract button value from CaseSudoku to only have it inside Jeu
            CaseSudoku caseSudoku = (CaseSudoku) origine;
            int ligne = Math.floorDiv(caseSudoku.getNumCase(), TAILLE_GRILLE);
            int colonne = caseSudoku.getNumCase() % TAILLE_GRILLE;
            matriceJeu[ligne][colonne] = caseSudoku.getValeurCase();
            afficherMatrice();

            boolean grilleValide = verifierGrille();
            if (grilleValide) {
                JOptionPane.showMessageDialog(null, "Vous avez gagné!");
            } else {
                System.out.println("Keep trying");
            }
        }
    }
}
