import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    
    private WordNet wordnet;
        
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)
    {
           this.wordnet = wordnet;
    }
        
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)   
    {
        int maxSumDist = -Integer.MAX_VALUE;
        String result = "";

        for (String noun : nouns)
        { 
            int sumDist = 0;
            for (String otherNoun : nouns)
                sumDist = sumDist + wordnet.distance(noun, otherNoun);
            if (sumDist > maxSumDist)
            {
                maxSumDist = sumDist;
                result = noun;
            }
            
        }
            
           return result;
    }
       
    // see test client below
    public static void main(String[] args)
    {
        String testFiles = "D:\\self\\algorithms\\assignment specification\\wordnet\\";
        String synsetsFile = "synsets.txt";
        String hypernymsFile = "hypernyms.txt";
        
        WordNet wordnet = new WordNet(testFiles+synsetsFile, testFiles+hypernymsFile);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 0; t < args.length; t++) {
            In in = new In(testFiles+args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }   
    }  

}
