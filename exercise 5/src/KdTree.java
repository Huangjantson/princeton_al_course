import java.util.Iterator;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int size;
    private node root;
    private enum dimension {x, y};
    
    
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
        assert ((p.x() >= 0)&&(p.x() <= 1)&&(p.y() >= 0)&&(p.y() <= 1)):
        new java.lang.IllegalArgumentException();
        //assert for area of p,TODO
        
        //insert the root
        if (size == 0){
            this.root =  new node(p, dimension.x);
            this.root.rect = new RectHV(0, 0, 1, 1);
            size++;
            }
        else
        {
        //insert others : find the parent of the point, and connect the new node to its parents
            findingPoint searchResult = new findingPoint(p);
            if (!searchResult.exist)
            {
                dimension newDimension = searchResult.parent.splitDimension();
                
                //revert the dimension to new dimension
                if (newDimension == dimension.x)
                    newDimension = dimension.y;
                else
                    newDimension = dimension.x;
                
                if (searchResult.leftRight == -1){
                    searchResult.parent.setLeftChild(new node(p, newDimension));
                    size++;
                    }
                else{
                    searchResult.parent.setRightChild(new node(p, newDimension));
                    size++;
                }
            }
            
        }
    }
    
    public boolean contains(Point2D p)
    {
        findingPoint searchResult = new findingPoint(p);
        return searchResult.exist;
    }
    
    public void draw()
    {
        //TODO 
        recurDraw(root);
    }
    
    public Iterable<Point2D> range(RectHV rect)             
    {
        // all points that are inside the rectangle 
        //start at root
        //search in both sub-trees
        //if the query rectangle does no intersect with the rect then pass
        //=>need to come up with the query rectangle
        assert (rect != null) : new java.lang.NullPointerException();
        rangePointIterable rangePoints = new rangePointIterable(rect);
        return rangePoints;
    }
    
    public Point2D nearest(Point2D p)            
    { 
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (isEmpty())
            return null;
        
        //pruning rule for searching the nearest
        distNode temp = recurNearest(root, p, 20);
        
        return temp.n.selfPoint;
    }

    private class node
    {
        private node leftChild, rightChild;
        private Point2D selfPoint;
        private dimension splitDimension;
        public RectHV rect;
        
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
            //set up n's rect;we change the upper bound for the left child
            if (this.splitDimension == dimension.x)
                n.rect=new RectHV(this.rect.xmin(),this.rect.ymin(),this.selfPoint.x(),this.rect.ymax());
            else
                n.rect=new RectHV(this.rect.xmin(),this.rect.ymin(),this.rect.xmax(),this.selfPoint.y());
        }
        
        public void setRightChild(node n)
        {
            this.rightChild = n;
            //set up n's rect;we change the lower bound for the right child
            if (this.splitDimension == dimension.x)
                n.rect=new RectHV(this.selfPoint.x(),this.rect.ymin(),this.rect.xmax(),this.rect.ymax());
            else
                n.rect=new RectHV(this.rect.xmin(),this.selfPoint.y(),this.rect.xmax(),this.rect.ymax());
        }
        
    }
    
    private class findingPoint
    {
        private boolean exist;
        private node location;
        private node parent;
        private int leftRight;
        //using -1 for left and 1 for right, 
        //describing if the point should be its parent's left or right
        //for not existing especially
        
        public findingPoint(Point2D p)
        {
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
                boolean dimensinX =  true;
                
                while (currentNode != null)
                {
                    if (p.equals(currentNode.point()))
                    {
                        this.exist = true;
                        this.location = currentNode;
                        break;
                    }
                    else
                    {
                            //condition 1: the dimension=>going always down, maybe use not to change it all the way
                            //condition 2: left or right
                            //condition 3: exist or not
                        if (dimensinX)
                            if (p.x() < currentNode.point().x())
                                if (currentNode.leftChild == null)
                                {
                                    //not found, return with current state
                                    this.exist = false;
                                    this.parent = currentNode;
                                    this.leftRight = -1;
                                    break;
                                }
                                else
                                {
                                    //left child does exist, keep on
                                    currentNode = currentNode.leftChild;
                                    dimensinX = !dimensinX;
                                }
                            else
                            {
                                //while in the larger case
                                if (currentNode.rightChild == null)
                                {
                                    this.exist = false;
                                    this.parent = currentNode;
                                    this.leftRight = 1;
                                    break;
                                }
                                else
                                {
                                    //right child does exist, keep on
                                    currentNode = currentNode.rightChild;
                                    dimensinX = !dimensinX;
                                }
                            }
                        else
                        {
                            //trace on with y
                            if (p.y() < currentNode.point().y())
                                if (currentNode.leftChild == null)
                                {
                                    //not found, return with current state
                                    this.exist = false;
                                    this.parent = currentNode;
                                    this.leftRight = -1;
                                    break;
                                }
                                else
                                {
                                    //left child does exist, keep on
                                    currentNode = currentNode.leftChild;
                                    dimensinX = !dimensinX;
                                }
                            else
                            {
                                //while in the larger case
                                if (currentNode.rightChild == null)
                                {
                                    this.exist = false;
                                    this.parent = currentNode;
                                    this.leftRight = 1;
                                    break;
                                }
                                else
                                {
                                    //right child does exist, keep on
                                    currentNode = currentNode.rightChild;
                                    dimensinX = !dimensinX;
                                }
                            }
                        }            
                        
                    }
                }
            }
        }
    }
    
    private class rangePointIterable implements Iterable<Point2D>
    {
        
        private TreeSet<Point2D> treeForRange;
        
        public rangePointIterable(RectHV rect)
        {
            treeForRange =  new TreeSet<Point2D>();    
            //start from root, test all the parts
            recurAdd(root, rect,treeForRange);
        }
        
        private void recurAdd(node n, RectHV rect, TreeSet<Point2D> treeForRange)
        {
            
            //adding the corresponding points to treeForRange
            if (rect.contains(n.point()))
                treeForRange.add(n.point());
            
            //check if intersect with the rect
            if (!rect.intersects(n.rect))
                return;
            
            //check and do it recursively
            if (n.leftChild != null)
                recurAdd(n.leftChild, rect,treeForRange);
            
            if (n.rightChild != null)
                recurAdd(n.rightChild, rect,treeForRange);
        }
        
        public Iterator<Point2D> iterator() {
            return treeForRange.iterator();
        }
    }
    
    private class distNode
    {
        private node n;
        private double dist;
        public distNode(node n, double dist){
            this.n = n;
            this.dist = dist;
        }
    }
    
    private distNode recurNearest(node n, Point2D p, double minDist){
        
        double minDistance = minDist;
        node cloestNode = n;
        
        //check if the point is exactly the one looking for
        if (p.equals(cloestNode.selfPoint))
            return new distNode(n, 0);
        
        //check if the rect of this point is shorter than the distance with the nearest one
        if (cloestNode.rect.distanceTo(p) > minDistance)
            return new distNode(n, 20);
        
        //change the minDist if this is the minDist
        if (cloestNode.selfPoint.distanceTo(p) < minDistance)
            minDistance = cloestNode.selfPoint.distanceTo(p); 
        
       //recursive look for the left/right sub-tree, if the returing smaller than return their returning
        if (n.leftChild != null)
            {
            distNode temp = recurNearest(n.leftChild, p ,minDistance);
            if (temp.dist < minDistance)
            {
                minDistance = temp.dist;
                cloestNode  = temp.n;
            }
            }
        
        if (n.rightChild != null)
        {
        distNode temp = recurNearest(n.rightChild, p, minDistance);
        if (temp.dist < minDistance)
        {
            minDistance = temp.dist;
            cloestNode  = temp.n;
        }
        }
        
        return new distNode(cloestNode, minDistance);
    }

    private void recurDraw(node n)
    {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.selfPoint.x(), n.selfPoint.y());
        
        StdDraw.setPenRadius(0.001);
        if (n.splitDimension == dimension.x)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.selfPoint.x(), 0, n.selfPoint.x(), 1);
        }
            else 
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(0, n.selfPoint.y(), 1, n.selfPoint.y());
        }
        
        if (n.leftChild != null)
            recurDraw(n.leftChild);
        if (n.rightChild != null)
            recurDraw(n.rightChild);
    }
    
    public static void main(String args[])
    {

    // test for establishing
    KdTree testSet = new KdTree();
    
    // test for isEmpty 1
    System.out.println("should be empty while the return for isEmpty is ");
    System.out.println(testSet.isEmpty());
    
    // test for size 1
    System.out.println("should be empty while the return for size is ");
    System.out.println(testSet.size());
    
    // test for insert
    System.out.println("Insert these points: {(0.2,0.5),(0.1,0.4),(0.4,0.3),(0.6,0.5),(0.8,0.6)}");
    testSet.insert(new Point2D(0.2, 0.5));
    testSet.insert(new Point2D(0.1, 0.4));
    testSet.insert(new Point2D(0.4, 0.3));
    testSet.insert(new Point2D(0.6, 0.5));
    testSet.insert(new Point2D(0.8, 0.6));
    
    // test for isEmpty 2
    System.out.println("should not be empty while the return for isEmpty is ");
    System.out.println(testSet.isEmpty());
    
    // test for size 2
    System.out.println("should have a size of 5 while the return for isEmpty is ");
    System.out.println(testSet.size());
    
    // test for contain
    System.out.println("test for contain using point (0.2,0.5)");
    System.out.println(testSet.contains(new Point2D(0.2, 0.5)));
    System.out.println("test for contain using point (0.4,0.3)");
    System.out.println(testSet.contains(new Point2D(0.4, 0.3)));
    System.out.println("test for contain using point (0.4,0.4),which isn't contained");
    System.out.println(testSet.contains(new Point2D(0.4, 0.4)));
    
    
    // test for draw
    testSet.draw();
    
    // test for range
    System.out.println("the following poins are contained in the rect (0,0,0.5,0.5)");
    RectHV rect = new RectHV(0, 0, 0.5, 0.5); 
    for (Point2D each : testSet.range(rect))
        System.out.println(each);
    
    //test for nearest
    System.out.println("finding the nearest point for (0.1,0.1):");
    System.out.println(testSet.nearest(new Point2D(0.1, 0.1)));
    System.out.println("finding the nearest point for (0.5,0.5):");
    System.out.println(testSet.nearest(new Point2D(0.5, 0.5)));
    
    }
}
