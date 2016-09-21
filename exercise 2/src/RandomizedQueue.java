import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int length;
	private Item[] itemArray;
	
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		//
		length=0;
		itemArray = (Item[]) new Object[1];
	}
	
	public boolean isEmpty(){
		//check if the queue is empty
		return (length==0);
	}
	
	public int size(){
		//return the size of the RQueue now
		return length;
	} 
	
	private void resize(int capacity){
		//resize the RQueue

		Item[] tempItem = (Item[]) new Object[capacity];
		for(int i=0;i<length;i++)
			tempItem[i]=itemArray[i];
		itemArray=tempItem;
	}
	
	public void enqueue(Item item){
		 // add the item
		 //check and resize
		 if (length == itemArray.length)
			 resize(2*length);
		 itemArray[length]=item;
		 length++;
	}
	
	public Item dequeue(){                    
	// remove and return a random
		//get random index
		//Random randomGenerator=new Random();
		if (length==0)
			throw new java.util.NoSuchElementException();
		
		int index=StdRandom.uniform(length);
		
		//get the value
		Item value=itemArray[index];
		
		//use the last one to make it countinous
		itemArray[index]=itemArray[length-1];
		length--;
		
		//resize if the length<<2*(size of the Array)
		 if (( length>10 )&&( length < itemArray.length/4 ))
			 resize(itemArray.length/2);
		 
		return value;
	}
	
	public Item sample(){
		 // return (but do not remove) a random item
		//Random randomGenerator=new Random();
		if (length==0)
			throw new java.util.NoSuchElementException();
		
		int index=StdRandom.uniform(length);
		
		//get the value
		return itemArray[index];
	 }
	
	private class RQueueIterator implements Iterator<Item>{
		private int[] indexSeries; 
		private int location,ending;
		
		public  RQueueIterator(int currentLength){
			//generate random series
			//Random randomGenerator=new Random();
			if (currentLength>0)
				{
				indexSeries=new int[currentLength];
				for(int i=0;i<currentLength;i++)indexSeries[i]=i;
				for(int i=0;i<currentLength;i++)
				{
					int r = StdRandom.uniform(length);
					
					int tempValue=indexSeries[i];
					indexSeries[i]=indexSeries[r];
					indexSeries[r]=tempValue;
				}
				}			
			//location starts at 0
			location=0;
			ending=currentLength;
		} 
		 
		@Override
		public boolean hasNext() {
			return (location<ending);
		}

		@Override
		public Item next() {
			if (!hasNext())
				throw new java.util.NoSuchElementException();
			Item value=itemArray[indexSeries[location]];
			location++;			
			return value;
		}	 
	 }
	 
	public Iterator<Item> iterator(){
		return new RQueueIterator(length);
	}
	
	public static void main(String[] args) {
		//  unit testing
		RandomizedQueue<Integer> RQ1 = new RandomizedQueue<Integer>();
		System.out.println(RQ1.isEmpty());
		RQ1.enqueue(3);
		System.out.println(RQ1.isEmpty());
		System.out.println(RQ1.size());
		RQ1.enqueue(5);
		RQ1.enqueue(8);
		System.out.println(RQ1.size());
		int test1=RQ1.dequeue();
		System.out.println("the output is :"+test1);
		System.out.println("the size now is :"+RQ1.size());
		test1=RQ1.sample();
		System.out.println("the output is :"+test1);
		System.out.println("the size now is :"+RQ1.size());
		RQ1.enqueue(2);
		RQ1.enqueue(9);
		RQ1.enqueue(7);
		for (Integer element :	RQ1)
		{
			System.out.print(element+" ");
		}
		System.out.println(RQ1.isEmpty());
	}

}
