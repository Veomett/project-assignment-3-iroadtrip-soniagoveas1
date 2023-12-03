import java.util.*;
import static java.lang.Integer.MAX_VALUE;


/**
 * largely Prof. Veomett's implementation
 */
public class Graph {
    private Map<String, Map<String, Integer>> distanceMap;
   // private LinkedList<Edge>[] vertexArr;
    public int n;
    private int[] setArr;
   // private int numVertices;

    public Map<String, Integer> getDistanceMap(String country) {
        if(distanceMap.containsKey(country)) {
            return distanceMap.get(country);
        } else {
            return null;
        }
    }

   /***
     * Creates a symmetric edge.  For non-directed graph
     * @param v1 vertex labeled with int v1
     * @param v2 vertex labeled with int v1
     * @param weight is the weight of undirected edge (v1, v2)
     */
    public void addEdge(int v1, int v2, int weight){
        Edge v1Edge = new Edge(v1, v2, weight);
        vertexArr[v1].add(v1Edge);
        Edge v2Edge = new Edge(v2, v1, weight);
        vertexArr[v2].add(v2Edge);
    }

    /***
     * Creates a directed edge from v1 to v2, with weight 1
     * @param v1 vertex labeled with int v1
     * @param v2 vertex labeled with int v2
     */
    public void addDirEdge(int v1, int v2, int w){
        Edge myEdge = new Edge(v1, v2, w);
        vertexArr[v1].add(myEdge);
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
    // This is used for Dijkstra's algorithm
    private class NodeCost implements Comparable<NodeCost>{
        int node;
        int cost;
        NodeCost(int n, int c){
            node=n;
            cost=c;
        };
        @Override
        public int compareTo(NodeCost nc1)
        {
            return this.cost - nc1.cost;
        }
    }

    private NodeCost[] nodeCosts;

    Graph(int n){ // declares graph with n vertices
        numVertices = n;
        vertexArr = new LinkedList[n];
        nodeCosts = new NodeCost[numVertices];
        for (int i = 0; i < numVertices; i++){
            vertexArr[i] = new LinkedList<>();
            nodeCosts[i] = new NodeCost(i, MAX_VALUE);
        }
    }


    /**
     * 
     * @param source
     */
    public void dijkstra(int source){
        PriorityQueue<NodeCost> costMinHeap = new PriorityQueue<>();
        int[] finalCosts = new int[numVertices];
        for (int i=0; i<numVertices; i++){
            if (i == source){
                nodeCosts[source].cost = 0;}
                finalCosts[i] = MAX_VALUE;
                costMinHeap.add(nodeCosts[i]);
        }
        int num_finalized = 0;

        while (num_finalized < numVertices){
            NodeCost curNode = costMinHeap.remove();
            int curVertex = curNode.node;
            if (finalCosts[curVertex] == MAX_VALUE){
                finalCosts[curVertex] = curNode.cost;
                num_finalized++;
                Iterator<Edge> it = vertexArr[curVertex].iterator();
                while(it.hasNext()){
                    Edge curEdge = it.next();
                    if ((curEdge.weight + curNode.cost) < nodeCosts[curEdge.dest].cost) {
                        nodeCosts[curEdge.dest].cost = curEdge.weight + curNode.cost;
                        //Note: we're adding a new NodeCost b/c Java's priority queue doesn't allow us to update values :-(
                        NodeCost insertedNode = new NodeCost(curEdge.dest, curEdge.weight + finalCosts[curVertex]);
                        costMinHeap.add(insertedNode);
                    }
                }
            }
        }
        System.out.println(Arrays.toString(finalCosts));
    }
}
