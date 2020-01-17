import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.image.*;
import java.util.HashMap;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BoardAnimation extends JPanel {
    // Properties
    private boolean blnServer, blnTurn;
    private boolean blnClientStarted = false;
    private static Board chessBoard;
    private boolean blnPressed = false;
    // temporary piece used to drag and drop
    private Piece temp = null;
    private int[] intPieces = { 4, 1, 3, 2, 6 };
    // jlabels showing what pieces have been captured
    // {queen, rook, bishop, knight, pawn}
    // {4,1,3,2,6}
    private JLabel[] serverCaptureLabels = new JLabel[5];
    private JLabel[] clientCaptureLabels = new JLabel[5];
    // used for labelling captures and whether it is promotion time
    public JLabel serverInfoLabel = new JLabel("CAPTURED PIECES");
    public JLabel clientInfoLabel = new JLabel("CAPTURED PIECES");
    // images representing captured pieces
    private ArrayList<BufferedImage> whiteCaptureImages = new ArrayList<>();
    private ArrayList<BufferedImage> blackCaptureImages = new ArrayList<>();
    // images for pieces on the board
    public static HashMap<Integer, BufferedImage> pieceImages = new HashMap<>();

    // Methods
    public static void initImages() {
        String path = "Assets/Pieces/";
        String[] fileNames = { "Rook.png", "Knight.png", "Bishop.png", "Queen.png", "King.png", "Pawn.png",
                "RookBlack.png", "KnightBlack.png", "BishopBlack.png", "QueenBlack.png", "KingBlack.png",
                "PawnBlack.png" };
        for (int i = 0; i < fileNames.length; i++) {
            pieceImages.put(i + 1, Utility.loadImage(path + fileNames[i]));
        }
    }

    // Probably don't need this unless we are trying to save some memory, so
    // I'm just going to leave this here.
    // public void initializeSounds() {
    //
    // }

    public static void playSound(String sound) {
        try {
            if (sound.equalsIgnoreCase("move")) {
                String soundName1 = "Assets/Sounds/Move.wav";
                AudioInputStream audioInputStream1 = AudioSystem
                        .getAudioInputStream(new File(soundName1).getAbsoluteFile());
                Clip clip1 = AudioSystem.getClip();
                clip1.open(audioInputStream1);
                clip1.start();
            } else if (sound.equalsIgnoreCase("check")) {
                String soundName2 = "Assets/Sounds/Check.wav";
                AudioInputStream audioInputStream2 = AudioSystem
                        .getAudioInputStream(new File(soundName2).getAbsoluteFile());
                Clip clip2 = AudioSystem.getClip();
                clip2.open(audioInputStream2);
                clip2.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void initCaptureImages() {
        for (int intPiece : intPieces) {
            blackCaptureImages.add(Utility.resizeImage(pieceImages.get(intPiece), 60, 120));
            whiteCaptureImages.add(Utility.resizeImage(pieceImages.get(intPiece + 6), 60, 120));
        }
    }

    public void changeTurn() {
        blnTurn = !blnTurn;
    }

    public void initializeGame() {
        blnClientStarted = true;
        chessBoard = new Board(blnServer);
        addMouseListener(new MyMouseAdaptor());
        addMouseMotionListener(new MyMouseAdaptor());
        initializeCaptureLabels();
    }

    // Sets locations and sizes for all the capture labels and adds them to the
    // panel
    private void initializeCaptureLabels() {
        serverInfoLabel.setSize(200, 20);
        serverInfoLabel.setLocation(900, 5);
        serverInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        serverInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(serverInfoLabel, 12);
        add(serverInfoLabel);
        for (int i = 0; i < serverCaptureLabels.length; i++) {
            serverCaptureLabels[i] = new JLabel("0");
            Utility.setLabelStyle(serverCaptureLabels[i], 18);
            serverCaptureLabels[i].setSize(60, 20);
            serverCaptureLabels[i].setLocation(800 + (i * 80), 190);
            serverCaptureLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            serverCaptureLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            add(serverCaptureLabels[i]);
        }

        clientInfoLabel.setSize(200, 20);
        clientInfoLabel.setLocation(900, 210);
        clientInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clientInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(clientInfoLabel, 12);
        add(clientInfoLabel);
        for (int i = 0; i < clientCaptureLabels.length; i++) {
            clientCaptureLabels[i] = new JLabel("0");
            Utility.setLabelStyle(clientCaptureLabels[i], 18);
            clientCaptureLabels[i].setSize(60, 20);
            clientCaptureLabels[i].setLocation(800 + (i * 80), 390);
            clientCaptureLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            clientCaptureLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            add(clientCaptureLabels[i]);
        }
    }

    // recount number of captured pieces
    public void updateCaptures() {
        int[] intPieces = { 4, 1, 3, 2, 6 };
        for (int i = 0; i < 5; i++) {
            serverCaptureLabels[i].setText(chessBoard.capturedPieceCount(true, intPieces[i]) + "");
            clientCaptureLabels[i].setText(chessBoard.capturedPieceCount(false, intPieces[i]) + "");
        }
    }

    public static Board getBoard() {
        return chessBoard;
    }

    private void drawBoard(Graphics g) {
        Color boardColor = Settings.getBoardColor();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor(((i % 2 == 0) == (j % 2 == 0)) ? Color.WHITE : boardColor);
                g.fillRect(j * 90, i * 90, 90, 90);
            }
        }
    }

    private void drawPieces(Graphics g) {
        for (Piece p : chessBoard.pieces) {
            p.update(g);
        }
    }

    private void drawCapturedPieces(Graphics g) {
        for (int i = 0; i < 5; i++) {
            g.drawImage(whiteCaptureImages.get(i), 800 + (i * 80), 50, null);
            g.drawImage(blackCaptureImages.get(i), 800 + (i * 80), 250, null);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (blnClientStarted) {
            drawBoard(g);
            drawPieces(g);
            g.setColor(Color.WHITE);
            // border separating board from other panel elements
            g.drawLine(720, 0, 720, 720);
            drawCapturedPieces(g);
        }
    }

    // Constructor
    public BoardAnimation(boolean blnIsServer) {
        super();
        setBackground(Settings.isDark() ? new Color(46, 44, 44) : Color.WHITE);
        this.blnServer = blnIsServer;
        blnTurn = blnIsServer;
        initImages();
        if (pieceImages != null) {
            initCaptureImages();
        }
    }

    // class the handles mouse input for dragging and dropping pieces
    private class MyMouseAdaptor extends MouseAdapter {
        // flag for whether the user tried to pick up the opponent's pieces
        private boolean blnWrongColor;
        private boolean blnMouseError;

        @Override
        public void mouseClicked(MouseEvent evt) {
            // get promotion choice and promote the piece
            if (blnTurn && chessBoard.promotionInProgress()) {
                ArrayList<Piece> promotionChoices = chessBoard.getPromotionChoices(blnServer);
                for (Piece piece : promotionChoices) {
                    if ((evt.getX() <= piece.intXPos + 60 && evt.getX() >= piece.intXPos)
                            && (evt.getY() >= piece.intYPos && evt.getY() <= piece.intYPos + 120)) {
                        chessBoard.promotePiece(piece);
                        serverInfoLabel.setText("CAPTURED PIECES");
                        clientInfoLabel.setText("CAPTURED PIECES");
                        changeTurn();
                        repaint();
                        break;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            blnWrongColor = false;
            blnMouseError = false;
            // get the position of the piece at the pressed spot
            int intXPos = chessBoard.roundDown(evt.getX(), 90);
            int intYPos = chessBoard.roundDown(evt.getY(), 90);
            // get the array index corresponding with the coordinates
            // subtract by 7 if client because the array is read flipped
            int intXIndex = blnServer ? intXPos / 90 : 7 - (intXPos / 90);
            int intYIndex = blnServer ? intYPos / 90 : 7 - (intYPos / 90);
            // check to see if click was within the board array length
            boolean blnInBounds = intXIndex >= 0 && intXIndex < 8 && intYIndex >= 0 && intYIndex < 8;
            // check to see if the user picked their piece
            boolean blnCorrectColor = blnInBounds
                    ? (chessBoard.isWhite(intXIndex, intYIndex) && blnServer)
                            || (!chessBoard.isWhite(intXIndex, intYIndex) && !blnServer)
                    : false;

            if (blnTurn && blnInBounds && !chessBoard.promotionInProgress()
                    && chessBoard.getPiece(intXIndex, intYIndex) != 0 && blnCorrectColor) {
                for (Piece piece : chessBoard.pieces) {
                    // get the piece at the pressed position
                    if ((evt.getX() <= piece.intXPos + 90 && evt.getX() >= piece.intXPos)
                            && (evt.getY() >= piece.intYPos && evt.getY() <= piece.intYPos + 90) && blnPressed == false
                            && piece.blnColor == blnServer) {
                        blnPressed = true;
                        temp = piece;
                        // record last position to calculate legal moves
                        temp.setPreviousPosition(temp.intXPos, temp.intYPos);
                        break;
                    }
                }
            } else if (blnTurn && blnInBounds && !chessBoard.promotionInProgress()
                    && chessBoard.getPiece(intXIndex, intYIndex) != 0 && !blnCorrectColor) {
                // if user clicks on the other side's piece, let the appropriate user know
                // reset message after 3 seconds
                Timer labelTimer = null;
                if (blnServer) {
                    serverInfoLabel.setText("You can only move your own pieces");
                    labelTimer = new Timer(3000, event -> serverInfoLabel.setText("CAPTURED PIECES"));
                } else {
                    clientInfoLabel.setText("You can only move your own pieces");
                    labelTimer = new Timer(3000, event -> clientInfoLabel.setText("CAPTURED PIECES"));
                }

                blnWrongColor = true;
                labelTimer.setRepeats(false);
                labelTimer.start();
            } else if (!blnTurn && blnInBounds && !chessBoard.promotionInProgress()
                    && chessBoard.getPiece(intXIndex, intYIndex) != 0 && blnCorrectColor) {
                // if user clicks on something when it is not thier turn, let the appropriate
                // user know
                // reset message after 3 seconds
                Timer labelTimer = null;
                if (blnServer) {
                    serverInfoLabel.setText("It is not your turn");
                    labelTimer = new Timer(3000, event -> serverInfoLabel.setText("CAPTURED PIECES"));
                } else {
                    clientInfoLabel.setText("It is not your turn");
                    labelTimer = new Timer(3000, event -> clientInfoLabel.setText("CAPTURED PIECES"));
                }

                labelTimer.setRepeats(false);
                labelTimer.start();
            } else {
                // something went wrong
                System.out.println("mouse error");
                blnMouseError = true;
            }
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if (blnPressed && temp != null && blnTurn && !chessBoard.promotionInProgress() && !blnMouseError) {
                // keep updating the piece position to the cursor location
                temp.setPosition(evt.getX(), evt.getY());
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            // get the position of where the piece will be dropped
            int intXPos = chessBoard.roundDown(evt.getX(), 90);
            int intYPos = chessBoard.roundDown(evt.getY(), 90);
            blnPressed = false;
            // make sure it is within the board array length
            boolean blnInBounds = intXPos / 90 >= 0 && intXPos / 90 < 8 && intYPos / 90 >= 0 && intYPos / 90 < 8;
            // if something went wrong, don't drop anything
            if (!blnTurn || blnWrongColor || blnMouseError) {
                return;
            } else if (temp != null && blnTurn && !blnInBounds && !chessBoard.promotionInProgress()) {
                // if user is placing outside the board
                temp.goBack();
            } else if (temp != null && blnTurn && blnInBounds && !chessBoard.promotionInProgress()) {
                if (chessBoard.executeMove(temp, intXPos, intYPos)) {
                    // when the move is succesful, update any possible captures
                    updateCaptures();
                    if (chessBoard.promotionInProgress()) {
                        // a promotion is occuring so let the appropriate user know
                        if (chessBoard.getPieceToPromote().blnColor) {
                            serverInfoLabel.setText("PROMOTION, CHOOSE A PIECE");
                        } else {
                            clientInfoLabel.setText("PROMOTION, CHOOSE A PIECE");
                        }
                    } else {
                        if (chessBoard.inCheck) {
                            if (blnServer) {
                                serverInfoLabel.setText("OH NO, YOU'RE IN CHECK");
                            } else {
                                clientInfoLabel.setText("OH NO, YOU'RE IN CHECK");
                            }
                        } else {
                            serverInfoLabel.setText("CAPTURED PIECES");
                            clientInfoLabel.setText("CAPTURED PIECES");
                        }
                        changeTurn();
                    }
                }
            }
            repaint();
        }
    }
}
