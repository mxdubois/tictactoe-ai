package com.michaelxdubois.tictactoe;

import java.io.*;

/**
 * A Tic Tac Toe player that can be either Minimax AI or user via CLI.
 * @author Michael DuBois
 * @version 2013.10.05
 */
class Player {

    private String name;
    private int AILevel = 0;
    private boolean nameEndsWithS = false;
    private int wins = 0;

    /**
     * Constructs a Player, either AI or user depending on format of name.
     * @param name - "AI-*" creates AI with Minimax depth *, otherwise, CLI user
     */
    public Player(String name) {
        this.name = name;
        if(name.charAt(name.length() - 1) == 's' || 
                name.charAt(name.length() - 1) == 'S') {
            nameEndsWithS = true;
        }
        if(name.substring(0,3).equals("AI-")) {
            try {
                AILevel = Integer.parseInt(name.substring(3));
            } catch(NumberFormatException e) {
                // Ideally we would ask the user if they meant to enter an AI,
                // but I'm lazy, so we'll just assume it's a user player.
                // So we do nothing. AILevel = 0;
            }
        }
    }

    /**
     * Returns the user-specified move or a move from Minimax.
     * @param gameState - the current GameState.
     * @return int index of square in GameState that should be marked.
     */
    public int getNextMove(GameState gameState) {
        int move = -1;
        if(nameEndsWithS) {
            System.out.println(name + "' move.");
        } else {
            System.out.println(name + "'s move.");
        }
        if(AILevel == 0) {
            move = getMoveFromUser();
        } else {
            // Otherwise, minimax with AILevel plies
            move = Minimax.getOptimalMove(gameState, AILevel);
        }
        return move;
    }

    /**
     * Prompts the user for a move via CLI input
     * @return int supplied by user
     */
    public int getMoveFromUser() {
        System.out.print("Enter move: ");
        String moveStr = null;
        int moveInt = -1;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        //  read input from CLI
        try {
            moveStr = br.readLine();
            moveInt = Integer.parseInt(moveStr) - 1;

            // TODO check if move is possible
        } catch (IOException ioe) {
            System.out.println("IO error reading input from user.");
            System.exit(1);
        } catch(NumberFormatException e) {
            System.out.println("You must enter a number corresponding to" + 
                    " the square in which you wish to play. Please try again.");
            moveInt = getMoveFromUser();
        }
        return moveInt;
    }

    /**
     * Returns the Player's name.
     * @return String this player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Player's AI-level.
     * @return player's AI-Level.
     */
    public int getAILevel() {
        return AILevel;
    }

    /**
     * Sets the Player's win count.
     * @param w - int number of wins
     */
    public void setWins(int w) {
        wins = w;
    }

    /**
     * Returns the Player's win count.
     * @return int number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns true if Player is an AI.
     * Suck it Turing.
     * @return boolean true if Player is AI, false if Player is human
     */
    public boolean isAI() {
        return (AILevel > 0);
    }
}
