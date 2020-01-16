import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.*;

public class EndScreen extends JPanel {
    public static final int WON = 0, LOST = 1, RESIGN = 2;
    private ArrayList<String> moveLog = new ArrayList<>();
    private String strReason, strName, strOpponentName;
    private JLabel endLabel = new JLabel();
    private JButton okButton = new JButton("OKAY");

    public EndScreen(ArrayList<String> movesMade, int intReason, String strServerName, String strClientName, boolean blnServer) {
        super(null);
        setPreferredSize(Utility.panelDimensions);
        setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
        this.moveLog = movesMade;
        this.strName = blnServer?strServerName:strClientName;
        this.strOpponentName = blnServer?strClientName:strServerName;

        endLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endLabel.setVerticalAlignment(SwingConstants.CENTER);
        endLabel.setSize(800, 400);
        endLabel.setLocation(240, 100);
        Utility.setLabelStyle(endLabel, 24);
        add(endLabel);

        okButton.setSize(200, 100);
        okButton.setLocation(520, 600);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Utility.changePanel(new MainMenu().getMenuPanel());
            }
        });
        Utility.setButtonStyle(okButton, 20);
        add(okButton);

        setEndText(intReason);
        saveLog();
    }

    public void saveLog() {
        if(moveLog.size() < 2) {
            return;
        }

        PrintWriter writer = Utility.getWriter("review/" + strName.replaceAll(" ", "_") + "_vs_" + strOpponentName.replaceAll(" ", "_") + ".txt");
        writer.println(strName + "vs" + strOpponentName);
        for(String strMove : moveLog) {
            writer.println(strMove);
        }
        writer.print("end");

        writer.close();
    }

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
}
