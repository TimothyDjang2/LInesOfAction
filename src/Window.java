import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Window {
    
    private static Window instance;

    private JFrame frame;
    private JLabel content;
    private BufferedImage display, buffer;
    private Graphics2D dispG, bufG;

    private static final Color BACKGROUND_COLOR = Color.decode("#aaaaff");
    private static final Color WHITE_PIECES = Color.decode("#aaaaaa");
    private static final Color BLACK_PIECES = Color.decode("#222222");

    private static final int WIDTH = 800, HEIGHT = 800;
    private static final int MARGIN_X = 80, MARGIN_Y = 80;
    private static final int SQUARE_SIZE = (WIDTH - (MARGIN_X * 2)) / 8;
    private static final int PIECE_MARGIN = (int)(.1 * SQUARE_SIZE);
    private static final int PIECE_SIZE = (int)(SQUARE_SIZE - ( 2 * PIECE_MARGIN));

    /**
     * Handles all the rendering. This constructor just sets up the window.
     */
    private Window() {

        frame = new JFrame("Lines of Action");
        frame.setUndecorated(false);
        frame.setSize(WIDTH + 20, HEIGHT + 60);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBackground(BACKGROUND_COLOR);

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

    /**
     * Do all the cool rendering.
     */
    public void render() {
        clear();
        if (Game.getInstance().getGameState() == 0) {
            drawBoard();
            drawSelectedSquare();
            drawPieces();
        }
        else if (Game.getInstance().getGameState() == 1) { drawTitle("White Wins", Color.WHITE); }
        else if (Game.getInstance().getGameState() == 2) { drawTitle("Black Wins", BLACK_PIECES); }
        else if (Game.getInstance().getGameState() == 3) { drawTitle("Tie", BLACK_PIECES); }
        blit();
    }

    /**
     * Reset canvas for a new frame.
     */
    private void clear() {
        bufG.setColor(BACKGROUND_COLOR);
        bufG.fillRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Draws text in the "middle" of the screen.
     * @param title - Text to draw
     * @param color - Color of text
     */
    private void drawTitle(String title, Color color) {
        bufG.setColor(color);
        bufG.setFont(new Font("Monospace", Font.BOLD, 100));
        int offset = (title.length() * 28);

        bufG.drawString(title, (WIDTH / 2) - offset, (HEIGHT / 2) + 28);
    }

    /**
     * Draws a pretty checkerboard
     */
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
     * Draw the singular red square that shows you which piece is selected.
     */
    private void drawSelectedSquare() {
        Location loc = Game.getInstance().getSelectedLocation();
        if (loc != null) {
            int x = loc.getX();
            int y = loc.getY();
            bufG.setColor(new Color(255, 127, 127));
            bufG.fillRect(MARGIN_X + (x * SQUARE_SIZE), MARGIN_Y + (y * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    /**
     * Draws all the grarbage that Game gives it, (tokens and valid move squares.)
     */
    private void drawPieces() {
        int[][] pieces = Game.getInstance().getPieces();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] - 4 > -1) { 
                    bufG.setColor(new Color(127, 255, 127, 255));
                    bufG.fillRect(MARGIN_X + (i * SQUARE_SIZE), MARGIN_Y + (j * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
                    pieces[i][j] -= 4;
                } if (pieces[i][j] - 2 > -1) { 
                    bufG.setColor(BLACK_PIECES); 
                    bufG.fillOval(MARGIN_X + (i * SQUARE_SIZE) + (PIECE_MARGIN), MARGIN_Y + (j * SQUARE_SIZE) + (PIECE_MARGIN), PIECE_SIZE, PIECE_SIZE);
                    pieces[i][j] -= 2;
                } if (pieces[i][j] == 1) { 
                    bufG.setColor(WHITE_PIECES); 
                    bufG.fillOval(MARGIN_X + (i * SQUARE_SIZE) + (PIECE_MARGIN), MARGIN_Y + (j * SQUARE_SIZE) + (PIECE_MARGIN), PIECE_SIZE, PIECE_SIZE);
                }
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
