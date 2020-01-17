import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ConnectPlayer implements ActionListener {
	
	//Properties
    private JPanel connectPanel = new JPanel(null); //JPanel
    private JButton backButton = new JButton("BACK"); //back button
    private JButton serverButton = new JButton("HOST GAME"); //go to page for host/server computer
    private JButton clientButton = new JButton("JOIN GAME");//join a game with a server host

	//Methods
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource(); //set event object as get source method
        if(event == backButton) {//if back button is clicked
            Utility.changePanel(new MainMenu().getMenuPanel());//go to main menu
        } else if(event == serverButton) { //server button
            Utility.changePanel(new ConnectPlayer2(true, false).getConnect2Panel()); //server page
        } else if(event == clientButton) { //client button
            Utility.changePanel(new ConnectPlayer2(false, false).getConnect2Panel()); //client page
        }
    }
    public JPanel getConnectPanel() { //public method to return connect panel for other classes
        return connectPanel;
    }
    
	//Constructor
    public ConnectPlayer() {
		//set panel for connecting players
        connectPanel.setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
        connectPanel.setPreferredSize(Utility.panelDimensions); 
		
		//set back button
        backButton.setSize(100, 25);
        backButton.setLocation(30, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12); //use preset button style
		
		//server button
        serverButton.setSize(595, 600);
        serverButton.setLocation(30, 70);
        serverButton.addActionListener(this);
        Utility.setButtonStyle(serverButton, 24);
		
		//client button
        clientButton.setSize(595, 600);
        clientButton.setLocation(655, 70);
        clientButton.addActionListener(this);
        Utility.setButtonStyle(clientButton, 24);
		
		//add buttons to panel
        connectPanel.add(serverButton);
        connectPanel.add(clientButton);
        connectPanel.add(backButton);
    }
	
    
}
