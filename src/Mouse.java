import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Mouse implements MouseListener {

    private int lastClickX, lastClickY;

    /**
     * Triggers whenever the mouse button is pressed down.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastClickX = Window.getInstance().getBoardX(e.getX());
        lastClickY = Window.getInstance().getBoardY(e.getY());
        Game.getInstance().processClick(lastClickX, lastClickY);
        Window.getInstance().render();
    }
    
    // MouseListeners have a lot of stuff that gets overridden that I don't need.
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    private static Mouse instance;

    /**
     * This class processes input and sends it to the engine.
     * Has a getInstance method since I only need one of them.
     * @return
     */
    public static Mouse getInstance() {
        if (instance == null) instance = new Mouse();
        return instance;
    }

}
