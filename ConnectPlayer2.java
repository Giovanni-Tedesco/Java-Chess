import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ConnectPlayer2 implements ActionListener {

	//Properties
	//variables for labels
	private String strName = "Enter Name: "; //asking user to input name
	private String strIp = "Server IP: "; //show user ip address
	private String strPort = "Server Port: "; //show user port number
	private String strEnterPort = "Enter Server Port:"; //ask client for server ip address
	private String strEnterIP = "Enter Server IP:"; //ask client for server port number
	private String strServerInfo1 = "Please tell the other player to enter the IP address and the port number displayed here."; //labels to show server user what to do
	private String strServerInfo2 = "Make sure to start the game here before the other player does.";
	private String strClientInfo1 = "Please enter the IP address and the port number displayed on the host screen."; //labels to show client what to do
	private String strClientInfo2 = "Wait till the host has started the game to join.";
	private String strUserName = "", strPortNumber = "", strIpAddress = ""; //Username, port number and ip address

	private boolean blnServer, blnWrongPort = false, blnError; //declare if user is server, declare if port number is correct, variable to check if the server and port match for connections
	private JPanel connectPanel2 = new JPanel(null); //jpanel

	private JButton backButton = new JButton("BACK"); //back button
	private JButton startServerButton = new JButton("START SERVER"); //start server
	private JButton joinServerButton = new JButton("CONNECT TO SERVER"); //connect to server

	//labels for UI
	private JLabel nameLabel = new JLabel(strName); //display name
	private JLabel ipLabel = new JLabel(strIp); //ip
	private JLabel portLabel = new JLabel(strPort); //port number
	private JLabel enterPortLabel = new JLabel(strEnterPort); //choose port number for client
	private JLabel enterIpLabel = new JLabel(strEnterIP); //choose ip address
	private JLabel infoLabel1 = new JLabel(); //info labels
	private JLabel infoLabel2 = new JLabel();
	private JLabel errorLabel = new JLabel(); //display error info

	//text fields for input
	private JTextField portField = new JTextField(); //port number
	private JTextField ipField = new JTextField(); //ip address
	private JTextField nameField = new JTextField(); //name of player

	//Methods
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object event = evt.getSource();
		if (event == backButton) { //if back button is pressed
			Utility.changePanel(new ConnectPlayer().getConnectPanel()); //change panel to connection page
		} else if (event == startServerButton) { //host game/start server
			strUserName = nameField.getText(); //get and display username
			strUserName = (strUserName == null || strUserName.equals("")) ? Utility.getRandomName() : strUserName;
			System.out.println("NAME: " + strUserName);
			Utility.changePanel(new ChessGame(strUserName).getChessPanel()); //change panel to chess board
		} else if (event == joinServerButton) { //join game/join server
			strUserName = nameField.getText(); //get and display username
			strUserName = (strUserName == null || strUserName.equals("")) ? Utility.getRandomName() : strUserName;
			System.out.println("NAME: " + strUserName);
			int intPortNumber = 0; //set default port number

			//get inputted text for ip address and port number
			strIpAddress = ipField.getText();
			strPortNumber = portField.getText();

			//if user enters invalid input, tell them to enter again
			try {
				intPortNumber = Integer.parseInt(strPortNumber);
				blnWrongPort = false;
			} catch (NumberFormatException exc) {
				errorLabel.setVisible(true);
				blnWrongPort = true;
				errorLabel.setText("Please enter a port number");
				Timer timer = new Timer(3000, e -> errorLabel.setVisible(false));
				timer.setRepeats(false);
				timer.start();
			}

			//if port number is correct
			if (!blnWrongPort) {
				ChessGame chess = new ChessGame(strUserName, strIpAddress, intPortNumber);
				//if connection failed, send error message
				if (chess.connectionFailed()) {
					errorLabel.setVisible(true);
					errorLabel.setText("Failed to connect");
					Timer timer = new Timer(3000, e -> errorLabel.setVisible(false));
					timer.setRepeats(false);
					timer.start();
					System.out.println("THE ERROR");
				} else { //else go to chess panel
					Utility.changePanel(chess.getChessPanel());
				}
			}

		}
	}
	//Constructor
	public ConnectPlayer2(boolean blnIsServer, boolean blnError) {
		//set size and color of connectionpanel
		connectPanel2.setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
		connectPanel2.setPreferredSize(Utility.panelDimensions);
		blnServer = blnIsServer; //determine whether or not user is client or server
		int intPort = Settings.getPortNumber(); //get port number from settings

		//initialize the JComponents for UI
		initializeButtons();
		initializeLabels();
		initializeFields();

		if (blnServer) { //if user is the server
			SuperSocketMaster temp = new SuperSocketMaster(intPort, this); //set up supersocketmaster
			//set labels to their corresponding information
			ipLabel.setText(strIp + temp.getMyAddress()); //ip address
			portLabel.setText(strPort + intPort); //port number
			infoLabel1.setText(strServerInfo1); //information labels
			infoLabel2.setText(strServerInfo2);

			//add labels and buttons to connect panel
			connectPanel2.add(ipLabel);
			connectPanel2.add(portLabel);
			connectPanel2.add(startServerButton);
		} else { //if user is the client
			infoLabel1.setText(strClientInfo1); //set info labels for client
			infoLabel2.setText(strClientInfo2);

			//add labels and buttons to panel
			connectPanel2.add(enterIpLabel);
			connectPanel2.add(enterPortLabel);
			connectPanel2.add(ipField);
			connectPanel2.add(portField);
			connectPanel2.add(joinServerButton);
		}

		if (blnError) { //if there is an error, display error label
			errorLabel.setForeground(Color.RED);
			errorLabel.setVisible(true);
			errorLabel.setText("Failed to connect");
			System.out.println("THE ERROR");
		}
		//add remaining labels and buttons to panel
		connectPanel2.add(infoLabel1);
		connectPanel2.add(infoLabel2);
		connectPanel2.add(backButton);
		connectPanel2.add(nameLabel);
		connectPanel2.add(nameField);
		connectPanel2.add(errorLabel);
	}

	private void initializeButtons() { //method for setting up jbuttons

		//back button
		backButton.setSize(100, 25);
		backButton.setLocation(30, 20);
		backButton.addActionListener(this);
		Utility.setButtonStyle(backButton, 12);
		//server start button
		startServerButton.setSize(500, 100);
		startServerButton.setLocation(390, 565);
		startServerButton.addActionListener(this);
		Utility.setButtonStyle(startServerButton, 24);
		//join server button
		joinServerButton.setSize(500, 100);
		joinServerButton.setLocation(390, 565);
		joinServerButton.addActionListener(this);
		Utility.setButtonStyle(joinServerButton, 24);
	}

	private void initializeLabels() { //method for setting up jlabels
		//name
		nameLabel.setSize(185, 50);
		nameLabel.setLocation(30, 55);
		Utility.setLabelStyle(nameLabel, 32);
		//info label 1
		infoLabel1.setSize(1200, 100);
		infoLabel1.setLocation(40, 410);
		infoLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel1.setVerticalAlignment(SwingConstants.CENTER);
		Utility.setLabelStyle(infoLabel1, 30);
		//info label 2
		infoLabel2.setSize(1200, 100);
		infoLabel2.setLocation(40, 460);
		infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel2.setVerticalAlignment(SwingConstants.CENTER);
		Utility.setLabelStyle(infoLabel2, 30);

		//server labels
		ipLabel.setSize(800, 150); //ip address
		ipLabel.setLocation(240, 100);
		ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ipLabel.setVerticalAlignment(SwingConstants.CENTER);
		Utility.setLabelStyle(ipLabel, 72);

		portLabel.setSize(800, 150); //port number
		portLabel.setLocation(240, 250);
		portLabel.setHorizontalAlignment(SwingConstants.CENTER);
		portLabel.setVerticalAlignment(SwingConstants.CENTER);
		Utility.setLabelStyle(portLabel, 72);

		//client labels
		enterIpLabel.setSize(400, 100); //ip address
		enterIpLabel.setLocation(30, 150);
		Utility.setLabelStyle(enterIpLabel, 52);

		enterPortLabel.setSize(500, 100); //port number
		enterPortLabel.setLocation(30, 250);
		Utility.setLabelStyle(enterPortLabel, 52);

		errorLabel.setSize(200, 50); //error label
		errorLabel.setLocation(540, 50);
		Utility.setLabelStyle(errorLabel, 14);
		errorLabel.setVisible(false);
	}

	private void initializeFields() { //initialize text fields
		//name of user
		nameField.setSize(155, 25);
		nameField.setLocation(235, 70);

		//ip address
		ipField.setSize(400, 40);
		ipField.setLocation(420, 180);

		//port number
		portField.setSize(350, 40);
		portField.setLocation(470, 280);
	}

	public JPanel getConnect2Panel() { //public method for returning JPanel for other classes
		return connectPanel2;
	}
}
