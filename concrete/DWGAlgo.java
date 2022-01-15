package concrete;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import parsing.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class DWGAlgo implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph g;

    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    @Override
    public DirectedWeightedGraph copy() {
        var nodes = new ArrayList<NodeData>();
        var edges = new ArrayList<EdgeData>();

        var nodesIter = g.nodeIter();
        while (nodesIter.hasNext()) {
            var n = nodesIter.next();
            var geo =  new Coordinates(n.getLocation().x(), n.getLocation().y(), n.getLocation().z());
            var node = new Node(n.getKey(),geo, n.getWeight(), n.getInfo(), n.getTag());

            nodes.add(node);
        }

        var edgesIter = g.edgeIter();
        while (edgesIter.hasNext()) {
            var e = edgesIter.next();

            edges.add(new Edge(e.getSrc(), e.getDest(), e.getWeight(), e.getInfo(), e.getTag()));
        }

        return new DWGraph(nodes, edges);
    }

    @Override
    public boolean isConnected() {

        if (!g.nodeIter().hasNext()) {
            return  true;
        }
        var node_id = g.nodeIter().next();
        var reg_visited = new HashSet<NodeData>();
        var rev_visited = new HashSet<NodeData>();

        dfs(g, node_id, reg_visited);
        dfs(reverse(g), node_id, rev_visited);

        var nodeIter = g.nodeIter();

        while (nodeIter.hasNext()) {
            var node = nodeIter.next();

            if (!reg_visited.contains(node) || !rev_visited.contains(node)) {
                return  false;
            }
        }
        return true;
    }

    private static DirectedWeightedGraph reverse(DirectedWeightedGraph g) {
        var nodes = new ArrayList<NodeData>();
        var edges = new ArrayList<EdgeData>();

        var nodesIter = g.nodeIter();
        while (nodesIter.hasNext()) {
            var n = nodesIter.next();
            nodes.add(n);
        }

        var edgesIter = g.edgeIter();
        while (edgesIter.hasNext()) {
            var e = edgesIter.next();

            edges.add(new Edge(e.getDest(), e.getSrc(), e.getWeight(), e.getInfo(), e.getTag()));
        }

        return new DWGraph(nodes, edges);
    }

    private static void dfs(DirectedWeightedGraph g, NodeData node, HashSet<NodeData> visited) {
        visited.add(node);

        var neighbors = g.edgeIter(node.getKey());

        while (neighbors.hasNext()) {
            var neighbor = g.getNode(neighbors.next().getDest());
            if (!visited.contains(neighbor)) {
                dfs(g, neighbor, visited);
            }
        }

    }

    @Override
    public double shortestPathDist(int src, int dest) {

        var max = 1 + maxNodeID();
        var path = new int[max];
        var dist = new double[max];
        dijkstra(src, path, dist);
        return dist[dest];
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        var max = 1 + maxNodeID();
        var path = new int[max];
        var dist = new double[max];
        dijkstra(src, path, dist);

        if (dist[dest] == -1) {
            return  null;
        }

        List<NodeData> p = new ArrayList<>();
        var curr = dest;
        while (curr != src) {
            p.add(g.getNode(curr));
            curr = path[curr];
        }

        p.add(g.getNode(src));

        Collections.reverse(p);
        return p;
    }

    private void dijkstra(int src, int[] path, double[] dist) {
        var visited = new HashSet<Integer>();
        double inf = 100000000;

        var nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            var node = nodeIter.next();

            path[node.getKey()] = -1;
            dist[node.getKey()] = inf;
        }

        dist[src] = 0;
        path[src] = -1;

        var current = src;

        var sett = new HashSet<Integer>();

        while (true) {
            visited.add(current);

            var edgesIter = g.edgeIter(current);

            while (edgesIter.hasNext()) {
                var e = edgesIter.next();
                var v = e.getDest();

                if (visited.contains(v)) {
                    continue;
                }

                sett.add(v);
                double alt = dist[current] + e.getWeight();
                if (alt < dist[v])
                {
                    dist[v] = alt;
                    path[v] = current;
                }
            }

            sett.remove(current);

            if (sett.isEmpty()) {
                break;
            }

            double minDist = inf;
            int index = 0;

            for(var a : sett)
            {
                if (dist[a] < minDist)
                {
                    minDist = dist[a];
                    index = a;
                }
            }
            current = index;
        }
    }

    private int maxNodeID() {
        int max = -1;
        var nodeIter = g.nodeIter();

        while (nodeIter.hasNext()) {
            var node = nodeIter.next();

            if (max < node.getKey()) {
                max = node.getKey();
            }
        }

        return max;
    }

    @Override
    public NodeData center() {
        if (!isConnected() || !g.nodeIter().hasNext()) {
            return null;
        }

        double inf = 10000000;

        int max = 1 + maxNodeID();
        var dist = new double[max][max];

        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                dist[i][j] = inf;
            }
        }

        var edgesIter = g.edgeIter();
        while (edgesIter.hasNext()) {
            var e = edgesIter.next();
            dist[e.getSrc()][e.getDest()] = e.getWeight();
        }

        for (int i = 0; i < max; i++) {
            dist[i][i] = 0;
        }

        var t1 = g.nodeIter();
        while (t1.hasNext()) {
            var k = t1.next();
            var t2 = g.nodeIter();
            while (t2.hasNext()) {
                var i = t2.next();
                var t3 = g.nodeIter();
                while (t3.hasNext()) {
                    var j = t3.next();
                    if (dist[i.getKey()][j.getKey()] > dist[i.getKey()][k.getKey()] + dist[k.getKey()][j.getKey()]) {
                        dist[i.getKey()][j.getKey()] = dist[i.getKey()][k.getKey()] + dist[k.getKey()][j.getKey()];
                    }
                }
            }
        }

        NodeData center = null;
        double currentSum = inf;
        var nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            var node = nodeIter.next();
            double sum = 0;
            for (int i = 0; i < max; i++) {
                sum += dist[node.getKey()][i];
            }

            if (center == null || sum < currentSum) {
                center = node;
                currentSum = sum;
            }
        }

        return center;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities.size() == 0)
        {
            return cities;
        }

        return cities.stream().distinct().toList();
    }

    @Override
    public boolean save(String file) {
        return Parser.putGraph(g, file);
    }

    @Override
    public boolean load(String file) {
        var graph = Parser.getGraph(file);

        if (graph == null) {
            return false;
        }

        g = graph;
        return true;
    }
}
