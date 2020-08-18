import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class SearchNode implements Comparable<SearchNode> {
    private final Board board;
    private final SearchNode prev;
    private final int moveCount;
    private final SearchMode mode;

    public SearchNode(Board board, SearchNode prev, int moveCount, SearchMode mode) {
        this.board = board;
        this.prev = prev;
        this.moveCount = moveCount;
        this.mode = mode;
    }

    private Board board() {
        return board;
    }

    private SearchNode prev() {
        return prev;
    }

    public int moveCount() {
        return moveCount;
    }

    public Iterable<SearchNode> neighbors() {
        Queue<SearchNode> neighbors = new Queue<>();
        for (Board neighborBoard : board.neighbors()) {
            if (!containsBoard(neighborBoard))
                neighbors.enqueue(new SearchNode(neighborBoard, this, moveCount + 1, mode));
        }
        return neighbors;
    }

    public boolean isGoal() {
        return board.isGoal();
    }

    public Iterable<Board> moves() {
        Stack<Board> moves = new Stack<>();
        moves.push(board);
        SearchNode prevNode = prev;
        while (prevNode != null) {
            moves.push(prevNode.board());
            prevNode = prevNode.prev();
        }

        return moves;
    }

    private boolean containsBoard(Board someBoard) {
        return prev != null && prev.board().equals(someBoard);
    }

    public int compareTo(SearchNode searchNode) {
        return this.priority() - searchNode.priority();
    }

    private int priority() {
        return (mode == SearchMode.Manhattan ? board.manhattan() : board.hamming()) + moveCount;
    }
}
