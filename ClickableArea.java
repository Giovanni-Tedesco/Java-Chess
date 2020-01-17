import javax.swing.event.*;
import java.awt.event.*;

//this class is a blueprint for an object that represents area on the jpanel that can be clicked by the mouse. Used in the help panel
public class ClickableArea {
    //PROPERTIES
    //intNumber represents the help screen number
    public int intXPos, intYPos, intWidth = 37, intHeight = 43, intNumber;

    //METHODS
    //check to see if the mouse was within the bounds of the area
    public boolean clickedArea(MouseEvent evt) {
        int intMouseX = evt.getX();
        int intMouseY = evt.getY();

        return (intMouseX >= intXPos) && (intMouseX <= intXPos + intWidth) && (intMouseY >= intYPos) && (intMouseY <= intYPos + intHeight);
    }

    //CONSTRUCTOR
    public ClickableArea(int intXPos, int intYPos, int intNumber) {
        this.intXPos = intXPos;
        this.intYPos = intYPos;
        this.intNumber = intNumber;
    }
}
