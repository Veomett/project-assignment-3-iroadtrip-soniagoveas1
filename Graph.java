import java.util.*;
import static java.lang.Integer.MAX_VALUE;


/**
 * partly Prof. Veomett's implementation with edits to fit reqs :)
 */
public class Graph {
    private Map<String, Map<String, Integer>> distanceMap;
    private Map<String, Map<String, Integer>> adjacencyList;
    private NodeCost[] nodeCosts;
    public int n;


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
    * 
    * @param country1
    * @param country2
    * @return: -1 if countries do not share a border
    */
   public int getDistance(String country1, String country2) {
        if(adjacencyList.containsKey(country1) && adjacencyList.get(country1).containsKey(country2)) {
            return adjacencyList.get(country1).get(country2);
        }
        //return is distance cannot be found
        //or countries do not share a border
        return -1;
   }
    

   /**
    * finds all the places distance for other countries
    * @param country
    * @return resulting map, else return null
    */
   public Map<String, Integer> getDistanceMap(String country) {
        if(distanceMap.containsKey(country)) {
            return distanceMap.get(country);
        } else {
            return null;
        }
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


    /***
     * Creates a directed edge from v1 to v2, with weight 1
     * @param v1 vertex labeled with int v1
     * @param v2 vertex labeled with int v2
     */
    public void addDirEdge(String source, String dest, int w){
      //  Edge myEdge = new Edge(v1, v2, w);
       // vertexArr[v1].add(myEdge);
       adjacencyList.putIfAbsent(source, new HashMap<>());

       adjacencyList.get(source).put(dest, w);
    }


    /***
     * tells whether v1 and v2 are adjacent
     * If the graph is directed, returns true if either (v1, v2)
     * or (v2, v1) is an edge
     * @param v1 vertex labeled with int v1
     * @param v2 vertex labeled with int v2
     * @return true if (v1, v2) or (v2, v1) is an edge
     */
    public boolean areAdjacent(int v1, int v2){
        boolean toReturn = false;
        LinkedList<Edge> v1LL = vertexArr[v1];
        Iterator<Edge> v1it = v1LL.iterator();
        Edge currEdge;
        while(v1it.hasNext()){
            currEdge = v1it.next();
            if(currEdge.source == v1 & currEdge.dest == v2 ) {
                toReturn = true;
            }
        }
        LinkedList<Edge> v2LL = vertexArr[v2];
        Iterator<Edge> v2it = v2LL.iterator();
        while(v2it.hasNext()){
            currEdge = v2it.next();
            if(currEdge.source == v2 & currEdge.dest == v1 ) {
                toReturn = true;
            }
        }
        return toReturn;
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

    
    private LinkedList<Edge>[] vertexArr;
    private int numVertices;
    
    //helper for dijkstra's algorithm
    private class NodeCost implements Comparable<NodeCost> {
        int node;
        int cost;
        NodeCost(int n, int c) {
            node=n;
            cost=c;
        };
        @Override
        public int compareTo(NodeCost nc1) {
            return this.cost - nc1.cost;
        }
    }


    /**
     * creating a new graph
     * @param n: number of vertices
     */
    Graph(int n) { 
        numVertices = n;
        //vertexArr = new LinkedList[n];
        nodeCosts = new NodeCost[numVertices];
        for (int i = 0; i < numVertices; i++){
            vertexArr[i] = new LinkedList<>();
            nodeCosts[i] = new NodeCost(i, MAX_VALUE);
        }
    }

 
    /**
     * dijkstra's algo finds shortest path between two nodes
     * one node is the source and this is compared to all the other nodes in graph
     * @param source
     */
    public void dijkstra(int source) {
        PriorityQueue<NodeCost> costMinHeap = new PriorityQueue<>();
        int[] finalCosts = new int[numVertices];
        for (int i=0; i<numVertices; i++){
            if (i == source){
                nodeCosts[source].cost = 0;}
                finalCosts[i] = MAX_VALUE;
                costMinHeap.add(nodeCosts[i]);
        }

        int numFinalized = 0;
        while (numFinalized < numVertices) {
            NodeCost curNode = costMinHeap.remove();
            int curVertex = curNode.node;
            if(finalCosts[curVertex] == MAX_VALUE) {
                finalCosts[curVertex] = curNode.cost;
                numFinalized++;
                Iterator<Edge> it = vertexArr[curVertex].iterator();
                while(it.hasNext()){
                    Edge curEdge = it.next();
                    if((curEdge.weight + curNode.cost) < nodeCosts[curEdge.dest].cost) {
                        nodeCosts[curEdge.dest].cost = curEdge.weight + curNode.cost;
                        NodeCost insertedNode = new NodeCost(curEdge.dest, curEdge.weight + finalCosts[curVertex]);
                        costMinHeap.add(insertedNode);
                    }
                }
            }
        }
        System.out.println(Arrays.toString(finalCosts));
    }
}