## Implementation. 
Setting upp the dataset. We have updated the pruning on the dataset to only retrive reasonable paths. The begining of the program asks for a souce and destination airports, as well as the date you would like to leave on and the date which you must arrive by. When a flightlist for each airport, it only contains flights in the window defined by the user. We have also updated the weights of the flights, and the algorithm returns the path with the lowest sum of minutes and dollars. Minutes is calculated from the minute you step on the first plane to the minute you step off the last plane at your destination. Currently, each minutes is worth 1$ but this could be updated in a real-world scenario. Now that the dataset has been pruned, we construct the graph the same as we did in milestone 3.  

The algorithm. We start our graph by creating the origin node with a cost of 0. There is a TreeSet named paths that holds every path found, in sorted order, taking O(log n) time to add each path. However, since most paths do not lead to the destination this loop is not a significant factor in the overall runtime. Starting at the first node, considering all the flights leaving this airport in the selected time period, we go through a depth first search of the entire graph. Each time we reach a new airport, a new node is created containing only the flights that depart atleast 40 minutes after the arrival time and no more than 48 hours after. The search continues through each airport, but ot through the same airport twice since a cycle can not be part of the optimal route. If we come across the destination node in our search, the current path is added to paths and we stop exploring that branch of the tree. This continues until the DFS is exhausted.

We can then take the shortest path from the Treeset, O(1) time, and return this as the shortest possible path within the confines chosen by the user.


## Results.

## Unexpected Cases/Difficulties.

THe whoe thing is on fire. IDE's were broken, some did not survive.

## Task Separation and Responsibilities. 
Ryan  
Josh  
Mike  
Liza  
