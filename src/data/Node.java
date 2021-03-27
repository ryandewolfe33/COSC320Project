package data;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private Flight incomingFlight = null;
    public final int airport_id;
    private ArrayList<Flight> next_flights = null;

    public Node(int airport_id, Flight connecting_flight, ArrayList<Flight> next_flights){
        incomingFlight = connecting_flight;
        this.airport_id = airport_id;
        this.next_flights = next_flights;
    }

    public Flight getThisFlight(){
        return incomingFlight;
    }

    public Flight getNextFlight(int index){
        if(index < next_flights.size() && index > 0) {
            return next_flights.get(index);
        }
        return null;
    }

    public int getNumNextFlights(){
        return next_flights.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (airport_id != node.airport_id) return false;
        if (!incomingFlight.equals(node.incomingFlight)) return false;
        return next_flights.equals(node.next_flights);
    }

    @Override
    public int compareTo(Node o) {
        //todo: implement heuristics
        return incomingFlight.compareTo(o.incomingFlight);
    }

    @Override
    public String toString() {
        if(incomingFlight != null) {
            return incomingFlight.toString();
        }
        return Integer.toString(airport_id);
    }
}
