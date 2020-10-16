package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ConteneurGrille extends JPanel {
    private static final int ESPACE_HORIZONTAL = 5;
    private static final int ESPACE_VERTICAL = 5;
    private ArrayList<Component> listeElementsGrille = new ArrayList<>();

    public ConteneurGrille(int nbLignes, int nbColonnes) {
        super(new GridLayout(nbLignes, nbColonnes, ESPACE_HORIZONTAL, ESPACE_VERTICAL));
        Random r = new Random();
        // Rajoute un peu de couleur, plus amusant
        this.setBackground(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
    }

    public void ajouterComposanteDansGrille(Component component) {
        listeElementsGrille.add(component);
        this.add(component);
    }
}
