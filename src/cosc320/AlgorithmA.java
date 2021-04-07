package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AlgorithmA {

    public AlgorithmA(){}

    public static Node findPath(int A, int B, LocalDate start, FlightList data, Boolean heuristic) throws Exception {
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();
        long heuristicValue;
        long layover = 0;

        Node current_node = new Node(A, null, null, data.getAllFlights(A, start), layover);
        do {
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(outgoing_flight)) {
                    if (current_node.getThisFlight() != null) {
                        var edge_A_side = current_node.getThisFlight().getArrivalDateTime();
                        var edge_B_side = outgoing_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side);
                    } else {
                        var edge_A_side = start.atStartOfDay();
                        var edge_B_side = outgoing_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side);
                    }
                    if (heuristic) {
                        heuristicValue = layover + outgoing_flight.getFlightTime();
                    } else {
                        heuristicValue = (long) outgoing_flight.getTicketPrice();
                    }
                    Node n = new Node(outgoing_flight.getDestinationAirportId(), current_node, outgoing_flight, data.getNextFlights(outgoing_flight), heuristicValue);
                    if (!open_list.contains(n)) {
                        open_list.add(n);
                    }
                }
            }
            current_node = open_list.poll();
            if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while (current_node.airport_id != B);
        return current_node;
    }
}
