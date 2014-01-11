package com.michaelxdubois.tictactoe;

import java.util.ArrayList;
import java.util.List;
/**
 * Stores a Tic Tac Toe game state and supplies helper functions for 
 * analyzing it.
 * @author Michael DuBois
 * @version 2013.10.05
 */
class GameState {
    // Tokens for P1 and P2. BEWARE: changing these might break things.
    public static final int P1 = 1;
    public static final int P2 = -1;

    public static final int BOARD_SQUARES = 9; // Must be square
    // (BOARD_SQUARES-1)/2 to hit diaganols if BOARD_SQUARES is even.
    public static final int BOARD_CENTER_IDX = (BOARD_SQUARES-1)/2;
    public static final int BOARD_ROW_LENGTH = 3;
    public static final int BOARD_COL_LENGTH = BOARD_ROW_LENGTH;
    public static final int BOARD_TOTAL_RUNS = 
        BOARD_ROW_LENGTH + BOARD_COL_LENGTH + 2;
    // Width of the board when printed, used for pretty toString
    private static final int BOARD_CHAR_WIDTH = BOARD_ROW_LENGTH * 4 + 1;

    private int[] squares;
    // we'll track this number so we don't have to compute each time
    private int filledSquares = 0;

    // These are cached the first time computeRuns() is run
    private int p1UnblockedRuns = -1;
    private int p2UnblockedRuns = -1;
    private int winner = 0;

    private int movingPlayer = P1; // player token that moves next
    private int lastMove = -1; // last move performed

    /**
     * Constructs the starting GameState
     */
    public GameState() {
        squares = new int[BOARD_SQUARES]; // all zeroes by default
    }

    /**
     * Constructs a GameState from a previous GameState and a move.
     * @param g - the previous GameState
     * @param move - the int index of the square on the board in which to move
     */
    public GameState(GameState g, int move) {
        if(g.isTerminal()) {
            throw new IllegalArgumentException("Gamestate g is terminal." + 
                    " Cannot create a new Gamestate from it.");
        }
        if(move >= BOARD_SQUARES) {
            throw new IllegalArgumentException(
                    (move + 1) + " is not a valid square.");
        }
        squares = g.getSquaresCopy();
        if(squares[move] != 0) {
            throw new IllegalArgumentException(
                    "That move cannot be performed. Square " + 
                    (move + 1) + " is already taken.");
        }
        squares[move] = g.getMovingPlayer();
        lastMove = move;
        filledSquares = g.getFilledSquares() + 1;
        movingPlayer = -1*g.getMovingPlayer();
    }

    /**
     * Returns true if this GameState marks the end of the game.
     * @return boolean, true if game tied or a player won, false if not over.
     */
    public boolean isTerminal() {
        return (BOARD_SQUARES == filledSquares || getWinner() != 0);
    }
    
    /**
     * Returns the unblocked partial runs for the given player.
     * @param playerToken - int GameState.P1 or GameState.P2
     */
    public int unblockedPartialRuns(int playerToken) {
        int runs = 0;
        if(p1UnblockedRuns == -1) {
            computeRuns();
        }
        if(playerToken == P1) {
            runs = p1UnblockedRuns;
        } else if(playerToken == P2) {
            runs = p2UnblockedRuns;
        }
        return runs;
    }

    /**
     * Caches the unblocked partial runs for both players and checks for wins
     */
    private void computeRuns() {
        p1UnblockedRuns = 0;
        p2UnblockedRuns = 0;
        // tracking p1
        int[] p1cols = new int[BOARD_ROW_LENGTH];
        int[] p1rows = new int[BOARD_COL_LENGTH];
        int[] p1diags = new int[2];
        // tracking p2, must be same length as corresponding p1 arrays
        int[] p2cols = new int[BOARD_ROW_LENGTH];
        int[] p2rows = new int[BOARD_COL_LENGTH];
        int[] p2diags = new int[2];

        // For each row
        for(int i=0; i < BOARD_COL_LENGTH; i++) {
            // For each item in row (for each col)
            for(int j=0; j < BOARD_ROW_LENGTH; j++) {
                findRunsForTile(p1cols, p1rows, p1diags, i, j, P1);
                findRunsForTile(p2cols, p2rows, p2diags, i, j, P2);
            }
        }
        for(int i=0; i < p1cols.length; i++) {
            // For p1
            if(p1cols[i] > 0) {
                p1UnblockedRuns++;
            } 
            if(p1cols[i] == BOARD_COL_LENGTH) {
                winner = P1;
            }
            // For p2
            if(p2cols[i] > 0) {
                p2UnblockedRuns++;
            } 
            if(p2cols[i] == BOARD_COL_LENGTH) {
                winner = P2;
            }

        }
        for(int i=0; i < p1rows.length; i++) {
            // For p1
            if(p1rows[i] > 0) {
                p1UnblockedRuns++;
            } 
            if(p1rows[i] == BOARD_ROW_LENGTH) {
                winner = P1;
            }
            // For p2
            if(p2rows[i] > 0) {
                p2UnblockedRuns++;
            } 
            if(p2rows[i] == BOARD_ROW_LENGTH) {
                winner = P2;
            }

        }
        for(int i=0; i < p1diags.length; i++) {
            // For p1
            if(p1diags[i] > 0) {
                p1UnblockedRuns++;
            }
            if(p1diags[i] == BOARD_ROW_LENGTH) {
                winner = P1;
            }
            // For p2
            if(p2diags[i] > 0) {
                p2UnblockedRuns++;
            }
            if(p2diags[i] == BOARD_ROW_LENGTH) {
                winner = P2;
            }
        }
   }

   /**
    * Increments unblocked runs by their length and marks blocked runs in given
    *  arrays.
    *  @param cols - int[] in which to count lengths for runs in columns
    *  @param rows - int[] in which to count lengths for runs in rows
    *  @param diags - int[] in which to count lengths for runs in diaganols
    *  @param i - the zero-indexed int row of the current tile
    *  @param j - the zero-indexed int column of the current tile
    *  @param playerToken - int GameState.P1 or GameState.P2 of player for runs
    */
   private void findRunsForTile(
           int[] cols, int[] rows, int[] diags, 
           int i, int j, int playerToken) {
        int idx = (i*BOARD_ROW_LENGTH) + j;
        if(squares[idx] == -1*playerToken) {
            cols[j] = -1;
            rows[i] = -1;
            // If in diag0
            if(i == j) {
                diags[0] = -1;
            }
            // If in diag1
            if(i == (BOARD_ROW_LENGTH - j - 1)) {
                diags[1] = -1;
            }

        } else if(squares[idx] == playerToken) {
            if(cols[j] != -1) {
                cols[j] +=1;
            }
            if(rows[i] != -1) {
                rows[i] +=1;
            }
            // If in diag0 and diag0 not blocked
            if(i == j && diags[0] != -1) {
                diags[0] +=1;
            }
            // If in diag1 and diag1 not blocked
            if(i == (BOARD_ROW_LENGTH - j - 1) && diags[1] != -1) {
                diags[1] += 1;
            }
        }

   }

   /**
    * Returns a List of possible child GameStates
    * @return List<GameState> of possible children states.
    */
   public List<GameState> getChildren() {
       List<GameState> children = new ArrayList<GameState>();
       int unfilledSquares = BOARD_SQUARES - filledSquares;
       for(int i=0; i < squares.length; i++) {
           if(squares[i] == 0) {
               children.add(new GameState(this, i));
           }
           // break early if the rest of the board is filled
           if(children.size() == unfilledSquares) {
               break;
           }
       }
       return children;
   }

   /**
    * Returns the moved performed to reach this state.
    * @return int index of square last filled in squares array
    */
   public int getLastMove() {
       return lastMove;
   }

   /**
    * Returns the int token of the player who should move next.
    * @return int either GameState.P1 or GameState.P2 of player moving next.
    */
   public int getMovingPlayer() {
       return movingPlayer;
   }

   /**
    * Returns the int token of the winner or zero if no current winner.
    * Note that a zero returned does not mean the game has tied. 
    * Use isTerminal() to check if the game is over.
    * @return int winner GameState.P1 or GameState.P2 or zero if no winner
    */
   public int getWinner() {
       if(p1UnblockedRuns == -1) {
          computeRuns();
       } 
       return winner;
    }

    /**
     * Returns the number of squares currently filled.
     * @return int number of squares filled (non-zero) in squares array.
     */
    public int getFilledSquares() {
        return filledSquares;
    }

    /** 
     * Returns a copy of the squares array.
     * @return int[] copy of squares array
     */
    public int[] getSquaresCopy() {
        int[] squaresCopy = new int[BOARD_SQUARES];
        System.arraycopy(squares, 0, squaresCopy, 0, squares.length);
        return squaresCopy;
    }

    /** 
     * Returns a string representation of this GameState.
     * @return String representing this GameState
     */
    public String toString() {
        String str = "";
        for(int i=0; i < BOARD_CHAR_WIDTH; i++) {
            str += "-";
        }
        for(int i=0; i < squares.length; i++) {
            if(i % BOARD_ROW_LENGTH == 0) {
                str += "\n|";
            }
            String squareStr = "" + squares[i];
            if(squares[i] == P1) {
                squareStr = "X";
            } else if( squares[i] == P2) {
                squareStr = "O";
            } else {
                squareStr = "" + (i + 1); // The 1-indexed number of the square
            }
            str+= " " + squareStr + " |";
        }
        str += "\n";
        for(int i=0; i < BOARD_CHAR_WIDTH; i++) {
            str += "-";
        }
        str += "\n";
        return str;
    }

}
