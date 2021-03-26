package algorithms;

import data.Flight;
import data.FlightList;
import data.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Astar {
    private FlightList data = null;

    public Astar(FlightList data){
        this.data = data;
    }

    public ArrayList<Node> findPath(int A, int B) throws Exception {
        ArrayList<Node> path = new ArrayList<>();
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();

        Node current_node = new Node(A,null, new ArrayList<>(data.getAllFlights(A)));
        do {
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for(int i = 0; i < current_node.getNumNextFlights(); ++i){
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if(!closed_list.contains(outgoing_flight)){
                    Node n = new Node(outgoing_flight.getOriginAirportId(), outgoing_flight, data.getNextFlights(outgoing_flight));
                    // calculate heuristics? or perhaps we should just implement a comparison that does that for us
                    open_list.add(n);
                }
            }
            current_node = open_list.poll();
            if(current_node == null){
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while(current_node.airport_id != B);
        return path;
    }
}
