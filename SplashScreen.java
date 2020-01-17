import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;

public class SplashScreen extends JPanel implements ActionListener {
	//Properties
    Timer timer = new Timer(1000 / 60, this);//timer for 60 fps
    int intCounter = 0;//counter for splash
    int intWidth = 50;//initial dimensions of the knight piece
    int intHeight = 50;
    boolean blnSwitch = false;//variable on when to switch to menu
    private String strPath = "Assets/Pieces/KnightBlack.png";//path to picture

    BufferedImage image = Utility.loadImage(strPath, this.getClass());//load image
	
	//Methods
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == timer) {//timer
            if (!blnSwitch) {//if not switching to main menu
                repaint();//repaint the panel
            } else {
                timer.stop();//stop the timer
                Utility.changePanel(new MainMenu().getMenuPanel());//change to menu panel
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (intWidth + 10 <= 720 && intHeight + 10 < 1280) {//if knight is smaller than set dimension
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 1280, 720);//draw a white rectangle, while knight grows
            image = Utility.resizeImage(image, intWidth += 10, intHeight += 10);//resize image
            g.drawImage(image, Utility.panelDimensions.width / 2 - (intWidth / 2),//draw image
                    Utility.panelDimensions.height / 2 - (intHeight / 2), null);
        } else {
            blnSwitch = true;//switch to menu panel
        }
    }
	
	//Constructor
    public SplashScreen() {
        super(); //transfers constructor from JPanel
        setPreferredSize(Utility.panelDimensions);//set size of panel
        timer.start();//start timer

    }
	//Main method
    public static void main(String[] args) {
        new SplashScreen();
    }

}
