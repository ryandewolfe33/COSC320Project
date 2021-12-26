## Problem Formulation

Given a list of flights of length n, some starting airport A and some destination B, the algorithm must return both the cheapest and shortest routes between the starting point and destination and their total respective values. The algorithm will treat the list of flights as a graph with n edges and m nodes, where each node represents a stop at an airport. Each node will have a unique number of edges, where each edge represents an outgoing flight from the current airport within a specified timeframe. Each edge will have values c and t which represent the cost and duration of the flight respectively.  The total value for t will have to be calculated at each node, following ttotal = tinitial(flight duration) + d(layover duration).
Pseudo-code
We will be using a best-first search algorithm.

```
ConcurrentSkipListMap AllFlights; //Gives us O(logn) look up times and is ordered (important for date ranges)
AllFlights.load(dataset);
GetUserInput(PointA,PointB,DateA,DateB); //we are passing references of variables, so we can return 4 values

ConcurrentSkipListMap FlightsInDateRange; //We again need an ordered container so that we can find all flights at a specific airport
FlightsInDateRange.load(AllFlights.get_range(DateA,DateB));
FindPath(PointA,PointB);

ConcurrentSkipListMap OpenList;
ConcurrentSkipListMap ClosedList;


GetUserInput(&pA,&pB,&dA,&dB) { //C++ syntax & -> pass by reference
	// code to ask user for input
	pA = takeUserInput();
	...
}
FindPath(A,B){
    Set current node to A
    associate all outgoing flights from A to current node
    Add current node to OpenList

    while (the destination is not the current node && there are still paths to explore) {
   	 Set current node to the cheapest node on the OpenList
   	 Add flight for current node to the ClosedList
   	 foreach(flight in currentNode){
   		 if(flight is not on ClosedList){
   			 create new node
   			 set new node to flight destination
   			 associate all outgoing flights from destination to current node
   			 set current node as parent to new node (connecting flight)
   			 calculate heuristics for new node
   			 if(new node has twin node in OpenList){
   				 if(new node is better than existing){
   					 update existing node to be new node
   				 }
   			 } else {
   				 add node to OpenList
   			 }
   		 }
   	 }
    }
    //found path logic
}
```

## Algorithm Analysis

### Proof of correctness

Let G be a graph of the airports, with edges being flights, and G’ be a subgraph of G.
OpenList is a list of all paths in G that require 1 additional  edge to be added to G’.

Loop invariant: The closed list contains the i edges required to make the i lowest cost unique paths of G using nodes in G’.

Base Case: Let G’ contain the origin airport, and choose the least cost flight from the origin to a new airport. Add the flight and the new airport to G’. Since the cheapest path was chosen, there is 1 edge in G’ making the 1 lowest cost path of G using nodes in G’.

Maintenance: For subgraph G’ with i edges, take the lowest cost path in OpenList and add the necessary edge to G’ (adding new airport N if necessary). If N was added to G’, add new paths to OpenList containing the path to N plus each edge departing from N. The cost of each new path is the cost of the path to N plus the cost of the edge used. Thus, G’ now contains i+1 edges making the i+1 unique cheapest paths using only nodes from G’, and OpenList contains all paths that can be made by adding 1 edge from G to G’ while keeping G’ connected.

Termination: When the destination is added to subgraph G’.

Proof that this is the shortest path: A proof by contradiction.
Assume that destination node N has just been added to graph G’ via path P, and that there is a shorter path from the origin to node N. By definition of the construction of G’, it contains all paths cheaper than path P. Therefore, the shorter path to N is already in G’, and thus node N was not just added. This is a contradiction. Therefore, path P must be the shortest path from the origin to node N.

### Running time analysis of findPath(A,B) 
Let n be the amount of nodes in a graph, and m the amount of edges.
The algorithm, then analyzing a current node, chooses the next node this way: it analyzes all paths available and chooses the path with lowest cost. Thus, in the worst case, B(destination) will be the last node visited by algorithm:

 
Thus, in the worst case algorithm runs n times, so algorithm’s complexity will be: O(n*k) there k is the average complexity of operations performed at each node. 
Now find k: at each node the algorithm explores all edges originating from that node, to check whether the cost of the nodes where those edges lead must be updated. In the worst-case, the complexity of that operation would be m (that means all available paths originate from the current node):

 
However, this cannot happen for every node, since branches are either distributed among nodes or, if all branches originate from one node, other nodes do not have any paths originating from them. In general, the complexity of operation at each node is m-x there x is the amount of paths which do not originate from that node and x<m. For example:

 
So, the general complexity of an algorithm is the amount of nodes multiplied by the total complexity of operations each node performs. That is C (for complexity)=O(nm) (given that x<m, we can ignore x)
Now, let’s take a look at the code:
The loop:    
while (the destination is not the current node and there are still paths to explore)  
will run, in the worst case as many times as there are nodes. That is, n times. 
The loop 
foreach(flight in currentNode) 
will run in the worst case m times. Therefore, the time complexity of the algorithm will be O(nm) as was to be shown.
Unexpected Cases/Difficulties.

We had to consider layovers and that decided that each layover can be no less than 1 hour
 and can be no more than 24 hours.
