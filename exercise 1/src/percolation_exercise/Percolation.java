/******************************************************************************
 *  Author: Huang Jantson
 *  Execution:  java Percolation 
 *  Dependencies: StdIn.java StdOut.java edu.princeton.cs.algs4.WeightedQuickUnionUF
 *
 *  Percolation
 *
 ******************************************************************************/

package percolation_exercise;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] openFlag;
    private int sideLength; 
    private WeightedQuickUnionUF unionArray,fullArray;
    //private int indexOfTop;
    
    public Percolation(int N) {
        //initializing 
    	if (N<1) throw new IllegalArgumentException();
    	
        openFlag = new boolean[N*N+2];
        //with 0 as the head and N as the tail 
        unionArray = new WeightedQuickUnionUF(N*N+2);
        fullArray = new WeightedQuickUnionUF(N*N+1);
        sideLength = N;
        
        //defaultly set then all the blocks as closed and not full
        for (int i = 1; i <= N*N; i++) openFlag[i] = false;
        
        //set the top and the bottom as open
        openFlag[0] = true;
        openFlag[N*N+1] = true;    
    }
    
    
    // validate that p,q is a valid index
    private void validate(int p, int q) {
        if ((p > sideLength) || (q > sideLength)||(p < 1) || (q < 1)) {
            throw new IndexOutOfBoundsException();  
        }
    }
    
    public boolean isOpen(int i, int j) {
        
        validate(i, j);
        
        //mind the location transferring
        int position = sideLength*(i-1)+j;
        return openFlag[position];
    }
    
    public boolean isFull(int i, int j) {
        
        validate(i, j);
        
        //mind the location transferring
        int position = sideLength*(i-1)+j;
        return fullArray.connected(0, position);
    }
    
    private boolean connected(int i, int j, int x, int y){
        validate(i, j);
        validate(x, y);
        int positionA = sideLength*(i-1)+j;
        int positionB = sideLength*(x-1)+y;
        return unionArray.connected(positionA, positionB);
    }
    
    public void open(int i, int j){
        
        validate(i, j);
        
        int position = sideLength*(i-1)+j;
        //using dynamic change:openFlag and fullFlag will change right after open
        openFlag[position] = true;
        
        //change unionArray to get new state of the unions
        if (i == 1) {
            //connect the top blocks to the virtual top
            unionArray.union(position, 0);
        	fullArray.union(position, 0);}
        else 
            //find if the upper block is open=>then connect
            if (isOpen(i-1, j))
            {
                unionArray.union(position, sideLength*(i-2)+j);
        	    fullArray.union(position, sideLength*(i-2)+j);
            }
        
        if (i == sideLength)
        { //connect the bottom blocks to the virtual top
            unionArray.union(position, sideLength*sideLength+1);
        }
        else
            if (isOpen(i+1, j))
            {//find if the lower block is open=>then connect
                unionArray.union(position, sideLength*i+j);
                fullArray.union(position, sideLength*i+j);
            }   
        
        //find if the left block is open=>then connect
        if (j>1)
            if (isOpen(i, j-1)){
            unionArray.union(position, sideLength*(i-1)+j-1);
            fullArray.union(position, sideLength*(i-1)+j-1);
            }
        
        //find if the right block is open=>then connect
        if (j < sideLength)
            if (isOpen(i, j+1)){
            unionArray.union(position, sideLength*(i-1)+j+1);
            fullArray.union(position, sideLength*(i-1)+j+1);
            }
    }
    
    public boolean percolates() {
        return unionArray.connected(0, sideLength*sideLength+1);
    }    
    
    public static void main(String[] args) {
        // test and see the result
        Percolation testPercolation = new Percolation(4);
        testPercolation.open(1, 2);
        System.out.println("open state of (1,1) is "+testPercolation.isOpen(1,1));
        System.out.println("open state of (1,2) is "+testPercolation.isOpen(1,2));
        testPercolation.open(2, 3);
        System.out.println("open state of (2,3) is "+testPercolation.isOpen(2,3));
        System.out.println("full state of (2,3) is "+testPercolation.isFull(2,3));
        testPercolation.open(3, 3);
        testPercolation.open(4, 3);
        testPercolation.open(4, 2);
        System.out.println("full state of (4,2) is "+testPercolation.isFull(4,2));
        System.out.println("the percolates state  is "+testPercolation.percolates());
        testPercolation.open(2, 2);
        System.out.println(testPercolation.connected(2,3,3,3));
        System.out.println("full state of (3,3) is "+testPercolation.isFull(3,3));
        System.out.println("the percolates state  is "+testPercolation.percolates());
        
    }
}
