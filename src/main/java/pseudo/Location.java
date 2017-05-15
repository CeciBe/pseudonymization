package pseudo;


import java.util.*;

public class Location {

    private LevenshteinDistance levDistance = null;
    private ArrayList<HashMap<String,String>> listOfLocations = null;
    private HashMap<String,String> locationMap = null;

    private String currentWord = null;
    private int levDistValue = 0;

    public Location() {
        levDistance = new LevenshteinDistance();
    }

    public HashMap<String,String> getLocationMap() {
        return locationMap;
    }


    public HashMap<String,String> evaluateLocation(String location, ArrayList<HashMap<String,String>> locationList) {
        HashMap<String,String> result = null;
        listOfLocations = locationList;
        result = checkIfAddress(location);
        if (result == null) {
            if(location.length() < 6) {
                result = checkIfAcronym(location);
            }
        }
        if(result == null) {
            result = checkIfCorrectSpelling(location);
        }
        return result;
    }

    public HashMap<String,String> fillHashMap(String location, String output) {
        HashMap<String,String> map = new HashMap<>();
        map.put(location, output);
        return map;
    }

    public HashMap<String, String> checkIfAcronym(String location) {
        HashMap<String, String> result = null;
        result = searchLocation(location, 1);
        return result;
    }

    public HashMap<String, String> checkIfCorrectSpelling(String location) {
        HashMap<String,String> result = null;
        result = searchLocation(location, 2);
        return result;
    }

    public HashMap<String, String> searchLocation(String location, int option) {
        HashMap<String, String> result = null;
        HashMap<String, String> locations;
        for (int index = 0; index < listOfLocations.size(); index++) {
            locations = listOfLocations.get(index);
            if (option == 1) {
                continiueSearchingAcronym(locations, location);
            } else if (option == 2) {
                continiueSearchingLocation(locations, location);
            } else if (option == 3) {
                getAdress(locations, location);
            }
        }
        if(currentWord != null) {
            result = fillHashMap(location, currentWord);
        }
        if(option == 3 && currentWord == null) {
            result = fillHashMap(location,location);
        }
        return result;
    }


    public void continiueSearchingAcronym(HashMap<String, String> locations, String location) {
        boolean response = false; boolean isFound = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: locations.entrySet()) {
            String key = iter.getKey();
            if((key.charAt(0) == location.charAt(0)) && (key.length() > location.length())) {
                tempList.add(key);
            }
        }
        if(!tempList.isEmpty() && location.length() < 4) {
            isFound = compareShortLocations(location, tempList);
            if(isFound == true) {
                response = true;
            }
        }
        if (!tempList.isEmpty() && isFound == false) {
            response = compareLocations(location, tempList);
        }
        if(response == true) {
            locationMap = locations;
        }
    }

    public void continiueSearchingLocation(HashMap<String, String> locations, String location) {
        boolean response = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: locations.entrySet()) {
            String key = iter.getKey();
            if(key.charAt(0) == location.charAt(0)) {
                tempList.add(key);
            }
        }
        if (!tempList.isEmpty()) {
            response = evaluateSpelling(location, tempList);
        }
        if(response == true) {
            locationMap = locations;
        }
    }

    public boolean compareShortLocations(String location, ArrayList<String> tempList) {
        boolean response = false;
        for(int index = 0; index < tempList.size(); index++) {
            String otherLocation = tempList.get(index).toLowerCase();
            String[] locationArray = otherLocation.split(" ");
            if (locationArray.length > 1) {
                int counter = 0;
                for (int i = 0; i < locationArray.length; i++) {
                    if (locationArray[i].charAt(0) == location.toLowerCase().charAt(counter)) {
                        counter++;
                    }
                    if (counter == location.length()) {
                        i = locationArray.length;
                        currentWord = otherLocation;
                        response = true;
                    }
                }
            }
        }
        return response;
    }

    public boolean compareLocations(String location, ArrayList<String> tempList) {

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
                int ld = levDistance.computeLevenshteinDistance(tempLocation.toLowerCase(), tempValue.toLowerCase());
                if(levDistValue > ld || levDistValue == 0) {
                    levDistValue = ld;
                    currentWord = value;
                    response = true;
                }
            }
        }
        return response;
    }

    public boolean evaluateSpelling(String location, ArrayList<String> tempList) {
        boolean response = false;
        String tempLocation = null;
        int limit = 4; int tempLDValue = 0;
        for(int index = 0; index < tempList.size(); index++) {
            String otherLocation  = tempList.get(index);
            int ldValue = levDistance.computeLevenshteinDistance(location.toLowerCase(), otherLocation.toLowerCase());
            if((ldValue < tempLDValue) || (tempLDValue == 0)) {
                tempLDValue = ldValue;
                tempLocation = otherLocation;
            }
        }
        if (((tempLDValue < limit) && (tempLDValue < levDistValue)) || ((tempLDValue < limit) && (levDistValue == 0))) {
            levDistValue = tempLDValue;
            currentWord = tempLocation;
            response = true;
        }
        return response;
    }

    public HashMap<String, String> checkIfAddress(String location) {

        HashMap<String,String> result = null;
        if(location.contains("väg") || location.contains("gata")) {
            result = searchLocation(location, 3);
        }
        return result;
    }

    public void getAdress(HashMap<String, String> locations, String location) {
        DataContainer dataContainer = new DataContainer();
        ArrayList<String> newList = dataContainer.getList(locations);
        if(newList != null) {
            String[] locationArray = location.split(" ");
            for(String otherLocation: newList) {
                String[] otherLocationArray = otherLocation.split(" ");
                int distance = levDistance.computeLevenshteinDistance(locationArray[0].toLowerCase(), otherLocationArray[0].toLowerCase());
                if(distance < 2) {
                    currentWord = otherLocation;
                }
            }
            locationMap = locations;
        }
    }


    class DataContainer {

        DataContainer() {}

        private ArrayList<String> getList(HashMap <String,String> tempMap) {
            ArrayList<String> tempList = new ArrayList<>();
            String value = null;
            for(Map.Entry<String, String> iter: tempMap.entrySet()) {
                String key = iter.getKey();
                value = iter.getValue();
                tempList.add(key);
            } if (value.equals("ADRESS")) {
                return tempList;
            } else {
                return null;
            }
        }
    }
}
