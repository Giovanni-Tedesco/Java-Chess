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
	private Timer timer = new Timer(1000/60, this);
	private SettingsPanel settingsPanel = new SettingsPanel();
	@Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == timer) {
            settingsPanel.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
      
    }
    public JPanel getsettingsPanel() {
        return settingsPanel;
    }
    
    public Settings(){
		settingsPanel.setPreferredSize(Utility.panelDimensions);
		settingsPanel.addMouseListener(this);
	}
    
    private class SettingsPanel extends JPanel implements ActionListener{
		 private JButton backButton = new JButton("BACK");
		 private JButton saveButton = new JButton("SAVE");
		 private JTextField portField = new JTextField();
		 private JRadioButton boardDarkGreyButt = new JRadioButton();
		 private JRadioButton boardRedButt = new JRadioButton();
		 private JRadioButton boardGreenButt = new JRadioButton();
		 private JRadioButton boardBrownButt = new JRadioButton();
		 private JRadioButton UIDarkButt = new JRadioButton();
		 private JRadioButton UILightButt = new JRadioButton();
		 private JRadioButton ProfanityNoButt = new JRadioButton();
		 private JRadioButton ProfanityYesButt = new JRadioButton();
		 private String strfileName = "Assets/AboutPanel.png";
		 private JLabel portLabel = new JLabel("Change port number(Enter number above 1000): ");
		 private JLabel boardcolorLabel = new JLabel("Change Board Color: ");
		 private JLabel darkgreyLabel = new JLabel("Dark Grey");
		 private JLabel redLabel = new JLabel("Red");
		 private JLabel greenLabel = new JLabel("Green");
		 private JLabel brownLabel = new JLabel("Brown");
		 private JLabel UIlabel = new JLabel("UI style");
		 private JLabel UIDark = new JLabel("Dark");
		 private JLabel UILight = new JLabel("Light");
		 private JLabel profLabel = new JLabel("Profanity Filter(for chat)");
		 private JLabel profYes = new JLabel("Yes");
		 private JLabel profNo = new JLabel("No");
		
		public void actionPerformed(ActionEvent evt) {
			if(evt.getSource()== backButton){
                    Utility.changePanel(new MainMenu().getMenuPanel());
			}else if(evt.getSource()== saveButton){
			}
		}

		public SettingsPanel(){
			super(null); //transfers constructor from JPanel	
            backButton.setSize(116, 45);
            backButton.setLocation(30, 28);
            backButton.addActionListener(this);
            Utility.setButtonStyle(backButton, 12);
            add(backButton);
			
			
			saveButton.setSize(100, 25);
			saveButton.setLocation(30, 20);
			saveButton.addActionListener(this);
			Utility.setButtonStyle(saveButton, 12);
			add(saveButton);
			
		}
	}
}

