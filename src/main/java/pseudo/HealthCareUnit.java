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
        if(result == null) {
            result = checkIfHospital(hcu);
        }
        if(result == null) {
            result = checkIfNursingHome(hcu);
        }
        if(result == null) {
            result = checkIfClinic(hcu);
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

    public HashMap<String, String> checkIfHospital(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("sjukhus") || tempHCU.contains("sjuk") || tempHCU.contains("lasarett") ||
                (tempHCU.contains("sjh")) || (tempHCU.contains("avd"))) {
            System.out.println("Sjukhus "+hcu);
            result = searchHCU(hcu, 3);
        }
        return result;
    }

    public HashMap<String, String> checkIfNursingHome(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("boende") || tempHCU.contains("sjukhem") || tempHCU.contains("service")) {
            result = searchHCU(hcu, 4);
        }
        return result;
    }

    public HashMap<String, String> checkIfClinic(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("vc") || tempHCU.contains("vårdcentral") || tempHCU.contains("mottagning")) {
            result = searchHCU(hcu, 5);
        }
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
            } else if (option == 3 || option == 4 || option == 5) {
                evaluateDefinedHCU(hcus,hcu,option);
            }
        }
        if(currentWord != null) {
            result = fillHashMap(hcu, currentWord);
        } else if((option == 3 || option == 4 || option == 5) && currentWord == null) {
            result = fillHashMap(hcu,hcu);
        }
        return result;
    }


    public void continiueSearchingAcronym(HashMap<String, String> hcus, String hcu) {
        boolean response = false; boolean isFound = false;
        ArrayList<String> tempList = new ArrayList<>();
        for(Map.Entry<String,String> iter: hcus.entrySet()) {
            String key = iter.getKey();
            if((key.charAt(0) == hcu.charAt(0)) && (key.length() > hcu.length())) {
                tempList.add(key);
            }
        }
        if(!tempList.isEmpty() && hcu.length() < 4) {
            isFound = compareShortHCUs(hcu, tempList);
            if(isFound == true) {
                response = true;
            }
        }
        if (!tempList.isEmpty() && isFound == false) {
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

    public boolean compareShortHCUs(String hcu, ArrayList<String> tempList) {
        boolean response = false;
        for(int index = 0; index < tempList.size(); index++) {
            String otherHCU = tempList.get(index).toLowerCase();
            String[] locationArray = otherHCU.split(" ");
            if (locationArray.length > 1) {
                int counter = 0;
                for (int i = 0; i < locationArray.length; i++) {
                    if (locationArray[i].charAt(0) == hcu.toLowerCase().charAt(counter)) {
                        counter++;
                    }
                    if (counter == hcu.length()) {
                        i = locationArray.length;
                        currentWord = otherHCU;
                        response = true;
                    }
                }
            }
        }
        return response;
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

    //3 = sjukhus, 4 = boende, 5 = vårdcentral
    public void evaluateDefinedHCU(HashMap<String, String> hcuValues, String hcu, int option) {
        DataContainer dataContainer = new DataContainer();
        ArrayList<String> list = dataContainer.getList(hcuValues, option);
        if(list != null) {
            if(option == 3) {
                evaluateHospital(hcu,list);
            } else if(option == 4) {
                evaluateNursingHome(hcu,list);
            } else if(option == 5) {
                evaluateClinic(hcu,list);
            }
            hcuMap = hcuValues;
        }
    }

    public void evaluateHospital(String hospital, ArrayList<String> list) {
        String tempHospital = hospital.toLowerCase();
        if(tempHospital.contains(".")) {
            tempHospital = tempHospital.replace(".", " ");
        }
        if(tempHospital.contains("sjh")) {
            tempHospital = tempHospital.replaceAll("sjh", "sjukhus");
        }
        String[] hospitalArray = tempHospital.split(" ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = list.get(index);
            int counter = 0;
            for(int i = 0; i < hospitalArray.length; i++) {
                if(otherHCU.toLowerCase().contains(hospitalArray[i])) {
                    counter++;
                }
            }
            if(counter == hospitalArray.length) {
                currentWord = otherHCU;
            }
        }
    }

    public void evaluateNursingHome(String nursingHome, ArrayList<String> list) {
        String tempNursingHome = nursingHome.toLowerCase();
        if(tempNursingHome.contains(".")) {
            tempNursingHome = tempNursingHome.replace(".", " ");
        }
        String[] nursHomArray = tempNursingHome.split(" ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = list.get(index);
            int counter = 0;
            for(int i = 0; i < nursHomArray.length; i++) {
                if(otherHCU.toLowerCase().contains(nursHomArray[i])) {
                    counter++;
                }
            }
            if(counter == nursHomArray.length) {
                currentWord = otherHCU;
            }
        }
    }

    public void evaluateClinic(String clinic, ArrayList<String> list) {
        String tempClinic = clinic.toLowerCase();
        if(tempClinic.contains(".")) {
            tempClinic = tempClinic.replace(".", " ");
        }
        if(tempClinic.contains("vc")) {
            tempClinic = tempClinic.replaceAll("vc", "vårdcentral");
        }
        String[] clinicArray = tempClinic.split( " ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = list.get(index);
            int counter = 0;
            for(int i = 0; i < clinicArray.length; i++) {
                if(otherHCU.toLowerCase().contains(clinicArray[i])) {
                    counter++;
                }
            }
            if(counter == clinicArray.length) {
                currentWord = otherHCU;
            }
        }
    }

    class DataContainer {

        DataContainer() {}

        private ArrayList<String> getList(HashMap<String, String> tempMap, int option) {
            ArrayList<String> tempList = new ArrayList<>();
            String value = null;
            String category = getCategory(option);
            for(Map.Entry<String, String> iter: tempMap.entrySet()) {
                String key = iter.getKey();
                value = iter.getValue();
                tempList.add(key);
            } if (value.equals(category)) {
                return tempList;
            } else {
                return null;
            }
        }

        private String getCategory(int option) {
            String category = null;
            switch (option) {
                case 3: category = "SJUKHUS";
                        break;
                case 4: category = "BOENDEN";
                        break;
                case 5: category = "VÅRDCENTRAL";
                        break;
            }
            return category;
        }
    }
}
