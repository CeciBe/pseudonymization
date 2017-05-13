package pseudo;


import java.util.*;

public class HealthCareUnit {

    private LevenshteinDistance levDistance = null;
    private HashMap<String,String> hcuMap;

    public HealthCareUnit() {
        levDistance = new LevenshteinDistance();
    }

    public HashMap<String,String> getHcuMap() {
        return hcuMap;
    }

    public void evaluateHCU(String hcu, ArrayList<HashMap<String,String>> hcuList) {
        //System.out.println(hcu);
    }

    public void checkIfAcronym() {

    }

    public void checkIfCorrectSpelling() {

    }
}
