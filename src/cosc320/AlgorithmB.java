package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;
import data.Path;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.TreeSet;

public class AlgorithmB {
    public static Node findPath(int start, int dest, FlightList data) throws Exception {
        try {
            Node start_node = new Node(start, null, null, data.getAllFlights(start), 0, 0);
            TreeSet<Path> paths = new TreeSet<>();
            HashSet<Integer> history = new HashSet<>();
            recursive(paths, history, start_node, data, dest);
            if (!paths.isEmpty()) {
                return paths.first().getTail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void recursive(TreeSet<Path> paths, HashSet<Integer> history, Node current_node, FlightList data, int dest) throws Exception {
        if (current_node == null) {
            throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
        }
        if(history.size() <= 4 && !history.contains(current_node.airport_id)) {
            history.add(current_node.airport_id);
            if (current_node.airport_id == dest) {
                paths.add(new Path(current_node));
                history.remove(current_node.airport_id);
                return;
            }
            // current outgoing flight
            Flight this_flight = current_node.getThisFlight();
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight next_flight = current_node.getNextFlight(i);
                long time_cost = 0;
                long layover = 0;

                if (this_flight != null) {
                    var edge_A_side = this_flight.getArrivalDateTime();
                    var edge_B_side = next_flight.getDepartureDateTime();
                    layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side) - this_flight.getTimezoneOffset();
                } else if (next_flight.getDestinationAirportId() == dest) {
                    continue;
                }
                time_cost = layover + next_flight.getFlightTime();
                var next_next_flights = data.getNextFlights(next_flight);
                if (next_next_flights != null) {
                    Node node = new Node(next_flight.getDestinationAirportId(), current_node, next_flight, next_next_flights, time_cost, next_flight.getTicketPrice());
                    if(!history.contains(node.airport_id)) {
                        recursive(paths, history, node, data, dest);
                    }
                }
            }
            history.remove(current_node.airport_id);
        }
    }
}
