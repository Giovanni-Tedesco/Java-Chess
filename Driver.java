import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/************************************************************************************************************
 * Program Header - Names: John Tedesco, Steven Lum, Mehul Pillai Teacher: Mr.
 * Cadawas Course: ISC4U1 Date Of Completion: 1-17-2020 Description: This
 * program is a chess game made for the ISC4U1 CPT
 **************************************************************************************************************/

// launch the program
public class Driver {
    public static void main(String[] args) {
        MainMenu.frame = new JFrame("Chess");
        Utility.changePanel(new SplashScreen());
    }
}
