import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AboutPanel extends JPanel implements ActionListener {
	private JButton backButton = new JButton("BACK");
	String strfileName = "AboutPanel.png";

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == backButton) {
			Utility.changePanel(new MainMenu().getMenuPanel());
		}
	}

	public void paintComponent(Graphics g){
		BufferedImage image = null;
		g.drawImage(Utility.loadImage(strfileName),0,0,null);
	}

	public AboutPanel(){
		super(); //transfers constructor from JPanel
		backButton.setSize(100, 25);
		backButton.setLocation(30, 20);
		backButton.addActionListener(this);
		Utility.setButtonStyle(backButton, 12);
		add(backButton);
	}
}
