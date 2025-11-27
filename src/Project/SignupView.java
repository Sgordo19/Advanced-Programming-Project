package Project;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class SignupView extends JFrame {

    private static final long serialVersionUID = 1L;

    // Labels
    JLabel lblUserID, lblFName, lblLName, lblPassword, lblRole, lblAddress, lblPhone, lblEmail, lblZone;

    // Fields
    JTextField txtUserID, txtFName, txtLName, txtEmail, txtAddress;
    JPasswordField txtPassword;
    JComboBox<String> cmbRole;
    JFormattedTextField txtPhone;
    JComboBox<Integer> comboZone;

    // Buttons
    JButton btnSignUp, btnCancel;

    public SignupView() {
        initializeComponents();
        layoutComponents();
        setWindowProperties();
        
        autoPopulateUserID();

        cmbRole.addActionListener(e -> onRoleChanged());
        btnSignUp.addActionListener(e -> onSignup());
        btnCancel.addActionListener(e -> onCancel());
    }

    private void initializeComponents() {
    	//Labels
    	lblZone = new JLabel("Zone:");
        lblUserID = new JLabel("User ID:");
        lblFName = new JLabel("First Name:");
        lblLName = new JLabel("Last Name:");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Password:");
        lblRole = new JLabel("Role:");
        lblAddress = new JLabel("Address:");
        lblPhone = new JLabel("Phone Number:");
        
        
        
        //Fields
        txtUserID = new JTextField(20);
        txtFName = new JTextField(20);
        txtLName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtAddress = new JTextField(20);

       
        //Combo boxes
        String[] roles = {"Customer", "Clerk", "Driver", "Manager"};
        cmbRole = new JComboBox<>(roles);
        comboZone = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        
        //Buttons
        btnSignUp = new JButton("Sign Up");
        btnCancel = new JButton("Cancel");
        
        //read only
        txtUserID.setEditable(false);
        txtUserID.setForeground(Color.BLUE);
        
        //Phone number formatting
        try {
            MaskFormatter phoneMask = new MaskFormatter("(876)###-####");
            phoneMask.setPlaceholderCharacter('_');
            txtPhone = new JFormattedTextField(phoneMask);
            txtPhone.setColumns(20);
        } catch (Exception e) {
            txtPhone = new JFormattedTextField();
            txtPhone.setColumns(20);
        }
        
        

        autoPopulateUserID();
    }

    private void layoutComponents() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0 - User ID
        gbc.gridx = 0; gbc.gridy = y;
        add(lblUserID, gbc);
        gbc.gridx = 1;
        add(txtUserID, gbc);

        // Row 1 - First Name
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblFName, gbc);
        gbc.gridx = 1;
        add(txtFName, gbc);

        // Row 2 - Last Name
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblLName, gbc);
        gbc.gridx = 1;
        add(txtLName, gbc);

        // Row 3 - Email
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblEmail, gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        // Row 4 - Password
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Row 5 - Role
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblRole, gbc);
        gbc.gridx = 1;
        add(cmbRole, gbc);

        // Row 6 - Address
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblAddress, gbc);
        gbc.gridx = 1;
        add(txtAddress, gbc);

        // Row 7 - Zone
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblZone, gbc);
        gbc.gridx = 1;
        add(comboZone, gbc);

        // Row 8 - Phone
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(lblPhone, gbc);
        gbc.gridx = 1;
        add(txtPhone, gbc);

        // Row 9 - Buttons
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        add(btnSignUp, gbc);
        gbc.gridx = 1;
        add(btnCancel, gbc);
    }

 
    private void setWindowProperties() {
        setTitle("User Signup");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        
        int seq =  User.getNextUserSequence();
        u.setUserID(User.generateUserID(seq, (String) cmbRole.getSelectedItem()));  

        //Save to database
        boolean saved = UserDAO.saveUser(u);

        if (saved) {
            JOptionPane.showMessageDialog(this, "User successfully registered!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register user.");
        }
    }

    private void onCancel() {
        dispose();
    }
}
