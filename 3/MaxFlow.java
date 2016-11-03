import java.util.*;

public class MaxFlow {

    private int numberOfVertices;
    private int numberOfEdges;
    private int source;
    private int sink;
    private int flowEdges;

    private int[][] capacity = new int[2001][2001];
    private int[][] flow = new int[2001][2001];
    private HashMap<Integer, ArrayList<Integer>> adjacencyList = 
        new HashMap<Integer, ArrayList<Integer>>(5000);

    public void initGraph(int vertices, int source, int sink, int edges) {
        numberOfVertices = vertices;
        this.source = source;
        this.sink = sink;
        numberOfEdges = edges;
    }

    public int getFlowEdges() {
        return flowEdges;
    }

    public void readEdge(int src, int dest, int cap) {
        capacity[src][dest] = cap;
        ArrayList<Integer> val = adjacencyList.get(src);
        if (val == null) {
            adjacencyList.put(src, new ArrayList<Integer>(50));
            val = adjacencyList.get(src);
        }
        val.add(dest);

        // FREDRIK: Added opposite edges also
        if (src != source && dest != sink) {
            ArrayList<Integer> val2 = adjacencyList.get(dest);
            if (val2 == null) {
                adjacencyList.put(dest, new ArrayList<Integer>(50));
                val2 = adjacencyList.get(dest);
            }
            val2.add(src);
        }
        //printlist();
    }
    
    public void printlist(){
        for(int i : adjacencyList.keySet()){
            ArrayList<Integer> al = adjacencyList.get(i);
            for(int j : al){
                System.out.println(i + "->" + j);
            }
        }
    }

    public MaxFlow() {
    }

    public void MaxFlowCalc() {
        while (BFS()) ; //FREDRIK:loop until we cant find a path
    }

    public ArrayList<String> getSolution() {
        ArrayList<String> res = new ArrayList<String>(numberOfEdges);
        for (int i = 0; i < flow.length; i++) {
            for (int j = 0; j < flow[i].length; j++) {

                if (i == source || j == source)
                    continue;
                if (i == sink || j == sink)
                    continue;

                if (flow[i][j] > 0) {
                    flowEdges++;
                    res.add((i - 1) + " " + (j - 1));
                }
            }
        }
        return res;
    }

    private boolean BFS() {
        if (adjacencyList.get(source) == null) {
            return false;
        }
        int[] parent = new int[numberOfVertices + 1];
        Arrays.fill(parent, -1); //FREDRIK:efficiency use 0 (we go from 1 to n)
        parent[source] = source;
        int[] m = new int[numberOfVertices + 1]; // Capacity of path to node
        m[source] = Integer.MAX_VALUE;
        // BFS begin
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            ArrayList<Integer> neighbors = adjacencyList.get(u);
            if (neighbors == null) {
                continue;
            }
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
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
