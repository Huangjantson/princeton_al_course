import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;


public class SAP {
   private final Digraph D;
   
   private class AccessPoint
   {
       private int distToSrc;
       private int src;
       private int vertex;
       
       public AccessPoint(int vtx, int distance, int source)
       {
           this.distToSrc = distance;
           this.src = source;
           this.vertex = vtx;
       }
   }
   
   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G)
   {
       
       if (G == null)
           throw new NullPointerException();
       
       D = new Digraph(G.V());
       for (int v = 0; v < G.V(); v++)
    	   for (int w : G.adj(v))
    		   D.addEdge(v, w);
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w)
   {
          
       boolean[] markV, markW;
       int[] distV, distW;
       
       Queue<AccessPoint> accessQueue =  new Queue<AccessPoint>();
       AccessPoint processing;
       int SAPlength = Integer.MAX_VALUE;
       
       markV = new boolean[D.V()];
       markW = new boolean[D.V()];
       distV = new int[D.V()];
       distW = new int[D.V()];
       
       accessQueue.enqueue(new AccessPoint(v, 0, 1));
       accessQueue.enqueue(new AccessPoint(w, 0, 2));
       
       while (!accessQueue.isEmpty())
       {
           processing = accessQueue.dequeue();
           if (processing.distToSrc > SAPlength)
               continue;
           if (processing.src == 1)
           {
               if (markV[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markV[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,1));
             	   }
               }
               markV[processing.vertex] = true;
               distV[processing.vertex] = processing.distToSrc;
           } 
           else
           {
               if (markW[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markW[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,2));
             	   }
               }
               markW[processing.vertex] = true;
               distW[processing.vertex] = processing.distToSrc;
           }
                      
           if ((markV[processing.vertex]) && (markW[processing.vertex]))
               if (distV[processing.vertex]+distW[processing.vertex] < SAPlength) 
               {
                   SAPlength = distV[processing.vertex]+distW[processing.vertex];
                   //result = processing.vertex; //ancestor
               }        
       }
       
       if (SAPlength == Integer.MAX_VALUE)
           return -1;
       else
           return SAPlength;
   }
   
   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w)
   {
       boolean[] markV, markW;
       int[] distV, distW;
       int result = -1;
       
       Queue<AccessPoint> accessQueue =  new Queue<AccessPoint>();
       AccessPoint processing;
       int SAPlength = Integer.MAX_VALUE;
       
       markV = new boolean[D.V()];
       markW = new boolean[D.V()];
       distV = new int[D.V()];
       distW = new int[D.V()];
       
       accessQueue.enqueue(new AccessPoint(v, 0, 1));
       accessQueue.enqueue(new AccessPoint(w, 0, 2));
       
       while (!accessQueue.isEmpty())
       {
           processing = accessQueue.dequeue();
           if (processing.distToSrc > SAPlength)
               continue;
           if (processing.src == 1)
           {
               if (markV[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
            	   if (!markV[adj]){  
                      accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,1));
            	   }
               }
               markV[processing.vertex] = true;
               distV[processing.vertex] = processing.distToSrc;
           } 
           else
           {
               if (markW[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
            	   if (!markW[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,2));
             	   }
               }
               markW[processing.vertex] = true;
               distW[processing.vertex] = processing.distToSrc;
           }
                      
           if ((markV[processing.vertex]) && (markW[processing.vertex]))
               if (distV[processing.vertex]+distW[processing.vertex] < SAPlength) 
               {
                   SAPlength = distV[processing.vertex]+distW[processing.vertex];
                   result = processing.vertex; //ancestor
               }        
       }
       
       if (SAPlength == Integer.MAX_VALUE)
           return -1;
       else
           return result;
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w)
   {
       boolean[] markV, markW;
       int[] distV, distW;
       //int result = -1;//ancestor
       
       Queue<AccessPoint> accessQueue =  new Queue<AccessPoint>();
       AccessPoint processing;
       int SAPlength = Integer.MAX_VALUE;
       
       markV = new boolean[D.V()];
       markW = new boolean[D.V()];
       distV = new int[D.V()];
       distW = new int[D.V()];
       
       //src = 1 then from v, src = 2 then from w
       for (int start : v)
           accessQueue.enqueue(new AccessPoint(start, 0, 1));
       for (int start : w)
           accessQueue.enqueue(new AccessPoint(start, 0, 2));
       
       while (!accessQueue.isEmpty())
       {
           processing = accessQueue.dequeue();
           if (processing.distToSrc > SAPlength)
               continue;
           if (processing.src == 1)
           {
               if (markV[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markV[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,1));
             	   }
               }
               markV[processing.vertex] = true;
               distV[processing.vertex] = processing.distToSrc;
           } 
           else
           {
               if (markW[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markW[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,2));
             	   }
               }
               markW[processing.vertex] = true;
               distW[processing.vertex] = processing.distToSrc;
           }
                      
           if ((markV[processing.vertex]) && (markW[processing.vertex]))
               if (distV[processing.vertex]+distW[processing.vertex] < SAPlength) 
               {
                   SAPlength = distV[processing.vertex]+distW[processing.vertex];
                   //result = processing.vertex;//ancestor
               }        
       }
       
       if (SAPlength == Integer.MAX_VALUE)
           return -1;
       else
           return SAPlength;
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
       boolean[] markV, markW;
       int[] distV, distW;
       int result = -1; //ancestor
       
       Queue<AccessPoint> accessQueue =  new Queue<AccessPoint>();
       AccessPoint processing;
       int SAPlength = Integer.MAX_VALUE;
       
       markV = new boolean[D.V()];
       markW = new boolean[D.V()];
       distV = new int[D.V()];
       distW = new int[D.V()];
       
       //src = 1 then from v, src = 2 then from w
       for (int start : v)
           accessQueue.enqueue(new AccessPoint(start, 0, 1));
       for (int start : w)
           accessQueue.enqueue(new AccessPoint(start, 0, 2));
       
       while (!accessQueue.isEmpty())
       {
           processing = accessQueue.dequeue();
           if (processing.distToSrc > SAPlength)
               continue;
           if (processing.src == 1)
           {
               if (markV[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markV[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,1));
             	   }
               }
               markV[processing.vertex] = true;
               distV[processing.vertex] = processing.distToSrc;
           } 
           else
           {
               if (markW[processing.vertex])
                   continue;
               for (int adj : D.adj(processing.vertex))
               {
               	   if (!markW[adj]){  
                       accessQueue.enqueue(new AccessPoint(adj, processing.distToSrc+1,2));
             	   }
               }
               markW[processing.vertex] = true;
               distW[processing.vertex] = processing.distToSrc;
           }
                      
           if ((markV[processing.vertex]) && (markW[processing.vertex]))
               if (distV[processing.vertex]+distW[processing.vertex] < SAPlength) 
               {
                   SAPlength = distV[processing.vertex]+distW[processing.vertex];
                   result = processing.vertex; //ancestor
               }        
       }
       
       if (SAPlength == Integer.MAX_VALUE)
           return -1;
       else
           return result;
   }

   // do unit testing of this class
   public static void main(String[] args)
   {
       String testFiles = "D:\\self\\algorithms\\assignment specification\\wordnet\\";
       String digraphFile = "digraph1.txt";
       In inputFile = new In(testFiles+digraphFile);
       Digraph D = new Digraph(inputFile);
       
       SAP testAgent = new SAP(D);
       System.out.println(testAgent.D.toString());
       System.out.println(testAgent.ancestor(3, 3));
       System.out.println(testAgent.length(3, 3));           
       
       /*
       Integer[] testV = new Integer[3];
       Integer[] testW = new Integer[2];
       testV[0]=4;
       testV[1]=10;
       testV[2]=2;
       testW[0]=8;
       testW[1]=12;
       
       LinkedList<Integer> testVl = new LinkedList<Integer>(); 
       for (Integer i : testV )
           testVl.add(i);
       LinkedList<Integer> testWl = new LinkedList<Integer>(); 
       for (Integer i : testW )
           testWl.add(i);
       
       System.out.println(testAgent.ancestor(testVl,testWl));
       System.out.println(testAgent.length(testVl,testWl));
   	   */
   }
}