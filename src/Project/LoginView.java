package Project;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Labels
    JLabel lblEmail, lblPassword, lblTitle, lblSubtitle;
    
    // Fields
    JTextField txtEmail;
    JPasswordField txtPassword;

    // Buttons
    JButton btnLogin, btnSignup, btnClear;

    public LoginView() {
        initializeComponents();
        layoutComponents();
        setWindowProperties();

        btnLogin.addActionListener(e -> onLogin());
        btnSignup.addActionListener(e -> onSignup());
        btnClear.addActionListener(e -> clearFields());
    }

    private void initializeComponents() {
        // Title and subtitle
        lblTitle = new JLabel("Shipping Management System", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 70, 140));
        
        lblSubtitle = new JLabel("User Login", JLabel.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 100, 100));

        // Labels
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");
        
        // Style labels
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        lblEmail.setFont(labelFont);
        lblPassword.setFont(labelFont);

        // Fields
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        
        // Style fields
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        txtEmail.setFont(fieldFont);
        txtPassword.setFont(fieldFont);
        
        // Set field borders
        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        txtEmail.setBorder(fieldBorder);
        txtPassword.setBorder(fieldBorder);

        // Buttons
        btnLogin = new JButton("Login");
        btnSignup = new JButton("Create Account");
        btnClear = new JButton("Clear");
        
        // Style buttons
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        
        btnSignup.setBackground(new Color(34, 139, 34));
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setFont(new Font("Arial", Font.BOLD, 13));
        btnSignup.setFocusPainted(false);
        
        btnClear.setBackground(new Color(220, 220, 220));
        btnClear.setFont(new Font("Arial", Font.PLAIN, 12));
        btnClear.setFocusPainted(false);
    }

    private void layoutComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel with titled border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            "Login Credentials",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(70, 130, 180)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0 - Email
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        // Row 1 - Password
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignup);
        buttonPanel.add(btnClear);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void setWindowProperties() {
        setTitle("Shipping Management System - Login");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Validate fields
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both email and password.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        User loggedInUser = UserDAO.loginUser(email, HashUtil.hashPassword(password));
        if (loggedInUser != null) {
            // Clear fields on successful login
            clearFields();
            
            // Navigate to appropriate dashboard
            switch (loggedInUser.getRole().toLowerCase()) {
                case "customer":
                    new CustomerDashboard((Customer) loggedInUser);
                    break;
                case "manager":
                    new ReportGUI((Manager) loggedInUser);
                    break;
                case "clerk":
                    new ClerkDashboard((Clerk) loggedInUser);
                    break;
                case "driver":
                    new DriverDashboard((Driver) loggedInUser);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, 
                        "Unknown user role: " + loggedInUser.getRole(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
            }
            dispose(); // Close login window after successful login
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid email or password. Please try again.", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            txtPassword.selectAll();
        }
    }

    private void onSignup() {
        dispose();
        new SignupView();
    }
    
    private void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
        txtEmail.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}