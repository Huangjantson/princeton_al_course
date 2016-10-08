//import java.io.IOException; 
//import java.nio.file.Paths;
//import java.util.Scanner; 
import java.util.Iterator; 


public class Board {
    private int[][] blocks; 
    private int[][] goal; 
    private int size; 

    public Board(int[][] blocks) {
        //get the size
        this.size = blocks.length; 
        this.blocks = new int[size][size]; 
        //if size is too small( < 2),raise an error 
        
        //deep copy the inputing blocks to the own private variation
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.blocks[i][j] = blocks[i][j];     
        
        //construct the goal board
        this.goal = new int[size][size]; 
        
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if ((i != size-1) || (j != size-1))
                        goal[i][j] = (i)*size+j+1; 
                else
                    goal[i][j] = 0; 
    }
    
    public int dimension(){
        return this.size; 
    }
    
    public int hamming(){
        //return the hamming distance by checking the board and the goal
        int result = 0; 
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if ((blocks[i][j] != goal[i][j]) && (blocks[i][j] != 0))
                    result++; 
        return result; 
    }
    
    public int manhattan()
    {
        //return the manhattan distance by computing each elements's distance vesus its goal location
        int result = 0; 
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if ((blocks[i][j] != goal[i][j]) && (blocks[i][j] != 0))
                {    
                    if (blocks[i][j] != 0)
                    {
                        //compute the goal horizontal location of the element
                        int x = (blocks[i][j]-1)/size; 
                        //get the goal vertical location of the element
                        int y = (blocks[i][j]-1) % size; 
                        //add the distance to size
                        result = result+Math.abs(x-i)+Math.abs(y-j); 
                    }
                    else
                        result = result+Math.abs(size-i-1)+Math.abs(size-j-1);     
                }
        return result; 
    }
    
    public boolean isGoal()
    {
        //use the hamming distance to decide if it is goal
        return (this.hamming() == 0); 
    }
    
    public Board twin()
    {
        // a board that is obtained by exchanging any pair of blocks
        //clone the block 
        int[][] twinBlock = new int[size][size]; 
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                twinBlock[i][j] = blocks[i][j]; 
        
        //only exchanging the first two elements,as any switch could lead to a twin board
        twinBlock[0][0] = blocks[0][1]; 
        twinBlock[0][1] = blocks[0][0]; 
        Board twinBoard = new Board(twinBlock); 
        return twinBoard; 
    }                    
    
    public boolean equals(Object y)
    {
    //check the type of objcet, if it is a Board
    if (y.getClass() != this.getClass())
        return false; 
    
    //check if their size equals,the check before has guaranteed that 
    //it is a Board entity
    //here we need to cast y to Board
    if (((Board) y).dimension() != this.dimension())
    {
        return false; 
    }    
    
    //check if each element equals
    int [][] yBoard = ((Board) y).blocks; 
    
    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
        {
            if (yBoard[i][j] != this.blocks[i][j])
                    return false; 
        }
    
    return true; 
    }
    
    /*Here begins the part about iterating the neighbor boards
     * */
    //the definition of the private iterable class
    
    //the method returns the iterable
    public Iterable<Board> neighbors()
    {
        return new neighborIterable(); 
    }
    
    private class neighborIterable implements Iterable<Board> 
    {

        @Override
        public Iterator<Board> iterator() {
            return new neighborIterator(); 
        }; 
        
        //HERE IS THE DEFINITION FOR THE "neighborIterator"
        private class neighborIterator implements Iterator<Board>
        {
            private Board[] neighborBoards; 
            private int neighborBoardNum; 
            private int neighborIteratorLoc; 
            
            public neighborIterator()
            {
                //generate the neighborBoards
                neighborBoards = new Board[4]; 
                neighborBoardNum = 4; 
                neighborIteratorLoc = 0; 
                int[][] tempBlocks = new int[size][size]; 
                
                int i, j = 0; 
                stop:
                for (i = 0; i < size; i++)
                    for (j = 0; j < size; j++)
                        if (blocks[i][j] == 0)
                            break stop; 
                if (i == 0)
                    neighborBoardNum = neighborBoardNum-1; 
                else
                {    
                    //deep copy tempblock from blocks
                    for (int x = 0; x < size; x++)
                        for (int y = 0; y < size; y++)
                            tempBlocks[x][y] = blocks[x][y];     
                    
                    //exchange i with the left,add the board into series
                    tempBlocks[i][j] = blocks[i-1][j]; 
                    tempBlocks[i-1][j] = blocks[i][j]; 
                    neighborBoards[neighborIteratorLoc] = new Board(tempBlocks); 
                    neighborIteratorLoc++; 
                }
                
                if (j == 0)
                    neighborBoardNum = neighborBoardNum-1; 
                else
                {    //deep copy tempblock from blocks
                    for (int x = 0; x < size; x++)
                        for (int y = 0; y < size; y++)
                            tempBlocks[x][y] = blocks[x][y];     
                    
                    //exchange i with the upper,add the board into series
                    tempBlocks[i][j-1] = blocks[i][j]; 
                    tempBlocks[i][j] = blocks[i][j-1]; 
                    neighborBoards[neighborIteratorLoc] = new Board(tempBlocks); 
                    neighborIteratorLoc++; 
                }
                
                if (j == size-1)
                    neighborBoardNum = neighborBoardNum-1; 
                else
                { //deep copy tempblock from blocks
                    for (int x = 0; x < size; x++)
                        for (int y = 0; y < size; y++)
                            tempBlocks[x][y] = blocks[x][y];     
                    //exchange i with the lower,add the board into series
                    tempBlocks[i][j+1] = blocks[i][j]; 
                    tempBlocks[i][j] = blocks[i][j+1]; 
                    neighborBoards[neighborIteratorLoc] = new Board(tempBlocks); 
                    neighborIteratorLoc++; 
                }
                    
                if (i == size-1)
                    neighborBoardNum = neighborBoardNum-1; 
                else
                { //deep copy tempblock from blocks
                    for (int x = 0; x < size; x++)
                        for (int y = 0; y < size; y++)
                            tempBlocks[x][y] = blocks[x][y];     
                    //exchange i with the right,add the board into series
                    tempBlocks[i+1][j] = blocks[i][j]; 
                    tempBlocks[i][j] = blocks[i+1][j]; 
                    neighborBoards[neighborIteratorLoc] = new Board(tempBlocks); 
                    neighborIteratorLoc++; 
                }
                
                neighborIteratorLoc = 0; 
    
            }
            
            @Override
            public boolean hasNext() {    
                return neighborIteratorLoc < neighborBoardNum; 
            }

            @Override
            public Board next() {
                neighborIteratorLoc++; 
                return neighborBoards[neighborIteratorLoc-1]; 
            }
            
        }
    }
    
    public String toString() {
        String result = ""; 
        result=result+size+"\n";
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                result = result+blocks[i][j]+"  "; 
            result = result+"\n"; 
        }
        return result; 
    }
/*   
    public static void main(String[] args) throws IOException {
        //the main function for testing this class    
        //create the testing blocks by file
        Scanner in  =  new Scanner(Paths.get(args[0]));  
        //test the establish of the goal-board
        int N = in.nextInt(); 
        int[][] blocks = new int[N][N]; 
        for (int i  =  0;  i  <  N;  i++)
            for (int j  =  0;  j  <  N;  j++)
                blocks[i][j]  =  in.nextInt(); 
        
        in.close(); 
        
        Board initial = new Board(blocks); 
        Board second = new Board(blocks); 
        //test toString and see the output
        System.out.println(initial.toString()); 
        System.out.println((new Board(initial.goal)).toString()); 
        
        
        //test the hamming distance
        System.out.println(initial.hamming()); 
        
        //test the manhattam distance
        System.out.println(initial.manhattan()); 
        
        //test the isGoal method
        System.out.println(initial.isGoal()); 
        
        //test the twin
        System.out.println(initial.twin().toString()); 
        //test the equals
        System.out.println(initial.equals(second)); 
        //neighbors
        for (Board neighbor : initial.neighbors())
            System.out.println(neighbor.toString()); 
        
    }
*/
}
