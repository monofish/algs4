/******************************************************************************
 *  Compilation:  javac PercolationVisualizer.java
 *  Dependencies: WeightedQuickUnionUF.java
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    // binary bits are used to represent the status of a site
    private static final int BLOCKED = 0; //0b000, the array initializes with all zeros
    private static final int OPEN = 4; //0b100
    private static final int FULL = 6; //0b110
    private static final int LEAKED = 5; //0b101, a leaked site means it connects to the bottom row
    
    private int size;    
    private int[] sites;
    private WeightedQuickUnionUF uf;
    private boolean percolates;
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N < 1) throw new IllegalArgumentException("Illeagal Argument");

        this.size = N;
        this.sites = new int[size * size]; // array sites[] represent the status of all sites
        this.uf = new WeightedQuickUnionUF(size * size); //uf represent the connectivity between sites

    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j); // check if arguments are legal
        int x = i - 1;
        int y = j - 1;
        int idx = x * size + y; // convert coordinates to the index of one-dimension array
        
        // open the site and check if it is in the top or bottom row
        sites[idx] = (sites[idx] | OPEN);
        if (x == 0) {
            sites[idx] = (sites[idx] | FULL);
        }
        if (x == size - 1) {
            sites[idx] = (sites[idx] | LEAKED);
        }
        
        // indices and coordinates of all neighbors
        int[] neighborIdx = {idx-1, idx+1, idx-size, idx+size};
        int[][] neighborXY = {{x, y-1}, {x, y+1}, {x-1, y}, {x+1, y}};
        
        for (int k = 0; k < 4; k++) {
            // if neighbor is legal and open
            if (neighborXY[k][0] >= 0 && neighborXY[k][0] < size
                   && neighborXY[k][1] >= 0 && neighborXY[k][1] < size
                   && isOpen(neighborXY[k][0] + 1, neighborXY[k][1] + 1)) {
                
                // share neighbor's status
                int rootStatus = sites[uf.find(neighborIdx[k])];
                if ((rootStatus & FULL) == FULL)
                    sites[idx] = (sites[idx] | FULL);
                if ((rootStatus & LEAKED) == LEAKED)
                    sites[idx] = (sites[idx] | LEAKED);
                
                // union with neighbor
                uf.union(neighborIdx[k], idx);
            }
        }
        
        int root = uf.find(idx);
        // share its new status with neighbors
        if ((sites[idx] & FULL) == FULL)
            sites[root] = (sites[root] | FULL);
        if ((sites[idx] & LEAKED) == LEAKED)
            sites[root] = (sites[root] | LEAKED);
        // the system percolate through this site only if the site is both full and leaked
        if ((sites[idx] & FULL) == FULL && (sites[idx] & LEAKED) == LEAKED)
                percolates = true;
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return ((sites[toIdx(i-1, j-1)] & OPEN) == OPEN);
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        int idx = toIdx(i-1, j-1);
        // it is full if its root is full
        if ((sites[uf.find(idx)] & FULL) == FULL)
            sites[idx] = sites[idx] | FULL;
        
        return (sites[idx] & FULL) == FULL;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }
    
    // convert the coordinates in two-dimension grid to the index in one-dimension array 
    private int toIdx(int x, int y) {
        return x *size + y;
    }
    
    // check if arguments are legal
    private void validate(int i, int j) {
        if (i < 1 || i > size) 
            throw new IndexOutOfBoundsException("Row index i out of bounds");
        if (j < 1 || j > size) 
            throw new IndexOutOfBoundsException("Column index j out of bounds");  
    }
    
    public static void main(String[] args) {
    }
}