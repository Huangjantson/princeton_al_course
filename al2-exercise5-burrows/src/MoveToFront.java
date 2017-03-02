import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class MoveToFront {
    
    // apply move-to-front encoding, 
    //reading from standard input and writing to standard output
    public static void encode()
    {
        char[] alphabet = new char[256];
        for (int each = 0; each < 256; each++)
            alphabet[each] = (char) each;
        In in = new In();
        while (in.hasNextChar())
        {
            //find the index for the char
            int id = 0;
            char temp = in.readChar();
            
            for (int i = 0; i < 256; i++)
                if (alphabet[i] == temp)
                {    id = i;
                    break;
                }
            //write the index for the char
            BinaryStdOut.write((char) id);
            
            
            //move the indexs for the chars
            char exchange = alphabet[id];

            for (int i = 0; i < id + 1; i++)
            {
                temp = alphabet[i];
                alphabet[i] = exchange;
                exchange = temp;
            }
            
        }
        
        BinaryStdOut.close();
        in.close();
    }

    // apply move-to-front decoding, 
    //reading from standard input and writing to standard output
    public static void decode()
    {
        char[] alphabet = new char[256];
        for (int each = 0; each < 256; each++)
            alphabet[each] = (char) each;
        
        Out out = new Out();
        
        while (!BinaryStdIn.isEmpty())
        {
            //input the id for the char
            int id = (int) BinaryStdIn.readChar();
            
            //write char with the index
            out.print(alphabet[id]);
            
            //move the indexs for the chars
            char exchange = alphabet[id];
            char temp;
            for (int i = 0; i < id + 1; i++)
            {
                temp = alphabet[i];
                alphabet[i] = exchange;
                exchange = temp;
            }
        }
        
        BinaryStdIn.close();
        out.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {

        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}