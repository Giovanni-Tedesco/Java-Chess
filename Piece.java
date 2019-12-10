import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
public class Piece {

  //Properties
  //True means white, false means black
  public boolean blnColor;
  public boolean blnFirst;
  public int intPiece;
  public int intXPos;
  public int intYPos;

  public int intBoardX;
  public int intBoardY;

  public int intLastX;
  public int intLastY;



  Piece(int xPos, int yPos, boolean color, int piece) {
    this.blnColor = color;
    this.intXPos = xPos;
    this.intYPos = yPos;
    this.intPiece = Math.abs(piece);
    this.blnFirst = true;
  }

  public void update(Graphics g){
    if(this.blnColor == true) {//If the pieces colour is white / pink
      g.setColor(Color.PINK);
    } else {
      g.setColor(Color.BLUE);//If the pieces colour is dark / blue
    }

    g.fillRect(intXPos, intYPos, 25, 50);
  }

  public boolean isLegalMove(boolean blnHasPiece) {
    System.out.println("hi1");
    if(intPiece == 6) {
      ArrayList<int[]> legalPawnMoves = ChessUtility.getLegalPawnMoves(blnFirst, blnHasPiece, intLastX, intLastY);
      System.out.println(legalPawnMoves);
      int [] position = {(intXPos/50),(intYPos/50)};
      if(ChessUtility.isInList(legalPawnMoves, position)) {
        return true;
      } else {
        return false;
      }
    }

    return false;
  }
}