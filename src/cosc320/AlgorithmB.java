package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Stack;

public class AlgorithmB {

    public AlgorithmB() {
    }

    public static Node findPath(int A, int B, FlightList data) throws Exception {
        Node current_node = new Node(A, null, null, data.getAllFlights(A), 0, 0);
        Stack<Node> stack = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();//TODO: change to ensure airports are visited once per path and flights are taken once globally
        Node bestFoundNode = null;
        stack.push(current_node);
        do {
            current_node = stack.pop();
            visited.add(current_node.airport_id);
            for (Flight f : data.getAllFlights(current_node.airport_id)) {
                if (!visited.contains(f.getDestinationAirportId())) {
                    stack.push(new Node(f.getDestinationAirportId(),
                                        current_node,
                                        f,
                                        data.getAllFlights(f.getDestinationAirportId()),
                                        f.getFlightTime(),
                                        f.getTicketPrice()));
                }
            }
            if (current_node.airport_id == B) {
                if(bestFoundNode == null) {
                    bestFoundNode = current_node;
                } else {
                    if (current_node.compareTo(bestFoundNode) < 0)
                        bestFoundNode = current_node;
                }
            } else if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while (!stack.isEmpty());
        return bestFoundNode;
    }
}
