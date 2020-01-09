import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class Settings extends MouseAdapter implements ActionListener{
	
	 JPanel settingsPanel = new JPanel();
	 JButton backButton = new JButton("BACK");
	 JButton saveButton = new JButton("SAVE");
	 JTextField portField = new JTextField();
	 JRadioButton boardDarkGreyButt = new JRadioButton();
	 JRadioButton boardRedButt = new JRadioButton();
	 JRadioButton boardGreenButt = new JRadioButton();
	 JRadioButton boardBrownButt = new JRadioButton();
	 JRadioButton UIDarkButt = new JRadioButton();
	 JRadioButton UILightButt = new JRadioButton();
	 JRadioButton ProfanityNoButt = new JRadioButton();
	 JRadioButton ProfanityYesButt = new JRadioButton();
	 String strfileName = "Assets/AboutPanel.png";
	 JLabel portLabel = new JLabel("Change port number(Enter number above 1000): ");
	 JLabel boardcolorLabel = new JLabel("Change Board Color: ");
	 JLabel darkgreyLabel = new JLabel("Dark Grey");
	 JLabel redLabel = new JLabel("Red");
	 JLabel greenLabel = new JLabel("Green");
	 JLabel brownLabel = new JLabel("Brown");
	 JLabel UIlabel = new JLabel("UI style");
	 JLabel UIDark = new JLabel("Dark");
	 JLabel UILight = new JLabel("Light");
	 JLabel profLabel = new JLabel("Profanity Filter(for chat)");
	 JLabel profYes = new JLabel("Yes");
	 JLabel profNo = new JLabel("No");
	 
	 
		
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == backButton) {
			Utility.changePanel(new MainMenu().getMenuPanel());
		}
       
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      
    }
    public JPanel getsettingsPanel() {
        return settingsPanel;
    }
    
	public Settings(){
		super(); //transfers constructor from JPanel
        settingsPanel.setPreferredSize(Utility.panelDimensions);
        settingsPanel.addMouseListener(this);
        
		backButton.setSize(100, 25);
		backButton.setLocation(30, 20);
		backButton.addActionListener(this);
		Utility.setButtonStyle(backButton, 12);
		settingsPanel.add(backButton);
		
		saveButton.setSize(100, 25);
		saveButton.setLocation(30, 20);
		saveButton.addActionListener(this);
		Utility.setButtonStyle(saveButton, 12);
		
	}
    
}
