import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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

    private Graph countryGraph;
    private Map<String, String> countryCodesMap;
    public Map<String, String> fixedCountriesMap;

    /**
     * 
     * @param args
     */
    public IRoadTrip (String [] args) {
        countryGraph = new Graph();
        countryCodesMap = new HashMap<String, String>();
        fixedCountriesMap = createFixedCountries();
        
        if(args.length != 3) {
            System.out.println("Must input borders.txt, capdist.csv, and state_name.tsv");
            return;
        }

        readBordersFile(args[0]);
        readCapitalDistancesFile(args[1]);
        readStateNameFile(args[2]);
    }

    /**
     * idea from Prof. Veomett
     * @return fixed countries
     */
    private Map<String, String> createFixedCountries() {
        Map<String, String> fixedCountries = new HashMap<>();
        fixedCountries.put("Bahamas", "Bahamas, The");
        fixedCountries.put("Bosnia-Herzegonia", "Bosnia and Herzegonia");
        fixedCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of the", "Republic of the Congo");
        fixedCountries.put("East Timor", "Timor-Leste");
        fixedCountries.put("Gambia, The", "The Gambia");
        fixedCountries.put("Gambia", "The Gambia");
        fixedCountries.put("German Federal Republic", "Germany");
        fixedCountries.put("Greenland).", "Greenland");
        fixedCountries.put("Italy.", "Italy");
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "Macedonia");
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("Unites States", "United States of America");
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("Zambia.", "Zambia");

        return fixedCountries;
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

               countryGraph.addNode(country);

               for(String border: borders) {
                    String borderCountry = border.trim();
                    countryGraph.addEdge(country, borderCountry);
               }
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

                countryGraph.addDistance(countryA, countryB, distance);
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

               countryCodesMap.put(countryID, countryName);
            } 
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param country1
     * @param country2
     * @return
     */
    public int getDistance(String country1, String country2) {
        Map<String, Integer> distanceFromCountry1 = countryGraph.getDistanceMap(country1);

        if(distanceFromCountry1 != null && distanceFromCountry1.containsKey(country2)) {
            return distanceFromCountry1.get(country2);
        } else {
            return -1;
        }
    }


    /**
     * find path from country 1 to country 2
     * through the capitals of other countries
     * @param country1
     * @param country2
     * @return
     */
    public List<String> findPath(String country1, String country2) {
       Map<String, Integer> distances = new HashMap<>();
       Map<String, String> previous = new HashMap<>();

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