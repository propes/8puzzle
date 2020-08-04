import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode goal;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        solve(initial, SearchMode.Manhattan);
    }

    public boolean isSolvable() {
        return goal != null;
    }

    public int moves() {
        return goal == null ? -1 : goal.moveCount();
    }

    public Iterable<Board> solution() {
        return goal == null ? null : goal.moves();
    }

    private void solve(Board initial, SearchMode mode) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, null, 0, mode));
        pqTwin.insert(new SearchNode(initial.twin(), null, 0, mode));
        SearchNode node = pq.delMin();
        SearchNode twinNode = pqTwin.delMin();

        while (!node.isGoal() && !twinNode.isGoal()) {
            for (SearchNode neighbor : node.neighbors()) {
                pq.insert(neighbor);
            }
            node = pq.delMin();
            if (node.isGoal()) break;

            for (SearchNode neighbor : twinNode.neighbors()) {
                pqTwin.insert(neighbor);
            }
            twinNode = pqTwin.delMin();
        }

        goal = node.isGoal() ? node : null;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
