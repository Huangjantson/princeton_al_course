import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.LinkedList;
import java.util.TreeSet;

public class WordNet {
    //private Bag<Synset>[] netStorage;
    private final Digraph netStorage; 
    private final Synset[] synsetArray;
    private final int synsetSize;
    private boolean existSAPfinder;
    private boolean existNounList;
    private TreeSet<String> nounList;
    private SAP finder;
    
    //Synsets as node
    private class Synset 
    {
        private String[] synWords;
        private String synsetStore;
        Synset(String inputWord)
        {
            
            String[] splited = inputWord.split(",");
            synsetStore = splited[1];
            synWords = splited[1].split(" ");
        }
    }
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) 
    {
        if (synsets == null)
            throw new NullPointerException();
        if (hypernyms == null)
            throw new NullPointerException();
        
        //count the synset number and build the arrays
        LinkedList<String> synsetList = new LinkedList<String>();
        In in = new In(synsets);  
        while (in.hasNextLine())
            synsetList.add(in.readLine());
        in.close();
        
        int count = synsetList.size();
        this.synsetArray = new Synset[count];
        this.netStorage = new Digraph(count);
        this.synsetSize = count;
        
        //build the synset array
        count = 0;
        for (String synset : synsetList)
        {    
            this.synsetArray[count] = new Synset(synset);
            count++;
        }
        
        //construnct the WordNet
        in = new In(hypernyms);
        String[] temp;
        int id;
        while (in.hasNextLine())
        {
            //build the bag for each line
            temp = in.readLine().split(",");
            id = Integer.parseInt(temp[0]);
            for (int i = 1; i < temp.length; i++)
                this.netStorage.addEdge(id, Integer.parseInt(temp[i]));
        }
        in.close();
        
        DAGChecker checker = new DAGChecker(netStorage);
        
        if (!checker.isDAG())
            throw new IllegalArgumentException("Input hypernyms not DAG");
        
        if (checker.endPointsSize() > 1)
            throw new IllegalArgumentException("More than one roots");
        
        existSAPfinder = false;
        existNounList = false;
        //finder = new SAP(this.netStorage);
    }
    
    //return the WordNet in Bags form, with the first noun printed
    private void printNet()
    {
        for (int i = 0; i < synsetArray.length; i++)
        {
              System.out.print(synsetArray[i].synWords[0]);
              for (int j : netStorage.adj(i))
                  System.out.print(" ->"+synsetArray[j].synWords[0]);
              System.out.println();
         }
    }
    
    private LinkedList<Integer> findNoun(String noun)
    {
        LinkedList<Integer> result = new LinkedList<Integer>();
        for (int i = 0; i < synsetArray.length; i++)
            for (String word : synsetArray[i].synWords)
                if (word.equals(noun))
                    result.add(i);
        return result;
    }
        
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
    	if (!existNounList)
    	{
    		nounList = new TreeSet<String>();
            for (Synset synset : synsetArray)
                for (String word : synset.synWords)
                	nounList.add(word);
            existNounList = true;
    	}
        return nounList;
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {   
        if (word == null)
            throw new NullPointerException();
        
    	if (!existNounList)
    	{
    		nounList = new TreeSet<String>();
            for (Synset synset : synsetArray)
                for (String newNoun : synset.synWords)
                	nounList.add(newNoun);
            existNounList = true;
    	}
        
        return nounList.contains(word);     
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if ((nounA == null) || (nounB == null))
            throw new NullPointerException();
        if (!(isNoun(nounA) && (isNoun(nounB))))
            throw new IllegalArgumentException("One of the input is not Noun included");
                       
        //find nounA and nounB's corresponding id
        LinkedList<Integer> IDA = findNoun(nounA);
        LinkedList<Integer> IDB = findNoun(nounB);
        
        if (!existSAPfinder)
        {
        	finder = new SAP(this.netStorage);
        	existSAPfinder = true;
        }
        //find the SAP distance by finder
        return finder.length(IDA, IDB);
    }
    
    // a synset (second field of synsets.txt) that is the 
    // common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if ((nounA == null) || (nounB == null))
            throw new NullPointerException();
        if (!(isNoun(nounA) && (isNoun(nounB))))
            throw new IllegalArgumentException("One of the input is not Noun included");
        
        //find nounA and nounB's corresponding id
        LinkedList<Integer> IDA = findNoun(nounA);
        LinkedList<Integer> IDB = findNoun(nounB);
        
        if (!existSAPfinder)
        {
        	finder = new SAP(this.netStorage);
        	existSAPfinder = true;
        }
        
        //find the SAP's ancestor by finder
        int ancestorID = finder.ancestor(IDA, IDB);
        
        //return the SAP's synset
        String result = synsetArray[ancestorID].synsetStore;
                
        return result;
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
       String testFiles = "D:\\self\\algorithms\\assignment specification\\wordnet\\";
       String synsetsFile = "synsets6.txt";
       String hypernymsFile = "hypernyms6TwoAncestors.txt";
        
        
       WordNet test = new WordNet(testFiles+synsetsFile,
                testFiles+hypernymsFile);
        
       test.printNet();
       System.out.println(test.synsetSize);
       for (String word : test.nouns())
           System.out.println(word);
       
       System.out.println(test.sap("a", "f"));
        
    }

}
