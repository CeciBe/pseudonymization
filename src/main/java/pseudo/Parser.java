package pseudo;

//import java.util.*;

import java.io.*;

class Parser {

    //Scanner scan;

    private Tokenizer tokenizer = null;


    Parser() {
        //scan = new Scanner();
        tokenizer = new Tokenizer();
    }

    public void openFile(String inputFile) throws TokenizerException, IOException {
        tokenizer.open(inputFile);
        tokenizer.moveNext();
    }

    public void close() throws IOException {
        if (tokenizer != null) {
            tokenizer.close();
        }
    }

}
