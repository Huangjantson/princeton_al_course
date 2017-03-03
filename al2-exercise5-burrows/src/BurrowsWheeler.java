import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard 
    // input and writing to standard output
    public static void encode()
    {
        StringBuffer buffer = new StringBuffer();
        while (!BinaryStdIn.isEmpty())
        {	
        	char c = BinaryStdIn.readChar();
        	//System.out.println(c);
        	buffer.append(c);
        }
        
        String s = buffer.toString();
        //System.out.println(s);
        char[] result = new char[s.length()];
        CircularSuffixArray csa = new CircularSuffixArray(s);
        
        int first = 0;
        for (int i = 0; i < csa.length(); i++)
        {
            if (csa.index(i) - 1 < 0)
                result[i] = s.charAt(csa.index(i) - 1 + csa.length());
            else 
                result[i] = s.charAt(csa.index(i) - 1);
            if (csa.index(i) == 0)
                first = i;
        }
        
        BinaryStdOut.write(first);;
        
        for (int i = 0; i < csa.length(); i++)
            BinaryStdOut.write(result[i]);
        
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, 
    //reading from standard input and writing to standard output
    public static void decode()
    {    
        //reading first and t[] for the tail chars
        int first = BinaryStdIn.readInt();
        
        StringBuffer buffer = new StringBuffer();
        
        while (!BinaryStdIn.isEmpty())
            buffer.append(BinaryStdIn.readChar());
        int length = buffer.length();
        char[] t = new char[length];
        
        for (int i = 0; i < length; i++)
        {
            t[i] = buffer.charAt(i);
        }
        
        //rebuilding the head by radix sorting
        char[] head = new char[length];
        int[] radix = new int[257];

        for (int j = 0; j < length; j++)
            radix[t[j]+1]++;
        /*
        for (int i = 0; i < 255 ; i++)
        	if (radix[i]!=0)
        		System.out.println((char) i+":"+radix[i]);
        */
        for (int r = 0; r < 256; r++)
            radix[r+1] += radix[r];  
        /*
        for (int i = 0; i < 255 ; i++)
        	if (radix[i]!=0)
        		System.out.print((char) (i-1)+":"+radix[i-1]);
        */
        //get a copy of radix for future use
        int[] start = new int[257];
        for (int i = 0; i < 256; i++)
            start[i] = radix[i];
        
        for (int j = 0; j < length; j++)
            head[radix[t[j]]++] = t[j];

        //generate the next[]
        int[] next =  new int[length];
        for (int i = 0; i < length; i++)
        {
            int previous = start[t[i]];
            next[previous] = i;
            start[t[i]]++;
        }
        /*
        System.out.println(first);        
        for (int i = 0; i < length; i++)
        	System.out.print(t[i]+" ");  
        for (int i = 0; i < length; i++)
        	System.out.print(next[i]+" ");                            
        */
        //restore the original string by next[], first and t[]
        boolean[] check = new boolean[length];
        int checking = first;
        //System.out.println(checking);
        int searched = 0;
        
        while (!check[checking])
        {
            //System.out.println(checking +":" +next[checking] +":"+check[next[checking]]+":"+searched);
            check[checking] = true;
            BinaryStdOut.write(head[checking]);
            if ((check[next[checking]])&&(searched < length-1))
            	checking = next[checking+1];
            else
            	checking = next[checking];
            searched++;
        }
        BinaryStdOut.flush();    
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}