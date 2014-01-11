package com.michaelxdubois.tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A CLI Tic Tac Toe game with Minimax AI opponents.
 * @author Michael DuBois
 * @version 2013.10.05
 */
class TicTacToe {

    public static void main (String[] args) {
        if(args.length < 2) {
            System.out.println(
                    "Invalid arguments. \n" +
                    "Usage: tictactoe user1 user2 [numMatches]");
            return;
        }

        String player1 = args[0];
        String player2 = args[1];
        int numMatches = (args.length >= 3) ? Integer.parseInt(args[2]) : 1;
        TicTacToe ticTacToe = new TicTacToe(player1, player2, numMatches);
        ticTacToe.run();

    }

    private static final int CONSOLE_WIDTH = 80;
    private Player player1;
    private Player player2;
    private int numMatches = 1;

    private int numTies = 0;

    /**
     * Constructs a TicTacToe object.
     */
    public TicTacToe(String p1, String p2, int n) {
        this.player1 = new Player(p1);
        this.player2 = new Player(p2);
        this.numMatches = n;
    }

    /**
     * Runs the tournament with numMatches.
     */
    public void run() {
        for(int i=0; i < numMatches; i++) {
            System.out.print(formatLine(CONSOLE_WIDTH, '#'));
            System.out.println("ROUND " + (i + 1) + " ... Fight!");
            System.out.print(formatLine(CONSOLE_WIDTH, '#'));
            Player winningPlayer = null;
            // Determine first player
            if(i % 2 == 0) {
                winningPlayer = playMatch(player1, player2);
            } else {
                winningPlayer = playMatch(player2, player1);
            }
            // Display results
            if(winningPlayer != null) {
                System.out.println(winningPlayer.getName() + 
                        " wins ROUND " + (i+1) + "!");
                winningPlayer.setWins(winningPlayer.getWins() + 1);
            } else {
                System.out.println("TIED ROUND " + (i+1) + ".");
                numTies++;
            }
            System.out.print(formatLine(CONSOLE_WIDTH, '='));
            if(i == numMatches - 1) {
                displayScore(true);
            } else {
                displayScore(false);
            }
        }
    }

    /**
     * Runs a single match.
     * @param firstPlayer - the Player who plays first
     * @param secondPlayer - the Player who plays second
     * @return the Player that wins or null if tied
     */
    private Player playMatch(Player firstPlayer, Player secondPlayer) {
        int winner = 0;
        Player winningPlayer = null;
        GameState gameState = new GameState();
        System.out.println(gameState.toString());
        int i = 0;
        while(!gameState.isTerminal()) {
            // Ask next player for move until they provide a valid one.
            GameState newGameState = null;
            while(newGameState == null) {
                int move = -1;
                if(i % 2 == 0) {
                    move = firstPlayer.getNextMove(gameState);
                } else {
                    move = secondPlayer.getNextMove(gameState);
                }
                try {
                    newGameState = new GameState(gameState, move);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    newGameState = null;
                }
            }
            gameState = newGameState;
            // Print game state
            System.out.println(gameState.toString());
            i++;
        }
        winner = gameState.getWinner();
        if(winner == GameState.P1) {
            winningPlayer = firstPlayer;
        } else if(winner == GameState.P2) {
            winningPlayer = secondPlayer;
        }
        return winningPlayer;
    }

    /**
     * Prints the current score and tournament winner if isFinal is true.
     * @param isFinal - true if tournament is over, false if not
     */
    private void displayScore(boolean isFinal) {
        System.out.println("PLAYER1 " + 
                player1.getName() + ": " + player1.getWins());
        System.out.println("PLAYER2 " + 
                player2.getName() + ": " + player2.getWins());
        if(numTies != 1) {
            System.out.println("(" + numTies + " ties)");
        } else {
            System.out.println("(" + numTies + " tie)");
        }
        if(isFinal) {
            if(player1.getWins() > player2.getWins()) {
                System.out.println("PLAYER1 " + 
                        player1.getName() + " WINS THE TOURNAMENT!");
            } else if(player1.getWins() < player2.getWins()) {
                System.out.println("PLAYER2 " + 
                        player2.getName() + " WINS THE TOURNAMENT!");

            } else {
                System.out.println("TOURNAMENT IS A DRAW!");
            }
        }
    }

    /**
     * Returns a string containing an ascii line charLength characters wide
     * @param charLength - the length of the line measured in char widths
     * @param character - the character with which to compose the line
     */
    public String formatLine(int charLength, char character) {
        String ret = "";
        // TODO is this stupidly inefficient with immutable strings?
        for(int i=0; i<=charLength; i++) {
            ret += character;
        }
        return ret += "\n";
    }

    /**
     * Returns a string containing an ascii line charLength characters wide
     * @param charLength - the length of the line measured in char widths
     * @param character - the character with which to compose the line
     */
    public String formatLine(int charLength) {
        return formatLine(charLength, '-');
    }

}
