import java.util.LinkedList;
import java.util.Arrays;

//This class holds functions for generating legal moves
//7 is subtracted from the coordinates for clients because their view is flipped
public class ChessUtility {

    //checks to see if a target int array is found inside a linked list
    public static boolean isInList(LinkedList<int[]> moveList, int[] intPostions) {
        System.out.println("positions: " + Arrays.toString(intPostions));
        for (int[] intLegalMove : moveList) {
            System.out.println("legal: " + Arrays.toString(intLegalMove));
            if (Arrays.equals(intPostions, intLegalMove)) {
                return true;
            }
        }
        return false;
    }

    //adds legal moves on found on the diagonals starting from a specified positions
    private static LinkedList<int[]> checkDiagonal(int intX, int intY, int[] intMoveRule, boolean blnWhite) {
        LinkedList<int[]> diagonalMoves = new LinkedList<int[]>();
        int intTempX = intX;
        int intTempY = intY;
        //get the correct board
        Board chessBoard = (BoardAnimation.getBoard() != null) ? BoardAnimation.getBoard() : TutorialMode.getBoard();

        //while in bounds
        while (intTempX >= 0 && intTempX < 8 && intTempY >= 0 && intTempY < 8) {
            if (intTempX == intX && intTempY == intY) {
                intTempX += intMoveRule[0];
                intTempY += intMoveRule[1];
                continue;
            } else if (chessBoard.getPiece(blnWhite ? intTempX : 7 - intTempX, blnWhite ? intTempY : 7 - intTempY) != Piece.EMPTY) {
                //if there is a piece at that spot


                //if current piece is same color as piece on the spot
                if ((blnWhite && chessBoard.isWhite(blnWhite ? intTempX : 7 - intTempX, blnWhite ? intTempY : 7 - intTempY))
                        || (!blnWhite && !chessBoard.isWhite(blnWhite ? intTempX : 7 - intTempX, blnWhite ? intTempY : 7 - intTempY))) {
                    break;
                } else if ((blnWhite && !chessBoard.isWhite(blnWhite ? intTempX : 7 - intTempX, blnWhite ? intTempY : 7 - intTempY))
                        || (!blnWhite && chessBoard.isWhite(blnWhite ? intTempX : 7 - intTempX, blnWhite ? intTempY : 7 - intTempY))) {
                    //if there is an opposite color piece on the spot, you can capture
                    diagonalMoves.add(new int[] {intTempX, intTempY});
                    break;
                } else {
                    diagonalMoves.add(new int[] {intTempX, intTempY});
                }
            } else {
                diagonalMoves.add(new int[] {intTempX, intTempY});
            }

            //keep adding in the diagonal directions
            intTempX += intMoveRule[0];
            intTempY += intMoveRule[1];
        }

        return diagonalMoves;
    }

    //adds legal moves on found on the diagonals starting from a specified positions but using a temporary board as a reference
    private static LinkedList<int[]> checkDiagonal(int intX, int intY, int[] moveRules, boolean blnWhite, int [][] intBoard) {
        LinkedList<int[]> diagonalMoves = new LinkedList<int[]>();
        int intTempX = intX;
        int intTempY = intY;

        while (intTempX >= 0 && intTempX < 8 && intTempY >= 0 && intTempY < 8) {
            if (intTempX == intX && intTempY == intY) {
                intTempX += moveRules[0];
                intTempY += moveRules[1];
                continue;
            } else if (intBoard[blnWhite ? intTempY : 7 - intTempY][blnWhite ? intTempX : 7 - intTempX] != Piece.EMPTY) {
                //if there is a piece at that spot

                //if piece is same color as piece on the spot
                if ((blnWhite && intBoard[blnWhite ? intTempY : 7 - intTempY][blnWhite ? intTempX : 7 - intTempX] > 0)
                        || (!blnWhite && intBoard[blnWhite ? intTempY : 7 - intTempY][blnWhite ? intTempX : 7 - intTempX] < 0)) {
                    break;
                } else if ((blnWhite && intBoard[blnWhite ? intTempY : 7 - intTempY][blnWhite ? intTempX : 7 - intTempX] < 0)
                        || (!blnWhite && intBoard[blnWhite ? intTempY : 7 - intTempY][blnWhite ? intTempX : 7 - intTempX] > 0)) {
                    //if there is an opposite color piece on the spot
                    diagonalMoves.add(new int[] { intTempX, intTempY });
                    break;
                } else {
                    diagonalMoves.add(new int[] { intTempX, intTempY });
                }
            } else {
                diagonalMoves.add(new int[] { intTempX, intTempY });
            }

            intTempX += moveRules[0];
            intTempY += moveRules[1];
        }

        return diagonalMoves;
    }

    //adds legal moves on found on the files starting from a specified positions
    private static LinkedList<int[]> checkFiles(int intX, int intY, boolean blnWhite) {
        LinkedList<int[]> fileMoves = new LinkedList<>();
        Board chessBoard = (BoardAnimation.getBoard() != null) ? BoardAnimation.getBoard() : TutorialMode.getBoard();

        // Check in x+ direction
        for (int i = intX; i < 8; i++) {
            if (i == intX)
                continue;
            if (chessBoard.getPiece(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY) != Piece.EMPTY) {
                //there is a piece at the spot
                if ((blnWhite && chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))
                        || (!blnWhite && !chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))) {
                            //there is a piece of the same color so break out
                    break;
                } else if ((blnWhite && !chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))
                        || (!blnWhite && chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))) {
                            //capturing space as there is an opposite color piece
                    fileMoves.add(new int[] { i, intY });
                    break;
                } else {
                    fileMoves.add(new int[] { i, intY });
                }
            } else {
                fileMoves.add(new int[] { i, intY });
            }
        }
        // Check in x- direction
        for (int i = intX; i >= 0; i--) {
            if (i == intX)
                continue;
            if (chessBoard.getPiece(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY) != Piece.EMPTY) {
                if ((blnWhite && chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))
                        || (!blnWhite && !chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))) {
                    break;
                } else if ((blnWhite && !chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))
                        || (!blnWhite && chessBoard.isWhite(blnWhite ? i : 7 - i, blnWhite ? intY : 7 - intY))) {
                    fileMoves.add(new int[] { i, intY });
                    break;
                } else {
                    fileMoves.add(new int[] { i, intY });
                }
            } else {
                fileMoves.add(new int[] { i, intY });
            }
        }
        // Check in y+ direction
        for (int i = intY; i < 8; i++) {
            if (i == intY)
                continue;
            if (chessBoard.getPiece(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i) != Piece.EMPTY) {
                if ((blnWhite && chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))
                        || (!blnWhite && !chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))) {
                    break;
                } else if ((blnWhite && !chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))
                        || (!blnWhite && chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))) {
                    fileMoves.add(new int[] { intX, i });
                    break;
                } else {
                    fileMoves.add(new int[] { intX, i });
                }
            } else {
                fileMoves.add(new int[] { intX, i });
            }
        }
        // Check in y- direction
        for (int i = intY; i >= 0; i--) {
            if (i == intY)
                continue;
            if (chessBoard.getPiece(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i) != Piece.EMPTY) {
                if ((blnWhite && chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))
                        || (!blnWhite && !chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))) {
                    break;
                } else if ((blnWhite && !chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))
                        || (!blnWhite && chessBoard.isWhite(blnWhite ? intX : 7 - intX, blnWhite ? i : 7 - i))) {
                    fileMoves.add(new int[] { intX, i });
                    break;
                } else {
                    fileMoves.add(new int[] { intX, i });
                }
            } else {
                fileMoves.add(new int[] { intX, i });
            }
        }

        return fileMoves;
    }

    //adds legal moves on found on the files starting from a specified positions but using a temporary board as the reference
    private static LinkedList<int[]> checkFiles(int intX, int intY, boolean blnWhite, int [][] intBoard) {
        LinkedList<int[]> fileMoves = new LinkedList<>();

        // Check in x+ direction
        for (int i = intX; i < 8; i++) {
            if (i == intX)
                continue;
            if (intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] != Piece.EMPTY) {
                if ((blnWhite && intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] > 0)
                        || (!blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] < 0)) {
                    break;
                } else if ((blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] < 0)
                        || (!blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] > 0)) {
                    fileMoves.add(new int[] { i, intY });
                    break;
                } else {
                    fileMoves.add(new int[] { i, intY });
                }
            } else {
                fileMoves.add(new int[] { i, intY });
            }
        }
        // Check in x- direction
        for (int i = intX; i >= 0; i--) {
            if (i == intX)
                continue;
            if ( intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] != Piece.EMPTY) {
                if ((blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] > 0)
                        || (!blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] < 0)) {
                    break;
                } else if ((blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] < 0)
                        || (!blnWhite &&  intBoard[blnWhite ? intY : 7 - intY][blnWhite ? i : 7 - i] > 0)) {
                    fileMoves.add(new int[] { i, intY });
                    break;
                } else {
                    fileMoves.add(new int[] { i, intY });
                }
            } else {
                fileMoves.add(new int[] { i, intY });
            }
        }
        // Check in y+ direction
        for (int i = intY; i < 8; i++) {
            if (i == intY)
                continue;
            if (intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] != Piece.EMPTY) {
                if ((blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] > 0)
                        || (!blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] < 0)) {
                    break;
                } else if ((blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] < 0)
                        || (!blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] > 0)) {
                    fileMoves.add(new int[] { intX, i });
                    break;
                } else {
                    fileMoves.add(new int[] { intX, i });
                }
            } else {
                fileMoves.add(new int[] { intX, i });
            }
        }
        // Check in y- direction
        for (int i = intY; i >= 0; i--) {
            if (i == intY)
                continue;
            if (intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] != Piece.EMPTY) {
                if ((blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] > 0)
                        || (!blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] < 0)) {
                    break;
                } else if ((blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] < 0)
                        || (!blnWhite && intBoard[blnWhite ? i : 7 - i][blnWhite ? intX : 7 - intX] > 0)) {
                    fileMoves.add(new int[] { intX, i });
                    break;
                } else {
                    fileMoves.add(new int[] { intX, i });
                }
            } else {
                fileMoves.add(new int[] { intX, i });
            }
        }

        return fileMoves;
    }

    //adds legal moves for the pawns from a specified position
    public static LinkedList<int[]> getLegalPawnMoves(boolean blnFirstMove, boolean blnHasPiece, boolean blnColour,
            int intLastX, int intLastY) {
        LinkedList<int[]> legalPawnMoves = new LinkedList<int[]>();
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;
        //if there is a piece, make sure it is on the diagonal
        //check if inbounds before adding the move
        if (blnHasPiece) {
            if(intYIndex-1 >= 0 && intYIndex-1 < 8 && intXIndex-1 >= 0 && intXIndex-1 < 8) {
                legalPawnMoves.add(new int[] { intXIndex - 1, intYIndex - 1 });
            }

            if(intYIndex-1 >= 0 && intYIndex-1 < 8 && intXIndex+1 >= 0 && intXIndex+1 < 8) {
                legalPawnMoves.add(new int[] { intXIndex + 1, intYIndex - 1 });
            }
        } else {

            if (blnFirstMove && (intYIndex-2 >= 0 && intYIndex-2 < 8 && intXIndex >= 0 && intXIndex < 8)) {
                legalPawnMoves.add(new int[] { intXIndex, intYIndex - 2 });
            }

            if(intYIndex-1 >= 0 && intYIndex-1 < 8 && intXIndex >= 0 && intXIndex < 8) {
                legalPawnMoves.add(new int[] { intXIndex, intYIndex - 1 });
            }
        }

        return legalPawnMoves;
    }

    //adds legal moves for the knight from a specified position
    public static LinkedList<int[]> getLegalKnightMoves(int intLastX, int intLastY) {
        //l formation moves
        int[] intTranslateY = { -1, -1, 1, 1, -2, -2, 2, 2 };
        int[] intTranslateX = { -2, 2, -2, 2, -1, 1, -1, 1 };
        LinkedList<int[]> legalKnightMoves = new LinkedList<int[]>();
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;

        for (int i = 0; i < intTranslateY.length; i++) {
            //check if in bounds before adding move
            if (intYIndex + intTranslateY[i] >= 0 && intYIndex + intTranslateY[i] < 8 && intXIndex + intTranslateX[i] >= 0
                    && intXIndex + intTranslateX[i] < 8) {
                legalKnightMoves.add(new int[] { intXIndex + intTranslateX[i], intYIndex + intTranslateY[i] });
            }
        }
        return legalKnightMoves;
    }

    //adds legal moves for the king from a specified position
    public static LinkedList<int[]> getLegalKingMoves(boolean blnFirst, int lastIndexX, int lastIndexY) {
        LinkedList<int[]> legalKingMoves = new LinkedList<int[]>();

        int[] intTranslateX = { 1, -1, 0, 0, 1, -1, 1, -1 }; // Just for regular moves in all directions
        int[] intTranslateY = { 0, 0, 1, -1, 1, -1, -1, 1 }; // Just for regular moves in all directions

        int intXIndex = lastIndexX / 90;
        int intYIndex = lastIndexY / 90;

        // Regular moves
        for (int i = 0; i < intTranslateX.length; i++) {
            //check if in bounds before adding move
            if (intYIndex + intTranslateY[i] >= 0 && intYIndex + intTranslateY[i] < 8 && intXIndex + intTranslateX[i] >= 0
                    && intXIndex + intTranslateX[i] < 8) {
                legalKingMoves.add(new int[] { intXIndex + intTranslateX[i], intYIndex + intTranslateY[i]});
            }
        }

        //castling moves
        if (blnFirst) {
            legalKingMoves.add(new int[] { intXIndex - 2, intYIndex });
            legalKingMoves.add(new int[] { intXIndex + 2, intYIndex });
        }

        return legalKingMoves;
    }

    //adds legal moves for the bishop from a specified position
    public static LinkedList<int[]> getLegalBishopMoves(int intLastX, int intLastY, boolean blnWhite) {
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;

        int[][] intPossibleMoves = { { -1, 1 }, // Upper left
                { 1, 1 }, // Upper right
                { 1, -1 }, // Botton right
                { -1, -1 } // Bottom left
        };

        LinkedList<int[]> legalBishopMoves = new LinkedList<int[]>();
        for (int i = 0; i < intPossibleMoves.length; i++) {
            legalBishopMoves.addAll(checkDiagonal(intXIndex, intYIndex, intPossibleMoves[i], blnWhite));
        }

        return legalBishopMoves;
    }

    //adds legal moves for the bishop from a specified position but using a temporary board instead
    public static LinkedList<int[]> getLegalBishopMoves(int intLastX, int intLastY, boolean blnWhite, int [][] intBoard) {
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;

        int[][] intPossibleMoves = { { -1, 1 }, // Upper left
                { 1, 1 }, // Upper right
                { 1, -1 }, // Botton right
                { -1, -1 } // Bottom left
        };

        LinkedList<int[]> legalBishopMoves = new LinkedList<int[]>();
        for (int i = 0; i < intPossibleMoves.length; i++) {
            legalBishopMoves.addAll(checkDiagonal(intXIndex, intYIndex, intPossibleMoves[i], blnWhite, intBoard));
        }

        return legalBishopMoves;
    }

    //adds legal moves for the rook from a specified position
    public static LinkedList<int[]> getLegalRookMoves(int intLastX, int intLastY, boolean blnWhite) {
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;
        LinkedList<int[]> legalRookMoves = new LinkedList<>();
        legalRookMoves.addAll(checkFiles(intXIndex, intYIndex, blnWhite));
        return legalRookMoves;
    }

    //adds legal moves for the rook from a specified position but using a temporary board instead
    public static LinkedList<int[]> getLegalRookMoves(int intLastX, int intLastY, boolean blnWhite, int [][] intBoard) {
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;
        LinkedList<int[]> legalRookMoves = new LinkedList<>();
        legalRookMoves.addAll(checkFiles(intXIndex, intYIndex, blnWhite, intBoard));
        return legalRookMoves;
    }

    //adds legal moves for the queen from a specified position
    public static LinkedList<int[]> getLegalQueenMoves(int intLastX, int intLastY, boolean blnWhite) {
        int intXIndex = intLastX / 90;
        int intYIndex = intLastY / 90;
        LinkedList<int[]> legalQueenMoves = new LinkedList<>();
        legalQueenMoves.addAll(checkFiles(intXIndex, intYIndex, blnWhite));
        int[][] intPossibleMoves = { { -1, 1 }, // Upper left
                { 1, 1 }, // Upper right
                { 1, -1 }, // Botton right
                { -1, -1 } // Bottom left
        };
        for (int i = 0; i < intPossibleMoves.length; i++) {
            legalQueenMoves.addAll(checkDiagonal(intXIndex, intYIndex, intPossibleMoves[i], blnWhite));
        }
        return legalQueenMoves;
    }
}
