package percolation_exercise;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
public class PercolationStats {

	private double[] PercolationArray;
	private int trials;
	
	private void validate(int n,int trial){
		if ((n<1)||(trial<1))
			throw new IllegalArgumentException("one of the input is smaller or equal to 0");
	}
	
	public PercolationStats(int n, int trials) {
		//initiallize
		int x,y,openLoc;
		float times;
		this.trials=trials;
		Percolation tempPercolation;
		validate(n,trials);
		
		PercolationArray = new double[trials];
		
		//create a percolated system
		for (int i=0;i<trials;i++)
		{
			tempPercolation=new Percolation(n);
			while(!tempPercolation.percolates()){
				openLoc=StdRandom.uniform(n*n);
				x=openLoc/n+1;
				y=openLoc%n+1;
				tempPercolation.open(x, y);
			}
		
			times=0;
			//count how many blocks are open	
			for (int p=1;p<=n;p++)
				for (int q=1;q<=n;q++)
					if (tempPercolation.isOpen(p, q))
					times++;
		
			PercolationArray[i]=times/(n*n);	
		}	
	}
	
	public double mean(){
		return StdStats.mean(PercolationArray);
	}

	public double stddev(){
		return StdStats.stddevp(PercolationArray);
	}
	
	public double confidenceLo() {
		return mean()-1.96*stddev()/Math.sqrt(trials);
	}
	
	public double confidenceHi() {
		return mean()+1.96*stddev()/Math.sqrt(trials);
	}
	
	public static void main(String[] args) {
		
		assert(args.length==2);
		
		int n,trials;
		n=Integer.parseInt(args[0]);
		trials=Integer.parseInt(args[1]);
		
		PercolationStats Ps= new PercolationStats(n,trials);
		
		System.out.printf("Mean = %f \n",Ps.mean());
		System.out.printf("stddev = %f \n",Ps.mean());
		System.out.printf("95% confidence interval = %f , %f \n",Ps.confidenceLo(),Ps.confidenceHi());
		
	}

}
