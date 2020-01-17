import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;

public class SplashScreen extends JPanel implements ActionListener {

    Timer timer = new Timer(1000 / 60, this);
    int intCounter = 0;
    int intWidth = 50;
    int intHeight = 50;
    boolean blnSwitch = false;
    private String strPath = "Assets/Pieces/KnightBlack.png";

    BufferedImage image = Utility.loadImage(strPath);

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == timer) {
            if (!blnSwitch) {
                repaint();
            } else {
                timer.stop();
                Utility.changePanel(new MainMenu().getMenuPanel());
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (intWidth + 10 <= 720 && intHeight + 10 < 1280) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 1280, 720);
            image = Utility.resizeImage(image, intWidth += 10, intHeight += 10);
            g.drawImage(image, Utility.panelDimensions.width / 2 - (intWidth / 2),
                    Utility.panelDimensions.height / 2 - (intHeight / 2), null);
        } else {
            blnSwitch = true;
        }
    }

    public SplashScreen() {

        super();
        setPreferredSize(Utility.panelDimensions);
        timer.start();

    }

    public static void main(String[] args) {
        new SplashScreen();
    }

}
