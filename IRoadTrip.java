import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class IRoadTrip {

    private HashMap<String, HashMap<String, Integer>> countryBorders = new HashMap<>();
    private HashMap<String, Integer> capitalDistances = new HashMap<>();
    private HashMap<String, String> stateName = new HashMap<>();

    /**
     * 
     * @param args
     */
    public IRoadTrip (String [] args) {
     
        if(args.length != 3) {
            System.out.println("Must input borders.txt, capdist.csv, and state_name.tsv");
            return;
        }

        readBordersFile(args[0]);
        readCapitalDistancesFile(args[1]);
        readStateNameFile(args[2]);
    }

    /**
     * 
     * @param fileName
     */
    private void readBordersFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line; 
            while((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                String country = parts[0].trim();
                String[] borders = parts[1].split(";");

                HashMap<String, Integer> borderMap = new HashMap<>();
                for(String border: borders) {
                    String[] borderParts = border.trim().split("\\s+");
                    String borderCountry = borderParts[0];
                    int borderLength = Integer.parseInt(borderParts[1]);

                    borderMap.put(borderCountry, borderLength);
                }
                countryBorders.put(country, borderMap);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param fileName
     */
    private void readCapitalDistancesFile(String fileName) {
         try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String countryA = parts[1];
                String countryB = parts[3];
                int distance = Integer.parseInt(parts[4]);

                capitalDistances.put(countryA + "-" + countryB, distance);
                capitalDistances.put(countryB + "-" + countryA, distance);
            } 
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param fileName
     */
    private void readStateNameFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String countryID = parts[1];
                String countryName = parts[2];

                stateName.put(countryName, countryID);
            } 
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int getDistance (String country1, String country2) {
        if(!stateName.containsKey(country1) || !stateName.containsKey(country2)) {
            System.out.println("One or more of these countries do not exist within our database");
            return -1;
        }
        if(!countryBorders.containsKey(country1) || !countryBorders.containsKey(country2)) {
            System.out.println("Inputted countries do not have a shared border");
            return -1;
        }

        String countryCode1 = stateName.get(country1);
        String countryCode2 = stateName.get(country2);
        String key = countryCode1 + "-" + countryCode2;

        if(capitalDistances.containsKey(key)) {
            return capitalDistances.get(key);
        }

        //if no direct or indirect path exists, return -1
        return -1;
    }


    /**
     * 
     * @param country1
     * @param country2
     * @return
     */
    public List<String> findPath (String country1, String country2) {
        if(!stateName.containsKey(country1) || !stateName.containsKey(country2)) {
            System.out.println("One or more of these countries do not exist within our database");
            return new ArrayList<>();
        }

        //creating queue for BFS traversal
        //map storing visited countries
        //set to store visited countries parents
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        //BFS from country1
        queue.add(country1);
        visited.add(country1);

        //country1 will not begin with a parent
        parentMap.put(country1, null);

        while(!queue.isEmpty()) {
            String currCountry = queue.poll();

            if(currCountry.equals(country2)) {
                return constructPath(parentMap, country2);
            }

            if(countryBorders.containsKey(currCountry)) {
                for(String neighbor: countryBorders.get(currCountry).keySet()) {
                    if(!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        parentMap.put(neighbor, currCountry);
                    }
                }
            }
        }

        System.out.println("No path found between " + country1 + " and " + country2);
        return new ArrayList<>();
    }

    /**
     * 
     * @param parentMap
     * @param dest
     * @return
     */
    private List<String> constructPath(Map<String, String> parentMap, String dest) {
        List<String> path = new ArrayList<>();
        String curr = dest;
        while(curr != null) {
            path.add(0, curr);
            curr = parentMap.get(curr);
        }
        return path;
    }

    /**
     * 
     */
    public void acceptUserInput() {
        // Replace with your code
        String input1;
        String input2;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(true) {
                System.out.println("Enter name of the first country (to quit type EXIT)");
                input1 = reader.readLine();
                if(input1.equalsIgnoreCase("EXIT")){
                    break;
                }
                    
                System.out.println("Enter the name of the second country (to quit type EXIT)");
                input2 = reader.readLine();

                if(input2.equalsIgnoreCase("EXIT")){
                     break;
                }
                   
                List<String> path = findPath(input1, input2);
                if(path.isEmpty()) {
                    System.out.println("No path found between " + input1 + " to " + input2); 
                } else {
                    System.out.println("Path from " + input1 + " to " + input2 + ":");
                    for(String step: path) {
                        System.out.println(" " + step);
                    }
                }
            }   
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
            

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        if(args.length!= 3) {
            System.out.println("Must input borders.txt, capdist.csv, and state_name.tsv");
        } else {
            System.out.println("Unable to complete task :( )");
        }
        a3.acceptUserInput();
    }

}