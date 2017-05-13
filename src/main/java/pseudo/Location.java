package pseudo;


import java.util.*;

public class Location {

    private LevenshteinDistance levDistance = null;
    private ArrayList<HashMap<String,String>> listOfLocations = null;
    private HashMap<String,String> locationMap = null;

    private String currentWord = null;
    private String currentCategory = null;
    private int levDistValue = 0;

    public Location() {
        levDistance = new LevenshteinDistance();
    }

    public HashMap<String,String> getLocationMap() {
        return locationMap;
    }


    public HashMap<String,String> evaluateLocation(String location, ArrayList<HashMap<String,String>> locationList) {
        HashMap<String,String> result = null;
        String output = null;
        listOfLocations = locationList;
        if(location.length() < 6) {
            output = checkIfAcronym(location);
        } if(output != null) {
            result = new HashMap<>();
            result.put(location, output);
        }
        return result;
    }

    public String checkIfAcronym(String location) {
        String result = null;
        HashMap<String, String> locations;
        for (int index = 0; index < listOfLocations.size(); index++) {
            locations = listOfLocations.get(index);
            continiueCheckAcronym(locations,location);
        }
        if(currentWord != null) {
            result = currentWord;
        }
        return result;
    }

    public void continiueCheckAcronym(HashMap<String, String> locations, String location) {
        String value = null;
        boolean response = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: locations.entrySet()) {
            String key = iter.getKey();
            value = iter.getValue();
            if((key.charAt(0) == location.charAt(0)) && (key.length() > location.length())) {
                tempList.add(key);
            }
        }
        if (!tempList.isEmpty()) {
            response = compareLocations(location, value, tempList);
            if(response == true) {
                locationMap = locations;
            }
        }
    }

    public boolean compareLocations(String location, String category, ArrayList<String> tempList) {

        boolean response = false;
        String tempLocation = location.toLowerCase().replaceAll(" ", "");

        for (int index = 0; index < tempList.size(); index++) {
            String value = tempList.get(index);
            String tempValue = value.toLowerCase().replaceAll(" ", "");
            int counter = 0; int i = 0;
            while(i < tempValue.length() && counter < tempLocation.length()) {
                if (tempLocation.charAt(counter) == tempValue.charAt(i)) {
                    counter++;
                }
                i++;
            }
            if(counter == tempLocation.length()) {
                //System.out.println("För "+tempLocation+", Funnit: " + tempValue);
                int ld = levDistance.computeLevenshteinDistance(tempLocation.toLowerCase(), tempValue.toLowerCase());
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
        //computeLevenshteinDistance();
    }

    public void checkIfAdress() {

    }
}
