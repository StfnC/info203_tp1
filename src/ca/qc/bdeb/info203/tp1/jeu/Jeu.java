package ca.qc.bdeb.info203.tp1.jeu;

import ca.qc.bdeb.info203.tp1.exceptions.GrilleInvalideException;
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

/**
 * Logique du jeu Sudoku
 * Cette logique fonctionne avec n'importe quelle taille de grille, en autant que ce soit un carré parfait (4x4, 9x9, 16x16, etc.)
 */
public class Jeu implements Observateur {
    private final int TAILLE_GRILLE = 4;
    private final String DELIMITEUR_GRILE = "-";
    private FenetrePrincipale fenetreJeu;
    private List<String> lignesGrillesInitiales;
    private int[][] matriceJeu;

    /**
     * Construit un jeu lié à une fenêtre
     *
     * @param fenetreJeu Fenêtre liée à l'instance du jeu
     */
    public Jeu(FenetrePrincipale fenetreJeu) {
        this.fenetreJeu = fenetreJeu;
        matriceJeu = new int[TAILLE_GRILLE][TAILLE_GRILLE];
    }

    /**
     * Donne la taille de la grille du jeu
     *
     * @return Taille de la grille du jeu
     */
    public int getTailleGrille() {
        return TAILLE_GRILLE;
    }

    /**
     * Donne la matrice contenant l'état de la grille
     *
     * @return Matrice contenant l'état de la grille
     */
    public int[][] getMatriceJeu() {
        return this.matriceJeu;
    }

    /**
     * Extrait les grilles d'un fichier
     *
     * @param fichier Fichier d'où il faut extraire les grilles
     * @throws GrilleInvalideException Exception lancée en cas de mauvais format de grille
     */
    public void lireFichier(File fichier) throws GrilleInvalideException {
        try {
            this.lignesGrillesInitiales = Files.readAllLines(fichier.toPath());
            // Cette ligne permet d'enlever les lignes qui séparent les grilles
            // On ne peut pas simplement utiliser un boucle et enlever des éléments de la liste, car cela lance une ConcurrentModificationException
            this.lignesGrillesInitiales.removeIf(ligne -> ligne.contains(DELIMITEUR_GRILE));

            if (this.lignesGrillesInitiales.size() < TAILLE_GRILLE || this.lignesGrillesInitiales.size() % TAILLE_GRILLE != 0) {
                throw new GrilleInvalideException("Vous avez des grilles incomplètes dans le fichier.");
            } else if (!validerLignesEgales()) {
                throw new GrilleInvalideException("Mauvais format de fichier.");
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(fenetreJeu, "Erreur d'entré/sortie", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valide si les lignes du fichier contenant les grilles sont toutes de la bonne taille
     *
     * @return Résultat de la validation de la taille des lignes
     */
    public boolean validerLignesEgales() {
        boolean valide = true;
        for (String ligne : this.lignesGrillesInitiales) {
            // On regarde si chaque ligne contient exactement le bon nombre de charactères
            valide &= (float) ligne.length() / TAILLE_GRILLE == 1.0;
        }
        return valide;
    }

    /**
     * Choisie une grille au hasard dans le fichier
     *
     * @return Index de début de la grille choisie
     */
    public int choisirGrilleRandom() {
        Random r = new Random();
        int nbHasard = r.nextInt(this.lignesGrillesInitiales.size());
        // La division entière permet de ne pas d'avoir la ligne exacte du début de la grille
        int indexLigneGrille = Math.floorDiv(nbHasard, TAILLE_GRILLE) * TAILLE_GRILLE;
        return indexLigneGrille;
    }

    /**
     * Initialise la matrice du jeu avec les valeurs initiales de la grille
     */
    public void initialiserMatriceJeu() {
        int indiceLigneGrille = choisirGrilleRandom();

        for (int i = indiceLigneGrille; i < (indiceLigneGrille + TAILLE_GRILLE); i++) {
            String ligne = this.lignesGrillesInitiales.get(i);
            char[] characteres = ligne.toCharArray();
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                int valeurNumerique = Character.getNumericValue(characteres[j]);
                // Character.getNumericValue() renvoie -1 si le character n'est pas un nombre
                matriceJeu[i % TAILLE_GRILLE][j] = Math.max(valeurNumerique, 0);
            }
        }
        afficherMatrice();
    }

    /**
     * Affiche la matrice du jeu
     */
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

    /**
     * Effectue toutes les vérifications pour que la grille soit complète
     *
     * @return Résultat de la validation de la grille
     */
    public boolean verifierSiGrilleValide() {
        boolean grilleValide = true;

        grilleValide &= verifierLignesHorizontales();
        grilleValide &= verifierLignesVericales();
        grilleValide &= verifierBlocs();

        return grilleValide;
    }

    /**
     * Vérifie si toutes les lignes horizontales respectent les conditions pour qu'une grille soit valide
     *
     * @return Résultat de la validation des lignes horizontales
     */
    public boolean verifierLignesHorizontales() {
        boolean valide = true;
        for (int[] ligneHorizontale : matriceJeu) {
            valide &= verifierBonsChiffresPresents(ligneHorizontale);
        }
        return valide;
    }

    /**
     * Vérifie si toutes les lignes verticales respectent les conditions pour qu'une grille soit valide
     *
     * @return Résultat de la validation des lignes verticales
     */
    public boolean verifierLignesVericales() {
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

    /**
     * Vérifie si tous les blocs respectent les conditions pour qu'une grille soit valide
     *
     * @return Résultat de la validation des blocs
     */
    public boolean verifierBlocs() {
        boolean valide = true;
        // Une bloc à une taille correspondant à la racine carrée de la taille de la grille
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

    /**
     * Valide qu'un tableau contient tous les chiffres entre 1 et la taille de la grille
     *
     * @param chiffres Tableau de valeurs à valider
     * @return true si tous les chiffres sont présents et uniques
     */
    public boolean verifierBonsChiffresPresents(int[] chiffres) {
        boolean valide = true;
        for (int i = 1; i <= TAILLE_GRILLE; i++) {
            // Si la liste ne contient pas un chiffre, c'est qu'il y en a un en double, ou il y a un 0
            valide &= tableauContient(chiffres, i);
        }
        return valide;
    }

    /**
     * Vérifie si un tableau contient une valeur
     *
     * @param chiffres Tableau d'entiers où il faut retrouver la valeur
     * @param valeur   Valeur à chercher
     * @return true si le tableau contient la valeur cherchée
     */
    public boolean tableauContient(int[] chiffres, int valeur) {
        boolean trouve = false;
        for (int chiffre : chiffres) {
            if (chiffre == valeur) {
                trouve = true;
            }
        }
        return trouve;
    }

    /**
     * Incrémente la valeur à l'emplacement de la case cliquée
     * Appelle l'interface graphique à se mettre à jour
     *
     * @param origine Objet Observable dont l'état a changé
     */
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

            // Mise à jour de l'interface graphique
            caseSudoku.setIndiceCouleurCase(niveauIndice);
            caseSudoku.setText("" + nouvelleValeur);
            afficherMatrice();

            // Vérifie si le joueur a complété la grille
            boolean grilleValide = verifierSiGrilleValide();
            if (grilleValide) {
                fenetreJeu.arreterTimer();
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
                fenetreJeu,
                "Vous avez gagné avec " + CaseSudoku.getNbTotalClics() + " modifications en " + fenetreJeu.getTempsPartie() + " secondes! \n" +
                        "Voulez-vous recommencer?",
                "Partie terminée", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (recommencer == JOptionPane.YES_OPTION) {
            fenetreJeu.recommencerPartie();
        } else {
            System.exit(0);
        }
    }

    /**
     * Évalue le niveau de validité d'une valeur à une position précise
     *
     * @param ligne   Ligne sur laquelle on veut placer une valeur
     * @param colonne Colonne colonne sur laquelle on veut placer une valeur
     * @param valeur  Valeur à vérifier
     * @return Niveau de validité de la valeur à l'emplacement évalué
     */
    public int peutPlacerValeur(int ligne, int colonne, int valeur) {
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

        // Si la valeur est unique sur la ligne et la colonne, on augmente le niveau de validité de 2
        if (pasSurLigne && pasSurColonne) {
            niveauDeCouleur += 2;
        } else if (pasSurLigne || pasSurColonne) {
            niveauDeCouleur++;
        }

        boolean pasDansBloc = true;
        // Ces calculs servent à détermniner dans quel bloc la valeur à vérifier se trouve
        // Adapté de l'algorithme présenté dans cette vidéo (vers 2:42): https://www.youtube.com/watch?v=G_UYXzGuqvM&ab_channel=Computerphile
        int indexColonneBloc = Math.floorDiv(colonne, limiteBloc) * limiteBloc;
        int indexLigneBloc = Math.floorDiv(ligne, limiteBloc) * limiteBloc;

        for (int i = indexLigneBloc; i < (indexLigneBloc + limiteBloc); i++) {
            for (int j = indexColonneBloc; j < (indexColonneBloc + limiteBloc); j++) {
                if (matriceJeu[i][j] == valeur) {
                    pasDansBloc = false;
                }
            }
        }

        // Si la valeur est unique dans le bloc, on augmente le niveau de validité de 1
        if (pasDansBloc) {
            niveauDeCouleur++;
        }

        return niveauDeCouleur;
    }

    /**
     * Résout la grille à l'aide d'un algorithme de backtracking
     */
    public void resoudre() {
        // Adapté de l'algorithme présenté dans cette vidéo (vers 4:00): https://www.youtube.com/watch?v=G_UYXzGuqvM&ab_channel=Computerphile
        for (int ligne = 0; ligne < TAILLE_GRILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE_GRILLE; colonne++) {
                // Si la case n'a pas encore de valeur
                if (matriceJeu[ligne][colonne] == 0) {
                    for (int candidat = 1; candidat <= TAILLE_GRILLE; candidat++) {
                        // 3 est le nombre de validations qu'une valeur doit passer pour que ce soit une bonne valeur
                        if (peutPlacerValeur(ligne, colonne, candidat) == 3) {
                            matriceJeu[ligne][colonne] = candidat;
                            resoudre();
                            matriceJeu[ligne][colonne] = 0;
                        }
                    }
                    return;
                }
            }
        }
        afficherMatrice();
        fenetreJeu.mettreAJourInterfaceAvecNouvelleGrille();
    }
}
