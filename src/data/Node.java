package data;

import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.SortedSet;

public class Node implements Comparable<Node> {
    public final Node parent;
    private Flight last_flight = null;
    public final int airport_id;
    private SortedSet<Flight> next_flights = null;
    private long heuristic;

    public Node(int airport_id, Node parent, Flight connecting_flight, SortedSet<Flight> next_flights, long heuristic){
        this.parent = parent;
        this.last_flight = connecting_flight;
        this.airport_id = airport_id;
        this.next_flights = next_flights;
        this.heuristic = heuristic;
    }

    public Flight getThisFlight(){
        return last_flight;
    }

    public Flight getNextFlight(int index){
        if(index < next_flights.size() && index > 0) {
            int i=0;
            for(Flight f: next_flights)
                if(i++ == index)
                    return f;

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
        if (!parent.equals(node.parent)) return false;
        return next_flights.equals(node.next_flights);
    }

    @Override
    public int compareTo(Node o) {
        if(this.heuristic < o.heuristic)
            return 0;
        return 1;
    }

    @Override
    public String toString() {
        if(last_flight != null) {
            return last_flight.toString() + "Heuristic: " + this.heuristic;
        }
        return Integer.toString(airport_id) + " Heuristic: " + this.heuristic;
    }

    public long calculateTimeHeuristic() {
        if(this.parent == null)
            return 0;
        else if(this.parent.last_flight == null)
            return this.last_flight.getFlightTime();
        else
            return this.parent.heuristic + ChronoUnit.MINUTES.between(this.parent.last_flight.getArrivalDateTime(),
                                                                      this.parent.last_flight.getDepartureDateTime());
    }
}
