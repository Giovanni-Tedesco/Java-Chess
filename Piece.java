import java.awt.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.LinkedList;

public class Piece {

    // Properties
    // True means white, false means black
    public boolean blnColor;
    public boolean blnFirst;
    public int intPiece;
    public int intXPos;
    public int intYPos;
    public int intLastX;
    public int intLastY;
    static boolean blnIsCheck;

    public Piece(int xPos, int yPos, boolean color, int piece) {
        this.blnColor = color;
        this.intXPos = xPos;
        this.intYPos = yPos;
        this.intPiece = Math.abs(piece);
        this.blnFirst = true;
    }

    public void update(Graphics g) {

        if (this.blnColor == true) {// If the pieces colour is white / pink
            g.drawImage(BoardAnimation.pieceImages.get(intPiece), intXPos, intYPos, null);
        } else {
            g.drawImage(BoardAnimation.pieceImages.get(intPiece + 6), intXPos, intYPos, null);// If the pieces colour is
                                                                                              // dark / blue
        }
    }

    public void setPreviousPosition(int intPosX, int intPosY) {
        intLastX = intPosX;
        intLastY = intPosY;
    }

    public void setPosition(int intPosX, int intPosY) {
        intXPos = intPosX;
        intYPos = intPosY;
    }

    public void goBack() {
        setPosition(intLastX, intLastY);
    }

    public boolean isCheck(LinkedList<int[]> moves) {
        for (int[] mv : moves) {
            if (BoardAnimation.getBoard().getPiece(mv[0], mv[1]) == 5) {
                return true;
            }

        }

        return false;
    }

    public boolean getCheck(boolean blnHasPiece) {
        int[] position = { (intXPos / 90), (intYPos / 90) };

        if (intPiece == 6) {
            LinkedList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, blnColor, intLastX,
                    intLastY);
            // return isCheck(legalPawnMoves);
            return false;
        } else if (intPiece == 2) {
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intLastX, intLastY);
            return isCheck(legalKnightMoves);
        } else if (intPiece == 5) {
            return false;
        } else if (intPiece == 1) {
            LinkedList<int[]> legalRookMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY, blnColor);
            return isCheck(legalRookMoves);
        } else if (intPiece == 3) {
            LinkedList<int[]> legalBishopMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY, blnColor);
            return isCheck(legalBishopMoves);
        } else if (intPiece == 4) {
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intLastX, intLastY, blnColor);
            return isCheck(legalQueenMoves);
        }

        return false;
    }

    public boolean isLegalMove(boolean blnHasPiece) {
        // int intXIndex = blnColor?intXPos/90:7-(intXPos/90);
        // int intYIndex = blnColor?intYPos/90:7-(intYPos/90);
        int[] position = { (intXPos / 90), (intYPos / 90) };
        if (intPiece == 6) {
            LinkedList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, blnColor, intLastX,
                    intLastY);
            blnIsCheck = isCheck(legalPawnMoves);
            return ChessUtility.isInList(legalPawnMoves, position);
        } else if (intPiece == 2) {
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intLastX, intLastY);
            blnIsCheck = isCheck(legalKnightMoves);
            return ChessUtility.isInList(legalKnightMoves, position);
        } else if (intPiece == 5) {
            LinkedList<int[]> legalKingMoves = ChessUtility.getLegalKingMoves(blnFirst, intLastX, intLastY);
            return ChessUtility.isInList(legalKingMoves, position);
        } else if (intPiece == 1) {
            LinkedList<int[]> legalRookMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY, blnColor);
            blnIsCheck = isCheck(legalRookMoves);
            return ChessUtility.isInList(legalRookMoves, position);
        } else if (intPiece == 3) {
            LinkedList<int[]> legalBishopMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY, blnColor);
            blnIsCheck = isCheck(legalBishopMoves);
            return ChessUtility.isInList(legalBishopMoves, position);
        } else if (intPiece == 4) {
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intLastX, intLastY, blnColor);
            blnIsCheck = isCheck(legalQueenMoves);
            return ChessUtility.isInList(legalQueenMoves, position);
        }
        return false;
    }

}
