import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Piece {

  //Properties
  //True means white, false means black
  public boolean blnColor;
  public int intPiece;
  public int intXPos;
  public int intYPos;

  public int intBoardX;
  public int intBoardY;


  Piece(int xPos, int yPos, boolean color, int piece) {
    this.blnColor = color;
    this.intXPos = xPos;
    this.intYPos = yPos;
    this.intPiece = Math.abs(intPiece);
  }

  public void update(Graphics g){
    if(this.blnColor == true) {      //If the pieces colour is white / pink
      g.setColor(Color.PINK);
    } else {
      g.setColor(Color.BLUE);    //If the pieces colour is dark / blue
    }
    g.fillRect(intXPos, intYPos, 25, 50);
  }

}
