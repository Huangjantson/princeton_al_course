//import java.io.IOException;
//import java.nio.file.Paths;
import java.util.Iterator;
//import java.util.Scanner;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int totalMoves;
    private Board[] solutionSeries;
    
    //Node for the game tree
    private class Node implements Comparable<Node>
    {
        private Board board;
        private int pace;
        private Node previous;
        private int manhattanPriority;
       
        public Node(Board board,int pace,Node previous) 
        {
            this.board = board;
            this.pace = pace;
            this.previous = previous;
            this.manhattanPriority = this.pace+this.board.manhattan();
        }
        
        public int getMHTPriority()
        {
            return this.manhattanPriority;
        }
        
        public Node getPrevious()
        {
            return this.previous;
        }
        
        public int getMoves()
        {
            return pace;
        }

        @Override
        public int compareTo(Node o) {
            return this.manhattanPriority-o.getMHTPriority();
        }    
    }
    
    private void getSolutionSeries(Board initial)
    {
        MinPQ<Node> NodePQ = new MinPQ<Node>();
        MinPQ<Node> twinNodePQ = new MinPQ<Node>();
        Node ending;
        Node processing = new Node(initial, 0, null);
        Node twinProcessing = new Node(initial.twin(), 0, null);
        Node traceBack;
        
        while (!(processing.board.isGoal() || twinProcessing.board.isGoal()))
        {
            //genenrate the neighbors and insert
            for (Board newNeighbor : processing.board.neighbors())
            {
                
                //check if this neighbor is the previous one
                traceBack = processing.getPrevious();
                if (traceBack != null)
                    if (traceBack.board.equals(newNeighbor))
                            continue;

                //insert the neighbors into the queue
                NodePQ.insert(new Node(newNeighbor, processing.getMoves()+1, processing));
            }
                
        //do it again for the twin board set
            for (Board newNeighbor : twinProcessing.board.neighbors()){
                
                //check if this neighbor is the previous one
                traceBack = twinProcessing.getPrevious();
                if (traceBack != null)
                    if (traceBack.board.equals(newNeighbor))
                            continue;
                
                //insert the neighbors into the queue
                twinNodePQ.insert(new Node(newNeighbor, twinProcessing.getMoves()+1, twinProcessing));
            }            
            
            //pop the node with minimun MHTPriority,set it as the next processing
            processing = NodePQ.delMin();
            twinProcessing = twinNodePQ.delMin();    
            
        }
        
        ending = processing;
        
        if (twinProcessing.board.isGoal()){
            totalMoves = -1;
        }
        else
            totalMoves = ending.getMoves();

        //use stack to get all the processings
        if (!isSolvable()) return;
        
        int move = 0;
        Board[] result = new Board[totalMoves+1];
        Stack<Board> tempStack = new Stack<Board>();
        Node Getting = ending;
        
        //resorting the solution by stack
        while (Getting != null)
        {
            tempStack.push(Getting.board);
            Getting = Getting.getPrevious();
        }
        
        while (!tempStack.isEmpty())
        {
            result[move] = tempStack.pop();
            move++;
        }
        
        solutionSeries  =  result;
        return;
    }
    
    public Solver(Board initial) 
    {
        //solve the problem while initialinzing
        getSolutionSeries(initial);
    }
    
    public boolean isSolvable()
    {
        return (this.totalMoves != -1);
    }
    
    public int moves()
    {
        return this.totalMoves;
    }
    
    /*
    private void() getSolutionSeries(){
        //use stack to get all the processings
        if(!isSolvable()) return null;
        
        int move = 0;
        Board[] result = new Board[totalMoves+1];
        Stack<Board> tempStack = new Stack<Board>();
        Node Getting = ending;
        
        //resorting the solution by stack
        while(Getting != null){
            tempStack.push(Getting.board);
            Getting = Getting.getPrevious();
        }
        
        while(!tempStack.isEmpty()){
            result[move] = tempStack.pop();
            move++;
        }
        return result;
    }
    */
    
    private class BoardList implements Iterable<Board>
    {

        public Iterator<Board> iterator() {
            if (!isSolvable())
                return null;
            else
                return new BoardIterator();
        }
        
        private class BoardIterator implements Iterator<Board>
        {
            
            private int step = 0;
            
            public boolean hasNext() {
                return !(step > totalMoves);
            }

            public Board next() {
                step++;
                return solutionSeries[step-1];
            }
            
        }
        
    }
    
    public Iterable<Board> solution() {
        BoardList answer  =  new BoardList();
        return answer;
    }
    
    /*
    public static void main(String[] args) throws IOException {
        //the main function for testing this class    
        //create the testing blocks by file
        Scanner in  =  new Scanner(Paths.get(args[0])); 
        //test the establish of the goal-board
        int N = in.nextInt();
        int[][] blocks = new int[N][N];
        for (int i  =  0; i < N; i++)
            for (int j  =  0; j < N; j++)
                blocks[i][j]  =  in.nextInt();
        
        in.close();
        
        Board initial = new Board(blocks);
        Solver testSolver = new Solver(initial);
        System.out.println(testSolver.moves());
        if (testSolver.isSolvable()){
        for (Board steps : testSolver.solution())
            System.out.println(steps.toString());
            }
    }
    */
}
