import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Implements the About screen
public class AboutPanel extends JPanel implements ActionListener {
	
	//Properties
	private JButton backButton = new JButton("BACK"); //back button
	String strfileName = "Assets/AboutPanel.png"; //about image

	//Methods
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == backButton) { //if back button is pressed
			Utility.changePanel(new MainMenu().getMenuPanel()); //change panel back to main menu
		}
	}

	public void paintComponent(Graphics g){
		BufferedImage image = null;
		//draw help screen image
		g.drawImage(Utility.loadImage(strfileName),0,0,null);
	}

	//constructor
	public AboutPanel(){
		//Initialize default JPanel properties
		super();
		//setting back button
		backButton.setSize(100, 25);
		backButton.setLocation(30, 20);
		backButton.addActionListener(this);
		Utility.setButtonStyle(backButton, 12); //setting preset button style
		add(backButton); //adding backbutton to panel
	}
}
