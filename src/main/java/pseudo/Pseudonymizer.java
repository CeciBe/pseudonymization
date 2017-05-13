package pseudo;

import java.io.*;
import java.util.*;


public class Pseudonymizer {

    private String locationListLink = "C:/LocationList.txt";
    private String hcuListLink = "C:/HCUList.txt";
    private String currentWord = null;
    private TempData tempObject = new TempData();

    private ArrayList <HashMap <String,String>> locationList = new ArrayList<>();
    private ArrayList <HashMap <String,String>> hcuList = new ArrayList<>();
    private HashMap <String, HashSet<String>> surrogates = new HashMap<>();         //<Surrogat,Ordinarie>
    private HashMap <String, String> pseudonymizedData = new HashMap<>();           //<Ordinarie,Surrogat>


    public Pseudonymizer() {}


    public void createLists() {
        String line;
        try {
            BufferedReader locationReader = new BufferedReader(new InputStreamReader(new FileInputStream(locationListLink), "ISO-8859-1"));
            while((line = locationReader.readLine()) != null) {
                HashMap <String,String> tempMap = new HashMap<>();
                String category = line;
                while(((line = locationReader.readLine()) != null) && !(line.equals("LOCATIONTYPE:"))) {
                    String locationUnit = line;
                    tempMap.put(locationUnit, category);
                }
                locationList.add(tempMap);
            }
            locationReader.close();
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
        continiueCreatingLists();
    }

    public void continiueCreatingLists() {
        String line;
        try {
            BufferedReader hcuReader = new BufferedReader(new InputStreamReader(new FileInputStream(hcuListLink), "ISO-8859-1"));
            while((line = hcuReader.readLine()) != null) {
                HashMap <String,String> tempMap = new HashMap<>();
                String category = line;
                while(((line = hcuReader.readLine()) != null) && !(line.equals("HCUTYPE:"))) {
                    String hcu = line;
                    tempMap.put(hcu, category);
                }
                hcuList.add(tempMap);
            }
            hcuReader.close();
        } catch (IOException ioEx) {
            System.err.println(ioEx.getMessage());
        }
        System.out.println("\nNew lists are created!\n");
    }


    public void pseudonymizeData(String data, String category) {
        boolean isFound = false;
        HashMap<String, String> tempMap = null;
        if(category.equals("Location")) {
            for (int index = 0; index < locationList.size(); index++) {
                tempMap = locationList.get(index);
                String categoryText = tempMap.get(data);
                if(categoryText != null) {
                    index = locationList.size();                //Denna rad är för att stoppa loopen!
                    isFound = true;
                }
            }
        } else if(category.equals("Health_Care_Unit")) {
            for(int index = 0; index < hcuList.size(); index++) {
                tempMap = hcuList.get(index);
                String categoryText = tempMap.get(data);
                if(categoryText != null) {
                    index = locationList.size();                //Denna rad är för att stoppa loopen!
                    isFound = true;
                }
            }
        }
        if (isFound == true) {
            continiuePseudonymization(data, tempMap, false);
        } else {
            boolean status = evaluateIfLocationOrHCU(data,category);
            if(status == false) {
                handleUnlistedValues(data, "ÖVRIGA VÄRDEN");
            }
        }
    }


    public boolean evaluateIfLocationOrHCU(String data, String category) {
        boolean status = false;
        Location location = new Location();
        HealthCareUnit healthCareUnit = new HealthCareUnit();
        HashMap<String, String> tempMap = null;
        HashMap<String,String> result = null;
        if (category.equals("Location")) {
            tempMap = location.evaluateLocation(data, locationList);
        } else if (category.equals("Health_Care_Unit")) {
            tempMap = healthCareUnit.evaluateHCU(data, hcuList);
        }
        if (tempMap != null) {
            String value = tempMap.get(data);
            if(category.equals("Location")) {
                result = location.getLocationMap();
            }else if(category.equals("Health_Care_Unit")) {
                result = healthCareUnit.getHcuMap();
            }
            if(result != null) {
                currentWord = data;
                System.out.println("Current word "+currentWord);
                continiuePseudonymization(value, result,true);
                status = true;
            }
        }
        return status;
    }


    public void continiuePseudonymization(String data, HashMap<String, String> tempMap, boolean anotherValue) {
        ArrayList <String> newList = null;
        newList = tempObject.getTemporaryList(tempMap);
        generateSurrogate(data,newList,anotherValue);
    }

    public void generateSurrogate(String value, ArrayList <String> newList, boolean anotherValue) {
        String surrogate = pseudonymizedData.get(value);        //Kollar om det finns ett surrogat sedan tidigare
        if(surrogate == null) {
            checkIfValidSurrogate(value, newList,anotherValue);
        } else if (anotherValue == true && surrogate != null) {
            pseudonymizedData.put(currentWord, surrogate);
            HashSet<String> tempList = surrogates.get(surrogate);
            tempList.add(currentWord);
            currentWord = null;
            //System.out.println("Surrogate already exists for "+value +", "+surrogate);
        }
    }

    public void checkIfValidSurrogate(String value, ArrayList <String> newList, boolean anotherValue) {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(newList.size() - 1);
        String newSurrogate = getSurrogateFromList(randomIndex, newList);
        if (newSurrogate.equals(value)) {
            checkIfValidSurrogate(value, newList,anotherValue);
            return;
        }
        HashSet<String> list = surrogates.get(newSurrogate);                //Kontrollerar om surrogatet använts tidigare
        if (list == null) {
            list = new HashSet<>();
            list.add(value);
            pseudonymizedData.put(value, newSurrogate);
            if(anotherValue == true) {
                pseudonymizedData.put(currentWord, newSurrogate);
                list.add(currentWord);
                currentWord = null;
            }
            surrogates.put(newSurrogate, list);
        } else {
            checkIfValidSurrogate(value, newList,anotherValue);
        }
    }


    public String getSurrogateFromList(int randomIndex, ArrayList <String> newList) {
        String surrogate = null;
        for(int counter = 0; counter <= randomIndex; counter++) {
            surrogate = newList.get(counter);
        }
        return surrogate;
    }

    class TempData {

        TempData() {}

        private ArrayList<String> getTemporaryList(HashMap <String,String> tempMap) {
            ArrayList<String> tempList = new ArrayList<>();
            for(Map.Entry<String, String> iter: tempMap.entrySet()) {
                String key = iter.getKey();
                tempList.add(key);
            }
            return tempList;
        }

        private ArrayList<String> switchValues(String category, HashMap<String, String> tempMap) {
            String value = null;
            ArrayList<String> tempList = new ArrayList<>();
            for(Map.Entry<String,String> iter: tempMap.entrySet()) {
                String key = iter.getKey();
                tempList.add(key);
                value = iter.getValue();
            }
            if(!category.equals(value)) {
                tempList = null;
            }
            return tempList;
        }
    }

    public void handleUnlistedValues(String input, String category) {
        HashMap <String, String> tempMap = null;
        ArrayList<String> list = null;
        for (int i = 0; i < locationList.size(); i++) {
            tempMap = locationList.get(i);
            list = tempObject.switchValues(category, tempMap);
            if(list != null) {
                i = locationList.size();
            }
        }
        generateSurrogate(input, list, false);
    }

    public String getSurrogate(String firstTag, String unit, String lastTag) {
        String surrogate = null;
        String newUnit = pseudonymizedData.get(unit);
        surrogate = firstTag + newUnit + lastTag;
        return surrogate;
    }

    public void printSurrogateList() {
        for(Map.Entry<String, HashSet<String>> iter: surrogates.entrySet()) {
            String key = iter.getKey();
            HashSet<String> valueList = iter.getValue();
            System.out.print("\n - " + key + ": " + valueList + "\n");
        }
        //System.out.println("\n"+pseudonymizedData);
    }
}
