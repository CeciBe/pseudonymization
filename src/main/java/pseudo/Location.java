package pseudo;


import java.util.*;

public class Location {

    private ArrayList<HashMap<String,String>> listOfLocations;
    private HashMap<String,String> locationMap;

    public Location() {}

    public HashMap<String,String> getLocationMap() {
        return locationMap;
    }

    public String evaluateLocation(String location, ArrayList<HashMap<String,String>> locationList) {
        String result = null;
        listOfLocations = locationList;
        System.out.println("Location: " + location);
        if(location.length() < 6) {
            //result = checkIfAcronym(location);
        }
        System.out.println("Result: "+result);
        return result;
    }

    public String checkIfAcronym(String location) {
        String result = null;
        HashMap<String, String> locations;
        char[] locationOne = location.toCharArray();
        for (int index = 0; index < listOfLocations.size(); index++) {
            locations = listOfLocations.get(index);
            result = continiueCheckAcronym(locations,locationOne);
            if(result != null) {
                index = listOfLocations.size();
                locationMap = locations;
            }
        }
        return result;
    }

    public String continiueCheckAcronym(HashMap<String, String> locationMap, char[] locationOne) {
        String result = null;
        for(Map.Entry<String,String> iter: locationMap.entrySet()) {
            String key = iter.getKey();
            //String value = iter.getValue();
            if(key.charAt(0) == locationOne[0]) {
                System.out.println("Ja" + locationOne + ", key: " + key);
                char[] locationTwo = key.toCharArray();
                if(key.length() > locationOne.length) {
                    boolean match = compareLocations(locationOne, locationTwo);
                    if(match == true) {
                        result = key;
                    }
                }
            }
        }
        return result;
    }

    public boolean compareLocations(char[] locationOne, char[] locationTwo) {
        int counter = 0;
        for (int index = 0; index < locationTwo.length; index++){
            System.out.println(counter);
            if(locationOne[counter] == locationTwo[index]) {
                counter++;
            }
        }
        if (counter == locationOne.length) {
            return true;
        } else {
            return false;
        }
    }

    public void checkIfCorrectSpelling() {
        //computeLevenshteinDistance();
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

    public void checkIfAdress() {

    }
}
