import java.util.ArrayList;
import java.util.HashMap;

public class MaxFlow {

    private int numberOfVertices;
    private int numberOfEdges;
    private int source;
    private int sink;
    private Kattio io;

    private int[][] capacity = new int[2000][2000];
    private int[][] restCapacity = new int[2000][2000];
    private int[][] flow = new int[2000][2000];
    private HashMap<Integer, ArrayList<Integer>> adjacencyList;

    private class Node {
        public Node previous;
        public boolean visited;

        public Node() {

        }
    }

    public void createGraph() {
        numberOfVertices = io.getInt();
        source = io.getInt();
        sink = io.getInt();
        numberOfEdges = io.getInt();
        adjacencyList = new HashMap<Integer, ArrayList<Integer>>(5000);

        for (int i = 0; i < numberOfEdges; i++) {
            int src = io.getInt();
            int dest = io.getInt();
            int cap = io.getInt();
            capacity[src][dest] = cap;
            restCapacity[src][dest] = cap;
            ArrayList val = adjacencyList.get(src);
            if(val == null) {
                adjacencyList.put(src, new ArrayList<Integer>(10));
                val = adjacencyList.get(src);
            }
            val.add(dest);
        }
        printAdjacencyList(adjacencyList);
    }

    public void printAdjacencyList(HashMap<Integer, ArrayList<Integer>> list){
        for(int i: list.keySet()){
            System.out.print("Key: " + i);
            ArrayList<Integer> al = list.get(i);
            System.out.println(" Values:");
            for(int val : al){
                System.out.print(" " + val + " c: " + restCapacity[i][val]);
            }
            System.out.println();
        }
    }

    public MaxFlow() {
        io = new Kattio(System.in, System.out);
        createGraph();
    }

    public static void main(String[] args) {
        MaxFlow maxFlow = new MaxFlow();
    }
}