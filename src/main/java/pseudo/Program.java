package pseudo;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class Program {

    public static void main(String[] args) {
        String inputFile = "C:/inputData.txt";
        String outputFile = "C:/outputData.txt";

        StringBuilder builder = null;
        FileOutputStream stream = null;
        OutputStreamWriter writer = null;

        //Behövs StringBuilder för att bygga texten? Antagligen (om inget annat alternativ är bättre).

        Parser parser = null; //new Parser();
        Scanner scan = null;

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            try {
                scan = new Scanner();
                parser = new Parser();
                parser.openFile(inputFile);
                //builder = new StringBuilder();

                //builder.append(inputFile);

                //stream = new FileOutputStream(outputFile);
                //writer = new OutputStreamWriter(stream);
                //writer.write(builder.toString());

                sourceChannel = new FileInputStream(inputFile).getChannel();
                destChannel = new FileOutputStream(outputFile).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            } catch (FileNotFoundException fileEx) {
                System.err.print("Couldn't open file " + fileEx.getMessage());
            }
            finally {
                //if (parser != null)
                    //parser.close();
                if (scan != null)
                    scan.close();
                if (writer != null)
                    writer.close();
                if (stream != null)
                    stream.close();
            }

        }catch (Exception exception){
            System.err.print("Exception: " + exception.getMessage());
        }

    }
}

//"C:/inputData.txt" "C:/outputData.txt"