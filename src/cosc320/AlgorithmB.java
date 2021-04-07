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
        HashSet<Integer> visited = new HashSet<>();
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
                            0,  //TODO: Add correct time/price values
                            0));
                }
            }
            if (current_node.airport_id == B) {
                //TODO: save path if it's cheaper than present saved path or present path is null
            } else if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while (!stack.isEmpty());
        //TODO: return the saved path
        return current_node;
    }
}
