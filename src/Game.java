/**
 * Lines Of Action - Made by Timothy Djang for CS-172, Nov 19, 2020
 * 
 * Emeline was technically my partner but we both made separate programs again, since I don't like using stdLib.
 * 
 * Features:
 * - The most disgusting code I've ever written.
 * - Stress.
 * - Singletons that make life difficult to unit test.
 * - No rhyme or reason when it comes to which things are made constants and which are hard-coded.
 */

public class Game {
    
    private int[][] board; //0 for empty, 1 for white, 2 for black, 4 for movement.
    private boolean turn = false; //Who's turn it is to move, false for black, true for white.
    private Location selectedPiece;
    private int dfsTotal; //Data storage for search function. Needs to be a class variable for reasons.
    private int gameState; //Stores the winstate of the game so it doesn't have to be recalculated every time it's queried.

    public static void main(String[] args) {
        Window.getInstance();
    }

    /**
     * Handles all the pieces, turns, etc.
     */
    private Game() {
        board = new int[8][8];
        for (int i = 1; i < 7; i++) { 
            board[i][0] = 1;
            board[i][7] = 1;
            board[0][i] = 2;
            board[7][i] = 2;
        }
    }

    /**
     * Gives the renderer the board, so it can commence rendering.
     */
    public int[][] getPieces() {
        int[][] ret = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ret[i][j] = board[i][j];
            }
        }
        return ret;
    }

    /**
     * Gives the renderer the selected piece so it can fill it in with a nice color.
     */
    public Location getSelectedLocation() {
        return selectedPiece;
    }

    /**
     * Returns the current state of the game, (aka has someone won yet)
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * Method that does all the correct stuff in order when the screen is clicked.
     */
    public void processClick(int x, int y) {
        if (x == -1 || y == -1) { return; }

        if (board[x][y] - 4 > -1) {
            board[x][y] = board[selectedPiece.getX()][selectedPiece.getY()];
            board[selectedPiece.getX()][selectedPiece.getY()] = 0;
            clearMoveSelectors();
            selectedPiece = null;
            gameState = detectWin();
            turn = !turn;
        } else {
            if ((board[x][y] == 2 && !turn) || (board[x][y] == 1 && turn)) {
                selectedPiece = new Location(x, y);
                getValidMoves(x, y);
            }
        }

    }

    /**
     * Adds all valid moves for the specified piece to the rendering queue, after clearing all the old valid move squares. I despise this code.
     * @param piece - Piece to find valid moves for.
     */
    public void getValidMoves(int x, int y) {
        clearMoveSelectors();

        // This half of the method finds valid moves in the cardinal directions.
        int col = 0, row = 0, upDiagonal = 0, downDiagonal = 0;
        
        for (int i = 0; i < 8; i++) {
            if (board[x][i] > 0) { col++; }
            if (board[i][y] > 0) { row++; }
        }

        boolean upTile = true, downTile = true;
        for (int i = 1; i <= col; i++) {
            if (!isValidSquare(x, y + i)) { 
                upTile = false; 
            } else { 
                if (board[x][y + i] != board[x][y] && board[x][y + i] != 0 && i < col) { upTile = false; }
                if (board[x][y + i] == board[x][y] && i == col) { upTile = false; }
            }
            if (!isValidSquare(x, y - i)) {
                downTile = false;
            } else {
                if (board[x][y - i] != board[x][y] && board[x][y - i] != 0 && i < col) { downTile = false; }
                if (board[x][y - i] == board[x][y] && i == col) { downTile = false; }
            }
        }
        if (upTile && isValidSquare(x, y + col)) board[x][y + col] += 4;
        if (downTile && isValidSquare(x, y - col)) board[x][y - col] += 4;

        boolean leftTile = true, rightTile = true;
        for (int i = 1; i <= row; i++) {
            if (!isValidSquare(x - i, y)) { 
                leftTile = false; 
            } else {
                if (board[x - i][y] != board[x][y] && board[x - i][y] != 0 && i < row) { leftTile = false; }
                if (board[x - i][y] == board[x][y] && i == row) { leftTile = false; }
            }
            if (!isValidSquare(x + i, y)) {
                rightTile = false;
            } else {
                if (board[x + i][y] != board[x][y] && board[x + i][y] != 0 && i < row) { rightTile = false; }
                if (board[x + i][y] == board[x][y] && i == row) { rightTile = false; }
            }
        }
        if (leftTile && isValidSquare(x - row, y)) board[x - row][y] += 4;
        if (rightTile && isValidSquare(x + row, y)) board[x + row][y] += 4;

        // This half of the method finds valid moves in diagonal directions. It's also uncomfortably long.
        Location upDiagLoc = new Location(x, y);
        while (isValidSquare(upDiagLoc.getX(), upDiagLoc.getY())) {
            upDiagLoc.shift(-1, -1);
        }
        upDiagLoc.shift(1, 1);

        Location downDiagLoc = new Location(x, y);
        while (isValidSquare(downDiagLoc.getX(), downDiagLoc.getY())) {
            downDiagLoc.shift(-1, 1);
        }
        downDiagLoc.shift(1, -1);

        while (isValidSquare(upDiagLoc.getX(), upDiagLoc.getY())) {
            if (board[upDiagLoc.getX()][upDiagLoc.getY()] > 0) { upDiagonal++; }
            upDiagLoc.shift(1, 1);
        }

        while (isValidSquare(downDiagLoc.getX(), downDiagLoc.getY())) {
            if (board[downDiagLoc.getX()][downDiagLoc.getY()] > 0) { downDiagonal++; }
            downDiagLoc.shift(1, -1);
        }

        boolean upLeftTile = true, downRightTile = true;
        for (int i = 1; i <= upDiagonal; i++) {
            if (!isValidSquare(x + i, y + i)) { 
                downRightTile = false; 
            } else { 
                if (board[x + i][y + i] != board[x][y] && board[x + i][y + i] != 0 && i < upDiagonal) { downRightTile = false; }
                if (board[x + i][y + i] == board[x][y] && i == upDiagonal) { downRightTile = false; }
            }
            if (!isValidSquare(x - i, y - i)) {
                upLeftTile = false;
            } else {
                if (board[x - i][y - i] != board[x][y] && board[x - i][y - i] != 0 && i < upDiagonal) { upLeftTile = false; }
                if (board[x - i][y - i] == board[x][y] && i == upDiagonal) { upLeftTile = false; }
            }
        }
        if (upLeftTile && isValidSquare(x - upDiagonal, y - upDiagonal)) board[x - upDiagonal][y - upDiagonal] += 4;
        if (downRightTile && isValidSquare(x + upDiagonal, y + upDiagonal)) board[x + upDiagonal][y + upDiagonal] += 4;

        boolean upRightTile = true, downLeftTile = true;
        for (int i = 1; i <= downDiagonal; i++) {
            if (!isValidSquare(x + i, y - i)) { 
                upRightTile = false; 
            } else { 
                if (board[x + i][y - i] != board[x][y] && board[x + i][y - i] != 0 && i < downDiagonal) { upRightTile = false; }
                if (board[x + i][y - i] == board[x][y] && i == downDiagonal) { upRightTile = false; }
            }
            if (!isValidSquare(x - i, y + i)) {
                downLeftTile = false;
            } else {
                if (board[x - i][y + i] != board[x][y] && board[x - i][y + i] != 0 && i < downDiagonal) { downLeftTile = false; }
                if (board[x - i][y + i] == board[x][y] && i == downDiagonal) { downLeftTile = false; }
            }
        }
        if (upRightTile && isValidSquare(x + downDiagonal, y - downDiagonal)) board[x + downDiagonal][y - downDiagonal] += 4;
        if (downLeftTile && isValidSquare(x - downDiagonal, y + downDiagonal)) board[x - downDiagonal][y + downDiagonal] += 4;
    }

    /**
     * Returns a value based on the win state of the game. Expects board to be free of move selectors.
     * @return 0: Neither side has won. 1: White wins. 2: Black wins. 3: Tie.
     */
    private int detectWin() {
        int totalWhite = 0, totalBlack = 0;
        Location firstWhite = null, firstBlack = null;
        int ret = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) { 
                    totalWhite++;
                    if (firstWhite == null) { firstWhite = new Location(i, j); }
                }
                if (board[i][j] == 2) { 
                    totalBlack++;
                    if (firstBlack == null) { firstBlack = new Location(i, j); }
                }
            }
        }
        if (totalWhite == 1) { ret += 1; }
        if (totalBlack == 1) { ret += 2; }
        if (ret > 0) { return ret; }

        boolean[][] visited = new boolean[8][8];
        dfsTotal = 0;
        
        dfs(firstWhite.getX(), firstWhite.getY(), visited, 1);
        if (totalWhite == dfsTotal) { ret += 1; }

        visited = new boolean[8][8];
        dfsTotal = 0;
        
        dfs(firstBlack.getX(), firstBlack.getY(), visited, 2);
        if (totalBlack == dfsTotal) { ret += 2; }

        return ret;
    }

    /**
     * Depth-First search algorithm that I may or may not have stolen from an internet forum and then modified.
     */
    private void dfs(int x, int y, boolean[][] visited, int input) {
        int dx[] = {1, 1, 0, -1, -1, -1, 0, 1};
        int dy[] = {0, 1, 1, 1, 0, -1, -1, -1};
        dfsTotal++;
        visited[x][y] = true;
        for(int i = 0; i < 8; i++) {
            int tstX = x + dx[i];
            int tstY = y + dy[i];

            if (isValidSquare(tstX, tstY)) {
                if(board[tstX][tstY] == input && visited[tstX][tstY] == false) dfs(tstX, tstY, visited, input);
            }
        }
    }

    /**
     * See if a square is within the limits of the board.
     */
    private boolean isValidSquare(int x, int y) {
        if (x > -1 && x < 8 && y > -1 && y < 8) return true;
        return false;
    }

    /**
     * Clears all green square "pieces" from the board.
     * Used when a move is made, or when a different token is selected.
     */
    public void clearMoveSelectors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] - 4 > -1) {
                    board[i][j] -= 4;
                }
            }
        }
    }

    
    private static Game instance;
    /**
     * Returns the static instance of the Game class.
     * Exists so I only ever have one Game object in existence.
     */
    public static Game getInstance() {
        if (instance == null) instance = new Game();
        return instance;
    }

}