import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ConnectPlayer implements ActionListener {
    private JPanel connectPanel = new JPanel(null);
    private String strIP = "Server IP: ";
    private String strPort = "Server Port: ";

    private JButton serverButton = new JButton("HOST GAME");
    private JButton clientButton = new JButton("JOIN GAME");

    private JLabel ipLabel = new JLabel(strIP);
    private JLabel portLabel = new JLabel(strPort);
    private JLabel enterIpLabel = new JLabel("Enter Server IP:");
    private JLabel enterPortLabel = new JLabel("Enter Server Port:");

    private JTextField ipField = new JTextField();
    private JTextField portField = new JTextField();

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == serverButton) {
            connectPanel.remove(serverButton);
            connectPanel.remove(clientButton);
        } else if(evt.getSource() == clientButton) {

        }
    }

    public ConnectPlayer() {
        serverButton.setSize();
        serverButton.setLocation();
        serverButton.addActionListener(this);
        Utility.setButtonStyle(serverButton, 24);

        clientButton.setSize();
        clientButton.setLocation();
        clientButton.addActionListener(this);
        Utility.setButtonStyle(clientButton, 24);

        connectPanel.add(serverButton);
        connectPanel.add(clientButton);        
    }   

    private JPanel getConnectPanel() {
        return connectPanel;
    }
}