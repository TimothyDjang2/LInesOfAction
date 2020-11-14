import java.util.ArrayList;

public class Game {
    
    private ArrayList<Piece> pieces;
    private static Game instance;
    private boolean turn = false; //Who's turn it is to move, false for black, true for white.
    private Piece selectedPiece;

    private Game() {
        pieces = new ArrayList<Piece>();
        for (int i = 1; i < 7; i++) { pieces.add(new Piece(i, 0, 1)); }
        for (int i = 1; i < 7; i++) { pieces.add(new Piece(0, i, 0)); }
        pieces.add(0, new Piece(1, 0, 2));
    }

    /**
     * Gives the renderer an array of pieces, (tokens and valid move squares,) to render.
     */
    public Piece[] getPieces() {
        Piece[] ret = new Piece[pieces.size()];
        pieces.toArray(ret);
        return ret;
    }

    public void processClick(int x, int y) {
        for (Piece p : pieces) {
            if (p.getX() == x && p.getY() == y) {
                if (p.getType() == 2) {
                    for (int i = 0; i < pieces.size(); i++) {
                        if (pieces.get(i).getX() == x && pieces.get(i).getY() == y) {
                            pieces.remove(i);
                            i--;
                        }
                    }
                    selectedPiece.setPos(x, y);
                    clearMoveSelectors();
                    turn = !turn;
                    break;
                } else {
                    if ((p.getType() == 0 && !turn) || (p.getType() == 1 && turn)) {
                        clearMoveSelectors();
                        selectedPiece = p;
                        getValidMoves(p);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Adds all valid moves for the specified piece to the rendering queue, after clearing all the old valid move squares.
     * @param piece - Piece to find valid moves for.
     */
    public void getValidMoves(Piece piece) {
        int count = 0;
        for (Piece p : pieces) {
            if (piece.getX() == p.getX()) { count++; }
        }
        if (piece.getY() - count > -1) { pieces.add(0, new Piece(piece.getX(), piece.getY() - count, 2)); }
        if (piece.getY() + count < 8) { pieces.add(0, new Piece(piece.getX(), piece.getY() + count, 2)); }

        count = 0;
        for (Piece p : pieces) {
            if (piece.getY() == p.getY()) { count++; }
        }
        if (piece.getX() - count > -1) { pieces.add(0, new Piece(piece.getX() - count, piece.getY(), 2)); }
        if (piece.getX() + count < 8) { pieces.add(0, new Piece(piece.getX() + count, piece.getY(), 2)); }
    }

    private Piece pieceAt(int x, int y) {
        for (Piece p : pieces) {
            if (p.getType() != 2 && p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        return null;
    }

    /**
     * Clears all green square "pieces" from the piece list.
     * Used when a move is made, or when a different token is selected.
     */
    public void clearMoveSelectors() {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getType() == 2) {
                pieces.remove(i);
                i--;
            }
        }
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

}
