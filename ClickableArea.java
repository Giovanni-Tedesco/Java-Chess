import javax.swing.event.*;
import java.awt.event.*;

public class ClickableArea {
    public int intXPos, intYPos, intNumber, intWidth = 37, intHeight = 43;
    public ClickableArea(int intXPos, int intYPos, int intNumber) {
        this.intXPos = intXPos;
        this.intYPos = intYPos;
        this.intNumber = intNumber;
    }

    public boolean clickedArea(MouseEvent evt) {
        int intMouseX = evt.getX();
        int intMouseY = evt.getY();

        return (intMouseX >= intXPos) && (intMouseX <= intXPos + intWidth) && (intMouseY >= intYPos) && (intMouseY <= intYPos + intHeight);
    }
}
