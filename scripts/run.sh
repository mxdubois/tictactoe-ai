DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
repodir="$(cd $DIR; git rev-parse --show-toplevel)"

java -cp "$repodir/bin" com.michaelxdubois.tictactoe.TicTacToe $1 $2 $3
