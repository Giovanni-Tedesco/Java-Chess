import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

//This class implements the Main Menu screen and takes user to all other screens
public class MainMenu implements ActionListener {
    //Properties
    public static JFrame frame;

    //JButton dimensions
    private final int intButtWidth = 400, intButtHeight = 75;

    private JPanel menuPanel = new JPanel();
    private JLabel titleLabel = new JLabel("CHESS");
    private JButton playButton = new JButton("PLAY");
    private JButton helpButton = new JButton("HELP");
    private JButton settingButton = new JButton("SETTINGS");
    private JButton aboutButton = new JButton("ABOUT");
    private JButton quitButton = new JButton("QUIT");

    private BoardAnimation board = new BoardAnimation();

    //Methods
    @Override
    public void actionPerformed(ActionEvent e) {
        //Set JFrame content depending on the button chosen
        if(e.getSource() == playButton) {
            Utility.changePanel(board);
        } else if(e.getSource() == helpButton) {
            Utility.changePanel(menuPanel);
        } else if(e.getSource() == settingButton) {
            Utility.changePanel(menuPanel);
        } else if(e.getSource() == aboutButton) {
            Utility.changePanel(menuPanel);
        } else if(e.getSource() == quitButton) {
            Utility.changePanel(menuPanel);
        }
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    //Constructor
    //Initialize the panel and all the JComponents
    public MainMenu() {
        board.setPreferredSize(new Dimension(400, 400));
        menuPanel.setPreferredSize(Utility.panelDimensions);
        menuPanel.setLayout(null);
        menuPanel.setBackground(Color.BLACK);

        titleLabel.setSize(600, 200);
        titleLabel.setLocation(340, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        playButton.setSize(intButtWidth, intButtHeight);
        playButton.setLocation(440, 220);
        playButton.addActionListener(this);

        helpButton.setSize(intButtWidth, intButtHeight);
        helpButton.setLocation(440, 305);
        helpButton.addActionListener(this);

        settingButton.setSize(intButtWidth, intButtHeight);
        settingButton.setLocation(440, 390);
        settingButton.addActionListener(this);

        aboutButton.setSize(intButtWidth, intButtHeight);
        aboutButton.setLocation(440, 475);
        aboutButton.addActionListener(this);

        quitButton.setSize(intButtWidth, intButtHeight);
        quitButton.setLocation(440, 560);
        quitButton.addActionListener(this);

        menuPanel.add(titleLabel);
        Utility.setLabelStyle(titleLabel, 52);
        menuPanel.add(playButton);
        Utility.setButtonStyle(playButton, 16);
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
