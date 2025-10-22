package keydust.gui;

import keydust.gui.core.PasswordManagerGUI;

import javax.swing.*;
import java.awt.*;

public class unlockgui extends PasswordManagerGUI {

    private JButton btnOpenDB;
    private JButton btnConfirm;
    private JButton btnNew;
    private JLabel lblPwd;
    private JPasswordField pwd;

    public unlockgui() {
        super("Password Manager", new Dimension(400,200));
    }

    @Override
    protected void initializeComponents() {
        createButtons();
        createPwdInput();
        initPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initPanel() {
        panel.setLayout(new GridBagLayout());
        
        placeElement(lblPwd, 1, 0, 2, null);
        placeElement(pwd, 1,1,2, null);
        placeElement(btnOpenDB, 1,2,2, new Insets(10,0,0,0));
        placeElement(btnConfirm, 1,3,1, new Insets(10,0,0,5));
        placeElement(btnNew, 2,3,1, new Insets(10,0,0,0));
    }

    private void createPwdInput() {
        lblPwd = new JLabel("Password");
        lblPwd.setHorizontalAlignment(JLabel.CENTER);
        pwd = new JPasswordField(16);
    }

    private void createButtons() {
        createOpenDBButton();
        createConfirmButton();
        createNewButton();
    }

    private void createNewButton() {
        btnNew = new JButton("New Database");
        btnNew.addActionListener(e -> {
            NewDBgui newDB = new NewDBgui();
            newDB.setVisible(true);
        });
    }

    private void createConfirmButton() {
        btnConfirm = new JButton("Open");
        btnConfirm.addActionListener(e -> {
            Passwordgui passwords = new Passwordgui();
            passwords.setVisible(true);
            dispose();
        });
    }

    private void createOpenDBButton() {
        btnOpenDB = new JButton("Select Database");
    }

    private void placeElement(JComponent component, int gridx, int gridy, int gridwidth, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;

        if (insets != null) {
            gbc.insets = insets;

        }

        panel.add(component, gbc);
    }
}
