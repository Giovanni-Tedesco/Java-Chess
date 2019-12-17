import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
//1) Have main screen -> options like play, help, settings, quit
//2) Have splash screen before main screen
//3) 1280 x 720, and has to be one window
//4) Help to have text and graphics maybe a tutorial
//5) Rock solid 60fps
//6) Settings -> change the port number and three other settings. Remember settings between instances
//7) Host game or join game option
//8) While in game, implement chat between players
//9) Gameplay must be animation based
//10) Use mouse or key listener to control animation
//11) Be able to go back and then play again
//12) Want to have some sound
//13) All images need to be made from scratch
//14) Write up req doc with needs but keep track of the wants

public class BoardAnimation extends JPanel {

    static Board chessBoard;
    public boolean pressed = false;
    public Piece temp = null;


    public static Board getBoard() {
        return chessBoard;
    }

    private void drawBoard(Graphics g) {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                g.setColor(((i % 2 == 0) == (j % 2 == 0))?Color.WHITE:Color.BLACK);
                g.fillRect(j * 50, i * 50, 50, 50);
            }
        }
    }

    private void drawPieces(Graphics g) {
        for(Piece p: chessBoard.pieces) {
            p.update(g);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }


    BoardAnimation() {
        super();
        chessBoard = new Board();
        addMouseListener(new MyMouseAdaptor());
        addMouseMotionListener(new MyMouseAdaptor());
    }

    private class MyMouseAdaptor extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent evt) {
            for(int i = 0; i < chessBoard.pieces.size(); i++){
                if((evt.getX() <= chessBoard.pieces.get(i).intXPos + 50 && evt.getX() >= chessBoard.pieces.get(i).intXPos)
                && (evt.getY() >= chessBoard.pieces.get(i).intYPos && evt.getY() <= chessBoard.pieces.get(i).intYPos + 50) && pressed == false) {
                    pressed = true;
                    temp = chessBoard.pieces.get(i);
                    temp.setPreviousPosition(temp.intXPos, temp.intYPos);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            if(pressed){
                movePiece(temp, evt);
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            finalMove(temp, evt);
        }

        private void movePiece(Piece piece, MouseEvent evt){
            piece.setPosition(evt.getX(), evt.getY());
            repaint();
        }

        private void finalMove(Piece piece, MouseEvent evt){
            pressed = false;
            piece.setPosition(chessBoard.roundDown(evt.getX(), 50), chessBoard.roundDown(evt.getY(), 50));

            boolean blnLegalMove = piece.isLegalMove(chessBoard.getPiece((piece.intXPos/50), (piece.intYPos/50)) != 0);

            if(!blnLegalMove || chessBoard.isWhite((piece.intXPos/50), (piece.intYPos/50))) {
                piece.setPosition(piece.intLastX, piece.intLastY);
            } else {
                String result = chessBoard.toCoord(piece.intLastX/50, piece.intLastY/50, piece.intXPos/50, piece.intYPos/50);
                System.out.println(result);
                chessBoard.move(result);
                chessBoard.printCharboard();
                piece.setPosition(chessBoard.roundDown(evt.getX(), 50), chessBoard.roundDown(evt.getY(), 50));
                piece.blnFirst = false;
            }

            repaint();
        }
    }
}
