import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.awt.image.*;
import javax.swing.filechooser.FileFilter;

public class GameReview extends KeyAdapter {

    JFrame frame = new JFrame("Game Review");
    private ReviewPanel reviewPanel = new ReviewPanel();
    private ArrayList<String> moveList = new ArrayList<>();
    // String testMove = "e2,e4";
    private int moveIndex = 0;

    private JTextArea movesArea = new JTextArea();
    private JScrollPane scroll = new JScrollPane(movesArea);

    private JFileChooser chooseFile = new JFileChooser(new File("review"));
    private BufferedReader reader;
    private String strName;

    @Override
    public void keyPressed(KeyEvent evt) {
        if(moveIndex >= moveList.size()) {
            return;
        }
        int key = evt.getKeyCode();
        System.out.println("I pressed a key");
        if (key == KeyEvent.VK_RIGHT) {
            String strMove = moveList.get(moveIndex);
            System.out.println("Testing move");
            reviewPanel.move(strMove);
            reviewPanel.repaint();
            moveIndex++;
        } else if(key == KeyEvent.VK_LEFT) {
            moveIndex--;
            if(moveIndex < 0) {
                return;
            }
            String strMove = moveList.get(moveIndex);
            System.out.println("Testing move");
            reviewPanel.move(strMove);
            reviewPanel.repaint();
        }
    }

    GameReview() {
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
            reader = Utility.getReader(chooseFile.getSelectedFile());
            System.out.println("Succ");
            String strContent = Utility.readLine(reader);
            strName = strContent;
            strContent = Utility.readLine(reader);
            while(!strContent.equals("end")) {
                moveList.add(strContent.trim());
                strContent = Utility.readLine(reader);
            }

            try {
                reader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("not succ");
            //Utility.changePanel(new MainMenu().getMenuPanel());
        }



        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(reviewPanel);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new GameReview();
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

        public void move(String move) {
            String[] moves = move.split(",");

            Point p1 = coordToLoc(moves[0]);
            System.out.println(p1.x + " " + p1.y);
            Point p2 = coordToLoc(moves[1]);
            System.out.println(p2.x + " " + p2.y);

            int intTemp = intBoard[p1.y][p1.x];
            System.out.println("intTemp = " + intTemp);
            intBoard[(int) (p1.y)][(int) (p1.x)] = Piece.EMPTY;
            System.out.println(intBoard[(int) (p1.y)][(int) (p1.x)] = 0);
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

        ReviewPanel() {
            initImages();
            setPreferredSize(Utility.panelDimensions);
            setBackground(Settings.isDark() ? Color.WHITE : Color.BLACK);
        }
    }

    private class ChoosePanel extends JPanel {

    }
}
