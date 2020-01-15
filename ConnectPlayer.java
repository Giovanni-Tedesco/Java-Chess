import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ConnectPlayer implements ActionListener {
    private JPanel connectPanel = new JPanel(null);
    private JButton testButton = new JButton("test");
    private JButton backButton = new JButton("BACK");
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
        connectPanel.setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
        connectPanel.setPreferredSize(Utility.panelDimensions);

        //temporary
        //testButton.setSize(100, 25);
        //testButton.setLocation(190, 20);
        //testButton.addActionListener(this);
        //Utility.setButtonStyle(testButton, 12);

        backButton.setSize(100, 25);
        backButton.setLocation(30, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);

        serverButton.setSize(595, 600);
        serverButton.setLocation(30, 70);
        serverButton.addActionListener(this);
        Utility.setButtonStyle(serverButton, 24);

        clientButton.setSize(595, 600);
        clientButton.setLocation(655, 70);
        clientButton.addActionListener(this);
        Utility.setButtonStyle(clientButton, 24);

        connectPanel.add(serverButton);
        connectPanel.add(clientButton);

        //test mode doesn't really work anymore, all testing needs to be done with two windows of the game
        //its probably better this way because any changes will have to work with the multiplayer aspect
        //connectPanel.add(testButton);

        connectPanel.add(backButton);
    }

    public JPanel getConnectPanel() {
        return connectPanel;
    }
}
