Weighted Graphs

1: Introduction

In graph theory, the traditional weighted graph consists of weights on edges only. Whereas weighing edges has many practical applications, weighing vertices as well also serve many purposes. In this paper, we will explore properties of a doubly-weighted graph—a graph in which both edges and vertices are weighted—and how they differ from an edge-only-weighted graph. Using these properties, we will explore and solve a problem by modeling it with a doubly-weighted graph.

links :

1.https://www.youtube.com/watch?v=_ZcfwrWvo28

2.http://olizardo.bol.ucla.edu/classes/soc-111/textbook/_book/2-2-the-building-blocks-of-graphs-edges-and-nodes.html#edges

3.https://www.youtube.com/watch?v=H922Eyzg-QU

Weighted Graphs :

A weighted graph is a graph with edges labeled by numbers (called weights). In general, we only consider nonnegative edge weights. Sometimes, ∞ can also be allowed as a weight, which in optimization problems generally means we must (or may not) use that edge.
In many applications, each edge of a graph has an associated numerical value, called a weight. Usually, the edge weights are nonnegative integers. Weighted graphs may be either directed or undirected.
The weight of an edge is often referred to as the “cost” of the edge. In applications, the weight may be a measure of the length of a route, the capacity of a line, the energy required to move between locations along a route, etc.


![גרף](https://user-images.githubusercontent.com/62513189/145251938-7b756f76-8a15-41da-bf86-6a8dc948fd42.png)


Shortest Paths :

The shortest path problem is about finding a path between 2 vertices in a graph such that the total sum of the edges weights is minimum.
Given a weighted graph, and a designated node S, we would like to find a path of least total weight from S to each of the other vertices in the graph. The total weight of a path is the sum of the weights of its edges.
We have seen that performing a DFS or BFS on the graph will produce a spanning tree, but neither of those algorithms takes edge weights into account.
Suppose we have a graph G of V nodes numbered from 1 to v . In addition, we have E edges that connect these nodes. We’re given two numbers S and D that represent the source node’s indices and the destination node, respectively.
Let’s check an example for better understanding. Suppose we have the following graph and we’re given S = 1 and D = 4:


![ed](https://user-images.githubusercontent.com/62513189/145251803-c288ed90-781b-46a1-8cbe-24a7a4ef19ee.png)


To go from node S to node D we have 4 paths:

1 --> 2 --> 4, with length equal to 2.

1 --> 2 --> 3 --> 4, with length equal to 3.

1 --> 3 --> 4, with length equal to 2.

1 --> 3 --> 2 --> 4, with length equal to 3.

As we can see, the shortest path has a length equal to 2.

Also, we notice that we have two paths having a length equal to 2. Therefore, there are 2 shortest paths between node S and node D.

* In order to run this, you need to install JAVA9

Time analysis (avrage of shortest path, center):

	a. size 1000: 10ms
  
	b. size 10000: 80ms
  
	c. size 100000: 670ms
  
	d. size 1000000: 1.2 seconds
