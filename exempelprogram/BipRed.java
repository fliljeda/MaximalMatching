import java.util.ArrayList;

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
    private int[] xArray;
    private int[] yArray;
    private int x;
    private int y;
    private int e;
    private int sink;
    private int newEdgeCount;
    private int newSize;

    //Flow
    private int numberofedges = 0;
    ArrayList<String> printstrings;

    void readBipartiteGraph() {

        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();
        e = io.getInt();

        xArray = new int[x];
        yArray = new int[y];

        newSize = x + y + 2; // source and sink
        sink = x + y + 1;
        newEdgeCount = x + y + e;

        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            int a = io.getInt();
            int b = io.getInt();

            xArray[i] = a;
            yArray[i] = b;
        }
    }


    void writeFlowGraph() {
        // Skriv ut antal hörn och kanter samt källa och sänka
        io.println(newSize);
        io.println(1 + " " + (sink + 1));
        io.println(newEdgeCount);

        for (int i = 0; i < x; i++) {
            io.println("1 " + (xArray[i]+1) + " 1");
        }

        for (int i = 0; i < e; ++i) {
            int a = xArray[i] + 1, b = yArray[i] + 1, c = 1;
            // Kant från a till b med kapacitet c
            io.println(a + " " + b + " " + c);
        }

        for (int i = 0; i < x; i++) {
            io.println((yArray[i]+1) + " " + sink+1);
        }

        // Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
        io.flush();

        // Debugutskrift
        System.err.println("Skickade iväg flödesgrafen");
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
        printstrings = new ArrayList<String>(10000);

        for (int i = 0; i < e; ++i) {
            // Flöde f från a till b
            int x = io.getInt();
            int y = io.getInt();
            int f = io.getInt();
            if(x == 1 || y == sink+1 || f == 0) {
                continue;
            }
            numberofedges++;
            printstrings.add(x + " " + y);
        }
    }


    void writeBipMatchSolution() {

        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(numberofedges);

        for (int i = 0; i < numberofedges; ++i) {
            // Kant mellan a och b ingår i vår matchningslösning
            io.println(printstrings.get(i));
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
        System.err.println("Bipred avslutar\n");

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }

    public static void main(String args[]) {
        new BipRed();
    }
}

