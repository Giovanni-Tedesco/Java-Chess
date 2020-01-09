
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

//This class has some methods to make things convenient
public class Utility {
    // Dimensions object that is used for every panel
    public static Dimension panelDimensions = new Dimension(1280, 720);

    // Returns a font object of the Google Sans font
    public static Font getFont() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("google_regular.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return font;
    }

    // Returns a specified image
    public static BufferedImage loadImage(String strImage) {
        try {
            BufferedImage image = ImageIO.read(new File(strImage));
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Wraps the instantiation of a PrintWriter
    public static PrintWriter getWriter(String strFile) {
        try {
            return new PrintWriter(new FileWriter(strFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Wraps the instantiation of a BufferedReader
    public static BufferedReader getReader(String strFile) {
        try {
            return new BufferedReader(new FileReader(strFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Wraps reading from a BufferedReader
    public static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Sets the font and text color of labels
    public static void setLabelStyle(JLabel label, int intFontSize) {
        label.setFont(getFont().deriveFont(Font.PLAIN, intFontSize));
        label.setForeground(Color.WHITE);
    }

    // Sets the font and color of buttons
    public static void setButtonStyle(JButton button, int intFontSize) {
        button.setBackground(Color.WHITE);// new Color(53,53,56));
        button.setForeground(Color.BLACK);
        button.setFont(getFont().deriveFont(Font.PLAIN, intFontSize));
    }

    // Return random name in case user does not enter name
    public static String getRandomName() {
        ArrayList<String> randomNameList = new ArrayList<>();
        randomNameList.add("Boomer");
        randomNameList.add("Bruh");
        randomNameList.add("Donald J Trump");
        randomNameList.add("Peter Parker");
        randomNameList.add("Tony Stark");
        randomNameList.add("Steve Rogers");
        randomNameList.add("Clark Kent");
        randomNameList.add("Bruce Wayne");
        randomNameList.add("James Buchanan Barnes");
        randomNameList.add("Peter Quill");
        randomNameList.add("Bruce Banner");
        randomNameList.add("Thor Odinson");
        randomNameList.add("Natasha Romanoff");
        randomNameList.add("Clint Barton");
        randomNameList.add("James Rhodes");
        randomNameList.add("Sam Wilson  ");
        randomNameList.add("Pietro Maximoff");
        randomNameList.add("Wanda");
        randomNameList.add("J.A.R.V.I.S");
        randomNameList.add("Hank Pym");
        randomNameList.add("Scott Lang");
        randomNameList.add("Hope Van Dyne");
        randomNameList.add("Yondu");
        randomNameList.add("T'Challa");
        randomNameList.add("Stephen Strange");
        randomNameList.add("Happy Hogan");
        randomNameList.add("Baby Yoda");
        randomNameList.add("Dad");

        int intRandomIndex = (int) (Math.random() * randomNameList.size());
        return randomNameList.get(intRandomIndex);
    }

    public static void displayArray(ArrayList<String> arr) {
        for (String s : arr) {
            System.out.println(s);
        }

    }

    // Sets a new content pane for the JFrame
    public static void changePanel(JPanel panel) {
        MainMenu.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenu.frame.setResizable(false);
        MainMenu.frame.setContentPane(panel);
        MainMenu.frame.pack();
        MainMenu.frame.setVisible(true);
    }
}
