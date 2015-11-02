import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int size;
    private int n;
    private LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        size = points.length;
        n = 0;
        segments = new LineSegment[size * size];
        
        Point[] pts = new Point[size];
        for (int i = 0; i < size; i++) { pts[i] = points[i]; }
        
        for (int i = 0; i < size; i++) {
            Arrays.sort(pts);
            Point pt = pts[i];
            Arrays.sort(pts, pt.slopeOrder());
            int head = 0;
            int tail = 0;
            int count = 0;
            
            for (int j = 1; j < size; j++) {
                double slope = pt.slopeTo(pts[j]);
                
                if (slope == Double.NEGATIVE_INFINITY) { throw new java.lang.IllegalArgumentException(); }
                
                if (j < size - 1) {
                    if (slope == pt.slopeTo(pts[j + 1])) {
                        if (slope != pt.slopeTo(pts[j - 1])) {
                            head = j;
                            count = 1;
                        } else { count++; }
                    } else if (slope == pt.slopeTo(pts[j - 1])) {
                        tail = j;
                        count++;
                        if (count >= 3 && pt.compareTo(pts[head]) < 0 && head != 0) {
                            segments[n++] = new LineSegment(pt, pts[tail]);
                        }
                    }
                } else if (slope == pt.slopeTo(pts[j - 1])) {
                    tail = j;
                    count++;
                    if (count >= 3 && pt.compareTo(pts[head]) < 0 && head != 0) {
                        segments[n++] = new LineSegment(pt, pts[tail]);
                    }
                }

//                if (slope == pt.slopeTo(points[j - 1])
//                        && slope == pt.slopeTo(points[j + 1])
//                        && pt.compareTo(points[j - 1]) == -1
//                        && pt.compareTo(points[j]) == -1
//                        && pt.compareTo(points[j + 1]) == -1) {
//                    segments[n++] = new LineSegment(pt, points[j + 1]);
//                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() { return n; }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[n];
        for (int i = 0; segments[i] != null; i++)
            segs[i] = segments[i];
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }

}