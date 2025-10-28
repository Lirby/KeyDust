package keydust.gui;

import keydust.gui.core.PasswordManagerGUI;
import keydust.contollers.CreateDatabaseController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;

public class NewDBgui extends PasswordManagerGUI {

    private JButton btnDBPath;
    private JTextField txtDBName;
    private JPasswordField pwd;
    private JButton btnConfirm;

    private String path;

    public NewDBgui() {
        super("Database", new Dimension(400,150), JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    protected void initializeComponents() {
        createDBPathButton();
        createDBNameInput();
        createDBPasswordInput();
        createConfirmButton();
        initPanel();
    }

    private void initPanel() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(btnDBPath);
        panel.add(txtDBName);
        panel.add(pwd);
        panel.add(btnConfirm);
    }

    private void createConfirmButton() {
        btnConfirm = new JButton("Save");
        btnConfirm.setAlignmentX(CENTER_ALIGNMENT);

        btnConfirm.addActionListener(e -> {
            String password = new String(pwd.getPassword());
            String dbName = txtDBName.getText();

            String completePath = Paths.get(path, dbName).toString();
            System.out.println(completePath);
            //später entfernen
            System.out.println(password);

            CreateDatabaseController controller = new CreateDatabaseController();
            try {
                controller.createDatabase(completePath, password);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this,
                        "Can't create a Database: " + e1.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            dispose();
        });
    }

    private void createDBPasswordInput() {
        pwd = new JPasswordField(12);
        pwd.setMaximumSize(new Dimension(600, 20));
        pwd.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void createDBNameInput() {
        txtDBName = new JTextField(8);
        txtDBName.setMaximumSize(new Dimension(600, 20));
        txtDBName.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void createDBPathButton() {
        btnDBPath = new JButton("Select Location");
        btnDBPath.setAlignmentX(CENTER_ALIGNMENT);

        btnDBPath.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showDialog(this, "Select Save Directory") == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getAbsolutePath();
            }
        });
    }


}