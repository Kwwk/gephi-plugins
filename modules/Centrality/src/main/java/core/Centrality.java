package core;

import org.gephi.graph.api.*;
import org.gephi.utils.progress.ProgressTicket;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Kasia on 31.05.2018.
 */
public abstract class Centrality {


    protected int nodesInGraph;
    protected boolean isDirected;
    protected ProgressTicket progress;
    protected boolean isCanceled;
    protected double[] centrality;


    protected void setInitParametetrsForNode(Node s, LinkedList<Node>[] P, double[] theta, int[] d, int index, int n) {
        for (int j = 0; j < n; j++) {
            P[j] = new LinkedList<>();
            theta[j] = 0;
            d[j] = -1;
        }
        theta[index] = 1;
        d[index] = 0;
    }
    protected EdgeIterable getEdgeIterable(Graph graph, Node v, boolean directed) {
        EdgeIterable edgeIter;
        if (directed) {
            edgeIter = ((DirectedGraph) graph).getOutEdges(v);
        } else {
            edgeIter = graph.getEdges(v);
        }
        return edgeIter;
    }

    public HashMap<Node, Integer> createNodeIndicesMap(Graph graph) {
        HashMap<Node, Integer> indices = new HashMap<>();
        int index[] = {0};
        Arrays.stream(graph.getNodes().toArray()).forEach(node -> {
            indices.put( node, index[0]);
            index[0]++;
        });
        return indices;
    }
    public double[] deputeDegreeCentrality(Graph graph) {
        isCanceled = false;
        graph.readLock();
        double [] centrility=null;
        try {
            nodesInGraph = graph.getNodeCount();
            centrility = new double[nodesInGraph];
            HashMap<Node, Integer> indices = createNodeIndicesMap(graph);
            double[] metrics = calculateDistance(graph, indices, isDirected);
            centrility = metrics;
            System.out.println();
        } finally {
            graph.readUnlock();
        }
        return centrility;
    }

    public void goAfterNeighbors(LinkedList<Node> neighborNodes, Stack<Node> stackNodes, HashMap<Node, Integer> indicies,
                                 Graph graph,boolean directed,int[] path, double[] numberOfShortestPathFromV1ToV2,
                                 LinkedList<Node>[] predecessors){
        while (!neighborNodes.isEmpty()) {
            Node v = neighborNodes.removeFirst();
            stackNodes.push(v);
            int v_index = indicies.get(v);
            EdgeIterable edgeIter = getEdgeIterable(graph, v, directed);
            Arrays.stream(edgeIter.toArray()).forEach(edge -> {
                Node reachable = graph.getOpposite(v, edge);

                int r_index = indicies.get(reachable);
                if (path[r_index] < 0) {
                    neighborNodes.addLast(reachable);
                    path[r_index] = path[v_index] + 1;
                }
                if (path[r_index] == (path[v_index] + 1)) {
                    numberOfShortestPathFromV1ToV2[r_index] = numberOfShortestPathFromV1ToV2[r_index] + numberOfShortestPathFromV1ToV2[v_index];
                    predecessors[r_index].addLast(v);
                }
            });
        }
    }
    public String getNodeWithValue(HashMap<Integer,Double> results) {
        final String[] value = new String[1];
        value[0]="";
        results.entrySet().forEach(a->{
            value[0] += a.getKey().toString();
            value[0] += " = ";
            value[0] += a.getValue();
            value[0] += "<br>";
        });

        return value[0];
    }
    public String generateReport(double[] centrality,
                                 String nameGenerate, String name, HashMap<Integer,Double> getNodeClosness){

        String report = "<html>"
                +"<"+nameGenerate+">"
                + "<br>"
                + "<h3>"+name+": </h2>"
                + "<br>"
                + "<h3> Node: </h2>"
                + "<br>"
                +"<h5>"
                +getNode(centrality)
                +"</h5>"
                + "<h3> Node: </h2>"
                + "<br>"
                +getNodeWithValue(getNodeClosness)
                +"</html>";

        return report;
    }
    public String getNode(double[] nodes){
        String value = " ";

        for(int i=0;i<nodes.length;i++){
            value += i;
            value += " = ";
            value += nodes[i];
            value += "<br>";
        }
        return value;
    }


    public HashMap<Integer,Double> getNodesFromCentrality(String value,double[]centrality){
        HashMap<Integer,Double> results = new HashMap<>();
        double valueNode[]={0};
        int node[]={0};
        IntStream.range(0, centrality.length-1).parallel().
                reduce((a,b)->centrality[a]<centrality[b]? b: a).
                ifPresent(ix -> {
                    results.put(ix,centrality[ix]) ;
                    valueNode[0] = centrality[ix];
                    node[0] = ix;

                } );

        for(int i=0; i<centrality.length;i++){
            if(valueNode[0] == centrality[i] && node[0]!=i){
                results.put(i,centrality[i]);
            }
        }


        return results;
    }


    public void setAttributeForNodes(Graph graph,double[]centrality,String name){
        int i[] = {0};
        Arrays.stream(graph.getNodes().toArray()).forEach(a->{
            a.setAttribute(name,centrality[i[0]]);
            i[0]++;
        });
    }

    public  abstract double[] calculateDistance(Graph graph, HashMap<Node, Integer> indicies, boolean directed);
}
