import java.awt.*;
import java.io.*;
import java.io.File;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Arrays;

public class Board {
    // Will be used later for networking
    // set to true to let white capture black, and vice versa

    /**
     * Boolean describing which colour the player is. True is white and false is
     * black
     */
    public boolean blnServer;
    // used to check if game is in promotion state, user can choose a piece tp
    // replace pawn

    /**
      *
      */
    private boolean blnPromotion;
    private boolean hasCastled = false;
    private boolean canCastle = true;
    private int[][] tempBoard;
    // This will likely be used when checking for checks impeding castling
    // To check impeding pieces that'll be done using a seperate function
    // private boolean canCastle = false;
    public boolean inCheck = false;

    private int[][] chessBoard = { { -1, -2, -3, -4, -5, -3, -2, -1 }, { -6, -6, -6, -6, -6, -6, -6, -6 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, { 6, 6, 6, 6, 6, 6, 6, 6 }, { 1, 2, 3, 4, 5, 3, 2, 1 }, };

    public ArrayList<Piece> pieces = new ArrayList<>();
    public HashMap<Integer, Piece> pieceLookup = new HashMap<>();
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

    private boolean inBounds(int intXIndex, int intYIndex) {
        return intXIndex >= 0 && intXIndex < 8 && intYIndex >= 0 && intYIndex < 8;
    }

    public void printCharboard() {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                System.out.print(chessBoard[i][j]);
            }
            System.out.println("");
        }
    }

    public void displayInformation() {
        System.out.println("Incheck: " + inCheck);
        System.out.println("Castled: " + hasCastled);
        System.out.println("The white king is at: " + pieceLookup.get(5).intXPos + ", " + pieceLookup.get(5).intYPos);
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

    // 0 - 5 will be white
    // 6 - 10 will be black
    public void initMap() {
        Iterator<Piece> pieceIter = pieces.iterator();

        while (pieceIter.hasNext()) {
            Piece piece = pieceIter.next();
            if (piece.intPiece != 6) {
                if (piece.blnColor) {
                    pieceLookup.put(piece.intPiece, piece);
                } else if (!piece.blnColor) {
                    pieceLookup.put(piece.intPiece + 5, piece);
                }
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

    // TODO: Implement castline
    // This may or may not work I have no idea.
    public boolean legalCastling() {
        Iterator<Piece> pieceIterator = pieces.iterator();

        while (pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();

            if (blnServer = !piece.blnColor) {
                for (int[] mv : piece.legalMoves) {
                    int i = mv[0];
                    int j = mv[1];
                    if (blnServer && i == 7 && j < 4) {
                        return false;
                    }
                    if (blnServer & i == 7 && j > 5) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void move(String move, boolean blnTemp) {
        System.out.println("****************************");
        System.out.println(inCheck);

        int[][] arr = blnTemp ? tempBoard : chessBoard;

        String[] moves = move.split(",");
        boolean impedingKnightWhite = (blnServer && arr[7][6] != Piece.EMPTY) ? true : false;
        boolean impedingKnightBlack = (blnServer && arr[0][1] != Piece.EMPTY) ? true : false;
        System.out.println("Impeding white knight: " + impedingKnightWhite);
        System.out.println("Impeding black knight: " + impedingKnightBlack);

        Point p1 = coordToLoc(moves[0]);
        System.out.println(p1.x + " " + p1.y);
        Point p2 = coordToLoc(moves[1]);
        System.out.println(p2.x + " " + p2.y);

        // This is simply to prevent someone from castling twice.
        if (!hasCastled && !inCheck) {
            if (moves[0].equals("e1") && moves[1].equals("g1")) {
                System.out.println("Gets here: found move");
                if (blnServer) {
                    castlesShort(p2.x * 90, p2.y * 90);
                    hasCastled = true;
                } else {
                    // System.out.println("Get's here: looking for piece");
                    // System.out.println("Looking for: x" + (7 - p2.x) * 90);
                    // System.out.println("Looking for: y" + (7 - p2.y) * 90);
                    castlesShort((7 - p2.x) * 90, (7 - p2.y) * 90);
                }
            } else if (moves[0].equals("e1") && moves[1].equals("c1") && impedingKnightWhite == false) {
                System.out.println("Gets here");
                if (blnServer) {
                    castlesLong(p2.x * 90, p2.y * 90);
                    hasCastled = true;
                } else {
                    castlesLong((7 - p2.x) * 90, (7 - p2.y) * 90);
                }
            }

            else if (moves[0].equals("e8") && moves[1].equals("g8")) {
                System.out.println("Get's here");
                if (!blnServer) {
                    castlesShortBlack((7 - p2.x) * 90, (7 - p2.y) * 90);
                    hasCastled = true;
                } else {
                    castlesShortBlack(p2.x * 90, p2.y * 90);
                }

            }

            else if (moves[0].equals("e8") && moves[1].equals("c8") && impedingKnightBlack == false) {
                System.out.println("Gets here");
                if (!blnServer) {
                    castlesLongBlack((7 - p2.x) * 90, (7 - p2.y) * 90);
                    hasCastled = true;
                } else {
                    castlesLongBlack(p2.x * 90, p2.y * 90);
                }
            }
        }

        int intTemp = arr[p1.y][p1.x];
        System.out.println("intTemp = " + intTemp);
        arr[(int) (p1.y)][(int) (p1.x)] = Piece.EMPTY;
        System.out.println(arr[(int) (p1.y)][(int) (p1.x)] = 0);
        arr[(int) (p2.y)][(int) (p2.x)] = intTemp;
        System.out.println("Position 2: " + arr[(int) (p2.y)][(int) (p2.x)]);

        System.out.println("****************************");
    }

    // This will only work for white.
    public void castlesShort(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        System.out.println("Searching intXPos for black is: " + (intXPos - 90));
        System.out.println("intYPos is: " + intYPos);

        while (pieceIterator.hasNext()) {

            Piece piece = pieceIterator.next();
            if (blnServer && piece.intXPos == intXPos + 90 && piece.intYPos == intYPos) {

                System.out.println("Gets here: checking the piece position");
                System.out.println(piece.intPiece);
                piece.setPosition(intXPos - 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                piece.setPosition(intXPos - 90, intYPos);
                chessBoard[intTempY][intTempX + 1] = Piece.EMPTY;
                chessBoard[intTempY][intTempX - 1] = Piece.ROOK;

            } else if (!blnServer && piece.intXPos == intXPos - 90 && piece.intYPos == intYPos) {
                System.out.println("Got here if server == false");
                System.out.println("The piece is: " + piece.intPiece);

                piece.setPosition(intXPos + 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("intTempX" + intTempX);
                System.out.println("intTempY" + intTempY);
                chessBoard[7 - intTempY][7 - intTempX + 1] = Piece.EMPTY;
                chessBoard[7 - intTempY][7 - intTempX - 1] = Piece.ROOK;
            }
        }
    }

    // TEMP: This is a temporary fix for black side castling short. All this does
    // is flip the white castlesShort but for now this will do.
    public void castlesShortBlack(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();
        System.out.println("Searching intXPos for black is: " + (intXPos - 90));
        System.out.println("intYPos is: " + intYPos);

        while (pieceIterator.hasNext()) {

            Piece piece = pieceIterator.next();
            if (!blnServer && piece.intXPos == intXPos - 90 && piece.intYPos == intYPos) {
                System.out.println(intXPos);
                System.out.println(intYPos);
                // Int yPos should be 0, int x Pos should be 90
                piece.setPosition(intXPos + 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("tempX: " + intTempX);
                System.out.println("tempY: " + intTempY);
                chessBoard[7 - intTempY][7 - intTempX + 1] = Piece.EMPTY;
                chessBoard[7 - intTempY][7 - intTempX - 1] = -Piece.ROOK;
            } else if (blnServer && piece.intXPos == intXPos + 90 && piece.intYPos == intYPos) {
                System.out.println(intXPos);
                System.out.println(intYPos);
                // Here int yPos should be 0 and intXPos should be 900(I think)
                piece.setPosition(intXPos - 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("intTempX: " + intTempX);
                System.out.println("intTempY: " + intTempY);

                chessBoard[intTempY][intTempX + 1] = Piece.EMPTY;
                chessBoard[intTempY][intTempX - 1] = -Piece.ROOK;

            }

        }
    }

    public void castlesLong(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();

        while (pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            if (blnServer && piece.intXPos == intXPos - 180 && piece.intYPos == intYPos) {
                System.out.println("Get's here");
                piece.setPosition(intXPos + 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("tempX: " + intTempX);
                System.out.println("tempY: " + intTempY);

                chessBoard[intTempY][intTempX - 2] = Piece.EMPTY;
                chessBoard[intTempY][intTempX + 1] = Piece.ROOK;
            } else if (!blnServer && piece.intXPos == intXPos + 180 && piece.intYPos == intYPos) {
                System.out.println("Get's here if found piece and server == false");
                piece.setPosition(intXPos - 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("tempX: " + intTempX);
                System.out.println("tempY: " + intTempY);

                chessBoard[7 - intTempY][7 - intTempX - 2] = Piece.EMPTY;
                chessBoard[7 - intTempY][7 - intTempX + 1] = Piece.ROOK;
            }
        }
    }

    // TEMP: This is a temporary fix for the black side long castles. This will
    // suffice until we can find a better solution for it.
    public void castlesLongBlack(int intXPos, int intYPos) {
        Iterator<Piece> pieceIterator = pieces.iterator();

        while (pieceIterator.hasNext()) {
            Piece piece = pieceIterator.next();
            if (blnServer && piece.intXPos == intXPos - 180 && piece.intYPos == intYPos) {
                System.out.println("Get's here");
                piece.setPosition(intXPos + 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("tempX" + intTempX);
                System.out.println("tempY" + intTempY);

                chessBoard[intTempY][intTempX - 2] = Piece.EMPTY;
                chessBoard[intTempY][intTempX + 1] = -Piece.ROOK;
            } else if (!blnServer && piece.intXPos == intXPos + 180 && piece.intYPos == intYPos) {
                System.out.println("Get's here if found piece and server == false");
                piece.setPosition(intXPos - 90, intYPos);
                int intTempX = intXPos / 90;
                int intTempY = intYPos / 90;
                System.out.println("tempX" + intTempX);
                System.out.println("tempY" + intTempY);
                chessBoard[7 - intTempY][7 - intTempX - 2] = Piece.EMPTY;
                chessBoard[7 - intTempY][7 - intTempX + 1] = -Piece.ROOK;
            }
        }

    }

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

    public void setCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public boolean isCheckMate() {
        if (blnServer && inCheck) {
            Piece king = pieceLookup.get(5);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intXPos, intYPos, true);
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intXPos, intYPos);

            legalQueenMoves.addAll(legalKnightMoves);
            if (legalQueenMoves.size() == 0) {
                return true;
            } else {
                return false;
            }
        } else if (!blnServer && inCheck) {
            Piece king = pieceLookup.get(10);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalQueenMoves(intXPos, intYPos, false);
            LinkedList<int[]> legalKnightMoves = ChessUtility.getLegalKnightMoves(intXPos, intYPos);

            legalQueenMoves.addAll(legalKnightMoves);
            if (legalQueenMoves.size() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks to see if a move gives a check to the opponent
     *
     * @param none
     * @return boolean true if the move gives a check and false if it does not
     */
    public boolean givesCheck() {
        if (blnServer) {
            Piece king = pieceLookup.get(10);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            System.out.println("intXPos: " + intXPos);
            System.out.println("intYPos: " + intYPos);

            for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
                if (chessBoard[p[1]][p[0]] == Piece.KNIGHT) {
                    System.out.println("Knight Check weee");
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, false)) {
                System.out.println(p[0] + " " + p[1]);
                if (chessBoard[p[1]][p[0]] == Piece.BISHOP || chessBoard[p[1]][p[0]] == Piece.QUEEN) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, false)) {
                System.out.println(p[0] + " " + p[1]);
                if (chessBoard[p[1]][p[0]] == Piece.QUEEN || chessBoard[p[1]][p[0]] == Piece.ROOK) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            //if white, then the pawn could be down left or down right
            if((inBounds((intXPos/90)-1, (intYPos/90)+1) ? chessBoard[(intYPos/90)+1][(intXPos/90)-1] == Piece.PAWN : false)
                    || (inBounds((intXPos/90)+1, (intYPos/90)+1) ? chessBoard[(intYPos/90)+1][(intXPos/90)+1] == Piece.PAWN : false)) {
                System.out.println("GET PWNED");
                return true;
            }
            return false;
        } else if (!blnServer) {
            Piece king = pieceLookup.get(5);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            System.out.println("intXPos: " + intXPos);
            System.out.println("intYPos: " + intYPos);
            System.out.println("white queen moves: " + ChessUtility.getLegalQueenMoves(intXPos, intYPos, true));
            System.out.println("black queen moves: " + ChessUtility.getLegalQueenMoves(intXPos, intYPos, false));
            for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
                System.out.println("Get's here: In knight check");
                System.out.println(p[1] + " " + p[0]);
                if (chessBoard[7-p[1]][7-p[0]] == -Piece.KNIGHT) {
                    System.out.println("Knight Check weee");
                    return true;
                }
            }
            for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, true)) {
                System.out.println(p[0] + " " + p[1]);
                if (chessBoard[7-p[1]][7-p[0]]== -Piece.BISHOP || chessBoard[7-p[1]][7-p[0]] == -Piece.QUEEN) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, true)) {
                System.out.println(p[0] + " " + p[1]);
                if (chessBoard[7-p[1]][7-p[0]] == -Piece.QUEEN || chessBoard[7-p[1]][7-p[0]] == -Piece.ROOK) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            if((inBounds(7-((intXPos/90)-1), 7-((intYPos/90)+1)) ? chessBoard[7-((intYPos/90)+1)][7-((intXPos/90)-1)] == -Piece.PAWN : false)
                    || (inBounds(7-((intXPos/90)+1), 7-((intYPos/90)+1)) ? chessBoard[7-((intYPos/90)+1)][7-((intXPos/90)+1)] == -Piece.PAWN : false)) {
                System.out.println("GET PWNED");
                return true;
            }
            return false;
        }

        return false;
    }

    /*
     * This will check if after making a move the king is still in check. If the
     * function returns true than then king is still in check and that causes the
     * move to be illegal
     */
    public boolean inCheck(boolean temp) {

        int[][] arr = temp ? tempBoard : chessBoard;

        if (blnServer) {
            Piece king = pieceLookup.get(5);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            System.out.println("Blocking intXPos: " + intXPos);
            System.out.println("Blocking intYPos: " + intYPos);

            for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
                if (arr[p[1]][p[0]] == -Piece.KNIGHT) {
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, true, arr)) {
                if (arr[p[1]][p[0]] == -Piece.BISHOP || arr[p[1]][p[0]] == -Piece.QUEEN) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, true, arr)) {
                if (arr[p[1]][p[0]] == -Piece.QUEEN || arr[p[1]][p[0]] == -Piece.ROOK) {
                    System.out.println("Rook check");
                    return true;
                }
            }

            if((inBounds((intXPos/90)-1, (intYPos/90)+1) ? arr[(intYPos/90)+1][(intXPos/90)-1] == -Piece.PAWN : false)
                    || (inBounds((intXPos/90)+1, (intYPos/90)+1) ? arr[(intYPos/90)+1][(intXPos/90)+1] == -Piece.PAWN : false)) {
                System.out.println("GET PWNED");
                return true;
            }
            return false;
        } else if (!blnServer) {
            Piece king = pieceLookup.get(10);
            int intXPos = king.intXPos;
            int intYPos = king.intYPos;
            for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
                if (arr[7-p[1]][7-p[0]] == Piece.KNIGHT) {
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, false, arr)) {
                if (arr[7-p[1]][7-p[0]] == Piece.BISHOP || arr[7-p[1]][7-p[0]] == Piece.QUEEN) {
                    System.out.println("Blocks check");
                    return true;
                }
            }

            for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, false, arr)) {
                if (arr[7-p[1]][7-p[0]] == Piece.QUEEN || arr[7-p[1]][7-p[0]] == Piece.ROOK) {
                    System.out.println("Blocks check");
                    return true;
                }
            }

            if((inBounds(7-((intXPos/90)-1), 7-((intYPos/90)+1)) ? arr[7-((intYPos/90)+1)][7-((intXPos/90)-1)] == Piece.PAWN : false)
                    || (inBounds(7-((intXPos/90)+1), 7-((intYPos/90)+1)) ? arr[7-((intYPos/90)+1)][7-((intXPos/90)+1)] == Piece.PAWN : false)) {
                System.out.println("GET PWNED");
                return true;
            }
            return false;
        }
        return false;
    }

    public int[][] deepCopy(int[][] arr) {
        int[][] ret = new int[arr.length][];

        for (int i = 0; i < arr.length; i++) {
            ret[i] = Arrays.copyOf(arr[i], arr[i].length);
        }

        return ret;
    }

    // Or walks into check
    public boolean stillInCheck(String strMove) {
        tempBoard = deepCopy(chessBoard);
        // Set to true because of temp
        move(strMove, true);

        if (inCheck(true)) {
            return true;
        } else {
            return false;
        }

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

        boolean blnLegalMove = false;

        if (inCheck == true) {
            blnLegalMove = piece.isLegalMove(chessBoard[intYIndex][intXIndex] != 0)
                    && !stillInCheck(toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex));
        } else {
            blnLegalMove = piece.isLegalMove(chessBoard[intYIndex][intXIndex] != 0) && !stillInCheck(toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex));
        }

        // if player is white and the spot has a white piece
        boolean blnSamePieceWhite = blnServer && isWhite(intXIndex, intYIndex) && chessBoard[intYIndex][intXIndex] != 0;
        // if the player is black and the spot has a black piece
        boolean blnSamePieceBlack = !blnServer && !isWhite(intXIndex, intYIndex)
                && chessBoard[intYIndex][intXIndex] != 0;

        if (isCheckMate()) {
            System.out.println("I've been mated feels bad man");
            return false;
        } else if (!blnLegalMove || blnSamePieceWhite || blnSamePieceBlack) {
            piece.goBack();
            return false;
        } else if (blnServer && !isWhite(intXIndex, intYIndex) && chessBoard[intYIndex][intXIndex] != 0) {
            // white captures black
            System.out.println("White -> Black");
            String result = toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex);
            // movesMade.add(result);
            move(result, false);
            printCharboard();
            // Utility.displayArray(movesMade);
            piece.setPosition(intXPos, intYPos);
            // if (inCheck && escapesCheck()) {
            // piece.goBack();
            // return false;
            // } else if (inCheck && !escapesCheck()) {
            // inCheck = false;
            // }

            System.out.println("Get's here: " + inCheck);

            if (givesCheck()) {
                System.out.println("Gives a check");
                result += "+";
            }
            piece.getLegalMoves();
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
            move(result, false);
            // movesMade.add(result);
            printCharboard();
            // Utility.displayArray(movesMade);
            piece.setPosition(intXPos, intYPos);
            // if (inCheck && escapesCheck()) {
            // piece.goBack();
            // return false;
            // } else if (inCheck && !escapesCheck()) {
            // inCheck = false;
            // }

            System.out.println("Get's here: " + inCheck);

            if (givesCheck()) {
                System.out.println("Gives a check");
                result += "+";
            }
            piece.getLegalMoves();
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
            move(result, false);
            // movesMade.add(result);
            // Utility.displayArray(movesMade);
            printCharboard();

            piece.setPosition(intXPos, intYPos);
            // if (inCheck && escapesCheck()) {
            // piece.goBack();
            // return false;
            // } else if (inCheck && !escapesCheck()) {
            // inCheck = false;
            // }

            System.out.println("Get's here: " + inCheck);

            if (givesCheck()) {
                System.out.println("Gives a check");
                result += "+";
            }
            System.out.println("Get's here");
            piece.getLegalMoves();
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

        return blnServer ? piece.intPiece == Piece.PAWN && intYIndex == 0 && (intXIndex >= 0 && intXIndex <= 7)
                : piece.intPiece == Piece.PAWN && intYIndex == 7 && (intXIndex >= 0 && intXIndex <= 7);
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
            if (temp.intXPos == newPiece.intXPos && temp.intYPos == newPiece.intYPos && temp.intPiece == Piece.PAWN) {
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
        if (ChessGame.getNetwork() != null) {
            ChessGame.getNetwork().sendText("promotion over," + intXIndex + "," + intYIndex + "," + newPiece.intPiece);
        }

        System.out.println("AFTER PROMOTION");
        printCharboard();
    }

    /**
     * Allows player to capture piece
     *
     * @param intXPos the x posiion
     * @param intYPos the y position
     * @return void
     */
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
        initMap();
        int[] intPieces = { 4, 1, 3, 2, 6 };
        for (int i = 0; i < 5; i++) {
            whitePromotion.add(new Piece(800 + (i * 80), 50, true, intPieces[i]));
            blackPromotion.add(new Piece(800 + (i * 80), 250, false, intPieces[i]));
        }
    }

}
