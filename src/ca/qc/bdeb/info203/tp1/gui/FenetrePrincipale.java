package ca.qc.bdeb.info203.tp1.gui;

import ca.qc.bdeb.info203.tp1.jeu.Jeu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FenetrePrincipale extends JFrame {
    private final String GRILLE_PAR_DEFAUT = "grille.txt";
    private final int DEFAULT_FRAME_WIDTH = 800;
    private final int DEFAULT_FRAME_HEIGHT = 600;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuGrille = new JMenu("Grille");
    private JMenuItem mnuChargerGrille = new JMenuItem("Charger grille");
    private JMenuItem mnuResoudre = new JMenuItem("Résoudre");
    private ConteneurGrille pnlJeu;
    private JFileChooser fileChooser = new JFileChooser();
    private Jeu jeu;

    // TODO: Add label that shows current number of moves
    // TODO: Add Javadoc

    public FenetrePrincipale() {
        // TODO: -AJOUTER FILE NOT FOUND EXCEPTIONS
        //       -PROMPT SOMETHING TO CHOOSE A FILE IF FileNotFound, make sure app doesn't crash if a bad file type/wrong grid format is loaded
        jeu = new Jeu(new File(GRILLE_PAR_DEFAUT));

        mnuChargerGrille.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int actionUtilisateur = fileChooser.showOpenDialog(FenetrePrincipale.this);

                if (actionUtilisateur == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();
                    // FIXME: -ONLY FOR TESTING, REPLACE ASAP
                    //        -Loading the grid doesn't update ui
                    jeu.lireFichier(fileToOpen);
                    jeu.initialiserMatriceJeu();
                    FenetrePrincipale.this.mettreAJourInterfaceAvecNouvelleGrille();
                }
            }
        });
        mnuGrille.add(mnuChargerGrille);
        mnuResoudre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.resoudre();
            }
        });
        mnuGrille.add(mnuResoudre);
        menuBar.add(mnuGrille);

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
}
