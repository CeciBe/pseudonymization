package pseudo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;


class Scanner {

    public static final char NULL = (char)0;
    public static final char EOF = (char)-1; // End of file.

    private FileInputStream stream = null;
    private InputStreamReader reader = null;
    private char current = NULL;

    public Scanner() {
    }

    public void open(String fileName) throws FileNotFoundException {
        stream = new FileInputStream(fileName);
        reader = new InputStreamReader(stream);
    }

    public char current() {
        return current;
    }


//En metod för att avgöra vilken typ av tagg det är, antingen <Health_Care_Unit> eller <Location>
//När den har avgjort vilken tagg det är kan man tokanizera det som är inom taggen till sluttagg.
//Tanke att dela upp tokenizern i två, en för vardera tagg med respektive kriterier.

    public void moveNext() throws IOException {
        if (reader == null)
            throw new IOException("No open file.");
        if (current != EOF)
            current = (char)reader.read();
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
        if (stream != null)
            stream.close();
    }


}
