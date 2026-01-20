package com.banking.ui;

import com.banking.BankingSystem;
import com.banking.Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class CreateAccountDialog extends JDialog {

    private final BankingSystem bankingSystem;

    private JTextField usernameField;
    private JTextField holderNameField;
    private JTextField balanceField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;

    private JLabel usernameError;
    private JLabel holderError;
    private JLabel balanceError;
    private JLabel pinError;

    private JButton createButton;

    public CreateAccountDialog(JFrame parent) {
        super(parent, "Create Account", true);
        this.bankingSystem = Main.bankingSystem;
        initUI();
    }

    private void initUI() {
        setSize(600, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(Theme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(root);

        root.add(createForm(), BorderLayout.CENTER);
        root.add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= FORM ================= */

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD_COLOR);

        GridBagConstraints gbc = baseGbc();

        usernameField = field(form, gbc, "Username:", 0);
        usernameError = error(form, gbc, 1);

        holderNameField = field(form, gbc, "Holder Name:", 2);
        holderError = error(form, gbc, 3);

        balanceField = field(form, gbc, "Initial Balance:", 4);
        balanceError = error(form, gbc, 5);

        pinField = password(form, gbc, "PIN:", 6);
        confirmPinField = password(form, gbc, "Confirm PIN:", 7);
        pinError = error(form, gbc, 8);

        attachValidators();

        return form;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private JTextField field(JPanel p, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(styledLabel(label), gbc);

        gbc.gridx = 1;
        JTextField f = new JTextField(22);
        styleField(f);
        p.add(f, gbc);
        return f;
    }

    private JPasswordField password(JPanel p, GridBagConstraints gbc, String label, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(styledLabel(label), gbc);

        gbc.gridx = 1;
        JPasswordField f = new JPasswordField(22);
        styleField(f);
        p.add(f, gbc);
        return f;
    }

    private JLabel error(JPanel p, GridBagConstraints gbc, int row) {
        gbc.gridx = 1;
        gbc.gridy = row;
        JLabel e = new JLabel(" ");
        e.setFont(Theme.BODY_FONT);
        e.setForeground(Color.RED);
        p.add(e, gbc);
        return e;
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel.setBackground(Theme.BACKGROUND_COLOR);

        createButton = primaryButton("Create Account");
        createButton.setEnabled(false);
        Theme.addHoverEffect(
                createButton,
                Theme.BUTTON_PRIMARY,
                Theme.BUTTON_PRIMARY_HOVER
        );
        createButton.addActionListener(e -> createAccount());

        JButton cancel = primaryButton("Cancel");
        Theme.addHoverEffect(
                cancel,
                Theme.BUTTON_SECONDARY,
                Theme.BUTTON_SECONDARY_HOVER
        );
        cancel.addActionListener(e -> dispose());

        panel.add(createButton);
        panel.add(cancel);
        return panel;
    }


    /* ================= VALIDATION ================= */

    private void attachValidators() {
        usernameField.getDocument().addDocumentListener(simple(() -> {
            String v = usernameField.getText().trim();
            if (v.isEmpty()) error(usernameError, "Username required");
            else if (bankingSystem.usernameExists(v)) error(usernameError, "Username exists");
            else clear(usernameError);
        }));

        holderNameField.getDocument().addDocumentListener(simple(() -> {
            if (holderNameField.getText().trim().isEmpty()) {
                error(holderError, "Holder name required");
            } else {
                clear(holderError);
            }
        }));


        balanceField.getDocument().addDocumentListener(simple(() -> {
            try {
                double b = Double.parseDouble(balanceField.getText().trim());
                if (b < 0) error(balanceError, "Negative balance");
                else clear(balanceError);
            } catch (Exception e) {
                error(balanceError, "Invalid number");
            }
        }));

        DocumentListener pinListener = simple(() -> {
            String p1 = new String(pinField.getPassword());
            String p2 = new String(confirmPinField.getPassword());
            if (p1.isEmpty()) error(pinError, "PIN required");
            else if (!p1.equals(p2)) error(pinError, "PINs do not match");
            else clear(pinError);
        });

        pinField.getDocument().addDocumentListener(pinListener);
        confirmPinField.getDocument().addDocumentListener(pinListener);
    }

    private void validateForm() {
        createButton.setEnabled(
                isClear(usernameError) &&
                        isClear(holderError) &&
                        isClear(balanceError) &&
                        isClear(pinError)
        );
    }

    /* ================= ACTION ================= */

    private void createAccount() {
        try {
            String accNo = bankingSystem.createAccount(
                    usernameField.getText().trim(),
                    holderNameField.getText().trim(),
                    Double.parseDouble(balanceField.getText().trim()),
                    new String(pinField.getPassword())
            );

            JOptionPane.showMessageDialog(this,
                    "Account Created\nAccount Number: " + accNo,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ================= HELPERS ================= */

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.SUBHEADER_FONT);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(Theme.BODY_FONT);
        f.setBackground(Theme.BACKGROUND_COLOR);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(Theme.SUBHEADER_FONT);
        b.setBackground(Theme.BUTTON_PRIMARY);
        b.setForeground(Theme.BUTTON_TEXT);
        b.setFocusPainted(false);
        return b;
    }

    private DocumentListener simple(Runnable r) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { r.run(); validateForm(); }
            public void removeUpdate(DocumentEvent e) { r.run(); validateForm(); }
            public void changedUpdate(DocumentEvent e) {}
        };
    }

    private void error(JLabel l, String msg) { l.setText(msg); }
    private void clear(JLabel l) { l.setText(" "); }
    private boolean isClear(JLabel l) { return l.getText().equals(" "); }
}
