package pseudo;


import java.io.FileNotFoundException;

public class Program {

    public static void main(String[] args) {
        String inputFile = "C:/inputData.txt";
        String outputFile = "C:/outputData.txt";

        //Behövs StringBuilder för att bygga texten? Antagligen (om inget annat alternativ är bättre).

        Parser parser = new Parser();
        try {
            parser.openFile(inputFile);
        } catch (FileNotFoundException fileEx) {
            System.err.print("Couldn't open file");
        }

    }
}

//"C:/inputData.txt" "C:/outputData.txt"