package Project;

import java.awt.BorderLayout;

import javax.swing.*;
import java.awt.*;

public class CheckoutView{
	private JFrame main; 
	private JPanel mainPanel; 
	private JPanel mainLeft; 
	private JPanel mainRight; 
	private JPanel costInfo; 
	
	
	
	private JLabel checkout;
	private JLabel delivering;
	private JLabel packageInfo;
	private JLabel height;
	private JLabel width;
	private JLabel length;
	private JLabel weight; 
	//labels for cost section 
	private JLabel preCost;
	private JLabel addCosts;
	private JLabel orderTotal; 
	private JLabel paymentMethod;
	
	//separator 
	private JSeparator panelSeparator;
	private JSeparator packageSeparator;
	
	//buttons 
	private JRadioButton cash;
	private JRadioButton card; 
	private ButtonGroup payment;
	private JButton makePayment; 
	private JButton cancelPayment; 
	
	Invoice invoice = new Invoice();
	
	JTextField txtRName, txtRAddress, txtPHeight, txtPWeight, txtPLength, txtPWidth, 
				txtPBCost, txtPACost, txtPTCost;
	
	
	public CheckoutView() {
		initialize();
	}
	
	private void initialize() {
		setFrameProperties();
		initialComponents();
		addComponentsToFrame();
		addComponentsToPanel();
		
	}
	
	private void initialComponents() {
		
		mainPanel= new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		mainLeft= new JPanel();
		mainLeft.setLayout(new BoxLayout(mainLeft,BoxLayout.PAGE_AXIS));
		mainLeft.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		mainRight= new JPanel();
		mainRight.setLayout(new BoxLayout(mainRight,BoxLayout.PAGE_AXIS));
		mainRight.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		costInfo= new JPanel();
		costInfo.setLayout(new BoxLayout(costInfo,BoxLayout.PAGE_AXIS));
		costInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		checkout= new JLabel("Checkout");
		delivering= new JLabel("Delivering to: ");
		txtRName = new JTextField();
		txtRAddress = new JTextField();
		packageInfo= new JLabel("Package");
		height= new JLabel("height");
		txtPHeight = new JTextField();
		width= new JLabel("width");
		txtPWidth = new JTextField();
		length= new JLabel("length");
		txtPLength = new JTextField();
		weight= new JLabel("Weight");
		txtPWeight = new JTextField();
		
		preCost= new JLabel("Base Cost: ");
		txtPBCost = new JTextField();
		addCosts= new JLabel("Discount or Surplus: ");
		txtPACost = new JTextField();
		orderTotal= new JLabel("Order Total: ");
		txtPTCost = new JTextField();
		paymentMethod= new JLabel("Payment Method ");
		
		panelSeparator= new JSeparator(SwingConstants.VERTICAL);
		panelSeparator.setMaximumSize(new Dimension(5, Integer.MAX_VALUE));
		
		packageSeparator= new JSeparator(SwingConstants.HORIZONTAL);
		
		cash= new JRadioButton("Cash");
		card= new JRadioButton("Card");
		
		payment= new ButtonGroup();
		payment.add(cash);
		payment.add(card);
		
		makePayment= new JButton("Make Payment");
		cancelPayment= new JButton("Cancel Payment");
		
	}
	
	private void setFrameProperties() {
		main= new JFrame("Checkout");
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		main.setSize(800,500);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
		
	}
	
	private void addComponentsToFrame() {
		main.add(checkout,BorderLayout.NORTH);
		main.add(mainPanel,BorderLayout.CENTER);
	}
	
	private void addComponentsToPanel() {
		mainPanel.add(mainLeft);
		mainPanel.add(panelSeparator);
		mainPanel.add(mainRight);
		
		mainLeft.add(delivering);
		mainLeft.add(txtRName);
		mainLeft.add(txtRAddress);
		mainLeft.add(packageSeparator);
		mainLeft.add(packageInfo);
		mainLeft.add(height);
		mainLeft.add(txtPHeight);
		mainLeft.add(length);
		mainLeft.add(txtPLength);
		mainLeft.add(width);
		mainLeft.add(txtPWidth);
		mainLeft.add(weight);
		mainLeft.add(txtPWeight);
		
		mainRight.add(costInfo);
		mainRight.add(paymentMethod);
		mainRight.add(cash);
		mainRight.add(card);
		mainRight.add(makePayment);
		mainRight.add(cancelPayment);
		
		costInfo.add(preCost);
		costInfo.add(txtPBCost);
		costInfo.add(addCosts);
		costInfo.add(txtPACost);
		costInfo.add(orderTotal);
		costInfo.add(txtPTCost);
	}
	
	
	
}