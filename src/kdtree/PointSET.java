import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET {
    private TreeSet<Point2D> points;
    
    // construct an empty set of points 
    public PointSET() {
        points = new TreeSet<Point2D>();
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    // number of points in the set 
    public int size() {
        return points.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw() {
        for (Point2D pt : points) { pt.draw(); }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException();
//        if (points.isEmpty()) return null;
        
        ArrayList<Point2D> pts = new ArrayList<Point2D>();
        for (Point2D pt : points) {
            if (rect.contains(pt)) {
                pts.add(pt);
            }
        }
        return pts;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (points.isEmpty()) return null;

        double shortestDist = 0.0;
        double dist = 0.0;
        Point2D nearest = null;
        for (Point2D pt : points) {
            dist = pt.distanceSquaredTo(p);
            if (nearest == null || dist < shortestDist) {
                shortestDist = dist;
                nearest = pt;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        
    }
}
