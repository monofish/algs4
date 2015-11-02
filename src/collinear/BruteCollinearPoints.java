import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int size;
    private int n;
    private LineSegment[] segments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new java.lang.NullPointerException(); }
        
        size = points.length;
        n = 0;
        double[] slope = new double[3];
        segments = new LineSegment[size * size];
        
        Point[] pts = new Point[size];
        for (int i = 0; i < size; i++) { pts[i] = points[i]; }
        Arrays.sort(pts);
        
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                slope[0] = pts[i].slopeTo(pts[j]);
                if (slope[0] == Double.NEGATIVE_INFINITY) { throw new java.lang.IllegalArgumentException(); }
                for (int k = j + 1; k < size; k++) {
                    slope[1] = pts[j].slopeTo(pts[k]);
                    if (slope[1] == Double.NEGATIVE_INFINITY) { throw new java.lang.IllegalArgumentException(); }
                    for (int l = k + 1; l < size; l++) {
                        slope[2] = pts[k].slopeTo(pts[l]);
                        if (slope[2] == Double.NEGATIVE_INFINITY) { throw new java.lang.IllegalArgumentException(); }
                        if (slope[0] == slope[1] && slope[0] == slope[2]) {
                            segments[n] = new LineSegment(pts[i], pts[l]);
                            n++;
                        }
                    }
                }
            }
        }
    }
    
    // the number of line segments
    public int numberOfSegments() { return n; }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[n];
        for (int i = 0; segments[i] != null; i++) { segs[i] = segments[i]; }
            
        return segs;
    }
    
    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }        
    }
}