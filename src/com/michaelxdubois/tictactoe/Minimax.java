package com.michaelxdubois.tictactoe;

import java.util.List;

/**
 * A class that performs minimax on TicTacToe GameStates.
 * @author Michael DuBois
 * @version 2013.10.06
 */
class Minimax {

    // A win score that is large enough that it won't conflict with heuristic 
    // scores
    public static final int WIN_SCORE = 2*GameState.BOARD_TOTAL_RUNS;
    
    /**
     * Private constructor. No Minimax instances for you.
     */
    private Minimax() {
    }

    /**
     * Static method to get the moving player's optimal move from current state.
     * @param g - current GameState
     * @param depth - int depth to search the GameState tree
     * @return int index of square in GameState representing optimal move
     */
    public static int getOptimalMove(GameState g, int depth) {
        // TODO run-blocking tie-breaking could make AI < 9 smarter
        if(g.getFilledSquares() == 0) {
            return GameState.BOARD_CENTER_IDX;
        }
        int playerToken = g.getMovingPlayer();
        GameState optimalState = null;
        // Start with worst possible score
        // positive if minimizing player, negative if maximizing player
        int bestScore = -2*playerToken*WIN_SCORE;
        for(GameState child : g.getChildren()) {
            // Get player1's score of this child (Max's score)
            int score = minimax(child, depth-1, false);
            // If playerToken is negative, it flips the inequality so that 
            // bestScore is minimized
            if(playerToken*score > playerToken*bestScore) {
                bestScore = score;
                optimalState = child;
            }
        }
        return optimalState.getLastMove();
    }

    /**
     * Static method that returns the depth-limited minimax score of specified 
     * node.
     * @param node - GameState to score
     * @param depth - int depth to search the tree
     * @param debug - boolean, true turns on debug print statements
     */
    public static int minimax(GameState node, int depth, boolean debug) {
        return minimax(node, depth, -1*WIN_SCORE, WIN_SCORE, debug);
    }

    /**
     * Static method uses recursion to score specified node up to depth.
     * @param node - the GameState to score
     * @param depth - int, the max depth to search the tree
     * @param alpha - int, the best score yet found
     * @param beta - int, the worst score yet found
     * @param debug - boolean, true turns on debug print statements
     */
    private static int minimax(GameState node, 
            int depth, 
            int alpha, 
            int beta, 
            boolean debug) {
        int initialAlpha = alpha;
        int initialBeta = beta;
        // Get the current moving player (-1 is Min, +1 is Max)
        int playerToken = node.getMovingPlayer();
        if(debug) {
            System.out.println("<depth" + depth + ">");
        }
        if(node.isTerminal()) {
            if(node.getWinner() == 0) {
                alpha = 0;
            } else if(node.getWinner() == GameState.P1) {
                alpha = WIN_SCORE;
            } else {
                alpha = -1*WIN_SCORE;
            }
            beta = alpha;
            if(debug) {
                System.out.println("Node is terminal.");
                System.out.println("playerToken: " + playerToken);
                System.out.println("alpha=beta=" + alpha);
            }

        } else if(depth == 0) {
            // Return heuristic approximation of likelihood of a win
            // Ours is based on the number of unblocked partial runs occupied
            // by player 1 (Max)
            alpha = node.unblockedPartialRuns(GameState.P1);
            // and by player 2 (Min)
            beta = -1*node.unblockedPartialRuns(GameState.P2);
        } else {
            // Recursively score this node's children to determine it's score.
            for(GameState child : node.getChildren()) {
                int value = minimax(child, depth - 1, alpha, beta, debug);
                // If moving player is Max
                if(playerToken > 0) {
                    alpha = Math.max(alpha, value);
                }
                // If moving player is Min
                if(playerToken < 0) {
                    beta = Math.min(beta, value);
                }
                // If the worst score is leq than the best score we've found
                if(beta <= alpha) {
                    // Then we don't need to look at any more of node's children
                    // b/c node's score will definitely be worse than one of
                    // it's sibling nodes
                    break;
                }
            }
        }
        if(debug) {
            System.out.println(node.toString());
            System.out.println("playerToken: " + playerToken);
            System.out.println("initial alpha: " + initialAlpha);
            System.out.println("initial beta: " + initialBeta);
            System.out.println("alpha: " + alpha);
            System.out.println("beta: " + beta);
            System.out.println("</depth" + depth + ">");
        }

        return (playerToken > 0) ? alpha : beta;
    }
}
