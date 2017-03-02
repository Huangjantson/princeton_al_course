
import java.util.LinkedList;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.In;

public class BoggleSolver {

	private Node root;
    
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
    	int val;
    	//build dictionary
    	for (String word : dictionary)
    		{
    		if(word.length() < 3)// actually 3
    			val = 0;
    		else if (word.length() < 5)
    				val = 1;
    		else
    			switch(word.length())
    				{
    				case 5 : val = 2; 
    				break;
    				case 6 : val = 3;
    				break;
    				case 7 : val = 5;
    				break;
    				default : val = 11;
    				break;
     				}
    		put(word, val);
    		}
    }
    
    private static class Node 
    {
        private char c;                        // character
        private Node left, mid, right;  // left, middle, and right subtries. And previous letter
        private Integer val;                     // value associated with string
    }

    //put the key into the dictionary
    private void put(String key, Integer val) 
    {
        root = put(root, key, val, 0);
    }
    
    //put the key into the dict recursively
    private Node put(Node x, String key, Integer val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }
    
    //return the value for the given key
    private Integer get(String key) 
    {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }
    
    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }
    
    //return the subtrie with the given char as the next char
    private Node find(Node x, char c)
    {
    	if (x == null) return null;
    	return searchFor(x.mid, c);
    } 
    
    //find the given char as the last letter in left/right nodes
    //or return itself when equals
    private Node searchFor(Node x, char c)
    {
    	if (x == null) return null;
    	if (c == x.c) 
    		return x;
    	else if (c < x.c)
    		return searchFor(x.left,c);
    	else
    		return searchFor(x.right,c); 		
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
     public Iterable<String> getAllValidWords(BoggleBoard board)
    {
    	TST<Integer> tokenList = new TST<Integer>();
    	int boardRows = board.rows();
    	int boardCols = board.cols();
    	boolean[][] searching = new boolean[boardRows][boardCols];
    	Node nodePointer;
    	
    	String possible;
    	for (int i = 0; i < boardRows; i++)
    		for (int j = 0; j < boardCols; j++)
    			//DFS-like algorithm for each node possible paths
    			{
    			
    			    if ((board.getLetter(i, j) == 'Q'))
					{
    			    	possible = "QU";
    			    	nodePointer = find(searchFor(root, 'Q'), 'U');
					}
    			    else
    			    {
    			    	possible = ""+board.getLetter(i, j);
    			    	nodePointer = searchFor(root, board.getLetter(i, j));
    			    }
    				if (nodePointer != null)
    				{
    					searching[i][j] = true;
    					concatenateNext(i, j, tokenList, searching, possible, board, nodePointer);
    					searching[i][j] = false;
    				}
    			}
    	return tokenList.keys();
    }
 
    //recursive use this to find the valid words
    private void concatenateNext(int iLoc, int jLoc, TST<Integer> tokenList,
    		boolean[][] searching, String prefix, BoggleBoard board, Node pointer)
    {
    	int iMax = board.rows() - 1;
    	int jMax = board.cols() - 1;
    	
    	//iStart,jStart,iEnd,jEnd
    	int iStart, jStart, iEnd, jEnd;
    	
    	if (iLoc - 1 < 0)
    		iStart = 0;
    	else
    		iStart = iLoc - 1;
    	
    	if (jLoc - 1 < 0)
    		jStart = 0;
    	else
    		jStart = jLoc - 1;
    	
    	if (iLoc + 1 > iMax)
    		iEnd = iMax;
    	else
    		iEnd = iLoc + 1;
    	
    	if (jLoc + 1 > jMax)
    		jEnd = jMax;
    	else
    		jEnd = jLoc + 1;
    	
    	for (int i = iStart; i<=iEnd;i++)
    		for (int j = jStart; j<=jEnd;j++)
    			if (!searching[i][j])
    			{
    				searching[i][j] = true;
    				//recursive find the available strings
    				String next;
    				Node nextPointer;

    				if ((board.getLetter(i, j) == 'Q'))
    				{
    					nextPointer = find(find(pointer, 'Q'), 'U');
    					next = prefix + "QU";
    				}
    				else
    				{
    					next = prefix + board.getLetter(i, j);
    					nextPointer = find(pointer, board.getLetter(i, j));
    				}
    				
    				//checking for if there is a valid prefix
    				if (nextPointer != null)
    				{
    					//check if it is a valid word
    					if (nextPointer.val != null)
    						if (nextPointer.val != 0)
    							tokenList.put(next, 0);
    					concatenateNext(i, j, tokenList, searching,
    							next, board, nextPointer);
    				}
    				searching[i][j] = false;
    			}
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	Integer temp = get(word);
    	if (temp == null)
    		return 0;
    	else
    		return temp;
    }
    
	public static void main(String[] args) 
	{
		String testLoc = "D:\\self\\algorithms\\assignment specification\\boggle";
		String dictFile = "dictionary-common.txt";
		//String boardFile = "board4x4.txt";
		String boardFile = "board-aqua.txt";
		
		String dictLoc = testLoc + "\\" + dictFile;
		String boardLoc = testLoc + "\\" + boardFile;
		
		LinkedList<String> tempString = new LinkedList<String>();
		In in = new In(dictLoc);
		while(in.hasNextLine())
		{
			String temp = in.readLine();
			if (temp!="")
				tempString.add(temp);
		}
		int len = tempString.size();
		
		String[] dict = new String[len];
		int count = 0;
		for (String temp : tempString)
		{
			dict[count] = temp;
			count++;
		}	
		
		//test the board
		BoggleBoard testBoard = new BoggleBoard(boardLoc);
		BoggleSolver test = new BoggleSolver(dict);
		
		//System.out.println(test.scoreOf("ZZ"));
		
		for (String temp : test.getAllValidWords(testBoard))
			System.out.println(temp);
	}

}
