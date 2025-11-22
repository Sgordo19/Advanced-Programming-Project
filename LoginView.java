package Project;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Labels
    JLabel lblEmail, lblPassword;

    // Fields
    JTextField txtEmail;
    JPasswordField txtPassword;

    // Buttons
    JButton btnLogin, btnSignup;

    public LoginView() {
        initializeComponents();
        layoutComponents();
        setWindowProperties();

        btnLogin.addActionListener(e -> onLogin());
        btnSignup.addActionListener(e -> onSignup());
    }

    private void initializeComponents() {
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");

        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);

        btnLogin = new JButton("Login");
        btnSignup = new JButton("Signup");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0 - Email
        gbc.gridx = 0; gbc.gridy = y;
        add(lblEmail, gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        // Row 1 - Password
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Row 2 - Buttons
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(btnLogin, gbc);
        gbc.gridx = 1;
        add(btnSignup, gbc);
    }

    private void setWindowProperties() {
        setTitle("User Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        User loggedInUser = UserDAO.loginUser(email, HashUtil.hashPassword(password));
        if (loggedInUser != null) {
            switch (loggedInUser.getRole().toLowerCase()) {
                case "customer":
                    new CustomerDashboard((Customer) loggedInUser); // safe now
                    break;
                case "manager":
                   // new ManagerDashboard((Manager) loggedInUser);
                    break;
                case "clerk":
                  //  new ClerkDashboard((Clerk) loggedInUser);
                    break;
                case "driver":
                  //  new DriverDashboard((Driver) loggedInUser);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }



    private void onSignup() {
    	dispose();
    	new SignupView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}
