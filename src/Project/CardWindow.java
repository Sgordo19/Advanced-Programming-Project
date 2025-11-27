package com.application.view;

import javax.swing.*;
import java.awt.*;

public class CardWindow extends JFrame {
    private JLabel lblBalanceValue;
    private JTextField txtAmountToPay;

    private JTextField txtHolderName;
    private JTextField txtCardNumber;
    private JTextField txtExpiryDate;
    private JPasswordField txtCVV;

    private JButton btnMakePayment;
    private JButton btnCancel;

    public CardWindow() {
        setTitle("Add Credit Card");
        setSize(450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();

        // Dummy balance value for now
        setBalance(2450.75);

        setVisible(true);
    }

    private void initComponents() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Balance Label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Outstanding Balance:"), gbc);

        gbc.gridx = 1;
        lblBalanceValue = new JLabel("$0.00");
        lblBalanceValue.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblBalanceValue, gbc);

        //Amt to be paid
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Amount to Be Paid:"), gbc);

        gbc.gridx = 1;
        txtAmountToPay = new JTextField(10);
        panel.add(txtAmountToPay, gbc);

        //Card Name
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Card Holder Name:"), gbc);

        gbc.gridx = 1;
        txtHolderName = new JTextField(10);
        panel.add(txtHolderName, gbc);

        //Card Num
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Card Number:"), gbc);

        gbc.gridx = 1;
        txtCardNumber = new JTextField(10);
        panel.add(txtCardNumber, gbc);

        // Expiry date
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Expiration Date (MM/YY):"), gbc);

        gbc.gridx = 1;
        txtExpiryDate = new JTextField(10);
        panel.add(txtExpiryDate, gbc);

        //CVV
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("CVV:"), gbc);

        gbc.gridx = 1;
        txtCVV = new JPasswordField(10);
        panel.add(txtCVV, gbc);

       
        btnMakePayment = new JButton("Make Payment");
        btnCancel = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnMakePayment);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; 
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        //Registered listeners
        btnCancel.addActionListener(e -> dispose());
        btnMakePayment.addActionListener(e -> onMakePayment());

        add(panel);
    }

    //balance setter method
    public void setBalance(double amount) {
        lblBalanceValue.setText(String.format("$%.2f", amount));
    }

    //Validation checks
    private void onMakePayment() {

        String amount = txtAmountToPay.getText().trim();
        String name = txtHolderName.getText().trim();
        String card = txtCardNumber.getText().trim();
        String expiry = txtExpiryDate.getText().trim();
        String cvv = new String(txtCVV.getPassword()).trim();

        // Validate amount
        if (!amount.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this, "Enter a valid payment amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate card fields
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Card holder name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!card.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Card number must be exactly 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Expiration date must be in MM/YY format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cvv.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "CVV must be exactly 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Payment Made!", "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();  
    }

}
