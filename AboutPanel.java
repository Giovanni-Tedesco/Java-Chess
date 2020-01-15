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
	private JButton backButton = new JButton("BACK");
	String strfileName = "Assets/AboutPanel.png";

	//Methods
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == backButton) {
			Utility.changePanel(new MainMenu().getMenuPanel());
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
		backButton.setSize(100, 25);
		backButton.setLocation(30, 20);
		backButton.addActionListener(this);
		Utility.setButtonStyle(backButton, 12);
		add(backButton);
	}
}
