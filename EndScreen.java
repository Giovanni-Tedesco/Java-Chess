import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.*;

//this class implements ui for the end screen
public class EndScreen extends JPanel {
    //PROPERTIES
    private Timer endTimer = new Timer(1000/60, event -> repaint());
    //possible reasons for coming to the end screen
    public static final int WON = 0, LOST = 1, RESIGN = 2;
    //the log of moves coming from a game
    private ArrayList<String> moveLog = new ArrayList<>();
    private String strReason, strName, strOpponentName;
    private JLabel endLabel = new JLabel();
    private JButton okButton = new JButton("OKAY");

    //save the log of moves from the arraylist to a file
    public void saveLog() {
        //if there are less than two moves, don't bother saving it
        if(moveLog.size() < 2) {
            return;
        }

        //open up a new file in the review folder for holding the move log
        PrintWriter writer = Utility.getWriter("review/" + strName.replaceAll(" ", "_") + "_vs_" + strOpponentName.replaceAll(" ", "_") + ".txt");
        writer.println(strName + "vs" + strOpponentName);
        //print moves to file
        for(String strMove : moveLog) {
            writer.println(strMove);
        }
        writer.print("end");
        writer.close();
    }

    //set the endLabel text depending on the reason
    public void setEndText(int intReason) {
        switch(intReason) {
            case WON:
                endLabel.setText("<html>" + "<div style='text-align: center;'>" +
                    "Congratulations " + strName + ", you won the game. You bested " + strOpponentName + " and made them look like a fool. " +
                    "Don't be mean about it though. " + " A log of the game has been saved and you can review it later for fun I guess" + "</div></html>");
                break;
            case LOST:
                endLabel.setText("<html>" + "<div style='text-align: center;'>" +
                    "Oof " + strName + ", you lost the game. Tough luck, " + strOpponentName + " made you look like a fool. " +
                    "Don't feel bad though, there's always next time." + " A log of the game has been saved and you can review it later for fun I guess" + "</div></html>");
                break;
            case RESIGN:
                endLabel.setText("<html>" + "<div style='text-align: center;'>" + "You cheated not only the game, but yourself. You didn't grow. " +
                    "You didn't improve. You took a shortcut and gained nothing. You experienced a hollow victory. Nothing was risked and nothing was gained. " +
                    "Also, a log of the game has been saved and you can review it later for fun I guess." + "</div></html>");
        }
    }

    //CONSTRUCTOR
    public EndScreen(ArrayList<String> movesMade, int intReason, String strServerName, String strClientName, boolean blnServer) {
        super(null);
        setPreferredSize(Utility.panelDimensions);
        setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
        this.moveLog = movesMade;
        //set current player name
        this.strName = blnServer?strServerName:strClientName;
        //set current player's opponent
        this.strOpponentName = blnServer?strClientName:strServerName;

        //set properties for endLabel
        endLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endLabel.setVerticalAlignment(SwingConstants.CENTER);
        endLabel.setSize(800, 400);
        endLabel.setLocation(240, 100);
        Utility.setLabelStyle(endLabel, 24);
        add(endLabel);

        //set properties for okButton
        okButton.setSize(200, 100);
        okButton.setLocation(520, 600);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                endTimer.stop();
                Utility.changePanel(new MainMenu().getMenuPanel());
            }
        });
        Utility.setButtonStyle(okButton, 20);
        add(okButton);

        setEndText(intReason);
        saveLog();
        endTimer.start();
    }
}
