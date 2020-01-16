
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;

public class Settings {
	private SettingsPanel settingsPanel = new SettingsPanel();
	private static HashMap<String, Object> settingsMap = new HashMap<>();
	//{gray, red, green, brown}
	private static Color [] boardColors = {new Color(79, 76, 69), new Color(79,8,17), new Color(3,150,8), new Color(145,84,17)};
	private static BufferedReader reader;
	private static PrintWriter writer;

	public static int getPortNumber() {
		loadSettings();
		return Integer.parseInt((String)settingsMap.get("port"));
	}

	public static Color getBoardColor() {
		loadSettings();
		return boardColors[Integer.parseInt((String)settingsMap.get("color"))];
	}

	public static boolean isDark() {
		loadSettings();
		return Boolean.parseBoolean((String)settingsMap.get("dark"));
	}

	public static boolean filterOn() {
		loadSettings();
		return Boolean.parseBoolean((String)settingsMap.get("filter"));
	}

	private static void loadSettings() {
		reader = Utility.getReader("settings.txt");
		for(int i = 0; i < 4; i++) {
			String [] line = Utility.readLine(reader).trim().split(" : ");
			settingsMap.put(line[0], line[1]);
		}

		try {
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveSettings() {
		writer = Utility.getWriter("settings.txt");
		writer.println("port : " + settingsMap.get("port"));
		writer.println("color : " + settingsMap.get("color"));
		writer.println("dark : " + settingsMap.get("dark"));
		writer.println("filter : " + settingsMap.get("filter"));
		writer.close();
	}

	public static void setDefaultSettings() {
		settingsMap.clear();
		settingsMap.put("port", 7777);
		settingsMap.put("color", 0);
		settingsMap.put("dark", true);
		settingsMap.put("filter", true);
		writer = Utility.getWriter("settings.txt");
		writer.print("port : 7777");
		writer.println("color : 0");
		writer.println("dark : true");
		writer.println("filter : true");
		writer.close();
	}

    public JPanel getsettingsPanel() {
        return settingsPanel;
    }

    public Settings(){
		loadSettings();
		settingsPanel.setPreferredSize(Utility.panelDimensions);
		settingsPanel.portField.setText((String)settingsMap.get("port"));
		settingsPanel.boardColorButtons[Integer.parseInt((String)settingsMap.get("color"))].setSelected(true);
		settingsPanel.darkButtons[((String)settingsMap.get("dark")).equals("true") ? 0 : 1].setSelected(true);
		settingsPanel.filterButtons[((String)settingsMap.get("filter")).equals("true") ? 0 : 1].setSelected(true);
	}

    private class SettingsPanel extends JPanel implements ActionListener{
		private Timer timer = new Timer(1000/60, this);
		private JButton backButton = new JButton("BACK");
	 	private JButton saveButton = new JButton("SAVE");
		private JButton defaultButton = new JButton("RESET TO DEFAULT");
		JTextField portField = new JTextField();
		JRadioButton [] boardColorButtons = new JRadioButton[4];
		JRadioButton [] darkButtons = new JRadioButton[2];
		JRadioButton [] filterButtons = new JRadioButton[2];
		private String strfileName = "Assets/AboutPanel.png";
		private JLabel portLabel = new JLabel("Change port number(Enter number above 1000): ");
		private JLabel boardColorLabel = new JLabel("Change Board Color: ");
		private JLabel darkLabel = new JLabel("Toggle Dark Mode:");
		private JLabel filterLabel = new JLabel("Profanity Filter(for chat):");
		private JLabel titleLabel = new JLabel("SETTINGS");
		private ButtonGroup boardGroup = new ButtonGroup();
		private ButtonGroup darkGroup = new ButtonGroup();
		private ButtonGroup filterGroup = new ButtonGroup();

		public void actionPerformed(ActionEvent evt) {
			if(evt.getSource() == timer) {
				repaint();
			} else if(evt.getSource()== backButton){
                Utility.changePanel(new MainMenu().getMenuPanel());
			}else if(evt.getSource() == saveButton){
				boolean blnSuccess = false;
				String strPort = portField.getText();
				int intPort = 0;
				try {
					intPort = Integer.parseInt(strPort);
					blnSuccess = true;
				} catch(NumberFormatException e) {
					portField.setText("ENTER A NUMBER");
					blnSuccess = false;
				}

				if(blnSuccess) {
					settingsMap.put("port", intPort);
					saveSettings();
				}
			} else if(evt.getSource() == defaultButton) {
				setDefaultSettings();
				Utility.changePanel(new Settings().settingsPanel);
			} else {
				for(int i = 0; i < boardColorButtons.length; i++) {
					if(evt.getSource() == boardColorButtons[i]) {
						settingsMap.put("color", i);
						System.out.println("COLOR");
						saveSettings();
						break;
					}
				}

				for(int i = 0; i < 2; i++) {
					if(evt.getSource() == darkButtons[i]) {
						settingsMap.put("dark", i == 0);
						saveSettings();
						Utility.changePanel(new Settings().settingsPanel);
						break;
					}
				}

				for(int i = 0; i < 2; i++) {
					if(evt.getSource() == darkButtons[i]) {
						settingsMap.put("filter", i == 0);
						saveSettings();
						break;
					}
				}
			}
		}

		public SettingsPanel() {
			super(null); //transfers constructor from JPanel
			setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
			initLabels();
			initButtons();
			initRadioButtons();
		}

		private void initLabels() {
			titleLabel.setSize(400, 100);
			titleLabel.setLocation(440, 10);
			Utility.setLabelStyle(titleLabel, 28);
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			titleLabel.setVerticalAlignment(SwingConstants.CENTER);

			portLabel.setSize(400, 50);
			portLabel.setLocation(175, 100);
			Utility.setLabelStyle(portLabel, 16);
			portLabel.setVerticalAlignment(SwingConstants.CENTER);

			portField.setSize(200, 25);
			portField.setLocation(580, 113);

			boardColorLabel.setSize(400, 50);
			boardColorLabel.setLocation(175, 150);
			Utility.setLabelStyle(boardColorLabel, 16);
			boardColorLabel.setVerticalAlignment(SwingConstants.CENTER);

			darkLabel.setSize(400, 50);
			darkLabel.setLocation(175, 300);
			Utility.setLabelStyle(darkLabel, 16);
			darkLabel.setVerticalAlignment(SwingConstants.CENTER);

			filterLabel.setSize(400, 50);
			filterLabel.setLocation(175, 400);
			Utility.setLabelStyle(filterLabel, 16);
			filterLabel.setVerticalAlignment(SwingConstants.CENTER);


			add(titleLabel);
			add(portLabel);
			add(portField);
			add(boardColorLabel);
			add(filterLabel);
			add(darkLabel);
		}

		private void initRadioButtons() {
			String [] strColorOptions = {"Gray", "Red", "Green", "Brown"};
			boolean isDark = Settings.isDark();
			for(int i = 0; i < 4; i++) {
				boardColorButtons[i] = new JRadioButton(strColorOptions[i]);
				boardColorButtons[i].setSize(100,25);
				boardColorButtons[i].setLocation(270 + (i * 150), 210);
				boardColorButtons[i].setForeground(isDark ? Color.WHITE : Color.BLACK);
				boardColorButtons[i].setBackground(isDark ? Color.BLACK : Color.WHITE);
				boardColorButtons[i].setFont(Utility.getFont().deriveFont(Font.PLAIN, 16));
				boardColorButtons[i].addActionListener(this);
				boardGroup.add(boardColorButtons[i]);
				add(boardColorButtons[i]);
			}

			String [] strOptions = {"YES", "NO"};
			for(int i = 0; i < 2; i++) {
				darkButtons[i] = new JRadioButton(strOptions[i]);
				darkButtons[i].setSize(100,25);
				darkButtons[i].setLocation(320 + (i * 150), 315);
				darkButtons[i].setForeground(isDark ? Color.WHITE : Color.BLACK);
				darkButtons[i].setBackground(isDark ? Color.BLACK : Color.WHITE);
				darkButtons[i].setFont(Utility.getFont().deriveFont(Font.PLAIN, 16));
				darkButtons[i].addActionListener(this);
				darkGroup.add(darkButtons[i]);
				add(darkButtons[i]);
			}

			for(int i = 0; i < 2; i++) {
				filterButtons[i] = new JRadioButton(strOptions[i]);
				filterButtons[i].setSize(100,25);
				filterButtons[i].setLocation(370 + (i * 150), 415);
				filterButtons[i].setForeground(isDark ? Color.WHITE : Color.BLACK);
				filterButtons[i].setBackground(isDark ? Color.BLACK : Color.WHITE);
				filterButtons[i].setFont(Utility.getFont().deriveFont(Font.PLAIN, 16));
				filterButtons[i].addActionListener(this);
				filterGroup.add(filterButtons[i]);
				add(filterButtons[i]);
			}
		}

		private void initButtons() {
			backButton.setSize(110, 45);
            backButton.setLocation(30, 30);
            backButton.addActionListener(this);
            Utility.setButtonStyle(backButton, 12);

			defaultButton.setSize(200, 75);
			defaultButton.setLocation(540, 600);
			defaultButton.addActionListener(this);
			Utility.setButtonStyle(defaultButton, 14);

			saveButton.setSize(200, 25);
			saveButton.setLocation(780, 113);
			saveButton.addActionListener(this);
			Utility.setButtonStyle(saveButton, 12);

			add(backButton);
			add(defaultButton);
			add(saveButton);
		}
	}
}
