/**
 * 
 */

/**
 * @author Wei
 *
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {          // find a solution to the initial board (using the A* algorithm)

    private MinPQ<Step> minPQ;
    private MinPQ<Step> minPQTwin;
    private final ArrayList<Step> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        
        solution = new ArrayList<Step>();
        
        minPQ = new MinPQ<Step>(priority());
        Step lastStep = null;
        minPQ.insert(new Step(initial, 0, lastStep));

        Board twin = initial.twin();
        minPQTwin = new MinPQ<Step>(priority());
        Step lastStepTwin = null;
        minPQTwin.insert(new Step(twin, 0, lastStepTwin));

        do {

//            StdOut.println("---------- Step " + solution.size() + ": ----------" + mov);

            lastStep = minPQ.delMin();

//            StdOut.println("Priority: " + lastStep.getPriority());
//            StdOut.println("Neighbors: ");

//                    solution.add(lastStep);
            for (Board neighbor : lastStep.getBoard().neighbors()) {
                if ((lastStep.getPrevious() == null) || !neighbor.equals(lastStep.getPrevious().getBoard())) {
                    minPQ.insert(new Step(neighbor, lastStep.getMoves() + 1, lastStep));
//                    StdOut.println(neighbor);
//                    StdOut.println("Manhattan: " + lastStep.getBoard().manhattan());
                }
            }

            lastStepTwin = minPQTwin.delMin();
            for (Board neighborTwin : lastStepTwin.getBoard().neighbors()) {
                if ((lastStepTwin.getPrevious() == null) || !neighborTwin.equals(lastStepTwin.getPrevious().getBoard())) {
                    minPQTwin.insert(new Step(neighborTwin, lastStep.getMoves() + 1, lastStepTwin));
                }
            }

//            if (lastStepTwin.getBoard().isGoal())
//                StdOut.println(lastStepTwin.getBoard().isGoal());
//            StdOut.println("Candidates:");
//            for (Step candidate : minPQ) {
//                StdOut.println(candidate.getBoard());
//                StdOut.println("Priority: " + candidate.getPriority());
//            }
        } while (!lastStep.getBoard().isGoal() && !lastStepTwin.getBoard().isGoal());

        solution.add(lastStep);
        while ((lastStep = lastStep.getPrevious()) != null) {
            solution.add(0, lastStep);
        }
    }

    private static class Step {
        private Board board;
        private Step prevStep;
        private int moves;

        private Step(Board bd, int mov, Step prev) {
            this.board = bd;
            this.moves = mov;
            this.prevStep = prev;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public int getPriority() {
            return board.manhattan() + moves;
        }

        public Step getPrevious() {
            return prevStep;
        }

    }

    private static Comparator<Step> priority() {
        return new Comparator<Step>() {
            @Override
            public int compare(Step s1, Step s2) {
                return s1.getPriority() - s2.getPriority();
            }
        };
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution.get(solution.size() - 1).getBoard().isGoal();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return solution.size() - 1;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new SolutionIterator();
            }
        };
        else return null;
    }

    private class SolutionIterator implements Iterator<Board> {
        private int index;

        public SolutionIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < solution.size();
        }

        @Override
        public Board next() {
            if (hasNext()) return solution.get(index++).getBoard();
            else return null;
        }

        @Override
        public void remove() { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        StdOut.println("============================ " + args[0] + " ============================");
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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