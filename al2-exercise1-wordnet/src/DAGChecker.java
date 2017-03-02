import edu.princeton.cs.algs4.Digraph;

public class DAGChecker {
	private int[] marker;
	private boolean hasLoop;
	private Digraph D;
	private int endPointNum;
	
	public DAGChecker(Digraph D)
	{
		this.D = D;
		marker = new int[D.V()];
		hasLoop = true;
		endPointNum = 0;
		for (int i = 0;i<D.V();i++)
			if (marker[i] == 0)
				dfsFindLoop(i);
	}
	
	public boolean isDAG()
	{
		return this.hasLoop;
	}
	
	public int endPointsSize()
	{
		return endPointNum;
	}
	
	private void dfsFindLoop(int vertice)
	{
		marker[vertice] = -1;
		if (!D.adj(vertice).iterator().hasNext())
			endPointNum++;
		
		for (int adj : D.adj(vertice))
		{
			if (marker[adj]==-1)
				{
				hasLoop = false;
				return;
				}
			if (marker[adj] == 0)
				dfsFindLoop(adj);
		}
		
		marker[vertice] = 1;
	}
	
	public static void main(String[] args)
	{
		
	}
}
