package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AlgorithmA {

    public static Node findPath(int A, int B, FlightList data) throws Exception {
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();

        Node current_node = new Node(A, null, null, data.getAllFlights(A), 0, 0);
        do {
            // current outgoing flight
            Flight this_flight = current_node.getThisFlight();
            closed_list.add(this_flight);
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight next_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(next_flight)) {
                    long time_cost = 0;
                    long layover = 0;
                    if (this_flight != null) {
                        var edge_A_side = this_flight.getArrivalDateTime();
                        var edge_B_side = next_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side) - this_flight.getTimezoneOffset();
                    } else if(next_flight.getDestinationAirportId() == B){
                        continue;
                    }
                    time_cost = layover + next_flight.getFlightTime();
                    var next_next_flights = data.getNextFlights(next_flight);
                    if(next_next_flights != null) {
                        Node n = new Node(next_flight.getDestinationAirportId(), current_node, next_flight, next_next_flights, time_cost, next_flight.getTicketPrice());
                        if (!open_list.contains(n)) {
                            open_list.add(n);
                        }
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
