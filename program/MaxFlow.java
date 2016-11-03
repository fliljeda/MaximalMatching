import java.util.*;

public class MaxFlow {

    private int numberOfVertices;
    private int numberOfEdges;
    private int source;
    private int sink;
    private int maxflow = 0;
    private Kattio io;

    private int[][] capacity = new int[2001][2001];
    private int[][] flow = new int[2001][2001];
    private HashMap<Integer, ArrayList<Integer>> adjacencyList;

    private void createGraph() {
        numberOfVertices = io.getInt();
        source = io.getInt();
        sink = io.getInt();
        numberOfEdges = io.getInt();
        adjacencyList = new HashMap<>(5000);

        for (int i = 0; i < numberOfEdges; i++) {
            int src = io.getInt();
            int dest = io.getInt();
            int cap = io.getInt();
            capacity[src][dest] = cap;
            ArrayList<Integer> val = adjacencyList.get(src);
            if (val == null) {
                adjacencyList.put(src, new ArrayList<>(50));
                val = adjacencyList.get(src);
            }
            val.add(dest);
            
            // FREDRIK: Added opposite edges also
            if(src != source && dest != sink){
                ArrayList<Integer> val2 = adjacencyList.get(dest);
                if (val2 == null) {
                    adjacencyList.put(dest, new ArrayList<>(50));
                    val2 = adjacencyList.get(dest);
                }
                val2.add(src);
            }
        }
        //printAdjacencyList(adjacencyList);
    }

    private void printAdjacencyList(HashMap<Integer, ArrayList<Integer>> list) {
        for (int i : list.keySet()) {
            System.out.print("Key: " + i);
            ArrayList<Integer> al = list.get(i);
            System.out.println(" Values:");
            for (int val : al) {
                //System.out.print("(d: " + val + " c: " + restCapacity[i][val] + ")");
            }
            System.out.println();
        }
    }

    private MaxFlow() {
        io = new Kattio(System.in, System.out);
        createGraph();
        while(BFS()); //FREDRIK:loop until we cant find a path
        writeOutput();
    }

    private void writeOutput() {
        ArrayList<String> al = new ArrayList<String>(numberOfEdges);
        int num = 0;
        for(int i = 0; i < flow.length; i++){
            for(int j = 0; j < flow[i].length; j++){
                if(flow[i][j] > 0){
                    num++;
                    al.add(i + " " + j + " " + flow[i][j]);
                }
            }
        }
        System.out.println(numberOfVertices);
        System.out.println(source + " " + sink  + " " + maxflow);
        System.out.println(num);
        for(String s: al){
            System.out.println(s);
        }
    }

    private boolean BFS() {
        if(adjacencyList.get(source) == null){
            return false;
        }
        int[] parent = new int[numberOfVertices+1];
        Arrays.fill(parent, -1); //FREDRIK:efficiency use 0 (we go from 1 to n)
        parent[source] = source;
        int[] m = new int[numberOfVertices+1]; // Capacity of path to node
        m[source] = Integer.MAX_VALUE;
        // BFS begin
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            ArrayList<Integer> neighbors = adjacencyList.get(u);
            if(neighbors == null){
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
                        maxflow+=m[sink];
                        return true;
                    }
                }
            }
        }
        return false;
        // TODO: if there isn't any path from source to sink
    }

    public static void main(String[] args) {
        MaxFlow maxFlow = new MaxFlow();
    }
}
