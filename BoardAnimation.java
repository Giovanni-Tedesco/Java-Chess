import java.awt.*;
import javax.swing.*;

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

  public void drawBoard(Graphics g) {
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        if ((i % 2 == 0) == (j % 2 == 0)) {
          g.setColor(Color.WHITE);
        }
        else {
          g.setColor(Color.BLACK);
        }

        g.fillRect(i * 50, j * 50, 50, 50);
      }
    }
  }

  public void drawPieces(Graphics g){
    g.setColor(Color.RED);
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        int piece = charBoard[i][j];
        if(piece == 0){
          continue;
        } else if(Math.abs(piece) == 1){
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        } else if(Math.abs(piece) == 2) {
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        } else if(Math.abs(piece) == 3) {
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        } else if(Math.abs(piece) == 4) {
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        } else if(Math.abs(piece) == 5) {
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        } else if(Math.abs(piece) == 6) {
          g.drawRect(j * 50 + 20, i * 50, 20, 40);
        }
      }
    }

  }

  @Override
  public void paintComponent(Graphics g){
    drawBoard(g);
    drawPieces(g);

  }


  BoardAnimation() {
    super();
  }


}
