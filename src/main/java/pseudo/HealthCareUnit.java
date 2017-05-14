package pseudo;


import java.util.*;

public class HealthCareUnit {

    private LevenshteinDistance levDistance = null;
    private ArrayList<HashMap<String,String>> listOfHCUs = null;
    private HashMap<String,String> hcuMap = null;

    private String currentWord = null;
    private int levDistValue = 0;

    public HealthCareUnit() {
        levDistance = new LevenshteinDistance();
    }

    public HashMap<String,String> getHcuMap() {
        return hcuMap;
    }

    public HashMap<String,String> evaluateHCU(String hcu, ArrayList<HashMap<String,String>> hcuList) {
        HashMap<String,String> result = null;
        listOfHCUs = hcuList;
        if(hcu.length() < 6) {
            result = checkIfAcronym(hcu);
        }
        if(result == null) {
            result = checkIfCorrectSpelling(hcu);
        }
        return result;
    }

    public HashMap<String,String> fillHashMap(String hcu, String output) {
        HashMap<String,String> map = new HashMap<>();
        map.put(hcu, output);
        return map;
    }

    public HashMap<String, String> checkIfAcronym(String hcu) {
        HashMap<String, String> result = null;
        result = searchHCU(hcu, 1);
        return result;
    }

    public HashMap<String, String> checkIfCorrectSpelling(String hcu) {
        HashMap<String,String> result = null;
        result = searchHCU(hcu, 2);
        return result;
    }

    public HashMap<String, String> searchHCU(String hcu, int option) {
        HashMap<String, String> result = null;
        HashMap<String, String> hcus;
        for (int index = 0; index < listOfHCUs.size(); index++) {
            hcus = listOfHCUs.get(index);
            if (option == 1) {
                continiueSearchingAcronym(hcus, hcu);
            } else if (option == 2) {
                continiueSearchingHCU(hcus, hcu);
            }
        }
        if(currentWord != null) {
            result = fillHashMap(hcu, currentWord);
        }
        return result;
    }


    public void continiueSearchingAcronym(HashMap<String, String> hcus, String hcu) {
        boolean response = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: hcus.entrySet()) {
            String key = iter.getKey();
            if((key.charAt(0) == hcu.charAt(0)) && (key.length() > hcu.length())) {
                tempList.add(key);
            }
        }
        if (!tempList.isEmpty()) {
            response = compareHCUs(hcu, tempList);
        }
        if(response == true) {
            hcuMap = hcus;
        }
    }

    public void continiueSearchingHCU(HashMap<String, String> hcus, String hcu) {
        boolean response = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: hcus.entrySet()) {
            String key = iter.getKey();
            if(key.charAt(0) == hcu.charAt(0)) {
                tempList.add(key);
            }
        }
        if (!tempList.isEmpty()) {
            response = evaluateSpelling(hcu, tempList);
        }
        if(response == true) {
            hcuMap = hcus;
        }
    }

    public boolean compareHCUs(String hcu, ArrayList<String> tempList) {

        boolean response = false;
        String tempHCU = hcu.toLowerCase().replaceAll(" ", "");

        for (int index = 0; index < tempList.size(); index++) {
            String value = tempList.get(index);
            String tempValue = value.toLowerCase().replaceAll(" ", "");
            int counter = 0; int i = 0;
            while(i < tempValue.length() && counter < tempHCU.length()) {
                if (tempHCU.charAt(counter) == tempValue.charAt(i)) {
                    counter++;
                }
                i++;
            }
            if(counter == tempHCU.length()) {
                int ld = levDistance.computeLevenshteinDistance(tempHCU.toLowerCase(), tempValue.toLowerCase());
                if(levDistValue > ld || levDistValue == 0) {
                    levDistValue = ld;
                    currentWord = value;
                    response = true;
                }
            }
        }
        return response;
    }

    public boolean evaluateSpelling(String hcu, ArrayList<String> tempList) {
        boolean response = false;
        String tempHCU = null;
        int limit = 4; int tempLDValue = 0;
        for(int index = 0; index < tempList.size(); index++) {
            String otherHCU  = tempList.get(index);
            int ldValue = levDistance.computeLevenshteinDistance(hcu.toLowerCase(), otherHCU.toLowerCase());
            if((ldValue < tempLDValue) || (tempLDValue == 0)) {
                tempLDValue = ldValue;
                tempHCU = otherHCU;
            }
        }
        if (((tempLDValue < limit) && (tempLDValue < levDistValue)) || ((tempLDValue < limit) && (levDistValue == 0))) {
            levDistValue = tempLDValue;
            currentWord = tempHCU;
            response = true;
        }
        return response;
    }
}
