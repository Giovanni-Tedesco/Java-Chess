import java.awt.*;
import java.util.ArrayList;
public class Board {
    //Will be used later for networking
    //set to true to let white capture black, and vice versa
    boolean blnServer = true;
    private int [][] chessBoard = {
        {-1,-2,-3,-4,-5,-3,-2,-1},
        {-6,-6,-6,-6,-6,-6,-6,-6},
        {0, 0, 0, 0, 0, 0, 0, 0 },
        {0, 0, 0, 0, 0, 0, 0, 0 },
        {0, 0, 0, 0, 0, 0, 0, 0 },
        {0, 0, 0, 0, 0, 0, 0, 0 },
        {6, 6, 6, 6, 6, 6, 6, 6 },
        {1, 2, 3, 4, 5, 3, 2, 1 },
    };

    public ArrayList<Piece> pieces = new ArrayList<Piece>();
    public ArrayList<Piece> captured = new ArrayList<Piece>();

    public int roundDown(int n, int m) {
        return n >= 0 ? (n / m) * m : ((n - m + 1) / m) * m;
    }

    private void printCharboard(){
        for(int i = 0; i < chessBoard.length; i++) {
            for(int j = 0; j < chessBoard[0].length; j++) {
                System.out.print(chessBoard[i][j]);
            }
            System.out.println("");
        }
    }

    private void initBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Piece p;
                int piece = chessBoard[i][j];
                if(piece > 0){
                    p = new Piece(j * 90, i * 90, true, piece);
                } else if(piece < 0){
                    p = new Piece(j * 90, i * 90, false, piece);
                } else {
                    continue;
                }
            pieces.add(p);
            }
        }
    }

    private String toCoord(int x1, int y1, int x2, int y2){
        String pos = "";

        pos += (char)((char)(x1) + 97);
        int loc = 8 - y1;
        pos += Integer.toString(loc);
        pos += ',';
        pos += (char)((char)(x2) + 97);
        loc = 8 - y2;
        pos += loc;

        System.out.println(pos);
        return pos;
    }

    //Changes the position of the pieces on the charBoard
    private void move(String move){
        System.out.println("****************************");
        String[] moves = move.split(",");
        // System.out.println(moves[0]);

        Point p1 = coordToLoc(moves[0]);
        System.out.println(p1.x + " " + p1.y);
        Point p2 = coordToLoc(moves[1]);
        System.out.println(p2.x + " " + p2.y);

        int intTemp = chessBoard[p1.y][p1.x];
        System.out.println("intTemp = " + intTemp);
        chessBoard[(int)(p1.y)][(int)(p1.x)] = 0;
        System.out.println(chessBoard[(int)(p1.y)][(int)(p1.x)] = 0);
        chessBoard[(int)(p2.y)][(int)(p2.x)] = intTemp;
        System.out.println("Position 2: " + chessBoard[(int)(p2.y)][(int)(p2.x)]);

        System.out.println("****************************");
    }

    private Point coordToLoc(String coord){
        // System.out.println(newCoord);
        int x = (int)(coord.charAt(0) - 97);
        int y = (int)(7 - coord.charAt(1) + 49 );
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

    //TODO: Implement captures. Remove pieces and add pieces to captured list. Update array as well
    public void executeMove(Piece piece, int intXPos, int intYPos) {
        piece.setPosition(intXPos, intYPos);

        boolean blnLegalMove = piece.isLegalMove(chessBoard[intYPos/90][intXPos/90] != 0);
        //if player is white and the spot has a white piece
        boolean blnSamePieceWhite = blnServer && isWhite(intXPos/90, intYPos/90) && chessBoard[intYPos/90][intXPos/90] != 0;
        //if the player is black and the spot has a black piece
        boolean blnSamePieceBlack = !blnServer && !isWhite(intXPos/90, intYPos/90) && chessBoard[intYPos/90][intXPos/90] != 0;

        if(!blnLegalMove || blnSamePieceWhite || blnSamePieceBlack) {
            piece.setPosition(piece.intLastX, piece.intLastY);
        } else if(blnServer && !isWhite(intXPos/90, intYPos/90) && chessBoard[intYPos/90][intXPos/90] != 0) {
            //white captures black
            System.out.println("White -> Black");
            String result = toCoord(piece.intLastX/90, piece.intLastY/90, piece.intXPos/90, piece.intYPos/90);
            move(result);
            piece.setPosition(intXPos, intYPos);
            capturePiece(intXPos, intYPos);
        } else if(!blnServer && isWhite(intXPos/90, intYPos/90) && chessBoard[intYPos/90][intXPos/90] != 0) {
            //black captures white
            System.out.println("Black -> White");
            String result = toCoord(piece.intLastX/90, piece.intLastY/90, piece.intXPos/90, piece.intYPos/90);
            move(result);
            piece.setPosition(intXPos, intYPos);
            capturePiece(intXPos, intYPos);
        } else {
            String result = toCoord(piece.intLastX/90, piece.intLastY/90, piece.intXPos/90, piece.intYPos/90);
            System.out.println(result);
            move(result);
            piece.setPosition(intXPos, intYPos);
            piece.blnFirst = false;
        }
        printCharboard();
    }

    public void capturePiece(int intXPos, int intYPos) {
        for(int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            if((piece.intXPos == intXPos && piece.intYPos == intYPos) && piece.blnColor != blnServer) {
                captured.add(piece);
                pieces.remove(i);
            }
        }
    }

    public Board() {
        initBoard();
    }

}
