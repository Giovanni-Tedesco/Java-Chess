import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Piece {

  //Properties
  //True means white, false means black
  public boolean color;
  public int piece;
  public int xPos;
  public int yPos;


  Piece(int xPos, int yPos, boolean color, int piece) {
    this.color = color;
    this.xPos = xPos;
    this.yPos = yPos;
    this.piece = Math.abs(piece);
  }

  public void update(Graphics g){
    if(this.color == true) {      //If the pieces colour is white / pink
      g.setColor(Color.PINK);
    } else {
      g.setColor(Color.BLUE);    //If the pieces colour is dark / blue
    }
    g.fillRect(xPos, yPos, 25, 50);
  }

}
