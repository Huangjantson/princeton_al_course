import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	 private int length;
	 private Node first;
	 private Node last;
	 
	 
	 private class Node{
		 Item item;
		 Node before;
		 Node after;
	 }
	 
	 private class dequeIterator implements Iterator<Item>{
		 private Node current = first.after;
		 
		 @Override
		 public boolean hasNext(){ return (current!=last);}

		 @Override
		 public Item next(){
			//corner case
			if (current==null)
				throw new java.util.NoSuchElementException("end of iteration");
			Item value =current.item;
			current=current.after;
			return value;
		 }
	 }
		
	public Deque() {
		/* A double-ended queue or deque (pronounced "deck") 
		 * is a generalization of a stack and a queue that 
		 * supports adding and removing items from either the 
		 * front or the back of the data structure. */
		
		this.first = new Node();
		this.last = new Node();
		this.first.after=last;
		this.last.before=first;
		this.first.before=null;
		this.last.after=null;
		this.length=0;
	 }
	 
	public boolean isEmpty(){
		// is the deque empty?
		return (length==0);
	}
	
	public int size(){
		// return the number of items on the deque
		return length;
	}  
	
	public void addFirst(Item item){
		// add the item to the front
		if (item==null)
			throw new NullPointerException("Inserting null item");
		
		Node newNode = new Node();

		newNode.after=first.after;
		newNode.before=first;
		first.after.before=newNode;
		first.after=newNode;
		
		newNode.item=item;
		length++;
	}
	
	public void addLast(Item item){
		// add the item to the end
		if (item==null)
			throw new NullPointerException("Inserting null item");
		
		Node newNode = new Node();

		newNode.after=last;
		newNode.before=last.before;
		last.before.after=newNode;
		last.before=newNode;
		
		newNode.item=item;
		length++;
	}
	
	public Item removeFirst(){
		// remove and return the item from the front
		if (this.isEmpty())
			throw new NoSuchElementException("the deque is empty"); 
		
		Node temp=first.after;
		Item value=temp.item;
		
		first.after=temp.after;
		temp.after.before=first;
		length--;
		
		return value;
	}
	
	public Item removeLast(){
		// remove and return the item from the front
		if (this.isEmpty())
			throw new NoSuchElementException("the deque is empty"); 
		
		Node temp=last.before;
		Item value=temp.item;
		
		last.before=temp.before;
		temp.before.after=last;
		length--;
		
		return value;
	}
	
	 @Override
	 public Iterator<Item> iterator(){
		 return new dequeIterator();
	 }

	public static void main(String[] args) {
		/*testing*/
		Integer test1,test2;
		Deque<Integer> testDeque = new Deque<Integer>();
		//test isEmpty
		System.out.println(testDeque.isEmpty());
		//test addFirst
		testDeque.addFirst(8);
		//test addLast
		testDeque.addLast(9);
		//test size
		System.out.println(testDeque.size());
		//check the Deque and test the iterator
		for (Integer element :	testDeque)
		{
			System.out.print(element+" ");
		}
		System.out.println();
		testDeque.addFirst(2);
		testDeque.addLast(3);
		testDeque.addLast(5);
		testDeque.addLast(7);
		//Now the numbers in Deque should be 2 8 9 3 5 7
		for (Integer element :	testDeque)
		{
			System.out.print(element+" ");
		}
		System.out.println();
		//test removeFirst
		test1=testDeque.removeFirst();
		System.out.println("removeFirst:"+test1);
		//test removeLast
		test2=testDeque.removeLast();
		System.out.println("removeLast:"+test2);
		System.out.println("Things left are:");
		for (Integer element :	testDeque)
		{
			System.out.print(element+" ");
		}
		System.out.println(testDeque.isEmpty());
	}

}
