package Project;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class SignupView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Labels
    JLabel lblUserID, lblFName, lblLName, lblPassword, lblRole, lblAddress, lblPhone, lblEmail, lblZone;
    JLabel lblTitle;

    // Fields
    JTextField txtUserID, txtFName, txtLName, txtEmail, txtAddress;
    JPasswordField txtPassword;
    JComboBox<String> cmbRole;
    JFormattedTextField txtPhone;
    JComboBox<Integer> comboZone;

    // Buttons
    JButton btnSignUp, btnCancel, btnBackToLogin;

    public SignupView() {
        initializeComponents();
        layoutComponents();
        setWindowProperties();
        
        autoPopulateUserID();

        cmbRole.addActionListener(e -> onRoleChanged());
        btnSignUp.addActionListener(e -> onSignup());
        btnCancel.addActionListener(e -> clearForm());
        btnBackToLogin.addActionListener(e -> backToLogin());
    }

    private void initializeComponents() {
        // Title
        lblTitle = new JLabel("Create New Account", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 70, 140));
        
        // Labels
        lblZone = new JLabel("Zone:");
        lblUserID = new JLabel("User ID:");
        lblFName = new JLabel("First Name:");
        lblLName = new JLabel("Last Name:");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");
        lblRole = new JLabel("Role:");
        lblAddress = new JLabel("Address:");
        lblPhone = new JLabel("Phone Number:");
        
        // Style labels
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        lblUserID.setFont(labelFont);
        lblFName.setFont(labelFont);
        lblLName.setFont(labelFont);
        lblEmail.setFont(labelFont);
        lblPassword.setFont(labelFont);
        lblRole.setFont(labelFont);
        lblAddress.setFont(labelFont);
        lblPhone.setFont(labelFont);
        lblZone.setFont(labelFont);
        
        // Fields
        txtUserID = new JTextField(20);
        txtFName = new JTextField(20);
        txtLName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtAddress = new JTextField(20);

        // Style fields
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);
        txtUserID.setFont(fieldFont);
        txtFName.setFont(fieldFont);
        txtLName.setFont(fieldFont);
        txtEmail.setFont(fieldFont);
        txtPassword.setFont(fieldFont);
        txtAddress.setFont(fieldFont);
        
        // Set field borders
        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        txtUserID.setBorder(fieldBorder);
        txtFName.setBorder(fieldBorder);
        txtLName.setBorder(fieldBorder);
        txtEmail.setBorder(fieldBorder);
        txtPassword.setBorder(fieldBorder);
        txtAddress.setBorder(fieldBorder);
       
        // Combo boxes
        String[] roles = {"Customer", "Clerk", "Driver", "Manager"};
        cmbRole = new JComboBox<>(roles);
        cmbRole.setFont(fieldFont);
        cmbRole.setBackground(Color.WHITE);
        
        comboZone = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        comboZone.setFont(fieldFont);
        comboZone.setBackground(Color.WHITE);
        
        // Buttons
        btnSignUp = new JButton("Create Account");
        btnCancel = new JButton("Clear Form");
        btnBackToLogin = new JButton("Back to Login");
        
        // Style buttons
        btnSignUp.setBackground(new Color(70, 130, 180));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFont(new Font("Arial", Font.BOLD, 13));
        btnSignUp.setFocusPainted(false);
        
        btnCancel.setBackground(new Color(220, 220, 220));
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancel.setFocusPainted(false);
        
        btnBackToLogin.setBackground(new Color(240, 240, 240));
        btnBackToLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBackToLogin.setFocusPainted(false);
        
        // Read only
        txtUserID.setEditable(false);
        txtUserID.setForeground(new Color(0, 100, 200));
        txtUserID.setBackground(new Color(240, 245, 255));
        
        // Phone number formatting
        try {
            MaskFormatter phoneMask = new MaskFormatter("(876)###-####");
            phoneMask.setPlaceholderCharacter('_');
            txtPhone = new JFormattedTextField(phoneMask);
            txtPhone.setColumns(20);
            txtPhone.setFont(fieldFont);
            txtPhone.setBorder(fieldBorder);
        } catch (Exception e) {
            txtPhone = new JFormattedTextField();
            txtPhone.setColumns(20);
            txtPhone.setFont(fieldFont);
            txtPhone.setBorder(fieldBorder);
        }
    }

    private void layoutComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel with titled border
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            "Account Information",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(70, 130, 180)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0 - User ID
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblUserID, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUserID, gbc);

        // Row 1 - First Name
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblFName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtFName, gbc);

        // Row 2 - Last Name
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblLName, gbc);
        gbc.gridx = 1;
        formPanel.add(txtLName, gbc);

        // Row 3 - Email
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        // Row 4 - Password
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Row 5 - Role
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblRole, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbRole, gbc);

        // Row 6 - Address
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblAddress, gbc);
        gbc.gridx = 1;
        formPanel.add(txtAddress, gbc);

        // Row 7 - Zone
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblZone, gbc);
        gbc.gridx = 1;
        formPanel.add(comboZone, gbc);

        // Row 8 - Phone
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblPhone, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void setWindowProperties() {
        setTitle("User Registration - Shipping Management System");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void autoPopulateUserID() {
        String selectedRole = (String) cmbRole.getSelectedItem();
        if (selectedRole == null || selectedRole.isEmpty()) return;

        // Get next sequence from DB
        int nextSeq = User.getNextUserSequence();

        // Generate ID using user class logic
        String newUserID = User.generateUserID(nextSeq, selectedRole);

        txtUserID.setText(newUserID);
    }

    private void onRoleChanged() {
        autoPopulateUserID();
    }

    private void onSignup() {
        // Validate required fields
        if (!validateForm()) {
            return;
        }

        // Collect data from form
        User u = new User();
        u.setFirstName(txtFName.getText().trim());
        u.setLastName(txtLName.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setPassword(HashUtil.hashPassword(new String(txtPassword.getPassword())));
        u.setRole((String) cmbRole.getSelectedItem());
        
        // Create Address object
        Address addr = new Address();
        addr.setAddress(txtAddress.getText().trim());

        // Get selected zone number
        addr.setZone(Zone.getZoneById((Integer) comboZone.getSelectedItem()));
        u.setAddress(addr);
        
        u.setPhoneNumber(txtPhone.getText().trim());

        int seq = User.getNextUserSequence();
        u.setUserID(User.generateUserID(seq, (String) cmbRole.getSelectedItem()));  

        // Save to database
        boolean saved = UserDAO.saveUser(u);

        if (saved) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\n\n" +
                "Your User ID: " + u.getUserID() + "\n" +
                "Role: " + u.getRole(),
                "Registration Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to create account. Please try again.\n" +
                "Possible reasons:\n" +
                "- Email already exists\n" +
                "- Database connection issue", 
                "Registration Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        // Check required fields
        if (txtFName.getText().trim().isEmpty()) {
            showValidationError("Please enter your first name.");
            txtFName.requestFocus();
            return false;
        }
        
        if (txtLName.getText().trim().isEmpty()) {
            showValidationError("Please enter your last name.");
            txtLName.requestFocus();
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            showValidationError("Please enter your email address.");
            txtEmail.requestFocus();
            return false;
        }
        
        if (txtPassword.getPassword().length == 0) {
            showValidationError("Please enter a password.");
            txtPassword.requestFocus();
            return false;
        }
        
        if (txtPassword.getPassword().length < 6) {
            showValidationError("Password must be at least 6 characters long.");
            txtPassword.requestFocus();
            return false;
        }
        
        if (txtAddress.getText().trim().isEmpty()) {
            showValidationError("Please enter your address.");
            txtAddress.requestFocus();
            return false;
        }
        
        // Validate phone number format
        String phone = txtPhone.getText().trim();
        if (phone.contains("_")) {
            showValidationError("Please enter a complete phone number.");
            txtPhone.requestFocus();
            return false;
        }
        
        // Validate email format
        String email = txtEmail.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showValidationError("Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void clearForm() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all fields?", 
            "Clear Form", 
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            txtFName.setText("");
            txtLName.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            txtAddress.setText("");
            txtPhone.setText("");
            cmbRole.setSelectedIndex(0);
            comboZone.setSelectedIndex(0);
            autoPopulateUserID();
            txtFName.requestFocus();
        }
    }

    private void backToLogin() {
        dispose();
        new LoginView();
    }
    
    // Simplified main method without the problematic UIManager call
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignupView());
    }
}