tictactoe-ai
============

CLI tictactoe with minimax ai opponents. Now you can master those essential
 tictactoe skillz without embarassing yourself!

Setup
-------
Ensure that you have jdk 7 installed. If you're on a debian/ubuntu machine, 
you can install the jdk with the following command: 

    sudo apt-get install openjdk-7-jdk

Ensure that the scripts in the `scripts` directory are executable. One of many
ways to do this is:

    chmod 775 scripts/*

Compile
-------
To compile the project via `bash` and `javac`:

    scripts/compile.sh   
 
Usage
-----
Once you've compiled the project, you can run a TicTacToe game with:

    scripts/run.sh user1 user2 [numMatches]

Where `user1`/`user2` can be either a human player's name (string) or one of 
`AI-1` to `AI-9`, where the number after AI- corresponds to the number of 
"plies" (turns in the game or levels in the minimax tree) that the AI 
will evaluate before making a decision.

If `numMatches` is provided, the users will play the specified number of matches
before a winner is announced. Players alternate taking the first turn beginning
with `user1`.
