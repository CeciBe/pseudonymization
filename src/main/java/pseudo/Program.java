package pseudo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;

/**OBS! Nedanstående två rader ska vi avvakta med då det har med weka att göra!**/
//import weka.core.Instances;         //För datainsamlingen
//import weka.filters.Filter;         //För att preprocessa data


public class Program {

    private Scanner scan = null;
    private boolean isRunning;
    private ArrayList<String> list = new ArrayList<>();


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
                case "4":
                    for(String s : list) {
                        //System.out.println("Så många strängar i listan: " + list.size());
                        System.out.println(s);
                    }
                    System.out.println("Så många strängar i listan: " + list.size());

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

            //HashMap<String, String> map = new HashMap<>();

            String verify, put1, put2, op;

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:/outputData.txt"), "ISO-8859-1"));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/newFile.txt"),"ISO-8859-1"));


            while ((verify = reader.readLine()) != null){
                int numberOfTokens = 0;
                StringTokenizer st = new StringTokenizer(verify);
                StringBuilder sb = new StringBuilder();
                String currentToken = "";
                System.out.println("Före: " + verify);

                System.out.println("Efter: ");

                //while (st.hasMoreTokens()) {
                    //currentToken = st.nextToken();


                    String patternString = "<Location>\\s*(.+?)\\s*</Location>";

                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(verify);

                    int count = 0;
                    while(matcher.find()) {
                        count++;
                        System.out.println("found: " + count + " : "
                                + matcher.start() + " - " + matcher.end());

                    }

                    writer.write(verify.replaceAll(patternString, "<Location>Stockholm</Location>"));


                    /*if (currentToken.contains("<Location>")) {
                        System.out.println("Nuvarande token: " + currentToken);
                        //put1 = String.valueOf(sb.append(currentToken).append(" "));

                        if (currentToken.contains("</Location>")) {
                            //list.add(currentToken);
                            put1 = valueOf(sb.append(currentToken).append("-"));
                            //list.add(put1);
                            //writer.write(verify.replaceAll(put1, "<Location>Stockholm</Location>"));
                            System.out.println("Nuvarande token i listan: " + currentToken);
                            //continue;
                        } else {
                            put1 = valueOf(sb.append(currentToken).append(" "));
                            currentToken = st.nextToken();
                            if (currentToken.contains("</Location>")) {
                                put1 = valueOf(sb.append(currentToken).append("-"));
                                //list.add(put1);
                                //writer.write(verify.replaceAll(put1, "<Location>Stockholm</Location>"));
                                System.out.println("Nuvarande kortare sträng: " + put1);
                                //continue;

                                for(String s : put1.split("-")) {
                                    System.out.println("Splittad sträng: " + s);
                                    list.add(s.trim());
                                    //writer.write(verify.replaceAll(s, "<Location>Stockholm</Location>"));
                                }
                            } else {
                                while (!currentToken.contains("</Location>")) {
                                    put1 = valueOf(sb.append(currentToken).append(" "));
                                    currentToken = st.nextToken();

                                    if (currentToken.contains("</Location>")) {
                                        put1 = valueOf(sb.append(currentToken).append("-"));
                                        //list.add(put1);
                                        //writer.write(verify.replaceAll(put1, "<Location>Stockholm</Location>"));
                                        //currentToken = st.nextToken();
                                        //put1 = String.valueOf(sb.append(currentToken).append(" "));
                                        System.out.println("Nuvarande sträng om inte är slut: " + put1);

                                        //continue;
                                        //} else {
                                        //put1 = String.valueOf(sb.append(currentToken).append(" "));
                                        //list.add(put1);

                                        //System.out.println("Nuvarande sträng om inte är slut: " + put1);
                                        //map.put(verify);
                                        //list.spliterator();



                                    }
                                }
                            }*/
                            //put1 = verify.replaceAll(String.valueOf(sb.append("<Location>" + "</Location>")), String.valueOf(sb.append("<Location>" + "Stockholm" + "</Location>")));
                            //put1 = verify.replaceAll("Location", "L");
                           // writer.write(verify.replaceAll(put1, "<Location>Stockholm</Location> "));
                            //currentToken = st.nextToken();
                            //}
                            //String[] items = put1.split("> <");
                            //List<String> list = Arrays.asList(items);

                            //System.out.println(list);
                            //for(String s : put1.split("> <")) {
                                //System.out.println("Splittad sträng: " + s);
                            //}
                           /*for(String s : put1.split("-")) {
                                System.out.println("Splittad sträng: " + s);
                                list.add(s.trim());
                                //writer.write(verify.replaceAll(s, "<Location>Stockholm</Location>"));
                            }*/

                        //}
                        //writer.write(verify.replaceAll(put1, "<Location>Stockholm</Location>"));
                        //list.add(put1);
                        //System.out.println("Listan innehåller: " + put1.length());
                        //verify.replaceAll(put1, "<Location>Stockholm</Location>");
                        //writer.write(verify.replaceAll(String.valueOf(list), "<Location>Stockholm</Location>"));
                      /*  for(String s : put1.split("-")) {
                            System.out.println("Splittad sträng: " + s);
                            list.add(s.trim());
                            writer.write(verify.replaceAll(s, "<Location>Stockholm</Location>"));

                        }*/
                    //}


                    /*if (verify.contains("<Health_Care_Unit>")) { //&& verify.endsWith("</Health_Care_Unit>")){
                        //list.add(verify);
                        put2 = verify.replaceAll("Health_Care_Unit", "H");
                        writer.write(put2);

                    }*/
                    //writer.write(verify);
                    //writer.write(verify.replaceAll(String.valueOf(verify.contains("<Location>")), "<Location>Stockholm</Location>"));

                    //numberOfTokens++;
                    //writer.write(verify.replaceAll(String.valueOf(verify.contains("<Location>")), "<Location>Stockholm</Location>"));

                //}
                System.out.println("Total Tokens: "+ numberOfTokens);

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
