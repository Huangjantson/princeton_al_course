
import java.util.Arrays; 

public class BruteCollinearPoints {

    private LineSegment[] collinearSegments; 
    private Point[] points; 
    private int segmentsTotal; 
    
    public BruteCollinearPoints(Point[] points) {
         // finds all line segments containing 4 points
         //using brute force = write multiple layers of cycles
        
    	this.points = points;
    	
        //check if there is any null point
        if (this.points == null)
            throw new NullPointerException("The inputting Point array is null."); 
        for (Point A : this.points)
            if (A == null) throw new NullPointerException(); 
        
        //check if there is any duplicate points
        Arrays.sort(this.points); 
        
        for (int i = 1; i < this.points.length; i++)
            if (this.points[i] == this.points[i-1]) throw new IllegalArgumentException("Duplicate points!"); 
        
        //at least four point in a segment so no need to iterate
        if (this.points.length  <  4) 
            collinearSegments  =  null; 
        
        //initializing for the following step
        LineSegment[] temp  =  new LineSegment[8]; 
        this.segmentsTotal  =  0; 
        
        //generate the segments
        for (int i = 0; i  <  this.points.length; i++)
            for (int j = i+1; j  <  this.points.length; j++)
                for (int k = j+1; k  <  this.points.length; k++)
                    for (int l = k+1; l  <  this.points.length; l++)
                    {
                        if ((this.points[i].slopeTo(this.points[j])  ==  this.points[i].slopeTo(this.points[k]))
                            &&(this.points[i].slopeTo(this.points[j])  ==  this.points[i].slopeTo(this.points[l])))
                            {
                            //extend
                            if (this.segmentsTotal  ==  temp.length)
                                temp = resizeSegemntArray(temp, this.segmentsTotal*2); 
                            this.segmentsTotal++; 
                            temp[segmentsTotal-1] = new LineSegment(this.points[i], this.points[l]); 
                            }
                    }
        
        //format the output
        collinearSegments  =   new LineSegment[segmentsTotal]; 
        collinearSegments  =  resizeSegemntArray(temp, segmentsTotal); 
    }
    
    private LineSegment[] resizeSegemntArray(LineSegment[] segmentArray, int size)
    {
        LineSegment[] temp; 
        int originalSize = segmentArray.length; 
        //copy new array
        temp = new LineSegment[size]; 
        if (originalSize < size)
            for (int i = 0; i < originalSize; i++) 
                temp[i] = segmentArray[i]; 
        else 
            for (int i = 0; i < size; i++) 
                temp[i] = segmentArray[i]; 
        return temp; 
    }
    
    public int numberOfSegments()
    {
        return segmentsTotal; 
    }
    
    public LineSegment[] segments()
    {
        return collinearSegments; 
    }

}
