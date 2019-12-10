import java.awt.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;

//Server side view
public class BoardAnimation extends JPanel {

  public int[][] charBoard = {
    {-1,-2,-3,-4,-5,-3,-2,-1},
    {-6,-6,-6,-6,-6,-6,-6,-6},
    {0, 0, 0, 0, 0, 0, 0, 0 },
    {0, 0, 0, 0, 0, 0, 0, 0 },
    {0, 0, 0, 0, 0, 0, 0, 0 },
    {0, 0, 0, 0, 0, 0, 0, 0 },
    {6, 6, 6, 6, 6, 6, 6, 6 },
    {1, 2, 3, 4, 5, 3, 2, 1 },
  };

  public boolean white = true;
  public boolean pressed = false;
  // public Piece[] pieces = Pieces[32];
  public ArrayList<Piece> pieces = new ArrayList<Piece>();
  public Piece temp = null;

  public int roundDown(int n, int m) {
    return n >= 0 ? (n / m) * m : ((n - m + 1) / m) * m;
  }

  public void drawBoard(Graphics g) {
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if ((i % 2 == 0) == (j % 2 == 0)) {
          g.setColor(Color.WHITE);
        }
        else {
          g.setColor(Color.BLACK);
        }

        g.fillRect(j * 50, i * 50, 50, 50);
      }
    }
  }

  public void initBoard(){
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        Piece p;
        int piece = charBoard[i][j];
        if(piece > 0){
          p = new Piece(j * 50, i * 50, true, piece);
        } else if(piece < 0){
          p = new Piece(j * 50, i * 50, false, piece);
        } else {
          continue;
        }
        pieces.add(p);
      }
    }

  }

  public String toCoord(int x1, int y1, int x2, int y2){
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

  public Point coordToLoc(String coord){
    String newCoord = coord.split(",")[1];

    System.out.println(newCoord);

    return null;

  }

  public void drawPieces(Graphics g) {
    for(Piece p: pieces){
      p.update(g);
    }
  }

  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    drawBoard(g);
    drawPieces(g);
  }


  BoardAnimation() {
    super();
    initBoard();
    addMouseListener(new MyMouseAdaptor());
    addMouseMotionListener(new MyMouseAdaptor());
  }

  private class MyMouseAdaptor extends MouseAdapter {
    public int intX1;
    public int intY1;
    // @Override
    public void mousePressed(MouseEvent evt) {
      for(int i = 0; i < pieces.size(); i++){
        if((evt.getX() <= pieces.get(i).intXPos + 50 && evt.getX() >= pieces.get(i).intXPos)
          && (evt.getY() >= pieces.get(i).intYPos && evt.getY() <= pieces.get(i).intYPos + 50) && pressed == false) {
          pressed = true;
          intX1 = roundDown(evt.getX(), 50) / 50;
          intY1 = roundDown(evt.getY(), 50) / 50;
          temp = pieces.get(i);
          System.out.println("x: " + temp.intXPos/50 + ", y: " + temp.intYPos/50);
          temp.intLastX = temp.intXPos;
          temp.intLastY = temp.intYPos;
        }
      }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
      if(pressed){
        movePiece(temp, evt);
      }
    }
    @Override
    public void mouseReleased(MouseEvent evt) {
      finalMove(temp, evt);
      int intX2 = roundDown(evt.getX(), 50) / 50;
      int intY2 = roundDown(evt.getY(), 50) / 50;

      String result = toCoord(intX1, intY1, intX2, intY2);
      coordToLoc(result);

      pressed = false;

      // temp = null;
      //if possible, execute move here
    }

    public void movePiece(Piece piece, MouseEvent evt){
      piece.intXPos = evt.getX();
      piece.intYPos = evt.getY();
      repaint();
    }

    public void finalMove(Piece piece, MouseEvent evt){
      piece.intXPos = roundDown(evt.getX(), 50);
      piece.intYPos = roundDown(evt.getY(), 50);
      System.out.println("Row: " + piece.intYPos/50 + ", col: " + piece.intXPos/50);
      pressed = false;
      System.out.println("piece: " + charBoard[(piece.intYPos/50)][(piece.intXPos/50)] + ", curr: " + piece.intPiece);
      boolean blnLegalMove = piece.isLegalMove(charBoard[(piece.intYPos/50)][(piece.intXPos/50)] != 0);
      System.out.println(blnLegalMove);

      if(!blnLegalMove) {
        piece.intXPos = piece.intLastX;
        piece.intYPos = piece.intLastY;
      } else {
        piece.intXPos = roundDown(evt.getX(), 50);
        piece.intYPos = roundDown(evt.getY(), 50);
        piece.blnFirst = false;
      }

      repaint();
    }
  }
}
