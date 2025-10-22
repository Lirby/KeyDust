package keydust.gui;

import keydust.gui.core.PasswordManagerGUI;

import javax.swing.*;
import java.awt.*;

public class AddPasswordGUI extends PasswordManagerGUI {

    private JTextField txtDesription;
    private JTextField txtUsername;
    private JPasswordField pwd;
    private JButton save;

    public AddPasswordGUI() {
        super("Add Password", new Dimension(300,200), JFrame.DISPOSE_ON_CLOSE);


    }

    @Override
    protected void initializeComponents() {
        createComponents();
        initPanel();
    }

    private void initPanel() {
        panel.setLayout(new GridLayout(4, 0));

        panel.add(new JLabel("Description:"));
        panel.add(txtDesription);
        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:"));
        panel.add(pwd);
        panel.add(save);
    }

    private void createComponents() {
        txtDesription = new JTextField();
        txtUsername = new JTextField();
        pwd = new JPasswordField();
        save = new JButton("Add Password");
    }
}
