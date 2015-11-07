import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] board;
    private Board[] neighbors;
    private int zeroI;
    private int zeroJ;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new java.lang.NullPointerException();
        if (blocks.length != blocks[0].length || blocks.length == 0) throw new java.lang.IllegalArgumentException();

        board = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                board[i][j] = blocks[i][j];
                if (board[i][j] == 0) { // calculate how many neighbors it have
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
    }

    // board dimension N
    public int dimension() {
        return board.length;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue;
                else if (!isRight(i, j)) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        int offset_i;
        int offset_j;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) continue;
                else {
                    offset_i = Math.abs((board[i][j] - 1) / dimension() - i);
                    offset_j = Math.abs((board[i][j] - 1) % dimension() - j);
//                    StdOut.println(board[i][j] + " [" + i + ", " + j + "] : " + offset_i + ", " + offset_j);
                    manhattan += (offset_i + offset_j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (!isRight(i, j)) return false;
                else continue;
            }
        }
        return true;
    }

    private boolean isRight(int i, int j) {
        return board[i][j] == (i * dimension() + j + 1) % (dimension() * dimension());
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] bd = new int[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            bd[row] = Arrays.copyOf(board[row], board[row].length);
        }
        
        int zeroIdx = zeroI * dimension() + zeroJ;
        int rand1;
        int rand2;
        do {
            rand1 = StdRandom.uniform(dimension() * dimension());
            rand2 = StdRandom.uniform(dimension() * dimension());
        } while (rand1 == zeroIdx || rand2 == zeroIdx || rand1 == rand2);
        
        int i1 = rand1 / dimension();
        int j1 = rand1 % dimension();
        int i2 = rand2 / dimension();
        int j2 = rand2 % dimension();
        
        int temp = bd[i1][j1];
        bd[i1][j1] = bd[i2][j2];
        bd[i2][j2] = temp;

        Board twin = new Board(bd);
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.board.length != that.board.length) return false;
        for (int i = 0; i < board.length; i++) {
            if (this.board[i].length != that.board[i].length) return false;
            for (int j = 0; j < board[i].length; j++) {
                if (this.board[i][j] != that.board[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                if (neighbors == null) {
                    ArrayList<Board> neighborsBuilder = new ArrayList<Board>();
                    if (zeroI > 0) {
                        neighborsBuilder.add(move(zeroI - 1, zeroJ));
                    }
                    if (zeroI < dimension() - 1) {
                        neighborsBuilder.add(move(zeroI + 1, zeroJ));
                    }
                    if (zeroJ > 0) {
                        neighborsBuilder.add(move(zeroI, zeroJ - 1));
                    }
                    if (zeroJ < dimension() - 1) {
                        neighborsBuilder.add(move(zeroI, zeroJ + 1));
                    }
                    neighbors = neighborsBuilder.toArray(new Board[neighborsBuilder.size()]);
                }
                return new NeighborsIterator();
            }
        };
    }

    private class NeighborsIterator implements Iterator<Board> {

        private int index;

        public NeighborsIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < neighbors.length;
        }

        @Override
        public Board next() {
            if (hasNext()) return neighbors[index++];
            else return null;
        }

        @Override
        public void remove() { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
    }

    private Board move(int i, int j) {
        //        StdOut.println("(" + zeroI + ", " + zeroJ + ") -> (" + i + ", " + j + ")");
        int offset = (i + j) - (zeroI + zeroJ);
        if (Math.abs(offset) != 1)  { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
        
        int[][] bd = new int[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            bd[row] = Arrays.copyOf(board[row], board[row].length);
        }
        bd[zeroI][zeroJ] = bd[i][j];
        bd[i][j] = 0;
        return new Board(bd);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests
    public static void main(String[] args) {

        int[][] tiles = new int[][]{{2, 3, 0}, {1, 4, 5}, {7, 8, 6}};
        Board initial = new Board(tiles);
        StdOut.println("Testing board:");
        StdOut.println(initial);
        StdOut.println("Hamming dist. " + initial.hamming());
        StdOut.println("Manhattan dist. " + initial.manhattan());
        StdOut.println("Goal? " + initial.isGoal());
        StdOut.println("------------------------------");
        StdOut.println("Neighbor boards:");
        for (Board neighbor : initial.neighbors()) StdOut.println(neighbor);
        StdOut.println("------------------------------");
        StdOut.println("Twin boards:");
        for (int i = 0; i < 3; i++) 
            StdOut.println(initial.twin());
        
    }
}
