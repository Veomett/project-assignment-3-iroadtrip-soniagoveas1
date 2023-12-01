import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<String, Map<String, Integer>> distanceMap;

    public Map<String, Integer> getDistanceMap(String country) {
        if(distanceMap.containsKey(country)) {
            return distanceMap.get(country);
        } else {
            return null;
        }
    }

    public void addNode() {
        
    }

}
