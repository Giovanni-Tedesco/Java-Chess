import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

//Class where the board is, the chat area is, and the captured piece display is
public class ChessGame implements ActionListener {
    //The server starts their game first but they should not see anything until the client joins
    //display a message saying waiting for client or something
    //once a message is received by the client, initialize the game for server
    private static SuperSocketMaster ssm;
    private JTextArea chatArea = new JTextArea();
    private JScrollPane chatScroll = new JScrollPane(chatArea);
    private JTextField chatField = new JTextField();
    private JButton backButton = new JButton("<- BACK");
    private JButton sendButton = new JButton("SEND");
    private JButton cancelButton = new JButton("CANCEL");
    private String strName, strOpponentName;
    private boolean blnServer, blnConnectionFailed = false;
    private BoardAnimation chessPanel;//= new BoardAnimation();

    private JLabel waitingLabel = new JLabel("Waiting for the other player");
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if(event == ssm) {
            String strMessage = ssm.readText();
            System.out.println(strMessage);
            if(strMessage.equals("ping")) {
                waitingLabel.setVisible(false);
                cancelButton.setVisible(false);
                chessPanel.remove(waitingLabel);
                chessPanel.remove(cancelButton);
                initializeChat();
                chessPanel.initializeGame();
                chessPanel.repaint();
            } else if(strMessage.contains("<") || strMessage.contains(">")) {
                //chat message
                chatArea.append(strMessage + "\n");
            } else if(strMessage.contains(",")) {
                chessPanel.changeTurn();
                Board chessBoard = BoardAnimation.getBoard();
                chessBoard.move(strMessage);
                String [] strMove = strMessage.split(",");
                Point initPos = chessBoard.coordToLoc(strMove[0]);
                Point finalPos = chessBoard.coordToLoc(strMove[1]);
                Piece temp = null;
                int intX, intY, intFinalX, intFinalY;

                if(blnServer) {
                    intX = initPos.x;
                    intY = initPos.y;
                    intFinalX = finalPos.x;
                    intFinalY = finalPos.y;
                } else {
                    intX = 7 - initPos.x;
                    intY = 7 - initPos.y;
                    intFinalX = 7 - finalPos.x;
                    intFinalY = 7 - finalPos.y;
                }

                for(int i = 0; i < chessBoard.pieces.size(); i++) {
                    Piece p = chessBoard.pieces.get(i);
                    if(p.intXPos / 90 == intFinalX && p.intYPos / 90 == intFinalY) {
                        chessBoard.captured.add(p);
                        chessBoard.pieces.remove(p);
                        break;
                    }
                }

                //chessBoard.capturePiece(intFinalX, intFinalY);

                for(Piece p : chessBoard.pieces) {
                    if(p.intXPos / 90 == intX && p.intYPos / 90 == intY) {
                        temp = p;
                        break;
                    }
                }

                temp.setPosition(intFinalX*90, intFinalY*90);
                chessPanel.repaint();
            }
        } else if(event == sendButton) {
            if(ssm != null) {
                ssm.sendText("<" + strName + ">" + " " + chatField.getText());
            }
            chatArea.append("<" + strName + ">" + " " + chatField.getText() + "\n");
            chatField.setText("");
        } else if(event == cancelButton || event == backButton) {
            if(ssm != null) {
                ssm.disconnect();
            }

            Utility.changePanel(new MainMenu().getMenuPanel());
        }
    }

    public boolean connectionFailed() {
        return blnConnectionFailed;
    }

    //constructor for testing
    public ChessGame() {
        //set true or false here to control the black or the white
        chessPanel = new BoardAnimation(true);
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        chessPanel.initializeGame();
        initializeChat();
    }

    //constructor for the server game instance
    public ChessGame(String strName) {
        blnServer = true;
        this.strName = strName;
        chessPanel = new BoardAnimation(true);
        chessPanel.setLayout(null);
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        //TODO: get port from settings later
        ssm = new SuperSocketMaster(6969, this);
        ssm.connect();

        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingLabel.setVerticalAlignment(SwingConstants.CENTER);
        waitingLabel.setSize(600, 100);
        waitingLabel.setLocation(340, 250);
        Utility.setLabelStyle(waitingLabel, 40);
        chessPanel.add(waitingLabel);

        cancelButton.setSize(300, 100);
        cancelButton.setLocation(490, 350);
        cancelButton.addActionListener(this);
        Utility.setButtonStyle(cancelButton, 20);
        chessPanel.add(cancelButton);
    }

    //TODO: figure out what to do if someone disconnects in the middle of a game
    //constructor for the client game instance
    public ChessGame(String strName, String strIp, int intPort) {
        //probably set this property in chess panel later
        blnServer = false;
        this.strName = strName;
        chessPanel = new BoardAnimation(false);
        chessPanel.setPreferredSize(new Dimension(1280,720));
        chessPanel.setBackground(Color.BLACK);
        ssm = new SuperSocketMaster(strIp, intPort, this);
        if(ssm.connect()) {
            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ping");
            //send name later as well so that the server will know the client name
            ssm.sendText("ping");
            initializeChat();
            chessPanel.initializeGame();
            chessPanel.repaint();
            blnConnectionFailed = false;
        } else {
            blnConnectionFailed = true;
        }
    }

    private void initializeChat() {
        chessPanel.setLayout(null);
        chatScroll.setLocation(725, 440);
        chatScroll.setSize(550, 250);
        chatArea.setEditable(false);
        chatArea.setFont(Utility.getFont().deriveFont(Font.PLAIN, 16));
        chatArea.setBackground(Color.GRAY);
        chatArea.setForeground(Color.GREEN);
        chatArea.setOpaque(true);

        chatField.setLocation(725, 695);
        chatField.setSize(450, 20);
        chatField.setFont(Utility.getFont().deriveFont(Font.PLAIN, 12));
        chatField.setBackground(Color.GRAY);
        chatField.setOpaque(true);
        chatField.setForeground(Color.GREEN);

        sendButton.setLocation(1175, 695);
        sendButton.setSize(100, 20);
        sendButton.addActionListener(this);
        Utility.setButtonStyle(sendButton, 12);

        backButton.setLocation(1175, 5);
        backButton.setSize(100, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);

        chessPanel.add(chatScroll);
        chessPanel.add(chatField);
        chessPanel.add(sendButton);
        chessPanel.add(backButton);
    }

    public JPanel getChessPanel() {
        return chessPanel;
    }

    public static SuperSocketMaster getNetwork() {
        return ssm;
    }
}
