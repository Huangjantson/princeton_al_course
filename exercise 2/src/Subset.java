import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {

	public static void main(String[] args) {
		
		String temp;
		
		int n =  Integer.parseInt(args[0]);
		
		String[] In = StdIn.readAllStrings();
		
		RandomizedQueue<String> RQ =  new RandomizedQueue<String>();
		
		for (int i = 0;i < In.length;i++)
			RQ.enqueue(In[i]);
		
		//return the random subset to stdout
		
		for (int i = 0;i < n;i++)
			{
			temp=RQ.dequeue();
			StdOut.println(temp);
			}
			
	}

}
