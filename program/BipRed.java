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
    HashMap<Integer,Vertex> edgeMap;
    HashSet<Integer> xSet;
    HashSet<Integer> ySet;
    
    private class Vertex{
        private int id;
        ArrayList<Integer> edges;
        Vertex(int id){
            this.id = id;
            edges = new ArrayList<Integer>();
        }
        
        public void addEdge(int edge){
            edges.add(edge);
        }
        
        public String[] getEdges(){
            int size = edges.size();
            String[] strings = new String[size];
            for(int i = 0; i < strings.length; i++){
                strings[i] = (id+1) + " " +  (edges.get(i)+1);
            }
            return strings;
        }
        
    }

    void readBipartiteGraph() {
        edgeMap = new HashMap<Integer,Vertex>();
        //xSet = new HashSet<Integer>();
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
        //    xSet.add(a);
            ySet.add(b);
            if(edgeMap.containsKey(a)){ //if it has the vertex already
                tempv = edgeMap.get(a);
                tempv.addEdge(b);
            }else{
                actualXsize++;
                tempv = new Vertex(a);
                tempv.addEdge(b);
                edgeMap.put(a,tempv);
            }
            //if((tempv = edgeMap.get(b)) != null){ //Both ways (could be removed)
            //    tempv.addEdge(a);
            //}else{
            //    tempv = new Vertex(b);
            //    tempv.addEdge(a);
            //    edgeMap.put(b,tempv);
            //}
        }
//        for(int i: xSet){
//            String[] strings = edgeMap.get(i).getEdges();
//            for(int y = 0; y < strings.length; y++){
//                System.out.println(strings[y] + " 1");
//            }
//        }
    }


    void writeFlowGraph() {
        // Skriv ut antal hörn och kanter samt källa och sänka
        io.println(size);
        io.println(source + " " + sink);
        io.println(e + actualXsize + ySet.size()); //edges in bipartite graph plus edges to sink and source
        for(int xVertex: edgeMap.keySet()){
            xVertex++; //add one for source push
            io.println("1 "+ xVertex + " 1");
        }
        for(int i: edgeMap.keySet()){
            String[] strings = edgeMap.get(i).getEdges();
            for(int y = 0; y < strings.length; y++){
                io.println(strings[y] + " 1");
            }
        }
        for(int yVertex: ySet){
            yVertex++;
            io.println(yVertex + " " + sink + " 1");
        }

        // Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
        io.flush();

        // Debugutskrift
        //System.err.println("Skickade iväg flödesgrafen");
    }


    void readMaxFlowSolution() {
        // Läs in antal hörn, kanter, källa, sänka, och totalt flöde
        // (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
        // skickade iväg)
        int v = io.getInt();
        int s = io.getInt();
        int t = io.getInt();
        int totflow = io.getInt();
        int e = io.getInt();
        solutionPrintStrings = new ArrayList<String>();

        for (int i = 0; i < e; ++i) {
            // Flöde f från a till b
            int x = io.getInt();
            int y = io.getInt();
            if(x != s && y != t){ //source and sink edges
                finalEdges++;
                if(x < y){ //if flow is reversed
                    solutionPrintStrings.add((x-1) + " " + (y-1));
                }else{
                    solutionPrintStrings.add((y-1) + " " + (x-1));
                }
            }
            int f = io.getInt(); //just trigger te flow variable
        }
    }


    void writeBipMatchSolution() {

        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(finalEdges);

        for (String s: solutionPrintStrings) {
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

        // debugutskrift
        //System.err.println("Bipred avslutar\n");

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }

    public static void main(String args[]) {
        new BipRed();
    }
}

