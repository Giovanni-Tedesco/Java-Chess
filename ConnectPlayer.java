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
        if(event == backButton) {
            Utility.changePanel(new MainMenu().getMenuPanel());
        } else if(event == serverButton) {
            Utility.changePanel(new ConnectPlayer2(true, false).getConnect2Panel());
        } else if(event == clientButton) {
            Utility.changePanel(new ConnectPlayer2(false, false).getConnect2Panel());
        } else if(event == testButton) {
            Utility.changePanel(new ChessGame().getChessPanel());
        }
    }

    public ConnectPlayer() {
        connectPanel.setBackground(Color.BLACK);
        connectPanel.setPreferredSize(Utility.panelDimensions);

        //tegmporary
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
}