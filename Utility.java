
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

//This class has some methods to make things convenient
public class Utility {
    //Dimensions object that is used for every panel
    public static Dimension panelDimensions = new Dimension(960, 540);

    //Returns a font object of the Google Sans font
    public static Font getFont() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("google_regular.ttf"));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return font;
    }

    //Returns a specified image
    public static BufferedImage loadImage(String strImage) {
        try {
            BufferedImage image = ImageIO.read(new File(strImage));
            return image;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Wraps the instantiation of a PrintWriter
    public static PrintWriter getWriter(String strFile) {
        try {
            return new PrintWriter(new FileWriter(strFile));
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Wraps the instantiation of a BufferedReader
    public static BufferedReader getReader(String strFile) {
        try {
            return new BufferedReader(new FileReader(strFile));
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Wraps reading from a BufferedReader
    public static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Sets the font and text color of labels
    public static void setLabelStyle(JLabel label, int intFontSize) {
        label.setFont(getFont().deriveFont(Font.PLAIN, intFontSize));
        label.setForeground(Color.WHITE);
    }

    //Sets the font and color of buttons
    public static void setButtonStyle(JButton button, int intFontSize) {
        button.setBackground(new Color(53,53,56));
        button.setForeground(Color.WHITE);
        button.setFont(getFont().deriveFont(Font.PLAIN, intFontSize));
    }

    //Sets a new content pane for the JFrame
    // public static void changePanel(JPanel panel) {
    //     MainMenu.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     MainMenu.frame.setResizable(false);
    //     MainMenu.frame.setContentPane(panel);
    //     MainMenu.frame.pack();
    //     MainMenu.frame.setVisible(true);
    // }
}
