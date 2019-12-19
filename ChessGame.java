import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ChessGame implements ActionListener {
    private SuperSocketMaster ssm;
    private String strName, strOpponentName;
    private boolean blnServer;
    @Override
    public void actionPerformed(ActionEvent evt) {

    } 
    private BoardAnimation chessPanel = new BoardAnimation();

    //constructor for the server game instance
    public ChessGame(String strName) {
        blnServer = true;
        this.strName = strName;
        chessPanel.setPreferredSize(new Dimension(400,400));
        //TODO: get port from settings later
        ssm = new SuperSocketMaster(6969, this);
    }

    //constructor for the client game instance
    public ChessGame(String strName, String strIp, int intPort) {
        blnServer = false;
        this.strName = strName;
        chessPanel.setPreferredSize(new Dimension(400,400));
        ssm = new SuperSocketMaster(strIp, intPort, this);
    }

    public JPanel getChessPanel() {
        return chessPanel;
    }
}