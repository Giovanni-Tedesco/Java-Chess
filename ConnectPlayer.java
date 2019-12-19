import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ConnectPlayer implements ActionListener {
    private JPanel connectPanel = new JPanel(null);
    private JButton testButton = new JButton("test");
    private JButton backButton = new JButton("<- BACK");
    private JButton serverButton = new JButton("HOST GAME");
    private JButton clientButton = new JButton("JOIN GAME");

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if(event == serverButton) {
            Utility.changePanel(new ConnectPlayer2(true).connectPanel2);
        } else if(event == clientButton) {
            Utility.changePanel(new ConnectPlayer2(false).connectPanel2);
        } else if(event == testButton) {
            Utility.changePanel(new ChessGame("bruh").getChessPanel());
        }
    }

    public ConnectPlayer() {
        connectPanel.setBackground(Color.BLACK);
        connectPanel.setPreferredSize(Utility.panelDimensions);

        //temporary
        testButton.setSize(100, 25);
        testButton.setLocation(190, 20);
        testButton.addActionListener(this);
        Utility.setButtonStyle(testButton, 12);   

        backButton.setSize(100, 25);
        backButton.setLocation(30, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);        

        serverButton.setSize(1220, 300);
        serverButton.setLocation(30, 60);
        serverButton.addActionListener(this);
        Utility.setButtonStyle(serverButton, 24);
        
        clientButton.setSize(1220, 300);
        clientButton.setLocation(30, 390);
        clientButton.addActionListener(this);
        Utility.setButtonStyle(clientButton, 24);

        connectPanel.add(serverButton);
        connectPanel.add(clientButton);
        connectPanel.add(testButton);
        connectPanel.add(backButton);
    }

    public JPanel getConnectPanel() {
        return connectPanel;
    }

    private class ConnectPlayer2 implements ActionListener {
        private String strName = "Enter Name: ", strIp = "Server IP: ", strPort = "Server Port: ", strEnterPort = "Enter Server Port:", strEnterIP = "Enter Server IP:",
                strServerInfo1 = "Please tell the other player to enter the IP address and the port number displayed here.",
                strServerInfo2 = "Make sure to start the game here before the other player does.",
                strClientInfo1 = "Please enter the IP address and the port number displayed on the host screen.",
                strClientInfo2 = "Wait till the host has started the game to join.";
        private String strUserName = "";
        private boolean blnServer;
        JPanel connectPanel2 = new JPanel(null);

        private JButton retryButton = new JButton("RESET CONNECTION");
        private JButton startServerButton = new JButton("START SERVER");
        private JButton joinServerButton = new JButton("CONNECT TO SERVER");

        private JLabel nameLabel = new JLabel(strName);
        private JLabel ipLabel = new JLabel(strIp);
        private JLabel portLabel = new JLabel(strPort);
        private JLabel enterPortLabel = new JLabel(strEnterPort);
        private JLabel enterIpLabel = new JLabel(strEnterIP);
        private JLabel infoLabel1 = new JLabel();
        private JLabel infoLabel2 = new JLabel();

        private JTextField portField = new JTextField();
        private JTextField ipField = new JTextField();
        private JTextField nameField = new JTextField();
        @Override
        public void actionPerformed(ActionEvent evt) {
            Object event = evt.getSource();
            if(event == backButton) {
                Utility.changePanel(new ConnectPlayer().getConnectPanel());
            } else if(event == startServerButton) {
                strName = nameField.getText();
                strName = (strName == null || strName.equals(""))?Utility.getRandomName():strName;
                System.out.println("NAME: " + strName);
                Utility.changePanel(new ChessGame(strName).getChessPanel());
            }
        }

        ConnectPlayer2(boolean blnIsServer) {
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
                connectPanel2.add(infoLabel1);
                connectPanel2.add(infoLabel2);
                connectPanel2.add(startServerButton);
            }

            connectPanel2.add(backButton);
            connectPanel2.add(nameLabel);
            connectPanel2.add(nameField);
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
        }

        private void initializeLabels() {
            nameLabel.setSize(185, 50);
            nameLabel.setLocation(30, 55);
            Utility.setLabelStyle(nameLabel, 32);
            
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
        }

        private void initializeFields() {
            nameField.setSize(155, 25);
            nameField.setLocation(235, 70);
        }

        public void notifyFailure() {
            //error message on connection failure for client
        }
    }
}