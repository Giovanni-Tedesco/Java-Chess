import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

//This class has some methods to make things convenient
public class Utility {
	// Dimensions object that is used for every panel
	public static Dimension panelDimensions = new Dimension(1280, 720);

	// Returns a font object of the Google Sans font
	public static Font getFont() {
		Font theFont = null;
		// Try to load the font from the jar file
		try {
			theFont = Font.createFont(Font.TRUETYPE_FONT, Utility.class.getResourceAsStream("google_regular.ttf"));
			return theFont;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Then try to load the font from the local filesystem
		try {
			theFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("google_regular.ttf"));
			return theFont;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return theFont;
	}

	// Returns a specified image
	public static BufferedImage loadImage(String strImage, Class source) {
		//Try to read the file from the jar file
		InputStream imageclass = null;
		imageclass = source.getResourceAsStream(strImage);
		if (imageclass != null) {
			try {
				return ImageIO.read(imageclass);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Then try to read the local file
		try {
			BufferedImage image = ImageIO.read(new File(strImage));
			return image;
		} catch (IOException e) {
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

	// Wraps the instantiation of a BufferedReader
	public static BufferedReader getReader(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage resizeImage(BufferedImage oldImage, int intWidth, int intHeight) {
		Image newImage = (Image) oldImage.getScaledInstance(intWidth, intHeight, Image.SCALE_DEFAULT);

		return toBufferedImage(newImage);
	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// Create a buffered image with transparency
		BufferedImage imageToBuffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		//Draw the image on to the buffered image
		Graphics2D backGround = imageToBuffered.createGraphics();
		backGround.drawImage(image, 0, 0, null);
		backGround.dispose();

		// Return the buffered image
		return imageToBuffered;
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
		if (Settings.isDark()) {
			label.setForeground(Color.WHITE);
		} else {
			label.setForeground(Color.BLACK);
		}
	}

	// Sets the font and color of buttons
	public static void setButtonStyle(JButton button, int intFontSize) {
		if (Settings.isDark()) {
			button.setBackground(Color.WHITE); // new Color(53,53,56));
			button.setForeground(Color.BLACK);
		} else {
			button.setBackground(Color.BLACK); // new Color(53,53,56));
			button.setForeground(Color.WHITE);
		}
		button.setOpaque(true);
		button.setFont(getFont().deriveFont(Font.PLAIN, intFontSize));
	}

	public static ArrayList<String> getBadWords() {
		ArrayList<String> badList = new ArrayList<>();
		BufferedReader reader = getReader("bad_words.txt");
		String strBadWord = readLine(reader);
		while (strBadWord != null) {
			badList.add(strBadWord);
			strBadWord = readLine(reader);
		}

		return badList;
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

		int intRandomIndex = (int)(Math.random() * randomNameList.size());
		return randomNameList.get(intRandomIndex);
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
