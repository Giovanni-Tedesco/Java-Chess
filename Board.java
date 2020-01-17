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

/**
 * <h1>Board</h1> This class handles the logic associated with a game of
 * chess<br>
 * <p>
 *
 * @author Mehul Pillai, John Tedesco, Steven Lum
 * @version 1.0
 * @since 2020-1-17
 */

public class Board {
	// Properties
	/**
	 * Boolean describing which colour the player is. True is white and false is
	 * black
	 */
	public boolean blnServer;
	// used to check if game is in promotion state, user can choose a piece tp
	// replace pawn

	/**
	 * Boolean used to check if game is in promotion state, user can choose a piece
	 * to replace a pawn
	 */
	private boolean blnPromotion;

	/**
	 * Boolean used to check if game intance has commenced a castling move
	 */
	private boolean hasCastled = false;
	/**
	 * Boolean used to check if a player can commence a castling move
	 */
	private boolean canCastle = true;
	/**
	 * Boolean used to check if a player is in tutorial mode
	 */
	private boolean blnTutorial = false;

	/**
	 * Integer array representing a snapshot of the actual chessboard. This board
	 * will be used for checking checkmates without overwritting the actual
	 * chessboard
	 */
	private int[][] intTempBoard;

	/**
	 * Boolean used to check if a player is in check state
	 */
	public boolean blnInCheck = false;

	/**
	 * Integer array representing the actual chess board. Negative pieces are black
	 * while positive pieces are white
	 */
	private int[][] chessBoard = { { -1, -2, -3, -4, -5, -3, -2, -1 }, { -6, -6, -6, -6, -6, -6, -6, -6 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 6, 6, 6, 6, 6, 6, 6, 6 }, { 1, 2, 3, 4, 5, 3, 2, 1 } };

	/**
	 * ArrayList of piece objects used in the game
	 */
	public ArrayList<Piece> pieces = new ArrayList<>();
	/**
	 * HashMap holding key piece references to make accessing easier
	 */
	public HashMap<Integer, Piece> pieceLookup = new HashMap<>();

	/**
	 * ArrayList of piece objects that have been captured
	 */
	public ArrayList<Piece> captured = new ArrayList<>();
	/**
	 * ArrayList of piece objects representing white promotion choices
	 */
	private ArrayList<Piece> whitePromotion = new ArrayList<>();

	/**
	 * ArrayList of piece objects representing black promotion choices
	 */
	private ArrayList<Piece> blackPromotion = new ArrayList<>();

	/**
	 * Piece that is used to hold a reference to the pawn that will be promoted
	 */
	private Piece pieceToPromote = null;

	// METHODS
	/**
	 * Getter method for the pawn to be promoted
	 *
	 * @return a reference to the pawn to be promoted
	 */
	public Piece getPieceToPromote() {
		return pieceToPromote;
	}

	/**
	 * Setter method to make the board a tutorial instance
	 */
	public void isTutorialMode() {
		blnTutorial = true;
	}

	/**
	 * Getter method for the list of promotion choice
	 *
	 * @param blnServer boolean variable representing what instance of the game is
	 *                  running
	 * @return the list of promotion choice
	 */
	public ArrayList<Piece> getPromotionChoices(boolean blnServer) {
		return blnServer ? whitePromotion : blackPromotion;
	}

	/**
	 * Getter method for the list of promotion choice
	 *
	 * @param blnServer boolean variable representing what instance of the game is
	 *                  running
	 * @param intPiece  integer representing the piece type
	 * @return the number of specified captured pieces
	 */
	public int capturedPieceCount(boolean blnColor, int intPiece) {
		int intPieceCount = 0;

		for (Piece p : captured) {
			if (p.blnColor != blnColor && p.intPiece == intPiece) {
				intPieceCount++;
			}
		}

		return intPieceCount;
	}

	/**
	 * Rounds down number to the specified place
	 *
	 * @param intNum      the number to be rounded
	 * @param intRoundNum the place to round number to
	 * @return the list of promotion choice
	 */
	public int roundDown(int intNum, int intRoundNum) {
		return intNum >= 0 ? (intNum / intRoundNum) * intRoundNum
				: ((intNum - intRoundNum + 1) / intRoundNum) * intRoundNum;
	}

	/**
	 * Checks to see if the specified index is within chess board bounds
	 *
	 * @param intXIndex the column index
	 * @param intYIndex the row index
	 * @return true if in bounds and false if not
	 */
	private boolean inBounds(int intXIndex, int intYIndex) {
		return intXIndex >= 0 && intXIndex < 8 && intYIndex >= 0 && intYIndex < 8;
	}

	/**
	 * Utility function that prints out the chess board
	 */
	public void printCharboard() {
		for (int i = 0; i < chessBoard.length; i++) {
			for (int j = 0; j < chessBoard[0].length; j++) {
				System.out.print(chessBoard[i][j]);
			}
			System.out.println("");
		}
	}

	/**
	 * Utility function that prints game status
	 */
	public void displayInformation() {
		System.out.println("Incheck: " + blnInCheck);
		System.out.println("Castled: " + hasCastled);
		System.out.println("The white king is at: " + pieceLookup.get(5).intXPos + ", " + pieceLookup.get(5).intYPos);
	}

	/**
	 * Initalize piece arraylist
	 */
	private void initBoard() {
		// the array is read flipped for the client so that their pieces appear at the
		// bottom
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

	/**
	 * Initalize piece lookup HashMap
	 */
	public void initMap() {
		Iterator<Piece> pieceIter = pieces.iterator();
		// 0 - 5 will be white
		// 6 - 10 will be black
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

	/**
	 * Converts a pieces array location into a coordinate
	 *
	 * @param x1 Original x position
	 * @param y1 Original y position
	 * @param x2 Future x position
	 * @param y2 Future y position
	 * @return String of the coordinate move
	 *
	 */
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

	/**
	 * Updates the chessboard array
	 *
	 * @param strMove coordinates holding initial and final positons
	 * @param blnTemp boolean dictating whether to use the tempBoard or not
	 */
	public void move(String strMove, boolean blnTemp) {
		System.out.println("****************************");
		System.out.println(intTempBoard);

		int[][] intCurrentBoard = blnTemp ? intTempBoard : chessBoard;

		String[] strMoves = strMove.split(",");
		boolean impedingKnightWhite = (blnServer && intCurrentBoard[7][6] != Piece.EMPTY) ? true : false;
		boolean impedingKnightBlack = (blnServer && intCurrentBoard[0][1] != Piece.EMPTY) ? true : false;
		System.out.println("Impeding white knight: " + impedingKnightWhite);
		System.out.println("Impeding black knight: " + impedingKnightBlack);

		Point p1 = coordToLoc(strMoves[0]);
		System.out.println(p1.x + " " + p1.y);
		Point p2 = coordToLoc(strMoves[1]);
		System.out.println(p2.x + " " + p2.y);

		// This is simply to prevent someone from castling twice.
		if (!hasCastled && !blnInCheck) {
			if (strMoves[0].equals("e1") && strMoves[1].equals("g1")) {
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
			} else if (strMoves[0].equals("e1") && strMoves[1].equals("c1") && impedingKnightWhite == false) {
				System.out.println("Gets here");
				if (blnServer) {
					castlesLong(p2.x * 90, p2.y * 90);
					hasCastled = true;
				} else {
					castlesLong((7 - p2.x) * 90, (7 - p2.y) * 90);
				}
			} else if (strMoves[0].equals("e8") && strMoves[1].equals("g8")) {
				System.out.println("Get's here");
				if (!blnServer) {
					castlesShortBlack((7 - p2.x) * 90, (7 - p2.y) * 90);
					hasCastled = true;
				} else {
					castlesShortBlack(p2.x * 90, p2.y * 90);
				}

			} else if (strMoves[0].equals("e8") && strMoves[1].equals("c8") && impedingKnightBlack == false) {
				System.out.println("Gets here");
				if (!blnServer) {
					castlesLongBlack((7 - p2.x) * 90, (7 - p2.y) * 90);
					hasCastled = true;
				} else {
					castlesLongBlack(p2.x * 90, p2.y * 90);
				}
			}
		}

		int intTemp = intCurrentBoard[p1.y][p1.x];
		System.out.println("intTemp = " + intTemp);
		intCurrentBoard[(int) (p1.y)][(int) (p1.x)] = Piece.EMPTY;
		System.out.println(intCurrentBoard[(int) (p1.y)][(int) (p1.x)] = 0);
		intCurrentBoard[(int) (p2.y)][(int) (p2.x)] = intTemp;
		System.out.println("Position 2: " + intCurrentBoard[(int) (p2.y)][(int) (p2.x)]);

		System.out.println("****************************");
	}

	/**
	 * Short castles for white based on physical position
	 *
	 * @param intXPos pieces physical x-position on the screen
	 * @param intYPos pieces physcial y-position on the screen
	 *
	 */
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

	/**
	 * Short castles for the black pieces based on the physical position
	 *
	 * @param intXPos physcial x-position of the piece
	 * @param intYPos physical y-position of the piece
	 *
	 */
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

	/**
	 * Castles long for white based on pieces physical position
	 *
	 * @param intXPos physical x-position of the piece
	 * @param intYPos physcial y-position of the piece
	 */
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

	/**
	 * Castles long for white based on pieces physical position
	 *
	 * @param intXPos physical x-position of the piece
	 * @param intYPos physcial y-position of the piece
	 */
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

	/**
	 * Converts a coordinate into a physical location in the array
	 *
	 * @param coord String coordinate of the board
	 * @return A point with the x, and y location of the board
	 *
	 */
	public Point coordToLoc(String coord) {
		// System.out.println(newCoord);
		int x = (int) (coord.charAt(0) - 97);
		int y = (int) (7 - coord.charAt(1) + 49);
		// System.out.println(x + ", " + y);

		Point retCoord = new Point(x, y);

		return retCoord;
	}

	/**
	 * Checks specified index for a white piece
	 *
	 * @param intXIndex column index
	 * @param intYIndex row index
	 * @return true if there was a white piece
	 */
	public boolean isWhite(int intXIndex, int intYIndex) {
		return chessBoard[intYIndex][intXIndex] > 0;
	}

	/**
	 * Getter method for the piece type at a specified index
	 *
	 * @param intXIndex column index
	 * @param intYIndex row index
	 * @return return the integer piece at the index
	 */
	public int getPiece(int intXIndex, int intYIndex) {
		return chessBoard[intYIndex][intXIndex];
	}

	/**
	 * Setter method for setting a piece type at a specified index
	 *
	 * @param intXIndex column index
	 * @param intYIndex row index
	 * @param intPiece  the piece type
	 */
	public void setPiece(int intXIndex, int intYIndex, int intPiece) {
		chessBoard[intYIndex][intXIndex] = intPiece;
	}

	/**
	 * Setter method for changing the value of blnInCheck
	 *
	 * @param blnInCheck value to be set
	 */
	public void setCheck(boolean blnInCheck) {
		this.blnInCheck = blnInCheck;
	}

	/**
	 * Checks to see if a move gives check to the opponent
	 *
	 * @return return true if there was a check
	 */
	public boolean givesCheck() {
		if (blnServer) {
			Piece king = pieceLookup.get(10);
			int intXPos = (7 * 90) - king.intXPos;
			int intYPos = (7 * 90) - king.intYPos;
			System.out.println("intXPos: " + intXPos);
			System.out.println("intYPos: " + intYPos);

			for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
				if (chessBoard[7 - p[1]][7 - p[0]] == Piece.KNIGHT) {
					System.out.println("Knight Check weee");
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, false)) {
				System.out.println(p[0] + " " + p[1]);
				if (chessBoard[7 - p[1]][7 - p[0]] == Piece.BISHOP || chessBoard[7 - p[1]][7 - p[0]] == Piece.QUEEN) {
					System.out.println("diag check");
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, false)) {
				System.out.println(p[0] + " " + p[1]);
				if (chessBoard[7 - p[1]][7 - p[0]] == Piece.QUEEN || chessBoard[7 - p[1]][7 - p[0]] == Piece.ROOK) {
					System.out.println("file check");
					return true;
				}
			}

			intXPos = (7 * 90) - king.intXPos;
			intYPos = (7 * 90) - king.intYPos;
			// if white, then the pawn could be down left or down right
			if ((inBounds((intXPos / 90) - 1, (intYPos / 90) + 1)
					? chessBoard[(intYPos / 90) + 1][(intXPos / 90) - 1] == Piece.PAWN
					: false)
					|| (inBounds((intXPos / 90) + 1, (intYPos / 90) + 1)
							? chessBoard[(intYPos / 90) + 1][(intXPos / 90) + 1] == Piece.PAWN
							: false)) {
				System.out.println("GET PWNED");
				return true;
			}
			return false;
		} else if (!blnServer) {
			Piece king = pieceLookup.get(5);
			int intXPos = (7 * 90) - king.intXPos;
			int intYPos = (7 * 90) - king.intYPos;
			System.out.println("intXPos: " + intXPos);
			System.out.println("intYPos: " + intYPos);
			// System.out.println("white queen moves: " +
			// ChessUtility.getLegalQueenMoves(intXPos, intYPos, true));
			// System.out.println("black queen moves: " +
			// ChessUtility.getLegalQueenMoves(intXPos, intYPos, false));
			for (int[] p : ChessUtility.getLegalKnightMoves(intXPos, intYPos)) {
				System.out.println("Get's here: In knight check");
				System.out.println(p[1] + " " + p[0]);
				if (chessBoard[p[1]][p[0]] == -Piece.KNIGHT) {
					System.out.println("Knight Check weee");
					return true;
				}
			}
			for (int[] p : ChessUtility.getLegalBishopMoves(intXPos, intYPos, true)) {
				System.out.println(p[0] + " " + p[1]);
				if (chessBoard[p[1]][p[0]] == -Piece.BISHOP || chessBoard[p[1]][p[0]] == -Piece.QUEEN) {
					System.out.println("diag check");
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalRookMoves(intXPos, intYPos, true)) {
				System.out.println(p[0] + " " + p[1]);
				if (chessBoard[p[1]][p[0]] == -Piece.QUEEN || chessBoard[p[1]][p[0]] == -Piece.ROOK) {
					System.out.println("Rook check");
					return true;
				}
			}

			if ((inBounds((intXPos / 90) - 1, (intYPos / 90) + 1)
					? chessBoard[(intYPos / 90) + 1][(intXPos / 90) - 1] == -Piece.PAWN
					: false)
					|| (inBounds((intXPos / 90), (intYPos / 90) + 1)
							? chessBoard[(intYPos / 90) + 1][(intXPos / 90)] == -Piece.PAWN
							: false)) {
				System.out.println("GET PWNED");
				return true;
			}
			return false;
		}

		return false;
	}

	/**
	 * Get's the kings position while checking for checkmate
	 *
	 * @param blnColor color of the piece being moved
	 * @param intBoard the chess board
	 * @return Point object containing the kings x and y position
	 */
	private Point getKingPosition(boolean blnColor, int[][] intBoard) {
		int intTarget = blnColor ? Piece.KING : -Piece.KING;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (intTarget == intBoard[i][j]) {
					return new Point(j, i);
				}
			}
		}

		return null;
	}

	/**
	 * This will check if after making a move the king is still in check. If the
	 * function returns true than then king is still in check and that causes the
	 * move to be illegal
	 *
	 * @param blnTemp if you are using the temporary board or not.
	 * @return boolean true if the piece is in check false if it is not
	 */
	public boolean inCheck(boolean blnTemp) {

		int[][] intCurrentBoard = blnTemp ? intTempBoard : chessBoard;

		if (blnServer) {
			Point kingPoint = getKingPosition(true, intCurrentBoard);
			int intXPos = 0;
			int intYPos = 0;
			if (kingPoint == null) {
				return true;
				// Piece king = pieceLookup.get(5);
				// intXPos = king.intXPos/90;
				// intYPos = king.intYPos/90;
			} else {
				intXPos = kingPoint.x;
				intYPos = kingPoint.y;
			}

			System.out.println("Blocking intXPos: " + intXPos);
			System.out.println("Blocking intYPos: " + intYPos);

			for (int[] p : ChessUtility.getLegalKnightMoves(intXPos * 90, intYPos * 90)) {
				if (intCurrentBoard[p[1]][p[0]] == -Piece.KNIGHT) {
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalBishopMoves(intXPos * 90, intYPos * 90, true, intCurrentBoard)) {
				if (intCurrentBoard[p[1]][p[0]] == -Piece.BISHOP || intCurrentBoard[p[1]][p[0]] == -Piece.QUEEN) {
					System.out.println("Rook check");
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalRookMoves(intXPos * 90, intYPos * 90, true, intCurrentBoard)) {
				if (intCurrentBoard[p[1]][p[0]] == -Piece.QUEEN || intCurrentBoard[p[1]][p[0]] == -Piece.ROOK) {
					System.out.println("Rook check");
					return true;
				}
			}

			if ((inBounds(intXPos - 1, intYPos + 1) ? intCurrentBoard[intYPos + 1][intXPos - 1] == -Piece.PAWN : false)
					|| (inBounds(intXPos + 1, intYPos + 1) ? intCurrentBoard[intYPos + 1][intXPos + 1] == -Piece.PAWN
							: false)) {
				System.out.println("GET PWNED");
				return true;
			}
			return false;
		} else if (!blnServer) {
			// Piece king = pieceLookup.get(10);
			Point kingPoint = getKingPosition(false, intCurrentBoard);
			int intXPos = 0;
			int intYPos = 0;
			if (kingPoint == null) {
				return true;
				// Piece king = pieceLookup.get(10);
				// intXPos = 7-(king.intXPos/90);
				// intYPos = 7-(king.intYPos/90);
			} else {
				intXPos = kingPoint.x;
				intYPos = kingPoint.y;
			}

			for (int[] p : ChessUtility.getLegalKnightMoves((7 - intXPos) * 90, (7 - intYPos) * 90)) {
				if (intCurrentBoard[7 - p[1]][7 - p[0]] == Piece.KNIGHT) {
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalBishopMoves((7 - intXPos) * 90, (7 - intYPos) * 90, false,
					intCurrentBoard)) {
				if (intCurrentBoard[7 - p[1]][7 - p[0]] == Piece.BISHOP
						|| intCurrentBoard[7 - p[1]][7 - p[0]] == Piece.QUEEN) {
					System.out.println("Blocks check");
					return true;
				}
			}

			for (int[] p : ChessUtility.getLegalRookMoves((7 - intXPos) * 90, (7 - intYPos) * 90, false,
					intCurrentBoard)) {
				if (intCurrentBoard[7 - p[1]][7 - p[0]] == Piece.QUEEN
						|| intCurrentBoard[7 - p[1]][7 - p[0]] == Piece.ROOK) {
					System.out.println("Blocks check");
					return true;
				}
			}

			intXPos = 7 - intXPos;
			intYPos = 7 - intYPos;

			if ((inBounds(7 - intXPos - 1, 7 - intYPos + 1)
					? intCurrentBoard[7 - intYPos + 1][7 - intXPos - 1] == Piece.PAWN
					: false)
					|| (inBounds(7 - intXPos + 1, 7 - intYPos + 1)
							? intCurrentBoard[7 - intYPos + 1][7 - intXPos + 1] == Piece.PAWN
							: false)) {
				System.out.println("GET PWNED");
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Creates a copoy of the contents of an array. Without sharing a refrence
	 *
	 * @param intBoard a two dimensinoal array
	 * @return A copy of the array that was passed in
	 */

	public int[][] deepCopy(int[][] intBoard) {
		int[][] intCopy = new int[intBoard.length][];

		for (int i = 0; i < intBoard.length; i++) {
			intCopy[i] = Arrays.copyOf(intBoard[i], intBoard[i].length);
		}

		return intCopy;
	}

	// Or walks into check
	/**
	 * Checks if a move puts the king in check
	 *
	 * @param strMove the move that is about to be made
	 * @return true if the move puts you in check. False if it does not
	 *
	 */
	public boolean stillInCheck(String strMove) {
		intTempBoard = deepCopy(chessBoard);
		// Set to true because of temp
		move(strMove, true);

		if (inCheck(true)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Checks if a move gives checkmate
	 *
	 * @return true if checkmate, false if king is able to escape
	 */
	public boolean checkmate() {
		// intTempBoard = deepCopy(chessBoard);
		System.out.println("In check: " + blnInCheck);

		if (!blnInCheck) {
			return false;
		} else if (blnInCheck && blnServer) {
			for (Piece p : pieces) {
				int intXIndex = p.intXPos / 90;
				int intYIndex = p.intYPos / 90;

				if (p.blnColor) {
					LinkedList<int[]> legalList = new LinkedList<>();
					if (p.intPiece == 5) {
						int[] intTranlateX = { 1, -1, 0, 0, 1, -1, 1, -1 }; // Just for regular moves
						int[] intTranlateY = { 0, 0, 1, -1, 1, -1, -1, 1 }; // Just for regular moves

						// Regular moves
						for (int i = 0; i < intTranlateX.length; i++) {
							if (intYIndex + intTranlateY[i] >= 0 && intYIndex + intTranlateY[i] < 8
									&& intXIndex + intTranlateX[i] >= 0 && intXIndex + intTranlateX[i] < 8) {
								if (chessBoard[(intYIndex + intTranlateY[i])][(intXIndex + intTranlateX[i])] > 0) {
									continue;
								}
								if (chessBoard[intYIndex + intTranlateY[i]][intXIndex + intTranlateX[i]] <= 0) {
									legalList.add(
											new int[] { intXIndex + intTranlateX[i], intYIndex + intTranlateY[i] });
								}
							}
						}
					} else {
						legalList = getLegalMovesCheck(p);
					}
					for (int[] moves : legalList) {
						System.out.println("Piece: " + p.intPiece);
						System.out.println("intXIndexLast: " + intXIndex);
						System.out.println("intYIndexLast: " + intYIndex);
						if (!stillInCheck(toCoord(intXIndex, intYIndex, moves[0], moves[1]))) {
							System.out.println("BRUH MOMENT");
							return false;
						}
					}
				}
			}
		} else if (blnInCheck && !blnServer) {
			for (Piece p : pieces) {
				int intXIndex = (p.intXPos / 90);
				int intYIndex = (p.intYPos / 90);

				if (!p.blnColor) {
					LinkedList<int[]> legalList = new LinkedList<>();
					if (p.intPiece == 5) {
						int[] intTranlateX = { 1, -1, 0, 0, 1, -1, 1, -1 }; // Just for regular moves
						int[] intTranlateY = { 0, 0, 1, -1, 1, -1, -1, 1 }; // Just for regular moves

						// Regular moves
						for (int i = 0; i < intTranlateX.length; i++) {
							if (intYIndex + intTranlateY[i] >= 0 && intYIndex + intTranlateY[i] < 8
									&& intXIndex + intTranlateX[i] >= 0 && intXIndex + intTranlateX[i] < 8) {
								if (chessBoard[7 - (intYIndex + intTranlateY[i])][7
										- (intXIndex + intTranlateX[i])] < 0) {
									continue;
								}
								if (chessBoard[7 - (intYIndex + intTranlateY[i])][7
										- (intXIndex + intTranlateX[i])] >= 0) {
									legalList.add(
											new int[] { intXIndex + intTranlateX[i], intYIndex + intTranlateY[i] });
								}
							}
						}
					} else {
						legalList = getLegalMovesCheck(p);
					}
					for (int[] moves : legalList) {
						System.out.println("Piece: " + p.intPiece);
						System.out.println("intXIndexLast: " + intXIndex);
						System.out.println("intYIndexLast: " + intYIndex);
						if (!stillInCheck(toCoord(7 - intXIndex, 7 - intYIndex, 7 - moves[0], 7 - moves[1]))) {
							System.out.println("BRUH MOMENT");
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Get's all legal moves of any given piece
	 *
	 * @param p the piece that is being moved
	 * @return a linked list of piece positions
	 */
	private LinkedList<int[]> getLegalMovesCheck(Piece p) {
		intTempBoard = deepCopy(chessBoard);
		switch (p.intPiece) {
		case Piece.ROOK:
			return ChessUtility.getLegalRookMoves(p.intXPos, p.intYPos, p.blnColor, intTempBoard);
		case Piece.KNIGHT:
			return ChessUtility.getLegalKnightMoves(p.intXPos, p.intYPos);
		case Piece.BISHOP:
			return ChessUtility.getLegalBishopMoves(p.intXPos, p.intYPos, p.blnColor, intTempBoard);
		case Piece.QUEEN:
			LinkedList<int[]> legalQueenMoves = ChessUtility.getLegalRookMoves(p.intXPos, p.intYPos, p.blnColor,
					intTempBoard);
			legalQueenMoves.addAll(ChessUtility.getLegalBishopMoves(p.intXPos, p.intYPos, p.blnColor, intTempBoard));
			return legalQueenMoves;
		case Piece.PAWN:
			LinkedList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(p.blnFirst, true, p.blnColor, p.intXPos,
					p.intYPos);
			legalPawnMoves.addAll(ChessUtility.getLegalPawnMoves(p.blnFirst, false, p.blnColor, p.intXPos, p.intYPos));
			return legalPawnMoves;
		}
		return null;
	}

	// TODO: Clean this function up, the if statements have very similar code. Could
	// probably be simplified
	// return true if move was succesful
	/**
	 * Executes a move based on the final x and y position of a mouse
	 *
	 * @param piece   The piece being moved
	 * @param intXPos Physical x-position of the piece on the screen
	 * @param intYPos Physcial y-position of the piece on the screen
	 * @return true if the move is possible, false if it is not
	 */
	public boolean executeMove(Piece piece, int intXPos, int intYPos) {
		SuperSocketMaster ssm = ChessGame.getNetwork();

		piece.setPosition(intXPos, intYPos);

		int intXIndexLast = blnServer ? piece.intLastX / 90 : 7 - (piece.intLastX / 90);
		int intYIndexLast = blnServer ? piece.intLastY / 90 : 7 - (piece.intLastY / 90);

		int intXIndex = blnServer ? intXPos / 90 : 7 - (intXPos / 90);
		int intYIndex = blnServer ? intYPos / 90 : 7 - (intYPos / 90);

		boolean blnLegalMove = false;
		boolean blnStillInCheck = stillInCheck(toCoord(intXIndexLast, intYIndexLast, intXIndex, intYIndex));
		blnInCheck = blnStillInCheck;

		if (!blnTutorial) {
			blnLegalMove = piece.isLegalMove(chessBoard[intYIndex][intXIndex] != 0) && !blnStillInCheck;
		} else {
			blnLegalMove = piece.isLegalMove(chessBoard[intYIndex][intXIndex] != 0);
		}

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
			ChessGame.addMove(result);
			move(result, false);
			BoardAnimation.playSound("move");
			printCharboard();
			// Utility.displayArray(movesMade);
			piece.setPosition(intXPos, intYPos);
			// if (inCheck && escapesCheck()) {
			// piece.goBack();
			// return false;
			// } else if (inCheck && !escapesCheck()) {
			// inCheck = false;
			// }

			System.out.println("Get's here: " + blnInCheck);

			if (givesCheck() && !blnTutorial) {
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
			BoardAnimation.playSound("move");
			ChessGame.addMove(result);
			printCharboard();
			// Utility.displayArray(movesMade);
			piece.setPosition(intXPos, intYPos);
			// if (inCheck && escapesCheck()) {
			// piece.goBack();
			// return false;
			// } else if (inCheck && !escapesCheck()) {
			// inCheck = false;
			// }

			System.out.println("Get's here: " + blnInCheck);

			if (givesCheck() && !blnTutorial) {
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
			BoardAnimation.playSound("move");
			ChessGame.addMove(result);
			// Utility.displayArray(movesMade);
			printCharboard();

			piece.setPosition(intXPos, intYPos);
			// if (inCheck && escapesCheck()) {
			// piece.goBack();
			// return false;
			// } else if (inCheck && !escapesCheck()) {
			// inCheck = false;
			// }

			System.out.println("Get's here: " + blnInCheck);

			if (givesCheck() && !blnTutorial) {
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

	/**
	 * Check if a pawn is able to be promoted
	 *
	 * @param piece the piece that is being moved
	 * @return true if the pawn is able to be promoted, false if not
	 *
	 */
	private boolean promotable(Piece piece) {
		int intXIndex = blnServer ? piece.intXPos / 90 : 7 - (piece.intXPos / 90);
		int intYIndex = blnServer ? piece.intYPos / 90 : 7 - (piece.intYPos / 90);

		return blnServer ? piece.intPiece == Piece.PAWN && intYIndex == 0 && (intXIndex >= 0 && intXIndex <= 7)
				: piece.intPiece == Piece.PAWN && intYIndex == 7 && (intXIndex >= 0 && intXIndex <= 7);
	}

	/**
	 * Checks if a promotion is taking place
	 *
	 * @return true if a promotion is taking place
	 */
	public boolean promotionInProgress() {
		return blnPromotion;
	}

	/**
	 * Promotes a pawn into another piece
	 *
	 * @param piece piece that the pawn is being promoted to
	 */
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

	/**
	 * Constructor for the board class. Creates a new board object
	 *
	 * @param blnServer the state of the server.
	 */
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
