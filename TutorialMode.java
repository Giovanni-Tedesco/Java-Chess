import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
//this class implements the tutorial mode that is found in the help screen
public class TutorialMode implements ActionListener {
    //PROPERTIES
    private javax.swing.Timer tutorialTimer;
    //all the tutorials that can be done
    private enum tutorials {
        PAWN,
        ROOK,
        KNIGHT,
        BISHOP,
        QUEEN,
        KING,
        CAPTURING,
        PROMOTION
    }

    private static Board tutorialBoard = new Board(true);
    //this stack of tutorials keeps track of the tutorials remaining
    private Stack<tutorials> tutorialsLeft = new Stack<>();
    private TutorialAnimation tutorialPanel = new TutorialAnimation();
    //this represents the help screen number and is used to remember what screen the tutorial was launched from
    private int intScreenNum;
    private JLabel serverInfoLabel = new JLabel("CAPTURED PIECES");
    private JLabel[] serverCaptureLabels = new JLabel[5];
    private JButton backButton = new JButton("BACK");
    private JButton nextButton = new JButton("NEXT");
    private JButton homeButton = new JButton("MAIN MENU");
    private JButton helpButton = new JButton("HELP");
    private JButton retryButton = new JButton("RETRY TUTORIAL");
    private JLabel tutorialTitle = new JLabel();
    private JLabel tutorialDesc = new JLabel();

    //METHODS
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object event = evt.getSource();
        if (event == backButton || event == helpButton) {
            tutorialTimer.stop();
            //go back to the help screen at the position that was saved
            Utility.changePanel(new Help(intScreenNum).getHelpPanel());
        } else if (event == nextButton) {
            if (tutorialsLeft.empty()) {
                //end tutorial because the stack is empty
                tutorialPanel.endTutorial();
                tutorialPanel.repaint();
            } else {
                //run the next tutorial
                runTutorial();
            }
        } else if (event == homeButton) {
            tutorialTimer.stop();
            Utility.changePanel(new MainMenu().getMenuPanel());
        } else if (event == retryButton) {
            tutorialTimer.stop();
            //change to a new instance of the tutorial mode to restart
            Utility.changePanel(new TutorialMode(intScreenNum).tutorialPanel);
        }
    }

    //push all the tutorials to the stack
    private void initTutorials() {
        tutorialsLeft.push(tutorials.PROMOTION);
        tutorialsLeft.push(tutorials.CAPTURING);
        tutorialsLeft.push(tutorials.KING);
        tutorialsLeft.push(tutorials.QUEEN);
        tutorialsLeft.push(tutorials.BISHOP);
        tutorialsLeft.push(tutorials.KNIGHT);
        tutorialsLeft.push(tutorials.ROOK);
        tutorialsLeft.push(tutorials.PAWN);
    }

    //pop the stack and read the value to run the appropriate tutorial
    private void runTutorial() {
        switch (tutorialsLeft.pop()) {
            case PAWN:
                runPawnTutorial();
                break;
            case ROOK:
                runRookTutorial();
                break;
            case KNIGHT:
                runKnightTutorial();
                break;
            case BISHOP:
                runBishopTutorial();
                break;
            case QUEEN:
                runQueenTutorial();
                break;
            case KING:
                runKingTutorial();
                break;
            case CAPTURING:
                runCaptureTutorial();
                break;
            case PROMOTION:
                runPromotionTutorial();
                break;
        }
        tutorialPanel.repaint();
    }

    private void runPawnTutorial() {
        tutorialTitle.setText(tutorials.PAWN.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" + "Pawn pieces can only move forward. " +
            "For their first move, they can move two spaces forward. " + "Afterwards, they can only move forward one space at a time. " +
            "They can only capture pieces by moving diagonally. " + "To move the pawn, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a pawn and update the board array
        tutorialBoard.pieces.add(new Piece(3 * 90, 6 * 90, true, Piece.PAWN));
        tutorialBoard.setPiece(3, 6, Piece.PAWN);
    }

    private void runRookTutorial() {
        tutorialTitle.setText(tutorials.ROOK.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "A Rook piece can move vertically or horizontally, as long as there are no pieces in between the initial and final positions. " +
            "To move the rook, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a rook and update the board array
        tutorialBoard.pieces.add(new Piece(3 * 90, 3 * 90, true, Piece.ROOK));
        tutorialBoard.setPiece(3, 3, Piece.ROOK);
    }

    private void runKnightTutorial() {
        tutorialTitle.setText(tutorials.KNIGHT.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "Knight pieces can move forward, backward, left or right two squares and must then move one square in either perpendicular direction. " +
            "To move the knight, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a knight and update the board array
        tutorialBoard.pieces.add(new Piece(3 * 90, 3 * 90, true, Piece.KNIGHT));
        tutorialBoard.setPiece(3, 3, Piece.KNIGHT);
    }

    private void runBishopTutorial() {
        tutorialTitle.setText(tutorials.BISHOP.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "Bishop pieces can only move diagonally (in any direction), as long as there are no pieces in between the initial and final positions. " +
            "To move the bishop, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a bishop and update the board array
        tutorialBoard.pieces.add(new Piece(3 * 90, 3 * 90, true, Piece.BISHOP));
        tutorialBoard.setPiece(3, 3, Piece.BISHOP);
    }

    private void runQueenTutorial() {
        tutorialTitle.setText(tutorials.QUEEN.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "The Queen piece can move both diagonally, vertically, and horizontally, as long as there are no pieces in between the initial and final positions. " +
            "To move the queen, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a queen and update the board
        tutorialBoard.pieces.add(new Piece(3 * 90, 3 * 90, true, Piece.QUEEN));
        tutorialBoard.setPiece(3, 3, Piece.QUEEN);
    }

    private void runKingTutorial() {
        tutorialTitle.setText(tutorials.KING.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "The King piece can only move one space, diagonally, vertically, and horizontally " +
            "in any direction, as long as the final position does not put the king at risk of being captured. " +
            "If the King is at risk of being captured, the King must move to the next possible position. " +
            "To move the king, select it and drag it to the desired position. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add a king and update the board
        tutorialBoard.pieces.add(new Piece(3 * 90, 3 * 90, true, Piece.KING));
        tutorialBoard.setPiece(3, 3, Piece.KING);
    }

    private void runCaptureTutorial() {
        tutorialTitle.setText(tutorials.CAPTURING.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "To capture, simply move your piece to a position occupied by an opponents piece. " +
            "If you do capture a piece, the captured piece is removed from the board and the capture count above is updated. " +
            "Capturing pieces allows you to progress through the game and makes it easier the checkmate the opponents king. " +
            "If the move is not legal, the piece will go back to the original spot" + "</div></html>");
        clearChessBoard();
        //add white pieces
        tutorialBoard.pieces.add(new Piece(0 * 90, 7 * 90, true, Piece.ROOK));
        tutorialBoard.pieces.add(new Piece(1 * 90, 7 * 90, true, Piece.KNIGHT));
        tutorialBoard.pieces.add(new Piece(2 * 90, 7 * 90, true, Piece.QUEEN));

        //add black pieces
        int[] intBlackPieces = {-Piece.ROOK, -Piece.KNIGHT, -Piece.BISHOP, -Piece.QUEEN, -Piece.KING, -Piece.BISHOP, -Piece.KNIGHT, -Piece.ROOK
        };
        for (int i = 0; i<intBlackPieces.length; i++) {
            tutorialBoard.pieces.add(new Piece(i * 90, 0 * 90, false, intBlackPieces[i]));
            tutorialBoard.setPiece(i, 0, intBlackPieces[i]);
        }

        //update booard array with white pieces
        tutorialBoard.setPiece(0, 7, Piece.ROOK);
        tutorialBoard.setPiece(1, 7, Piece.KNIGHT);
        tutorialBoard.setPiece(2, 7, Piece.QUEEN);
    }

    private void runPromotionTutorial() {
        tutorialTitle.setText(tutorials.PROMOTION.toString());
        tutorialDesc.setText("<html>" + "<div style='text-align: center;'>" +
            "Promotion occurs if a pawn reaches the opposing end of the board. " +
            "A pawn can then be promoted or converted to a queen, rook, bishop, or knight. " +
            "When a promotion is occuring, the player needs to choose a piece from the captured display on the right side of the board. " +
            "Move your pawn to the last spot to promote it and try to capture the black knight" + "</div></html>");
        clearChessBoard();
        //add white piece
        tutorialBoard.pieces.add(new Piece(7 * 90, 1 * 90, true, Piece.PAWN));
        tutorialBoard.setPiece(7, 1, Piece.PAWN);
        //add black piece
        tutorialBoard.pieces.add(new Piece(0 * 90, 7 * 90, false, Piece.KNIGHT));
        tutorialBoard.setPiece(0, 7, -Piece.KNIGHT);
    }

    //empty the board array
    private void clearChessBoard() {
        tutorialBoard.pieces.clear();
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                tutorialBoard.setPiece(j, i, Piece.EMPTY);
            }
        }
    }

    //set properties for JComponents on screen
    public void initComponents() {
        backButton.setLocation(1175, 5);
        backButton.setSize(100, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);
        tutorialPanel.add(backButton);

        nextButton.setLocation(730, 660);
        nextButton.setSize(540, 50);
        nextButton.addActionListener(this);
        Utility.setButtonStyle(nextButton, 18);
        tutorialPanel.add(nextButton);

        serverInfoLabel.setSize(200, 20);
        serverInfoLabel.setLocation(900, 5);
        serverInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        serverInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(serverInfoLabel, 12);
        tutorialPanel.add(serverInfoLabel);
        for (int i = 0; i<serverCaptureLabels.length; i++) {
            serverCaptureLabels[i] = new JLabel("0");
            Utility.setLabelStyle(serverCaptureLabels[i], 18);
            serverCaptureLabels[i].setSize(60, 20);
            serverCaptureLabels[i].setLocation(800 + (i * 80), 190);
            serverCaptureLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            serverCaptureLabels[i].setVerticalAlignment(SwingConstants.CENTER);
            tutorialPanel.add(serverCaptureLabels[i]);
        }

        tutorialTitle.setSize(400, 100);
        tutorialTitle.setLocation(800, 220);
        Utility.setLabelStyle(tutorialTitle, 32);
        tutorialTitle.setHorizontalAlignment(SwingConstants.CENTER);
        tutorialTitle.setVerticalAlignment(SwingConstants.CENTER);
        tutorialPanel.add(tutorialTitle);

        tutorialDesc.setSize(400, 300);
        tutorialDesc.setLocation(800, 320);
        Utility.setLabelStyle(tutorialDesc, 16);
        tutorialDesc.setHorizontalAlignment(SwingConstants.CENTER);
        tutorialDesc.setVerticalAlignment(SwingConstants.CENTER);
        tutorialPanel.add(tutorialDesc);

        homeButton.addActionListener(this);
        retryButton.addActionListener(this);
        helpButton.addActionListener(this);
    }

    public static Board getBoard() {
        return tutorialBoard;
    }

    public JPanel getTutorialPanel() {
        return tutorialPanel;
    }

    //CONSTRUCTOR
    public TutorialMode(int intHelpScreen) {
        tutorialPanel.setPreferredSize(Utility.panelDimensions);
        intScreenNum = intHelpScreen;
        initTutorials();
        initComponents();
        runTutorial();
    }

    //this class handles the ui for the tutorial screen
    private class TutorialAnimation extends JPanel {
        //PROPERTIES
        private ArrayList<BufferedImage> whiteCaptureImages = new ArrayList<>();
        //array that stores the order for the captured labels
        private int[] intPieces = {
            Piece.QUEEN, Piece.ROOK, Piece.BISHOP, Piece.KNIGHT, Piece.PAWN
        };
        private boolean blnPressed = false, blnFinished = false;
        //piece to hold the current piece object being dragged
        private Piece tempPiece = null;

        //METHODS
        //removes tutorial JComponents and sets ui for ending the tutorial
        public void endTutorial() {
            blnFinished = true;
            remove(backButton);
            remove(nextButton);
            remove(tutorialDesc);
            remove(tutorialTitle);
            remove(serverInfoLabel);
            for (JLabel label: serverCaptureLabels) {
                remove(label);
            }
            repaint();

            homeButton.setSize(235, 115);
            homeButton.setLocation(102, 545);
            Utility.setButtonStyle(homeButton, 18);

            helpButton.setSize(235, 115);
            helpButton.setLocation(518, 545);
            Utility.setButtonStyle(helpButton, 18);

            retryButton.setSize(235, 115);
            retryButton.setLocation(930, 545);
            Utility.setButtonStyle(retryButton, 18);

            add(homeButton);
            add(helpButton);
            add(retryButton);
        }

        //load images into the list used for the captured count display
        private void initCaptureImages() {
            for (int intPiece: intPieces) {
                whiteCaptureImages.add(Utility.resizeImage(BoardAnimation.pieceImages.get(intPiece + 6), 60, 120));
            }
        }

        //get the count of all captured pieces and update the captured display
        public void updateCaptures() {
            int[] intPieces = { 4, 1, 3, 2, 6 };
            for (int i = 0; i<5; i++) {
                serverCaptureLabels[i].setText(tutorialBoard.capturedPieceCount(true, intPieces[i]) + "");
            }
        }

        private void drawBoard(Graphics g) {
            //get the board color set in the settings
            Color boardColor = Settings.getBoardColor();

            for (int i = 0; i<8; i++) {
                for (int j = 0; j<8; j++) {
                    //draw the board with alternating
                    g.setColor(((i % 2 == 0) == (j % 2 == 0)) ? Color.WHITE : boardColor);
                    g.fillRect(j * 90, i * 90, 90, 90);
                }
            }
        }

        //iterate through all the pieces and draw them
        private void drawPieces(Graphics g) {
            for (Piece p: tutorialBoard.pieces) {
                p.update(g);
            }
        }

        //draw the captured display
        private void drawCapturedPieces(Graphics g) {
            for (int i = 0; i<5; i++) {
                g.drawImage(whiteCaptureImages.get(i), 800 + (i * 80), 50, null);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!blnFinished) {
                drawBoard(g);
                drawPieces(g);
                g.setColor(Color.WHITE);
                g.drawLine(720, 0, 720, 720);
                drawCapturedPieces(g);
            } else {
                //load tutorial end
                g.drawImage(Utility.loadImage("Assets/" + (Settings.isDark() ? "Dark" : "White") + "_Help/tutorial.png"), 0, 0, null);
            }
        }

        //CONSTRUCTOR
        TutorialAnimation() {
            super(null);
            tutorialTimer = new javax.swing.Timer(1000 / 60, event -> repaint());
            BoardAnimation.initImages();
            setBackground(Settings.isDark() ? new Color(46, 44, 44) : Color.WHITE);
            initCaptureImages();
            addMouseListener(new TutorialMouse());
            addMouseMotionListener(new TutorialMouse());
            tutorialTimer.start();
        }

        //this class handles mouse input for the tutorial
        private class TutorialMouse extends MouseAdapter {
            //PROPERTIES
            // flag for whether the user tried to pick up the opponent's
            private boolean blnWrongColor;
            private boolean blnMouseError;

            //METHODS
            @Override
            public void mouseClicked(MouseEvent evt) {
                // get promotion choice and promote the piece
                if (tutorialBoard.promotionInProgress()) {
                    ArrayList<Piece> promotionChoices = tutorialBoard.getPromotionChoices(true);
                    for (Piece piece: promotionChoices) {
                        if ((evt.getX()<= piece.intXPos + 60 && evt.getX() >= piece.intXPos) &&
                            (evt.getY() >= piece.intYPos && evt.getY()<= piece.intYPos + 120)) {
                            tutorialBoard.promotePiece(piece);
                            serverInfoLabel.setText("CAPTURED PIECES");
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
                // get the position of the piece at the pressed
                int intXPos = tutorialBoard.roundDown(evt.getX(), 90);
                int intYPos = tutorialBoard.roundDown(evt.getY(), 90);
                // get the array index corresponding with the coordinates
                int intXIndex = intXPos / 90;
                int intYIndex = intYPos / 90;
                // check to see if click was within the board array
                boolean blnInBounds = intXIndex >= 0 && intXIndex<8 && intYIndex >= 0 && intYIndex<8;
                // check to see if the user picked their piece
                boolean blnCorrectColor = blnInBounds ? tutorialBoard.isWhite(intXIndex, intYIndex) : false;

                if (blnInBounds && !tutorialBoard.promotionInProgress() &&
                    tutorialBoard.getPiece(intXIndex, intYIndex) != 0 && blnCorrectColor) {
                    for (Piece piece: tutorialBoard.pieces) {
                        // get the piece at the pressed position
                        if ((evt.getX()<= piece.intXPos + 90 && evt.getX() >= piece.intXPos) &&
                            (evt.getY() >= piece.intYPos && evt.getY()<= piece.intYPos + 90) && !blnPressed && piece.blnColor) {
                            blnPressed = true;
                            tempPiece = piece;
                            // record last position to calculate legal moves
                            tempPiece.setPreviousPosition(tempPiece.intXPos, tempPiece.intYPos);
                            break;
                        }
                    }
                } else if (blnInBounds && !tutorialBoard.promotionInProgress() &&
                    tutorialBoard.getPiece(intXIndex, intYIndex) != 0 && !blnCorrectColor) {
                    //// if user clicks on the other side's piece, let the user know
                    // reset message after 3 seconds
                    serverInfoLabel.setText("You can only move your own pieces");
                    javax.swing.Timer labelTimer = new javax.swing.Timer(3000, event -> serverInfoLabel.setText("CAPTURED PIECES"));
                    blnWrongColor = true;
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
                if (blnPressed && tempPiece != null && !tutorialBoard.promotionInProgress() && !blnMouseError) {
                    // keep updating the piece position to the cursor location
                    tempPiece.setPosition(evt.getX(), evt.getY());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                // get the position of where the piece will be dropped
                int intXPos = tutorialBoard.roundDown(evt.getX(), 90);
                int intYPos = tutorialBoard.roundDown(evt.getY(), 90);
                blnPressed = false;
                // make sure it is within the board array length
                boolean blnInBounds = intXPos / 90 >= 0 && intXPos / 90<8 && intYPos / 90 >= 0 && intYPos / 90<8;
                // if something went wrong, don't drop anything
                if (blnWrongColor || blnMouseError) {
                    return;
                } else if (tempPiece != null && !blnInBounds && !tutorialBoard.promotionInProgress()) {
                    // placing outside the board
                    tempPiece.goBack();
                } else if (tempPiece != null && blnInBounds && !tutorialBoard.promotionInProgress()) {
                    if (tutorialBoard.executeMove(tempPiece, intXPos, intYPos)) {
                        updateCaptures();
                        if (tutorialBoard.promotionInProgress()) {
                            serverInfoLabel.setText("PROMOTION, CHOOSE A PIECE");
                        }
                    }
                }

                repaint();
            }
        }
    }
}
