import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
//this class implements the help screen
public class Help extends MouseAdapter {
    //PROPERTIES
    //int value that represents the current help screen
    private int intHelpScreen;
    private HelpPanel helpPanel;
    private Timer helpTimer = new Timer(1000/60, event -> helpPanel.repaint());
    //image for the current help screen being displayed
    private BufferedImage currentHelpImage;
    //list of areas to be clicked, used to change the help screen
    private ArrayList<ClickableArea> clickAreaList = new ArrayList<>();
    //list of images for the help screens
    private ArrayList<BufferedImage> helpImageList = new ArrayList<>();

    //METHODS
    @Override
    public void mouseClicked(MouseEvent evt) {
        //iterate through areas to see if one was clicked
        for(ClickableArea area : clickAreaList) {
            if(area.clickedArea(evt)) {
                //set the current image to the chosen one
                currentHelpImage = helpImageList.get(area.intNumber - 1);
                //save the current screen number
                intHelpScreen = area.intNumber;
                break;
            }
        }
    }

    //add all the clickable areas to the lists
    private void initializeClickAreas() {
        for(int i = 0; i < 9; i++) {
            clickAreaList.add(new ClickableArea(382 + (i*60), 652, (i+1)));
        }
    }

    //load all the help images
    private void initImages() {
        for(int i = 0; i < 9; i++) {
            //load the dark or white version depending on settings
            helpImageList.add(Utility.loadImage("Assets/" + (Settings.isDark()?"Dark":"White") + "_Help/help_" + (i+1) + ".png", this.getClass()));
        }

        currentHelpImage = helpImageList.get(0);
    }

    public JPanel getHelpPanel() {
        return helpPanel;
    }

    //standard CONSTRUCTOR
    public Help() {
        helpPanel = new HelpPanel();
        helpPanel.setPreferredSize(Utility.panelDimensions);
        helpPanel.addMouseListener(this);
        initializeClickAreas();
        initImages();
        helpTimer.start();
    }

    //CONSTRUCTOR to launch the help panel at a particular screen
    public Help(int intScreenNum) {
        helpPanel = new HelpPanel();
        helpPanel.setPreferredSize(Utility.panelDimensions);
        helpPanel.addMouseListener(this);
        initializeClickAreas();
        initImages();
        helpTimer.start();
        currentHelpImage = helpImageList.get(intScreenNum-1<0?0:intScreenNum-1);
    }

    //class that implements the ui for the help screen
    private class HelpPanel extends JPanel {
        //PROPERTIES
        private JButton backButton = new JButton("BACK");
        private JButton tutorialButton = new JButton("TUTORIAL");

        //CONSTRUCTOR, initalize ui elements
        HelpPanel() {
            super(null);
            backButton.setSize(116, 45);
            backButton.setLocation(30, 28);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    helpTimer.stop();
                    Utility.changePanel(new MainMenu().getMenuPanel());
                }
            });
            Utility.setButtonStyle(backButton, 12);

            tutorialButton.setSize(150, 45);
            tutorialButton.setLocation(1100, 28);
            tutorialButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    helpTimer.stop();
                    Utility.changePanel(new TutorialMode(intHelpScreen).getTutorialPanel());
                }
            });
            Utility.setButtonStyle(tutorialButton, 12);

            add(backButton);
            add(tutorialButton);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(currentHelpImage != null) {
                //draw the current help image to the screen
                g.drawImage(currentHelpImage, 0, 0, null);
            }
        }
    }
}
