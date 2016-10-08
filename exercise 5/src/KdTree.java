import edu.princeton.cs.algs4.Point2D;

public class KdTree {

	private int size;
	private node root;
	enum dimension {x,y};
	
	
	public KdTree() 
	{
		size = 0;
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}

	public int size()
	{
		return this.size;
	}
	
	public void insert(Point2D p)
	{
		assert (p != null) : new java.lang.NullPointerException();
		
		//insert the root
		if (size == 0){
			this.root =  new node(p,dimension.x);
			size++;
			}
		else
		{
		//insert others
		
		}
	}
	
	private class node
	{
		private Point2D selfPoint;
		private node leftChild,rightChild;
		private dimension splitDimension;
		
		public node(Point2D p, dimension d)
		{
			selfPoint = p;
			splitDimension = d;
		}
		
		public Point2D point()
		{
			return this.selfPoint;
		}
		
		public dimension splitDimension()
		{
			return this.splitDimension;
		}
		
		public void setLeftChild(node n)
		{
			this.leftChild = n;
		}
		
		public void setRightChild(node n)
		{
			this.rightChild = n;
		}
		
	}
	
	private class findingPoint{
		private boolean exist;
		private node location;
		private node parent;
		
		public findingPoint(Point2D p){
			if (size == 0)//tree is empty
			{
				this.exist = false;
				this.location = null;
				this.parent = null;
			}
			else
			{	
				//start from the root
				node currentNode = root;
				node parentNode = null;
				dimension currentDimension = root.splitDimension();
				
				while ( currentNode != null)
				{
					if(p.equals(currentNode.point())){
						this.exist = true;
						this.location = currentNode;
						this.parent = parentNode;
					}
					else{
						if (currentDimension == dimension.x)
							if (p.x() < currentNode.point().x())
								if (currentNode.leftChild == null)
								{
									//not found, return with current state
									this.exist = false;
									this.location = null;
									this.parent = parentNode;
									break;
								}
								else
								{
									//left child does exist, keep on
									parentNode = currentNode;
									currentNode = currentNode.leftChild;
									currentDimension = currentNode.splitDimension();
								}
							else
							{
								//while in the larger case
								if (currentNode.rightChild == null)
								{
									this.exist = false;
									this.location = null;
									this.parent = parentNode;
									break;
								}
								else
								{
									//right child does exist, keep on
									parentNode = currentNode;
									currentNode = currentNode.rightChild;
									currentDimension = currentNode.splitDimension();
								}
							}
						else
						{
							//trace on with y
						}
							
					}
				}
			}
		}
	}
	
}
