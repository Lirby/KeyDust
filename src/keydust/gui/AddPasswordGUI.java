package keydust.gui;

import keydust.contollers.AddPwdController;
import keydust.db.SqliteDB;
import keydust.gui.core.PasswordManagerGUI;
import keydust.passwordmanager.Password;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddPasswordGUI extends PasswordManagerGUI {

    private JTextField txtDesription;
    private JTextField txtUsername;
    private JPasswordField pwd;
    private JButton save;

    String password;
    SqliteDB sqlite;

    private Passwordgui parent;

    public AddPasswordGUI(String password, SqliteDB sqlite, Passwordgui parent) {
        super("Add Password", new Dimension(300,200), JFrame.DISPOSE_ON_CLOSE);

        this.password = password;
        this.sqlite = sqlite;
        this.parent = parent;


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
        save.addActionListener(e -> {
            AddPwdController controller = new AddPwdController(this.password, this.sqlite);

            String description = txtDesription.getText();
            String username = txtUsername.getText();
            String password = pwd.getText();

            try {
                controller.savePassword(description, username, password);

                if (parent != null) {
                    parent.refreshTable();
                }

                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Could, not add credential: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}