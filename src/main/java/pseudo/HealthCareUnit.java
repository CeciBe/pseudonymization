package pseudo;


import java.util.*;

public class HealthCareUnit {

    private HashMap<String,String> hcuMap;

    public HealthCareUnit() {}

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
}
