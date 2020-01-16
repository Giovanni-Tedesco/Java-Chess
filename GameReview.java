import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.filechooser.FileFilter;

public class GameReview implements KeyListener {

    JFrame frame = new JFrame("Game Review");
    private BoardAnimation chessPanel = new BoardAnimation(true);
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
        Board chessBoard = BoardAnimation.getBoard();
        if (key == KeyEvent.VK_RIGHT) {
            String strMove = moveList.get(moveIndex);
            System.out.println("Testing move");
            chessBoard.move(strMove,false);
            String[] strMoveSplit = strMove.split(",");
            Point initPos = chessBoard.coordToLoc(strMoveSplit[0]);
            Point finalPos = chessBoard.coordToLoc(strMoveSplit[1]);
            Piece temp = null;

            int intX = initPos.x;
            int intY = initPos.y;
            int intFinalX = finalPos.x;
            int intFinalY = finalPos.y;

            for (int i = 0; i < chessBoard.pieces.size(); i++) {
                Piece p = chessBoard.pieces.get(i);
                if (p.intXPos / 90 == intFinalX && p.intYPos / 90 == intFinalY) {
                    chessBoard.captured.add(p);
                    chessBoard.pieces.remove(p);
                    break;
                }
            }

            for (Piece p : chessBoard.pieces) {
                if (p.intXPos / 90 == intX && p.intYPos / 90 == intY) {
                    temp = p;
                    break;
                }
            }

            chessPanel.updateCaptures();
            if (temp != null) {
                temp.setPosition(intFinalX * 90, intFinalY * 90);
            }
            chessPanel.repaint();
            moveIndex++;
        }
    }

    public void keyReleased(KeyEvent evt) {
        int code = evt.getKeyCode();
        System.out.println(code);
    }

    public void keyTyped(KeyEvent evt) {
        int code = evt.getKeyCode();
        System.out.println(code);

    }

    GameReview() {
        chessPanel.setPreferredSize(new Dimension(1280, 720));
        chessPanel.setLayout(null);
        chessPanel.initializeGame();

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

        int intResult = chooseFile.showDialog(chessPanel, "HELLO THERE");

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

        //moveList.add("e2,e4");
        //moveList.add("e6, e5");
        //moveList.add("g1,f3");
        // moveList.add("b8, c6");

        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chessPanel);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new GameReview();
    }

}
