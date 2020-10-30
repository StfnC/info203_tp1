package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.jeu.Jeu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FenetrePrincipale extends JFrame {
    private final String GRILLE_PAR_DEFAUT = "grille.txt";
    private final int DEFAULT_FRAME_WIDTH = 800;
    private final int DEFAULT_FRAME_HEIGHT = 600;
    private File fichierGrille = new File(GRILLE_PAR_DEFAUT);
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuJeu = new JMenu("Jeu");
    private JMenuItem mnuNouvellePartie = new JMenuItem("Nouvelle partie");
    private JMenuItem mnuChargerGrille = new JMenuItem("Charger grille");
    private JMenuItem mnuResoudre = new JMenuItem("Résoudre");
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
                int actionUtilisateur = fileChooser.showOpenDialog(FenetrePrincipale.this);

                if (actionUtilisateur == JFileChooser.APPROVE_OPTION) {
                    File fichierAOuvrir = fileChooser.getSelectedFile();
                    // FIXME: -ONLY FOR TESTING, REPLACE ASAP
                    //        -Loading the grid doesn't update ui
                    fichierGrille = fichierAOuvrir;
                    FenetrePrincipale.this.recommencerPartie();
                }
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

        this.mettreAJourInterfaceAvecNouvelleGrille();

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

    public void recommencerPartie() {
        CaseSudoku.setNbTotalCases(0);
        CaseSudoku.setNbTotalClics(0);
        jeu.lireFichier(fichierGrille);
        jeu.initialiserMatriceJeu();
        this.mettreAJourInterfaceAvecNouvelleGrille();
    }
}
