import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BipRed {

    private Kattio io;
    private int x;
    private int y;
    private int e;
    private int source;
    private int sink;
    private int size;
    private int actualXsize = 0; //will differ from input since vertices can have no edges
    private ArrayList<String> solutionPrintStrings;
    private HashMap<Integer, Vertex> edgeMap;
    private HashSet<Integer> ySet;
    private MaxFlow maxflow = new MaxFlow();

    private class Vertex {
        ArrayList<Integer> edges;

        Vertex(int id) {
            edges = new ArrayList<Integer>();
        }

        public void addEdge(int edge) {
            edges.add(edge);
        }


        public int[] getNeighbors() {
            int size = edges.size();
            int[] ints = new int[size];
            for (int i = 0; i < ints.length; i++) {
                ints[i] = edges.get(i) + 1;
            }
            return ints;
        }

    }

    /*
       Reads input from Kattis and creates an internal data structure to represent the Graph
     */
    private void readBipartiteGraph() {
        edgeMap = new HashMap<Integer, Vertex>();
        ySet = new HashSet<Integer>();
        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();
        e = io.getInt();
        size = x + y + 2; //plus source and sink
        source = 1;
        sink = x + y + 2; //source pushes all by 1 and sink is +1

        Vertex tempv;
        int o = 0;
        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            int a = io.getInt(); //make up for inserted source
            int b = io.getInt();
            ySet.add(b);
            if (edgeMap.containsKey(a)) { //if it has the vertex already
                tempv = edgeMap.get(a);
                tempv.addEdge(b);
            } else {
                actualXsize++;
                tempv = new Vertex(a);
                tempv.addEdge(b);
                edgeMap.put(a, tempv);
            }
        }
    }

    /*
        Add Source and Sink to Bipartite graph and send to MaxFlow
     */
    private void writeFlowGraph() {
        // Skriv ut antal hörn och kanter samt källa och sänk
        maxflow.initGraph(size, source, sink, e + actualXsize + ySet.size());
        for (int xVertex : edgeMap.keySet()) {
            xVertex++; //add one for source push
            maxflow.readEdge(1, xVertex, 1);
        }
        for (int i : edgeMap.keySet()) {
            int[] neighbors = edgeMap.get(i).getNeighbors();
            for (int y = 0; y < neighbors.length; y++) {
                maxflow.readEdge(i + 1, neighbors[y], 1);
            }
        }
        for (int yVertex : ySet) {
            yVertex++;
            maxflow.readEdge(yVertex, sink, 1);
        }
        maxflow.MaxFlowCalc();
    }

    /*
        Read MaxFlow solution
     */
    private void readMaxFlowSolution() {
        solutionPrintStrings = maxflow.getSolution();
    }

    /*
        Write MaxFlow solution to Kattis
     */
    private void writeBipMatchSolution() {
        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(maxflow.getFlowEdges());

        for (String s : solutionPrintStrings) {
            // Kant mellan a och b ingår i vår matchningslösning
            io.println(s);
        }
        io.flush();

    }

    public BipRed() {
        io = new Kattio(System.in, System.out);

        readBipartiteGraph(); // read input from Kattis

        writeFlowGraph(); // add source and sink to graph and send to MaxFlow

        readMaxFlowSolution(); // read MaxFlow solution

        writeBipMatchSolution(); // write output to Kattis

        io.close();
    }

    public static void main(String args[]) {
        new BipRed();
    }
}

