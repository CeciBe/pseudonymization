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
                    System.out.println("Maintenance work, please return later!\n");
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
       try {
            String verify, put1, put2;
            FileReader fr = new FileReader("C:/inputData.txt");
            FileWriter fw = new FileWriter("C:/outputData.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader br = new BufferedReader(fr);
            BufferedWriter bw = new BufferedWriter(fw);
            //int c;
            //while((c = fr.read()) != -1) {

            //String c;
            while ((verify = br.readLine()) != null){

                if (verify.contains("Location")) {
                    put1 = verify.replaceAll("Location", "L");
                    bw.write(put1);
                }
                if (verify.contains("Health_Care_Unit")){
                    put2 = verify.replaceAll("Health_Care_Unit", "H");
                    bw.write(put2);
                    }

            }
            br.close();
            bw.close();
            fr.close();
            fw.close();

        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }
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
