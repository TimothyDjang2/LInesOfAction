import java.util.ArrayList;

public class Game {
    
    private ArrayList<Piece> pieces;
    private static Game instance;
    private boolean turn = false; //Who's turn it is to move, false for black, true for white.
    private Piece selectedPiece;

    /**
     * Handles all the pieces, turns, etc.
     */
    private Game() {
        pieces = new ArrayList<Piece>();
        for (int i = 1; i < 7; i++) { 
            pieces.add(new Piece(i, 0, 1)); 
            pieces.add(new Piece(i, 7, 1)); 
        }
        for (int i = 1; i < 7; i++) { 
            pieces.add(new Piece(0, i, 0)); 
            pieces.add(new Piece(7, i, 0)); 
        }
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
        ArrayList<Piece> 
            row = new ArrayList<Piece>(), 
            col = new ArrayList<Piece>(), 
            upDiagonal = new ArrayList<Piece>(), // slope of 1
            downDiagonal = new ArrayList<Piece>(); // slope of -1

        for (Piece test : pieces) {
            if (piece.getX() == test.getX()) { col.add(test); }
            if (piece.getY() == test.getY()) { row.add(test); }
            if (piece.getX() - test.getX() == piece.getY() - test.getY()) { upDiagonal.add(test); }
            if (piece.getX() - test.getX() == piece.getY() + test.getY()) { downDiagonal.add(test); }
            if (piece.getX() + test.getX() == piece.getY() - test.getY()) { downDiagonal.add(test); }
        }

        if (isValidSquare(piece.getX(), piece.getY() - col.size())) { pieces.add(0, new Piece(piece.getX(), piece.getY() - col.size(), 2)); }
        if (isValidSquare(piece.getX(), piece.getY() + col.size())) { pieces.add(0, new Piece(piece.getX(), piece.getY() + col.size(), 2)); }
        if (isValidSquare(piece.getX() - row.size(), piece.getY())) { pieces.add(0, new Piece(piece.getX() - row.size(), piece.getY(), 2)); }
        if (isValidSquare(piece.getX() + row.size(), piece.getY())) { pieces.add(0, new Piece(piece.getX() + row.size(), piece.getY(), 2)); }

        if (isValidSquare(piece.getX() + upDiagonal.size(), piece.getY() + upDiagonal.size())) { pieces.add(0, new Piece(piece.getX() + upDiagonal.size(), piece.getY() + upDiagonal.size(), 2)); }
        if (isValidSquare(piece.getX() - upDiagonal.size(), piece.getY() - upDiagonal.size())) { pieces.add(0, new Piece(piece.getX() - upDiagonal.size(), piece.getY() - upDiagonal.size(), 2)); }
        if (isValidSquare(piece.getX() + downDiagonal.size(), piece.getY() - downDiagonal.size())) { pieces.add(0, new Piece(piece.getX() + downDiagonal.size(), piece.getY() - downDiagonal.size(), 2)); }
        if (isValidSquare(piece.getX() - downDiagonal.size(), piece.getY() + downDiagonal.size())) { pieces.add(0, new Piece(piece.getX() - downDiagonal.size(), piece.getY() + downDiagonal.size(), 2)); }
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
     * See if a square is within the limits of the board.
     */
    private boolean isValidSquare(int x, int y) {
        if (x > -1 && x < 8 && y > -1 && y < 8) return true;
        return false;
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
            } else { return; }
        }
    }

    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

}
