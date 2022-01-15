package concrete;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.*;

public class DWGraph implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges;
    private int nodesCount;
    private int edgesCount;
    private double lastChanged;
    private HashMap<Integer, Long> nodesLastChange;
    private HashSet<EdgeData> allEdges;


    public DWGraph(List<NodeData> rawNodes, List<EdgeData> rawEdges) {
        nodes = new HashMap<>();
        edges = new HashMap<>();

        for (var node : rawNodes) {
            nodes.put(node.getKey(), node);
            edges.putIfAbsent(node.getKey(), new HashMap<>());
        }

        allEdges = new HashSet<>();
        for (var e : rawEdges) {
            edges.putIfAbsent(e.getSrc(), new HashMap<>());
            edges.get(e.getSrc()).put(e.getDest(), e);

            allEdges.add(e);
        }

        nodesCount = rawNodes.size();
        edgesCount = rawEdges.size();

        var now = System.currentTimeMillis();

        lastChanged = now;
        nodesLastChange = new HashMap<>();
        for (int i = 0; i < rawNodes.size(); i++) {
            nodesLastChange.put(i, now);
        }
    }

    @Override
    public NodeData getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(NodeData n) {
        if (nodes.get(n.getKey()) == null) {
            nodesCount++;
        }

        edges.putIfAbsent(n.getKey(), new HashMap<>());

        nodes.put(n.getKey(), n);

        var now = System.currentTimeMillis();

        nodesLastChange.put(n.getKey(),  now);

        lastChanged = now;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (edges.get(src).get(dest) != null)
        {
            return;
        }
        EdgeData e = new Edge(src, dest, w, "",0 );
        edgesCount++;
        edges.get(src).put(dest, e);

        allEdges.add(e);

        var now = System.currentTimeMillis();

        nodesLastChange.put(e.getSrc(),  now);

        lastChanged = now;
    }

    @Override
    public Iterator<NodeData> nodeIter() {

        var iter = nodes.values().iterator();
        var created = System.currentTimeMillis();

        lastChanged = created;
        return new Iterator<NodeData>() {
            @Override
            public boolean hasNext() {
                return created >= lastChanged && iter.hasNext();
            }

            @Override
            public NodeData next() {
                if (created < lastChanged) {
                    throw new RuntimeException("Graph was changed during iteration..");
                }

                return iter.next();
            }
        };
    }

    @Override
    public Iterator<EdgeData> edgeIter() {

        var iter = allEdges.iterator();
        var created = System.currentTimeMillis();

        return new Iterator<EdgeData>() {
            @Override
            public boolean hasNext() {
                return created >= lastChanged && iter.hasNext();
            }

            @Override
            public EdgeData next() {
                if (created < lastChanged) {
                    throw new RuntimeException("Graph was changed during iteration..");
                }

                return iter.next();
            }
        };


    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {

        var iter = edges.get(node_id).values().iterator();
        var created = System.currentTimeMillis();

        return new Iterator<EdgeData>() {
            @Override
            public boolean hasNext() {
                return created >= lastChanged && created >= nodesLastChange.get(node_id) && iter.hasNext();
            }

            @Override
            public EdgeData next() {
                if ( created >= lastChanged && created >= nodesLastChange.get(node_id)) {
                        return iter.next();
                }
                throw new RuntimeException("Graph was changed during iteration..");
            }
        };
    }

    @Override
    public NodeData removeNode(int key) {

        NodeData n = nodes.get(key);

        if (n != null) {
            nodesCount--;
            edgesCount -= edges.get(key).size();

            nodes.remove(key);


            for (var id1 : edges.keySet()) {
                var edge = edges.get(id1).remove(key);
                if ( edge != null)
                {
                    allEdges.remove(edge);
                    edgesCount--;
                }
            }

            var dedges = edges.remove(key);
            dedges.values().forEach(e -> allEdges.remove(e));
        }

        var now = System.currentTimeMillis();

        lastChanged = now;

        return n;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        EdgeData e = edges.get(src).get(dest);

        if (e != null) {
            edgesCount--;
            edges.get(src).remove(e.getDest());
            allEdges.remove(e);
        }

        var now = System.currentTimeMillis();

        nodesLastChange.put(src, now);

        lastChanged = now;

        return e;
    }

    @Override
    public int nodeSize() {
        return nodesCount;
    }

    @Override
    public int edgeSize() {
        return edgesCount;
    }

    @Override
    public int getMC() {
        return 0;
    }
}
