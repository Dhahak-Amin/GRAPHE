package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
public class Launch {

    private static Node Origin;
    private static Node Destination;
    private static List <ArcInspector> listInspector;
    private static ArcInspector arcInspector;
    private static ShortestPathData data;
    private static BellmanFordAlgorithm bellManAlgo;
    private static DijkstraAlgorithm dijkstraAlgo;
    private static ShortestPathSolution solutionBellMan;
    private static ShortestPathSolution solutionDijkstra;
    private static Graph graphINSA = null;
    private static Graph graphWashington = null;
    private static Graph graphBelgium = null;
    private static Graph graphDence = null;
    private static Graph graphCarre = null;


    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void initAll() throws Exception {

         String mapINSA = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
         String mapBelgium = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr";
         String mapWashington = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/washington.mapgr";
         String mapCarreDense = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre_dense.mapgr" ;
         String mapCarre = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr" ;


        final GraphReader readerINSA = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapINSA))));
        final GraphReader readerBelgium = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapBelgium))));
        final GraphReader readerWashington = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapWashington))));
                final GraphReader readerDense= new BinaryGraphReader(
                    new DataInputStream(new BufferedInputStream(new FileInputStream(mapWashington))));
                    final GraphReader readerCarre = new BinaryGraphReader(
                        new DataInputStream(new BufferedInputStream(new FileInputStream(mapWashington))));

             


        graphINSA = readerINSA.read();
        System.out.println(" read insa  c bon ");

        graphBelgium = readerBelgium.read();
        System.out.println(" read belguim  c bon ");

        graphWashington = readerWashington.read();
        System.out.println(" read washington  c bon ");

        graphDence=readerDense.read();
        System.out.println(" read carredense c bon ");
        graphCarre=readerCarre.read();
        System.out.println(" read carre  c bon ");

    }

    public static void initialize(int Origin_param, int Destination_param, int Road, Graph graph) {
        // Récupérer les nœuds du graphe
      

        Origin = graph.get(Origin_param);
        Destination = graph.get(Destination_param);
        listInspector = ArcInspectorFactory.getAllFilters();
        arcInspector = listInspector.get(Road);

        data = new ShortestPathData(graph, Origin, Destination, arcInspector);
    
        dijkstraAlgo = new DijkstraAlgorithm(data);
    
        bellManAlgo = new BellmanFordAlgorithm(data);
    
        solutionDijkstra = dijkstraAlgo.run();

        solutionBellMan = bellManAlgo.run();

    }
    
    public static void main(String[] args) throws Exception {
  

        initAll();
        // Tests bon pour INSA
             //   initialize(0,9, 0, graphCarre);
      //  System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for Carre " + solutionDijkstra.getPath().getLength());
      //  initialize(0, 9, 0, graphCarre);
      //   System.out.println("Shortest path length from node 0 to node 100 with Bellman-Ford for Carre : " + solutionBellMan.getPath().getLength());

       // testShortestAllRoads("INSA", graphCarre ,0, 10, 0);
         //testShortestCarsOnly("INSA", graphCarre, 0,10, 1);

       initialize(0, 0, 0, graphINSA);
       System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for INSA: " + solutionDijkstra.getPath().getLength());
     initialize(0, 100, 0, graphINSA);
        System.out.println("Shortest path length from node 0 to node 100 with Bellman-Ford for INSA: " + solutionBellMan.getPath().getLength());

    //     testShortestAllRoads("INSA", graphINSA, 0, 150, 0);
    //      testShortestCarsOnly("INSA", graphINSA, 253, 5, 1);
    //      testFastestAllRoads("INSA", graphINSA, 0, 500, 2);
    //      testFastestCarsOnly("INSA", graphINSA, 0, 500, 2);
    //      testRoadCarsNotFound("INSA", graphINSA, 700, 0, 0);
    //  testShortestLongDistance("INSA", graphINSA, 143, 600, 0);
    //      testShortestShortDistance("INSA", graphINSA, 95, 200, 0);


       //  Test pour la Belgique
     //initialize(0, 100, 0, graphBelgium);
     //  System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for Belguim : " + solutionDijkstra.getPath().getLength());
       //initialize(10, 20, 0, graphBelgium);
        //System.out.println("Shortest path length from node 0 to node 100 with Bellman-Ford for INSA : " + solutionBellMan.getPath().getLength());

        // testShortestAllRoads("Belgium", graphBelgium, 0, 150, 0);
        // testFastestCarsOnly("Belgium", graphBelgium, 253, 5, 1);
        // testFastestAllRoads("Belgium", graphBelgium, 0, 500, 2);
        // testFastestCarsOnly("Belgium", graphBelgium, 0, 500, 2);
        // testRoadCarsNotFound("Belgium", graphBelgium, 700, 0, 0);
        // testShortestLongDistance("Belgium", graphBelgium, 143, 600, 0);
        // testShortestShortDistance("Belgium", graphBelgium, 95, 200, 0);

      //initialize(1, 9, 0, graphCarre);
      // System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for Carre: " + solutionDijkstra.getPath().getLength());
       //initialize(1, 9, 0, graphCarre);
        // System.out.println("Shortest path length from node 0 to node 100 with Bellman-Ford for Carre  : " + solutionBellMan.getPath().getLength());

      //   testShortestAllRoads("INSA", graphINSA, 0, 150, 0);
        // testShortestCarsOnly("INSA", graphINSA, 253, 5, 1);
        // testFastestAllRoads("INSA", graphINSA, 0, 500, 2);
        // testFastestCarsOnly("INSA", graphINSA, 0, 500, 2);
        // testRoadCarsNotFound("INSA", graphINSA, 700, 0, 0);
        // testShortestLongDistance("INSA", graphINSA, 143, 600, 0);
        // testShortestShortDistance("INSA", graphINSA, 95, 200, 0);


    
        
  
    }

    public static void testShortestAllRoads(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testShortestAllRoads -----------");

        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }

    public static void testShortestCarsOnly(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testShortestCarsOnly-----------");

        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }

    public static void testFastestAllRoads(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testFastestAllRoads-----------");

        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }

    public static void testFastestCarsOnly(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
      
        System.out.println("---- testFastestCarsOnly-----------");
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }

    public static void testRoadCarsNotFound(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testRoadCarsNotFound-----------");

        initialize(origin, destination, road, graph);
        if (solutionDijkstra.getPath() != null) {
            System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
            System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());

        }
        // Add the second initialization and print statements similarly...
    }

    public static void testShortestLongDistance(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testShortestLongDistance ----------");
        initialize(origin, destination, road, graph);

        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }

    public static void testShortestShortDistance(String graphName, Graph graph, int origin, int destination, int road) throws IOException {

        System.out.println("---- testShortestLongDistance -----------");
        initialize(origin, destination, road, graph);

        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Bellman-Ford: " + solutionBellMan.getPath().getLength());
    }
}
