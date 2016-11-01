public class MaxFlow {

    public int numberOfVertices;
    public int numberOfEdges;
    public int source;
    public int sink;
    public Kattio io;

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

        for (int i = 0; i < numberOfEdges; i++) {

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