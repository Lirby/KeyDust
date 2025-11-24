package keydust.gui;

import keydust.contollers.ShowPwdController;
import keydust.db.SqliteDB;
import keydust.gui.core.PasswordManagerGUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

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

        ShowPwdController controller = new ShowPwdController(password, sqlite);
        try {
            data = controller.loadPasswords();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        btnAddPassword = new JButton("Add a new Password");
        btnAddPassword.addActionListener(e -> {
            AddPasswordGUI addPasswordWindow = new AddPasswordGUI(this.password, this.sqlite, this);
            addPasswordWindow.setVisible(true);
        });
    }

    public void refreshTable() {
        ShowPwdController controller = new ShowPwdController(password, sqlite);
        try {
            data = controller.loadPasswords();
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            table.revalidate();
            table.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Could not reload password:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}