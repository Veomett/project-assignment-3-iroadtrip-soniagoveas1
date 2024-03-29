import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class IRoadTrip {

    public Graph countryGraph;
    public Map<String, String> countryCodesMap;
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
            return;
    
        }

        //reading each of the inputted files
        readBordersFile(args[0]);
        readCapitalDistancesFile(args[1]);
        readStateNameFile(args[2]);
    }


    /**
     * idea from Prof. Veomett
     * @return fixed countries
     * names are changed allow them to be recognized as the same country
     */
    public Map<String, String> createFixedCountries() {
        Map<String, String> fixedCountries = new HashMap<>();

        fixedCountries.put("Bahamas", "Bahamas, The");
        fixedCountries.put("Belarus (Byeloreussia)", "Belarus");
        fixedCountries.put("Bosnia-Herzegonia", "Bosnia and Herzegonia");
        fixedCountries.put("Botswana.", "Botswana");
        fixedCountries.put("Burkina Faso (Upper Volta)", "Burkina Faso");
        fixedCountries.put("Cape Verde", "Cabo Verde");
        fixedCountries.put("Cambodia (Kampuchea)", "Cambodia");                          
        fixedCountries.put("Congo", "Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of the", "Republic of the Congo");
        fixedCountries.put("Congo, Republic of the", "Republic of the Congo");
        fixedCountries.put("East Timor", "Timor-Leste");
        fixedCountries.put("Gambia, The", "The Gambia");
        fixedCountries.put("Gambia", "The Gambia");
        fixedCountries.put("German Federal Republic", "Germany");
        fixedCountries.put("Greenland).", "Greenland");
        fixedCountries.put("Italy.", "Italy");
        fixedCountries.put("Italy/Sardinia", "Italy");
        fixedCountries.put("Iran (Persia)", "Iran");
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Korea, People's Republic Of", "North Korea");
        fixedCountries.put("Korea, South", "South Korea");
        fixedCountries.put("Korea, Republic of", "South Korea");
        fixedCountries.put("Kyrgyz Republic", "Kyrgystan");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "Macedonia");
        fixedCountries.put("Myanmar (Burma)", "Burma");
        fixedCountries.put("Romania", "Rumania");
        fixedCountries.put("Russia (Soviet Union)", "Russia");
        fixedCountries.put("Sri Lanka (Ceylon)", "Sri Lanka");
        fixedCountries.put("Tanzania/Tanganyika", "Tanzania");
        fixedCountries.put("Turkey (Ottomon Empire)", "Turkey");
        fixedCountries.put("Turkey (Turkiye)", "Turkey");
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("UAE", "United Arab Emirates");
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("Unites States", "United States of America");
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("Vietnam, Democratic Republic of", "Vietnam");
        fixedCountries.put("Yemen (Arab Republic of Yemen)", "Yemen");
        fixedCountries.put("Zambia.", "Zambia");
        fixedCountries.put("Zimbabwe (Rhodesia)", "Zimbabwe");
    
        return fixedCountries;
    }


    /**
     * reading info from border file
     * finds the diff parts 
     * before equal is name of source country
     * other countries are separated by ';'
     * @param fileName: input of file w/ bordering countries
     */
    public void readBordersFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line; 
            while((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                String country = parts[0].trim();
                String[] borders = parts[1].split("\\d+ km;|\\d+ km|\\d+,\\d+ km;?|\\d+.\\d+ km;?");

               for(String border: borders) {
                    String borderCountry = border.trim();
                    countryGraph.addDirEdge(country, borderCountry, 1);
               }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * reads capdist.csv file
     * takes capital distances and adds into country graph
     * each edge represents the distance between the countries
     * @param fileName: input of file w/ distances
     */
    public void readCapitalDistancesFile(String fileName) {
         try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String countryA = parts[1];
                String countryB = parts[3];
                int distance = Integer.parseInt(parts[4]);

                countryGraph.addEdge(countryA, countryB, distance);
                } 
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * reads state names and IDs from state_name.tsv
     * creates map with the country IDs as keys  & country names as values
     * countries will only be added if they currently exist
     * so their end date is 2020-12-31
     * @param fileName: input of file w/ state names & IDs
     */
    public void readStateNameFile(String fileName) {
        LocalDate targetEndDate = LocalDate.of(2020, 12, 31);

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String countryID = parts[1];
                String countryName = parts[2];
                LocalDate endDate = LocalDate.parse(parts[4]);

                if(endDate.isEqual(targetEndDate)) {
                    countryCodesMap.put(countryName, countryID);
                }
               
            } 
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * get the distance between two countries
     * @param country1: name of first country
     * @param country2: name of second country
     * @return: the distance between the two countries
     *          else returns -1 if distance is not available
     *          or if the countries do not share a border
     */
    public int getDistance(String country1, String country2) {
        //checks if both exist
        if(countryGraph.hasCountry(country1) && countryGraph.hasCountry(country2) && countryGraph.areAdjacent(country1, country2)) {
           return countryGraph.adjacencyList.get(country1).get(country2);
       }
        return -1;
    }
    

    /**
     * find path from country 1 to country 2
     * through the capitals of other countries
     * @param country1: starting country
     * @param country2: destination country
     * @return: list of shortest path between the two countries
     */
    public List<String> findPath(String country1, String country2) {

        //initializing data structures
       Map<String, Integer> distances = new HashMap<>();
       Map<String, String> previous = new HashMap<>();
       PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        //initializing distances & prev nodes for both countries
       for(String country: countryGraph.getCountries()) {
            distances.put(country, Integer.MAX_VALUE);
            previous.put(country, null);
       } 

       //set starting country distance to 0
       distances.put(country1, 0);

       //add starting country to priority queue
       queue.add(country1);

       while(!queue.isEmpty()) {
            String currCountry = queue.poll();

            //breaks when destination is reached
            if(currCountry.equals(country2)) {
                break;
            }

            List<String> neighbors = countryGraph.getNeighbors(currCountry);

            for(String neighbor: neighbors) {
                int dist = distances.get(currCountry) + getDistance(currCountry, neighbor);

                if(dist < distances.get(neighbor)) {
                    distances.put(neighbor, dist);
                    previous.put(neighbor, currCountry);
                    queue.add(neighbor);
                }

            }
       }
        //constructing + returning the path
         if(!previous.containsKey(country2) || previous.get(country2) == null) {
            return new ArrayList<>();
        } else {
            return countryGraph.constructPath(previous, country2);
       }
   
    }

    
    /**
     * reads user input for two countries
     * finds path between them 
     * exits when "EXIT" is entered by user
     */
    public void acceptUserInput() {
        //initializing variables for user input of the two countries
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
                            //finds the path between the two countries   
                //printing :)
                List<String> path = findPath(input1, input2);
                if(path.isEmpty()) {
                    System.out.println("No path found between " + input1 + " to " + input2); 
                } else {
                    System.out.println("Route from " + input1 + " to " + input2);
                    for(int i = 0; i < path.size() - 1; i ++){
                        int distance = getDistance(path.get(i), path.get(i + 1));
                        System.out.println("* " + path.get(i) + " --> " + path.get(i + 1) + " (" + distance + " km)" );
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
        IRoadTrip roadTrip = new IRoadTrip(args);

        if(args.length!= 3) {
            System.out.println("Must input borders.txt, capdist.csv, and state_name.tsv");
        } else {
            System.out.println("Unable to complete task :( )");
        }
        roadTrip.acceptUserInput();
    }
}