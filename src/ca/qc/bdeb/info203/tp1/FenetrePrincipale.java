package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class FenetrePrincipale extends JFrame {
    private final int DEFAULT_FRAME_WIDTH = 800;
    private final int DEFAULT_FRAME_HEIGHT = 600;
    private static int TAILE_GRILLE = 4;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuGrille = new JMenu("Grille");
    private JMenuItem mnuChargerGrille = new JMenuItem("Charger grille");
    private ConteneurGrille pnlJeu;

    // TODO: Add Javadoc

    public FenetrePrincipale() {
        mnuGrille.add(mnuChargerGrille);
        menuBar.add(mnuGrille);

        // Kinda awkward
        pnlJeu = new ConteneurGrille(TAILE_GRILLE);
        pnlJeu.populerGrille(genererGrilleRandom());

        this.setJMenuBar(menuBar);
        this.setContentPane(pnlJeu);
        this.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Udoku");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // FIXME: For testing only, doesn't generate valid grids
    public ArrayList<Character> genererGrilleRandom() {
        ArrayList<Character> grille = new ArrayList<>();
        Random r = new Random();
        // Make this adaptable to any grid size
        char[] chars = {' ', '1', '2', '3', '4'};

        for (int i = 0; i < TAILE_GRILLE*TAILE_GRILLE; i++) {
            grille.add(chars[r.nextInt(chars.length)]);
        }

        return grille;
    }

    public static int getTaileGrille() {
        return TAILE_GRILLE;
    }
}
