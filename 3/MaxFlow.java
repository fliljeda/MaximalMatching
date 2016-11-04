import java.util.*;

public class MaxFlow {

    private int numberOfVertices;
    private int numberOfEdges;
    private int source;
    private int sink;
    private int flowEdges;

    private HashMap<Integer, HashMap<Integer, Edge>> adjacencyList =
            new HashMap<Integer, HashMap<Integer, Edge>>(7000);

    private class Edge {
        public int id = 0;
        public int cap = 0;
        public int flow = 0;

        public Edge(int id, int cap, int flow) {
            this.id = id;
            this.cap = cap;
            this.flow = flow;
        }
    }

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
        HashMap<Integer, Edge> val = adjacencyList.get(src);
        if (val == null) {
            adjacencyList.put(src, new HashMap<Integer, Edge>(10));
            val = adjacencyList.get(src);
        }
        val.put(dest, new Edge(dest, cap, 0));

        // FREDRIK: Added opposite edges also
        if (src != source && dest != sink) {
            HashMap<Integer, Edge> val2 = adjacencyList.get(dest);
            if (val2 == null) {
                adjacencyList.put(dest, new HashMap<Integer, Edge>(10));
                val2 = adjacencyList.get(dest);
            }
            val2.put(src, new Edge(src, 0, 0));
        }
    }

    public MaxFlow() {
    }

    public void MaxFlowCalc() {
        while (BFS()) ; //FREDRIK:loop until we cant find a path
    }

    public ArrayList<String> getSolution() {
        ArrayList<String> res = new ArrayList<String>(numberOfEdges);
        for (int i : adjacencyList.keySet()) {
            for (int j : adjacencyList.get(i).keySet()) {

                if (i == source || j == source)
                    continue;
                if (i == sink || j == sink)
                    continue;

                if (getFlow(i, j) > 0) {
                    flowEdges++;
                    res.add((i - 1) + " " + (j - 1));
                }
            }
        }
        return res;
    }

    /*Gets capacity of edge*/
    public int getCap(int src, int dest) {
        return adjacencyList.get(src).get(dest).cap;

    }

    /*Gets flow of edge*/
    public int getFlow(int src, int dest) {
        return adjacencyList.get(src).get(dest).flow;
    }

    /*Adds capacity to the given edge*/
    public void addCap(int src, int dest, int add) {
        if (src == sink || dest == source) return;
        adjacencyList.get(src).get(dest).cap += add;
    }

    /*Adds flow to the given edge*/
    public void addFlow(int src, int dest, int add) {
        if (src == sink || dest == source) return;
        adjacencyList.get(src).get(dest).flow += add;
    }

    private boolean BFS() {
        if (adjacencyList.get(source) == null) {
            return false;
        }
        int[] parent = new int[numberOfVertices + 1];
        //Arrays.fill(parent, -1); //FREDRIK:efficiency use 0 (we go from 1 to n)
        parent[source] = source;
        int[] m = new int[numberOfVertices + 1]; // Capacity of path to node
        m[source] = Integer.MAX_VALUE;
        // BFS begin
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            HashMap<Integer, Edge> neighbors = adjacencyList.get(u);
            if (neighbors == null) {
                continue;
            }
            for (int v : neighbors.keySet()) {
                // There is available capacity and v haven't been visited before
                if (getCap(u, v) > getFlow(u, v) && parent[v] == 0) {
                    parent[v] = u;
                    m[v] = Math.min(m[u], getCap(u, v) - getFlow(u, v));
                    if (v != sink)
                        queue.offer(v);
                    else {
                        // Backtrack search and write flow
                        while (parent[v] != v) {
                            u = parent[v];
                            addFlow(u, v, m[sink]);
                            addFlow(v, u, -m[sink]);
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
