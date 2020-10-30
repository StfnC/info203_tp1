package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.exceptions.GrilleInvalideException;
import ca.qc.bdeb.info203.tp1.jeu.Jeu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FenetrePrincipale extends JFrame {
    private final String GRILLE_PAR_DEFAUT = "grille.txt";
    private final String MESSAGE_COMMENT_JOUER = "Le but du jeu est de remplir chaque ligne, colonne et bloc de 2x2 avec les nombres de 1 à 4. \n" +
                                                 "Attention, aucun chiffre ne doit se répéter dans une ligne/colonne/bloc. \n" +
                                                 "Un indice de couleur vous indique combien de ces conditions sont remplies. \n" +
                                                 "Plus l'indice est foncé, plus la valeur dans la case est adéquate.";
    private final int DEFAULT_FRAME_WIDTH = 800;
    private final int DEFAULT_FRAME_HEIGHT = 600;
    private File fichierGrille = new File(GRILLE_PAR_DEFAUT);
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuJeu = new JMenu("Jeu");
    private JMenuItem mnuNouvellePartie = new JMenuItem("Nouvelle partie");
    private JMenuItem mnuChargerGrille = new JMenuItem("Charger grille");
    private JMenuItem mnuResoudre = new JMenuItem("Résoudre");
    private JMenu mnuAide = new JMenu("Aide");
    private JMenuItem mnuCommentJouer = new JMenuItem("Comment jouer");
    private JMenuItem mnuAPropos = new JMenuItem("À propos");
    private ConteneurGrille pnlJeu;
    private FileNameExtensionFilter filtreExtension = new FileNameExtensionFilter("Fichier texte (.txt)", "txt");
    private JFileChooser fileChooser = new JFileChooser();
    private Jeu jeu;

    // TODO: Add label that shows current number of moves
    // TODO: Add Javadoc

    public FenetrePrincipale() {
        fileChooser.setFileFilter(filtreExtension);
        // TODO: -AJOUTER FILE NOT FOUND EXCEPTIONS
        //       -PROMPT SOMETHING TO CHOOSE A FILE IF FileNotFound, make sure app doesn't crash if a bad file type/wrong grid format is loaded
        jeu = new Jeu(new File(GRILLE_PAR_DEFAUT), this);

        mnuNouvellePartie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FenetrePrincipale.this.recommencerPartie();
            }
        });
        mnuJeu.add(mnuNouvellePartie);
        mnuChargerGrille.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fichierGrille = FenetrePrincipale.this.choisirFichier();
                FenetrePrincipale.this.recommencerPartie();
            }
        });
        mnuJeu.add(mnuChargerGrille);
        mnuResoudre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.resoudre();
            }
        });
        mnuJeu.add(mnuResoudre);
        menuBar.add(mnuJeu);

        mnuCommentJouer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FenetrePrincipale.this, MESSAGE_COMMENT_JOUER);
            }
        });
        mnuAide.add(mnuCommentJouer);
        mnuAPropos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FenetrePrincipale.this, "Créé par Stefan Cotargasanu");
            }
        });
        mnuAide.add(mnuAPropos);
        menuBar.add(mnuAide);

        this.recommencerPartie();

        this.setJMenuBar(menuBar);
        this.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Udoku");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void mettreAJourInterfaceAvecNouvelleGrille() {
        this.setVisible(false);
        pnlJeu = new ConteneurGrille(jeu.getTailleGrille());
        pnlJeu.populerGrille(jeu.getMatriceJeu(), jeu);
        this.setContentPane(pnlJeu);
        this.setVisible(true);
    }

    public File choisirFichier() {
        int actionUtilisateur = fileChooser.showOpenDialog(FenetrePrincipale.this);
        File fichierAOuvrir = this.fichierGrille;

        if (actionUtilisateur == JFileChooser.APPROVE_OPTION) {
            fichierAOuvrir = fileChooser.getSelectedFile();
        }
        return fichierAOuvrir;
    }

    public void recommencerPartie() {
        CaseSudoku.setNbTotalCases(0);
        CaseSudoku.setNbTotalClics(0);
        boolean grilleChargee = false;
        do {
            try {
                jeu.lireFichier(fichierGrille);
                grilleChargee = true;
            } catch (GrilleInvalideException gie) {
                int actionUtilisateur = JOptionPane.showConfirmDialog(this, gie.getMessage() + " Voulez-vous choisir un autre fichier?",
                        "Une erreur est survenue", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (actionUtilisateur == JOptionPane.YES_OPTION) {
                    choisirFichier();
                } else {
                    System.exit(0);
                }
            }
        } while (!grilleChargee);
        jeu.initialiserMatriceJeu();
        this.mettreAJourInterfaceAvecNouvelleGrille();
    }
}
