package com.application.view;

import javax.swing.*;
import java.awt.*;

public class ClerkCash {
	 private JFrame frame;

	    private JLabel lblBalanceValue;
	    private JTextField txtAmountToPay;

	    private JButton btnMakePayment;
	    private JButton btnCancel;

	    private double currentBalance = 0.0;

	    public ClerkCash() {
	        initialize();
	        setBalance(1800.50);  // Dummy value for testing
	        frame.setVisible(true);
	    }

	    private void initialize() {
	        frame = new JFrame("Cash Payment");
	        frame.setSize(400, 250);
	        frame.setLocationRelativeTo(null);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	        JPanel panel = new JPanel(new GridBagLayout());
	        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(8, 8, 8, 8);
	        gbc.anchor = GridBagConstraints.WEST;
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        //Balance Label
	        gbc.gridx = 0; gbc.gridy = 0;
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

	        //Buttons
	        btnMakePayment = new JButton("Make Payment");
	        btnCancel = new JButton("Cancel");

	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(btnMakePayment);
	        buttonPanel.add(btnCancel);

	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.gridwidth = 2;
	        gbc.anchor = GridBagConstraints.CENTER;
	        panel.add(buttonPanel, gbc);

	        // Listeners
	        btnCancel.addActionListener(e -> frame.dispose());
	        btnMakePayment.addActionListener(e -> onMakePayment());

	        frame.add(panel);
	    }

	    //Set Balance 
	    public void setBalance(double amount) {
	        this.currentBalance = amount;
	        lblBalanceValue.setText(String.format("$%.2f", amount));
	    }

	    //Payment validation
	    private void onMakePayment() {
	        String amountStr = txtAmountToPay.getText().trim();

	        if (!amountStr.matches("\\d+(\\.\\d{1,2})?")) {
	            JOptionPane.showMessageDialog(frame, "Enter a valid payment amount.",
	                    "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        double amountPaid = Double.parseDouble(amountStr);

	        if (amountPaid <= 0) {
	            JOptionPane.showMessageDialog(frame, "Payment amount must be greater than zero.",
	                    "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // If paying more than the balance stops overflow
	        if (amountPaid >= currentBalance) {
	            JOptionPane.showMessageDialog(frame,
	                    "Payment Made! Full balance paid.",
	                    "Success", JOptionPane.INFORMATION_MESSAGE);
	            frame.dispose();
	            return;
	        }

	        // Partial payment
	        currentBalance -= amountPaid;
	        lblBalanceValue.setText(String.format("$%.2f", currentBalance));

	        JOptionPane.showMessageDialog(frame,
	                "Payment Made! Remaining balance: $" +
	                        String.format("%.2f", currentBalance),
	                "Success", JOptionPane.INFORMATION_MESSAGE);

	        frame.dispose();
	    }
}
