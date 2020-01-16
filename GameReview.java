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

public class GameReview extends KeyAdapter {

    private ReviewPanel reviewPanel;
    private ArrayList<String> moveList = new ArrayList<>();
    private Stack<Integer> pieceStack = new Stack<>();
    // String testMove = "e2,e4";
    private int moveIndex = 0;

    private JTextArea movesArea = new JTextArea();
    private JScrollPane scroll = new JScrollPane(movesArea);

    private JFileChooser chooseFile = new JFileChooser(new File("review"));
    private BufferedReader reader;
    private String strName;

    @Override
    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        System.out.println("I pressed a key");
        if (key == KeyEvent.VK_RIGHT) {
            if(moveIndex >= moveList.size()) {
                moveIndex = moveList.size() - 1;
                return;
            }
            String strMove = moveList.get(moveIndex);
            System.out.println("Testing move");
            reviewPanel.move(strMove, false);
            reviewPanel.repaint();
            moveIndex++;
        } else if(key == KeyEvent.VK_LEFT) {
            moveIndex--;
            if(moveIndex < 0) {
                moveIndex = 0;
                return;
            }
            String strMove = moveList.get(moveIndex);
            System.out.println("Testing move");
            reviewPanel.move(strMove, true);
            reviewPanel.repaint();
        }
    }

    public JPanel getChoosePanel() {
        return new ChoosePanel();
    }

    public GameReview() {
        MainMenu.frame.addKeyListener(this);
    }

    private class ReviewPanel extends JPanel {
        public HashMap<Integer, BufferedImage> pieceImages = new HashMap<>();
        public int [][] intBoard = { { -1, -2, -3, -4, -5, -3, -2, -1 }, { -6, -6, -6, -6, -6, -6, -6, -6 },
                { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 }, { 6, 6, 6, 6, 6, 6, 6, 6 }, { 1, 2, 3, 4, 5, 3, 2, 1 }, };

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            drawBoard(g);
            drawPieces(g);
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
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    int intPiece = intBoard[i][j];
                    if(intPiece == 0) {
                        continue;
                    } else if(intPiece > 0) {
                        g.drawImage(pieceImages.get(intPiece), j*90 , i*90, null);
                    } else if(intPiece < 0) {
                        g.drawImage(pieceImages.get((-intPiece)+6), j*90 , i*90, null);
                    }
                }
            }
        }

        private void initImages() {
            String path = "Assets/Pieces/";
            String[] fileNames = { "Rook.png", "Knight.png", "Bishop.png", "Queen.png", "King.png", "Pawn.png",
                    "RookBlack.png", "KnightBlack.png", "BishopBlack.png", "QueenBlack.png", "KingBlack.png",
                    "PawnBlack.png" };
            for (int i = 0; i < fileNames.length; i++) {
                pieceImages.put(i + 1, Utility.loadImage(path + fileNames[i]));
            }
        }

        public void move(String move, boolean blnBack) {
            String[] moves = move.split(",");

            Point p1 = blnBack ? coordToLoc(moves[1]) : coordToLoc(moves[0]);
            System.out.println(p1.x + " " + p1.y);
            Point p2 = blnBack ? coordToLoc(moves[0]) : coordToLoc(moves[1]);
            System.out.println(p2.x + " " + p2.y);


            int intTemp = intBoard[p1.y][p1.x];
            if(!blnBack) {
                pieceStack.push(intBoard[(int) (p2.y)][(int) (p2.x)]);
            }
            System.out.println("intTemp = " + intTemp);
            intBoard[(int) (p1.y)][(int) (p1.x)] = blnBack ? pieceStack.pop() : Piece.EMPTY;
            System.out.println(intBoard[(int) (p1.y)][(int) (p1.x)]);
            intBoard[(int) (p2.y)][(int) (p2.x)] = intTemp;
            System.out.println("Position 2: " + intBoard[(int) (p2.y)][(int) (p2.x)]);
            System.out.println("****************************");

            repaint();
        }

        public Point coordToLoc(String coord) {
            // System.out.println(newCoord);
            int x = (int) (coord.charAt(0) - 97);
            int y = (int) (7 - coord.charAt(1) + 49);
            // System.out.println(x + ", " + y);

            Point retCoord = new Point(x, y);

            return retCoord;
        }

        ReviewPanel(String strName) {
            super(null);
            initImages();
            setPreferredSize(Utility.panelDimensions);
            setBackground(Settings.isDark() ? Color.WHITE : Color.BLACK);
        }
    }

    private class ChoosePanel extends JPanel implements ActionListener {
        private JLabel reviewLabel = new JLabel("<html>" + "<div style='text-align: center;'>" + "Welcome to game review! " +
            "Here, you can review logs of previous games and perhaps reflect on past mistakes or triumphs. " +
            "To cycle forward through the log of moves, press the right arrow key. To cycle backwards, press the left arrow key." + "</div></html>");
        private JLabel errorLabel = new JLabel("Something went wrong loading the file");

        private JButton chooseButton = new JButton("CHOOSE A FILE");
        private JFileChooser chooseFile = new JFileChooser(new File("review"));
        private BufferedReader reader;
        private String strName;

        @Override
        public void actionPerformed(ActionEvent evt) {
            loadMoves();
            reviewPanel = new ReviewPanel(strName);
            Utility.changePanel(reviewPanel);
        }

        ChoosePanel() {
            super(null);
            setPreferredSize(Utility.panelDimensions);
            setBackground(Settings.isDark() ? Color.BLACK : Color.WHITE);

            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setVerticalAlignment(SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setSize(100, 50);
            errorLabel.setLocation(590, 50);
            Utility.setLabelStyle(errorLabel, 14);
            errorLabel.setVisible(false);
            add(errorLabel);

            reviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            reviewLabel.setVerticalAlignment(SwingConstants.CENTER);
            reviewLabel.setSize(800, 400);
            reviewLabel.setLocation(240, 100);
            Utility.setLabelStyle(reviewLabel, 24);
            add(reviewLabel);

            chooseButton.setSize(200, 100);
            chooseButton.setLocation(520, 600);
            chooseButton.addActionListener(this);
            Utility.setButtonStyle(chooseButton, 20);
            add(chooseButton);
        }

        public void loadMoves() {
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

            int intResult = chooseFile.showDialog(reviewPanel, "HELLO THERE");

            if(intResult == JFileChooser.APPROVE_OPTION) {
                errorLabel.setVisible(false);
                reader = Utility.getReader(chooseFile.getSelectedFile());
                System.out.println("Succ");
                String strContent = Utility.readLine(reader);
                strName = strContent;
                strContent = Utility.readLine(reader);
                while(!strContent.equals("end")) {
                    moveList.add(strContent.trim());
                    strContent = Utility.readLine(reader);
                }

                System.out.println(moveList);

                try {
                    reader.close();
                } catch(IOException e) {
                    e.printStackTrace();
                    errorLabel.setVisible(true);
                }
            } else {
                System.out.println("not succ");
                errorLabel.setVisible(true);
                //Utility.changePanel(new MainMenu().getMenuPanel());
            }
        }


    }
}
