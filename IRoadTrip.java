import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class IRoadTrip {
    private HashMap<String, HashMap<String, Integer>> countryBorders = new HashMap<>();
    private HashMap<String, Integer> capitalDistances = new HashMap<>();
    private HashMap<String, Integer> stateName = new HashMap<>();

    public IRoadTrip (String [] args) {
        // Replace with your code
        if(args.length != 3) {
            System.out.println("Must input borders.txt, capdist.csv, and state_name.tsv");
            return;
        }

        readBordersFile(args[0]);
        readCapitalDistancesFile(args[1]);
        readStateNameFile(args[2]);
    }

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

    private void readCapitalDistancesFile(String fileName) {
        try(BufferedReader br = BufferedReader(new FileReader(fileName))) {
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
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public void acceptUserInput() {
        // Replace with your code
        String input1;
        String input2;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(true) {
                System.out.println("Enter name of the first country (to quit type EXIT)");
                input1 = reader.readLine();
                if(input1.equalsIgnoreCase("EXIT"));
                    break;
            }

            input2 = reader.readLine();
            System.out.println("Enter the name of the second country (to quit typer EXIT)");
            if(input2.equalsIgnoreCase("EXIT"));
                break;
        }
      //  System.out.println("IRoadTrip - skeleton");

        List<String> path = filePath(input1, input2);
        if(path.isEmpty()) {
            System.out.println("No path found from " + input1 + " to " + input2);
            for(String step: path) {
                System.System.out.println(step);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}



