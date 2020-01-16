import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.io.*;
import java.awt.image.*;
import javax.swing.filechooser.FileFilter;

//This class implements the inputs for reviewing game logs
public class GameReview extends KeyAdapter implements ActionListener {

    //PROPERTIES
    private ReviewPanel reviewPanel;
    //list to store all the moves read from the log file
    private ArrayList<String> moveList = new ArrayList<>();
    //stack of pieces to remember all pieces that have been replaced
    //this will be used to undo any captures in the log
    private Stack<Integer> pieceStack = new Stack<>();

    //current move
    private int intMoveIndex = 0;

    //text area that keeps track of what move was most recently executed
    private JTextArea movesArea = new JTextArea();
    private JScrollPane moveScroll = new JScrollPane(movesArea);

    //label for the names of players
    private JLabel titleLabel = new JLabel();

    private JButton backButton = new JButton("BACK");
    @Override
    public void keyPressed(KeyEvent evt) {
        int intKey = evt.getKeyCode();
        //pressing right means going forward through the move log
        if (intKey == KeyEvent.VK_RIGHT && intMoveIndex<moveList.size()) {
            //move is out of bounds so exit out of function
            if (intMoveIndex >= moveList.size()) {
                intMoveIndex = moveList.size() - 1;
                return;
            }

            String strMove = moveList.get(intMoveIndex);
            reviewPanel.move(strMove, false);
            reviewPanel.repaint();
            intMoveIndex++;
        } else if (intKey == KeyEvent.VK_LEFT && intMoveIndex > 0) {
            //pressing left means going backwars through the move log

            intMoveIndex--;
            if (intMoveIndex<0) { //move is out of bounds so exit out of function
                intMoveIndex = 0;
                return;
            }
            String strMove = moveList.get(intMoveIndex);
            reviewPanel.move(strMove, true);
            reviewPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == backButton) {
            Utility.changePanel(new MainMenu().getMenuPanel());
            MainMenu.frame.removeKeyListener(this);
        }
    }

    //set properties oj JComponents in the review panel
    private void initPanel() {
        movesArea.setEditable(false);
        movesArea.setOpaque(true);
        movesArea.setBackground(Settings.isDark() ? Color.WHITE : Color.BLACK);
        movesArea.setForeground(Settings.isDark() ? Color.BLACK : Color.WHITE);
        movesArea.setFont(Utility.getFont().deriveFont(Font.PLAIN, 16));

        moveScroll.setLocation(725, 215);
        moveScroll.setSize(550, 500);

        reviewPanel.add(moveScroll);

        backButton.setLocation(1175, 5);
        backButton.setSize(100, 20);
        backButton.addActionListener(this);
        Utility.setButtonStyle(backButton, 12);

        titleLabel.setLocation(725, 25);
        titleLabel.setSize(550, 200);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Utility.setLabelStyle(titleLabel, 22);

        reviewPanel.add(backButton);
        reviewPanel.add(titleLabel);
    }

    //return panel for choosing file to be used from the main menu
    public JPanel getChoosePanel() {
        return new ChoosePanel();
    }

    //CONTRUCTOR
    public GameReview() {
        //add key listener
        MainMenu.frame.addKeyListener(this);
        MainMenu.frame.setFocusable(true);
        MainMenu.frame.requestFocus();
    }

    //class that handles updating the chess array and updating the ui
    private class ReviewPanel extends JPanel {
        private String[] strPieceNames = {"rook", "knight", "bishop", "queen", "king", "pawn"};
        //map to hold references to all the needed images
        public HashMap<Integer, BufferedImage> pieceImages = new HashMap<>();
        //array representing the chess board
        public int[][] intBoard = {
            {-1, -2, -3, -4, -5, -3, -2, -1},
            {-6, -6, -6, -6, -6, -6, -6, -6},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {6, 6, 6, 6, 6, 6, 6, 6},
            {1, 2, 3, 4, 5, 3, 2, 1},
        };

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
            drawPieces(g);
        }

        private void drawBoard(Graphics g) {
            //get the board color set in the settings
            Color boardColor = Settings.getBoardColor();

            //draw the board with alternating colors
            for (int i = 0; i<8; i++) {
                for (int j = 0; j<8; j++) {
                    g.setColor(((i % 2 == 0) == (j % 2 == 0)) ? Color.WHITE : boardColor);
                    g.fillRect(j * 90, i * 90, 90, 90);
                }
            }
        }

        private void drawPieces(Graphics g) {
            for (int i = 0; i<8; i++) {
                for (int j = 0; j<8; j++) {
                    int intPiece = intBoard[i][j];
                    if (intPiece == 0) {
                        continue;
                    } else if (intPiece > 0) {
                        //draw a white piece
                        g.drawImage(pieceImages.get(intPiece), j * 90, i * 90, null);
                    } else if (intPiece<0) {
                        //draw a black piece
                        g.drawImage(pieceImages.get((-intPiece) + 6), j * 90, i * 90, null);
                    }
                }
            }
        }

        //load all the piece images to the image map
        private void initImages() {
            String path = "Assets/Pieces/";
            String[] fileNames = {
                "Rook.png", "Knight.png", "Bishop.png", "Queen.png", "King.png", "Pawn.png",
                "RookBlack.png", "KnightBlack.png", "BishopBlack.png", "QueenBlack.png", "KingBlack.png",
                "PawnBlack.png"
            };
            for (int i = 0; i<fileNames.length; i++) {
                pieceImages.put(i + 1, Utility.loadImage(path + fileNames[i]));
            }
        }

        //update the board array using the move sent
        public void move(String strMove, boolean blnBack) {
            strMove = strMove.replace("+", "");
            String[] moves = strMove.split(",");

            //if going back, the final position in the move should actually be the initial position
            Point p1 = blnBack ? coordToLoc(moves[1]) : coordToLoc(moves[0]);
            //if going back, the initial position in the move should actually be the final position
            Point p2 = blnBack ? coordToLoc(moves[0]) : coordToLoc(moves[1]);

            //get piece at initial position
            int intTemp = intBoard[p1.y][p1.x];
            if (!blnBack) {
                //push the piece at the final position onto the stack for saving when undoing captures
                pieceStack.push(intBoard[(int)(p2.y)][(int)(p2.x)]);
                if (intTemp != 0) {
                    //display the current move to the move area
                    movesArea.append(intTemp > 0 ? ("White " + strPieceNames[intTemp - 1] + ": " + strMove + "\n") :
                        ("Black " + strPieceNames[((-intTemp) - 1)] + ": " + strMove + "\n"));
                }
            }

            //if going back, pop the last piece from the stack and place it at the current spot, else, set it to be empty
            intBoard[(int)(p1.y)][(int)(p1.x)] = blnBack ? pieceStack.pop() : Piece.EMPTY;
            //the final position is set the piece that was at the initial position
            intBoard[(int)(p2.y)][(int)(p2.x)] = intTemp;
            repaint();
        }

        //converts a string coordinate, for ex, (e2), to an array location
        public Point coordToLoc(String coord) {
            //convert the letter to integer using ascii
            int intX = (int)(coord.charAt(0) - 97);
            int intY = (int)(7 - coord.charAt(1) + 49);

            return new Point(intX,intY);
        }

        //CONSTRUCTOR
        ReviewPanel(String strName) {
            super(null);
            initImages();
            setPreferredSize(Utility.panelDimensions);
            setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);
            String[] strNames = strName.split("vs");
            //set the name of both players to the title label
            titleLabel.setText(strNames[0] + " V.S " + strNames[1]);
        }
    }

    //this class implements the ui for choosing a log file
    private class ChoosePanel extends JPanel implements ActionListener {
        private JLabel reviewLabel = new JLabel("<html>" + "<div style='text-align: center;'>" + "Welcome to game review! " +
            "Here, you can review logs of previous games and perhaps reflect on past mistakes or triumphs. " +
            "To cycle forward through the log of moves, press the right arrow key. To cycle backwards, press the left arrow key." + "</div></html>");
        private JLabel errorLabel = new JLabel("Something went wrong loading the file");

        private JButton chooseButton = new JButton("CHOOSE A FILE");
        //open file chooser in the review directory
        private JFileChooser chooseFile = new JFileChooser(new File("review"));
        private BufferedReader reader;
        private String strName;

        @Override
        public void actionPerformed(ActionEvent evt) {
            if(evt.getSource() == chooseButton) {
                loadMoves();
                reviewPanel = new ReviewPanel(strName);
                Utility.changePanel(reviewPanel);
                initPanel();
            }
        }

        ChoosePanel() {
            super(null);
            setPreferredSize(Utility.panelDimensions);
            setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);

            //set properties for the error label
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setVerticalAlignment(SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setSize(100, 50);
            errorLabel.setLocation(590, 50);
            Utility.setLabelStyle(errorLabel, 14);
            errorLabel.setVisible(false);
            add(errorLabel);

            //set properties for the review label
            reviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            reviewLabel.setVerticalAlignment(SwingConstants.CENTER);
            reviewLabel.setSize(800, 400);
            reviewLabel.setLocation(240, 100);
            Utility.setLabelStyle(reviewLabel, 24);
            add(reviewLabel);

            //set properties for the choose button
            chooseButton.setSize(200, 100);
            chooseButton.setLocation(520, 600);
            chooseButton.addActionListener(this);
            Utility.setButtonStyle(chooseButton, 20);
            add(chooseButton);
        }

        //starts jfile chooser and reads the file. loads the contents to the moves arraylist
        public void loadMoves() {
            //filter that makes sure that the file is a text file and comes from the specified directory
            chooseFile.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    final String name = f.getName();
                    return name.endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return ".txt";
                }
            });

            //open up the file chooser dialog and get the result
            int intResult = chooseFile.showDialog(reviewPanel, "HELLO THERE");

            if (intResult == JFileChooser.APPROVE_OPTION) {
                errorLabel.setVisible(false);
                //when successful, get the file and read contents
                reader = Utility.getReader(chooseFile.getSelectedFile());
                String strContent = Utility.readLine(reader);
                strName = strContent;
                strContent = Utility.readLine(reader);
                //add moves from file
                while (!strContent.equals("end")) {
                    moveList.add(strContent.trim());
                    strContent = Utility.readLine(reader);
                }

                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    errorLabel.setVisible(true);
                }
            } else {
                errorLabel.setVisible(true);
            }
        }
    }
}
