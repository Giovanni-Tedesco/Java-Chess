import java.awt.*;
import java.io.*;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class Board {
    // Will be used later for networking
    // set to true to let white capture black, and vice versa

    /**
     * Boolean describing which colour the player is. True is white and false is
     * black
     */
    private boolean blnServer;
    // used to check if game is in promotion state, user can choose a piece tp
    // replace pawn

    /**
      *
      */
    private boolean blnPromotion;
    private boolean inCheck;

    private int[][] chessBoard = { { -1, -2, -3, -4, -5, -3, -2, -1 }, { -6, -6, -6, -6, -6, -6, -6, -6 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, { 6, 6, 6, 6, 6, 6, 6, 6 }, { 1, 2, 3, 4, 5, 3, 2, 1 }, };

    public ArrayList<Piece> pieces = new ArrayList<>();
    public ArrayList<Piece> captured = new ArrayList<>();
    // public static ArrayList<String> movesMade = new ArrayList<>();
    private ArrayList<Piece> whitePromotion = new ArrayList<>();
    private ArrayList<Piece> blackPromotion = new ArrayList<>();

    private Piece pieceToPromote = null;

    public Piece getPieceToPromote() {
        return pieceToPromote;
    }

    public ArrayList<Piece> getPromotionChoices(boolean blnServer) {
        return blnServer ? whitePromotion : blackPromotion;
    }

    public int capturedPieceCount(boolean blnColor, int intPiece) {
        int intPieceCount = 0;

        for (Piece p : captured) {
            if (p.blnColor != blnColor && p.intPiece == intPiece) {
                intPieceCount++;
            }
        }

        return intPieceCount;
    }

    public int roundDown(int n, int m) {
        return n >= 0 ? (n / m) * m : ((n - m + 1) / m) * m;
    }

    public void printCharboard() {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                System.out.print(chessBoard[i][j]);
            }
            System.out.println("");
        }
    }

    private void initBoard() {
        for (int i = 0, i2 = 7; i < 8; i++, i2--) {
            for (int j = 0, j2 = 7; j < 8; j++, j2--) {
                Piece p;
                int piece = chessBoard[i][j];
                if (piece != 0) {
                    p = new Piece(blnServer ? (j * 90) : (j2 * 90), blnServer ? (i * 90) : (i2 * 90), piece > 0, piece);
                } else {
                    continue;
                }
                pieces.add(p);
            }
        }
    }

    public String toCoord(int x1, int y1, int x2, int y2) {
        String pos = "";

        pos += (char) ((char) (x1) + 97);
        int loc = 8 - y1;
        pos += Integer.toString(loc);
        pos += ',';
        pos += (char) ((char) (x2) + 97);
        loc = 8 - y2;
        pos += loc;

        System.out.println(pos);
        return pos;
    }

    // public boolean searchMoves(String move) {
    // for (String mv : movesMade) {
    // if (mv.equalsIgnoreCase(move)) {
    // return true;
    // }
    // }
    // return false;
    // }
    // Changes the position of the pieces on the charBoard

    public void move(String move) {
        System.out.println("****************************");
        String[] moves = move.split(",");
        // System.out.println(moves[0]);

        Point p1 = coordToLoc(moves[0]);
        System.out.println(p1.x + " " + p1.y);
        Point p2 = coordToLoc(moves[1]);
        System.out.println(p2.x + " " + p2.y);

        if (moves[0].equals("e1") && moves[1].equals("g1")) {
            System.out.println("Gets here");
            castlesShort(p2.x * 90, p2.y * 90);
        }

        int intTemp = chessBoard[p1.y][p1.x];
        System.out.println("intTemp = " + intTemp);
        chessBoard[(int) (p1.y)][(int) (p1.x)] = 0;
        System.out.println(chessBoard[(int) (p1.y)][(int) (p1.x)] = 0);
        chessBoard[(int) (p2.y)][(int) (p2.x)] = intTemp;
        System.out.println("Position 2: " + chessBoard[(int) (p2.y)][(int) (p2.x)]);

        System.out.println("****************************");
    }

    public void castlesShort(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();

        while (pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            if (piece.intXPos == intXPos + 90 && piece.intYPos == intYPos) {
                System.out.println("Gets here");
                System.out.println(piece.intPiece);
                piece.setPosition(intXPos - 90, intYPos);

                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                chessBoard[intTempY][intTempX + 1] = 0;
                chessBoard[intTempY][intTempX - 1] = 1;
            }
        }
    }
    //
    // public void castlesLong(int intXPos, intYPos) {
    // Piece piece = pieceIterator.next();
    // while(pieceIterator.hasNext()) {
    // Piece piece = pieceIterator.next();
    // // if(piece.intXPos == intXPos)
    // }
    //
    // }

    public Point coordToLoc(String coord) {
        // System.out.println(newCoord);
        int x = (int) (coord.charAt(0) - 97);
        int y = (int) (7 - coord.charAt(1) + 49);
        // System.out.println(x + ", " + y);

        Point retCoord = new Point(x, y);

        return retCoord;
    }

    public boolean isWhite(int intXIndex, int intYIndex) {
        return chessBoard[intYIndex][intXIndex] > 0;
    }

    public int getPiece(int intXIndex, int intYIndex) {
        return chessBoard[intYIndex][intXIndex];
    }

    public void setPiece(int intXIndex, int intYIndex, int intPiece) {
        chessBoard[intYIndex][intXIndex] = intPiece;
    }

    // TODO: Clean this function up, the if statements have very similar code. Could
    // probably be simplified
    // return true if move was succesful
    public boolean executeMove(Piece piece, int intXPos, int intYPos) {
        SuperSocketMaster ssm = ChessGame.getNetwork();
        piece.setPosition(intXPos, intYPos);

        int intXIndexLast = blnServer ? piece.intLastX / 90 : 7 - (piece.intLastX / 90);
        int intYIndexLast = blnServer ? piece.intLastY / 90 : 7 - (piece.intLastY / 90);

        int intXIndex = blnServer ? intXPos / 90 : 7 - (intXPos / 90);
        int intYIndex = blnServer ? intYPos / 90 : 7 - (intYPos / 90);

        boolean blnLegalMove = piece.isLegalMove(chessBoard[intYIndex][intXIndex] != 0);
        // if player is white and the spot has a white piece
        boolean blnSamePieceWhite = blnServer && isWhite(intXIndex, intYIndex) && chessBoard[intYIndex][intXIndex] != 0;
        // if the player is black and the spot has a black piece
        boolean blnSamePieceBlack = !blnServer && !isWhite(intXIndex, intYIndex)
                && chessBoard[intYIndex][intXIndex] != 0;

        if (!blnLegalMove || blnSamePieceWhite || blnSamePieceBlack) {
            piece.goBack();
            return false;
        } else if (blnServer && !isWhite(intXIndex, intYIndex) && chessBoard[intYIndex][intXIndex] != 0) {
            // white captures black
            System.out.println("White -> Black");
            String result = toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex);
            // movesMade.add(result);
            move(result);
            printCharboard();
            // Utility.displayArray(movesMade);
            piece.setPosition(intXPos, intYPos);
            capturePiece(intXPos, intYPos);
            blnPromotion = promotable(piece);
            pieceToPromote = blnPromotion ? piece : null;
            if (ssm != null) {
                if (blnPromotion) {
                    ssm.sendText(result + ",promotion");
                } else {
                    ssm.sendText(result);
                }
            }
            return true;
        } else if (!blnServer && isWhite(intXIndex, intYIndex) && chessBoard[intYIndex][intXIndex] != 0) {
            // black captures white
            System.out.println("Black -> White");
            String result = toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex);
            move(result);
            // movesMade.add(result);
            printCharboard();
            // Utility.displayArray(movesMade);
            piece.setPosition(intXPos, intYPos);
            capturePiece(intXPos, intYPos);
            blnPromotion = promotable(piece);
            pieceToPromote = blnPromotion ? piece : null;
            if (ssm != null) {
                if (blnPromotion) {
                    ssm.sendText(result + ",promotion");
                } else {
                    ssm.sendText(result);
                }
            }
            return true;
        } else {
            String result = toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex);
            System.out.println(result);
            move(result);
            // movesMade.add(result);
            // Utility.displayArray(movesMade);
            printCharboard();

            piece.setPosition(intXPos, intYPos);
            piece.blnFirst = false;
            blnPromotion = promotable(piece);
            pieceToPromote = blnPromotion ? piece : null;
            if (ssm != null) {
                if (blnPromotion) {
                    ssm.sendText(result + ",promotion");
                } else {
                    ssm.sendText(result);
                }
            }
            return true;
        }
    }

    private boolean promotable(Piece piece) {
        int intXIndex = blnServer ? piece.intXPos / 90 : 7 - (piece.intXPos / 90);
        int intYIndex = blnServer ? piece.intYPos / 90 : 7 - (piece.intYPos / 90);

        return blnServer ? piece.intPiece == 6 && intYIndex == 0 && (intXIndex >= 0 && intXIndex <= 7)
                : piece.intPiece == 6 && intYIndex == 7 && (intXIndex >= 0 && intXIndex <= 7);
    }

    public boolean promotionInProgress() {
        return blnPromotion;
    }

    public void promotePiece(Piece piece) {
        Piece newPiece = piece;
        newPiece.setPosition(pieceToPromote.intXPos, pieceToPromote.intYPos);
        Iterator<Piece> pieceIterator = pieces.iterator();
        while (pieceIterator.hasNext()) {
            Piece temp = pieceIterator.next();
            if (temp.intXPos == newPiece.intXPos && temp.intYPos == newPiece.intYPos && temp.intPiece == 6) {
                System.out.println("GOT HERE");
                pieceIterator.remove();
                pieces.add(newPiece);
                break;
            }
        }

        int intXIndex = blnServer ? newPiece.intXPos / 90 : 7 - (newPiece.intXPos / 90);
        int intYIndex = blnServer ? newPiece.intYPos / 90 : 7 - (newPiece.intYPos / 90);

        chessBoard[intYIndex][intXIndex] = newPiece.blnColor ? newPiece.intPiece : -newPiece.intPiece;
        blnPromotion = false;
        ChessGame.getNetwork().sendText("promotion over," + intXIndex + "," + intYIndex + "," + newPiece.intPiece);

        System.out.println("AFTER PROMOTION");
        printCharboard();
    }

    public void capturePiece(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        while (pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            if ((piece.intXPos == intXPos && piece.intYPos == intYPos) && piece.blnColor != blnServer) {
                captured.add(piece);
                pieceIterator.remove();
            }
        }
    }

    // testing
    public Board() {
        blnServer = true;
        initBoard();

    }

    public Board(boolean blnServer) {
        this.blnServer = blnServer;
        initBoard();
        int[] intPieces = { 4, 1, 3, 2, 6 };
        for (int i = 0; i < 5; i++) {
            whitePromotion.add(new Piece(800 + (i * 80), 50, true, intPieces[i]));
            blackPromotion.add(new Piece(800 + (i * 80), 250, false, intPieces[i]));
        }
    }

}
