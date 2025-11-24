package keydust.gui.core;

import javax.swing.*;
import java.awt.*;

public abstract class PasswordManagerGUI extends JFrame {

    protected JPanel panel;

    public PasswordManagerGUI(String title, Dimension preferredSize) {
        init(title, preferredSize);

    }



    public PasswordManagerGUI(String title, Dimension preferredSize, int preferredCloseOperation) {
        setDefaultCloseOperation(preferredCloseOperation);
        init(title, preferredSize);
    }

    protected abstract void initializeComponents();

    private void init(String title, Dimension preferredSize) {
        panel = new JPanel();

        initializeComponents();

        setContentPane(panel);
        setTitle(title);
        setPreferredSize(preferredSize);
        pack();
    }
}