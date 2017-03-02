import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    
    //private Picture picture;
    private int[][] red;
    private int[][] blue;
    private int[][] green;
    private double[][] energyArray;
    private boolean changed;
    private int width;
    private int height;
    
   //  create a seam carver object based on the given picture
   public SeamCarver(Picture picture)
   {                
       // deep copy
       //this.picture =  new Picture(picture);
       changed = false;
       width = picture.width();
       height = picture.height();
       
       red = new int[width][height];
       blue = new int[width][height];
       green = new int[width][height];
       energyArray = new double[width][height];
       
       for (int i = 0; i < width; i++)
           for (int j = 0; j < height; j++)
           {
               red[i][j] = picture.get(i, j).getRed();
               blue[i][j] = picture.get(i, j).getBlue();
               green[i][j] = picture.get(i, j).getGreen();
           }
       
       for (int i = 0; i < width; i++)
           for (int j = 0; j < height; j++)
               energyArray[i][j] = energy(i, j);
       
   }
         
   //  current picture
   public Picture picture()                          
   {
       //if (changed)
       //{
	   Picture picture = new Picture(width, height);
           for (int i = 0; i < width; i++)
               for (int j = 0; j < height; j++)
               {
                   picture.set(i, j, new Color(red[i][j], green[i][j], blue[i][j]));
               }  
           changed = false;
       //}
       return new Picture(picture);
   }
   
    //  width of current picture
   public     int width()                            
   {
       return width;
   }
   
   //  height of current picture
   public     int height()                           
   {
       return height;
   }
   
   //  energy of pixel at column x and row y
   public  double energy(int x, int y)               
   {
       // position check
       if ((x < 0) || (y < 0) || (x > width() - 1) || (y > height() - 1))
           throw new IndexOutOfBoundsException("x = "+x+", y= "+y+" with "+width+"x"+height);
       
       // return 1000.0 if on the border
       if ((x == 0) || (y == 0) || (x == width() - 1) || (y == height() - 1))
           return 1000.0;
       
       // compute the difference on R/G/B in X/Y direction
       double Rx = red[x+1][y] - red[x-1][y];
       double Gx = green[x+1][y] - green[x-1][y];
       double Bx = blue[x+1][y] - blue[x-1][y];

       double Ry = red[x][y+1] - red[x][y-1];
       double Gy = green[x][y+1] - green[x][y-1];
       double By = blue[x][y+1] - blue[x][y-1];
       
       // compute deltaXSquare by Rx Gx Bx, compute deltaYSquare by Ry Gy By
       double deltaXSquare = Math.pow(Rx, 2) + Math.pow(Gx, 2) + Math.pow(Bx, 2);
       double deltaYSquare = Math.pow(Ry, 2) + Math.pow(Gy, 2) + Math.pow(By, 2);
       
       // return the energy 
       return Math.sqrt(deltaXSquare + deltaYSquare);
   }
   
   //  sequence of indices for horizontal seam
   public   int[] findHorizontalSeam()
   {
       // corner case: height() < 3, return with all 0
       if (height() < 3)
           return new int[width()];
       
       // corner case: width() < 3, return with all 0
       if (width() < 3)
           return new int[width()];
       
       // horizontalSeam, returning int[] for each column and representing the row
       double[][] distance = new double[width()][height()]; 
       for (int i = 1; i < width(); i++)
           for (int j = 0; j < height(); j++)
               distance[i][j] = Double.MAX_VALUE;
       
       // store the source from the left column 
       int[][] source = new int[width()][height()];
       
       // relax the points in the next column 
       for (int i = 0; i < width()-1; i++)
           for (int j = 0; j < height(); j++)
           {
               int start = j - 1;
               int end  = j + 1;
               
               // corner case
               if (j == 0)
                   start = 0;
               if (j == height() - 1)
                   end = height() - 1;
               
               // relax all the pointed pixels
               for (int processing = start; processing < end + 1; processing++)
                   if (distance[i+1][processing] 
                               > energyArray[i+1][processing] + distance[i][j])
                   {
                       distance[i+1][processing] 
                               = energyArray[i+1][processing] + distance[i][j];
                       source[i+1][processing] = j;
                   }
           }
       
       // find the end point 
       double min = Double.MAX_VALUE;
       int endPoint = 0;
       for (int j = 0; j < height(); j++)
           if (distance[width() - 1][j] < min)
           {
               min = distance[width() - 1][j];
               endPoint = j;
           }
       
       int[] result = new int[width()];
       int loc = endPoint;
       for (int i = width() - 1; i >= 0; i--)
       {
           result[i] = loc;
           loc = source[i][loc];
       }
       return result;
   }
   
   //  sequence of indices for vertical seam
   public   int[] findVerticalSeam()                 
   {
       // corner case: height() < 3, return with all 0
       if (height() < 3)
           return new int[height()];
       
       // corner case: width() < 3, return with all 0
       if (width() < 3)
           return new int[height()];
       
       // vertiacal Seam, the distance of the first row is set to 0 
       double[][] distance = new double[width()][height()]; 
       for (int j = 1; j < height(); j++)
           for (int i = 0; i < width(); i++)
               distance[i][j] = Double.MAX_VALUE;
       
       // store the source from the upper row 
       int[][] source = new int[width()][height()];
       
       // relax the points in the next row 
       for (int j = 0; j < height()-1; j++)
           for (int i = 0; i < width(); i++)
           {
               int start = i - 1;
               int end  = i + 1;
               
               // corner case
               if (i == 0)
                   start = 0;
               if (i == width() - 1)
                   end = width() - 1;
               
               // relax all the pointed pixels
               for (int processing = start; processing < end + 1; processing++)
                   if (distance[processing][j+1] 
                       > energyArray[processing][j+1]+distance[i][j])
                   {
                       distance[processing][j+1] 
                       = energyArray[processing][j+1]+distance[i][j];
                       source[processing][j+1] = i;
                   }
           }
       
       // find the end point 
       double min = Double.MAX_VALUE;
       int endPoint = 0;
       for (int i = 0; i < width(); i++)
           if (distance[i][height() - 1] < min)
           {
               min = distance[i][height() - 1];
               endPoint = i;
           }
       
       int[] result = new int[height()];
       int loc = endPoint;
       for (int j = height() - 1; j >= 0; j--)
       {
           result[j] = loc;
           loc = source[loc][j];
       }
       return result;
   }
   
   // check corner cases
   private void removeCheckHorizontal(int[] seam)
   {
       // null check
       if (seam == null)
           throw new java.lang.NullPointerException();
       
       // check length
       if (seam.length != width())
           throw new java.lang.IllegalArgumentException("Not corresponding length");
       
       // check picture
       if (height() <= 1)
           throw new java.lang.IllegalArgumentException("Picture too small");
       
       // check the elements
       for (int i : seam)
           if ((i > height()-1) || (i < 0)) 
               throw new IllegalArgumentException("Illegal input in the seam: "+ i);
       
       // check the elements' difference
       for (int i = 0; i < seam.length - 1; i++)
           if (Math.abs(seam[i] - seam[i+1]) > 1) 
               throw new IllegalArgumentException("Seam element difference is not one at "+i+" and its next element");
   }
   
   //  remove horizontal seam from current picture
   public    void removeHorizontalSeam(int[] seam)   
   {
       // check corner cases
       removeCheckHorizontal(seam);
       
       // remove the seam from red[][], blue[][], green and height--
       for (int i = 0; i < width; i++)
       {
           for (int j = seam[i]; j < height-1; j++)
               {
                  red[i][j] = red[i][j+1];
                  blue[i][j] = blue[i][j+1];
                  green[i][j] = green[i][j+1];
                  energyArray[i][j] = energyArray[i][j+1];
               }
       }
       height--;
       
       int j = 0;
       // energy plugs the hole in previous section
       // here recompute alone the seam for j and j+1
       for (int i = 0; i < width; i++)
       {
           j = seam[i];
           if (j < height)
                energyArray[i][j] = energy(i, j);
           if (j > 0)
                energyArray[i][j-1] = energy(i, j-1);
       }
      
       changed = true;
   }
   
   // check corner cases
   private void removeCheckVertical(int[] seam)
   {
       // null check
       if (seam == null)
           throw new java.lang.NullPointerException();
       
       // check length
       if (seam.length != height())
           throw new java.lang.IllegalArgumentException("Not corresponding length");
       
       // check picture
       if (width() <= 1)
           throw new java.lang.IllegalArgumentException("Picture too small");
       
       // check the elements
       for (int i : seam)
           if ((i > width()-1) || (i < 0)) 
               throw new IllegalArgumentException("Illegal input in the seam: "+ i);
       
       // check the elements' difference
       for (int i = 0; i < seam.length - 1; i++)
           if (Math.abs(seam[i] - seam[i+1]) > 1) 
               throw new IllegalArgumentException("Seam element difference is not one at "+i+"and its next element");
   }
   
   //  remove vertical seam from current picture
   public    void removeVerticalSeam(int[] seam)
   {
       // check corner cases
       removeCheckVertical(seam);
       
       // remove the seam from red[][], blue[][], green and width--
       for (int j = 0; j < height; j++)
       {
           for (int i = seam[j]; i < width-1; i++)
               {
                  red[i][j] = red[i+1][j];
                  blue[i][j] = blue[i+1][j];
                  green[i][j] = green[i+1][j];
                  energyArray[i][j] = energyArray[i+1][j];
               }
       }
       width--;
       
       int i = 0;
       // energy plugs the hole in previous section
       // here recompute alone the seam for i and i+1
       for (int j = 0; j < height; j++)
       {
           i = seam[j];
           if (i < width)
               energyArray[i][j] = energy(i, j);
           if (i > 0)
               energyArray[i-1][j] = energy(i-1, j);
       }
      
       changed = true;
       
   }
   
   public static void main(String[] args)
   {
       String testDir = 
       "D:\\self\\algorithms\\assignment specification\\seamCarving-testing\\seamCarving";
       
       String testPic = "12x10.png";
       // test loading the pic
       String testLoc = testDir + "\\" + testPic;
       
       Picture picture = new Picture(testLoc);
       
       SeamCarver tester = new SeamCarver(picture);
       
       // test height(), weight()
       System.out.println(tester.width());
       System.out.println(tester.height());
       // tester.picture().show();
       System.out.println("The testing output for horizontal seam:");
       for (int i : tester.findHorizontalSeam())
           System.out.print(i+" ");
       System.out.println();
       System.out.println("The testing output for vertical seam:");
       for (int i : tester.findVerticalSeam())
           System.out.print(i+" ");
       System.out.println();
       
       tester.picture().save("D:\\self\\algorithms\\assignment specification\\seamCarving-testing\\seamCarving\\tester.png");
       
       // tester.removeHorizontalSeam(tester.findHorizontalSeam());
       tester.removeVerticalSeam(tester.findVerticalSeam());
       
       tester.picture().save("D:\\self\\algorithms\\assignment specification\\seamCarving-testing\\seamCarving\\tester-1.png");
   
       System.out.println("The testing output for vertical seam after remove:");
       for (int i : tester.findVerticalSeam())
           System.out.print(i+" ");
       System.out.println();
       
       Picture picture2 = new Picture("D:\\self\\algorithms\\assignment specification\\seamCarving-testing\\seamCarving\\tester-1.png");
       SeamCarver tester2 = new SeamCarver(picture2);
       System.out.println("The testing output for vertical seam using the samp pic as removed:");
       for (int i : tester2.findVerticalSeam())
           System.out.print(i+" ");
       
   
   }
   
}