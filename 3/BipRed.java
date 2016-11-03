import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Exempel på in- och utdatahantering för maxflödeslabben i kursen
 * ADK.
 * <p>
 * Använder Kattio.java för in- och utläsning.
 * Se http://kattis.csc.kth.se/doc/javaio
 *
 * @author: Per Austrin
 */

public class BipRed {

    private Kattio io;
    private int x;
    private int y;
    private int e;
    private int source;
    private int sink;
    private int size;
    private int finalEdges = 0; //Edges for solution
    private int actualXsize = 0; //will differ from input since vertices can have no edges
    ArrayList<String> solutionPrintStrings;
    HashMap<Integer, Vertex> edgeMap;
    HashSet<Integer> ySet;
    MaxFlow maxflow = new MaxFlow();

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
                ints[i] = edges.get(i);
            }
            return ints;
        }

    }

    void readBipartiteGraph() {
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


    void writeFlowGraph() {
        // Skriv ut antal hörn och kanter samt källa och sänk
        maxflow.initGraph(size, source, sink, e + actualXsize + ySet.size());
        for (int xVertex : edgeMap.keySet()) {
            xVertex++; //add one for source push
            maxflow.readEdge(1, xVertex, 1);
        }
        for (int i : edgeMap.keySet()) {
            int[] neighbors = edgeMap.get(i).getNeighbors();
            for (int y = 0; y < neighbors.length; y++) {
                maxflow.readEdge(i, neighbors[y], 1);
            }
        }
        for (int yVertex : ySet) {
            yVertex++;
            maxflow.readEdge(yVertex, sink, 1);
        }
        maxflow.MaxFlowCalc();
    }


    void readMaxFlowSolution() {

        solutionPrintStrings = maxflow.getSolution();
    }


    void writeBipMatchSolution() {
        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(maxflow.getFlowEdges());

        for (String s : solutionPrintStrings) {
            // Kant mellan a och b ingår i vår matchningslösning
            io.println(s);
        }
        io.flush();

    }

    BipRed() {
        io = new Kattio(System.in, System.out);

        readBipartiteGraph();

        writeFlowGraph();

        readMaxFlowSolution();

        writeBipMatchSolution();

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }

    public static void main(String args[]) {
        new BipRed();
    }
}

