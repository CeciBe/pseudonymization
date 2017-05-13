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
    private boolean isRunning, isFilled, isPseudonymized;


    public Program() {
        pseudonymizer = new Pseudonymizer();
        scan = new Scanner(System.in);
        isPseudonymized = false;
        isFilled = false;
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
                    checkListStatus();
                    break;
                case "3":
                    checkStatus();
                    break;
                case "4":
                    pseudonymizer.printSurrogateList();
                    break;
                case "5":
                    isRunning = false; System.out.println("The program is closing"); closeScanner();
                    return;
                case "6":
                    computeLevDistance();
                    break;
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
        return "1. Copy EPR data \n2. Fill lists \n3. Initiate pseudonymization \n4. View distribution of surrogates" +
                " \n5. Close program \n6. (Temporary option) Test spellchecking! )";
    }


    public void checkListStatus() {
        if(isFilled == false) {
            pseudonymizer.createLists();
            isFilled = true;
        } else {
            System.out.println("\nThe lists are already filled!\n");
        }
    }

    public void checkStatus() {
        if(isPseudonymized == false && isFilled == true) {
            pseudonymize();
        } else if(isPseudonymized == false && isFilled == false) {
            System.out.println("\nThe lists are not filled yet!\n");
        } else {
            System.out.println("\nThe text has already been pseudonymized!\n");
        }
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

            while ((verify = reader.readLine()) != null){
                String patternString1 = "<Health_Care_Unit>\\s*(.+?)\\s*</Health_Care_Unit>";
                String patternString2 = "<Location>\\s*(.+?)\\s*</Location>";

                Pattern pattern1 = Pattern.compile(patternString1);
                Matcher matcher1 = pattern1.matcher(verify);

                Pattern pattern2 = Pattern.compile(patternString2);
                Matcher matcher2 = pattern2.matcher(verify);

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
        isPseudonymized = true;
        printText();
    }


    public void printText() {
        try {
            String verify;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/outputData.txt"), "ISO-8859-1"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/newFile.txt"),"ISO-8859-1"));

            while((verify = reader.readLine()) != null) {
                String patternString = "((<Health_Care_Unit>|<Location>)\\s*(.+?)\\s*(</Health_Care_Unit>|</Location>))";
                Pattern pattern = Pattern.compile(patternString);
                Matcher match = pattern.matcher(verify);
                String finalSentence = verify;
                //System.out.println("BEFORE: "+ finalSentence);   //Testning för att se meningen som inte är pseudonymiserad
                while(match.find()) {
                    String originalString = match.group();
                    String firstTag = match.group(2);
                    String unit = match.group(3);
                    String lastTag = match.group(4);
                    String temp = finalSentence.replaceAll(originalString, pseudonymizer.getSurrogate(firstTag,unit,lastTag));
                    finalSentence = temp;       //Uppdaterar texten som ändrats hittills
                }
                //System.out.println("AFTER: " + finalSentence);   //Testning för att se meningen som är pseudonymiserad
                writer.write(finalSentence);
                writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException ex) {
            System.err.print(ex.getMessage());
        }
        System.out.println("\nThe text is pseudonymized!\n");
    }

    public void computeLevDistance() {
        String one = "Danderyds Sjukhus"; String two = "Danderyds sjukhus";
        int result = computeLevenshteinDistance(one.toLowerCase(),two.toLowerCase());
        System.out.println("Värde 1; " + one + ", Värde 2; " + two + " Distans: "+result);

    }

    public int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newCost = new int[len0];

        for (int i = 0; i < len0; i++) cost[i] = i;

        for (int j = 1; j < len1; j++) {
            newCost[0] = j;

            for(int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newCost[i - 1] + 1;

                newCost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            int[] swap = cost; cost = newCost; newCost = swap;
        }

        return cost[len0 - 1];

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














/*int LevenshteinDistance(String s, String t)
    {
        // degenerate cases
        if (s == t) return 0;
        if (s.length() == 0) return t.length();
        if (t.length() == 0) return s.length();

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++)
            v0[i] = i;

        for (int i = 0; i < s.length(); i++)
        {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < t.length(); j++)
            {
                var cost = (s[i] == t[j]) ? 0 : 1;
                v1[j + 1] = Minimum(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            for (int j = 0; j < v0.length; j++)
                v0[j] = v1[j];
        }

        return v1[t.length()];
    }*/