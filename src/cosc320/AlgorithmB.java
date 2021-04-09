package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;
import data.Path;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;

public class AlgorithmB {
    private TreeSet<Path> paths = new TreeSet<>();
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
            for (Flight f : (data.getNextFlights(current_node.getThisFlight()))) {
                if (!visited.contains(f.getDestinationAirportId())) {
                    stack.push(new Node(f.getDestinationAirportId(),
                            current_node,
                            f,
                            data.getNextFlights(f),
                            f.getFlightTime(),
                            f.getTicketPrice()));
                }
            }
            if (current_node.airport_id == B) {
                if (bestFoundNode == null) {
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

    private void recursive(Node current_node, FlightList data, int B) throws Exception {
        if (new Path(current_node).getLength() >= 10) {
            if (current_node.airport_id == B) {
               paths.add(new Path(current_node));
            } else if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            } else {
                Flight this_flight = current_node.getThisFlight();
                long time_cost = 0;
                long layover = 0;
                for (Flight next_flight : (data.getNextFlights(current_node.getThisFlight()))) {
                    if (this_flight != null) {
                        var edge_A_side = this_flight.getArrivalDateTime();
                        var edge_B_side = next_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side) - this_flight.getTimezoneOffset();
                    } else if (next_flight.getDestinationAirportId() == B) {
                        continue;
                    }
                    time_cost = layover + next_flight.getFlightTime();
                    if (new Path(current_node).contains(next_flight.getDestinationAirportId()))
                        recursive(new Node(
                                        next_flight.getDestinationAirportId(),
                                        current_node,
                                        next_flight,
                                        data.getNextFlights(next_flight),
                                        time_cost,
                                        next_flight.getTicketPrice()),
                                data, B);
                }
            }
        }
    }
}
