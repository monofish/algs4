import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    
    private static final RectHV WHOLE_CANVAS = new RectHV(0, 0, 1, 1);

    private Node root;
    
    // construct an empty set of points 
    public KdTree() {
        root = null;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return root == null;
    }
    
    // number of points in the set
    public int size() {
        if (this.isEmpty()) return 0;
        return root.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (this.isEmpty()) root = new Node(p, 0);
        else insert(p, root);
    }
    
    private void insert(Point2D p, Node n) {
        if (p.equals(n.point())) return;
        if (n.level() % 2 == 0) {
            if (p.x() < n.point().x()) {
                if (n.leftChild() != null) {
                    insert(p, n.leftChild());
                } else {
                    n.leftChild(new Node(p, n.level() + 1));
                }
            } else {
                if (n.rightChild() != null) {
                    insert(p, n.rightChild());
                } else {
                    n.rightChild(new Node(p, n.level() + 1));
                }
            }
        } else {
            if (p.y() < n.point().y()) {
                if (n.leftChild() != null) {
                    insert(p, n.leftChild());
                } else {
                    n.leftChild(new Node(p, n.level() + 1));
                }
            } else {
                if (n.rightChild() != null) {
                    insert(p, n.rightChild());
                } else {                                      
                    n.rightChild(new Node(p, n.level() + 1));
                }
            }
        }
        
        n.updateSize();
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return this.contains(p, root, WHOLE_CANVAS);
    }
    
    private boolean contains(Point2D p, Node n, RectHV region) {
        if (n == null || !region.contains(p)) return false;
        if (p.equals(n.point())) return true;

        if (n.level() % 2 == 0) {
            if (p.x() < n.point().x()) {
                return contains(p, n.leftChild(), leftRegion(n, region));
            } else {
                return contains(p, n.rightChild(), rightRegion(n, region));
            }
        } else {
            if (p.y() < n.point().y()) {
                return contains(p, n.leftChild(), leftRegion(n, region));
            } else {
                return contains(p, n.rightChild(), rightRegion(n, region));
            }
        }
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root, WHOLE_CANVAS);
    }
    
    private void draw(Node n, RectHV region) {
        if (n == null) return;
        else {
            StdDraw.setPenRadius(.001);
            if (n.level() % 2 == 0) {   // draw the vertical line segment in red
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.point().x(), region.ymin(), n.point().x(), region.ymax());
            } else { // draw the horizontal line segment in blue
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(region.xmin(), n.point().y(), region.xmax(), n.point().y());
            }
            draw(n.leftChild(), leftRegion(n, region));
            draw(n.rightChild(), rightRegion(n, region));
            StdDraw.setPenRadius(.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            n.point().draw(); // draw the point in black
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException();
        ArrayList<Point2D> pts = new ArrayList<Point2D>();
        pts = findInRect(root, pts, rect, WHOLE_CANVAS);
        return pts;
    }
    
    private ArrayList<Point2D> findInRect(Node n, ArrayList<Point2D> pts, RectHV rect, RectHV region) {
        if (n != null && rect.intersects(region)) {
            if (rect.contains(n.point())) pts.add(n.point());
            pts = findInRect(n.leftChild(), pts, rect, leftRegion(n, region));
            pts = findInRect(n.rightChild(), pts, rect, rightRegion(n, region));
        }
        return pts;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        Point2D nearest = null;
        nearest = findNearest(root, nearest, p, WHOLE_CANVAS);
        return nearest;
    }
    
    private Point2D findNearest(Node n, Point2D nearest, Point2D p, RectHV region) {
        if (nearest == null && root != null) { nearest = root.point(); }
        if (n != null && p.distanceSquaredTo(nearest) >= region.distanceSquaredTo(p)) {
            if (p.distanceSquaredTo(n.point()) <= p.distanceSquaredTo(nearest)) {
                nearest = n.point();
            }
            if (n.level() % 2 == 0) {
                if (p.x() < n.point().x()) {
                    nearest = findNearest(n.leftChild(), nearest, p, leftRegion(n, region));
                    nearest = findNearest(n.rightChild(), nearest, p, rightRegion(n, region));
                } else {
                    nearest = findNearest(n.rightChild(), nearest, p, rightRegion(n, region));
                    nearest = findNearest(n.leftChild(), nearest, p, leftRegion(n, region));
                }
            } else {
                if (p.y() < n.point().y()) {
                    nearest = findNearest(n.leftChild(), nearest, p, leftRegion(n, region));
                    nearest = findNearest(n.rightChild(), nearest, p, rightRegion(n, region));
                } else {
                    nearest = findNearest(n.rightChild(), nearest, p, rightRegion(n, region));
                    nearest = findNearest(n.leftChild(), nearest, p, leftRegion(n, region));
                }
            }
        }
        return nearest;
    }
    
    private RectHV leftRegion(Node n, RectHV region) {
        if (n.level() % 2 == 0) {
            return new RectHV(region.xmin(), region.ymin(), n.point().x(), region.ymax());
        } else {
            return new RectHV(region.xmin(), region.ymin(), region.xmax(), n.point().y());
        }
    }
    
    private RectHV rightRegion(Node n, RectHV region) {
        if (n.level() % 2 == 0) {
            return new RectHV(n.point().x(), region.ymin(), region.xmax(), region.ymax());
        } else {
            return new RectHV(region.xmin(), n.point().y(), region.xmax(), region.ymax());
        }
    }
    
    private class Node {
        private final Point2D pt;
        private final int level;
        
        private int size;
        private Node left;
        private Node right;
        
        private Node(Point2D pt, int level) {
            this.pt = pt;
            this.level = level;
            this.size = 1;
            this.left = null;
            this.right = null;
        }
        
        public Point2D point() {
            return pt;
        }
        
        public int size() {
            return this.size;
        }
        
        private int updateSize() {
          int leftSize = 0;
          int rightSize = 0;
          if (this.leftChild() != null) {
              leftSize = this.leftChild().size();
          }
          if (this.rightChild() != null) {
              rightSize = this.rightChild().size();
          }
          this.size = leftSize + rightSize +1;
          return this.size;
        }
        
//        private int updateSize() {
//            int leftSize = 0;
//            int rightSize = 0;
//            if (this.leftChild() != null) {
//                leftSize = this.leftChild().updateSize();
//            }
//            if (this.rightChild() != null) {
//                rightSize = this.rightChild().updateSize();
//            }
//            this.size = leftSize + rightSize +1;
//            return this.size;
//        }
        
        public int level() {
            return this.level;
        }

        public Node leftChild() {
            return this.left;
        }
        
        private Node leftChild(Node n) {
            this.left = n;
            return this.left;
        }
        
        public Node rightChild() {
            return right;
        }
        
        private Node rightChild(Node n) {
            this.right = n;
            return this.right;
        }
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        
    }
}
