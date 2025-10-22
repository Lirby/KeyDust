package keydust.gui;

import keydust.gui.core.PasswordManagerGUI;

import javax.swing.*;
import java.awt.*;

public class NewDBgui extends PasswordManagerGUI {

    private JButton btnDBPath;
    private JTextField txtDBName;
    private JPasswordField pwd;
    private JButton btnConfirm;

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
    }


}
