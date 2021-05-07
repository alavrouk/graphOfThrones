import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GraphOfThrones {

    /**
     * Helper method that constructs graph based on input file.
     * This graph is:
     *  -  undirected
     *  -  does not allow self loops
     *  -  Nodes are strings
     *  -  Values of the edges are boolean to indicate a positive/negative relationship
     *  -  MutableValueGraph from google guava
     *
     * I think this is O(n^2), but the point of the problem is not to optimize this method.
     * I would rather have a functional graph that is built slowly as opposed to a potentially
     * inaccurate graph.
     *
     * @param directory this is the directory in which the relationship is located.
     * @return Returns the built graph
     * @throws IOException IOException is thrown when the input file does not exist.
     * The format for the input file is "input.txt" in pwd.
     */
    private static MutableValueGraph<String, Boolean> graphBuilder(String directory) throws IOException {
        /*
        I/O to read contents of input into an arrayList.
        Also sets the amount of nodes and edges the graph has.
         */
        URL url = GraphOfThrones.class.getResource(directory);
        File inputFile = new File(url.getPath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        ArrayList<String> inputList = new ArrayList<>();
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            inputList.add(input);
        }
        String nodesAndEdgesString = inputList.get(0);
        String[] nodesAndEdges = nodesAndEdgesString.split(" ");
        int numNodes = Integer.parseInt(nodesAndEdges[0]);
        int numEdges = Integer.parseInt(nodesAndEdges[1]);
        inputList.remove(nodesAndEdgesString);
        /*
        Reading the contents of the arrayList into the graph.
        A positive relationship edge has a boolean value of true.
        A negative relationship edge has a boolean value of false.
         */
        int numNodesAdded = 0;
        int numEdgesAdded = 0;
        MutableValueGraph<String, Boolean> graph = ValueGraphBuilder.undirected().build();
        for (int i = 0; i < inputList.size() && numNodesAdded <= numNodes && numEdgesAdded <= numEdges; i++) {
            String[] lineIfPositive = inputList.get(i).split(" \\+\\+ ");
            String[] lineIfNegative = inputList.get(i).split(" \\-\\- ");
            if (lineIfPositive.length > 1) {
                if (!(graph.nodes().contains(lineIfPositive[0]))) {
                    graph.addNode(lineIfPositive[0]);
                }
                if (!(graph.nodes().contains(lineIfPositive[1]))) {
                    graph.addNode(lineIfPositive[1]);
                }
                graph.putEdgeValue(lineIfPositive[0],lineIfPositive[1], true);
            } else {
                if (!(graph.nodes().contains(lineIfNegative[0]))) {
                    graph.addNode(lineIfNegative[0]);
                }
                if (!(graph.nodes().contains(lineIfNegative[1]))) {
                    graph.addNode(lineIfNegative[1]);
                }
                graph.putEdgeValue(lineIfNegative[0], lineIfNegative[1], false);
            }
        }
        System.out.println(graph.toString());
        return graph;
    }

    /**
     * O(n^3) solution.
     *
     * @return Returns whether the graph is balanced.
     * @throws IOException Whenever the input file doesn't exist.
     */
    public static boolean isStructurallyBalanced() throws IOException {
        MutableValueGraph<String, Boolean> graph = graphBuilder("input.txt");
        Object[] nodesList = graph.nodes().toArray();
        for (int i = 0; i < nodesList.length - 2; i++) {
            for (int j = i + 1; j <  nodesList.length - 1; j++) {
                for (int k = j + 1; k < nodesList.length; k++) {
                    boolean firstRelation = graph.edgeValue((String) nodesList[i], (String) nodesList[j]).get();
                    boolean secondRelation = graph.edgeValue((String) nodesList[i], (String) nodesList[k]).get();
                    boolean thirdRelation = graph.edgeValue((String) nodesList[j], (String) nodesList[k]).get();
                    if (!firstRelation && !secondRelation && !thirdRelation) {
                        return false;
                    }
                    if (firstRelation && secondRelation && !thirdRelation) {
                        return false;
                    }
                    if (firstRelation && !secondRelation && thirdRelation) {
                        return false;
                    }
                    if (!firstRelation && secondRelation && thirdRelation) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * O(n^2) solution.
     *
     * @return Returns whether the graph is balanced.
     * @throws IOException Whenever the input file doesn't exist.
     */
    public static boolean isStructurallyBalancedTwoElectricBoogaloo() throws IOException {
        MutableValueGraph<String, Boolean> graph = graphBuilder("input.txt");
        String firstNode = graph.nodes().iterator().next();
        ArrayList<String> team1 = new ArrayList<>();
        ArrayList<String> team2 = new ArrayList<>();
        for(String dude : graph.nodes()) {
            if (firstNode.equals(dude)) {
                //Nothing
            }
            else if (graph.edgeValue(firstNode, dude).get()) {
                team1.add(dude);
            } else {
                team2.add(dude);
            }
        }
        team1.add(firstNode);
        for (int i = 0; i < team1.size() - 1; i++) {
            for (int j = i + 1; j < team1.size(); j++) {
                if (!graph.edgeValue(team1.get(i), team1.get(j)).get()) {
                    return false;
                }
            }
        }
        for (int i = 0; i < team2.size() - 1; i++) {
            for (int j = i + 1; j < team2.size(); j++) {
                if (!graph.edgeValue(team2.get(i), team2.get(j)).get()) {
                    return false;
                }
            }
        }
        return true;
    }
}
