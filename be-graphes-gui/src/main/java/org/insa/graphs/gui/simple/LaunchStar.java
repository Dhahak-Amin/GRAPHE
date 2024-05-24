package org.insa.graphs.gui.simple;

import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.PriorityQueue;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;

public class LaunchStar {

    private static List<ArcInspector> listInspector;
    private static ShortestPathData data;
    private static BellmanFordAlgorithm bellManAlgo;
    private static DijkstraAlgorithm dijkstraAlgo;
    private static AStarAlgorithm aStarAlgo;
    private static ShortestPathSolution solutionBellMan;
    private static ShortestPathSolution solutionDijkstra;
    private static ShortestPathSolution solutionAStar;

    private static Graph graphINSA = null;
    private static Graph graphToulouse = null;
    private static Graph graphParis= null;

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
        String mapParis = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/paris.mapgr";
        String mapToulouse= "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";

        final GraphReader readerINSA = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapINSA))));
        final GraphReader readerParis = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapParis))));
        final GraphReader readerToulouse = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapToulouse))));

        graphINSA = readerINSA.read();
        graphParis= readerParis.read();
        graphToulouse= readerToulouse.read();
    }

    public static void initialize(int originParam, int destinationParam, Graph graph) {
        Node origin = graph.get(originParam);
        Node destination = graph.get(destinationParam);

        listInspector = ArcInspectorFactory.getAllFilters();

        for (ArcInspector arcInspector : listInspector) {
            System.out.println("Testing with filter: " + arcInspector.toString());

            data = new ShortestPathData(graph, origin, destination, arcInspector);
            dijkstraAlgo = new DijkstraAlgorithm(data);
            bellManAlgo = new BellmanFordAlgorithm(data);
            aStarAlgo = new AStarAlgorithm(data);

            solutionBellMan = bellManAlgo.run();
            solutionDijkstra = dijkstraAlgo.run();
            solutionAStar = aStarAlgo.run();

            if (solutionDijkstra.getPath() != null && solutionBellMan.getPath() != null && solutionAStar.getPath() != null) {
                System.out.println("Shortest path length with Dijkstra: " + solutionDijkstra.getPath().getLength());
                System.out.println("Shortest path length with Bellman-Ford: " + solutionBellMan.getPath().getLength());
                System.out.println("Shortest path length with A*: " + solutionAStar.getPath().getLength());

                verifyIntermediatePoint(origin, destination, graph, arcInspector);
            } else {
                System.out.println("No valid path found for the given filter.");
            }
        }
    }

    private static void verifyIntermediatePoint(Node origin, Node destination, Graph graph, ArcInspector arcInspector) {
        int middleIndex = (origin.getId() + destination.getId()) / 2;
        Node middleNode = graph.get(middleIndex);

        ShortestPathData dataToMiddle = new ShortestPathData(graph, origin, middleNode, arcInspector);
        ShortestPathData dataFromMiddle = new ShortestPathData(graph, middleNode, destination, arcInspector);

        DijkstraAlgorithm dijkstraToMiddle = new DijkstraAlgorithm(dataToMiddle);
        DijkstraAlgorithm dijkstraFromMiddle = new DijkstraAlgorithm(dataFromMiddle);

        ShortestPathSolution solutionToMiddle = dijkstraToMiddle.run();
        ShortestPathSolution solutionFromMiddle = dijkstraFromMiddle.run();

        if (solutionToMiddle.getPath() != null && solutionFromMiddle.getPath() != null) {
            float totalLength = solutionToMiddle.getPath().getLength() + solutionFromMiddle.getPath().getLength();
            assert totalLength == solutionDijkstra.getPath().getLength() :
                    "Intermediate point check failed. Total length: " + totalLength + ", Expected: " + solutionDijkstra.getPath().getLength();
            System.out.println("Intermediate point check passed.");
        } else {
            System.out.println("No valid path found for intermediate check.");
        }
    }
// djikistra sort des valeurs croissants 

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        initAll();
        Graph[] graphs = {graphINSA, graphParis, graphToulouse};
        String[] graphNames = {"INSA", "Paris", "Toulouse"};

        for (int i = 0; i < 10; i++) { // Réduire le nombre de tests à 10
            int graphIndex = random.nextInt(graphs.length);
            Graph graph = graphs[graphIndex];
            String graphName = graphNames[graphIndex];

            int originIndex = random.nextInt(graph.size());
            int destinationIndex = random.nextInt(graph.size());

            while (originIndex == destinationIndex) {
                destinationIndex = random.nextInt(graph.size());
            }

            System.out.println("Testing on graph: " + graphName);
            initialize(originIndex, destinationIndex, graph);
        }
    }
}
