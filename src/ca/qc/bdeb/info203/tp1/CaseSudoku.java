package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CaseSudoku extends JButton {
    private static int nbTotalClics = 0;
    private int valeurMax;
    private int valeurCase;
    private boolean estCliquable;
    private Color coueurParDefaut = Color.WHITE;

    public CaseSudoku(int valeurMax) {
        this.valeurMax = valeurMax;
        this.valeurCase = 0;
        this.estCliquable = true;
        this.setBackground(coueurParDefaut);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incrementerValeur();
            }
        });
    }

    public CaseSudoku(int valeurMax, int valeurCase) {
        super("" + valeurCase);
        this.valeurCase = valeurCase;
        this.estCliquable = false;
        this.setEnabled(false);
    }

    public void incrementerValeur() {
        nbTotalClics++;
        System.out.println(nbTotalClics);
        // Le modulo + 1 permet de toujours gader la valeur de la case entre 1 et valeurMax
        valeurCase = (valeurCase % valeurMax) + 1;
        this.setText("" + valeurCase);
        this.invalidate();
        this.repaint();
    }

    public static int getNbTotalClics() {
        return nbTotalClics;
    }
}
