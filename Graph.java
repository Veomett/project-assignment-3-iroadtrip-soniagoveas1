import java.util.*;
import static java.lang.Integer.MAX_VALUE;


/**
 * partly Prof. Veomett's implementation with edits to fit reqs + additional code :)
 */
public class Graph {
    public Map<String, Map<String, Integer>> adjacencyList;

   /**
    * creating a new hashmap
    */
   public Graph() {
    adjacencyList = new HashMap<>();
    
   }


   /**
    * @return new arrayList of countries
    */
   public List<String> getCountries() {
        return new ArrayList<>(adjacencyList.keySet());
   }


   /**
    * creates set of elements in hash map
    * @param country: takes in a country
    * @return: array list of keySey from addjacencyList
    */
   public List<String> getNeighbors(String country) {
        if(adjacencyList.containsKey(country)) {
            return new ArrayList<>(adjacencyList.get(country).keySet());
        }

        return new ArrayList<>();
   }


   /**
    * adds edge between two countries with the given distance
    * @param country1: name of the first country
    * @param country2: name of the second country
    * @param distance:distance between the two countries
    */
    public void addEdge(String country1, String country2, int distance) {

        //adding an edge from country1 to country2
        adjacencyList.computeIfAbsent(country1, k -> new HashMap<>()).put(country2, distance);

        //adding an edge from country2 to country1 
        adjacencyList.computeIfAbsent(country2, k -> new HashMap<>()).put(country1, distance);
    }


    /**
     * add directed edge between two countries with a given weight
     * @param source: source country of the directed edge
     * @param dest: destination country of the directed edge
     * @param w: weight of the directed edge between source & destination
     */
    public void addDirEdge(String source, String dest, int w){
       adjacencyList.putIfAbsent(source, new HashMap<>());

       adjacencyList.get(source).put(dest, w);
    }

    
    /**
     * checks if two countries are adjacent
     * countries are adjacent if they are connected by an edge in the graph
     * @param country1: name of the first country
     * @param country2: name of the second country
     * @return: true if the countries are adjacent
     *          false if not adjacent
     *          false if either or both countries do not exist in graph
     */
    public boolean areAdjacent(String country1, String country2) {
        if(adjacencyList.containsKey(country1) && adjacencyList.containsKey(country2)) {
            return adjacencyList.get(country1).containsKey(country2) || adjacencyList.get(country2).containsKey(country1);
        }
        return false;
    }


    /**
     * checks whether the graph contains a country
     * @param country: name of country you want to check if exists in grapj
     * @return: true if country exists in the graph
     *          false if country does not exist
     */
    public boolean hasCountry(String country) {
        return adjacencyList.containsKey(country);
    }


    /**
     * comparing edges by weight
     */
    public class Edge implements Comparable<Edge>{
        Integer source;
        Integer dest;
        int weight;
        Edge(){
            source = null;
            dest = null;
            weight = 0;
        }
        Edge(int v1, int v2, int w){
            source = v1;
            dest = v2;
            weight = w;
        }
        public String toString(){
            return source + ":" + dest + "(" + weight + ") ";
        }
        public int compareTo(Edge e){// needed for Kruskal's algorithm
            return this.weight - e.weight;
        }
    }

    
    public LinkedList<Edge>[] vertexArr;
    public int numVertices;
    
    //helper for dijkstra's algorithm
    public class NodeCost implements Comparable<NodeCost> {
        String node;
        int cost;

        NodeCost(String country, int c) {
            node = country;
            cost = c;
        }

        @Override
        public int compareTo(NodeCost other) {
            return Integer.compare(this.cost, other.cost);
        }
    }


    /**
     * creating a new graph
     * @param n: number of vertices
     */
    Graph(int n) { 
        numVertices = n;
        //vertexArr = new LinkedList[n];
        for (int i = 0; i < numVertices; i++){
            vertexArr[i] = new LinkedList<>();
        }
    }


     /**
     * constructs path from previous nodes map
     * @param prev: map containign previous nodes for each country
     * @param dest: destination country
     * @return: path of countries from source to destination
     */
    public List<String> constructPath(Map<String, String> prev, String dest) {
        List<String> path = new ArrayList<>();
        String curr = dest;

        while(curr != null) {
            path.add(0, curr);
            curr = prev.get(curr);
        }
        return path;
    }

 
    /***
     * finds shortest path from source to destination 
     * in weighted graph
     * @param source: starting node in the path
     * @param dest: destination node to find the shortest path to
     * @return: list of strings representing the shortest path from source to destination
     *          if no path exists, return an empty list
     */         
    public List<String> dijkstra(String source, String dest) {
        PriorityQueue<NodeCost> costMinHeap = new PriorityQueue<>();
        Map<String, Integer> finalCost = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for(String country: adjacencyList.keySet()) {
            if(country.equals(source)) {
                finalCost.put(country, 0);
            } else {
                finalCost.put(country, MAX_VALUE);
            }
            prev.put(country, null);
            costMinHeap.add(new NodeCost(country, finalCost.get(country)));
        }
        
        while(!costMinHeap.isEmpty()) {
            NodeCost currNode = costMinHeap.poll();
            String currCountry = currNode.node;

            if(currCountry.equals(dest)) {
                return constructPath(prev, dest); 
            }

            for(Map.Entry<String, Integer> neighbor: adjacencyList.get(currCountry).entrySet()) {
                String neighborCountry = neighbor.getKey();
                int edgeWeight = neighbor.getValue();
                int newCost = finalCost.get(currCountry) + edgeWeight;

                if(newCost < finalCost.get(neighborCountry)) {
                    finalCost.put(neighborCountry, newCost);
                    prev.put(neighborCountry, currCountry);
                    costMinHeap.add(new NodeCost(neighborCountry, newCost));
                }
            }
         }

         return new ArrayList<>();
    }
}
