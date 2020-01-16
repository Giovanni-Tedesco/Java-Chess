import java.awt.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.LinkedList;

public class Piece {
    public static final int EMPTY = 0;
    public static final int ROOK = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int QUEEN = 4;
    public static final int KING = 5;
    public static final int PAWN = 6;

    // Properties
    // True means white, false means black
    public boolean blnColor;
    public boolean blnFirst;
    public int intPiece;
    public int intXPos;
    public int intYPos;
    public int intLastX;
    public int intLastY;
    public LinkedList<int[]> legalMoves = new LinkedList<>();
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
        if (BoardAnimation.getBoard() == null) {
            return false;
        }

        for (int[] mv : moves) {
            if (BoardAnimation.getBoard().getPiece(mv[0], mv[1]) == KING) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<int[]> getLegalMoves() {
        if (intPiece == KNIGHT) {
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intLastX, intLastY);
            this.legalMoves = legalKnightMoves;
        } else if (intPiece == KING) {
            LinkedList<int[]> legalKingMoves = ChessUtility.getLegalKingMoves(blnFirst, intLastX, intLastY);
            this.legalMoves = legalKingMoves;
        } else if (intPiece == ROOK) {
            LinkedList<int[]> legalRookMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY, blnColor);
            this.legalMoves = legalRookMoves;
        } else if (intPiece == BISHOP) {
            LinkedList<int[]> legalBishopMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY, blnColor);
            this.legalMoves = legalBishopMoves;
        } else if (intPiece == QUEEN) {
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intLastX, intLastY, blnColor);
            this.legalMoves = legalQueenMoves;
        }

        return this.legalMoves;
    }

    public boolean isLegalMove(boolean blnHasPiece) {
        // int intXIndex = blnColor?intXPos/90:7-(intXPos/90);
        // int intYIndex = blnColor?intYPos/90:7-(intYPos/90);
        int[] position = { (intXPos / 90), (intYPos / 90) };
        if (intPiece == PAWN) {
            LinkedList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, blnColor, intLastX,
                    intLastY);
            return ChessUtility.isInList(legalPawnMoves, position);
        } else if (intPiece == KNIGHT) {
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intLastX, intLastY);
            return ChessUtility.isInList(legalKnightMoves, position);
        } else if (intPiece == KING) {
            LinkedList<int[]> legalKingMoves = ChessUtility.getLegalKingMoves(blnFirst, intLastX, intLastY);
            return ChessUtility.isInList(legalKingMoves, position);
        } else if (intPiece == ROOK) {
            LinkedList<int[]> legalRookMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY, blnColor);
            return ChessUtility.isInList(legalRookMoves, position);
        } else if (intPiece == BISHOP) {
            LinkedList<int[]> legalBishopMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY, blnColor);
            return ChessUtility.isInList(legalBishopMoves, position);
        } else if (intPiece == QUEEN) {
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intLastX, intLastY, blnColor);
            return ChessUtility.isInList(legalQueenMoves, position);
        }
        return false;
    }

}
