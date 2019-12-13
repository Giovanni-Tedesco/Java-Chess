import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Driver implements ActionListener {

  //Properties
  JFrame frame = new JFrame("Chess");
  BoardAnimation panel = new BoardAnimation();
  Timer timer = new Timer(1000/60, this);


  //Methods
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == timer){
      panel.repaint();
    }
  }



  //Constructor
  public Driver() {
    panel.setPreferredSize(new Dimension(400, 400));
    panel.setLayout(null);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    timer.start();


  }

  public static void main(String[] args){
    new Driver();
  }


}
