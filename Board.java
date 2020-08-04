import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] tiles;

    public Board(int[][] tiles) {
        if (tiles.length != tiles[0].length) throw new IllegalArgumentException(
                "Array must have an equal number of rows and columns.");

        this.tiles = tiles;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append("\n");

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(String.format("%2d ", tiles[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public int dimension() {
        return tiles.length;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (i == dimension() - 1 && j == dimension() - 1) break;
                if (tiles[i][j] != rowColToNumber(i, j)) count++;
            }
        }
        return count;
    }

    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0) continue;
                sum += distanceFromGoalPosition(tiles[i][j], i, j);
            }
        }

        return sum;
    }


    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (i == dimension() - 1 && j == dimension() - 1) break;
                if (tiles[i][j] != rowColToNumber(i, j)) return false;
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;

        if (board.dimension() != this.dimension()) return false;

        return Arrays.deepEquals(tiles, board.tiles);
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public Iterable<Board> neighbors() {
        Queue<Board> neighbours = new Queue<>();
        int[] rc = findBlankTile();

        int[][] offsets = {
                { -1, 0 },
                { 0, -1 },
                { 0, 1 },
                { 1, 0 }
        };
        for (int[] offset : offsets) {
            if (rc[0] + offset[0] < 0 || rc[1] + offset[1] < 0) continue;
            int[][] tilesCopy = copyTiles();
            swapTiles(tilesCopy, rc, new int[] { rc[0] + offset[0], rc[1] + offset[1] });
            neighbours.enqueue(new Board(tilesCopy));
        }

        return neighbours;
    }

    public Board twin() {
        int[][] tilesCopy = copyTiles();
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tilesCopy[i][j] == 0) continue;
                swapTilesWithNext(tilesCopy, new int[] { i, j });
                return new Board(tilesCopy);
            }
        }

        throw new IndexOutOfBoundsException("Board does not contain non-blank tiles");
    }

    private void swapTilesWithNext(int[][] tilesCopy, int[] rc) {
        int pos = rowColToPosition(rc[0], rc[1]);
        int[] rcNext = positionToRowCol(pos + 1);
        swapTiles(tilesCopy, rc, rcNext);
    }

    private int distanceFromGoalPosition(int number, int row, int col) {
        int[] rc = numberToRowCol(number);
        return Math.abs(rc[0] - row) + Math.abs(rc[1] - col);
    }

    private int rowColToPosition(int row, int col) {
        return row * dimension() + col;
    }

    private int rowColToNumber(int row, int col) {
        return rowColToPosition(row, col) + 1;
    }

    private int[] positionToRowCol(int position) {
        return new int[] { position / dimension(), position % dimension() };
    }

    private int[] numberToRowCol(int number) {
        return positionToRowCol(number - 1);
    }

    private void swapTiles(int[][] tilesCopy, int[] rc1, int[] rc2) {
        int temp = tilesCopy[rc1[0]][rc1[1]];
        tilesCopy[rc1[0]][rc1[1]] = tilesCopy[rc2[0]][rc2[1]];
        tilesCopy[rc2[0]][rc2[1]] = temp;
    }

    private int[][] copyTiles() {
        int[][] copy = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    private int[] findBlankTile() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0) return new int[] { i, j };
            }
        }
        throw new NoSuchElementException("Board does not contain a blank tile");
    }

    public static void main(String[] args) {
        int[][] tiles = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };

        Board board = new Board(tiles);
        StdOut.println(board.toString());

        Assert.equal(3, board.dimension(), "dimension should be 3");

        Assert.equal(0, board.hamming(), "hamming should be zero when all tiles are in place");
        Assert.equal(0, board.manhattan(), "manhattan should be zero when all tiles are in place");
        Assert.isTrue(board.isGoal(), "board should be goal when all tiles are in place");

        tiles = new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 0, 8 }
        };
        board = new Board(tiles);

        Assert.equal(1, board.hamming(), "hamming should be one when one tile is out of place");
        Assert.equal(1, board.manhattan(), "manhattan should be one when one tile is out of place");
        Assert.isFalse(board.isGoal(),
                       "board should not be one goal when one tile is out of place");

        tiles = new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };
        board = new Board(tiles);

        Assert.equal(5, board.hamming(), "hamming should match be 5 when 5 tiles are out of place");
        Assert.equal(10, board.manhattan(), "manhattan should be expected value");

        int[][] tiles2 = {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };

        Board board2 = new Board(tiles2);
        Assert.isTrue(board.equals(board2),
                      "board should be equal to another board with the same tile positions");

        tiles2 = new int[][] {
                { 1, 8, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };
        board2 = new Board(tiles2);

        Assert.isFalse(board.equals(board2),
                       "board should not be equal to another board with the different tile positions");

        tiles = new int[][] {
                { 1, 2, 3, },
                { 4, 0, 5, },
                { 6, 7, 8, }
        };
        board = new Board(tiles);
        Iterator<Board> iter = board.neighbors().iterator();
        Board next = iter.next();

        tiles2 = new int[][] {
                { 1, 0, 3, },
                { 4, 2, 5, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2), "1st neighbor should be as expected");

        next = iter.next();

        tiles2 = new int[][] {
                { 1, 2, 3, },
                { 0, 4, 5, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2), "2nd neighbor should be as expected");

        next = iter.next();

        tiles2 = new int[][] {
                { 1, 2, 3, },
                { 4, 5, 0, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2), "3rd neighbor should be as expected");

        next = iter.next();

        tiles2 = new int[][] {
                { 1, 2, 3, },
                { 4, 7, 5, },
                { 6, 0, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2), "4th neighbor should be as expected");

        tiles = new int[][] {
                { 0, 1, 2, },
                { 3, 4, 5, },
                { 6, 7, 8, }
        };
        board = new Board(tiles);
        iter = board.neighbors().iterator();
        next = iter.next();

        tiles2 = new int[][] {
                { 1, 0, 2, },
                { 3, 4, 5, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2),
                      "1st neighbor should be as expected given blank tile is in the top left corner");

        next = iter.next();

        tiles2 = new int[][] {
                { 3, 1, 2, },
                { 0, 4, 5, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(next.equals(board2),
                      "2nd neighbor should be as expected given blank tile is in the top left corner");

        tiles = new int[][] {
                { 0, 1, 2, },
                { 3, 4, 5, },
                { 6, 7, 8, }
        };
        board = new Board(tiles);

        tiles2 = new int[][] {
                { 0, 2, 1, },
                { 3, 4, 5, },
                { 6, 7, 8, }
        };
        board2 = new Board(tiles2);

        Assert.isTrue(board.twin().equals(board2),
                      "twin board should have first two tiles swapped");

        if (Assert.allTestsPassed())
            StdOut.println("All tested passed.");
    }
}
