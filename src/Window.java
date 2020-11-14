import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;

import java.awt.Color;
import java.awt.Graphics2D;

public class Window {
    
    private static Window instance;

    private JFrame frame;
    private JLabel content;
    private BufferedImage display, buffer;
    private Graphics2D dispG, bufG;

    private static final Color backgroundColor = Color.decode("#aaaaff");

    private static final int WIDTH = 800, HEIGHT = 800;
    private static final int MARGIN_X = 80, MARGIN_Y = 80;
    private static final int SQUARE_SIZE = (WIDTH - (MARGIN_X * 2)) / 8;
    private static final int PIECE_MARGIN = (int)(.1 * SQUARE_SIZE);
    private static final int PIECE_SIZE = (int)(SQUARE_SIZE - ( 2 * PIECE_MARGIN));

    private Window() {

        frame = new JFrame("Lines of Action");
        frame.setUndecorated(false);
        frame.setSize(WIDTH + 20, HEIGHT + 60);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBackground(backgroundColor);

        display = new BufferedImage(WIDTH, HEIGHT + 20, BufferedImage.TYPE_INT_ARGB);
        buffer = new BufferedImage(WIDTH, HEIGHT + 20, BufferedImage.TYPE_INT_ARGB);
        
        ImageIcon icon = new ImageIcon(display);

        content = new JLabel(icon);
        content.setSize(WIDTH, HEIGHT);
        content.addMouseListener(Mouse.getInstance());
        
        dispG = display.createGraphics();
        bufG = buffer.createGraphics();

        frame.setContentPane(content);

        render();
    }

    public void render() {
        clear();
        drawBoard();
        drawPieces();
        blit();
    }

    private void clear() {
        bufG.setColor(backgroundColor);
        bufG.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawBoard() {
        
        boolean color = false;
        bufG.setColor(Color.BLACK);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                bufG.fillRect(MARGIN_X + (i * SQUARE_SIZE), MARGIN_Y + (j * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);

                if (color) { 
                    bufG.setColor(Color.BLACK); 
                } else { 
                    bufG.setColor(Color.WHITE); 
                }
                color = !color;
            }
            
            if (color) { 
                bufG.setColor(Color.BLACK); 
            } else { 
                bufG.setColor(Color.WHITE); 
            }
            color = !color;
        }

    }

    /**
     * Draws all the grarbage that Game gives it, (tokens and valid move squares.)
     */
    private void drawPieces() {
        Piece[] pieces = Game.getInstance().getPieces();
        for (Piece p : pieces) {
            if (p.getType() == 0) { 
                bufG.setColor(Color.decode("#222222")); 
                bufG.fillOval(MARGIN_X + (p.getX() * SQUARE_SIZE) + (PIECE_MARGIN), MARGIN_Y + (p.getY() * SQUARE_SIZE) + (PIECE_MARGIN), PIECE_SIZE, PIECE_SIZE);
            } else if (p.getType() == 1) { 
                bufG.setColor(Color.decode("#aaaaaa")); 
                bufG.fillOval(MARGIN_X + (p.getX() * SQUARE_SIZE) + (PIECE_MARGIN), MARGIN_Y + (p.getY() * SQUARE_SIZE) + (PIECE_MARGIN), PIECE_SIZE, PIECE_SIZE);
            } else if (p.getType() == 2) { 
                bufG.setColor(new Color(0, 255, 0, 127));
                bufG.fillRect(MARGIN_X + (p.getX() * SQUARE_SIZE), MARGIN_Y + (p.getY() * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    /**
     * returns an X-coordinate of a board square if the pixel value provided is within its range, otherwise returns -1.
     * @param px
     */
    public int getBoardX(int px) {
        if (px < MARGIN_X || px > (WIDTH - MARGIN_X)) return -1;
        return ((px - MARGIN_X) / SQUARE_SIZE);
    }

    /**
     * returns a Y-coordinate of a board square if the pixel value provided is within its range, otherwise returns -1.
     * @param px
     */
    public int getBoardY(int px) {
        if (px < MARGIN_Y || px > (HEIGHT - MARGIN_Y)) return -1;
        return ((px - MARGIN_Y) / SQUARE_SIZE);
    }

    /**
     * Puts the (theoretically) fully-drawn buffer image to the screen. Called last in the rendering process. 
     */ 
    private void blit() {
        dispG.drawImage(buffer, 0, 0, null);
        frame.repaint();
    }

    /**
     * Returns the static instance of the window class.
     * Exists so I only ever have one window object in existence.
     */
    public static Window getInstance() {
        if (instance == null) instance = new Window();
        return instance;
    }

}
