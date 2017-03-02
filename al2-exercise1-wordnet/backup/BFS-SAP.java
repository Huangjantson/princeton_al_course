import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
   private Digraph D;
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G){
	   D = G;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w){
	   BreadthFirstDirectedPaths BFSv =new BreadthFirstDirectedPaths(D,v);
	   BreadthFirstDirectedPaths BFSw =new BreadthFirstDirectedPaths(D,w);
	   
	   int distance = Integer.MAX_VALUE;
	   
	   for (int vertex = 0 ; vertex < D.V();vertex++)
		   if ((BFSv.hasPathTo(vertex))
				   &&(BFSw.hasPathTo(vertex))
				   &&(BFSv.distTo(vertex)+BFSw.distTo(vertex))<distance)
		   {
			   distance = BFSv.distTo(vertex)+BFSw.distTo(vertex);
		   }
	   if (distance != Integer.MAX_VALUE)
		   return distance;
	   else
		   return -1;
   }
   
   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w){
	   BreadthFirstDirectedPaths BFSv =new BreadthFirstDirectedPaths(D,v);
	   BreadthFirstDirectedPaths BFSw =new BreadthFirstDirectedPaths(D,w);
	   
	   int distance = Integer.MAX_VALUE;
	   int ancestor = -1;
	   for (int vertex = 0 ; vertex < D.V();vertex++)
		   if ((BFSv.hasPathTo(vertex))
				   &&(BFSw.hasPathTo(vertex))
				   &&(BFSv.distTo(vertex)+BFSw.distTo(vertex))<distance)
		   {
			   ancestor = vertex;
			   distance = BFSv.distTo(vertex)+BFSw.distTo(vertex);
		   }

		   return ancestor;
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w){
	   int[] distanceV,distanceW;
	   boolean[] markV,markW;
	   
	   return 0;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
	   return 0;
   }

   // do unit testing of this class
   public static void main(String[] args){
       String testFiles = "D:\\self\\algorithms\\assignment specification\\wordnet\\";
       String digraphFile = "digraph4.txt";
	   In inputFile = new In(testFiles+digraphFile);
	   Digraph D = new Digraph(inputFile);
	   
	   SAP testAgent = new SAP(D);
	   System.out.println(testAgent.D.toString());
   }
}