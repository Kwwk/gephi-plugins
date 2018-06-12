package view;


import core.Resources;

import javax.swing.*;
import java.awt.*;


public class SettingsPanel extends JPanel {

    private JLabel infoLabel;

    public SettingsPanel(String name) {
        initComponents(name);
    }
    private void initComponents(String name) {

        infoLabel = new JLabel();
        infoLabel.setText(name);
        add(infoLabel);

    }

}
