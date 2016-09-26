/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

//import java.util.Arrays;
import java.util.Comparator;
//import java.util.Scanner;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable <Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the  < em>x < /em>-coordinate of the point
     * @param  y the  < em>y < /em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
    	if ((that == null) || (this == null))
    		throw new NullPointerException();
    	
        if (this.x == that.x)
        {
            if (this.y == that.y)
                //the same point
                return Double.NEGATIVE_INFINITY;
            else 
                //vertical
                return Double.POSITIVE_INFINITY;
        }
        else 
        	if (this.y == that.y) return +0.0;
        else 
        return (that.y-this.y)*1.0/(that.x-this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0  <  y1 or if y0 = y1 and x0  <  x1.
     *
     * @param  that the other point
     * @return the value  < tt>0 < /tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y)
            return -1;
        else{
            if (this.y == that.y)
            {
                if (this.x < that.x)
                    return -1;
                if (this.x == that.x)
                    return 0;
                else
                    return 1;
                }
            else
                return 1;
        }
    }
    
    private class slopeComparator implements Comparator<Point>
    {
        @Override
        public int compare(Point that1, Point that2) {
            //Comparing
            int result;
            double slope1, slope2;
            
            
            slope1 = slopeTo(that1);
            slope2 = slopeTo(that2);
            /*
            if ((slope1-slope2) > 0)
                result = 1;
            else
                if ((slope1-slope2) == 0)
                    result = 0;
                else
                    result = -1;
            */
            if (slope1 == slope2)
            	result = 0;
            else 
                if (slope1-slope2 > 0)
            	    result = 1;
                else 
                   //(slope1-slope2 < 0)
                   result = -1;
            return result;
        }
            
    }
  
    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator <Point> slopeOrder() 
    {
        /* YOUR CODE HERE */
        slopeComparator slCp = new slopeComparator();
        return slCp;
    }
    
    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
   
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        /*Point A,B,C,E;
        A= new Point(10000,0);
        B= new Point(3000,7000);
        C= new Point(3000,4000);
        E= new Point(7000,3000);
        //test compareTo
        System.out.println("Testting "+A+" compareTo "+B+","+A.compareTo(B));
        System.out.println("Testting "+A+" compareTo "+E+","+A.compareTo(E));
        System.out.println("Testting "+A+" compareTo "+A+","+A.compareTo(A));
        //test slopeTo
        System.out.println("Testting sloptTo "+A+" with "+E+","+A.slopeTo(B));
        System.out.println("Testting sloptTo "+A+" with "+B+","+A.slopeTo(C));
        System.out.println("Testting sloptTo "+A+" with "+E+","+A.slopeTo(E));
        //test slopeOrder
        System.out.println("Testting slopeOrder of "+A+" with "+B+" and "+C+","+A.slopeOrder().compare(C,E));
        */
        /*
    	Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Point[] points = new Point[N];
        for (int i = 0; i  <  N; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            points[i] = new Point(x, y);
        }
        in.close();
        Point pointer=points[0];
        
        int total=points.length;        
        Point[] pointsAxi=new Point[total];
        
        Arrays.sort(points);
        for (int j=1;j < total-1;j++) {
            System.out.println(points[j]);
            System.out.println("own slop is "+pointer.slopeTo(points[j])+" next slope is "+pointer.slopeTo(points[j+1]));
            System.out.println("compare result: "+pointer.slopeOrder().compare(points[j], points[j+1]));
        }
        
        
        /*for (int j=1;j < total;j++)
            //points is already sorted during initialization
            pointsAxi[j]=points[j];
        
        Arrays.sort(pointsAxi,1,total,points[0].slopeOrder());
        for (Point AC : pointsAxi) {
            System.out.println(AC);
            if (AC!=null) System.out.println(pointer.slopeTo(AC));
        }
        System.out.println();
        Arrays.sort(pointsAxi,1,total,points[0].slopeOrder());
        for (Point AC : pointsAxi) {
            System.out.println(AC);
            if (AC!=null) System.out.println(pointer.slopeTo(AC));
        }*/
    }
}