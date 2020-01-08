import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AboutPanel extends JPanel{
	
			//properties
		
		String strfileName = "AboutPanel.png";
		
		//method
		public void paintComponent(Graphics g){
			
				BufferedImage image = null;
				
				//try and catch IOException
	
					//read file name
					
					
					
				
				
				//draw image
				g.drawImage(Utility.loadImage(strfileName),0,0,null);
		}
		public AboutPanel(){
			super(); //transfers constructor from JPanel
		}
}

	
	
