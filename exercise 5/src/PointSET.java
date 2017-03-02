import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import java.util.Iterator;
import java.util.TreeSet;


public class PointSET {

    private TreeSet<Point2D> pointArr;
    
    public PointSET() 
    {
        //construct an empty set of points
        pointArr =  new TreeSet<Point2D>();
    }
    
    public boolean isEmpty()
    {
        return pointArr.size() == 0;
    }
    
    public int size()
    {
        return pointArr.size();
    }
    
    public void insert(Point2D p)
    {
        assert (p != null) : new java.lang.NullPointerException();
        pointArr.add(p);
    }
    
    public boolean contains(Point2D p)
    {
        assert (p != null) : new java.lang.NullPointerException();
        return pointArr.contains(p);
    }
    
    public void draw()
    {    
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLUE);
        
        for (Point2D point : pointArr)
        {
            StdDraw.point(point.x(), point.y());
            //System.out.println("drawing point"+point);
        }
        //StdDraw.show();
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {
        assert (rect != null) : new java.lang.NullPointerException();
        rangePointIterable rangePointset = new rangePointIterable(rect);
        return rangePointset;
    }
    
    public Point2D nearest(Point2D p)
    {
        assert (p != null) : new java.lang.NullPointerException();
        if (pointArr.size() == 0)
            return null;
        
        //return the point with min euclian distance
        Point2D nearestPoint = pointArr.first();
        double nearestDist = p.distanceTo(nearestPoint);
        double tempDist = 1.0;
        
        for (Point2D each : pointArr)
        {    
            tempDist = p.distanceTo(each);
            if (tempDist < nearestDist)
            {
                nearestDist = tempDist;
                nearestPoint =  each;
            }
        }
        
        return nearestPoint;
    }
    
    private class rangePointIterable implements Iterable<Point2D>{
        
        private TreeSet<Point2D> treeForRange;
        
        public rangePointIterable(RectHV rect)
        {
            treeForRange =  new TreeSet<Point2D>();
            
            for (Point2D each : pointArr)
                if (rect.contains(each))
                    treeForRange.add(each);
        }
        
        public Iterator<Point2D> iterator() {
            return treeForRange.iterator();
        }
    }

    public static void main(String args[])
    {
        // test for establishing
        PointSET testSet = new PointSET();
        
        // test for isEmpty 1
        System.out.println("should be empty while the return for isEmpty is ");
        System.out.println(testSet.isEmpty());
        
        // test for size 1
        System.out.println("should be empty while the return for size is ");
        System.out.println(testSet.size());
        
        // test for insert
        System.out.println("Insert these points: {(0,0),(0.2,0.5),(0.1,0.4),(0.4,0.3),(0.6,0.5),(0.8,0.6)}");
        testSet.insert(new Point2D(0, 0));
        testSet.insert(new Point2D(0.2, 0.5));
        testSet.insert(new Point2D(0.1, 0.4));
        testSet.insert(new Point2D(0.4, 0.3));
        testSet.insert(new Point2D(0.6, 0.5));
        testSet.insert(new Point2D(0.8, 0.6));
        
        // test for isEmpty 2
        System.out.println("should not be empty while the return for isEmpty is ");
        System.out.println(testSet.isEmpty());
        
        // test for size 2
        System.out.println("should have a size of 6 while the return for isEmpty is ");
        System.out.println(testSet.size());
        
        // test for contain
        System.out.println("test for contain using point (0.2, 0.5)");
        System.out.println(testSet.contains(new Point2D(0.2, 0.5)));
        System.out.println("test for contain using point (0.4, 0.3)");
        System.out.println(testSet.contains(new Point2D(0.4, 0.3)));
        System.out.println("test for contain using point (0.4, 0.4),which isn't contained");
        System.out.println(testSet.contains(new Point2D(0.4, 0.4)));
        
        
        // test for draw
        testSet.draw();
        
        // test for range
        System.out.println("the following poins are contained in the rect (0, 0, 0.5, 0.5)");
        RectHV rect = new RectHV(0, 0, 0.5, 0.5); 
        for (Point2D each : testSet.range(rect))
            System.out.println(each);
        
        //test for nearest
        System.out.println("finding the nearest point for (0.1, 0.1):");
        System.out.println(testSet.nearest(new Point2D(0.1, 0.1)));
        System.out.println("finding the nearest point for (0.5, 0.5):");
        System.out.println(testSet.nearest(new Point2D(0.5, 0.5)));
    }
}
