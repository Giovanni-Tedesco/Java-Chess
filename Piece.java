import java.awt.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
public class Piece {

    //Properties
    //True means white, false means black
    public boolean blnColor;
    public boolean blnFirst;
    public int intPiece;
    public int intXPos;
    public int intYPos;
    public int intLastX;
    public int intLastY;

    Piece(int xPos, int yPos, boolean color, int piece) {
        this.blnColor = color;
        this.intXPos = xPos;
        this.intYPos = yPos;
        this.intPiece = Math.abs(piece);
        this.blnFirst = true;
    }

    public void update(Graphics g){
        if(this.blnColor == true) {//If the pieces colour is white / pink
            g.setColor(Color.PINK);
        } else {
            g.setColor(Color.BLUE);//If the pieces colour is dark / blue
        }
        g.fillRect(intXPos, intYPos, 25, 50);
    }

    public void setPreviousPosition(int intPosX, int intPosY) {
        intLastX = intPosX;
        intLastY = intPosY;
    }

    public void setPosition(int intPosX, int intPosY) {
        intXPos = intPosX;
        intYPos = intPosY;
    }

    public boolean isLegalMove(boolean blnHasPiece) {
        int [] position = {(intXPos/50),(intYPos/50)};
        if(intPiece == 6) {
            ArrayList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, intLastX, intLastY);
            return ChessUtility.isInList(legalPawnMoves, position);
        } else if(intPiece == 2) {
            ArrayList<int []> legalKnightMoves = ChessUtility.getLegalKnightMoves(intLastX, intLastY);
            return ChessUtility.isInList(legalKnightMoves, position);
        } else if(intPiece == 5){
            ArrayList<int []> legalKingMoves = ChessUtility.getLegalKingMoves(blnFirst, intLastX, intLastY);
            return ChessUtility.isInList(legalKingMoves, position);
        } else if(intPiece == 1){
            ArrayList<int[]> legalRookMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY);
            return ChessUtility.isInList(legalRookMoves, position);
        } else if(intPiece == 3) {
            ArrayList<int []> legalBishopMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY);
            return ChessUtility.isInList(legalBishopMoves, position);
        }
        return false;
    }

}
