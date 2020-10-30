package ca.qc.bdeb.info203.tp1.jeu;

import ca.qc.bdeb.info203.tp1.gui.CaseSudoku;
import ca.qc.bdeb.info203.tp1.gui.FenetrePrincipale;
import ca.qc.bdeb.info203.tp1.observer.Observable;
import ca.qc.bdeb.info203.tp1.observer.Observateur;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class Jeu implements Observateur {
    private final int TAILLE_GRILLE = 4;
    private final String DELIMITEUR_GRILE = "-";
    private FenetrePrincipale fenetreJeu;
    private List<String> lignesGrillesInitiales;
    private int[][] matriceJeu;

    public Jeu(File grille, FenetrePrincipale fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
        matriceJeu = new int[TAILLE_GRILLE][TAILLE_GRILLE];
        this.lireFichier(grille);
        this.initialiserMatriceJeu();
    }

    public int getTailleGrille() {
        return TAILLE_GRILLE;
    }

    public int[][] getMatriceJeu() {
        return this.matriceJeu;
    }

    public void lireFichier(File fichier) {
        // TODO: Handle the exception better
        // TODO: Make sure that ligneGrillesInitiales % 4 == 0, so there are only complete grids
        try {
            this.lignesGrillesInitiales = Files.readAllLines(fichier.toPath());
            // Cette ligne permet d'enlever les lignes qui séparent les grilles.
            // On ne peut pas simplement utiliser un boucle et enlever des éléments de la liste, car cela lance une ConcurrentModificationException
            this.lignesGrillesInitiales.removeIf(ligne -> ligne.contains(DELIMITEUR_GRILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int choisirGrilleRandom() {
        Random r = new Random();
        int nbHasard = r.nextInt(this.lignesGrillesInitiales.size());
        int indexLigneGrille = Math.floorDiv(nbHasard, TAILLE_GRILLE) * TAILLE_GRILLE;
        return indexLigneGrille;
    }

    public void initialiserMatriceJeu() {
        // FIXME: Refactor these loops
        int indiceLigneGrille = choisirGrilleRandom();
        for (int i = indiceLigneGrille; i < (indiceLigneGrille + TAILLE_GRILLE); i++) {
            String ligne = this.lignesGrillesInitiales.get(i);
            char[] characteres = ligne.toCharArray();
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                int valeurNumerique = Character.getNumericValue(characteres[j]);

                // Character.getNumericValue() renvoie -1 si le character n'est pas un nombre
                // FIXME: This is kinda whack
                matriceJeu[i % TAILLE_GRILLE][j] = Math.max(valeurNumerique, 0);
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

    public boolean verifierSiGrilleValide() {
        boolean grilleValide = true;

        grilleValide = grilleValide && verifierLignesHorizontales();

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
            CaseSudoku caseSudoku = (CaseSudoku) origine;
            int ligne = Math.floorDiv(caseSudoku.getNumCase(), TAILLE_GRILLE);
            int colonne = caseSudoku.getNumCase() % TAILLE_GRILLE;

            int valeurChangee = matriceJeu[ligne][colonne];
            // Le modulo + 1 permet de toujours gader la valeur de la case entre 1 et valeurMax
            int nouvelleValeur = (valeurChangee % TAILLE_GRILLE) + 1;
            int niveauIndice = peutPlacerValeur(ligne, colonne, nouvelleValeur);
            matriceJeu[ligne][colonne] = nouvelleValeur;
            caseSudoku.setIndiceCouleurCase(niveauIndice);
            caseSudoku.setText("" + nouvelleValeur);
            afficherMatrice();

            boolean grilleValide = verifierSiGrilleValide();
            if (grilleValide) {
                proposerDeRecommencer();
            }
        }
    }

    /**
     * Propose au joueur de recommencer
     */
    public void proposerDeRecommencer() {
        // On demande au joueur s'il veut recommencer
        int recommencer = JOptionPane.showConfirmDialog(
                null,
                "Vous avez gagné avec " + CaseSudoku.getNbTotalClics() + " modifications! Voulez-vous recommencer?",
                "Partie terminée", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (recommencer == JOptionPane.YES_OPTION) {
            fenetreJeu.recommencerPartie();
        } else {
            System.exit(0);
        }
    }

    /**
     * @param ligne Ligne dans la matrice
     * @param colonne Colonne dans la matrice
     * @param valeur Valeur a verifier
     * @return Niveau de validite du move
     */
    public int peutPlacerValeur(int ligne, int colonne, int valeur) {
        // TODO: -Make this DRY, for now, only for testing
        //       -Maybe use this instead of all the verifier blocs to assign the correct color to the button
        //       -Expplain why chose to use int
        int niveauDeCouleur = 0;
        int limiteBloc = (int) Math.sqrt(TAILLE_GRILLE);

        boolean pasSurLigne = true;
        boolean pasSurColonne = true;
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            // Verifier chaque valeur sur la ligne
            if (matriceJeu[ligne][i] == valeur) {
                pasSurLigne = false;
            }
            // Verifier chaque valeurs sur la colonne
            if (matriceJeu[i][colonne] == valeur) {
                pasSurColonne = false;
            }
        }

        if (pasSurLigne && pasSurColonne) {
            niveauDeCouleur += 2;
        } else if (pasSurLigne || pasSurColonne) {
            niveauDeCouleur++;
        }

        // FIXME: Not very DRY, these loops are a mess
        // FIXME: Doesn't work
        boolean pasDansBloc = true;
        int indexColonneBloc = Math.floorDiv(colonne, limiteBloc) * limiteBloc;
        int indexLigneBloc = Math.floorDiv(ligne, limiteBloc) * limiteBloc;
        for (int i = indexLigneBloc; i < (indexLigneBloc + limiteBloc); i++) {
            for (int j = indexColonneBloc; j < (indexColonneBloc + limiteBloc); j++) {
                if (matriceJeu[i][j] == valeur) {
                    pasDansBloc = false;
                }
            }
        }
        if (pasDansBloc) {
            niveauDeCouleur++;
        }

        return niveauDeCouleur;
    }

    public void resoudre() {
        // FIXME: Broken
        // Adapté d'ici: https://www.youtube.com/watch?v=G_UYXzGuqvM&ab_channel=Computerphile
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                // Si la case n'a pas encore de valeur
                if (matriceJeu[i][j] == 0) {
                    for (int k = 1; k <= TAILLE_GRILLE; k++) {
                        if (peutPlacerValeur(i, j, k) == 0) {
                            matriceJeu[i][j] = k;
                            resoudre();
                            matriceJeu[i][j] = 0;
                        }
                    }
                }
            }
        }
        afficherMatrice();
    }
}
