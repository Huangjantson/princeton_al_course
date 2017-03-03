import java.util.Arrays;

public class CircularSuffixArray {
    private int length;
    private CircularSuffix[] cs;
    
    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
        if (s == null)
            throw new java.lang.NullPointerException();
        length = s.length();
        cs = sortSuffix(s);
  
    }  
    
    private class CircularSuffix implements Comparable<CircularSuffix>
    {
        private int start;
        private int firstRank;
        private int secondRank;
        private char[] s;
        
        CircularSuffix(char[] s, int start)
        {
            this.s = s;
            this.start = start;
            this.firstRank = (int) s[start];
            if (start == s.length - 1)
                this.secondRank = s[0];
            else
                this.secondRank = s[start+1];
        }

        @Override
        public int compareTo(CircularSuffix o) {
            if (this.getClass() != o.getClass())
                throw new java.lang.IllegalArgumentException();
            
            if (this.firstRank < o.firstRank)
                return -1;
            else if (this.firstRank > o.firstRank)
                return 1;
            else if (this.secondRank < o.secondRank)
                return -1;
            else if (this.secondRank > o.secondRank)
                return 1;
            else
            return 0;
        }
        
        public boolean rankEquals(CircularSuffix o)
        {
            return (this.firstRank == o.firstRank) &&
                    (this.secondRank == o.secondRank);
        }
        
        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            int location = start;
            for (int i = 0; i < s.length; i++)
            {
                if (location >= s.length)
                    location = location - s.length;
                buffer.append(s[location]);
                location++;
            }
            return buffer.toString();
        }
    }
    
    private CircularSuffix[] sortSuffix(String s)
    {
        char [] charStore = new char[length];
        for (int i = 0; i < length; i++)
        {    
            charStore[i] = s.charAt(i);
        }
        
        CircularSuffix[] cs = new CircularSuffix[length];
        
        //initialize the ranks for tht first part by char and initialize ids
        for (int i = 0; i < length; i++)
            cs[i] = new CircularSuffix(charStore, i);
        /*
        for (int i = 0; i < this.length; i++){
            System.out.println(i+":"+(char) cs[i].firstRank+":"+(char) cs[i].secondRank);
        }
        */
        //initial sort : using nlogn sorting for the first two parts
        Arrays.sort(cs);
       
        /*
        for (int i = 0; i < this.length; i++){
            System.out.println(cs[i].toString()+":"+cs[i].start);
        }
        
        System.out.println("================================");
        */
        //following sorts : using radix sorting 
        for (int l = 2; l < length; l = l*2)
        {
            //  initial the new rank for the first parts 
            //by original first and second rank
            //  check if there is any first rank equals
            boolean repeatedRank = false;
            int[] newFirst = new int[length];
            int[] firstCount = new int[length];
            firstCount[0] = 1;
            for (int i = 1; i < length; i++)
            {
                if (cs[i-1].rankEquals(cs[i]))
                {
                    newFirst[i] = newFirst[i-1];
                    repeatedRank = true;
                }
                else
                    newFirst[i] = i;
                firstCount[newFirst[i]]++;
            }
            
            
            if (!repeatedRank)
                break;
            
            
            
            //get the newFirst to the cs firstRank
            //rebuild the location array for the specified start
            int[] startLocation = new int[length];
            for (int i = 0; i < length; i++)
            {
                cs[i].firstRank = newFirst[i];
                startLocation[cs[i].start] = i;
            }
            
            //initial the second part by shifting
            for  (int i = 0; i < length; i++)
            {
                int shiftedStart = cs[i].start+l;
                if (shiftedStart >= length)
                    shiftedStart = shiftedStart-length;
                cs[i].secondRank = newFirst[startLocation[shiftedStart]];
            }
            
            int scanLoc = 0;
            //sort the second part, based on the first part
            while (scanLoc < length - 1)
            {    
                assert firstCount[scanLoc] > 0;
                //when 1 just pass
                if (firstCount[scanLoc] == 1)
                    scanLoc++;
                else if (firstCount[scanLoc] > 1)
                {
                    // radix sort
                    int[] radix = new int[length+1];
                    CircularSuffix[] tempCs = 
                            new CircularSuffix[firstCount[scanLoc]];
                    for (int j = scanLoc; j < scanLoc+firstCount[scanLoc]; j++)
                        radix[cs[j].secondRank+1]++;
                    for (int r = 0; r < length; r++)
                        radix[r+1] += radix[r];
                    for (int j = scanLoc; j < scanLoc+firstCount[scanLoc]; j++)
                        tempCs[radix[cs[j].secondRank]++] = cs[j];
                    for (int j = 0; j < firstCount[scanLoc]; j++)
                        cs[j+scanLoc] = tempCs[j];
                    scanLoc += firstCount[scanLoc];
                }
            }
        }
        
        return cs;
    }

    public int length()
    {
        return this.length;
    }                   
    
    // returns index of ith sorted suffix
    public int index(int i)
    {
        return cs[i].start;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < test.length; i++){
            System.out.println(test.cs[i].toString()+":"+test.index(i));
        }
    }
}