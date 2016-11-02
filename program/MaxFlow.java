import java.util.*;

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
            if (val == null) {
                adjacencyList.put(src, new ArrayList<Integer>(10));
                val = adjacencyList.get(src);
            }
            val.add(dest);
        }
        printAdjacencyList(adjacencyList);
    }

    public void printAdjacencyList(HashMap<Integer, ArrayList<Integer>> list) {
        for (int i : list.keySet()) {
            System.out.print("Key: " + i);
            ArrayList<Integer> al = list.get(i);
            System.out.println(" Values:");
            for (int val : al) {
                System.out.print(" " + val + " c: " + restCapacity[i][val]);
            }
            System.out.println();
        }
    }

    public MaxFlow() {
        io = new Kattio(System.in, System.out);
        createGraph();
        BFS();
        writeOutput();
    }

    private void writeOutput() {
        //TODO: write output
    }

    private void BFS() {
        int[] parent = new int[numberOfVertices];
        Arrays.fill(parent, -1);
        parent[source] = source;
        int[] m = new int[numberOfVertices]; // Capacity of path to node
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        LOOP:
        while (!queue.isEmpty()) {
            int u = queue.poll();
            ArrayList<Integer> neighbors = adjacencyList.get(u);
            for (int v : neighbors) {
                // There is available capacity and v haven't been visited before
                if (capacity[u][v] > flow[u][v] && parent[v] == -1) {
                    parent[v] = u;
                    m[v] = Math.min(m[u], capacity[u][v] - flow[u][v]);
                    if (v != sink)
                        queue.offer(v);
                    else {
                        // Backtrack search and write flow
                        while (parent[v] != v) {
                            u = parent[v];
                            flow[u][v] += m[sink];
                            flow[v][u] -= m[sink];
                            v = u;
                        }
                        break LOOP;
                    }
                }
            }
        }
        // TODO: if there isn't any path to sink
    }

    public static void main(String[] args) {
        MaxFlow maxFlow = new MaxFlow();
    }
}