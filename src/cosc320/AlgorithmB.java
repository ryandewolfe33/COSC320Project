package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;
import data.Path;

import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class AlgorithmB {
    private static TreeSet<Path> paths = new TreeSet<>();

    public AlgorithmB() {
    }

    public static Node findPath(int start, int dest, FlightList data) throws Exception {
        Node start_node = new Node(start, null, null, data.getAllFlights(start), 0, 0);
        recursive(start_node, data, dest);
        return paths.first().getTail();
    }

    private static void recursive(Node current_node, FlightList data, int dest) throws Exception {
        if (current_node == null)
            throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
        Path route = new Path(current_node);
        if (route.getLength() >= 10) {
            if (current_node.airport_id == dest) {
                paths.add(route);
            } else {
                Flight this_flight = current_node.getThisFlight();
                long time_cost = 0;
                long layover = 0;
                for (Flight next_flight : data.getNextFlights(current_node.getThisFlight())) {
                    if (this_flight != null) {
                        var edge_A_side = this_flight.getArrivalDateTime();
                        var edge_B_side = next_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side) - this_flight.getTimezoneOffset();
                    } else if (next_flight.getDestinationAirportId() == dest) {
                        continue;
                    }
                    time_cost = layover + next_flight.getFlightTime();
                    if (route.contains(next_flight.getDestinationAirportId()))
                        recursive(new Node(
                                        next_flight.getDestinationAirportId(),
                                        current_node,
                                        next_flight,
                                        data.getNextFlights(next_flight),
                                        time_cost,
                                        next_flight.getTicketPrice()),
                                data, dest);
                }
            }
        }
    }
}
