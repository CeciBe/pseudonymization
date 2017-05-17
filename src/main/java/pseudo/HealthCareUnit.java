package pseudo;


import java.util.*;

public class HealthCareUnit {

    private LevenshteinDistance levDistance = null;
    private ArrayList<HashMap<String,String>> listOfHCUs = null;
    private HashMap<String,String> hcuMap = null;

    private String currentWord = null, mostProbableWord = null;
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
            result = checkIfClinic(hcu);
        }
        if(result == null) {
            result = checkIfEmergency(hcu);
        }
        if(result == null) {
            result = checkIfEndocrine(hcu); //Tar bort neuro, medicin
        }
        if(result == null) {
            result = checkIfSurgery(hcu);
        }
        if(result == null) {
            result = checkIfGeriatrics(hcu);    //geriatrik, sjukhem och palliativ
        }
        if(result == null) {
            result = checkIfDental(hcu);
        }
        if(result == null) {
            result = checkIfNursingHome(hcu);
        }
        if (result == null) {
            result = checkIfRehab(hcu);
        }
        if(result == null) {
            result = checkIfFarmacy(hcu);
        }
        if(result == null) {
            result = checkIfMedicine(hcu);
        }
        if(result == null) {
            //result = checkIfDept(hcu);
        }
        return result;
    }

    public String preprocessData(String hcu) {
        String result = null;
        if(hcu.contains(".")) {
            hcu = hcu.replace(".", " ");
        }
        if(hcu.contains("/")) {
            hcu = hcu.replace("/", " ");
        }
        if(hcu.contains(",")) {
            hcu = hcu.replace(",", " ");
        }
        if(hcu.contains(":")) {
            hcu = hcu.replace(":", " ");
        }
        if(hcu.contains("-")) {
            hcu = hcu.replace("-", " ");
        }
        result = hcu.trim();
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

        if(tempHCU.contains("sjukh") || tempHCU.contains("lasaret") || (tempHCU.contains("sjh")) || (tempHCU.contains("avd"))) {
            result = searchHCU(hcu, 3);
        }
        return result;
    }

    public HashMap<String,String> checkIfFarmacy(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();
        if(tempHCU.contains("apotek")) {
            result = searchHCU(hcu,13);
        }
        return result;
    }

    public HashMap<String,String> checkIfEmergency(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();
        if(tempHCU.contains("akut") || tempHCU.contains("intensiv")) {
            result = searchHCU(hcu,9);
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

    /***public HashMap<String,String> checkIfDept(String hcu) {
        String tempHCU = preprocessData(hcu.toLowerCase());
        tempHCU = tempHCU.replace(" ")
        if(tempHCU.contains("avd") || tempHCU.length())
    }***/

    public HashMap<String, String> checkIfClinic(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("vc") || tempHCU.contains("vårdcentral") || tempHCU.contains("mottagning")) {
            result = searchHCU(hcu, 5);
        }
        return result;
    }

    public HashMap<String, String> checkIfEndocrine(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("endokrin") || tempHCU.contains("diabetes") || tempHCU.contains("njur") ||
                tempHCU.contains("dialys") || tempHCU.contains("urolog")) {
            result = searchHCU(hcu, 6);
        }
        return result;
    }

    public HashMap<String,String> checkIfDental(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("tand") || tempHCU.contains("oral") || tempHCU.contains("käk") ||
                tempHCU.contains("endodonti") || tempHCU.contains("odontologi") || tempHCU.contains("dental")) {
            result = searchHCU(hcu, 10);
        }
        return result;
    }

    public HashMap<String, String> checkIfSurgery(String hcu) {
        HashMap<String,String> result = null;
        String tempSurgery = hcu.toLowerCase();

        if(tempSurgery.contains("kirurg") || tempSurgery.contains("operat")) {
            result = searchHCU(hcu, 7);
        }
        return result;
    }

    public HashMap<String, String> checkIfRehab(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();

        if(tempHCU.contains("rehab")) {
            result = searchHCU(hcu, 12);
        }
        return result;
    }

    public HashMap<String, String> checkIfGeriatrics(String hcu) {
        HashMap<String,String> result = null;
        String tempGer = hcu.toLowerCase();

        if(tempGer.contains("geri") || tempGer.contains("operat") || tempGer.contains("sjukhem") ||
                tempGer.contains("asih") || tempGer.contains("palliativ") || tempGer.contains("hospice")) {
            result = searchHCU(hcu, 8);
        }
        return result;
    }

    public HashMap<String,String> checkIfMedicine(String hcu) {
        HashMap<String,String> result = null;
        String tempHCU = hcu.toLowerCase();
        result = searchHCU(hcu, 11);
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
            } else {
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
            response = checkFirstPartOfSpelling(hcu, tempList);
            if(response == false) {
                response = evaluateSpelling(hcu, tempList);
            }
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

    public boolean checkFirstPartOfSpelling(String hcu, ArrayList<String> tempList) {
        boolean response = false;
        hcu = preprocessData(hcu).toLowerCase();
        for(String otherHCU: tempList) {
            String otherValue = preprocessData(otherHCU).toLowerCase();
            if (otherValue.contains(hcu)) {
                mostProbableWord = otherHCU;
                response = true;
                if(currentWord == null || currentWord.length() > mostProbableWord.length()) {
                    currentWord = mostProbableWord;
                }
            }
        }
        return response;
    }

    public void evaluateDefinedHCU(HashMap<String, String> hcuValues, String hcu, int option) {
        DataContainer dataContainer = new DataContainer();
        ArrayList<String> list = dataContainer.getList(hcuValues, option);
        if(list != null) {
            if(option == 3) {
                evaluateHospital(hcu,list);
            } else if(option == 5) {
                evaluateClinic(hcu,list);
            } else {
                evaluateOtherHCU(hcu,list);
            }
            hcuMap = hcuValues;
        }
    }

    public void evaluateHospital(String hospital, ArrayList<String> list) {
        String tempHospital = preprocessData(hospital).toLowerCase();
        if(tempHospital.contains("sjh")) {
            tempHospital = tempHospital.replaceAll("sjh", "sjukhus");
        }
        String[] hospitalArray = tempHospital.split(" ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = preprocessData(list.get(index).toLowerCase());
            int counter = 0;
            for(int i = 0; i < hospitalArray.length; i++) {
                if(otherHCU.contains(hospitalArray[i])) {
                    counter++;
                }
            }
            if(counter == hospitalArray.length) {
                currentWord = otherHCU;
            }
        }
    }

    public void evaluateOtherHCU(String hcu, ArrayList<String> list) {
        String tempHCU = preprocessData(hcu).toLowerCase();
        String[] hcuArray = tempHCU.split(" ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = preprocessData(list.get(index).toLowerCase());
            int counter = 0;
            for(int i = 0; i < hcuArray.length; i++) {
                if(otherHCU.contains(hcuArray[i])) {
                    counter++;
                }
            }
            if(counter == hcuArray.length) {
                currentWord = otherHCU;
            }
        }
    }

    public void evaluateClinic(String clinic, ArrayList<String> list) {
        String tempClinic = preprocessData(clinic).toLowerCase();
        if(tempClinic.contains("vc")) {
            tempClinic = tempClinic.replaceAll("vc", "vårdcentral");
        }
        String[] clinicArray = tempClinic.split( " ");
        for(int index = 0; index < list.size(); index++) {
            String otherHCU = preprocessData(list.get(index).toLowerCase());
            int counter = 0;
            for(int i = 0; i < clinicArray.length; i++) {
                if(otherHCU.contains(clinicArray[i])) {
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
                case 6: category = "NJURENDOKRIN";
                        break;
                case 7: category = "KIRURGI";
                        break;
                case 8: category = "GERIATRIK";
                        break;
                case 9: category = "AKUT";
                        break;
                case 10: category = "TAND";
                        break;
                case 11: category = "MEDICIN";
                        break;
                case 12: category = "REHAB";
                        break;
                case 13: category = "APOTEK";
                        break;
                case 14: category = "AVDELNING";
                        break;
            }
            return category;
        }
    }
}
