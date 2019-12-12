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
  public ArrayList<Piece> captured = new ArrayList<Piece>();
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

  public void printCharboard(int[][] arr){
    for(int i = 0; i < arr.length; i++){
      for(int j = 0; j < arr[0].length; j++){
        System.out.print(arr[i][j]);
      }
      System.out.println("");
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

  //Changes the position of the pieces on the charBoard
  public int[][] move(String move, int[][] charBoard){
    String[] moves = move.split(",");
    // System.out.println(moves[0]);

    Point p1 = coordToLoc(moves[0]);
    System.out.println(p1.x + " " + p1.y);
    Point p2 = coordToLoc(moves[1]);

    System.out.println(p2.x + " " + p2.y);

    int intTemp = charBoard[p1.x][p1.y];
    charBoard[(int)(p1.getX())][(int)(p1.getY())] = 0;
    // System.out.println(moves[1]);
    charBoard[(int)(p2.getX())][(int)(p2.getY())] = intTemp;

    return charBoard;


  }

  public Point coordToLoc(String coord){
    // System.out.println(newCoord);
    int x = (int)(coord.charAt(0) - 97);
    int y = (int)(7 - coord.charAt(1) + 49 );
    // System.out.println(x + ", " + y);

    Point retCoord = new Point(x, y);

    return retCoord;

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
    @Override
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
      System.out.println(result);
      // coordToLoc(result);
      charBoard = move(result, charBoard);
      printCharboard(charBoard);

      pressed = false;

      // temp = null;
    }

    public void movePiece(Piece piece, MouseEvent evt){
      piece.intXPos = evt.getX();
      piece.intYPos = evt.getY();
      repaint();
    }

    public void finalMove(Piece piece, MouseEvent evt){
      int intX2 = roundDown(evt.getX(), 50) / 50;
      int intY2 = roundDown(evt.getY(), 50) / 50;

      String result = toCoord(intX1, intY1, intX2, intY2);
      coordToLoc(result);

      piece.intXPos = roundDown(evt.getX(), 50);
      piece.intYPos = roundDown(evt.getY(), 50);
      System.out.println("Row: " + piece.intYPos/50 + ", col: " + piece.intXPos/50);
      pressed = false;
      System.out.println("piece: " + charBoard[(piece.intYPos/50)][(piece.intXPos/50)] + ", curr: " + piece.intPiece);
      boolean blnLegalMove = piece.isLegalMove(charBoard[(piece.intYPos/50)][(piece.intXPos/50)] != 0);
      System.out.println(blnLegalMove);

      if(!blnLegalMove || charBoard[(piece.intYPos/50)][(piece.intXPos/50)] > 0) {
        piece.intXPos = piece.intLastX;
        piece.intYPos = piece.intLastY;
      } else {
        piece.intXPos = roundDown(evt.getX(), 50);
        piece.intYPos = roundDown(evt.getY(), 50);
        int pieceNum = charBoard[intY1][intX1];
        charBoard[intY1][intX1] = 0;
        charBoard[intY2][intX2] = pieceNum;
        piece.blnFirst = false;
      }

      repaint();
    }
  }
}
