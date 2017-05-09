package pseudo;

import java.io.*;
import java.util.*;

public class Pseudonymizer {

    private String locationListLink = "C:/LocationList.txt";
    private String hcuListLink = "C:/HCUList.txt";
    private TempData tempObject = new TempData();

    private ArrayList <HashMap <String,String>> locationList = new ArrayList<>();
    private ArrayList <HashMap <String,String>> hcuList = new ArrayList<>();
    private HashMap <String, String> pseudonymizedData = new HashMap<>();

    //private ArrayList <String> listOfDefalutValues = new ArrayList<>();     //För värden som inte går att kategorisera
    //private HashMap <String, String> newDefaultValues = new HashMap<>();


    public Pseudonymizer() {}


    public void createLists() {
        BufferedReader locationReader = null;
        String line;
        try {
            locationReader = new BufferedReader(new InputStreamReader(new FileInputStream(locationListLink), "ISO-8859-1"));
            while((line = locationReader.readLine()) != null) {
                HashMap <String,String> tempMap = new HashMap<>();
                String category = line;
                while(((line = locationReader.readLine()) != null) && !(line.equals("LOCATIONTYPE:"))) {
                    String input = line;
                    tempMap.put(input, category);
                }
                locationList.add(tempMap);
            }
            locationReader.close();
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
        continiueCreatingLists();
        System.out.println("\nNew lists are created!\n");
    }

    public void continiueCreatingLists() {
        BufferedReader hcuReader = null;
        String line;
        try {
            hcuReader = new BufferedReader(new InputStreamReader(new FileInputStream(hcuListLink), "ISO-8859-1"));
            while((line = hcuReader.readLine()) != null) {
                HashMap <String,String> tempMap = new HashMap<>();
                String category = line;
                while(((line = hcuReader.readLine()) != null) && !(line.equals("HCUTYPE:"))) {
                    String input = line;
                    tempMap.put(input, category);
                }
                hcuList.add(tempMap);
            }
            hcuReader.close();
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
    }


    public void pseudonymizeData(String inputData, String category) {

        boolean isFound = false;
        HashMap<String, String> tempMap = null;
        if(category.equals("Location")) {
            for (int index = 0; index < locationList.size(); index++) {
                tempMap = locationList.get(index);
                String categoryText = tempMap.get(inputData);
                if(categoryText != null) {
                    index = locationList.size();
                    isFound = true;
                }
            }
        } else if(category.equals("Health_Care_Unit")) {
            for(int index = 0; index < hcuList.size(); index++) {
                tempMap = hcuList.get(index);
                String categoryText = tempMap.get(inputData);
                if(categoryText != null) {
                    index = locationList.size();
                    isFound = true;
                }
            }
        }
        if (isFound == true) {
            continiuePseudonymization(inputData,category, tempMap);
        }
    }

    public void continiuePseudonymization(String inputData, String category, HashMap<String, String> tempMap) {
        ArrayList <String> newList = null;
        if(category.equals("Location")) {
            newList = tempObject.getTemporaryList(tempMap);
        } else if(category.equals("Health_Care_Unit")) {
            newList = tempObject.getTemporaryList(tempMap);
        }
        generateSurrogate(inputData,newList);
    }

    public void generateSurrogate(String inputData, ArrayList <String> newList) {

        System.out.println(inputData + " " + newList);
    }

    class TempData {

        TempData() {
        }

        public ArrayList<String> getTemporaryList(HashMap<String, String> tempMap) {
            ArrayList<String> tempList = new ArrayList<>();
            for(Map.Entry<String, String> iter: tempMap.entrySet()) {
                    String key = iter.getKey();
                    tempList.add(key);
            }
            return tempList;
        }
    }
}







































//Nedanstående text används ej för tillfället



    /*private HashMap <Character, ArrayList<String>> länder = new HashMap<>();
    private HashMap <Character, ArrayList<String>> landskap = new HashMap<>();
    private HashMap <Character, ArrayList<String>> landsDelar = new HashMap<>();
    private HashMap <Character, ArrayList<String>> län = new HashMap<>();*/



/*** ArrayList<String> tempList = LocationMap.get(firstChar);
 if(tempList == null) {
 tempList = new ArrayList<>();
 tempList.add(line);
 //LocationMap.put(firstChar, tempList);
 } else {
 tempList.add(line);
 }***/



/*public void createLists() {
        BufferedReader locationReader = null;
        String line; String currentCategory; int counter = 0;
        try {
            locationReader = new BufferedReader(new InputStreamReader(new FileInputStream(locationListLink), "ISO-8859-1"));

            while(((line = locationReader.readLine()) != null) && !(line.equals("LOCATIONTYPE:")))  {
                char firstChar = line.charAt(0);
                ArrayList<String> tempList = LocationMap.get(firstChar);
                if(tempList == null) {
                    tempList = new ArrayList<>();
                    tempList.add(line);
                    //LocationMap.put(firstChar, tempList);
                } else {
                    tempList.add(line);
                }
            }
           // System.out.println(countries);

            locationReader.close();
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
    }*/