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
            if(blnFirstMove) {
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
        int intXIndex = intLastX/50;
        int intYIndex = intLastY/50;
        //y - 1, x - 2
        //y - 1, x + 2
        //y + 1, x - 2
        //y + 1, x + 2

        //y + 2, x - 1
        //y + 2, x + 1
        //y - 2, x - 1
        //y - 2, x + 1

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
}