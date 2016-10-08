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
		for (Point2D point : pointArr)
			StdDraw.point(point.x(), point.y());
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
		
		public rangePointIterable(RectHV rect){
			for ( Point2D each : pointArr )
				if (rect.contains(each))
					treeForRange.add(each);
		}
		
		public Iterator<Point2D> iterator() {
			return treeForRange.iterator();
		}
	}

}
