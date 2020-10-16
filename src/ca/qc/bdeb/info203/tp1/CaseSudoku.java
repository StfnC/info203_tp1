package ca.qc.bdeb.info203.tp1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CaseSudoku extends JButton {
    private int valeurCase;
    private int nbClics;
    private boolean estCliquable;
    private Color coueurParDefaut = Color.WHITE;

    public CaseSudoku() {
        this.valeurCase = 0;
        this.nbClics = 0;
        this.estCliquable = true;
        this.setBackground(coueurParDefaut);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incrementerValeur();
            }
        });
    }

    public CaseSudoku(int valeurCase) {
        super("" + valeurCase);
        this.valeurCase = valeurCase;
        this.estCliquable = false;
        this.setEnabled(false);
    }

    public void incrementerValeur() {
        nbClics++;
        // Le modulo + 1 permet de toujours gader la valeur de la case entre 1 et 4
        valeurCase = (valeurCase % 4) + 1;
        this.setText("" + valeurCase);
        this.invalidate();
        this.repaint();
    }
}
