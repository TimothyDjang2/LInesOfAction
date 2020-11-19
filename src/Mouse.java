import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Mouse implements MouseListener {

    private boolean mouseDown = false;
    private int lastClickX, lastClickY;

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        lastClickX = Window.getInstance().getBoardX(e.getX());
        lastClickY = Window.getInstance().getBoardY(e.getY());
        Game.getInstance().processClick(lastClickX, lastClickY);
        Window.getInstance().render();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public boolean getMouseDown() { return mouseDown; }
    
    private static Mouse instance;

    /**
     * Processes input and sends it to the engine.
     * @return
     */
    public static Mouse getInstance() {
        if (instance == null) instance = new Mouse();
        return instance;
    }

}
