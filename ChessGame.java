import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Iterator;
import java.util.ArrayList;

//Class where the board is, the chat area is, and the captured piece display is
public class ChessGame implements ActionListener {
    // The server starts their game first but they should not see anything until the client joins
    // once a message is received from the client, initialize the game for server

    //PROPERTIES
    private static SuperSocketMaster ssm;
    private JTextArea chatArea = new JTextArea();
    private JScrollPane chatScroll = new JScrollPane(chatArea);
    private JTextField chatField = new JTextField();
    private JButton backButton = new JButton("<- BACK");
    private JButton sendButton = new JButton("SEND");
    private JButton cancelButton = new JButton("CANCEL");
    private JButton resignButton = new JButton("RESIGN");
    private String strServerName = "", strClientName = "";
    private boolean blnServer, blnConnectionFailed = false;
    private BoardAnimation chessPanel;// = new BoardAnimation();
    private JLabel serverName = new JLabel("White: ");
    private JLabel clientName = new JLabel("Black: ");
    private JLabel waitingLabel = new JLabel("Waiting for the other player");
    //array list for log of moves
    private static ArrayList<String> movesMade = new ArrayList<String>();

    private String[] strPieceNames = { "rook", "knight", "bishop", "queen", "king", "pawn" };
    private ArrayList<String> badList = Utility.getBadWords();

    //METHODS
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if (event == ssm) {
            String strMessage = ssm.readText();
            System.out.println(strMessage);
            if (strMessage.contains("<") || strMessage.contains(">")) {
                // chat message
                chatArea.append(strMessage + "\n");
            } else if (strMessage.contains("resigned")) {
                if(ssm != null) {
                    ssm.disconnect();
                }
                //switch to winning end screen
                Utility.changePanel(new EndScreen(movesMade, EndScreen.WON, strServerName, strClientName, blnServer));
            } else if (strMessage.contains("GAMEOVER")) {
                //switch to winning end screen
                Utility.changePanel(new EndScreen(movesMade, EndScreen.WON, strServerName, strClientName, blnServer));
            } else if (strMessage.contains("ping")) {
                //initial message from the client after they have joined
                String[] strClientStart = strMessage.split(",");
                strClientName = strClientStart[1];
                //start the game now
                waitingLabel.setVisible(false);
                cancelButton.setVisible(false);
                chessPanel.remove(waitingLabel);
                chessPanel.remove(cancelButton);
                initializeNameLabels();
                clientName.setText("Black: " + strClientName);
                initializeChat();
                chessPanel.initializeGame();
                chessPanel.repaint();
                //server sends connect confirmation along with name to client
                sendConnectMessage(true, strServerName);
            } else if (strMessage.contains("pong")) {
                //initial message sent from the server after the client has joined
                String[] strServerStart = strMessage.split(",");
                strServerName = strServerStart[1];
                serverName.setText("White: " + strServerName);
            } else if (strMessage.contains("promotion over")) {
                chessPanel.serverInfoLabel.setText("CAPTURED PIECES");
                chessPanel.clientInfoLabel.setText("CAPTURED PIECES");
                chessPanel.changeTurn();
                String[] strPromote = strMessage.split(",");
                //construct a new piece using the sent message, this is the piece that was chosen to replace the pawn
                Board chessBoard = BoardAnimation.getBoard();
                int intXIndex = Integer.parseInt(strPromote[1]);
                int intYIndex = Integer.parseInt(strPromote[2]);
                int intXPos = blnServer ? intXIndex * 90 : (7 - intXIndex) * 90;//array is read flipped for client
                int intYPos = blnServer ? intYIndex * 90 : (7 - intYIndex) * 90;

                Piece piece = new Piece(intXPos, intYPos, !blnServer, Integer.parseInt(strPromote[3]));
                //update the board array as well
                chessBoard.setPiece(intXIndex, intYIndex,
                        piece.blnColor ? Integer.parseInt(strPromote[3]) : -Integer.parseInt(strPromote[3]));

                //let the user know what piece was chosen by their opponent
                if (blnServer) {
                    chessPanel.serverInfoLabel
                            .setText("Promotion over, BLACK chose " + strPieceNames[piece.intPiece - 1]);
                } else {
                    chessPanel.clientInfoLabel
                            .setText("Promotion over, WHITE chose " + strPieceNames[piece.intPiece - 1]);
                }

                //find any pawn at the sent position and remove it, add the new piece after
                Iterator<Piece> pieceIterator = chessBoard.pieces.iterator();
                while (pieceIterator.hasNext()) {
                    Piece tempPiece = pieceIterator.next();
                    if (tempPiece.intXPos == intXPos && tempPiece.intYPos == intYPos && tempPiece.intPiece == 6) {
                        System.out.println("GOT HERE X:" + intXPos + " Y: " + intYPos);
                        pieceIterator.remove();
                        chessBoard.pieces.add(piece);
                        break;
                    }
                }

                System.out.println("AFTER PROMOTION");
                chessBoard.printCharboard();
                chessPanel.repaint();
            } else if (strMessage.contains(",")) {
                //standard move

                chessPanel.serverInfoLabel.setText("CAPTURED PIECES");
                chessPanel.clientInfoLabel.setText("CAPTURED PIECES");
                chessPanel.changeTurn();
                Board chessBoard = BoardAnimation.getBoard();
                chessBoard.move(strMessage, false);
                //BoardAnimation.playSound("check");
                movesMade.add(strMessage);
                System.out.println("Moves: " + movesMade);

                //get the initial and final position from the move string sent
                String[] strMove = strMessage.split(",");
                Point initPos = chessBoard.coordToLoc(strMove[0]);
                Point finalPos = chessBoard.coordToLoc(strMove[1]);
                Piece tempPiece = null;
                int intX, intY, intFinalX, intFinalY;

                chessBoard.displayInformation();

                //if the message has 3 elements, that means a promotion happened
                if (strMove.length == 3) {
                    chessPanel.changeTurn();
                    if (blnServer) {
                        chessPanel.clientInfoLabel.setText("PROMOTION IN PROGRESS");
                    } else {
                        chessPanel.serverInfoLabel.setText("PROMOTION IN PROGRESS");
                    }
                }

                //the array is read flipped for the client
                if (blnServer) {
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

                //find the piece at the specified final position and remove any pieces there
                for (int i = 0; i < chessBoard.pieces.size(); i++) {
                    Piece p = chessBoard.pieces.get(i);
                    if (p.intXPos / 90 == intFinalX && p.intYPos / 90 == intFinalY) {
                        chessBoard.captured.add(p);
                        chessBoard.pieces.remove(p);
                        break;
                    }
                }

                //find the piece object at the specified initial position
                for (Piece p : chessBoard.pieces) {
                    if (p.intXPos / 90 == intX && p.intYPos / 90 == intY) {
                        tempPiece = p;
                        break;
                    }
                }

                chessPanel.updateCaptures();
                if (tempPiece != null) {
                    //update that piece object to the new final position
                    tempPiece.setPosition(intFinalX * 90, intFinalY * 90);
                }

                //if there is a check
                if (strMessage.contains("+")) {
                    System.out.println("This gives a check");
                    chessBoard.setCheck(true);
                    BoardAnimation.playSound("check");

                    //the game is over
                    if (chessBoard.checkmate()) {
                        Utility.changePanel(
                                new EndScreen(movesMade, EndScreen.LOST, strServerName, strClientName, blnServer));
                        ssm.sendText("GAMEOVER");
                    }

                    if (blnServer) {
                        chessPanel.serverInfoLabel.setText("OH NO, YOU'RE IN CHECK");
                    } else {
                        chessPanel.clientInfoLabel.setText("OH NO, YOU'RE IN CHECK");
                    }

                } else {
                    chessBoard.setCheck(false);
                }
                chessPanel.repaint();
            }
        } else if (event == sendButton) {
            //send chat message with the correct user name
            String strChatName = blnServer ? strServerName : strClientName;
            String strChatMessage = chatField.getText();
            if (Settings.filterOn()) {
                for (String strBadWord : badList) {
                    if (strChatMessage.contains(strBadWord)) {
                        strChatMessage = "I tried to send a bad word.";
                        break;
                    }
                }
            }
            if (ssm != null) {
                ssm.sendText("<" + strChatName + ">" + " " + strChatMessage);
            }
            chatArea.append("<" + strChatName + ">" + " " + strChatMessage + "\n");
            chatField.setText("");
        } else if (event == cancelButton || event == backButton) {
            if (ssm != null) {
                ssm.disconnect();
            }

            Utility.changePanel(new MainMenu().getMenuPanel());
        } else if (event == resignButton) {
            if (ssm != null) {
                ssm.sendText("resigned");
                ssm.disconnect();
            }

            Utility.changePanel(new EndScreen(movesMade, EndScreen.RESIGN, strServerName, strClientName, blnServer));
        }
    }

    public static void addMove(String strMove) {
        movesMade.add(strMove);
    }

    public boolean connectionFailed() {
        return blnConnectionFailed;
    }

    //set properties for jcomponents related to chat
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

        resignButton.setLocation(1175, 30);
        resignButton.setSize(100, 20);
        resignButton.addActionListener(this);
        Utility.setButtonStyle(resignButton, 12);

        chessPanel.add(chatScroll);
        chessPanel.add(chatField);
        chessPanel.add(sendButton);
        chessPanel.add(backButton);
        chessPanel.add(resignButton);
    }

    private void initializeNameLabels() {
        serverName.setSize(300, 20);
        serverName.setLocation(725, 5);
        Utility.setLabelStyle(serverName, 14);

        clientName.setSize(300, 20);
        clientName.setLocation(725, 210);
        Utility.setLabelStyle(clientName, 14);

        serverName.setText("White: " + strServerName);
        clientName.setText("Black: " + strClientName);

        chessPanel.add(serverName);
        chessPanel.add(clientName);
    }

    //sends a confirmation message used for initial connection
    private void sendConnectMessage(boolean blnIsServer, String strName) {
        String strConnect = blnIsServer ? "pong" : "ping";
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ssm != null) {
            System.out.println(strConnect + "," + strName);
            ssm.sendText(strConnect + "," + strName);
        }
    }

    public JPanel getChessPanel() {
        return chessPanel;
    }

    public static SuperSocketMaster getNetwork() {
        return ssm;
    }

    // constructor for the server game instance
    public ChessGame(String strName) {
        blnServer = true;
        strServerName = strName;
        chessPanel = new BoardAnimation(true);
        chessPanel.setLayout(null);
        chessPanel.setPreferredSize(Utility.panelDimensions);
        ssm = new SuperSocketMaster(Settings.getPortNumber(), this);
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

    // constructor for the client game instance
    public ChessGame(String strName, String strIp, int intPort) {
        blnServer = false;
        strClientName = strName;
        chessPanel = new BoardAnimation(false);
        chessPanel.setPreferredSize(Utility.panelDimensions);
        ssm = new SuperSocketMaster(strIp, intPort, this);
        if (ssm.connect()) {
            // client sends connect confirmation along with name
            sendConnectMessage(false, strName);
            initializeChat();
            initializeNameLabels();
            chessPanel.initializeGame();
            chessPanel.repaint();
            blnConnectionFailed = false;
        } else {
            blnConnectionFailed = true;
        }
    }
}
