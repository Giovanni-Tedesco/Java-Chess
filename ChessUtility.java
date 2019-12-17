import java.util.ArrayList;
import java.util.Arrays;
public class ChessUtility {
    public static ArrayList<int []> getLegalPawnMoves(boolean blnFirstMove, boolean blnHasPiece, int intLastX, int intLastY) {
        ArrayList<int []> legalPawnMoves = new ArrayList<int []>();
        int intXIndex = intLastX/50;
        int intYIndex = intLastY/50;
        if(blnHasPiece) {
            legalPawnMoves.add(new int [] {intXIndex-1, intYIndex-1});
            legalPawnMoves.add(new int [] {intXIndex+1, intYIndex-1});
        } else {
            if(blnFirstMove) legalPawnMoves.add(new int [] {intXIndex, intYIndex-2});

            legalPawnMoves.add(new int [] {intXIndex, intYIndex-1});
        }

        return legalPawnMoves;
    }

    public static ArrayList<int []> getLegalKnightMoves(int intLastX, int intLastY) {
        int [] translateY = {-1,-1,1,1,-2,-2,2,2};
        int [] translateX = {-2,2,-2,2,-1,1,-1,1};
        ArrayList<int []> legalKnightMoves = new ArrayList<int []>();
        int intXIndex = intLastX/50;
        int intYIndex = intLastY/50;

        for(int i = 0; i < translateY.length; i++) {
            if(intYIndex+translateY[i] >= 0 && intYIndex+translateY[i] < 8 && intXIndex+translateX[i] >= 0 && intXIndex+translateX[i] < 8 ) {
                legalKnightMoves.add(new int [] {intXIndex+translateX[i], intYIndex+translateY[i]});
            }
        }

        return legalKnightMoves;
    }

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

        int intXIndex = lastIndexX / 50;
        int intYIndex = lastIndexY / 50;

        //Regular moves
        for(int i = 0; i < translateX.length; i++){
            if(intXIndex + translateX[i] >= 0 && intXIndex + translateX[i] < 8 &&
                intYIndex + translateY[i] >= 0 && intYIndex + translateY[i] < 8) {
                    legalKingMoves.add(new int[] {intXIndex + translateX[i], intYIndex + translateY[i]});

                }
        }

        return legalKingMoves;
    }

    public static ArrayList<int []> getLegalRookMoves(int lastIndexX, int lastIndexY){
        int intX = lastIndexX / 50;
        int intY = lastIndexY / 50;
        ArrayList<int[]> legalRookMoves = new ArrayList<int[]>();
        int[] translateY = {0, 0, 1, -1};

        //Check in x+ direction
        for(int i = intX; i < 8; i++){
            if(i == intX){
                continue;
            }
            else if(BoardAnimation.getBoard().getPiece(i, intY) != 0 && i != intX){
                break;
            } else {
                legalRookMoves.add(new int[] {i + intX, intY});
            }
        }

        //Check in x- direction
        for(int i = intX; i > -1; i--){
            if(i == intX){
                continue;
            }
            else if(BoardAnimation.getBoard().getPiece(i, intY) != 0 && i != intX){
                break;
            } else {
                legalRookMoves.add(new int[] {(i - intX) + intX, intY});
            }
        }

        //Check in y+ direction
        for(int i = intY; i < 8; i++){
            if(i == intY){
                continue;
            }
            else if(BoardAnimation.getBoard().getPiece(intX, i) != 0 && i != intY){
                break;
            } else {
                legalRookMoves.add(new int[] {intX, i + intY} );
            }
        }

        //Check in y- direction
        for(int i = intY; i > -1; i--){
            if(i == intY){
                continue;
            }
            else if(BoardAnimation.getBoard().getPiece(intX, i) > 1){
                break;
            } else {
                legalRookMoves.add(new int[] {intX, (i - intY) + intY});
            }
        }

        return legalRookMoves;

    }
}
