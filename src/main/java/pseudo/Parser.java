package pseudo;

//import java.util.*;

import java.io.FileNotFoundException;

class Parser {

    Scanner scan;

    Parser() {
        scan = new Scanner();
    }

    public void openFile(String inputFile) throws FileNotFoundException {
        scan.open(inputFile);
    }

}
