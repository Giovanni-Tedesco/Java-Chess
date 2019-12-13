import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Driver {
    public static void main(String[] args){
        MainMenu.frame = new JFrame("Chess");
        Utility.changePanel(new MainMenu().getMenuPanel());
    }
}

