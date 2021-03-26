package data;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private Flight me = null;
    public final int airport_id;
    private ArrayList<Flight> next_flights = null;

    public Node(int airport_id, Flight connecting_flight, ArrayList<Flight> next_flights){
        me = connecting_flight;
        this.airport_id = airport_id;
        this.next_flights = next_flights;
    }

    public Flight getThisFlight(){
        return me;
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
        if (!me.equals(node.me)) return false;
        return next_flights.equals(node.next_flights);
    }

    @Override
    public int compareTo(Node o) {
        //todo: implement heuristics
        return me.compareTo(o.me);
    }

    @Override
    public String toString() {
        if(me != null) {
            return me.toString();
        }
        return Integer.toString(airport_id);
    }
}
