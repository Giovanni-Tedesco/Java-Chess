import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

class ConnectPlayer2 implements ActionListener {
    private String strName = "Enter Name: ", strIp = "Server IP: ", strPort = "Server Port: ", strEnterPort = "Enter Server Port:", strEnterIP = "Enter Server IP:",
                strServerInfo1 = "Please tell the other player to enter the IP address and the port number displayed here.",
                strServerInfo2 = "Make sure to start the game here before the other player does.",
                strClientInfo1 = "Please enter the IP address and the port number displayed on the host screen.",
                strClientInfo2 = "Wait till the host has started the game to join.";
    private String strUserName = "", strPortNumber = "", strIpAddress = "";
    private boolean blnServer, blnWrongPort = false, blnError;
    private JPanel connectPanel2 = new JPanel(null);

    private JButton backButton = new JButton("BACK");
    private JButton startServerButton = new JButton("START SERVER");
    private JButton joinServerButton = new JButton("CONNECT TO SERVER");

    private JLabel nameLabel = new JLabel(strName);
    private JLabel ipLabel = new JLabel(strIp);
    private JLabel portLabel = new JLabel(strPort);
    private JLabel enterPortLabel = new JLabel(strEnterPort);
    private JLabel enterIpLabel = new JLabel(strEnterIP);
    private JLabel infoLabel1 = new JLabel();
    private JLabel infoLabel2 = new JLabel();
    private JLabel errorLabel = new JLabel();

    private JTextField portField = new JTextField();
    private JTextField ipField = new JTextField();
    private JTextField nameField = new JTextField();
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if(event == backButton) {
            Utility.changePanel(new ConnectPlayer().getConnectPanel());
        } else if(event == startServerButton) {
            strUserName = nameField.getText();
            strUserName = (strUserName == null || strUserName.equals(""))?Utility.getRandomName():strUserName;
            System.out.println("NAME: " + strUserName);
            Utility.changePanel(new ChessGame(strUserName).getChessPanel());
        } else if(event == joinServerButton) {
            strUserName = nameField.getText();
            strUserName = (strUserName == null || strUserName.equals(""))?Utility.getRandomName():strUserName;
            System.out.println("NAME: " + strUserName);
            int intPortNumber = 0;

            strIpAddress = ipField.getText();
            strPortNumber = portField.getText();

            try {
                intPortNumber = Integer.parseInt(strPortNumber);
                blnWrongPort = false;
            } catch(NumberFormatException exc) {
                errorLabel.setVisible(true);
                blnWrongPort = true;
                errorLabel.setText("Please enter a port number");
                Timer timer = new Timer(3000, e -> errorLabel.setVisible(false));
                timer.setRepeats(false);
                timer.start();
            }

            if(!blnWrongPort) {
                ChessGame chess = new ChessGame(strUserName, strIpAddress, intPortNumber);
                if(chess.connectionFailed()) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Failed to connect");
                    Timer timer = new Timer(3000, e -> errorLabel.setVisible(false));
                    timer.setRepeats(false);
                    timer.start();
                    System.out.println("THE ERROR");
                } else {
                    Utility.changePanel(chess.getChessPanel());
                }
            }
                //TODO: Handle failures in connection later
        }
    }

    public ConnectPlayer2(boolean blnIsServer, boolean blnError) {
        connectPanel2.setBackground(Color.BLACK);
        connectPanel2.setPreferredSize(Utility.panelDimensions);
        blnServer = blnIsServer;

        initializeButtons();
        initializeLabels();
        initializeFields();

        if(blnServer) {
            SuperSocketMaster temp = new SuperSocketMaster(6969, this);
            ipLabel.setText(strIp + temp.getMyAddress());
            portLabel.setText(strPort + 6969);
            infoLabel1.setText(strServerInfo1);
            infoLabel2.setText(strServerInfo2);

            connectPanel2.add(ipLabel);
            connectPanel2.add(portLabel);
            connectPanel2.add(startServerButton);
        } else {
            infoLabel1.setText(strClientInfo1);
            infoLabel2.setText(strClientInfo2);

            connectPanel2.add(enterIpLabel);
            connectPanel2.add(enterPortLabel);
            connectPanel2.add(ipField);
            connectPanel2.add(portField);
            connectPanel2.add(joinServerButton);
        }

        if(blnError) {
            errorLabel.setVisible(true);
            errorLabel.setText("Failed to connect");
            System.out.println("THE ERROR");
        }

        connectPanel2.add(infoLabel1);
        connectPanel2.add(infoLabel2);
        connectPanel2.add(backButton);
        connectPanel2.add(nameLabel);
        connectPanel2.add(nameField);
        connectPanel2.add(errorLabel);
    }

    private void initializeButtons() {
        backButton.setSize(100, 25);
        backButton.setLocation(30, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);

        startServerButton.setSize(500, 100);
        startServerButton.setLocation(390, 565);
        startServerButton.addActionListener(this);
        Utility.setButtonStyle(startServerButton, 24);

        joinServerButton.setSize(500, 100);
        joinServerButton.setLocation(390, 565);
        joinServerButton.addActionListener(this);
        Utility.setButtonStyle(joinServerButton, 24);
    }

    private void initializeLabels() {
        nameLabel.setSize(185, 50);
        nameLabel.setLocation(30, 55);
        Utility.setLabelStyle(nameLabel, 32);

        infoLabel1.setSize(1200, 100);
        infoLabel1.setLocation(40, 410);
        infoLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel1.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(infoLabel1, 30);

        infoLabel2.setSize(1200, 100);
        infoLabel2.setLocation(40, 460);
        infoLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel2.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(infoLabel2, 30);

        //server stuff
        ipLabel.setSize(800, 150);
        ipLabel.setLocation(240, 100);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(ipLabel, 72);

        portLabel.setSize(800, 150);
        portLabel.setLocation(240, 250);
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(portLabel, 72);

        //client stuff
        enterIpLabel.setSize(400, 100);
        enterIpLabel.setLocation(30, 150);
        Utility.setLabelStyle(enterIpLabel, 52);

        enterPortLabel.setSize(500, 100);
        enterPortLabel.setLocation(30, 250);
        Utility.setLabelStyle(enterPortLabel, 52);

        errorLabel.setSize(200, 50);
        errorLabel.setLocation(540, 50);
        Utility.setLabelStyle(errorLabel, 14);
        errorLabel.setVisible(false);
    }

    private void initializeFields() {
        nameField.setSize(155, 25);
        nameField.setLocation(235,   70);

        ipField.setSize(400, 40);
        ipField.setLocation(420, 180);

        portField.setSize(350, 40);
        portField.setLocation(470, 280);
    }

    public JPanel getConnect2Panel() {
        return connectPanel2;
    }
}
