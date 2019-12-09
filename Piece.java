import java.awt.event.*;
import javax.swing.event.*;
public class Piece {

  //Properties
  //True means white, false means black
  public boolean color;
  public int pieceType;
  public int xPos;
  public int yPos;


  Piece(int xPos, int yPos, boolean color) {
    this.color = color;
    this.xPos = xPos;
    this.yPos = yPos;
  }


}
