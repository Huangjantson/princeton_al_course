import java.util.Arrays; 
//import java.util.Scanner; 
//import edu.princeton.cs.algs4.StdDraw; 

public class FastCollinearPoints {
    private Point[] points; 
    
    public FastCollinearPoints(Point[] points) {
        //Construnct the structure
        this.points = points; 
        
        //check if there is any null point
        if (this.points == null)
            throw new NullPointerException("The inputting Point array is null."); 
        for (Point A : this.points)
            if (A == null) throw new NullPointerException(); 
        
        //check if there is any duplicate points
        Arrays.sort(this.points); 
        
        for (int i = 1;  i  <  this.points.length;  i++)
            if (this.points[i] == this.points[i-1]) 
               throw new IllegalArgumentException(); 
    }

    public int numberOfSegments()
    {
        // the number of line segments
        LineSegment[] temp = segments(); 
        return temp.length; 
    }
    
    private LineSegment[] resizeSegemntArray(LineSegment[] segmentArray, int size)
    {
        LineSegment[] temp; 
        int originalSize = segmentArray.length; 
        //copy new array
        temp = new LineSegment[size]; 
        if (originalSize  <  size)
            for (int i = 0; i < originalSize; i++) 
                temp[i] = segmentArray[i]; 
        else 
            for (int i = 0; i < size; i++) 
                temp[i] = segmentArray[i]; 
        return temp; 
    }
    
    public LineSegment[] segments()
    {
        LineSegment[] segmentArray = new LineSegment[1]; 
        int total = points.length;         
        Point[] pointsAxi = new Point[total]; 
        Point pointer; 
        
        //at least four point in a segment so no need to iterate
        if (total < 4) 
            return null; 
        
        int segmentNumber = 0; //record how many segments
        int continuous; //record continuous points with the same slope
        //record if the contiunous point on the slope is smaller than poins[i]
        boolean metSmaller; 
        
        //iterator over all the points
        //at least four point in a segment so no need to iterate over all
        for (int i = 0; i < total-3; i++)
        {    
            pointer = points[i]; 
            for (int j = 0; j < total; j++)
                //points is already sorted during initialization
                pointsAxi[j] = points[j]; 
            
            //sorting by slope
            Arrays.sort(pointsAxi, pointer.slopeOrder()); 
            
            continuous = 0; 
            metSmaller = false; 
            
            for (int j = 0; j < total-1; j++)
            {
                if (pointer.slopeTo(pointsAxi[j]) == pointer.slopeTo(pointsAxi[j+1]))
                    {
                     continuous++; 
                     if (pointsAxi[j].compareTo(pointer) < 0) metSmaller = true; 
                    }
                else
                {
                    if (continuous > 0)
                    {
                        if ((continuous > 1) && (!metSmaller))
                        {
                             //in which case we have at least 4 points at a line,
                             //ended by the jth point
                        	//extend the segmentArray
                            if (segmentNumber >= segmentArray.length)
                                segmentArray = resizeSegemntArray(segmentArray, segmentNumber*2); 
                                segmentArray[segmentNumber] = new LineSegment(pointer, pointsAxi[j]); 
                                segmentNumber++; 
                        }
                        continuous = 0; 
                        metSmaller = false; 
                    }
                }
            }
            if ((continuous > 1) && (!metSmaller))
            {//in which case we have at least 4 points at a line,ended by the jth point
                 if (segmentNumber >= segmentArray.length)//extend the segmentArray
                      segmentArray = resizeSegemntArray(segmentArray, segmentNumber*2); 
                      segmentArray[segmentNumber] = new LineSegment(pointer, pointsAxi[total-1]); 
                      segmentNumber++; 
            }
        }
        
        segmentArray = resizeSegemntArray(segmentArray, segmentNumber); 
        return segmentArray; 
    }
/*
    public static void main(String[] args) {

        // read the N points from a file
        Scanner in = new Scanner(System.in); 
        int N = in.nextInt(); 
        Point[] points = new Point[N]; 
        for (int i = 0;  i  <  N;  i++) {
            int x = in.nextInt(); 
            int y = in.nextInt(); 
            points[i] = new Point(x, y); 
        }
        in.close(); 
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
            System.out.println(segment); 
            segment.draw(); 
        }
    }
    */
}


