import java.awt.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.LinkedList;

//this class is a blueprint for all the pieces used in the chess game
public class Piece {
    //PROPERTIES
    //constant values representing each piece type
    public static final int EMPTY = 0;
    public static final int ROOK = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int QUEEN = 4;
    public static final int KING = 5;
    public static final int PAWN = 6;

    //true means white, false means black
    public boolean blnColor;
    public boolean blnFirst;
    //holds the piece type
    public int intPiece;
    //holds the x position of the piece on the jpanel
    public int intXPos;
    //holds the y position of the piece on the jpanel
    public int intYPos;
    //holds the previous x position of the piece on the jpanel
    public int intLastX;
    //holds the previous y position of the piece on the jpanel
    public int intLastY;

    //list holding all of the pieces legal moves
    public LinkedList<int[]> legalMoves = new LinkedList<>();

    //CONSTRUCTOR
    public Piece(int intXPos, int intYPos, boolean blnColor, int intPiece) {
        this.blnColor = blnColor;
        this.intXPos = intXPos;
        this.intYPos = intYPos;
        this.intPiece = Math.abs(intPiece);
        this.blnFirst = true;
    }

    //METHODS
    //draw the appropriate piece image at its current posiion
    public void update(Graphics g) {
        if (this.blnColor) {// If the pieces colour is white
            g.drawImage(BoardAnimation.pieceImages.get(intPiece), intXPos, intYPos, null);
        } else {
            g.drawImage(BoardAnimation.pieceImages.get(intPiece + 6), intXPos, intYPos, null);

        }
    }
	//set current position to previous position
    public void setPreviousPosition(int intXPos, int intYPos) {
        intLastX = intXPos;
        intLastY = intYPos;
    }
	//set current position
    public void setPosition(int intXPos, int intYPos) {
        this.intXPos = intXPos;
        this.intYPos = intYPos;
    }
	
	//go back to previous position
    public void goBack() {
        setPosition(intLastX, intLastY);
    }

    //gets the legal moves of the appropriate piece
    public LinkedList<int[]> getLegalMoves() {
        if (intPiece == KNIGHT) {
            this.legalMoves =  ChessUtility.getLegalKnightMoves(intLastX, intLastY);
        } else if (intPiece == KING) {
            this.legalMoves = ChessUtility.getLegalKingMoves(blnFirst, intLastX, intLastY);
        } else if (intPiece == ROOK) {
            this.legalMoves = ChessUtility.getLegalRookMoves(intLastX, intLastY, blnColor);
        } else if (intPiece == BISHOP) {
            this.legalMoves = ChessUtility.getLegalBishopMoves(intLastX, intLastY, blnColor);
        } else if (intPiece == QUEEN) {
            this.legalMoves = ChessUtility.getLegalQueenMoves(intLastX, intLastY, blnColor);
        }

        return this.legalMoves;
    }

    //gets the legal moves of the appropriate piece
    public LinkedList<int[]> getCurrentLegalMoves() {
        if (intPiece == KNIGHT) {
            this.legalMoves =  ChessUtility.getLegalKnightMoves(intXPos, intYPos);
        } else if (intPiece == KING) {
            this.legalMoves = ChessUtility.getLegalKingMoves(blnFirst, intXPos, intYPos);
        } else if (intPiece == ROOK) {
            this.legalMoves = ChessUtility.getLegalRookMoves(intXPos, intYPos, blnColor);
        } else if (intPiece == BISHOP) {
            this.legalMoves = ChessUtility.getLegalBishopMoves(intXPos, intYPos, blnColor);
        } else if (intPiece == QUEEN) {
            this.legalMoves = ChessUtility.getLegalQueenMoves(intXPos, intYPos, blnColor);
        }

        return this.legalMoves;
    }


    
    //compares the list of legal moves to position for each piece based 
    //returns true if the move made was legal
    public boolean isLegalMove(boolean blnHasPiece) {

        int[] position = {(intXPos / 90),(intYPos / 90)};
        if (intPiece == PAWN) {
            LinkedList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, blnColor, intLastX,intLastY);
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
        return false;//return as illegal move if no legal moves were made
    }
}
