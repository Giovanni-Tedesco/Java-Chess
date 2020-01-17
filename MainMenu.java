import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

//This class implements the Main Menu screen and takes user to all other screens
public class MainMenu implements ActionListener {
    // Properties
    public static JFrame frame;

    // JButton dimensions
    private final int intButtWidth = 400, intButtHeight = 75;

    private JPanel menuPanel = new JPanel();
    private AboutPanel aboutPanel = new AboutPanel();
    private SplashScreen splashScreen = new SplashScreen();
    private JLabel titleLabel = new JLabel("CHESS");
    private JButton playButton = new JButton("PLAY");
    private JButton reviewButton = new JButton("REVIEW");
    private JButton helpButton = new JButton("HELP");
    private JButton settingButton = new JButton("SETTINGS");
    private JButton aboutButton = new JButton("ABOUT");
    private JButton quitButton = new JButton("QUIT");

    // Methods
    @Override
    public void actionPerformed(ActionEvent e) {
        // Set JFrame content depending on the button chosen
        if (e.getSource() == playButton) {
            Utility.changePanel(new ConnectPlayer().getConnectPanel());
        } else if (e.getSource() == helpButton) {
            Utility.changePanel(new Help().getHelpPanel());
        } else if (e.getSource() == settingButton) {
            Utility.changePanel(new Settings().getsettingsPanel());
        } else if (e.getSource() == aboutButton) {
            Utility.changePanel(aboutPanel);
        } else if (e.getSource() == quitButton) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } else if(e.getSource() == reviewButton) {
            Utility.changePanel(new GameReview().getChoosePanel());
        }
    }

    public void displaySplash() {
        Utility.changePanel(splashScreen);
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    // Constructor
    // Initialize the panel and all the JComponents
    public MainMenu() {
        menuPanel.setPreferredSize(Utility.panelDimensions);
        menuPanel.setLayout(null);
        menuPanel.setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);

        aboutPanel.setPreferredSize(Utility.panelDimensions);
        aboutPanel.setLayout(null);
        aboutPanel.setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);

        titleLabel.setSize(600, 200);
        titleLabel.setLocation(340, 0);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        playButton.setSize(intButtWidth, intButtHeight);
        playButton.setLocation(440, 170);
        playButton.addActionListener(this);

        reviewButton.setSize(intButtWidth, intButtHeight);
        reviewButton.setLocation(440, 255);
        reviewButton.addActionListener(this);

        helpButton.setSize(intButtWidth, intButtHeight);
        helpButton.setLocation(440, 340);
        helpButton.addActionListener(this);

        settingButton.setSize(intButtWidth, intButtHeight);
        settingButton.setLocation(440, 425);
        settingButton.addActionListener(this);

        aboutButton.setSize(intButtWidth, intButtHeight);
        aboutButton.setLocation(440, 510);
        aboutButton.addActionListener(this);

        quitButton.setSize(intButtWidth, intButtHeight);
        quitButton.setLocation(440, 595);
        quitButton.addActionListener(this);

        menuPanel.add(titleLabel);
        Utility.setLabelStyle(titleLabel, 52);
        menuPanel.add(playButton);
        Utility.setButtonStyle(playButton, 16);
        menuPanel.add(reviewButton);
        Utility.setButtonStyle(reviewButton, 16);
        menuPanel.add(helpButton);
        Utility.setButtonStyle(helpButton, 16);
        menuPanel.add(settingButton);
        Utility.setButtonStyle(settingButton, 16);
        menuPanel.add(aboutButton);
        Utility.setButtonStyle(aboutButton, 16);
        menuPanel.add(quitButton);
        Utility.setButtonStyle(quitButton, 16);
    }
}
