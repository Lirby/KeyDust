package keydust.gui;

import keydust.db.SqliteDB;
import keydust.gui.core.PasswordManagerGUI;

import javax.swing.*;
import java.awt.*;

public class Passwordgui extends PasswordManagerGUI {

    String[][] data = {};
    String[] columnNames = {"Description", "Username", "Password"};

    private JButton btnAddPassword;
    private JTable table;
    private JScrollPane sp;

    String password;
    SqliteDB sqlite;

    public Passwordgui(String password, SqliteDB sqlite) {
        super("Passwords",new Dimension(800,400), JFrame.EXIT_ON_CLOSE);

        this.password = password;
        this.sqlite = sqlite;

        createTable();
        initPanel();
    }

    @Override
    protected void initializeComponents() {
        createAddPasswordButton();

    }

    private void initPanel() {
        panel.setLayout(new BorderLayout());

        panel.add(btnAddPassword, BorderLayout.PAGE_START);
        panel.add(sp, BorderLayout.CENTER);
    }

    private void createTable() {
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);
    }

    private void createAddPasswordButton() {
        btnAddPassword = new JButton("Add Password");
        btnAddPassword.addActionListener(e -> {
            AddPasswordGUI password = new AddPasswordGUI(this.password, this.sqlite);
            password.setVisible(true);
        });
    }


}