import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode goal;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        solve(initial);
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
        return goal == null ? -1 : goal.moveCount();
    }

    public Iterable<Board> solution() {
        return goal == null ? null : goal.moves();
    }

    private void solve(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new SearchNodeComparator());
        pq.insert(new SearchNode(initial, null, 0, SearchMode.Hamming));
        SearchNode node = pq.delMin();

        while (!node.isGoal()) {
            for (SearchNode neighbor : node.neighbors()) {
                pq.insert(neighbor);
            }

            node = pq.delMin();
        }

        goal = node;
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
