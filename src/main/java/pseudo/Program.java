package pseudo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**OBS! Nedanstående två rader ska vi avvakta med då det har med weka att göra!**/
//import weka.core.Instances;         //För datainsamlingen
//import weka.filters.Filter;         //För att preprocessa data


public class Program {

    private Scanner scan = null;
    private boolean isRunning;

    public Program() {
        scan = new Scanner(System.in);
    }

    public void run(boolean condition) {
        isRunning = condition;
        System.out.println(initiate());
        while(isRunning) {
            System.out.println(printMenu());
            switch(commando()) {
                case "1":
                    copyData();
                    break;
                case "2":
                    pseudonymize();
                    //System.out.println("Maintenance work, please return later!\n");
                    break;
                case "3":
                    isRunning = false; System.out.println("The program is closing"); closeScanner();
                    return;
                default:
                    System.out.println("The option is not valid try again!\n");
            }
        }
    }

    public String readText(String text) {
        System.out.print(text);
        return scan.nextLine();
    }

    public String commando() {
        return readText("> ").toLowerCase();
    }

    public String initiate() {
        return "The program is initiated\n";
    }

    public String printMenu() {
        return "1. Copy EPR data \n2. Initiate pseudonymization \n3. Close program";
    }



    public void copyData() {

        String inputFile = "C:/inputData.txt";
        String outputFile = "C:/outputData.txt";

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            try {
                sourceChannel = new FileInputStream(inputFile).getChannel();
                destChannel = new FileOutputStream(outputFile).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            } catch (FileNotFoundException fileEx) {
                System.err.print("Couldn't open file " + fileEx.getMessage());
            }
            finally {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (destChannel != null) {
                    destChannel.close();
                }
            }

        }catch (Exception exception){
            System.err.print("Exception: " + exception.getMessage());
        }
        System.out.println("\nA copy of the file is created!\n");
    }


    public void pseudonymize(){

       try {
           String verify, put1, put2;

           BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/outputData.txt"), "ISO-8859-1"));

           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/newFile.txt"),"ISO-8859-1"));

           // Always wrap FileReader in BufferedReader.

            while ((verify = reader.readLine()) != null){

                if (verify.contains("Location")) {
                    put1 = verify.replaceAll("Location", "L");
                    writer.write(put1);
                }
                if (verify.contains("Health_Care_Unit")){
                    put2 = verify.replaceAll("Health_Care_Unit", "H");
                    writer.write(put2);
                    }

            }
           reader.close();
           writer.close();


        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public void closeScanner() {
        if(scan != null) {
            scan.close();
        }
    }

    public static void main(String[] args) {

        Program program = new Program();
        program.run(true);

    }
}

//"C:/inputData.txt" "C:/outputData.txt"
