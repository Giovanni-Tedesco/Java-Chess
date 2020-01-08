import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
public class Help extends MouseAdapter implements ActionListener {
    private Timer timer = new Timer(1000/60, this);
    private HelpPanel helpPanel;
    private BufferedImage currentHelpImage;
    private ArrayList<ClickableArea> clickAreaList = new ArrayList<>();
    private ArrayList<BufferedImage> helpImageList = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == timer) {
            helpPanel.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        for(ClickableArea area : clickAreaList) {
            if(area.clickedArea(evt)) {
                currentHelpImage = helpImageList.get(area.intNumber - 1);
                break;
            }
        }
    }

    public Help() {
        helpPanel = new HelpPanel();
        helpPanel.setPreferredSize(Utility.panelDimensions);
        helpPanel.addMouseListener(this);
        initializeClickAreas();
        initImages();
        timer.start();
    }

    private void initializeClickAreas() {
        for(int i = 0; i < 9; i++) {
            clickAreaList.add(new ClickableArea(382 + (i*60), 652, (i+1)));
        }
    }

    private void initImages() {
        for(int i = 0; i < 9; i++) {
            helpImageList.add(Utility.loadImage("Assets/Dark_Help/help_" + (i+1) + ".png"));
        }

        currentHelpImage = helpImageList.get(0);
    }

    public JPanel getHelpPanel() {
        return helpPanel;
    }

    private class HelpPanel extends JPanel {
        private JButton backButton = new JButton("BACK");

        HelpPanel() {
            super(null);
            backButton.setSize(116, 45);
            backButton.setLocation(30, 28);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Utility.changePanel(new MainMenu().getMenuPanel());
                }
            });
            Utility.setButtonStyle(backButton, 12);
            add(backButton);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(currentHelpImage != null) {
                g.drawImage(currentHelpImage, 0, 0, null);
            }
        }
    }
}
