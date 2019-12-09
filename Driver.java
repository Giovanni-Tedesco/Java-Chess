import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Driver {

  //Properties
  JFrame frame = new JFrame("Chess");
  BoardAnimation panel = new BoardAnimation();


  //Methods


  //Constructor
  public Driver() {
    panel.setPreferredSize(new Dimension(400, 400));
    panel.setLayout(null);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel);
    frame.pack();
    frame.setVisible(true);



  }

  public static void main(String[] args){
    new Driver();
  }


}
