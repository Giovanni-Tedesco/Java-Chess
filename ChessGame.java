import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

//Class where the board is, the chat area is, and the captured piece display is
public class ChessGame implements ActionListener {
    //The server starts their game first but they should not see anything until the client joins
    //display a message saying waiting for client or something
    //once a message is received by the client, initialize the game for server
    private SuperSocketMaster ssm;
    private String strName, strOpponentName;
    private boolean blnServer, blnConnectionFailed = false;
    private BoardAnimation chessPanel = new BoardAnimation();

    private JLabel waitingLabel = new JLabel("Waiting for the other player");
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if(event == ssm) {
            String strMessage = ssm.readText();
            System.out.println(strMessage);
            if(strMessage.equals("ping")) {
                waitingLabel.setVisible(false);
                chessPanel.remove(waitingLabel);
                chessPanel.initializeGame();
                chessPanel.repaint();
            }
        }
    }

    public boolean connectionFailed() {
        return blnConnectionFailed;
    }

    //constructor for testing
    public ChessGame() {
        blnServer = true;
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        chessPanel.initializeGame();
    }

    //constructor for the server game instance
    public ChessGame(String strName) {
        blnServer = true;
        this.strName = strName;
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        //TODO: get port from settings later
        ssm = new SuperSocketMaster(6969, this);
        ssm.connect();

        waitingLabel.setSize(300, 100);
        waitingLabel.setLocation(400, 400);
        Utility.setLabelStyle(waitingLabel, 40);
        chessPanel.add(waitingLabel);
    }

    //TODO: figure out what to do if someone disconnects in the middle of a game
    //constructor for the client game instance
    public ChessGame(String strName, String strIp, int intPort) {
        //probably set this property in chess panel later
        blnServer = false;
        this.strName = strName;
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        ssm = new SuperSocketMaster(strIp, intPort, this);
        if(ssm.connect()) {
            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            ssm.sendText("ping");
            chessPanel.initializeGame();
            chessPanel.repaint();
            blnConnectionFailed = false;
        } else {
            blnConnectionFailed = true;
        }
    }

    public JPanel getChessPanel() {
        return chessPanel;
    }
}
