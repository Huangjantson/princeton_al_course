import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.In;


public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard 
    // input and writing to standard output
    public static void encode()
    {
        In in = new In();
        StringBuffer buffer = new StringBuffer();
        while (in.hasNextChar())
            buffer.append(in.readChar());
        
        String s = buffer.toString();
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
        
        BinaryStdOut.write(first);
        
        for (int i = 0; i < csa.length(); i++)
            //System.out.print(result[i]);
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
        int[] radix = new int[256];

        for (int j = 0; j < length; j++)
            radix[t[j]+1]++;
        for (int r = 0; r < 255; r++)
            radix[r+1] += radix[r];        
        
        //get a copy of radix for future use
        int[] start = new int[256];
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
        
        //restore the original string by next[], first and t[]
        boolean[] check = new boolean[length];
        int checking = first;
        while (!check[checking])
        {
            check[checking] = true;
            BinaryStdOut.write(head[checking]);
            checking = next[checking];
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