package pseudo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;


public class Program {

    private Pseudonymizer pseudonymizer = null;
    private Scanner scan = null;
    private boolean isRunning;


    public Program() {
        pseudonymizer = new Pseudonymizer();
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
                    pseudonymizer.createLists();
                    break;
                case "3":
                    pseudonymize();
                    break;
                case "4":
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
        return "1. Copy EPR data \n2. Fill lists \n3. Initiate pseudonymization \n4. Close program";
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


    public void pseudonymize() {
        try {

            String verify;

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/outputData.txt"), "ISO-8859-1"));
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/newFile.txt"),"ISO-8859-1"));


            while ((verify = reader.readLine()) != null){

                String patternString1 = "<Health_Care_Unit>\\s*(.+?)\\s*</Health_Care_Unit>";
                String patternString2 = "<Location>\\s*(.+?)\\s*</Location>";

                Pattern pattern1 = Pattern.compile(patternString1);
                Matcher matcher1 = pattern1.matcher(verify);

                Pattern pattern2 = Pattern.compile(patternString2);
                Matcher matcher2 = pattern2.matcher(verify);


                //TODO, angående nedanstående loopar, vi måste se till att innehållet som hämtas ska hanteras på något sätt

                while (matcher1.find()) {
                    String h_c_u_Unit = matcher1.group(1);
                    pseudonymizer.pseudonymizeData(h_c_u_Unit, "Health_Care_Unit");
                }

                while (matcher2.find()) {
                    String locationUnit = matcher2.group(1);
                    pseudonymizer.pseudonymizeData(locationUnit, "Location");
                }
            }
            reader.close();
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
