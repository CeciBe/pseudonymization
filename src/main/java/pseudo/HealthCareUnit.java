package pseudo;


import java.util.*;

public class HealthCareUnit {

    private LevenshteinDistance levDistance = null;
    private ArrayList<HashMap<String,String>> listOfHCUs = null;
    private HashMap<String,String> hcuMap = null;

    private String currentWord = null;
    private String currentCategory = null;
    private int levDistValue = 0;

    public HealthCareUnit() {
        levDistance = new LevenshteinDistance();
    }

    public HashMap<String,String> getHcuMap() {
        return hcuMap;
    }

    public HashMap<String,String> evaluateHCU(String hcu, ArrayList<HashMap<String,String>> hcuList) {
        HashMap<String,String> result = null;
        String output = null;
        listOfHCUs = hcuList;
        if(hcu.length() < 6) {
            output = checkIfAcronym(hcu);
        } if(output != null) {
            result = new HashMap<>();
            result.put(hcu, output);
        }
        return result;
    }

    public String checkIfAcronym(String hcu) {
        String result = null;
        HashMap<String, String> hcus;
        for (int index = 0; index < listOfHCUs.size(); index++) {
            hcus = listOfHCUs.get(index);
            continiueCheckAcronym(hcus,hcu);
        }
        if(currentWord != null) {
            result = currentWord;
        }
        return result;
    }

    public void continiueCheckAcronym(HashMap<String, String> hcus, String hcu) {
        String value = null;
        boolean response = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: hcus.entrySet()) {
            String key = iter.getKey();
            value = iter.getValue();
            if((key.charAt(0) == hcu.charAt(0)) && (key.length() > hcu.length())) {
                tempList.add(key);
            }
        }
        if (!tempList.isEmpty()) {
            response = compareLocations(hcu, value, tempList);
            if(response == true) {
                hcuMap = hcus;
            }
        }
    }

    public boolean compareLocations(String hcu, String category, ArrayList<String> tempList) {

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
                //System.out.println("För "+tempHCU+", Funnit: " + tempValue);
                int ld = levDistance.computeLevenshteinDistance(tempHCU.toLowerCase(), tempValue.toLowerCase());
                if(levDistValue > ld || levDistValue == 0) {
                    levDistValue = ld;
                    currentWord = value;
                    currentCategory = category;
                    response = true;
                }
            }
        }
        return response;
    }

    public void checkIfCorrectSpelling() {

    }
}
