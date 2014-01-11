DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
repodir="$(cd $DIR; git rev-parse --show-toplevel)"

javac "$repodir"/src/com/michaelxdubois/tictactoe/*.java -d "$repodir/bin"
