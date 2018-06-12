package core;

import org.gephi.graph.api.*;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Lookup;

import java.util.*;


public class Beetweness extends Centrality implements Statistics, LongTask {

    private double[] betweenness;
    private  Graph graph;

    public Beetweness() {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getGraphModel() != null) {
            isDirected = graphController.getGraphModel().isDirected();
        }
    }

    @Override
    public void execute(GraphModel graphModel) {
        Graph graph;
        if (isDirected) {
            graph = graphModel.getDirectedGraphVisible();
        } else {
            graph = graphModel.getUndirectedGraphVisible();
        }
        this.graph = graph;
        if (graphModel.getNodeTable().hasColumn(Resources.NAME_BETWEENESS)) {
            graphModel.getGraphVisible().getNodes().forEach(Element::clearAttributes);
        } else {
            graphModel.getNodeTable().addColumn(Resources.NAME_BETWEENESS, double.class);
        }
        this.betweenness = deputeDegreeCentrality(graph);
    }

    @Override
    public  double[] calculateDistance(Graph graph, HashMap<Node, Integer> indicies, boolean directed) {
        int nodes = graph.getNodeCount();
        double[] nodeBeetweenness = new double[nodes];
        Progress.start(progress, graph.getNodeCount());
        final int[] count = {0};
        final int[] totalPaths = {0};
        NodeIterable nodesIterable = graph.getNodes();
        Arrays.stream(nodesIterable.toArray()).forEach((Node s) -> {
            Stack<Node> stackNodes = new Stack<>();
            LinkedList<Node>[] predecessors = new LinkedList[nodes];
            double[] numberOfShortestPathFromV1ToV2 = new double[nodes];
            int[] path = new int[nodes];
            int s_index = indicies.get(s);
            setInitParametetrsForNode(s, predecessors, numberOfShortestPathFromV1ToV2, path, s_index, nodes);
            LinkedList<Node> neighborNodes = new LinkedList<>();
            neighborNodes.addLast(s);
            goAfterNeighbors(neighborNodes,stackNodes,indicies,graph,directed,path,numberOfShortestPathFromV1ToV2,predecessors);
            double reachable = 0;
            totalPaths[0] += reachable;
            double[] delta = new double[nodes];
            while (!stackNodes.empty()) {
                Node w = stackNodes.pop();
                int w_index = indicies.get(w);
                ListIterator<Node> iter1 = predecessors[w_index].listIterator();
                while (iter1.hasNext()) {
                    Node u = iter1.next();
                    int u_index = indicies.get(u);
                    delta[u_index] += (numberOfShortestPathFromV1ToV2[u_index] / numberOfShortestPathFromV1ToV2[w_index]) * (1 + delta[w_index]);
                }
                if (w != s) {
                    nodeBeetweenness[w_index] += delta[w_index];
                }
            }
            count[0]++;
            if (isCanceled) {
                nodesIterable.doBreak();
                return;
            }
            Progress.progress(progress, count[0]);
        });
        calculateCorrection(graph, indicies, nodeBeetweenness, directed);

        return nodeBeetweenness;
    }
    private void calculateCorrection(Graph graph, HashMap<Node, Integer> indicies,
                                     double[] nodeBetweenness, boolean directed) {

        int n = graph.getNodeCount();
        for (Node s : graph.getNodes()) {
            int s_index = indicies.get(s);
            if (!directed) {
                nodeBetweenness[s_index] /= 2;
            }
            nodeBetweenness[s_index] /= directed ? (n - 1) * (n - 2) : (n - 1) * (n - 2) / 2;

        }
    }

    @Override
    public String getReport() {
        setAttributeForNodes(graph,betweenness,Resources.NAME_BETWEENESS);
        HashMap<Integer,Double> getNodeClosness =  getNodesFromCentrality(Resources.NAME_BETWEENESS,betweenness);
        String report = generateReport(betweenness,Resources.BETWEENNESS,Resources.BETWEENNESS,getNodeClosness);
        return report;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean cancel() {
        this.isCanceled = true;
        return true;
    }

    /**
     *
     * @param progressTicket
     */
    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progress = progressTicket;
    }
}
