package ca.qc.bdeb.info203.tp1;

import javax.swing.*;

public class FenetrePrincipale extends JFrame {
    private final int DEFAULT_FRAME_WIDTH = 800;
    private final int DEFAULT_FRAME_HEIGHT = 600;
    private int tailleGrille;
    private int nbBlocks;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuGrille = new JMenu("Grille");
    private JMenuItem mnuChargerGrille = new JMenuItem("Charger grille");
    private ConteneurGrille pnlJeu;

    public FenetrePrincipale(int tailleGrille) {
        this.tailleGrille = tailleGrille;
        this.nbBlocks = (int) Math.sqrt(tailleGrille);

        mnuGrille.add(mnuChargerGrille);
        menuBar.add(mnuGrille);

        pnlJeu = new ConteneurGrille(nbBlocks, nbBlocks);
        for (int i = 0; i < tailleGrille; i++) {
            pnlJeu.ajouterComposanteDansGrille(creerBlock());
        }

        this.setJMenuBar(menuBar);
        this.setContentPane(pnlJeu);
        this.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Udoku");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public ConteneurGrille creerBlock() {
        ConteneurGrille block = new ConteneurGrille(nbBlocks, nbBlocks);
        for (int i = 0; i < tailleGrille; i++) {
            block.ajouterComposanteDansGrille(new CaseSudoku(4));
        }
        return block;
    }
}
