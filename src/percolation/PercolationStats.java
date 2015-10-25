import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    
    private double[] threshold;
    private int size;
    private int times;
        
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        
        threshold = new double[T];
        size = N;
        times = T;
        int total = size * size;
        
        // for T-time experiments
        for (int k = 0; k < T; k++) {
            
            Percolation perc = new Percolation(size);
            
            // keep opening random sites until the percolation system percolates
            while (!perc.percolates()) {
                int i = StdRandom.uniform(size) + 1;
                int j = StdRandom.uniform(size) + 1;
                if (!perc.isOpen(i, j)) {
                    perc.open(i, j);
                    threshold[k]++;
                }
            }
            threshold[k] /= total; // the threshold of this experiment #k
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(times);
    }
        
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(times);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        if (N <= 0)
            throw new IllegalArgumentException("Positive integer parameters required!");
        if (T <= 0)
            throw new IllegalArgumentException("Positive integer parameters required!");
        
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
        
        
    }
}
