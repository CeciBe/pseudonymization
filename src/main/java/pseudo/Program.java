package pseudo;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Program {

    public static void main(String[] args) {
        String inputFile = "C:/inputData.txt";
        String outputFile = "C:/outputData.txt";

        StringBuilder builder = null;
        FileOutputStream stream = null;
        OutputStreamWriter writer = null;

        //Behövs StringBuilder för att bygga texten? Antagligen (om inget annat alternativ är bättre).

        Parser parser = new Parser();
        try {
            try {
                parser.openFile(inputFile);
                builder = new StringBuilder();

                stream = new FileOutputStream(outputFile);
                writer = new OutputStreamWriter(stream);
                writer.write(builder.toString());


            } catch (FileNotFoundException fileEx) {
                System.err.print("Couldn't open file " + fileEx.getMessage());
            }
        }catch (Exception exception){
            System.err.print("Exception: " + exception.getMessage());
        }

    }
}

//"C:/inputData.txt" "C:/outputData.txt"