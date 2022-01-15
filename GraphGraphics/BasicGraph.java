package GraphGraphics;

import api.DirectedWeightedGraph;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import javax.swing.*;
import java.awt.*;

public class BasicGraph extends JApplet {
    private static final long serialVersionUID = 2202072534703043194L;

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    private JGraphXAdapter<Integer, DefaultEdge> jgxAdapter;
    private  DirectedWeightedGraph graph;
    /**
     * An alternative starting point for this demo, to also allow running this applet as an
     * application.
     *
     * @param args command line arguments
     */
    public static JApplet getGraph(DirectedWeightedGraph graph)
    {
        BasicGraph applet = new BasicGraph();
        applet.graph = graph;
        applet.init();

        return applet;
    }

    @Override
    public void init()
    {
        // create a JGraphT graph
        ListenableGraph<Integer, DefaultEdge> g =
                new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));




        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<>(g);

        //jgxAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        //jgxAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, "0");
        jgxAdapter.setCellsEditable(false);
        jgxAdapter.setCellsMovable(false);
        jgxAdapter.setEdgeLabelsMovable(false);
        jgxAdapter.setCellsDeletable(false);
        jgxAdapter.setCellsDisconnectable(false);
        jgxAdapter.setCellsResizable(false);
        jgxAdapter.setCellsBendable(false);

        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(component);
        resize(DEFAULT_SIZE);


        var nodeIter = graph.nodeIter();
        while (nodeIter.hasNext()) {
            var node = nodeIter.next();

            g.addVertex(node.getKey());
        }

        var edgeIter = graph.edgeIter();
        while (edgeIter.hasNext()) {
            var edge = edgeIter.next();
            g.addEdge(edge.getSrc(), edge.getDest());
        }




        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());
    }
}
