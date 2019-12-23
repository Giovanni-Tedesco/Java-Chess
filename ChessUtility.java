import java.util.ArrayList;
import java.util.Arrays;
public class ChessUtility {
    public static boolean isInList(ArrayList<int []> moveList, int [] positions) {
        System.out.println("positions: " + Arrays.toString(positions));
        for(int [] legalMove : moveList) {
            System.out.println("legal: " + Arrays.toString(legalMove));
            if(Arrays.equals(positions, legalMove)) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<int[]> checkDiagonal(int intX, int intY, int[] moveRules, boolean blnWhite) {
      ArrayList<int[]> moves = new ArrayList<int[]>();
      int tempX = intX;
      int tempY = intY;
      while(tempX >= 0 && tempX < 8 && tempY >= 0 && tempY < 8) {
        if(tempX == intX && tempY == intY) {
          tempX += moveRules[0];
          tempY += moveRules[1];
          continue;
        }
        else if(BoardAnimation.getBoard().getPiece(tempX, tempY) != 0){
            if((blnWhite && BoardAnimation.getBoard().isWhite(tempX, tempY)) || (!blnWhite && !BoardAnimation.getBoard().isWhite(tempX, tempY))) {
                break;
            } else if((blnWhite && !BoardAnimation.getBoard().isWhite(tempX, tempY)) || (!blnWhite && BoardAnimation.getBoard().isWhite(tempX, tempY))) {
                moves.add(new int[] {tempX, tempY});
                break;
            } else {
                moves.add(new int[] {tempX, tempY});
            }
        }
        else {
          moves.add(new int[] {tempX, tempY});
        }

        tempX += moveRules[0];
        tempY += moveRules[1];
      }

      return moves;
    }

    public static ArrayList<int []> getLegalPawnMoves(boolean blnFirstMove, boolean blnHasPiece, boolean blnColour,int intLastX, int intLastY) {
        ArrayList<int []> legalPawnMoves = new ArrayList<int []>();
        int intXIndex = intLastX/90;
        int intYIndex = intLastY/90;
        if(blnHasPiece) {
            legalPawnMoves.add(new int [] {intXIndex-1, intYIndex-1});
            legalPawnMoves.add(new int [] {intXIndex+1, intYIndex-1});
        } else {
            if(blnFirstMove){
                legalPawnMoves.add(new int [] {intXIndex, intYIndex-2});
            }
            legalPawnMoves.add(new int [] {intXIndex, intYIndex-1});
        }

        return legalPawnMoves;
    }

    public static ArrayList<int []> getLegalKnightMoves(int intLastX, int intLastY) {
        int [] translateY = {-1,-1,1,1,-2,-2,2,2};
        int [] translateX = {-2,2,-2,2,-1,1,-1,1};
        ArrayList<int []> legalKnightMoves = new ArrayList<int []>();
        int intXIndex = intLastX/90;
        int intYIndex = intLastY/90;

        for(int i = 0; i < translateY.length; i++) {
            if(intYIndex+translateY[i] >= 0 && intYIndex+translateY[i] < 8 && intXIndex+translateX[i] >= 0 && intXIndex+translateX[i] < 8 ) {
                legalKnightMoves.add(new int [] {intXIndex+translateX[i], intYIndex+translateY[i]});
            }
        }
        return legalKnightMoves;
    }

    //This will most likely be extremely frustrating to do as it involves moving two seperate pieces.
    //Perhaps we can pass in an array of past moves from the board to see if the king or rook has moved at all.
    // public static ArrayList<int []> castles(boolean kingMoved, boolean rookMoved, int lastIndexX, intLastIndexY){
    //
    //
    // }

    public static ArrayList<int []> getLegalKingMoves(boolean blnInCheck, int lastIndexX, int lastIndexY){
        ArrayList<int[]> legalKingMoves = new ArrayList<int[]>();

        int[] translateX = {1, -1, 0, 0, 1, -1, 1, -1}; //Just for regular moves
        int[] translateY = {0, 0, 1, -1, 1, -1, -1, 1}; //Just for regular moves

        int intXIndex = lastIndexX / 90;
        int intYIndex = lastIndexY / 90;

        //Regular moves
        for(int i = 0; i < translateX.length; i++){
            if(intXIndex + translateX[i] >= 0 && intXIndex + translateX[i] < 8 &&
                intYIndex + translateY[i] >= 0 && intYIndex + translateY[i] < 8) {
                    legalKingMoves.add(new int[] {intXIndex + translateX[i], intYIndex + translateY[i]});

                }
        }

        return legalKingMoves;
    }

    public static ArrayList<int[]> getLegalBishopMoves(int lastIndexX, int lastIndexY, boolean blnWhite){
      int intX = lastIndexX / 90;
      int intY = lastIndexY / 90;

      int[][] possibleMoves = {
        {-1, 1}, // Upper left
        {1, 1}, // Upper right
        {1, -1}, // Botton right
        {-1, -1} // Bottom left
      };

      ArrayList<int[]> legalBishopMoves = new ArrayList<int[]>();
      for(int i = 0; i < possibleMoves.length; i++) {
          legalBishopMoves.addAll(checkDiagonal(intX, intY, possibleMoves[i], blnWhite));
      }

      return legalBishopMoves;
    }

    private static ArrayList<int[]> checkFiles(int intX, int intY, boolean blnWhite) {
        ArrayList<int[]> fileMoves = new ArrayList<>();
        Board chessBoard = BoardAnimation.getBoard();

         //Check in x+ direction
        for(int i = intX; i < 8; i++){
            if(i == intX) continue;
            if(BoardAnimation.getBoard().getPiece(i, intY) != 0) {
                if((blnWhite && chessBoard.isWhite(i, intY)) || (!blnWhite && !chessBoard.isWhite(i, intY))) {
                    break;
                } else if((blnWhite && !chessBoard.isWhite(i, intY)) || (!blnWhite && chessBoard.isWhite(i, intY))) {
                    fileMoves.add(new int[] {i, intY});
                    break;
                } else {
                    fileMoves.add(new int[] {i, intY});
                }
            } else {
                fileMoves.add(new int[] {i, intY});
            }
        }
        //Check in x- direction
        for(int i = intX; i >= 0; i--) {
            if(i == intX) continue;
            if(BoardAnimation.getBoard().getPiece(i, intY) != 0) {
                if((blnWhite && chessBoard.isWhite(i, intY)) || (!blnWhite && !chessBoard.isWhite(i, intY))) {
                    break;
                } else if((blnWhite && !chessBoard.isWhite(i, intY)) || (!blnWhite && chessBoard.isWhite(i, intY))) {
                    fileMoves.add(new int[] {i, intY});
                    break;
                } else {
                    fileMoves.add(new int[] {i, intY});
                }
            } else {
                fileMoves.add(new int[] {i, intY});
            }
        }
        //Check in y+ direction
        for(int i = intY; i < 8; i++){
            if(i == intY) continue;
            if(BoardAnimation.getBoard().getPiece(intX, i) != 0) {
                if((blnWhite && chessBoard.isWhite(intX, i)) || (!blnWhite && !chessBoard.isWhite(intX, i))) {
                    break;
                } else if((blnWhite && !chessBoard.isWhite(intX, i)) || (!blnWhite && chessBoard.isWhite(intX, i))) {
                    fileMoves.add(new int[] {intX, i});
                    break;
                } else {
                    fileMoves.add(new int[] {intX, i});
                }
            } else {
                fileMoves.add(new int[] {intX, i});
            }
        }

        //Check in y- direction
        for(int i = intY; i >= 0; i--) {
            if(i == intY) continue;
            if(BoardAnimation.getBoard().getPiece(intX, i) != 0) {
                if((blnWhite && chessBoard.isWhite(intX, i)) || (!blnWhite && !chessBoard.isWhite(intX, i))) {
                    break;
                } else if((blnWhite && !chessBoard.isWhite(intX, i)) || (!blnWhite && chessBoard.isWhite(intX, i))) {
                    fileMoves.add(new int[] {intX, i});
                    break;
                } else {
                    fileMoves.add(new int[] {intX, i});
                }
            } else {
                fileMoves.add(new int[] {intX, i});
            }
        }

        return fileMoves;
    }

    public static ArrayList<int []> getLegalRookMoves(int lastIndexX, int lastIndexY, boolean blnWhite){
        int intX = lastIndexX / 90;
        int intY = lastIndexY / 90;
        ArrayList<int[]> legalRookMoves = new ArrayList<>();
        legalRookMoves.addAll(checkFiles(intX, intY, blnWhite));
        return legalRookMoves;
    }

    public static ArrayList<int []> getLegalQueenMoves(int intLastX, int intLastY, boolean blnWhite) {
        int intXIndex = intLastX/90;
        int intYIndex = intLastY/90;
        ArrayList<int []> legalQueenMoves = new ArrayList<>();
        legalQueenMoves.addAll(checkFiles(intXIndex, intYIndex, blnWhite));
        int[][] possibleMoves = {
          {-1, 1}, // Upper left
          {1, 1}, // Upper right
          {1, -1}, // Botton right
          {-1, -1} // Bottom left
        };
        for(int i = 0; i < possibleMoves.length; i++) {
            legalQueenMoves.addAll(checkDiagonal(intXIndex, intYIndex, possibleMoves[i], blnWhite));
        }
        return legalQueenMoves;
    }
}
